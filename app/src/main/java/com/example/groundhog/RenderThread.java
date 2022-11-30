package com.example.groundhog;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import java.util.Random;

public class RenderThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final Groundhog[] groundhogs = new Groundhog[6];
    private final Renderable[] renderables = new Renderable[groundhogs.length + 1];
    private boolean quit = false;

    public RenderThread(SurfaceHolder surfaceHolder, Resources resources) {
        super(RenderThread.class.getSimpleName());

        this.surfaceHolder = surfaceHolder;

        Background background = new Background(resources);
        renderables[0] = background;
        for (int i = 0; i < groundhogs.length; i++) {
            Groundhog groundhog = new Groundhog(resources, i);
            groundhogs[i] = groundhog;
            renderables[i + 1] = groundhog;
        }
    }

    public void setSize(int width, int height) {
        for (Renderable r : renderables) {
            r.resize(width, height);
        }
    }

    @Override
    public void run() {
        Groundhog.setActive(pickRandomHog());

        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 5;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (!quit) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                integrate(ticks);
                if (ticks == 5) {
                    Groundhog.setActive(null);
                }
                delta -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                Groundhog.setActive(pickRandomHog());
                lastTimer += 1000;
                System.out.println(ticks + " ticks, " + frames + " frames");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public boolean smash(int x, int y) {
        System.out.println(x);
        System.out.println(y);
        Groundhog active = Groundhog.getActive();
        if (active != null) {
            int startX = active.getStartX();
            int endX = active.getWidth();
            int startY = active.getStartY();
            int endY = active.getHeight();
            if (x >= startX && x <= endX && y >= startY && y <= endY) {
                System.out.println("smashed");
            }
        }
        return false;
    }

    private Groundhog pickRandomHog() {
        Random random = new Random();
        int index = random.nextInt(groundhogs.length);
        return groundhogs[index];
    }

    private void integrate(int frame)
    {
        for (final Renderable renderable : renderables) {
            renderable.update(frame);
        }
    }

    private void render()
    {
        Canvas canvas = surfaceHolder.lockCanvas();
        for (Renderable renderable : renderables) {
            renderable.draw(canvas);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void quit() {
        quit = true;
        try {
            this.join();
        }
        catch(InterruptedException e){
            //
        }
    }
}
