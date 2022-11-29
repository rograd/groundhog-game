package com.example.groundhog;

import android.content.res.Resources;
import android.graphics.*;

public class Background implements Renderable {
    private static final float INITIAL_OFFSET = 115f;
    private Bitmap image;
    private final float offsetRatio;
    public static int offset;

    public Background(Resources resources) {
        this.image = BitmapFactory.decodeResource(resources, R.drawable.background);
        this.offsetRatio = INITIAL_OFFSET / image.getHeight();
    }

    @Override
    public void resize(int width, int height) {
        offset = (int)(height * offsetRatio);
        this.image = Bitmap.createScaledBitmap(this.image, width, height, false);
    }

    @Override
    public void update(int frame) {
    }

    @Override
    public void draw(Canvas c) {
        c.drawBitmap(image, 0, 0, null);
    }
}
