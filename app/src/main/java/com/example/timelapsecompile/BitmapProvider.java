package com.example.timelapsecompile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;

public class BitmapProvider {
    private Context context;
    private File image = new File("/sdcard/Images/i1.png");

    BitmapProvider(Context context) {
        this.context = context;
    }

    Bitmap getBitmap() {
        return BitmapFactory.decodeFile(image.getAbsolutePath());
    }
}
