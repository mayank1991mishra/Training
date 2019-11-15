package com.lexnarro.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lexnarro.R;
import com.lexnarro.adapter.YearAdapter;
import com.lexnarro.databaseHandler.DatabaseHandler;
import com.lexnarro.databaseHandler.DatabaseInterface;
import com.lexnarro.fragment.Category;
import com.lexnarro.fragment.MyCategoryFragment;
import com.lexnarro.network.ApiInterface;
import com.lexnarro.network.ClientConnection;
import com.lexnarro.request.SoapBody;
import com.lexnarro.request.SoapEnvelop;
import com.lexnarro.request.SoapRequestData;
import com.lexnarro.response.FinancialYear;
import com.lexnarro.response.MyCategory;
import com.lexnarro.response.Profile;
import com.lexnarro.response.SoapResponseEnvelope;
import com.lexnarro.response.SoapResult;
import com.lexnarro.sharedprefrences.SPreferenceKey;
import com.lexnarro.sharedprefrences.SharedPreferenceWriter;
import com.lexnarro.util.DownloadOrEmail;
import com.lexnarro.util.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener, TabLayout.OnTabSelectedListener {

    TextView txtstatus;
    ViewPager pager;
    TabLayout tabs, tab_layout;
    private ArrayList<FinancialYear> fin_years;
    private String financial_year;
    private String year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtstatus = (TextView) findViewById(R.id.txtstatus);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tabs.addTab(tabs.newTab().setText("Download/Email").setIcon(R.drawable.download));
        tabs.addTab(tabs.newTab().setText("View").setIcon(R.drawable.view));
        tabs.addTab(tabs.newTab().setText("Join").setIcon(R.drawable.ic_join));
        View tabItem = ((ViewGroup) tabs.getChildAt(0)).getChildAt(2);
        tabItem.setEnabled(false);
        tabItem.setClickable(false);
        tabItem.setAlpha(0.1f);
        setTabDivider(tabs);


        tabs.addOnTabSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.viewpager_progress);


        tab_layout.setupWithViewPager(pager);
