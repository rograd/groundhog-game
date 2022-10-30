package com.example.groundhog.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class GameView extends View {

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.YELLOW);
        Paint p = new Paint();
        canvas.drawCircle(10, 10, 30, p);
    }
}
