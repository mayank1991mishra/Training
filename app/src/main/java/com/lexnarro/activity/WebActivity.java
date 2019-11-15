package com.lexnarro.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import com.lexnarro.R;
import com.lexnarro.util.CenteredToolbar;

import java.io.File;

public class WebActivity extends AppCompatActivity {

    private String url;
    private DownloadManager.Request request;
    private String fileName;
    private  static int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE=555;
    private WebView mWebView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = findViewById(R.id.web_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        final boolean hasPermission = checkPermissionForExternalStorage();
        url = getIntent().getStringExtra("url");
        String  title=getIntent().getStringExtra("content");
        toolbar.setTitle(title);
        FloatingActionButton fab=  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfDownload(hasPermission);
            }
        });
       final ProgressDialog pd = new ProgressDialog(WebActivity.this);
        pd.setMessage("loading");
        pd.show();
        if(url.contains(".PDF") || url.contains(".pdf")){
            fab.show();
//            mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
        }else if(url.contains(".jpg")
                || url.contains(".JPG")
                || url.contains(".JPEG")
                || url.contains(".PNG")
                || url.contains(".png")
                || url.contains(".jpeg")){
            fab.show();
//            mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
//            mWebView.loadUrl(url);

            Display display = getWindowManager().getDefaultDisplay();
            int width=display.getWidth();

            String data="<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width="+width+", initial-scale=0.65 \" /></head>";
            data=data+"<body><center><img width=\""+width+"\" src=\""+url+"\" /></center></body></html>";
            mWebView.loadData(data, "text/html", null);
        }
        else {
            mWebView.loadUrl(getIntent().getStringExtra("url"));
        }

        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                pd.dismiss();
            }
        });





    }


    private void pdfDownload(boolean hasPermission){
        request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Lexnarro App");

// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        request.setTitle(fileName);
        String fname = "/LEXNARRO/" + fileName;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + fname);
        final Uri uri = Uri.fromFile(file);

        if (!file.exists()) {

            if (hasPermission) {
                downloadPDF();
            } else {
                requestPermissionForExternalStorage();
            }
        }
    }
    public void downloadPDF() {
        fileName=fileName.replace("%20"," ");
        request.setDestinationInExternalPublicDir("/LEXNARRO/", fileName);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Toast.makeText(this, "Your file has been downloaded to folder LEXNARRO", Toast.LENGTH_LONG).show();
        manager.enqueue(request);
    }

    public boolean requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                listener.ongotoSetting();
            Toast.makeText(this, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 121);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Permission to access the Storage and this is required for this app to Download the file & save in LEXNARRO Folder")
                    .setTitle("Permission required");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    ActivityCompat.requestPermissions(WebActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 555:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        downloadPDF();
                } else {

                        requestPermissionForExternalStorage();
                }

                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121) {
            if (checkPermissionForExternalStorage()) {
                downloadPDF();
            }


        }
    }

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(WebActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

}
