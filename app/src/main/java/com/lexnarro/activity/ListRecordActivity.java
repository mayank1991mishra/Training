package com.lexnarro.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.adapter.RecordAdapter;
import com.lexnarro.adapter.YearAdapter;
import com.lexnarro.databaseHandler.DatabaseHandler;
import com.lexnarro.databaseHandler.DatabaseInterface;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.FinancialYear;
import com.lexnarro.response.Profile;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.TrainingRecord;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;
import com.lexnarro.util.DownloadOrEmail;
import com.lexnarro.util.ProgressBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRecordActivity extends AppCompatActivity implements RecordAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener, TabLayout.OnTabSelectedListener {


    private List<FinancialYear> fin_years;
    private String financial_year;
    private FinancialYear year;
    private TabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Toolbar toolbar = findViewById(R.id.record_toolbar);
        toolbar.setTitle(R.string.my_records);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Download").setIcon(R.drawable.download));
        tabs.addTab(tabs.newTab().setText("Email").setIcon(R.drawable.view));
        tabs.addOnTabSelectedListener(this);
        fin_years = (List<FinancialYear>) getIntent().getSerializableExtra("year");
        financial_year = getIntent().getStringExtra("fin_year");
        setYearSpinner(fin_years);
        setTabDivider(tabs);
        try {

            serviceGetTraining(financial_year);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void open(final String id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Delete Record");
        alertDialogBuilder.setMessage("Do you want to delete this record ?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        try {
                            serviceDeleteRecord(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setTabDivider(TabLayout tabs) {

        View root = tabs.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
//            drawable.setColor(getResources().getColor(R.color.colorPrimary));
            drawable.setSize(2, 1);
////            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }

    private void setYearSpinner(List<FinancialYear> years) {
        YearAdapter adapter = new YearAdapter(this, 0, years);
        Spinner spinYear = findViewById(R.id.yearspin);
        spinYear.setAdapter(adapter);
        spinYear.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    private void transactActivity() {

        startActivity(new Intent(ListRecordActivity.this, CreateTrainingActivity.class).putExtra("year", fin_years.get(fin_years.size() - 1).getText()));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_add:
                transactActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void inIt(List<TrainingRecord> recordList) {

        RecyclerView duty_list_view = findViewById(R.id.duty_list);
        RecordAdapter mAdapter = new RecordAdapter(recordList);
        mAdapter.setOnItemClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        duty_list_view.setLayoutManager(mLayoutManager);
        duty_list_view.setItemAnimator(new DefaultItemAnimator());
        duty_list_view.setAdapter(mAdapter);
        if (year != null) {
            for (FinancialYear years : fin_years) {
                years.setSelected("false");
            }
            year.setSelected("true");
            setYearSpinner(fin_years);
        }
    }

    private Profile getProfile() {
        DatabaseInterface databaseInterface = new DatabaseHandler(this);
        return databaseInterface.getProfile();
    }

    private void serviceGetTraining(String fin_year) throws Exception {
        ProgressBar.showDialog(ListRecordActivity.this, getString(R.string.please_wait), true, false).show();
        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setUser_email(getProfile().getEmailAddress());
        if (fin_year != null) {
            data.setFinYear(fin_year);
        }

        body.setGetTraining(data);
        envelope.setBody(body);
        call = service.training(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getGetTrainingResponse() != null
                        && response.body().getBody().getGetTrainingResponse().getGetTrainingResult() != null
                        && response.body().getBody().getGetTrainingResponse().getGetTrainingResult().getStatus() != null) {


                    if (response.body().getBody().getGetTrainingResponse().getGetTrainingResult().getStatus().equalsIgnoreCase("SUCCESS")
                            && response.body().getBody().getGetTrainingResponse().getGetTrainingResult().getUserTrainings() != null) {
                        inIt(response.body().getBody().getGetTrainingResponse().getGetTrainingResult().getUserTrainings());

                    } else {
                        Toast.makeText(ListRecordActivity.this, response.body().getBody().getGetTrainingResponse().getGetTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                } else {
                    Toast.makeText(ListRecordActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {
                onBackPressed();
                Toast.makeText(ListRecordActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });


    }

    @Override
    public void onItemClick(TrainingRecord record, String type) {
        if (type.equalsIgnoreCase("delete")) {

            try {
                open(record.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            startActivity(new Intent(ListRecordActivity.this, CreateTrainingActivity.class).putExtra("record", record).putExtra("type", type));
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
    }

    private void serviceDeleteRecord(String record_id) throws Exception {
        ProgressBar.showDialog(ListRecordActivity.this, getString(R.string.please_wait), true, false).show();
        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setDeleteId(record_id);
        body.setDeleteTraining(data);
        envelope.setBody(body);
        call = service.training(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(Call<SoapResponseEnvelope> call, Response<SoapResponseEnvelope> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getDeleteTrainingResponse() != null
                        && response.body().getBody().getDeleteTrainingResponse().getDeleteTrainingResult() != null
                        && response.body().getBody().getDeleteTrainingResponse().getDeleteTrainingResult().getStatus() != null) {


                    if (response.body().getBody().getDeleteTrainingResponse().getDeleteTrainingResult().getStatus().equalsIgnoreCase("SUCCESS")) {
                        ProgressBar.cancelDialog();
                        try {
                            SharedPreferenceWriter.getInstance(ListRecordActivity.this).writeBooleanValue(SPreferenceKey.RELOAD,true);
                            serviceGetTraining(financial_year);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(ListRecordActivity.this, response.body().getBody().getDeleteTrainingResponse().getDeleteTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(ListRecordActivity.this, response.body().getBody().getDeleteTrainingResponse().getDeleteTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ListRecordActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                }
                ProgressBar.cancelDialog();
            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                Toast.makeText(ListRecordActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position != 0) {
            try {
                year = fin_years.get(position);
                financial_year = year.getValue();

                serviceGetTraining(financial_year);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                downloadEmail(DownloadOrEmail.DOWNLOAD_RECORD);
                break;
            case 1:
                downloadEmail(DownloadOrEmail.EMAIL_RECORD);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                downloadEmail(DownloadOrEmail.DOWNLOAD_RECORD);
                break;
            case 1:
                downloadEmail(DownloadOrEmail.EMAIL_RECORD);
                break;
            default:
                break;
        }
    }


    private void downloadEmail(String type) {
        SoapRequestData data = new SoapRequestData();
        data.setFinYear(financial_year);
        data.setUser_Id(getProfile().getID());
        data.setUserEmail(getProfile().getEmailAddress());
        data.setStateShortName(getProfile().getStateEnrolledShortName());
        try {
            DownloadOrEmail.getInstance().setCall(data, type, ListRecordActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
