package com.example.timelapsecompile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;

public class MainActivity extends AppCompatActivity {

    // TODO check that the needed permissions exist and display warning if they dont
    // TODO intent codes
    // TODO only request perms if necessary

    public static final int PERMISSION_EXTERNAL_STORAGE = 1;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        //testShit();
        loadFFMPEG();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                createVideo();
            }
        });

    }

    private void loadFFMPEG() {
        if (FFmpeg.getInstance(this).isSupported()) {
            // ffmpeg is supported
        } else {
            // ffmpeg is not supported
        }
    }

    private void createVideo() {
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        // to execute "ffmpeg -version" command you just need to pass "-version"
        File f = Environment.getExternalStorageDirectory();
        f = new File(f, "Test");
        String imagePath = f.getAbsolutePath() + "/Test/A";

        String[] cmd = {
                "-framerate", "3",
                "-start_number", "7305090",
               // "-f ", "image2 ",
               // "-s ", "1920x1080 ",
                "-i", "" + imagePath + "%d.JPG",
               // "-vcodec ", "libx264 ",
               // "-crf ", "25 ",
                "-pix_fmt", "yuv420p",
                "" + f.getAbsolutePath() + "/Test/test.mp4" };

//        String[] cmd = { "-r 60 -f image2 -s 1920x1080 -i " + imagePath + "%d.JPG -vcodec libx264 -crf 25 -pix_fmt yuv420p " + f.getAbsolutePath() + "/test.mp4 " };

//        String[] cmd = {
//                "-framerate 25",
//                "-pattern_type sequence",
//                "-start_number 7305088",
//                "-r 3",
//                "-i A%d.jpg",
//                "-s 720x480 test.avi" };

//        String[] cmd = { "-help" };

        ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

            @Override
            public void onStart() {
                testShit();
            }

            @Override
            public void onProgress(String message) {}

            @Override
            public void onFailure(String message) {
                testShit();
            }

            @Override
            public void onSuccess(String message) {
                testShit();
            }

            @Override
            public void onFinish() {
                testShit();
            }

        });
    }

    private void testShit() {
        Environment.getExternalStorageDirectory();
//        EncodeAndMuxTest e = new EncodeAndMuxTest(Environment.getExternalStorageDirectory());
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                e.testEncodeVideoToMp4();
//            }
//        });
    }
/*
    private void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private void checkAllFileAccess() {
//        if (!Environment.isExternalStorageManager()) {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), 0);
        //startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));

//        }
    }

    private void displayImg() {
        BitmapProvider b = new BitmapProvider(getApplicationContext());
        ImageView iv = (ImageView) findViewById(R.id.image);
        iv.setImageBitmap(b.getBitmap());
    }
*/
    public void selectImage(View view) {

    }
/*
    public void selectImages(View view) {
        File dir = Environment.getExternalStorageDirectory();
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

 */
    public static Context getAppContext() { return context; }

}