package com.example.groundhog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder surfaceHolder;
    private RenderThread renderThread;
    private RenderThread.GameCallback callback;

    public GameView(Context context, RenderThread.GameCallback callback) {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.callback = callback;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        renderThread = new RenderThread(surfaceHolder, getResources(), callback);
        renderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        renderThread.setSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        renderThread.quit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        renderThread.smash((int)event.getX(), (int)event.getY());
        return super.onTouchEvent(event);
    }
}
