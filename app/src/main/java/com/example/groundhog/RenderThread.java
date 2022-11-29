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
    private static final int FRAMES = 4;

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
        double fps = 1f / FRAMES;
        double currentTime = SystemClock.elapsedRealtime();
        while (!quit) {
            double newTime = SystemClock.elapsedRealtime();
            double frameTime = (newTime - currentTime) / 1000.0f;
            currentTime = newTime;

            while (frameTime > 0.0) {
                double deltaTime = Math.min(frameTime, fps);
                integrate(2);
                frameTime -= deltaTime;
            }
            render();
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
