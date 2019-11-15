package com.lexnarro.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.adapter.PlanAdapter;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.Plans;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;
import com.lexnarro.util.ProgressBar;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * THIS FILE IS OVERWRITTEN BY `androidSDK/src/<general|partner>sampleAppJava.
 * ANY UPDATES TO THIS FILE WILL BE REMOVED IN RELEASES.
 * <p>
 * Basic sample using the SDK to make a payment or consent to future payments.
 * <p>
 * For sample mobile backend interactions, see
 * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
 */
public class SubscribeActivity extends AppCompatActivity implements PlanAdapter.OnItemClickListener {
    private static final String TAG = "LexNarro";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
//    private static final String CONFIG_CLIENT_ID = "ATXDoaFF0zx5alPuzG1cPzeHsKRJy1pDC6csqN16sTeetvtC6CsoItYceFIJwyC6tKuT2sWkinTAektK";
    private static final String CONFIG_CLIENT_ID = "AcI-THy4vbc-GLaSj3Uan_dxxvzrsrEAdDb3F9hmNsCpCr3f6bAIhWIY6IFQctpI4BXQDU5gLLIBAksu";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(CONFIG_CLIENT_ID).merchantName("Lexnarro")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://lexnarro.com.au/"))
            .merchantUserAgreementUri(
                    Uri.parse("https://lexnarro.com.au/"));
    private Plans plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);


        Toolbar toolbar = findViewById(R.id.plan_toolbar);
        toolbar.setTitle(R.string.choose_plan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


//        Start paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        try {
            serviceGetPlans();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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


    private void makePayment(Plans plans) {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(plans.getAmount()), "AUD", plans.getPlan(), PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = Objects.requireNonNull(data).getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        postTransaction(confirmation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private void serviceGetPlans() throws Exception {


        ProgressBar.showDialog(this, getString(R.string.please_wait), true, false).show();

        ApiInterface service = ClientConnection.getInstance().createService();
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        body.setPlans(data);


        envelope.setBody(body);

        service.getGetPlans(envelope).enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(Call<SoapResponseEnvelope> call, Response<SoapResponseEnvelope> response) {

                if (response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getPlansResponse() != null
                        && response.body().getBody().getPlansResponse().getPlansResult() != null
                        && response.body().getBody().getPlansResponse().getPlansResult().getStatus() != null) {

                    if (response.body().getBody().getPlansResponse().getPlansResult().getStatus().equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(SubscribeActivity.this, response.body().getBody().getPlansResponse().getPlansResult().getMessage(), Toast.LENGTH_SHORT).show();

                        setPlan(response.body().getBody().getPlansResponse().getPlansResult().getPlans());
                    } else {
                        Toast.makeText(SubscribeActivity.this, response.body().getBody().getPlansResponse().getPlansResult().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

                ProgressBar.cancelDialog();


            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                Toast.makeText(SubscribeActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();


            }
        });
    }


    private void postTransaction(PaymentConfirmation confirmation) throws Exception {


        ProgressBar.showDialog(this, getString(R.string.please_wait), true, false).show();

        ApiInterface service = ClientConnection.getInstance().createService();
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setUser_email(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.Email));
        data.setTransection_ID(confirmation.getProofOfPayment().getTransactionId());
        data.setPlan_ID(plans.getPlan_ID());
        data.setRate_Id(plans.getRate_Id());
        data.setAmount(plans.getAmount());
        data.setPayerId(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.Email));
        data.setPaymentId(confirmation.getProofOfPayment().getPaymentId());
        body.setPostTransaction(data);


        envelope.setBody(body);

        service.postTransaction(envelope).enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(Call<SoapResponseEnvelope> call, Response<SoapResponseEnvelope> response) {

                if (response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getPostTransactionResponse() != null
                        && response.body().getBody().getPostTransactionResponse().getPostTransactionResult() != null
                        && response.body().getBody().getPostTransactionResponse().getPostTransactionResult().getStatus() != null) {

                    if (response.body().getBody().getPostTransactionResponse().getPostTransactionResult().getStatus().equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(SubscribeActivity.this, response.body().getBody().getPostTransactionResponse().getPostTransactionResult().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SubscribeActivity.this, response.body().getBody().getPostTransactionResponse().getPostTransactionResult().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

                ProgressBar.cancelDialog();


            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                Toast.makeText(SubscribeActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();


            }
        });
    }


    private void setPlan(List<Plans> plansList) {

        RecyclerView plan_list_view = findViewById(R.id.plan_list);
        PlanAdapter mAdapter = new PlanAdapter(plansList);
        mAdapter.setOnItemClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        plan_list_view.setLayoutManager(mLayoutManager);
        plan_list_view.setItemAnimator(new DefaultItemAnimator());
        plan_list_view.setAdapter(mAdapter);

    }

    @Override
    public void onItemClick(Plans plans, String type) {

        makePayment(plans);

        this.plans = plans;
    }


}