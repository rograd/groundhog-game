package com.example.groundhog;

import android.graphics.Canvas;
import android.graphics.RectF;

public interface Renderable {
    /**
     * This method is called when user interface gets resized.
     * @param width screen width
     * @param height screen height
     */
    void resize(int width, int height);

    /**
     * Used to update properties of a renderable based on current frame.
     * @param frame frame number
     */
    void update(int frame);

    /**
     * Drawing method. Allows to use canvas in order to paint a renderable.
     * @param c canvas
     */
    void draw(Canvas c);
}
