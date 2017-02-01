package au.org.ipdc.personalprofile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import au.org.ipdc.adapter.NavigationDrawerCallbacks;
import au.org.ipdc.model.DatabaseHelper;
import au.org.ipdc.model.AllData;


public class DailyProfile extends AppCompatActivity implements NavigationDrawerCallbacks, View.OnClickListener {

    private EditText etQuranReciteDP, etQuranStudyDP, etHadithStudyDP, etIslamiLiteratureStudyDP,
                     etNamajJamaatDP, etFriendDawahDP, etTargetedWorkerContactDP,
                     etWorkerContactDP, etBookDistributionDP, etTimeDedicationDP, etGeneralContactDP,
                     etSuggestionDP;

    private CheckBox cbFamilyMeetingDP, cbSelfCriticismDP;

    private TextView tvTitle;
    private ImageButton ibNext, ibPrevious;
    private Button bSave;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private SharedPreferences profile;
    private Toolbar mToolbar;
    private DatabaseHelper dbHelper;

    private Calendar today, month;
    private String dayId = "", monthId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_daily_profile);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Daily Profile");

        profile = getSharedPreferences("profile", 0);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mNavigationDrawerFragment.setUserData(profile.getString("user_name", ""),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));


        dbHelper = new DatabaseHelper(this);
        today = Calendar.getInstance();
        month = Calendar.getInstance();

        initialize();


        ibPrevious.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                setPreviousDay();
                refreshCalendar();
            }

        });
        ibNext.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                if (setNextDay()) {
                    refreshCalendar();
                }
            }

        });
        bSave.setOnClickListener(this);

        refreshCalendar();
    }


    private void initialize() {

        //study -- 4
        etQuranReciteDP = (EditText) findViewById(R.id.etQuranReciteDP);
        etQuranStudyDP = (EditText) findViewById(R.id.etQuranStudyDP);
        etHadithStudyDP = (EditText) findViewById(R.id.etHadithStudyDP);
        etIslamiLiteratureStudyDP = (EditText) findViewById(R.id.etIslamiLiteratureStudyDP);

        //Namaj - 1
        etNamajJamaatDP = (EditText) findViewById(R.id.etNamajJamaatDP);

        //contact -- 6
        etFriendDawahDP = (EditText) findViewById(R.id.etFriendDawahDP);
        etTargetedWorkerContactDP = (EditText) findViewById(R.id.etTargetedWorkerContactDP);
        etWorkerContactDP = (EditText) findViewById(R.id.etWorkerContactDP);
        etBookDistributionDP = (EditText) findViewById(R.id.etBookDistributionDP);
        etTimeDedicationDP = (EditText) findViewById(R.id.etTimeDedicationDP);
        etGeneralContactDP = (EditText) findViewById(R.id.etGeneralContactDP);

        //miscellaneous -- 3
        cbFamilyMeetingDP = ((CheckBox) findViewById(R.id.cbFamilyMeetingDP));
        cbSelfCriticismDP = ((CheckBox) findViewById(R.id.cbSelfCriticismDP));
        etSuggestionDP = ((EditText) findViewById(R.id.etSuggestionDP));

        //others method
        ibPrevious = (ImageButton) findViewById(R.id.ibPreviousDP);
        ibNext = (ImageButton) findViewById(R.id.ibNextDP);
        bSave = (Button) findViewById(R.id.bSaveDP);
    }


    private void setValueInLayoutElement() {
        HashMap<String, String> hashmap = dbHelper.getDailyProfileFromDatabase(dayId);
        int cbState = 0;

        //study -- 4
        etQuranReciteDP.setText(hashmap.get(AllData.DP_QURAN_RECITE));
        etQuranStudyDP.setText(hashmap.get(AllData.DP_QURAN_STUDY));
        etHadithStudyDP.setText(hashmap.get(AllData.DP_HADITH_STUDY));
        etIslamiLiteratureStudyDP.setText(hashmap.get(AllData.DP_ISLAMI_LITERATURE_STUDY));

        //namaj -- 1
        etNamajJamaatDP.setText(hashmap.get(AllData.DP_NAMAJ_JAMAAT));

        //contact -- 6
        etFriendDawahDP.setText(hashmap.get(AllData.DP_FRIEND_DAWAH));
        etTargetedWorkerContactDP.setText(hashmap.get(AllData.DP_TARGETED_WORKER_CONTACT));
        etWorkerContactDP.setText(hashmap.get(AllData.DP_WORKER_CONTACT));
        etBookDistributionDP.setText(hashmap.get(AllData.DP_BOOK_DISTRIBUTION));
        etTimeDedicationDP.setText(hashmap.get(AllData.DP_TIME_DEDICATION));
        etGeneralContactDP.setText(hashmap.get(AllData.DP_GENERAL_CONTACT));

        //miscellaneous -- 3
        if (hashmap.get(AllData.DP_FAMILY_MEETING) != null) {
            cbState = Integer.parseInt(hashmap.get(AllData.DP_FAMILY_MEETING));
            if (cbState == 1) {
                cbFamilyMeetingDP.setChecked(true);
            } else {
                cbFamilyMeetingDP.setChecked(false);
            }
        } else {
            cbFamilyMeetingDP.setChecked(false);
        }

        if (hashmap.get(AllData.DP_SELF_CRITICISM) != null) {
            cbState = Integer.parseInt(hashmap.get(AllData.DP_SELF_CRITICISM));
            if (cbState == 1) {
                cbSelfCriticismDP.setChecked(true);
            } else {
                cbSelfCriticismDP.setChecked(false);
            }
        } else {
            cbSelfCriticismDP.setChecked(false);
        }

        etSuggestionDP.setText(hashmap.get(AllData.DP_SUGGESTION));
    }
    


    private void clearFocusFromAllEditext(){
        //etQuranReciteDP.clearFocus();
        etQuranStudyDP.clearFocus();
        etHadithStudyDP.clearFocus();
        etIslamiLiteratureStudyDP.clearFocus();
        etNamajJamaatDP.clearFocus();
        etFriendDawahDP.clearFocus();
        etTargetedWorkerContactDP.clearFocus();
        etWorkerContactDP.clearFocus();
        etBookDistributionDP.clearFocus();
        etTimeDedicationDP.clearFocus();
        etGeneralContactDP.clearFocus();
        etSuggestionDP.clearFocus();
    }



    private void refreshCalendar() {
        tvTitle = (TextView) findViewById(R.id.tvTitleDP);
        dayId = DateFormat.format("EE, dd MMMM yyyy", month).toString();
        monthId = DateFormat.format("MMMM yyyy", month).toString();

        tvTitle.setText(dayId);
        setValueInLayoutElement();
        clearFocusFromAllEditext();
        isButtonSaveOrUpdate();
    }



    private boolean setNextDay() {
        if (month.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                month.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                month.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {

            Toast.makeText(this, "You can't keep next day's profile !", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (month.get(Calendar.DAY_OF_MONTH) == month.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
                month.set(month.get(Calendar.YEAR) + 1, month.getActualMinimum(Calendar.MONTH), 1);

            } else {
                month.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1, 1);
            }
        } else {
            month.add(Calendar.DAY_OF_MONTH, 1);
        }
        return true;
    }



    private void setPreviousDay() {
        if (month.get(Calendar.DAY_OF_MONTH) == month.getActualMinimum(Calendar.DAY_OF_MONTH)) {
            if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
                month.set(month.get(Calendar.YEAR) - 1, month.getActualMaximum(Calendar.MONTH), 31);
                return;

            } else {
                month.add(Calendar.MONTH, -1);
                month.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), month.getActualMaximum(Calendar.DAY_OF_MONTH));
                return;
            }
        } else {
            month.add(Calendar.DAY_OF_MONTH, -1);
            return;
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0: {
                startActivity(new Intent(this, MonthlyProfile.class));
                break;
            }
            case 1: {
                startActivity(new Intent(this, MonthlyProfileReport.class));
                break;
            }
            case 2: {
                startActivity(new Intent(this, MonthlyProfilePlan.class));
                break;
            }
            case 3: {
                startActivity(new Intent(this, Settings.class));
                break;
            }

            case 4: {
                startActivity(new Intent(this, About.class));
                break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Exit Personal Profile");
            builder.setMessage("Are you sure to exit ?");
            builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    dbHelper.close();
                    finish();
                }
            });

            builder.setNegativeButton("No", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                }
            });
            builder.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }*/
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        HashMap<String, String> dailyProfile = new HashMap<>();
        dailyProfile.put("day_id", dayId);
        dailyProfile.put("month_id", monthId);

        //study -- 4
        dailyProfile.put(AllData.DP_QURAN_RECITE,               etQuranReciteDP.getText().toString());
        dailyProfile.put(AllData.DP_QURAN_STUDY,                etQuranStudyDP.getText().toString());
        dailyProfile.put(AllData.DP_HADITH_STUDY,               etHadithStudyDP.getText().toString());
        dailyProfile.put(AllData.DP_ISLAMI_LITERATURE_STUDY,    etIslamiLiteratureStudyDP.getText().toString());

        //namaj -- 1
        dailyProfile.put(AllData.DP_NAMAJ_JAMAAT,               etNamajJamaatDP.getText().toString());

        //contact -- 6
        dailyProfile.put(AllData.DP_FRIEND_DAWAH,               etFriendDawahDP.getText().toString());
        dailyProfile.put(AllData.DP_TARGETED_WORKER_CONTACT,    etTargetedWorkerContactDP.getText().toString());
        dailyProfile.put(AllData.DP_WORKER_CONTACT,             etWorkerContactDP.getText().toString());
        dailyProfile.put(AllData.DP_BOOK_DISTRIBUTION,          etBookDistributionDP.getText().toString());
        dailyProfile.put(AllData.DP_TIME_DEDICATION,            etTimeDedicationDP.getText().toString());
        dailyProfile.put(AllData.DP_GENERAL_CONTACT,            etGeneralContactDP.getText().toString());

        //miscellaneous  -- 3
        if (cbFamilyMeetingDP.isChecked()) {
            dailyProfile.put(AllData.DP_FAMILY_MEETING, "1");
        } else {
            dailyProfile.put(AllData.DP_FAMILY_MEETING, "0");
        }

        if (cbSelfCriticismDP.isChecked()) {
            dailyProfile.put(AllData.DP_SELF_CRITICISM, "1");

        } else {
            dailyProfile.put(AllData.DP_SELF_CRITICISM, "0");
        }

        dailyProfile.put(AllData.DP_SUGGESTION,         etSuggestionDP.getText().toString());

        if (dbHelper.getIdFromDayId(dayId) == 0) {
            Toast.makeText(this, "Your profile is saved", Toast.LENGTH_SHORT).show();
            dbHelper.insertRowInTableDailyProfile(dailyProfile);
            return;

        } else {
            dailyProfile.put("_id", String.valueOf(dbHelper.getIdFromDayId(dayId)));
            Toast.makeText(this, "Your profile is updated", Toast.LENGTH_SHORT).show();
            dbHelper.updateRowInTableDailyProfile(dailyProfile);
            return;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().hasExtra("day_id")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE, dd MMMM yyyy");
            try {
                Date date = dateFormat.parse(getIntent().getStringExtra("day_id"));
                month.setTime(date);
                refreshCalendar();

            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        }
    }



    public void isButtonSaveOrUpdate(){
        if (dbHelper.getIdFromDayId(monthId) == 0) {
            bSave.setText("Save");

        }else{
            bSave.setText("Update");
        }
    }
}
