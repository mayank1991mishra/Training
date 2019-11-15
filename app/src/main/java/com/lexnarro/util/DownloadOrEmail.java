package com.lexnarro.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.SoapResult;

import java.io.File;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadOrEmail {
    private static DownloadOrEmail context;

    public static final String DOWNLOAD_INVOICE = "download_invoice";
    public static final String DOWNLOAD_RECORD = "download_record";
    public static final String EMAIL_INVOICE = "email_invoice";
    public static final String EMAIL_RECORD = "email_record";
    private DownloadManager.Request request;
    private String fileName;

    private DownloadOrEmail() {

    }

    public static synchronized DownloadOrEmail getInstance() {
        if (context == null) {
            context = new DownloadOrEmail();
            return context;
        }
        return context;
    }

    public void setCall(SoapRequestData data, String type, Activity activity) throws Exception {
        ProgressBar.showDialog(activity, activity.getString(R.string.please_wait), true, false).show();
        ApiInterface service = ClientConnection.getInstance().createService();
        Call<SoapResponseEnvelope> call;
        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        switch (type) {
            case "email_invoice":
                body.setEmailInvoice(data);
                envelope.setBody(body);
                call = service.downloadAndEmail(envelope);
                serviceDownloadEmailInvoiceRecord(call, type, activity);
                break;
            case "email_record":
                body.setEmailTrainingReport(data);
                envelope.setBody(body);
                call = service.downloadAndEmail(envelope);
                serviceDownloadEmailInvoiceRecord(call, type, activity);
                break;
            case "download_invoice":
                body.setDownloadInvoice(data);
                envelope.setBody(body);
                call = service.downloadAndEmail(envelope);
                serviceDownloadEmailInvoiceRecord(call, type, activity);
                break;
            case "download_record":
                body.setDownloadTrainingReport(data);
                envelope.setBody(body);
                call = service.downloadAndEmail(envelope);
                serviceDownloadEmailInvoiceRecord(call, type, activity);
                break;
            default:
                break;
        }

    }


    private void getCall(String type, Response<SoapResponseEnvelope> response, Activity activity) {
        String status;
        String message;
        SoapResult result;
        switch (type) {
            case "email_record":

                try {
                    result = Objects.requireNonNull(response.body()).getBody().getEmailTrainingReportResponse().getEmailTrainingReportResult();
                    status = result.getStatus();
                    message = result.getMessage();
                } catch (NullPointerException e) {
                    status = null;
                    message = "Please try again!";
                }


                if (status != null && status.equalsIgnoreCase("SUCCESS")) {

                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                } else if (status != null && status.equalsIgnoreCase("FAILURE")) {

                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Please try again!", Toast.LENGTH_SHORT).show();
                }


                break;

            case "email_invoice":

                try {
                    result = Objects.requireNonNull(response.body()).getBody().getEmailInvoiceResponse().getEmailInvoiceResult();
                    status = result.getStatus();
                    message = result.getMessage();
                } catch (NullPointerException e) {
                    status = null;
                    message = "Please try again!";
                }


                if (status != null && status.equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();


                } else if (status != null && status.equalsIgnoreCase("FAILURE")) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Please try again!", Toast.LENGTH_SHORT).show();
                }


                break;
            case "download_record":

                try {
                    result = Objects.requireNonNull(response.body()).getBody().getDownloadTrainingReportResponse().getDownloadTrainingReportResult();
                    status = result.getStatus();
                    message = result.getMessage();
                } catch (NullPointerException e) {
                    status = null;
                    message = "Please try again!";
                    result=null;
                }
                if (result!=null &&status != null && status.equalsIgnoreCase("SUCCESS")) {
                    pdfDownload(result.getDocumentUrl(),activity);
                } else if (status!=null &&status.equalsIgnoreCase("FAILURE")) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Please try again!", Toast.LENGTH_SHORT).show();
                }


                break;

            case "download_invoice":

                try {
                    result = Objects.requireNonNull(response.body()).getBody().getDownloadInvoiceResponse().getDownloadInvoiceResult();
                    status = result.getStatus();
                    message = result.getMessage();
                } catch (NullPointerException e) {
                    status = null;
                    message = "Please try again!";
                    result=null;
                }
                if (result!=null &&status != null && status.equalsIgnoreCase("SUCCESS")) {
                    pdfDownload(result.getDocumentUrl(),activity);
                } else if (status != null && status.equalsIgnoreCase("FAILURE")) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Please try again!", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }

    }


    private void serviceDownloadEmailInvoiceRecord(Call<SoapResponseEnvelope> call, final String type, final Activity activity) {
        call.enqueue(new Callback<SoapResponseEnvelope>() {
            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()) {
                    ProgressBar.cancelDialog();
                    getCall(type, response, activity);
                } else {
                    Toast.makeText(activity, "Please try again!", Toast.LENGTH_SHORT).show();
                    ProgressBar.cancelDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SoapResponseEnvelope> call, @NonNull Throwable t) {
                Toast.makeText(activity, "Please try again!", Toast.LENGTH_SHORT).show();
                ProgressBar.cancelDialog();
            }
        });
    }


    private void pdfDownload(String url,Activity activity){
        request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Lexnarro mobile App");
        Log.e("URL",url);

// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        request.setTitle(fileName);
        String fname = "/Lexnarro/" + fileName;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + fname);
        final Uri uri = Uri.fromFile(file);

        if (!file.exists()) {

           downloadPDF(activity);
        }else
        {
            Toast.makeText(activity, "Your file already been downloaded!", Toast.LENGTH_LONG).show();
        }
    }
    private void downloadPDF(Activity activity) {
        fileName=fileName.replace("%20"," ");
        request.setDestinationInExternalPublicDir("/Lexnarro/", fileName);
        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Toast.makeText(activity, "Your file has been downloaded to folder Lexnarro.", Toast.LENGTH_LONG).show();
        manager.enqueue(request);
    }
}
