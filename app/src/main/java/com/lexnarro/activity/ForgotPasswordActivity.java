package com.lexnarro.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.util.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.password_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(R.string.f_password);
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    serviceGetPassword((EditText) findViewById(R.id.edt_email));
                    ProgressBar.showDialog(ForgotPasswordActivity.this, getString(R.string.please_wait), true, false).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }


    private void serviceGetPassword(EditText email) throws Exception {
        if (!email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {


            ApiInterface service = ClientConnection.getInstance().createService();
            Call<SoapResponseEnvelope> call = null;
            SoapEnvelop envelope = new SoapEnvelop();
            SoapBody body = new SoapBody();
            SoapRequestData data = new SoapRequestData();
            data.setEmailId(email.getText().toString().trim());
            body.setMailPassword(data);
            envelope.setBody(body);
            call = service.forgotPassword(envelope);
            call.enqueue(new Callback<SoapResponseEnvelope>() {
                @Override
                public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                    if (response.isSuccessful()
                            && response.body() != null
                            && response.body().getBody() != null
                            && response.body().getBody().getMailPasswordResponse() != null
                            && response.body().getBody().getMailPasswordResponse().getMailPasswordResult() != null
                            && response.body().getBody().getMailPasswordResponse().getMailPasswordResult().getStatus() != null) {


                        Toast.makeText(ForgotPasswordActivity.this, response.body().getBody().getMailPasswordResponse().getMailPasswordResult().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                    }
                    ProgressBar.cancelDialog();
                    onBackPressed();

                }

                @Override
                public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                    Toast.makeText(ForgotPasswordActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    ProgressBar.cancelDialog();
                }
            });
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter correct email address", Toast.LENGTH_SHORT).show();

            ProgressBar.cancelDialog();
        }


    }
}
