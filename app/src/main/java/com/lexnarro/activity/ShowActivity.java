package com.lexnarro.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.lexnarro.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ShowActivity extends AppCompatActivity {

    ImageView imageView;
    PDFView pdfView;

    boolean isImageFitToScreen;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Toolbar toolbar = findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageView = findViewById(R.id.imageView);
        pdfView=findViewById(R.id.pdf_view);
        String path=getIntent().getStringExtra("path");
        final String url=getIntent().getStringExtra("url");
        boolean pdf=getIntent().getBooleanExtra("type",false);
        boolean network=getIntent().getBooleanExtra("network",false);
        String  title=getIntent().getStringExtra("content");
        toolbar.setTitle(title);


        if (pdf && !network){
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromFile(new File(path))
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        } else if (pdf && network){
            pdfView.setVisibility(View.VISIBLE);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        //Your code goes here
                        URL pdfUrl = new URL(url);
                        URLConnection connection = pdfUrl.openConnection();
                        connection.connect();

                        // download the file
                        final InputStream input = new BufferedInputStream(connection.getInputStream());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                pdfView.fromStream(input)
                                        .defaultPage(0)
                                        .enableSwipe(true)
                                        .swipeHorizontal(false)
                                        .enableAnnotationRendering(true)
                                        .scrollHandle(new DefaultScrollHandle(ShowActivity.this))
                                        .load();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }else if (!pdf && !network){
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(new File(path)).into(imageView);
        }else if (!pdf && network){
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(url).into(imageView);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}