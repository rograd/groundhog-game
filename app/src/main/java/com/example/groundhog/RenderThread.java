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
    private GameCallback callback;
    private int score;
    private int tries;

    public RenderThread(SurfaceHolder surfaceHolder, Resources resources, GameCallback callback) {
        super(RenderThread.class.getSimpleName());

        this.surfaceHolder = surfaceHolder;
        this.callback = callback;

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

    public interface GameCallback {
        void onScore(int score);
        void onOver();
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

        while (!quit && tries < 20) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                ticks++;
                integrate(ticks);
                if (ticks == 5) {
                    Groundhog.setActive(null);
                    tries++;
                }

                delta -= 1;
            }

            try {
                sleep(2);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            frames++;
            render();

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                Groundhog.setActive(pickRandomHog());
                lastTimer += 1000;
                System.out.println(ticks + " ticks, " + frames + " frames");
                frames = 0;
                ticks = 0;
            }
        }
        callback.onOver();
    }

    public void smash(int x, int y) {
        Groundhog active = Groundhog.getActive();
        if (active != null) {
            int startX = active.getStartX();
            int endX = startX + active.getWidth();
            int startY = active.getStartY();
            int endY = startY + active.getHeight();
            if (x >= startX && x <= endX && y >= startY && y <= endY) {
                score++;
                callback.onScore(score);
            }
        }
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
