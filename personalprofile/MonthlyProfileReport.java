package au.org.ipdc.personalprofile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import au.org.ipdc.adapter.AdapterForAddedNewWork;
import au.org.ipdc.model.AllData;
import au.org.ipdc.model.DatabaseHelper;

public class MonthlyProfileReport extends AppCompatActivity implements android.view.View.OnClickListener {

    private Button bSaveMPR, bAddNewWorkMPR;
    private RadioButton rbBaitulmalIncreasYesMPR, rbBaitulmalIncreasNoMPR;
    private RadioGroup rbSelector;
    private EditText etIntroductoryDistributionMPR, etPatientCareMPR, etReadingBooksMPR,
            etMemberIncreaseMPR, etSocialWorkMPR, etGeneralDawahMPR,
            etSuggestionMPR;

    private static TextView tvEyanotPaymentDateMPR, tvDateANW;

    private DatabaseHelper dbHelper;
    private CheckBox cbPlanExistMD;
    private Toolbar mToolbar;
    private ImageButton ibNext, ibPrevious;
    private Calendar currentMonth, month;
    private String monthId = "";
    private HashMap<String, String> mProfileReport;

    private TextView tvTitle, tvProfileKeepingDays;
    private static  int number, nDay, nMonth, nYear;
    private static ListView lvAddedNewWorkMPR;
    private boolean isYes = true;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ac_monthly_profile_report);
        dbHelper = new DatabaseHelper(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Monthly Profile Report");

        initialize();
        setClickListener();
        refreshCalendar();
    }



    private void initialize() {
        //first part - 8
        etIntroductoryDistributionMPR = ((EditText) findViewById(R.id.etIntroductoryDistributionMPR));
        etReadingBooksMPR = ((EditText) findViewById(R.id.etReadingBooksMPR));
        etMemberIncreaseMPR = (EditText) findViewById(R.id.etMemberIncreaseMPR);

        tvEyanotPaymentDateMPR = ((TextView) findViewById(R.id.tvEyanotPaymentDateMPR));

        rbSelector = (RadioGroup) findViewById(R.id.rbSelectorMPR);
        rbBaitulmalIncreasYesMPR = ((RadioButton) rbSelector.findViewById(R.id.rbBaitulmalIncreasYesMPR));
        rbBaitulmalIncreasNoMPR = ((RadioButton) rbSelector.findViewById(R.id.rbBaitulmalIncreasNoMPR));

        etSocialWorkMPR = (EditText) findViewById(R.id.etSocialWorkMPR);
        etPatientCareMPR = ((EditText) findViewById(R.id.etPatientCareMPR));
        etGeneralDawahMPR = (EditText) findViewById(R.id.etGeneralDawahMPR);

        //last part - 1
        etSuggestionMPR = (EditText) findViewById(R.id.etSuggestionMPR);

        month = Calendar.getInstance();
        currentMonth = Calendar.getInstance();

        ibPrevious = (ImageButton) findViewById(R.id.ibPreviousMPR);
        ibNext = (ImageButton) findViewById(R.id.ibNextMPR);
        bSaveMPR = (Button) findViewById(R.id.bSaveMPR);
        bAddNewWorkMPR = (Button) findViewById(R.id.bAddNewWorkMPR);

        lvAddedNewWorkMPR = (ListView) findViewById(R.id.lvAddedNewWorkMPR);
    }



    private void setClickListener(){
        ibPrevious.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ibNext.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                if (setNextMonth()) {
                    refreshCalendar();
                }
            }
        });

        tvEyanotPaymentDateMPR.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                number = 1;
                showDatePickerDialog();
            }
        });

        bSaveMPR.setOnClickListener(this);
        bAddNewWorkMPR.setOnClickListener(this);

        rbSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbBaitulmalIncreasYesMPR: {
                        isYes = true;
                        break;
                    }
                    case R.id.rbBaitulmalIncreasNoMPR: {
                        isYes = false;
                        break;
                    }
                }//end of switch
            }
        }); //end of onCheckChangedListener
    }



    private void setValueInLayoutElement() {
        //first part -- 8
        etIntroductoryDistributionMPR.setText(mProfileReport.get(AllData.MPR_INTRO_DIST));
        etReadingBooksMPR.setText(mProfileReport.get(AllData.MPR_READING_BOOKS));
        etMemberIncreaseMPR.setText(mProfileReport.get(AllData.MPR_MEMBER_INCREASE));

        tvEyanotPaymentDateMPR.setText(mProfileReport.get(AllData.MPR_EYANOT_PAYMENT_DATE));
        etSocialWorkMPR.setText(mProfileReport.get(AllData.MPR_SOCIAL_WORK));
        etPatientCareMPR.setText(mProfileReport.get(AllData.MPR_PAITENT_CARE));
        etGeneralDawahMPR.setText(mProfileReport.get(AllData.MPR_GENERAL_DAWAH));

        if (mProfileReport.get(AllData.MPR_BAITULMAL_INCREASE) != null) {
            if (Integer.parseInt(mProfileReport.get(AllData.MPR_BAITULMAL_INCREASE)) == 1) {
                rbBaitulmalIncreasYesMPR.setChecked(true);

            } else if(Integer.parseInt(mProfileReport.get(AllData.MPR_BAITULMAL_INCREASE)) == 0){
                rbBaitulmalIncreasNoMPR.setChecked(true);
            }
        } else {
            rbBaitulmalIncreasYesMPR.setChecked(true);
        }
        //last part -- 1
        etSuggestionMPR.setText(mProfileReport.get(AllData.MPR_COMMENT_SUGEGSTION));
    }


    private void refreshCalendar() {
        monthId = DateFormat.format("MMMM yyyy", month).toString();
        mProfileReport = dbHelper.getAllContentFromTableProfileReport(monthId);

        tvTitle = (TextView) findViewById(R.id.tvMonthOfDairyMPR);
        tvProfileKeepingDays = (TextView) findViewById(R.id.tvProfileKeepingDaysMPR);
        cbPlanExistMD = (CheckBox) findViewById(R.id.cbPlanExistMPR);

        tvTitle.setText(monthId);
        int cbState = dbHelper.checkMonthlyPlanExistOrNot(monthId);
        if (cbState == 1) {
            cbPlanExistMD.setChecked(true);
        } else {
            cbPlanExistMD.setChecked(false);
        }
        tvProfileKeepingDays.setText("Total profile Keeping Days:  " + getMonthlyProfileKeepingDays(monthId));
        lvAddedNewWorkMPR.setAdapter(new AdapterForAddedNewWork(listUpdate(),
                MonthlyProfileReport.this));

        clearFocusFromEditText();
        setValueInLayoutElement();
        isButtonSaveOrUpdate();
    }



    public void onClick(View view) {

        switch(view.getId()){
            case R.id.bAddNewWorkMPR:{
                final Dialog dialog = new Dialog(MonthlyProfileReport.this);
                dialog.setContentView(R.layout.dl_add_new_work);
                dialog.setTitle("Include new work");

                tvDateANW = (TextView) dialog.findViewById(R.id.dl_tvDateANW);
                final EditText etProgramDescirptionANW = (EditText) dialog.findViewById(R.id.dl_etProgramDescirptionANW);
                final EditText etTimeANW = (EditText) dialog.findViewById(R.id.dl_etTimeANW);
                final EditText etUnitANW = (EditText) dialog.findViewById(R.id.dl_etUnitANW);
                final EditText etCommentANW = (EditText) dialog.findViewById(R.id.dl_etCommentANW);

                tvDateANW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = 2;
                        showDatePickerDialog();
                    }
                });


                Button bAddNewWorkANW = (Button) dialog.findViewById(R.id.dl_bAddNewWorkANW);
                bAddNewWorkANW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String , String> hashmap = new HashMap();
                        hashmap.put(AllData.MPR_MONTH_ID,              monthId);
                        hashmap.put(AllData.MPR_DL_DATE,               tvDateANW.getText().toString());
                        hashmap.put(AllData.MPR_DL_PR0G_DESCRIPTION,   etProgramDescirptionANW.getText().toString());
                        hashmap.put(AllData.MPR_DL_TIME,               etTimeANW.getText().toString());
                        hashmap.put(AllData.MPR_DL_UNIT,               etUnitANW.getText().toString());
                        hashmap.put(AllData.MPR_DL_COMMENT,            etCommentANW.getText().toString());

                        dbHelper.insertRowInTableWorkAddition(hashmap);
                        Toast.makeText(MonthlyProfileReport.this, "New work is successfully added !!",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        lvAddedNewWorkMPR.setAdapter(new AdapterForAddedNewWork(listUpdate(),
                                MonthlyProfileReport.this));
                    }
                });

                dialog.show();
                break;
            }

            case R.id.bSaveMPR:{
                mProfileReport = new HashMap<>();
                mProfileReport.put(AllData.MPR_MONTH_ID,            monthId);

                //first part -- 8
                mProfileReport.put(AllData.MPR_INTRO_DIST,          etIntroductoryDistributionMPR.getText().toString());
                mProfileReport.put(AllData.MPR_READING_BOOKS,       etReadingBooksMPR.getText().toString());
                mProfileReport.put(AllData.MPR_MEMBER_INCREASE,     etMemberIncreaseMPR.getText().toString());

                mProfileReport.put(AllData.MPR_EYANOT_PAYMENT_DATE, tvEyanotPaymentDateMPR.getText().toString());

                if(isYes){
                    mProfileReport.put(AllData.MPR_BAITULMAL_INCREASE,  "1");
                }else{
                    mProfileReport.put(AllData.MPR_BAITULMAL_INCREASE,  "0");
                }

                mProfileReport.put(AllData.MPR_SOCIAL_WORK,         etSocialWorkMPR.getText().toString());
                mProfileReport.put(AllData.MPR_PAITENT_CARE,        etPatientCareMPR.getText().toString());
                mProfileReport.put(AllData.MPR_GENERAL_DAWAH,       etGeneralDawahMPR.getText().toString());

                //last part -- 1
                mProfileReport.put(AllData.MPR_COMMENT_SUGEGSTION,  etSuggestionMPR.getText().toString());

                if (dbHelper.isThisMonthProfileReportExist(monthId) == 0) {
                    Toast.makeText(this, "Your report is saved successfully !!", Toast.LENGTH_SHORT).show();
                    dbHelper.insertRowInTableProfileReport(mProfileReport);
                    return;

                } else {
                    dbHelper.updateRowInTableProfileReport(mProfileReport);
                    Toast.makeText(this, "Your report is updated successfully !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }//end of case
        }
    }

    // ########## methods that frequently doesn't change ###############################
    private  ArrayList<HashMap<String, String>>  listUpdate(){
        ArrayList<HashMap<String, String>> myMap;
        myMap = dbHelper.getContentFromTableWorkAddition(monthId);
        return myMap;
    }

    public void isButtonSaveOrUpdate(){
        if (dbHelper.isThisMonthProfileReportExist(monthId) == 0) {
            bSaveMPR.setText("Save");

        }else{
            bSaveMPR.setText("Update");
        }
    }

    private boolean setNextMonth() {
        if (month.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR) &&
                month.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)) {

            Toast.makeText(getApplicationContext(), "You can't see next month's profile report!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set(month.get(Calendar.YEAR) + 1, month.getActualMinimum(Calendar.MONTH), 1);

        } else {
            month.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1, 1);
        }
        return true;
    }


    private void setPreviousMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set(month.get(Calendar.YEAR) - 1, month.getActualMaximum(Calendar.MONTH), 1);
            return;

        } else {
            month.add(Calendar.MONTH, -1);
            month.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), 1);
            return;
        }
    }



    public String getMonthlyProfileKeepingDays(String monthId) {
        ArrayList<Integer> list = dbHelper.getTotalNumberOfProfileKeepingInfoForMonth(monthId);
        return (list.size() + "");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().hasExtra("month_id")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
            try {
                Date date = dateFormat.parse(getIntent().getStringExtra("month_id"));
                month.setTime(date);
                refreshCalendar();

            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        }
    }



    private void clearFocusFromEditText() {
        etReadingBooksMPR.clearFocus();
        etMemberIncreaseMPR.clearFocus();
        etSocialWorkMPR.clearFocus();
        etPatientCareMPR.clearFocus();
        etGeneralDawahMPR.clearFocus();
        etSuggestionMPR.clearFocus();
    }


    //########################## methods for date picking ##########################33
    public void showDatePickerDialog() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            setDate(day, month, year);
        }
    }


    private static void setDate(int day, int month, int year) {
        nDay = day; nMonth = month + 1; nYear = year;
        String sDay = String.valueOf(nDay);
        String sMonth = String.valueOf(nMonth);

        Calendar cl = Calendar.getInstance();

        if (nDay < 10) {
            sDay = (new StringBuilder("0")).append(nDay).toString();
        }
        if (nMonth < 10) {
            sMonth = (new StringBuilder("0")).append(nMonth).toString();
        }

        String sDate = ((new StringBuilder(sDay)).append(" - ").append(sMonth).append(" - ").
                append(nYear)).toString();

        if(number == 1){
            tvEyanotPaymentDateMPR.setText(sDate);
        }else if(number == 2){
            tvDateANW.setText(sDate);
        }

    }

}

