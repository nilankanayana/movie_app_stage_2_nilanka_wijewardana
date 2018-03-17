package com.inusak.android.moviesapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides functionalities to handle all image data conversions. Currently not being used.
 * Created by Nilanka on 3/11/2018.
 */

public class ImageUtils {

    /**
     * This method can be used to retrieve byte[] of data from bitmap image
     *
     * @param bitmap {@link Bitmap}
     * @return byte[]
     */
    public static byte[] getImageBytes(Bitmap bitmap) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    /**
     * This method can be used to retrieve a bitmap object from image data byte[]
     *
     * @param image byte[]
     * @return Bitmap
     */
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * This method can be used to retrieve byte[] of data from an input stream
     *
     * @param inputStream {@link InputStream}
     * @return bye[]
     * @throws IOException
     */
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

}
