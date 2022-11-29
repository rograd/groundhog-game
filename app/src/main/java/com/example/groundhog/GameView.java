package com.example.groundhog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, GameController {
    private final SurfaceHolder surfaceHolder;
    private RenderThread renderThread;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        renderThread = new RenderThread(surfaceHolder, getResources());
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

    @Override
    public void onStart() {
        renderThread.start();
    }
}
