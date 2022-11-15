package com.example.groundhog.view;

import android.content.Context;
import android.graphics.*;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.*;

import com.example.groundhog.R;
import com.example.groundhog.controller.GameController;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, GameController {
    private SurfaceHolder surfaceHolder;
    private RenderThread mThread;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mThread = new RenderThread(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mThread.setSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mThread.quit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onStart() {
        mThread.start();
    }

    private class RenderThread extends Thread {
        private SurfaceHolder mHolder;
        private boolean mQuit;
        private int mWidth;
        private int mHeight;
        private Renderable[] mRenderables = new Renderable[4];

        public RenderThread(SurfaceHolder holder) {
            super(RenderThread.class.getSimpleName());
            mHolder = holder;

            mRenderables[0] = new Background();
            mRenderables[1] = new Ball(1500);
            mRenderables[2] = new Groundhog(500, 500);
            mRenderables[3] = new Groundhog(100, 200);
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
            for (Renderable renderable : mRenderables) {
                renderable.setSize(width, height);
            }
        }

        @Override
        public void run() {
            mQuit = false;
            Rect dirty = new Rect();
            RectF dirtyF = new RectF();
            double dt = 1 / 60.0; // upper-bound 60fps
            double currentTime = SystemClock.elapsedRealtime();
            while (!mQuit) {
                double newTime = SystemClock.elapsedRealtime();
                double frameTime = (newTime - currentTime) / 1000.0f;
                currentTime = newTime;
                dirtyF.setEmpty();
                while (frameTime > 0.0) {
                    double deltaTime = Math.min(frameTime, dt);
                    integrate(dirtyF, 1.0f * deltaTime);
                    frameTime -= deltaTime;
                }

                dirty.set(
                        (int) dirtyF.left,
                        (int) dirtyF.top,
                        (int) Math.round(dirtyF.right),
                        (int) Math.round(dirtyF.bottom));

                render(dirty);
            }
        }

        private void integrate(RectF dirty, double timeDelta) {
            for (int i = 0; i < mRenderables.length; i++) {
                final Renderable renderable = mRenderables[i];
                renderable.unionRect(dirty);
                renderable.update(dirty, timeDelta);
                renderable.unionRect(dirty);
            }
        }

        private void render(Rect dirty) {
            Canvas c = mHolder.lockCanvas(dirty);
            if (c != null) {
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                for (Renderable renderable : mRenderables) {
                    renderable.draw(c);
                }
                mHolder.unlockCanvasAndPost(c);
            }
        }

        public void quit() {
            mQuit = true;
            try {
                mThread.join();
            } catch (InterruptedException e) {
                //
            }
        }


        private class Background extends Renderable {
            private final Bitmap mBp;

            public Background() {
                DisplayMetrics metrics = new DisplayMetrics();
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                display.getRealMetrics(metrics);
                Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.background);
                mBp = Bitmap.createScaledBitmap(source, metrics.widthPixels, metrics.heightPixels, true);
            }

            @Override
            public void setSize(int width, int height) {

            }

            @Override
            public void update(RectF dirty, double timeDelta) {

            }

            @Override
            public void draw(Canvas c) {
                c.drawBitmap(mBp, 0, 0, null);
            }
        }

        private class Groundhog extends Renderable {
            private Bitmap sprite;
            private Bitmap xd;
            private int mWidth;
            private int mHeight;

            public Groundhog(int width, int height) {
                this.mWidth = width;
                this.mHeight = height;
                sprite = BitmapFactory.decodeResource(getResources(), R.drawable.spritesheet);
                this.xd = sprite;
            }

            @Override
            public void setSize(int width, int height) {

            }

            @Override
            public void update(RectF dirty, double timeDelta) {

            }

            @Override
            public void draw(Canvas c) {
                c.drawBitmap(xd, mWidth, mHeight, null);
            }
        }

        private class Ball extends Renderable {
            private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            private float mXVelDir, mYVelDir; // pixels-per-second
            private float mSize;


            public Ball(int speed) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.RED);
                mXVelDir = speed;
                mYVelDir = speed;
            }

            @Override
            public void setSize(int width, int height) {
                mSize = width / 15.0f;
            }

            @Override
            public void update(RectF dirty, double timeDelta) {
                mRect.left += (mXVelDir * timeDelta);
                mRect.top += (mYVelDir * timeDelta);
                mRect.right = mRect.left + mSize;
                mRect.bottom = mRect.top + mSize;
                if (mRect.left <= 0) {
                    mRect.offset(-mRect.left, 0);
                    mXVelDir = -mXVelDir;
                } else if (mRect.right >= mWidth) {
                    mRect.offset(mWidth - mRect.right, 0);
                    mXVelDir = -mXVelDir;
                }
                if (mRect.top <= 0) {
                    mRect.offset(0, -mRect.top);
                    mYVelDir = -mYVelDir;
                } else if (mRect.bottom >= mHeight) {
                    mRect.offset(0, mHeight - mRect.bottom);
                    mYVelDir = -mYVelDir;
                }
            }

            @Override
            public void draw(Canvas c) {
                c.drawCircle(mRect.centerX(), mRect.centerY(),
                        mRect.width() / 2.0f, mPaint);
            }
        }

        private abstract class Renderable {
            protected final RectF mRect = new RectF();

            public abstract void setSize(int width, int height);

            public abstract void update(RectF dirty, double timeDelta);

            public abstract void draw(Canvas c);

            public final void unionRect(RectF dirty) {
                dirty.union(mRect);
            }
        }
    }
}
