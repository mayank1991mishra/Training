package com.lexnarro.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lexnarro.R;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.TransactionRecord;
import com.lexnarro.util.DownloadOrEmail;

public class TransactionDetailActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {


    private TabLayout tabs;
    private TransactionRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        Toolbar toolbar = findViewById(R.id.userTrans_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(R.string.transaction_details);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Download").setIcon(R.drawable.download));
        tabs.addTab(tabs.newTab().setText("Email").setIcon(R.drawable.email));
        tabs.addOnTabSelectedListener(this);
        setTabDivider(tabs);
        inIt();
    }

    private void inIt() {
        if (getIntent() != null && getIntent().getSerializableExtra("record") != null) {
             record = (TransactionRecord) getIntent().getSerializableExtra("record");

            ((TextView) findViewById(R.id.txt_first_name)).setText(record.getFirstName());
            ((TextView) findViewById(R.id.txt_plan)).setText(record.getPlanName());
            ((TextView) findViewById(R.id.txt_amount)).setText(record.getAmount());
            ((TextView) findViewById(R.id.txt_s_date)).setText(record.getStart_Date());
            ((TextView) findViewById(R.id.txt_e_date)).setText(record.getEnd_Date());
            ((TextView) findViewById(R.id.txt_p_status)).setText(record.getPayment_Status());
            ((TextView) findViewById(R.id.txt_t_id)).setText(record.getTransection_ID());
            ((TextView) findViewById(R.id.txt_status)).setText(record.getStatus());
            ((TextView) findViewById(R.id.txt_payment_date)).setText(record.getPayment_Date());
            ((TextView) findViewById(R.id.txt_invoice)).setText(record.getInvoice_No());
            ((TextView) findViewById(R.id.txt_p_method)).setText(record.getPayment_Method());
            ((TextView) findViewById(R.id.txt_r_card)).setText(record.getRate_ID());
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                downloadEmailInvoice(DownloadOrEmail.DOWNLOAD_INVOICE);
                break;
            case 1:
                downloadEmailInvoice(DownloadOrEmail.EMAIL_INVOICE);
                break;
            default:
                break;
        }
        tabs.setSelected(false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:
                downloadEmailInvoice(DownloadOrEmail.DOWNLOAD_INVOICE);
                break;
            case 1:
                downloadEmailInvoice(DownloadOrEmail.EMAIL_INVOICE);
                break;
            default:
                break;
        }
    }

    private void setTabDivider(TabLayout tabs){

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

    private void downloadEmailInvoice(String type) {
        SoapRequestData data = new SoapRequestData();
        data.setInvoiceId(record.getId());
        data.setUser_Id(record.getUser_ID());
        try {
            DownloadOrEmail.getInstance().setCall(data, type, TransactionDetailActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
