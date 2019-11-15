package com.lexnarro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.adapter.MyAdapter;
import com.lexnarro.databaseHandler.DatabaseHandler;
import com.lexnarro.databaseHandler.DatabaseInterface;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.Profile;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.SoapResult;
import com.lexnarro.response.StateName;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;
import com.lexnarro.util.ProgressBar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText phone;
    EditText street_number;
    EditText street_name;
    EditText post_code;
    EditText state_enrolled;
    EditText law_society_no;
    EditText sub;
    EditText firm;
    EditText password;
    TextView country;
    private boolean edit;
    private List<StateName> stateNames;
    private Profile profile;
    private Spinner state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        toolbar.setTitle(R.string.my_profile);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            serviceLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_edit:
                if (!edit) {
                    item.setIcon(R.drawable.save);
                    edit(true);
                    edit = true;
                } else {
                    if (validate()) {
                        try {
                            updateProfile(item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void inIt() {
        first_name = findViewById(R.id.edt_fname);
        last_name = findViewById(R.id.edt_lname);
        email = findViewById(R.id.edt_email);
        phone = findViewById(R.id.edt_mobilenumber);
        street_number = findViewById(R.id.edt_streetnumber);
        street_name = findViewById(R.id.edt_streeetname);
        post_code = findViewById(R.id.edt_postcode);
        state_enrolled = findViewById(R.id.edt_stateenroll);
        law_society_no = findViewById(R.id.edt_lsn);
        sub = findViewById(R.id.edt_suburb);
        firm = findViewById(R.id.edt_firm);
        password = findViewById(R.id.edt_password);
        country = findViewById(R.id.txt_country);

        DatabaseInterface databaseInterface = new DatabaseHandler(this);
        profile = databaseInterface.getProfile();

        first_name.setText(profile.getFirstName());
        last_name.setText(profile.getLastName());
        email.setText(profile.getEmailAddress());
        phone.setText(profile.getPhoneNumber());
        street_number.setText(profile.getStreetNumber());
        street_name.setText(profile.getStreetName());
        post_code.setText(profile.getPostCode());
        state_enrolled.setText(profile.getStateEnrolledName());
        law_society_no.setText(profile.getLawSocietyNumber());
        sub.setText(profile.getSuburb());
        firm.setText(profile.getFirm());
        country.setText(profile.getCountryName());
        password.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.Password));
        try {
            serviceAllState();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void edit(boolean edit) {
        phone.setEnabled(edit);
        street_number.setEnabled(edit);
        street_name.setEnabled(edit);
        post_code.setEnabled(edit);
        law_society_no.setEnabled(edit);
        sub.setEnabled(edit);
        password.setEnabled(edit);
        state.setEnabled(edit);
        findViewById(R.id.spinner_state).setEnabled(edit);
    }


    private void serviceAllState() throws Exception {

        ProgressBar.showDialog(ProfileActivity.this, getString(R.string.please_wait), true, false).show();
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


                } else {
                    Toast.makeText(ProfileActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }

                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {


                Toast.makeText(ProfileActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
                onBackPressed();
            }
        });


    }

    private void setStateSpinner(List<StateName> stateNames) {
        Collections.sort(stateNames, new Comparator<StateName>() {
            @Override
            public int compare(StateName a, StateName b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        });
        state = findViewById(R.id.spinner_state);
        state.setEnabled(false);
        MyAdapter state_myAdapter = new MyAdapter(stateNames, getApplicationContext(), true, profile.getStateID());
        state.setOnItemSelectedListener(this);
        state.setAdapter(state_myAdapter);
        state.setEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (edit) {

            profile.setStateID(stateNames.get(position).getId());
            profile.setStateName(stateNames.get(position).getName());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private boolean validate() {
        if (phone.getText().toString().isEmpty() && phone.getText().toString().trim().length() != 10) {
            Toast.makeText(this, "Please enter 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (street_number.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter street number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (street_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter street name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (post_code.getText().toString().isEmpty() && post_code.getText().toString().length() != 4) {
            Toast.makeText(this, "Please enter 4 digit postal code", Toast.LENGTH_SHORT).show();
            return false;
        }  else if (firm.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enter_frim, Toast.LENGTH_SHORT).show();
            return false;
        } else if (law_society_no.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter law society number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (sub.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter suburb", Toast.LENGTH_SHORT).show();
            return false;
        }else if (sub.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter suburb", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().length() < 4) {
            Toast.makeText(this, "Please enter the correct password, Minimum 4 digit", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            profile.setPhoneNumber(phone.getText().toString());
            profile.setStreetNumber(street_number.getText().toString());
            profile.setStreetName(street_name.getText().toString());
            profile.setPostCode(post_code.getText().toString());
            profile.setLawSocietyNumber(law_society_no.getText().toString());
            profile.setSuburb(sub.getText().toString());
            profile.setPassword(password.getText().toString());
            return true;
        }
    }


    private void updateProfile(final MenuItem item) throws Exception {
        ProgressBar.showDialog(ProfileActivity.this, getString(R.string.please_wait), true, false).show();
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        final SoapRequestData data = new SoapRequestData();
        data.setId(profile.getID());
        data.setFirstName(profile.getFirstName());
        data.setLastName(profile.getLastName());
        data.setOtherName(profile.getOtherName());
        data.setStreet_Name(profile.getStreetName());
        data.setPost_Code(profile.getPostCode());
        data.setSub_urb(profile.getSuburb());
        data.setStateId(profile.getStateID());
        data.setCountryId(profile.getCountryID());
        data.setStreet_Number(profile.getStreetNumber());
        data.setLawSociety_Number(profile.getLawSocietyNumber());
        data.setPhoneNumber(profile.getPhoneNumber());
        data.setAddress(profile.getStreetNumber() + "," +
                profile.getStreetName() + "," +
                profile.getPostCode());
        data.setFirm(profile.getFirm());
        data.setPassword(profile.getPassword());

        body.setUpdateProfile(data);
        envelope.setBody(body);

        Call<SoapResponseEnvelope> call = ClientConnection.getInstance().createService().updateProfile(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getUpdateUserProfileResponse() != null
                        && response.body().getBody().getUpdateUserProfileResponse().getUpdateUserProfileResult() != null
                        && response.body().getBody().getUpdateUserProfileResponse().getUpdateUserProfileResult().getStatus() != null
                        && response.body().getBody().getUpdateUserProfileResponse().getUpdateUserProfileResult().getStatus().equalsIgnoreCase("SUCCESS")) {


                   Profile profile = response.body().getBody().getUpdateUserProfileResponse().getUpdateUserProfileResult().getUserProfile();
                   profile.setPassword(ProfileActivity.this.profile.getPassword());
                   SharedPreferenceWriter.getInstance(ProfileActivity.this).writeStringValue(SPreferenceKey.Password,profile.getPassword());


                    DatabaseInterface databaseInterface = new DatabaseHandler(ProfileActivity.this);
                    databaseInterface.addProfile(profile);
                    item.setIcon(R.drawable.edit);
                    edit(false);
                    edit = false;
                    Toast.makeText(ProfileActivity.this, response.body().getBody().getUpdateUserProfileResponse().getUpdateUserProfileResult().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                }

                ProgressBar.cancelDialog();
            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });


    }


    private void serviceLogin() throws Exception {

        ProgressBar.showDialog(ProfileActivity.this, getString(R.string.please_wait), true, false).show();
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        final SoapRequestData data = new SoapRequestData();
        data.setUsername(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.Email));
        data.setPassword(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.Password));
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


                        ProgressBar.cancelDialog();
                        DatabaseInterface databaseInterface = new DatabaseHandler(ProfileActivity.this);
                        databaseInterface.addProfile(result.getProfile());
                        inIt();

                    } else {
                        ProgressBar.cancelDialog();
                        onBackPressed();
                    }
                }




            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {
                ProgressBar.cancelDialog();
                Toast.makeText(ProfileActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                onBackPressed();


            }
        });

    }
}
