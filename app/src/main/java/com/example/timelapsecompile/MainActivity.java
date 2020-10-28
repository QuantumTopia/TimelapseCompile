package com.example.timelapsecompile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPremission();
    }

    void checkPremission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_EXTERNAL_STORAGE);
    }

    private void displayImg() {
        BitmapProvider b = new BitmapProvider(getApplicationContext());
        ImageView iv = (ImageView) findViewById(R.id.image);
        iv.setImageBitmap(b.getBitmap());
    }

    public void selectImage(View view) {
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent,"Select Picture:"),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            List<Uri> uris = new ArrayList<>();
            TextView tv = (TextView) findViewById(R.id.image_view);
            if (resultData.getClipData() != null) {
                ClipData clipData = resultData.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uris.add(clipData.getItemAt(i).getUri());
                }
            }
            else if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
                tv.setText(uri.toString());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageView im = (ImageView) findViewById(R.id.image);
                im.setImageBitmap(bitmap);
                FileNameGetter.getAdjacentFileNames(uri);
            }
            //https://stackoverflow.com/questions/17096726/how-to-encode-bitmaps-into-a-video-using-mediacodec
            //https://bigflake.com/mediacodec/
            String s = new String("");
            for (Uri ur : uris) {
                s = s.concat(uris.toString() + "\n");
            }
            tv.setText(s);
        }
    }
}