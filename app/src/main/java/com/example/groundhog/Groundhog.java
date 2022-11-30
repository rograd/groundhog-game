package com.example.groundhog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Groundhog implements Renderable {
    private final Bitmap sprite;
    private static int count;
    private Bitmap current;
    private static final int SPRITE_COUNT = 5;
    private final int number;
    private static Groundhog active;
    private int startX;

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private int startY;
    private final int height;
    private final int width;

    public Groundhog(Resources resources, int number) {
        sprite = BitmapFactory.decodeResource(resources, R.drawable.spritesheet);
        width = sprite.getWidth() / SPRITE_COUNT;
        height = sprite.getHeight();
        this.number = number;
        this.startY = Background.offset + height / 4;

        count++;
    }

    @Override
    public void resize(int width, int height) {
        int spacePerHog = width / count;
        this.startX = this.number * spacePerHog;

        if (this.number % 2 != 0) {
            this.startX -= width / 8;
            this.startY = height - this.height;
        }
    }

    @Override
    public void update(int frame) {
        int startX = 0;
        if (active.equals(this)) {
            startX = (frame - 1) * this.width;
        }

        this.current = Bitmap.createBitmap(this.sprite, startX, 0, width, height);
    }

    @Override
    public void draw(Canvas c) {
        if (this.current != null) {
            c.drawBitmap(this.current, this.startX, this.startY, null);
        }
    }

    public static void setActive(Groundhog groundhog) {
        active = groundhog;
    }

    public static Groundhog getActive() {
        return active;
    }
}