//        setupTabLayout();


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("POSITION", "" + position);

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected", "" + position);
//                callService(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        try {
            serviceDashboard(getFinancialYear());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabs = findViewById(R.id.tabs);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    private void setupViewPager(ViewPager viewPager, List<MyCategory> myCategory, double progress, List<MyCategory> myExCategory) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(MyCategoryFragment.newInstance(new ArrayList<MyCategory>(myCategory), progress, new ArrayList<MyCategory>(myExCategory)), "");

        viewPager.setOffscreenPageLimit(myCategory.size());
        for (int i = 0; i < myCategory.size(); i++) {
            adapter.addFrag(Category.newInstance(myExCategory.get(i)), "");
        }

        viewPager.setAdapter(adapter);
    }

    private void setYearSpinner(List<FinancialYear> years) {
        YearAdapter adapter = new YearAdapter(this, 0, years);
        Spinner spinYear = findViewById(R.id.yearspin);
        spinYear.setAdapter(adapter);
        spinYear.setOnItemSelectedListener(this);
    }


    private Profile getProfile() {
        DatabaseInterface databaseInterface = new DatabaseHandler(this);
        return databaseInterface.getProfile();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            transactActivity(CreateTrainingActivity.class, false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void logout(){
        SharedPreferenceWriter.getInstance(HomeActivity.this).writeBooleanValue(SPreferenceKey.LOGGEDIN, false);
        SharedPreferenceWriter.getInstance(HomeActivity.this).writeStringValue(SPreferenceKey.Email, "");
        new DatabaseHandler(this).clearAllTable();
        transactActivity(LoginActivity.class, true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                logout();
                break;
            case R.id.profile:
                transactActivity(ProfileActivity.class, false);
                break;

            case R.id.create_training:
                transactActivity(CreateTrainingActivity.class, false);
                break;
            case R.id.carry_over:
                transactActivity(CarryOverActivity.class, false);
                break;
            case R.id.record:
                transactActivity(ListRecordActivity.class, false);
                break;
            case R.id.transactions:
                transactActivity(ListTransactionActivity.class, false);
                break;
            case R.id.subscriptions:
                transactActivity(SubscribeActivity.class, false);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void transactActivity(Class Activity, boolean finish) {

        startActivity(new Intent(HomeActivity.this, Activity).putExtra("year", fin_years).putExtra("fin_year", financial_year));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        if (finish) {
            finishAffinity();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position != 0) {
            try {
                financial_year = fin_years.get(position).getValue();
                serviceDashboard(financial_year);

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
                downloadOrEmailDialog();
                break;
            case 1:
                transactActivity(ListRecordActivity.class, false);
                break;
            case 2:
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
                downloadOrEmailDialog();
                break;
            case 1:
                transactActivity(ListRecordActivity.class, false);
                break;
            case 2:
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
            DownloadOrEmail.getInstance().setCall(data, type, HomeActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void downloadOrEmailDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Download/Email");
        alertDialogBuilder.setMessage("Please select to download or email the record.");
        alertDialogBuilder.setPositiveButton("Download",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        downloadEmail(DownloadOrEmail.DOWNLOAD_RECORD);
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton("Email", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadEmail(DownloadOrEmail.EMAIL_RECORD);
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void serviceDashboard(String year) throws Exception {
        ProgressBar.showDialog(HomeActivity.this, getString(R.string.please_wait), true, false).show();

        SoapEnvelop envelope = new SoapEnvelop();
        SoapBody body = new SoapBody();
        final SoapRequestData data = new SoapRequestData();
        data.setEmailID(SharedPreferenceWriter.getInstance(HomeActivity.this).getString(SPreferenceKey.Email));
        data.setFinYear(year);
        body.setDashboard(data);
        envelope.setBody(body);

        ApiInterface service = ClientConnection.getInstance().createService();

        Call<SoapResponseEnvelope> call = service.dashboard(envelope);
        call.enqueue(new Callback<SoapResponseEnvelope>() {


            @Override
            public void onResponse(@NonNull Call<SoapResponseEnvelope> call, @NonNull Response<SoapResponseEnvelope> response) {
                if (response.isSuccessful()) {
                    SoapResult result = Objects.requireNonNull(response.body()).getBody().getTrainingResponse().getTrainingResult();

                    if (result!=null && result.getStatus() != null && result.getStatus().equalsIgnoreCase("Success")) {
                        int max = result.getMyCategory().size();
//                        Toast.makeText(HomeActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPreferenceWriter.getInstance(HomeActivity.this).writeBooleanValue(SPreferenceKey.RELOAD, false);


                        double totalUnits = 0.0;
                        for (int i = 0; i < max; i++) {
                            totalUnits = totalUnits + Double.parseDouble(result.getExistingCategory().get(i).getUnits_Done());
                        }
                        txtstatus.setText(String.format("%s%s", daysRemaining(), " " + getString(R.string.days_remaining)));
                        ((TextView) findViewById(R.id.txt_short_state)).setText(getProfile().getStateEnrolledShortName());

                        setupViewPager(pager, result.getMyCategory(), totalUnits, result.getExistingCategory());
                        fin_years = (ArrayList<FinancialYear>) result.getFinancialYears();
                        if (fin_years != null) {
                            FinancialYear f = new FinancialYear();
                            f.setDisabled("false");
                            f.setSelected("false");
                            f.setText("");
                            f.setValue("");
                            fin_years.add(0, f);
                            setYearSpinner(fin_years);
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        if (result.getMessage().contains("delete")){
                            logout();
                        }
                    }
                }

                ProgressBar.cancelDialog();

            }

            @Override
            public void onFailure(Call<SoapResponseEnvelope> call, Throwable t) {

                ProgressBar.cancelDialog();
                Snackbar snackbar = Snackbar
                        .make(tab_layout, "Something went wrong, Please try again", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            serviceDashboard(financial_year);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void onResume() {
        if (SharedPreferenceWriter.getInstance(this).getBoolean(SPreferenceKey.RELOAD)) {
            try {
                serviceDashboard(financial_year);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();
    }

    private String getFinancialYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        System.out.println("Financial month : " + month);
        if (month <= 3) {
            this.year = String.valueOf(year);
            return financial_year = (year - 1) + "-" + year;
        } else {
            this.year = String.valueOf(year + 1);
            return financial_year = year + "-" + (year + 1);
        }
    }


    private String daysRemaining() {

        String inputDateString = "31/03/" + year;
        Calendar cal = Calendar.getInstance();
        long dayNow = TimeUnit.MILLISECONDS.toDays(cal.getTimeInMillis());
        try {
            cal.setTime(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(inputDateString));
            cal.add(Calendar.HOUR_OF_DAY, 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dayDob = TimeUnit.MILLISECONDS.toDays(cal.getTimeInMillis());
        long ageInDays = dayDob - dayNow;
        return String.valueOf(ageInDays);

    }
}
