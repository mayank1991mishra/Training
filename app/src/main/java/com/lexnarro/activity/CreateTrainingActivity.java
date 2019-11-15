package com.lexnarro.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.lexnarro.R;
import com.lexnarro.adapter.MyAdapter;
import com.lexnarro.databaseHandler.DatabaseHandler;
import com.lexnarro.databaseHandler.DatabaseInterface;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.MyCategory;
import com.lexnarro.response.Profile;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.TrainingRecord;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;
import com.lexnarro.util.MediaHelper;
import com.lexnarro.util.ProgressBar;
import com.lexnarro.util.TakeImage;
import com.lexnarro.util.TimePickerDialogs;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTrainingActivity extends AppCompatActivity implements View.OnClickListener, Callback<SoapResponseEnvelope>, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private TextView txt_date;
    private EditText txt_hours;
    private EditText provider;
    private EditText description;
    private String activity_id;
    private String base64 = "";
    private String path;
    private String file_name = "";

    private String sub_activity_id;
    private String category_id;
    private String role;
    private String time;
    private Spinner spinner;
    private TrainingRecord record;
    private boolean edit;
    private MyAdapter myAdapterCategory;
    private MyAdapter myAdapterActivity;
    private MyAdapter myAdapterSub;
    private String fina_year;
    private ImageView imageView;
    private PDFView pdfView;
    private boolean network;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);

        Toolbar toolbar = findViewById(R.id.userTrans_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        inIt();

        if (getIntent() != null && getIntent().getSerializableExtra("record") != null) {
            record = (TrainingRecord) getIntent().getSerializableExtra("record");
            edit = true;
            setEditDetails(toolbar);
            try {
                category_id = record.getCategoryId();
                activity_id = record.getActivityId();
                sub_activity_id = record.getSubActivityId();
                serviceTraining(true);
                serviceGetTraining();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (getIntent() != null && getIntent().getSerializableExtra("year") != null) {
                fina_year = getIntent().getStringExtra("year");
            }
        } else if (getIntent() != null && getIntent().getSerializableExtra("year") != null) {
            fina_year = getIntent().getStringExtra("year");
            toolbar.setTitle(R.string.create_record);
            setCurrentDate();
        } else {
            toolbar.setTitle(R.string.create_record);
            setCurrentDate();

        }
        try {
            serviceTraining(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pickDocument(String from, int request_code) {
        Intent intent_cam = new Intent(this, TakeImage.class);
        intent_cam.putExtra("from", from);
        startActivityForResult(intent_cam, request_code);
    }


    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Upload Document");
        alertDialogBuilder.setMessage("1. Please select the document to upload.\n" +
                "2. File size not more than 1MB.\n" +
                "3. Select only image(PNG, JPG, JPEG) or PDF file.");
        alertDialogBuilder.setPositiveButton("Camera",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        pickDocument("camera", 0);
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton("Browse", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

                pickDocument("storage", 0);
                dialog.cancel();
            }
        });

        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private Profile getProfile() {
        DatabaseInterface databaseInterface = new DatabaseHandler(this);
        return databaseInterface.getProfile();
    }

    private void inIt() {


        txt_date = findViewById(R.id.txtdate);
        txt_hours = findViewById(R.id.txt_hours);
        provider = findViewById(R.id.edt_provider);
        description = findViewById(R.id.edt_description);
        txt_date.setOnClickListener(this);
        imageView = findViewById(R.id.img_upload);
        pdfView = findViewById(R.id.pdf_upload);
        findViewById(R.id.btn_create).setOnClickListener(this);
        findViewById(R.id.pdf_upload).setOnClickListener(this);
        findViewById(R.id.img_upload).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
        ((RadioGroup) findViewById(R.id.roles)).setOnCheckedChangeListener(this);


        // Show current date

    }

    private void setCurrentDate() {
        // Get current date by calender
        final String[] MONTHS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = String.valueOf(day) + "/" + MONTHS[month] + "/" +
                year + " ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date d = dateFormat.parse(date);
            txt_date.setText(dateFormat.format(d));
        } catch (Exception e) {
            //java.text.ParseException: Unparseable date: Geting error
            System.out.println("Excep" + e);
        }
    }

    private void disableAllWithDetails() {

        LinearLayout ll = findViewById(R.id.root);
        for (View view : ll.getTouchables()) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.setEnabled(false);
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
            }
            if (view instanceof Spinner) {
                Spinner spinner = (Spinner) view;
                spinner.setEnabled(false);
                spinner.setFocusable(false);
                spinner.setFocusableInTouchMode(false);
            }
            view.setEnabled(false);
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
        }

        setExistingDetails();

    }


    private void setExistingDetails() {
        txt_date.setText(record.getDate());
        txt_hours.setText(record.getHours());
        provider.setText(record.getProvider());
        description.setText(record.getDescrption());
        setRole(record.getYour_Role());
    }

    private void setEditDetails(Toolbar toolbar) {
        if (getIntent() != null && getIntent().getSerializableExtra("record") != null) {

            switch (getIntent().getStringExtra("type")) {

                case "edit":
                    ((Button) findViewById(R.id.btn_create)).setText(R.string.save);
                    toolbar.setTitle(R.string.edit_training);
                    setExistingDetails();
                    break;
                case "detail":
                    ((Button) findViewById(R.id.btn_create)).setVisibility(View.GONE);
                    toolbar.setTitle(R.string.training_details);
                    disableAllWithDetails();
                    break;
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtdate:
                showDatePickerDialog();
                break;
            case R.id.upload:
                open();
                break;
            case R.id.img_upload:

                if (network){
                    viewNetwork();
                }else {
                    viewLarge(false);
                }
                break;
            case R.id.pdf_upload:
                if (network){
                    viewNetwork();
                }else {
                    viewLarge(true);
                }
                break;
            case R.id.btn_create:
                try {
                    serviceCreateEditTraining();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void viewLarge(boolean type){
        startActivity(new Intent(this,ShowActivity.class).putExtra("path",path).putExtra("url",url).putExtra("network",network).putExtra("type",type).putExtra("content",file_name));
    }

    private void viewNetwork(){
        startActivity(new Intent(this,WebActivity.class).putExtra("url",url).putExtra("content",file_name));
    }


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


    private void setCategory(List<MyCategory> userCategory) {
        MyCategory act = new MyCategory();
        act.setName("Please select category");
        act.setId("0");
        userCategory.add(0, act);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        if (edit) {
            myAdapterCategory = new MyAdapter(userCategory, this, 0, true, record.getCategoryId());
        } else {
            myAdapterCategory = new MyAdapter(userCategory, this, 0);
        }

        spinner.setAdapter(myAdapterCategory);
        spinner.setOnItemSelectedListener(this);
    }


    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void serviceCreateEditTraining() throws Exception {

        if (!validate()) {
            return;
        }
        ProgressBar.showDialog(CreateTrainingActivity.this, getString(R.string.please_wait), true, false).show();

        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setUserId(getProfile().getID());
        data.setDate(txt_date.getText().toString().trim());
        data.setState_Id(getProfile().getStateEnrolled());
        data.setCategory_Id(category_id);
        data.setActivity_Id(activity_id);
        if (sub_activity_id == null || sub_activity_id.trim().equalsIgnoreCase("NULL") || sub_activity_id.trim().equalsIgnoreCase("")) {
            data.setSubActivity_Id("");
        } else {
            data.setSubActivity_Id(sub_activity_id);
        }

        data.setHours(txt_hours.getText().toString());
        data.setProvider(provider.getText().toString().trim());
        data.setYour_Role(role);
        data.setDescription(description.getText().toString().trim());
        data.setFile_name(file_name);
        data.setFile(base64);

        if (edit) {
            data.setRecordId(record.getTransactionId());
            body.setEditTraining(data);
        } else {
            body.setCreateTraining(data);
        }


        envelope.setBody(body);
        call = service.training(envelope);

        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(Call<SoapResponseEnvelope> call, Response<SoapResponseEnvelope> response) {

                if (response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getCreateTrainingResponse() != null
                        && response.body().getBody().getCreateTrainingResponse().getCreateTrainingResult() != null
                        && response.body().getBody().getCreateTrainingResponse().getCreateTrainingResult().getStatus() != null) {

                    if (response.body().getBody().getCreateTrainingResponse().getCreateTrainingResult().getStatus().equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(CreateTrainingActivity.this, response.body().getBody().getCreateTrainingResponse().getCreateTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPreferenceWriter.getInstance(CreateTrainingActivity.this).writeBooleanValue(SPreferenceKey.RELOAD, true);

                        onBackPressed();

                    } else if (response.body().getBody().getCreateTrainingResponse().getCreateTrainingResult().getStatus().equalsIgnoreCase("FAILURE")) {
                        Toast.makeText(CreateTrainingActivity.this, response.body().getBody().getCreateTrainingResponse().getCreateTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(CreateTrainingActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                    }

                } else if (response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getEditTrainingResponse() != null
                        && response.body().getBody().getEditTrainingResponse().getEditTrainingResult() != null
                        && response.body().getBody().getEditTrainingResponse().getEditTrainingResult().getStatus() != null) {

                    if (response.body().getBody().getEditTrainingResponse().getEditTrainingResult().getStatus().equalsIgnoreCase("SUCCESS")) {

                        Toast.makeText(CreateTrainingActivity.this, response.body().getBody().getEditTrainingResponse().getEditTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPreferenceWriter.getInstance(CreateTrainingActivity.this).writeBooleanValue(SPreferenceKey.RELOAD, true);
                        onBackPressed();

                    } else if (response.body().getBody().getEditTrainingResponse().getEditTrainingResult().getStatus().equalsIgnoreCase("FAILURE")) {

                        Toast.makeText(CreateTrainingActivity.this, response.body().getBody().getEditTrainingResponse().getEditTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateTrainingActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                    }
                }

                ProgressBar.cancelDialog();


            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                Toast.makeText(CreateTrainingActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();


            }
        });
    }


    private void serviceTraining(boolean sub) throws Exception {

        if (sub) {
            getCall("sub_activity").enqueue(this);
        } else {
            getCall("category").enqueue(this);
            getCall("activity").enqueue(this);
        }
    }


    private Call<SoapResponseEnvelope> getCall(String type) throws Exception {

        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        final SoapRequestData data = new SoapRequestData();
        switch (type) {
            case "category":
                data.setStateId(getProfile().getStateEnrolled());
                body.setGetCategories(data);
                envelope.setBody(body);
                call = service.training(envelope);
                break;
            case "activity":
                data.setStateId(getProfile().getStateEnrolled());
                body.setGetActivities(data);
                envelope.setBody(body);
                call = service.training(envelope);
                break;
            case "sub_activity":
                data.setStateId(getProfile().getStateEnrolled());
                data.setUserActivityId(activity_id);
                body.setGetSubActivities(data);
                envelope.setBody(body);
                call = service.training(envelope);
                break;
            default:
                break;
        }

        return call;

    }

    @Override
    public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
        if (response.isSuccessful()) {


            if (response.body() != null
                    && response.body().getBody() != null
                    && response.body().getBody().getCategoryResponse() != null
                    && response.body().getBody().getCategoryResponse().getCategoryResult() != null
                    && response.body().getBody().getCategoryResponse().getCategoryResult().getUserCategories() != null) {

                setCategory(response.body().getBody().getCategoryResponse().getCategoryResult().getUserCategories());
            }

            if (response.body() != null
                    && response.body().getBody() != null
                    && response.body().getBody().getActivityResponse() != null
                    && response.body().getBody().getActivityResponse().getActivityResult() != null
                    && response.body().getBody().getActivityResponse().getActivityResult().getUserActivity() != null) {
                setActivity(response.body().getBody().getActivityResponse().getActivityResult().getUserActivity());
            }
            if (response.body() != null
                    && response.body().getBody() != null
                    && response.body().getBody().getSubActivityResponse() != null
                    && response.body().getBody().getSubActivityResponse().getSubActivityResult() != null
                    && response.body().getBody().getSubActivityResponse().getSubActivityResult().getUserSubActivity() != null) {
                setSubActivity(response.body().getBody().getSubActivityResponse().getSubActivityResult().getUserSubActivity());
            }


        }
    }

    private void setSubActivity(List<MyCategory> subActivity) {
        MyCategory act = new MyCategory();
        act.setName("Please select sub activity");
        act.setId("0");
        subActivity.add(0, act);
        spinner = (Spinner) findViewById(R.id.sub_activity_spinner);


        if (edit) {
            myAdapterSub = new MyAdapter(subActivity, this, 2, true, record.getSubActivityId());
        } else {
            myAdapterSub = new MyAdapter(subActivity, this, 2);
        }
        spinner.setAdapter(myAdapterSub);
        spinner.setOnItemSelectedListener(this);
    }

    private void setActivity(List<MyCategory> activity) {
        MyCategory act = new MyCategory();
        act.setName("Please select activity");
        act.setId("0");
        activity.add(0, act);
        Spinner spinner = (Spinner) findViewById(R.id.activity_spinner);


        if (edit) {
            myAdapterActivity = new MyAdapter(activity, this, 1, true, record.getActivityId());
        } else {
            myAdapterActivity = new MyAdapter(activity, this, 1);
        }
        spinner.setAdapter(myAdapterActivity);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            switch (parent.getId()) {
                case R.id.spinner:
                    category_id = view.getTag().toString();
                    break;
                case R.id.activity_spinner:
                    activity_id = view.getTag().toString();
                    try {
                        serviceTraining(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.sub_activity_spinner:
                    sub_activity_id = view.getTag().toString();
                    break;
                default:
                    break;

            }
        } else if (edit) {

            switch (parent.getId()) {
                case R.id.spinner:
                    myAdapterCategory.notifyDataSetChanged();
                    break;
                case R.id.activity_spinner:
                    myAdapterActivity.notifyDataSetChanged();
                    break;
                case R.id.sub_activity_spinner:
                    myAdapterSub.notifyDataSetChanged();
                    break;
                default:
                    break;

            }
        } else {
            switch (parent.getId()) {
                case R.id.spinner:
                    category_id = "";
                    break;
                case R.id.activity_spinner:
                    activity_id = "";
                    break;
                case R.id.sub_activity_spinner:
                    sub_activity_id = "";
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void pickTime() {

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final TimePickerDialogs time_dialog = new TimePickerDialogs(this, new TimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if (i1 % 15 == 0 || i1 % 15 == 1 || i1 % 15 == 2 || i1 % 15 == 3 || i1 % 15 == 4) {
                    time = String.valueOf((new StringBuilder().append(i).append(":")
                            .append(i1)));

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
                    Date dt;
                    try {
                        dt = sdf.parse(time);
                        txt_hours.setText(sdf.format(dt));
                    } catch (ParseException | java.text.ParseException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(CreateTrainingActivity.this, "Please select time in the multiple of 15", Toast.LENGTH_LONG).show();
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        time_dialog.show();


    }


    private void setRole(String role) {
        if (role != null) {
            RadioGroup radioGroup = findViewById(R.id.roles);
            switch (role) {
                case "Attendee":
                    radioGroup.check(R.id.atd);
                    break;
                case "Presenter":
                    radioGroup.check(R.id.pre);
                    break;
                case "Author":
                    radioGroup.check(R.id.auth);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.atd:
                role = ((RadioButton) findViewById(checkedId)).getText().toString();
                break;
            case R.id.pre:
                role = ((RadioButton) findViewById(checkedId)).getText().toString();
                break;
            case R.id.auth:
                role = ((RadioButton) findViewById(checkedId)).getText().toString();
                break;
            default:
                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            if (((CreateTrainingActivity) getActivity()).edit) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                try {
                    Date d = dateFormat.parse(((CreateTrainingActivity) getActivity()).record.getDate());
                    c.setTime(d);
                } catch (Exception e) {
                    //java.text.ParseException: Unparseable date: Geting error
                    System.out.println("Excep" + e);
                }
            }
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            if (((CreateTrainingActivity) getActivity()).edit) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                try {
                    Date d = dateFormat.parse(((CreateTrainingActivity) getActivity()).record.getDate());
                    c.setTime(d);
                } catch (Exception e) {
                    //java.text.ParseException: Unparseable date: Geting error
                    System.out.println("Excep" + e);
                }
            }
            DatePickerDialog d = new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
            d.getDatePicker().setMaxDate(System.currentTimeMillis());
            return d;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            final String[] MONTHS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

            String date = String.valueOf(day) + "/" + MONTHS[month] + "/" +
                    year + " ";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            try {
                Date d = dateFormat.parse(date);
                ((CreateTrainingActivity) getActivity()).txt_date.setText(dateFormat.format(d));
            } catch (Exception e) {
                //java.text.ParseException: Unparseable date: Geting error
                System.out.println("Excep" + e);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            path = data.getStringExtra("FilePath");
//            file_name = data.getStringExtra("FileName");
            file_name = MediaHelper.getMediaHelper().getFileName(path);
//            base64 = data.getStringExtra("Base64String");
            base64 = MediaHelper.getMediaHelper().encodeFileToBase64Binary(path);
            ((TextView) findViewById(R.id.txt_file_name)).setText(file_name);
            showImage(path);

        } else {

            Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean validate() {
        if (txt_date.getText().toString().isEmpty() || txt_date.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select date!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (category_id == null || category_id.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select category!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (activity_id == null || activity_id.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select activity!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinner.getVisibility() == View.VISIBLE && (sub_activity_id == null || sub_activity_id.trim().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Please select sub activity!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txt_hours.getText().toString().isEmpty() || txt_hours.getText().toString().trim().equalsIgnoreCase("") || txt_hours.getText().toString().trim().equalsIgnoreCase("0")) {
            Toast.makeText(this, "Please enter hour(s) & hour(s) can't be 0!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (provider.getText().toString().isEmpty() || provider.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter provider!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (role == null || role.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select role!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (description.getText().toString().isEmpty() || description.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter description!", Toast.LENGTH_SHORT).show();
            return false;
        }

//        else if (!edit && (base64 == null || base64.trim().equalsIgnoreCase(""))) {
//            Toast.makeText(this, "Please select a file to upload!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }


    private void showImage(String path) {
        network=false;
        if (path.contains("pdf")) {
            imageView.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromFile(new File(path))
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        } else {
            imageView.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            Picasso.get().load(new File(path)).into(imageView);
        }


    }

    private void showNetworkData(final String url) throws IOException {
        this.url=url;
        network=true;
        if (url.contains("pdf")) {
            imageView.setVisibility(View.GONE);
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
                                        .scrollHandle(new DefaultScrollHandle(CreateTrainingActivity.this))
                                        .load();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } else {
            imageView.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            Picasso.get().load(url).into(imageView);
        }


    }


    private void serviceGetTraining() throws Exception {
        ProgressBar.showDialog(CreateTrainingActivity.this, getString(R.string.please_wait), true, false).show();
        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call = null;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        SoapRequestData data = new SoapRequestData();
        data.setId(record.getId());


        body.setDetailTraining(data);
        envelope.setBody(body);
        call = service.training(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getBody() != null
                        && response.body().getBody().getDetailTrainingResponse() != null
                        && response.body().getBody().getDetailTrainingResponse().getDetailTrainingResult() != null
                        && response.body().getBody().getDetailTrainingResponse().getDetailTrainingResult().getStatus() != null) {


                    if (response.body().getBody().getDetailTrainingResponse().getDetailTrainingResult().getStatus().equalsIgnoreCase("SUCCESS")
                            && response.body().getBody().getDetailTrainingResponse().getDetailTrainingResult().getUserTrainings() != null) {


                        TrainingRecord record= response.body().getBody().getDetailTrainingResponse().getDetailTrainingResult().getUserTrainings().get(0);
                        try {
                            CreateTrainingActivity.this.file_name=record.getFileName();
                            ((TextView) findViewById(R.id.txt_file_name)).setText(file_name);
                            showNetworkData(record.getImageLink());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CreateTrainingActivity.this, response.body().getBody().getDetailTrainingResponse().getDetailTrainingResult().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CreateTrainingActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();

                }
                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {
                onBackPressed();
                Toast.makeText(CreateTrainingActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });


    }
}

