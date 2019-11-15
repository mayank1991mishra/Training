package com.lexnarro.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.databaseHandler.DatabaseHandler;
import com.lexnarro.databaseHandler.DatabaseInterface;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.SoapResult;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;
import com.lexnarro.util.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_signup, f_password;
    EditText edt_username, edt_password;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_signup = (TextView) findViewById(R.id.txt_signup);
        f_password = (TextView) findViewById(R.id.f_password);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.f_password).setOnClickListener(this);

        String stringFirst = "Dont have an account? ";
        String stringSecond = "SignUp";

        SpannableString spannable = new SpannableString(stringFirst + stringSecond);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        };
        spannable.setSpan(clickableSpan, stringFirst.length(), stringFirst.length() + stringSecond.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt_signup.setText(spannable);
        txt_signup.setMovementMethod(LinkMovementMethod.getInstance());
        txt_signup.setHighlightColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
        if (getIntent() != null && getIntent().hasExtra("message")) {
            message = getIntent().getStringExtra("message");
            edt_username.setText(getIntent().getStringExtra("email"));

        }
        if (!(message.equalsIgnoreCase(""))) {
            activationDialog();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                try {
                    serviceLogin();
                    ProgressBar.showDialog(LoginActivity.this, getString(R.string.please_wait), true, true).show();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.f_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
    }

    public void activationDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_popup);

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_message);
        Button btn_activated = (Button) dialog.findViewById(R.id.btn_activated);
        txt_cancel.setText(message);

        btn_activated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void serviceLogin() throws Exception {


        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        final SoapRequestData data = new SoapRequestData();
        data.setUsername(edt_username.getText().toString());
        data.setPassword(edt_password.getText().toString());
        data.setImei(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.IMEI));
        data.setDeviceToken("12523456");
        data.setDeviceType("Android");

        body.setLogin(data);
        envelope.setBody(body);

        ApiInterface service = ClientConnection.getInstance().createService();

        Call<SoapResponseEnvelope> call = service.login(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {


            @Override
            public void onResponse(Call<SoapResponseEnvelope> call, Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()) {
                    SoapResult result = response.body().getBody().getLoginResponse().getLoginResult();
                    if (result.getStatus() != null && result.getStatus().equalsIgnoreCase("SUCCESS")) {
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(SPreferenceKey.LOGGEDIN, true);
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SPreferenceKey.Email, edt_username.getText().toString());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SPreferenceKey.Password, edt_password.getText().toString());


                        DatabaseInterface databaseInterface = new DatabaseHandler(LoginActivity.this);
                        databaseInterface.addProfile(result.getProfile());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("profile", result.getProfile()));
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                        finishAffinity();

                    } else {
                        Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        message = result.getMessage();
                        activationDialog();
                    }
                }


                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {
                ProgressBar.cancelDialog();
            }
        });

    }
}
