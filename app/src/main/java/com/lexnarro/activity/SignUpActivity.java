package com.lexnarro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.adapter.MyAdapter;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.SoapResult;
import com.lexnarro.response.StateName;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;
import com.lexnarro.util.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView txt_country;
    ;
    EditText edt_fname;
    EditText edt_lname;
    EditText edt_email;
    EditText edt_mobilenumber;
    EditText edt_streetnumber;
    EditText edt_streeetname;
    EditText edt_postcode;
    EditText edt_lsn, edt_password, edt_sub, edt_firm;
    Button btn_signup;
    String state_name;
    String state_enroll;
    private List<StateName> stateNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SignUP");

        edt_fname = (EditText) findViewById(R.id.edt_fname);
        edt_lname = (EditText) findViewById(R.id.edt_lname);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobilenumber = (EditText) findViewById(R.id.edt_mobilenumber);
        edt_streetnumber = (EditText) findViewById(R.id.edt_streetnumber);
        edt_streeetname = (EditText) findViewById(R.id.edt_streeetname);
        edt_postcode = (EditText) findViewById(R.id.edt_postcode);
        txt_country = (TextView) findViewById(R.id.txt_country);
        edt_lsn = (EditText) findViewById(R.id.edt_lsn);
        edt_sub = (EditText) findViewById(R.id.edt_suburb);
        edt_firm = (EditText) findViewById(R.id.edt_firm);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
        try {
            serviceAllState();
            ProgressBar.showDialog(SignUpActivity.this, getString(R.string.please_wait), true, false).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addPrefixOnMobile();


    }

    private void addPrefixOnMobile() {
        edt_mobilenumber.getText().insert(edt_mobilenumber.getSelectionStart(), "0");
        edt_mobilenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("0")) {
                    edt_mobilenumber.setText("0");
                }
            }
        });
    }


    private void setEnrollSpinner(List<StateName> stateNames) {
        Collections.sort(stateNames, new Comparator<StateName>() {
            @Override
            public int compare(StateName a, StateName b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        });
        List<StateName> names = new ArrayList<>(stateNames);
        StateName stateName = new StateName();
        stateName.setId("00001");
        stateName.setName("Select State Enroll");
        names.add(0, stateName);
        Spinner state_en = findViewById(R.id.spinner_state_en);
        MyAdapter state_en_myAdapter = new MyAdapter(names, getApplicationContext());
        state_en.setAdapter(state_en_myAdapter);
        state_en.setOnItemSelectedListener(this);
    }


    private void setStateSpinner(List<StateName> stateNames) {
        Collections.sort(stateNames, new Comparator<StateName>() {
            @Override
            public int compare(StateName a, StateName b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        });
        List<StateName> names = new ArrayList<>(stateNames);
        StateName stateName = new StateName();
        stateName.setId("00001");
        stateName.setName("Select State");
        names.add(0, stateName);
        Spinner state = findViewById(R.id.spinner_state);
        MyAdapter state_myAdapter = new MyAdapter(names, getApplicationContext());
        state.setAdapter(state_myAdapter);
        state.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_signup:
                signUpValidation();
                break;
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

    private void signUpValidation() {
        if (!edt_fname.getText().toString().trim().isEmpty()) {
            if (!edt_lname.getText().toString().trim().isEmpty()) {
                if (!edt_email.getText().toString().trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()) {
                    if (!edt_mobilenumber.getText().toString().trim().isEmpty() && edt_mobilenumber.getText().toString().trim().length() == 10) {
                        if (!edt_streetnumber.getText().toString().trim().isEmpty()) {
                            if (!edt_streeetname.getText().toString().trim().isEmpty()) {
                                if (!edt_postcode.getText().toString().trim().isEmpty() && edt_postcode.getText().toString().trim().length() == 4) {
                                    if (state_name != null) {
                                        if (state_enroll != null) {
                                            if (!edt_lsn.getText().toString().trim().isEmpty() && edt_lsn.getText().toString().length() > 4 && edt_lsn.getText().toString().length() <= 10) {
                                                if (!edt_sub.getText().toString().trim().isEmpty()) {
                                                    if (!edt_password.getText().toString().trim().isEmpty() && edt_password.getText().toString().trim().length() >= 8) {
                                                        if (!edt_firm.getText().toString().trim().isEmpty()) {
                                                            try {
                                                                serviceSignup();

                                                                ProgressBar.showDialog(SignUpActivity.this, getString(R.string.please_wait), true, true).show();

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            Toast.makeText(this, R.string.enter_frim, Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(this, R.string.suburb, Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(this, R.string.lsn, Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(this, R.string.state_enroll, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, R.string.select_state, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(this, R.string.postcode, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, R.string.sname, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, R.string.snumber, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.mnumber, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.email, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.lname, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.fname, Toast.LENGTH_SHORT).show();

        }
    }

    private void serviceSignup() throws Exception {


        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        final SoapRequestData data = new SoapRequestData();
        data.setFname(edt_fname.getText().toString().trim());
        data.setLname(edt_lname.getText().toString().trim());
        data.setOname("");
        data.setStreetNumber(edt_streetnumber.getText().toString().trim());
        data.setStreetName(edt_streeetname.getText().toString().trim());
        data.setPostCode(edt_postcode.getText().toString().trim());
        data.setSuburb(edt_sub.getText().toString().trim());
        data.setStateName(state_name);
        data.setCountryName(txt_country.getText().toString().trim());
        data.setPostCode(edt_postcode.getText().toString().trim());
        data.setStateEnrolled(state_enroll);
        data.setLawSocietyNumber(edt_lsn.getText().toString().trim());
        data.setEmail(edt_email.getText().toString().trim());
        data.setPhonenumber(edt_mobilenumber.getText().toString().trim());
        data.setFirm(edt_firm.getText().toString().trim());
//        data.setUsername(edt_email.getText().toString().trim());
        data.setPassword(edt_password.getText().toString().trim());
//        data.setDate("01-01-1970");
//        data.setRole("1");
        data.setAddress(edt_streetnumber.getText().toString().trim() + "," +
                edt_streeetname.getText().toString().trim() + "," +
                edt_postcode.getText().toString().trim());
        data.setImei(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.IMEI));
        data.setDeviceToken("12523456");
        data.setDeviceType("Android");

        body.setSignUp(data);
        envelope.setBody(body);

        ApiInterface service = ClientConnection.getInstance().createService();

        Call<SoapResponseEnvelope> call = service.signup(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {


            @Override
            public void onResponse(Call<SoapResponseEnvelope> call, Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()) {
                    SoapResult result = response.body().getBody().getSignUpResponse().getSignUpResult();
                    if (result.getStatus() != null && result.getStatus().equalsIgnoreCase("SUCCESS")) {
//                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SPreferenceKey.Email, edt_email.getText().toString());


                        String message = result.getMessage();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.putExtra("message", message);
                        intent.putExtra("email", edt_email.getText().toString());

                        startActivity(intent);

                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

                    } else {
                        Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                Toast.makeText(SignUpActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });

    }


    private void serviceAllState() throws Exception {


        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setCountryId("9");
        body.setAllStates(data);
        envelope.setBody(body);
        call = service.getState(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getAllStatesResponse() != null
                        && response.body().getBody().getAllStatesResponse().getGetAllStatesResult() != null
                        && response.body().getBody().getAllStatesResponse().getGetAllStatesResult().getStatus() != null
                        && response.body().getBody().getAllStatesResponse().getGetAllStatesResult().getStatus().equalsIgnoreCase("SUCCESS")) {


                    stateNames = response.body().getBody().getAllStatesResponse().getGetAllStatesResult().getStates();
                    setStateSpinner(stateNames);
                    setEnrollSpinner(stateNames);


                } else {
                    Toast.makeText(SignUpActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                }

                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                Toast.makeText(SignUpActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (parent.getId()) {
            case R.id.spinner_state:
                if (position != 0) {
                    state_name = ((TextView) view).getText().toString();
                } else {
                    state_name = null;
                }
                break;
            case R.id.spinner_state_en:
                if (position != 0) {
                    state_enroll = ((TextView) view).getText().toString();
                } else {
                    state_enroll = null;
                }
                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

