package com.lexnarro.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import com.lexnarro.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TakeImage extends Activity {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 4;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static String path = "";
    private final int CAMERA_PIC_REQUEST = 1888, REQ_CODE_PICK_IMAGE = 1;
    private final String capture_dir = Environment
            .getExternalStorageDirectory() + "/lexnarro/";
    Activity activity;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    Uri imageFileUri;


    public TakeImage() {

    }

    public TakeImage(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String image_from = b.getString("from");
            if (image_from != null && image_from.equalsIgnoreCase("camera")) {
                File file = new File(capture_dir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                path = capture_dir + System.currentTimeMillis() + ".jpg";
//                imageFileUri = Uri.fromFile(new File(path));
                imageFileUri = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        new File(path));
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    Intent cameraIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            imageFileUri);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }
            } else if (image_from != null && image_from.equalsIgnoreCase("gallery")) {

                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                }
            }

            else if (image_from != null && image_from.equalsIgnoreCase("storage")) {

                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
//                    Intent intent = new Intent(
//                            Intent.ACTION_GET_CONTENT,
//                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    intent.setType("application/pdf");
//                    intent.setType("application/xml");
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                    browseDocuments();
                }
            }
        }

    }

    private void browseDocuments(){

//        String[] mimeTypes =
//                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
//                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
//                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
//                        "text/plain",
//                        "application/pdf",
//                        "application/zip"};

        String[] mimeTypes =
                {"image/*","application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
            intent.setType("image/*|application/pdf");
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), REQ_CODE_PICK_IMAGE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalCameraStorage();
                    }

                } else {

                    onBackPressed();
                }
                return;
            }
            case CAMERA_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent cameraIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            imageFileUri);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

                } else {

                    onBackPressed();
                }
                return;
            }
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, REQ_CODE_PICK_IMAGE);

                } else {

                    onBackPressed();
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MediaHelper helper= MediaHelper.getMediaHelper();
        if (resultCode == RESULT_OK) {

            if (requestCode == REQ_CODE_PICK_IMAGE ) {

                path= helper.getPath(this,data.getData());

                if (path != null&& helper.isFileInSize(this,path,1024)) {
                    if (helper.isImageFile(path)) {
                        setImageCrop(path, 0);
                    }
                    returnIntent(RESULT_OK,helper);
                }else {
                    returnIntent(RESULT_CANCELED,helper);
                }
            }
            if (requestCode == CAMERA_PIC_REQUEST) {
                try {
                    setImageCrop(path, 1);
                    if (path != null&& helper.isFileInSize(this,path,1024)) {
                        returnIntent(RESULT_OK,helper);
                    }else {
                        returnIntent(RESULT_CANCELED,helper);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    path = "";
                    returnIntent(RESULT_CANCELED,helper);
                }
            }

        } else {
            path = "";
            returnIntent(RESULT_CANCELED,helper);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void returnIntent(int result,MediaHelper helper ){
        Intent intent = new Intent();
//        intent.putExtra("FileName", helper.getFileName(path));
        intent.putExtra("FilePath", path);
//        intent.putExtra("Base64String", helper.encodeFileToBase64Binary(path));
        setResult(result, intent);
        finish();
    }

    private void setImageCrop(final String path, int i) {
        try {
            final Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            final int sample = Math.min(options.outWidth / 500,
                    options.outHeight / 250);
            options.inSampleSize = sample;
            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[16 * 1024];
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            int orientation = 0;
            try {
                final ExifInterface exif = new ExifInterface(path);
                final String exifOrientation = exif
                        .getAttribute(ExifInterface.TAG_ORIENTATION);
                orientation = Integer.valueOf(exifOrientation);
            } catch (final Exception e) {
                e.printStackTrace();
            }
            final Matrix mat = new Matrix();
            if (orientation == 1) {
                mat.postRotate(0);
            } else if (orientation == 6) {
                mat.postRotate(90);
            } else if (orientation == 8) {
                mat.postRotate(270);
            } else if (orientation == 3) {
                mat.postRotate(180);
            }

            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                    mat, true);
            savebitmap(bm, path);
            bm.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    private void savebitmap(final Bitmap img, final String imagePath) {
        try {
            final File f = new File(imagePath);
            if (f.isFile()) {
                f.delete();
            }
            img.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(
                    new File(imagePath)));
            final File f1 = new File(imagePath);
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    public class MarshMallowPermission {

        Activity activity;

        public MarshMallowPermission(Activity activity) {
            this.activity = activity;
        }

        public boolean checkPermissionForRecord() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public boolean checkPermissionForExternalStorage() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public boolean checkPermissionForCamera() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public void requestPermissionForRecord() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
            }
        }

        public boolean requestPermissionForExternalStorage() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
            return true;
        }

        public boolean requestPermissionForExternalCameraStorage() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
            return true;
        }

        public boolean requestPermissionForCamera() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
            return true;
        }
    }
}