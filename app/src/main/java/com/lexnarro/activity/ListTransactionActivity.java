package com.lexnarro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.adapter.TransactionAdapter;
import com.lexnarro.databaseHandler.DatabaseHandler;
import com.lexnarro.databaseHandler.DatabaseInterface;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.Profile;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.TransactionRecord;
import com.lexnarro.util.ProgressBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTransactionActivity extends AppCompatActivity implements TransactionAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);
        Toolbar toolbar = findViewById(R.id.transaction_toolbar);
        toolbar.setTitle(R.string.my_transaction);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            serviceGetTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void inIt(List<TransactionRecord> recordList) {

        RecyclerView duty_list_view = findViewById(R.id.transaction_list);
        TransactionAdapter mAdapter = new TransactionAdapter(recordList);
        mAdapter.setOnItemClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        duty_list_view.setLayoutManager(mLayoutManager);
        duty_list_view.setItemAnimator(new DefaultItemAnimator());
        duty_list_view.setAdapter(mAdapter);
    }


    private Profile getProfile() {
        DatabaseInterface databaseInterface = new DatabaseHandler(this);
        return databaseInterface.getProfile();
    }

    private void serviceGetTransaction() throws Exception {
        ProgressBar.showDialog(ListTransactionActivity.this, getString(R.string.please_wait), true, false).show();

        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setUser_email(getProfile().getEmailAddress());
        body.setTransactions(data);
        envelope.setBody(body);
        call = service.getTransaction(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getGetTransactionsResponse() != null
                        && response.body().getBody().getGetTransactionsResponse().getGetTransactionsResult() != null
                        && response.body().getBody().getGetTransactionsResponse().getGetTransactionsResult().getStatus() != null
                        && response.body().getBody().getGetTransactionsResponse().getGetTransactionsResult().getTransactions() != null) {


                    if (response.body().getBody().getGetTransactionsResponse().getGetTransactionsResult().getStatus().equalsIgnoreCase("SUCCESS"))

                    {
                        inIt(response.body().getBody().getGetTransactionsResponse().getGetTransactionsResult().getTransactions());

                    } else {
                        Toast.makeText(ListTransactionActivity.this, response.body().getBody().getGetTransactionsResponse().getGetTransactionsResult().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ListTransactionActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                }
                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                Toast.makeText(ListTransactionActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });


    }

    @Override
    public void onItemClick(TransactionRecord record, String type) {
        startActivity(new Intent(this, TransactionDetailActivity.class).putExtra("record", record).putExtra("type", type));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
