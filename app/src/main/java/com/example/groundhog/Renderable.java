package com.example.groundhog;

import android.graphics.Canvas;
import android.graphics.RectF;

public interface Renderable {
    void resize(int width, int height);
    void update(int frame);
    void draw(Canvas c);
}
