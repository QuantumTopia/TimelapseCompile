package com.example.timelapsecompile;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileNameGetter {
    private Uri uri;

    public FileNameGetter(Uri uri) {
        this.uri = uri;
    }

    private static String getPath(Uri uri) {
        return uri.getPath();
    }

    public static List<Uri> getAdjacentFileNames(Uri uri) {
        List<Uri> uris = new ArrayList<>();

        File f = new File(getPath(uri));

//        File f = new File(getPath(uri));
//        String s = f.getParent();
//        f = new File(Environment.getExternalStorageDirectory().toString(), s);
//        f.exists()

//        File f = new File(getPath(uri));
//        f = new File(f.getParent());
//        f.listFiles()
        //Arrays.stream(f.listFiles()).forEach(filename -> uris.add(filename.toUri()));

        return uris;
    }
}
