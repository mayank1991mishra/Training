package com.lexnarro.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.adapter.CarryAdapter;
import com.lexnarro.adapter.YearAdapter;
import com.lexnarro.bean.CarryOverData;
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
import com.lexnarro.util.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarryOverActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CarryAdapter.OnEditListener {
    private TabLayout tabs;
    private String financial_year;
    private List<FinancialYear> fin_years;
    private Double unit;
    private OnUnitChangeListener onUnitChangeListener;
//    private List<Double> unitList = new ArrayList<>();
    private List<CarryOverData> dataList = new ArrayList<>();
    private List<TrainingRecord> getUserTrainings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carry_over);
        Toolbar toolbar = findViewById(R.id.carry_toolbar);
        toolbar.setTitle(R.string.carry_over);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fin_years = (List<FinancialYear>) getIntent().getSerializableExtra("year");
        financial_year = getIntent().getStringExtra("fin_year");
        try {
            serviceCarryOver(financial_year);
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.carry_tabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    serviceDoCarryOver();
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


    private void inIt(List<TrainingRecord> recordList) {

        RecyclerView carry_list_view = findViewById(R.id.carry_list);
        CarryAdapter mAdapter = new CarryAdapter(recordList, unit, this);
        mAdapter.setOnEditListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        carry_list_view.setLayoutManager(mLayoutManager);
        carry_list_view.setItemAnimator(new DefaultItemAnimator());
        carry_list_view.setAdapter(mAdapter);

    }

    private void setYearSpinner(List<FinancialYear> years) {
        YearAdapter adapter = new YearAdapter(this, 0, years);
        Spinner spinYear = findViewById(R.id.year_spinner);
        spinYear.setAdapter(adapter);
        spinYear.setOnItemSelectedListener(this);
    }

    private Profile getProfile() {
        DatabaseInterface databaseInterface = new DatabaseHandler(this);
        return databaseInterface.getProfile();
    }


    private void serviceCarryOver(String fin_year) throws Exception {
        ProgressBar.showDialog(this, getString(R.string.please_wait), true, false).show();
        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setStateId(getProfile().getStateEnrolled());
        data.setUserId(getProfile().getID());
        if (fin_year != null) {
            data.setFinYear(fin_year);
        }

        body.setGetCarryOverRecords(data);
        envelope.setBody(body);
        call = service.carryOver(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getCarryOverRecordsResponse() != null
                        && response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult() != null
                        && response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult().getStatus() != null) {


                    if (response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult().getStatus().equalsIgnoreCase("SUCCESS")
                            && response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult().getUserTrainings() != null) {


                        CarryOverActivity.this.unit = Double.parseDouble(response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult().getMaximumUnitsAllowed());
                        ((TextView) findViewById(R.id.txt_allow_unit)).setText(String.format("Allowed units to transfer = %s", unit));
                        getUserTrainings=response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult().getUserTrainings();
                        inIt(getUserTrainings);
                        setYearSpinner(response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult().getFinancialYears());

                    } else {
                        Toast.makeText(CarryOverActivity.this, response.body().getBody().getCarryOverRecordsResponse().getCarryOverRecordsResult().getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                } else {
                    Toast.makeText(CarryOverActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {
                onBackPressed();
                Toast.makeText(CarryOverActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            try {
                financial_year = fin_years.get(position).getValue();

                serviceCarryOver(financial_year);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidate(){
        double j=0;
        if (getUserTrainings.size()>0) {

            for (int i = 0; i < getUserTrainings.size(); i++) {
                if (getUserTrainings.get(i).isSelected())

                j=j+Double.parseDouble(getUserTrainings.get(i).getEntered_unit());

            }
        }
        if (j<=unit){
            return true;
        }else {
            Toast.makeText(this, "Total entered unit can not be greater than allowed unit", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onUnitEnter( int position, double unit, boolean add,List<TrainingRecord> getUserTrainings) {
//        this.getUserTrainings.clear();
//        this.getUserTrainings.addAll(getUserTrainings);
//        if (add){
//            if(unitList.size()>position){
//                unitList.set(position, unit);
//            }else {
//                unitList.add(position, unit);
//            }
//
//        }else {
//            unitList.remove(position);
//        }


//        this.unit= this.unit-unit;
//        onUnitChangeListener.onUnitChange(this.unit);
//        ((TextView)findViewById(R.id.txt_remainng_unit)).setText(String.valueOf(this.unit));
    }

    @Override
    public void onUnitDeleted(double unit) {
//        this.unit=this.unit+unit;
//        onUnitChangeListener.onUnitChange(this.unit);
//        ((TextView)findViewById(R.id.txt_remainng_unit)).setText(String.valueOf(this.unit));
    }

    @Override
    public void setListener(OnUnitChangeListener listener) {
        this.onUnitChangeListener = listener;
        listener.onUnitChange(unit);
    }

    public interface OnUnitChangeListener {
        void onUnitChange(double unit);
    }

    private void serviceDoCarryOver() throws Exception {



        ProgressBar.showDialog(this, getString(R.string.please_wait), true, false).show();
        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        if (getUserTrainings!=null && isValidate()){
            String ids="";
            String units="";
            for (int i=0;i<getUserTrainings.size();i++) {
                if (getUserTrainings.get(i).isSelected()) {
                    if (ids.equalsIgnoreCase("")) {
                        ids = getUserTrainings.get(i).getTransactionId();
                        units = getUserTrainings.get(i).getEntered_unit();
                    } else {
                        ids = ids + "," + getUserTrainings.get(i).getTransactionId();
                        units = units + "," + getUserTrainings.get(i).getEntered_unit();
                    }

                }
            }

                if (ids.equalsIgnoreCase("")){

                    ProgressBar.cancelDialog();
                    Toast.makeText(this, "Please select atleast one activity to carry over", Toast.LENGTH_SHORT).show();
                    return;

                }


            data.setIds(ids);
            data.setUnits(units);
            data.setStateId(getUserTrainings.get(0).getStateId());
            data.setUserID(getProfile().getID());
            data.setFinancialYear(financial_year);


        }else {
            ProgressBar.cancelDialog();
            return;
        }



        body.setDoCarryOver(data);
        envelope.setBody(body);
        call = service.carryOver(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(Call<SoapResponseEnvelope> call, Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getDoCarryOverResponse() != null
                        && response.body().getBody().getDoCarryOverResponse().getDoCarryOverResult() != null
                        && response.body().getBody().getDoCarryOverResponse().getDoCarryOverResult().getStatus() != null) {


                    if (response.body().getBody().getDoCarryOverResponse().getDoCarryOverResult().getStatus().equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(CarryOverActivity.this, response.body().getBody().getDoCarryOverResponse().getDoCarryOverResult().getMessage(), Toast.LENGTH_SHORT).show();

                        try {
                            ProgressBar.cancelDialog();
                            serviceCarryOver(financial_year);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        Toast.makeText(CarryOverActivity.this, response.body().getBody().getDoCarryOverResponse().getDoCarryOverResult().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CarryOverActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                }
                ProgressBar.cancelDialog();
            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {
                ProgressBar.cancelDialog();
                onBackPressed();
            }
        });


    }
}
