package com.example.groundhog.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Common {
    public static <T extends View> T createCopyOf(int resource, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource, null);
        return (T)view;
    }
}
