package com.lexnarro.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lexnarro.R;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;

public class SplashActivity extends AppCompatActivity {

    private String IMEI;
    private int permissionCheck;
    private int REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
            TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String Imei = mngr.getDeviceId();
            Log.d(IMEI,Imei);
            SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SPreferenceKey.IMEI,Imei);
            if(SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.LOGGEDIN)){
                gotoNextActivity(HomeActivity.class);
            }else{
                gotoNextActivity(LoginActivity.class);
            }
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {


                    TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String Imei = mngr.getDeviceId();
                    Log.d(IMEI,Imei);
                    SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SPreferenceKey.IMEI,Imei);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.LOGGEDIN)){
                                gotoNextActivity(HomeActivity.class);
                            }else{
                                gotoNextActivity(LoginActivity.class);
                            }

                        }
                    },2000);

                }

                    //TODO
                break;

            default:
                break;
        }
    }

    private void gotoNextActivity(final Class aClass) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, aClass));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        },3000);


    }


    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("PAYPAL ERROR");
        alertDialogBuilder.setMessage("Device not authenticated, Please contact to Administrator!");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
