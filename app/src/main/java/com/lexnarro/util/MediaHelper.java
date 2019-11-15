package com.lexnarro.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * Created by mobulous1 on 13/9/17.
 */

public class MediaHelper {
    private static MediaHelper mediaHelper;


    private MediaHelper() {
    }

    public static MediaHelper getMediaHelper() {
        if (mediaHelper == null) {
            mediaHelper = new MediaHelper();
            return mediaHelper;
        } else {
            return mediaHelper;
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     */
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if(id != null) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (Exception e) {
                        return null;
                    }
                }

//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    public static File copyBitmapToFile(File src, String rootPath, String name) throws IOException {
        //create a file to write bitmap data
        File f = new File(rootPath, name);
        boolean create = f.createNewFile();

//Convert bitmap to byte array
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(src.getAbsolutePath()), 640, 480, true);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitMapData = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitMapData);
        fos.flush();
        fos.close();
        return f;

    }

    public static File copyScaledBitmapToFile(File src, String rootPath, String name, int width, int height) throws IOException {
        //create a file to write bitmap data
        File f = new File(rootPath, name);
        boolean create = f.createNewFile();

//Convert bitmap to byte array
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(src.getAbsolutePath()), width, height, true);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitMapData = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitMapData);
        fos.flush();
        fos.close();
        return f;

    }

    public static File copyScaledBitmapToFile(Bitmap src, String rootPath, String name, int width, int height) throws IOException {
        //create a file to write bitmap data
        File f = new File(rootPath, name);
        boolean create = f.createNewFile();

//Convert bitmap to byte array
        Bitmap bitmap = Bitmap.createScaledBitmap(src, width, height, true);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitMapData = bos.toByteArray();
//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitMapData);
        fos.flush();
        fos.close();
        return f;

    }


    public String encodeFileToBase64Binary(String path) {

        String encodedString = null;

        if (isImageFile(path)) {
            encodedString = Base64.encodeToString(loadImage(path), Base64.DEFAULT);
        } else if (isPDF(path)) {
            try {
                encodedString = Base64.encodeToString(loadFile(path), Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return encodedString;
    }

    public boolean isFileInSize(Context context, String path, int size) {


        return getFileSize(context, path) != 0 && size >= getFileSize(context, path);

    }

    public int getFileSize(Context context, String path) {

            String size = Formatter.formatShortFileSize(context, new File(path).length());
            File file = new File(path);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            Log.e("Size", size + " " + file_size);
            return file_size;
    }

    public boolean isImageFile(String path) {
        if (path!=null) {
            String mimeType = URLConnection.guessContentTypeFromName(path);
            return mimeType != null && mimeType.startsWith("image");
        }
        return false;
    }

    private boolean isPDF(String path) {
        if (path!=null) {
            String filenameArray[] = path.split("\\.");
            String extension = filenameArray[filenameArray.length - 1];

            return extension.equalsIgnoreCase("pdf");
        }
        return false;
    }

    public boolean isFile(String path) {

        return isImageFile(path) || isPDF(path);
    }

    private static byte[] loadImage(String path) {
        Bitmap bMap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bMap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        //bMap is the bitmap object
        byte[] b = baos.toByteArray();
        int i = b.length;
        Log.e("the output is", "" + i);
        return b;
    }

    public String getFileName(String path) {
        if (path!=null) {
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return "";
    }

    private static byte[] loadFile(String path) throws IOException {

        File file = new File(path);
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

}
