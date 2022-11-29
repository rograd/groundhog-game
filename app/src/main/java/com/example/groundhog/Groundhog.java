package com.example.groundhog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Groundhog implements Renderable {
    private final int number;
    private Bitmap sprite;
    private static int count;
    private Bitmap current;
    private final int width;
    private final int height;
    private static final int SPRITE_COUNT = 5;
    private int screenWidth;
    private static Groundhog active;

    public Groundhog(Resources resources, int number) {
        this.number = number;
        this.sprite = BitmapFactory.decodeResource(resources, R.drawable.spritesheet);
        this.width = this.sprite.getWidth() / SPRITE_COUNT;
        this.height = this.sprite.getHeight();
        count++;
    }

    @Override
    public void resize(int width, int height) {
        this.screenWidth = width;
    }

    @Override
    public void update(int frame) {
        int startX = 0;
        if (active.equals(this)) {
            startX = (frame - 1) * this.width;
        }

        this.current = Bitmap.createBitmap(this.sprite, startX, 0, this.width, this.height);
    }

    @Override
    public void draw(Canvas c) {
        if (this.current == null)
            return;

        int pxPerHog = screenWidth / count;
        int x = pxPerHog * this.number;
        int y = Background.offset + height / 4;
        if (this.number % 2 != 0) {
            x -= width / 4;
            y = height - height / 4;
        }

        c.drawBitmap(this.current, x, y, null);
    }

    public static void setActive(Groundhog groundhog) {
        active = groundhog;
    }

    public static Groundhog getActive() {
        return active;
    }
}
