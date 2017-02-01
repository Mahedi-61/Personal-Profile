package au.org.ipdc.personalprofile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import au.org.ipdc.adapter.AdapterForAddedNewPeople;
import au.org.ipdc.model.AllData;
import au.org.ipdc.model.DatabaseHelper;
import au.org.ipdc.model.TextViewWithBanglaFont;

public class MonthlyProfilePlan extends AppCompatActivity implements View.OnClickListener {

    private EditText
            etQuranReciteMPP, etQuranStudyMPP, etHadithStudyMPP, etIslamiLiteratureStudyMPP,
            etDarsulQuranNoteMPP, etDarsulHadithNoteMPP, etTopicBasedBookNoteMPP, etMemorizeAyatMPP,
            etMemorizeHadithMPP,
            etWorkerContactMPP, etBookDistributionMPP, etTimeDedicationMPP, etGeneralContactMPP,
            etTotalJamaatMPP, etFamilyMeetingMPP, etSelfCriticismMPP;

    private ImageButton ibNext, ibPrevious;
    private Button bSaveMPP, bAddNewDawahFriendMPP, bAddNewWorkerFriendMPP;
    private TextView tvTitle;
    private static TextView dl_etAdditionDateMPP, dl_etMeetingDateMPP;
    private Toolbar mToolbar;
    private static ListView  lvAddedDawahFriedMPP, lvAddedWorkerFriedMPP;

    private Calendar month, currentMonth;
    private String monthId = "";
    private static int nDay, nMonth, nYear;

    private static int number;
    private DatabaseHelper dbHelper;
    private HashMap<String, String> monthlyPlan;



    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ac_monthly_profile_plan);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Monthly Plan");

        month = Calendar.getInstance();
        currentMonth = Calendar.getInstance();
        dbHelper = new DatabaseHelper(this);
        initialize();
        setClickListener();
        refreshCalendar();
    }


    private void initialize(){
        //study -- 9
        etQuranReciteMPP = ((EditText) findViewById(R.id.etQuranReciteMPP));
        etQuranStudyMPP = ((EditText) findViewById(R.id.etQuranStudyMPP));
        etHadithStudyMPP = ((EditText) findViewById(R.id.etHadithStudyMPP));
        etIslamiLiteratureStudyMPP = ((EditText) findViewById(R.id.etIslamiLiteratureStudyMPP));

        etDarsulQuranNoteMPP = ((EditText) findViewById(R.id.etDarsulQuranNoteMPP));
        etDarsulHadithNoteMPP = ((EditText) findViewById(R.id.etDarsulHadithNoteMPP));
        etTopicBasedBookNoteMPP = ((EditText) findViewById(R.id.etTopicBasedBookNoteMPP));
        etMemorizeAyatMPP = ((EditText) findViewById(R.id.etMemorizeAyatMPP));
        etMemorizeHadithMPP = ((EditText) findViewById(R.id.etMemorizeHadithMPP));

        //namaj - 1
        etTotalJamaatMPP = (EditText) findViewById(R.id.etTotalJamaatMPP);

        //contact -- 4
        etWorkerContactMPP = ((EditText) findViewById(R.id.etWorkerContactMPP));
        etBookDistributionMPP = ((EditText) findViewById(R.id.etBookDistributionMPP));
        etTimeDedicationMPP = ((EditText) findViewById(R.id.etTimeDedicationMPP));
        etGeneralContactMPP = ((EditText) findViewById(R.id.etGeneralContactMPP));

        //miscellaneous - 2
        etFamilyMeetingMPP = ((EditText) findViewById(R.id.etFamilyMeetingMPP));
        etSelfCriticismMPP = ((EditText) findViewById(R.id.etSelfCriticismMPP));

        //add new
        bAddNewDawahFriendMPP = (Button) findViewById(R.id.bAddNewDawahFriendMPP);
        lvAddedDawahFriedMPP = (ListView) findViewById(R.id.lvAddedDawahFriedMPP);
        bAddNewWorkerFriendMPP = (Button) findViewById(R.id.bAddNewWorkerFriendMPP);
        lvAddedWorkerFriedMPP = (ListView) findViewById(R.id.lvAddedWorkerFriedMPP);

        //others
        ibPrevious = (ImageButton) findViewById(R.id.ibPreviousMPP);
        ibNext = (ImageButton) findViewById(R.id.ibNextMPP);
        bSaveMPP = (Button) findViewById(R.id.bSaveMPP);
    }



    private void setClickListener(){
        ibPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setPreviousMonth();
                refreshCalendar();
            }
        });


        ibNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setNextMonth();
                refreshCalendar();
            }
        });

        bSaveMPP.setOnClickListener(this);
        bAddNewDawahFriendMPP.setOnClickListener(this);
        bAddNewWorkerFriendMPP.setOnClickListener(this);
    }



    private void setValueInLayoutElement() {
        //study -- 9
        etQuranReciteMPP.setText(monthlyPlan.get(AllData.MPP_QURAN_RECITE));
        etQuranStudyMPP.setText(monthlyPlan.get(AllData.MPP_QURAN_STUDY));
        etHadithStudyMPP.setText(monthlyPlan.get(AllData.MPP_HADITH_STUDY));
        etIslamiLiteratureStudyMPP.setText(monthlyPlan.get(AllData.MPP_ISLAMI_LITERATURE_STUDY));

        etDarsulQuranNoteMPP.setText(monthlyPlan.get(AllData.MPP_DARSUL_QURAN_NOTE));
        etDarsulHadithNoteMPP.setText(monthlyPlan.get(AllData.MPP_DARSUL_HADITH_NOTE));
        etTopicBasedBookNoteMPP.setText(monthlyPlan.get(AllData.MPP_TOPIC_BASED_BOOK_NOTE));
        etMemorizeAyatMPP.setText(monthlyPlan.get(AllData.MPP_MEMORIZE_AYAT));
        etMemorizeHadithMPP.setText(monthlyPlan.get(AllData.MPP_MEMORIZE_HADITH));

        //namaj -- 1
        etTotalJamaatMPP.setText(monthlyPlan.get(AllData.MPP_NAMAJ_JAMAAT));

        //contact -- 4
        etWorkerContactMPP.setText(monthlyPlan.get(AllData.MPP_WORKER_CONTACT));
        etBookDistributionMPP.setText(monthlyPlan.get(AllData.MPP_BOOK_DISTRIBUTION));
        etTimeDedicationMPP.setText(monthlyPlan.get(AllData.MPP_TIME_DEDICATION));
        etGeneralContactMPP.setText(monthlyPlan.get(AllData.MPP_GENERAL_CONTACT));

        //miscellaneous - 2
        etFamilyMeetingMPP.setText(monthlyPlan.get(AllData.MPP_FAMILY_MEETING));
        etSelfCriticismMPP.setText(monthlyPlan.get(AllData.MPP_SELF_CRITICISM));
    }



    //each time month changed whole plan is re-setup
    private void refreshCalendar() {
        tvTitle = (TextView) findViewById(R.id.tvMonthOfPlanMPP);
        monthId = DateFormat.format("MMMM yyyy", month).toString();
        tvTitle.setText(monthId);
        monthlyPlan = dbHelper.getAllContentFromTableMonthlyPlan(monthId);

        makeAllElementClear();
        setValueInLayoutElement();
        bSaveOrUpdate();

        //for previous month edit and save option is disabled
        if (month.get(Calendar.YEAR) >= currentMonth.get(Calendar.YEAR)){
            if( month.get(Calendar.MONTH)  >= currentMonth.get(Calendar.MONTH)){
                makeAllElementsEnabledOrDisabled(true);

            }else{
                makeAllElementsEnabledOrDisabled(false);
            }
        }else{
            makeAllElementsEnabledOrDisabled(false);
        }

        lvAddedDawahFriedMPP.setAdapter(new AdapterForAddedNewPeople(listUpdate("1"), MonthlyProfilePlan.this));
        lvAddedWorkerFriedMPP.setAdapter( new AdapterForAddedNewPeople(listUpdate("2"), MonthlyProfilePlan.this));
    }



    public void onClick(View view) {

        switch(view.getId()){

            case R.id.bAddNewDawahFriendMPP:{
                final Dialog dialog = new Dialog(MonthlyProfilePlan.this);
                dialog.setContentView(R.layout.dl_add_new_friend);
                dialog.setTitle("Include new friend");

                final EditText dl_etFriendNameMPP = (EditText) dialog.findViewById(R.id.dl_etFriendNameMPP);
                dl_etAdditionDateMPP = (TextView) dialog.findViewById(R.id.dl_etAdditionDateMPP);
                dl_etMeetingDateMPP = (TextView) dialog.findViewById(R.id.dl_etMeetingDateMPP);


                dl_etAdditionDateMPP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = 1;
                        showDatePickerDialog();
                    }
                });

                dl_etMeetingDateMPP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = 2;
                        showDatePickerDialog();
                    }
                });

                Button dl_bAddNewFriendMPP = (Button) dialog.findViewById(R.id.dl_bAddNewFriendMPP);
                dl_bAddNewFriendMPP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String , String> hashmap = new HashMap();
                        hashmap.put(AllData.MPP_MONTH_ID,               monthId);
                        hashmap.put(AllData.MPP_DL_FRIEND_NAME,         dl_etFriendNameMPP.getText().toString());
                        hashmap.put(AllData.MPP_DL_ADDITION_DATE,       dl_etAdditionDateMPP.getText().toString());
                        hashmap.put(AllData.MPP_DL_MEETING_DATE,        dl_etMeetingDateMPP.getText().toString());
                        hashmap.put(AllData.MPP_DL_FRIEND_TYPE,         "1");  //1 = dawah friend

                        dbHelper.insertRowInTablePeopleAddition(hashmap);
                        Toast.makeText(MonthlyProfilePlan.this, "New friend is successfully added !!",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        lvAddedDawahFriedMPP.setAdapter(new AdapterForAddedNewPeople(listUpdate("1"), MonthlyProfilePlan.this));
                    }
                });

                dialog.show();
                break;
            }

            case R.id.bAddNewWorkerFriendMPP:{
                final Dialog dialog = new Dialog(MonthlyProfilePlan.this);
                dialog.setContentView(R.layout.dl_add_new_friend);
                dialog.setTitle("Include new friend");

                TextViewWithBanglaFont tvFriendName = (TextViewWithBanglaFont) dialog.findViewById(R.id.dl_tvFriendNameMPP);
                tvFriendName.setText("কর্মী টার্গেট (নাম )");

                TextViewWithBanglaFont dl_tvAdditionDateMPP = (TextViewWithBanglaFont) dialog.findViewById(R.id.dl_tvAdditionDateMPP);
                dl_tvAdditionDateMPP.setText("টার্গেটভুক্তির তারিখ");

                final EditText dl_etFriendNameMPP = (EditText) dialog.findViewById(R.id.dl_etFriendNameMPP);
                dl_etAdditionDateMPP = (TextView) dialog.findViewById(R.id.dl_etAdditionDateMPP);
                dl_etMeetingDateMPP = (TextView) dialog.findViewById(R.id.dl_etMeetingDateMPP);


                dl_etAdditionDateMPP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = 1;
                        showDatePickerDialog();
                    }
                });

                dl_etMeetingDateMPP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = 2;
                        showDatePickerDialog();
                    }
                });

                Button dl_bAddNewFriendMPP = (Button) dialog.findViewById(R.id.dl_bAddNewFriendMPP);
                dl_bAddNewFriendMPP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String , String> hashmap = new HashMap();
                        hashmap.put(AllData.MPP_MONTH_ID,               monthId);
                        hashmap.put(AllData.MPP_DL_FRIEND_NAME,         dl_etFriendNameMPP.getText().toString());
                        hashmap.put(AllData.MPP_DL_ADDITION_DATE,       dl_etAdditionDateMPP.getText().toString());
                        hashmap.put(AllData.MPP_DL_MEETING_DATE,        dl_etMeetingDateMPP.getText().toString());
                        hashmap.put(AllData.MPP_DL_FRIEND_TYPE,         "2");  //2 = worker target

                        Toast.makeText(MonthlyProfilePlan.this, "New target is successfully added !!",
                                Toast.LENGTH_SHORT).show();
                        dbHelper.insertRowInTablePeopleAddition(hashmap);
                        dialog.dismiss();
                        lvAddedWorkerFriedMPP.setAdapter(new AdapterForAddedNewPeople(listUpdate("2"), MonthlyProfilePlan.this));
                    }
                });
                dialog.show();
                break;
            }

            case R.id.bSaveMPP:{
                HashMap<String , String> hashmap = new HashMap();
                hashmap.put(AllData.MPP_MONTH_ID,                      monthId);

                hashmap.put(AllData.MPP_QURAN_RECITE,                  etQuranReciteMPP.getText().toString());
                hashmap.put(AllData.MPP_QURAN_STUDY,                   etQuranStudyMPP.getText().toString());
                hashmap.put(AllData.MPP_HADITH_STUDY,                  etHadithStudyMPP.getText().toString());
                hashmap.put(AllData.MPP_ISLAMI_LITERATURE_STUDY,       etIslamiLiteratureStudyMPP.getText().toString());

                hashmap.put(AllData.MPP_DARSUL_QURAN_NOTE,             etDarsulQuranNoteMPP.getText().toString());
                hashmap.put(AllData.MPP_DARSUL_HADITH_NOTE,            etDarsulHadithNoteMPP.getText().toString());
                hashmap.put(AllData.MPP_TOPIC_BASED_BOOK_NOTE,         etTopicBasedBookNoteMPP.getText().toString());
                hashmap.put(AllData.MPP_MEMORIZE_AYAT,                 etMemorizeAyatMPP.getText().toString());
                hashmap.put(AllData.MPP_MEMORIZE_HADITH,               etMemorizeHadithMPP.getText().toString());

                hashmap.put(AllData.MPP_NAMAJ_JAMAAT,                  etTotalJamaatMPP.getText().toString());

                hashmap.put(AllData.MPP_WORKER_CONTACT,                etWorkerContactMPP.getText().toString());
                hashmap.put(AllData.MPP_BOOK_DISTRIBUTION,             etBookDistributionMPP.getText().toString());
                hashmap.put(AllData.MPP_TIME_DEDICATION,               etTimeDedicationMPP.getText().toString());
                hashmap.put(AllData.MPP_GENERAL_CONTACT,               etGeneralContactMPP.getText().toString());

                hashmap.put(AllData.MPP_FAMILY_MEETING,                etFamilyMeetingMPP.getText().toString());
                hashmap.put(AllData.MPP_SELF_CRITICISM,                etSelfCriticismMPP.getText().toString());


                if (dbHelper.checkMonthlyPlanExistOrNot(monthId) == 0) {
                    Toast.makeText(this, "Your plan is saved successfully !!", Toast.LENGTH_SHORT).show();
                    dbHelper.insertRowInTableMonthlyPlan(hashmap);
                    return;

                } else {
                    dbHelper.updateRowInTableMonthlyPlan(hashmap);
                    Toast.makeText(this, "Your plan is updated successfully !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }



    // ################### methods which frequently doesn't change ######################
    private  ArrayList<HashMap<String, String>>  listUpdate(String friendType){
        ArrayList<HashMap<String, String>> myMap;
        myMap = dbHelper.getContentFromTablePeopleAddition(monthId, friendType);

        return myMap;
    }


    private void setNextMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set(month.get(Calendar.YEAR) + 1, month.getActualMinimum(Calendar.MONTH), 1);
            return;

        } else {
            month.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1, 1);
            return;
        }
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



    public void bSaveOrUpdate(){
        if (dbHelper.checkMonthlyPlanExistOrNot(monthId) == 0) {
            bSaveMPP.setText("Save");

        }else{
            bSaveMPP.setText("Update");
        }
    }



    public void makeAllElementClear() {
        //study -- 9 - 1 = 8
        etQuranStudyMPP.clearFocus();
        etHadithStudyMPP.clearFocus();
        etIslamiLiteratureStudyMPP.clearFocus();
        etDarsulQuranNoteMPP.clearFocus();
        etDarsulHadithNoteMPP.clearFocus();
        etTopicBasedBookNoteMPP.clearFocus();
        etMemorizeAyatMPP.clearFocus();
        etMemorizeHadithMPP.clearFocus();

        //namaj -- 1
        etTotalJamaatMPP.clearFocus();

        // contact -- 4
        etWorkerContactMPP.clearFocus();
        etBookDistributionMPP.clearFocus();
        etTimeDedicationMPP.clearFocus();
        etSelfCriticismMPP.clearFocus();

        //miscellaneous - 2
        etFamilyMeetingMPP.clearFocus();
        etGeneralContactMPP.clearFocus();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home: {
                this.finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }


    public void makeAllElementsEnabledOrDisabled(boolean condition) {

        etQuranReciteMPP.setEnabled(condition);
        etQuranStudyMPP.setEnabled(condition);
        etHadithStudyMPP.setEnabled(condition);
        etIslamiLiteratureStudyMPP.setEnabled(condition);
        etDarsulQuranNoteMPP.setEnabled(condition);
        etDarsulHadithNoteMPP.setEnabled(condition);
        etTopicBasedBookNoteMPP.setEnabled(condition);
        etMemorizeAyatMPP.setEnabled(condition);
        etMemorizeHadithMPP.setEnabled(condition);


        etWorkerContactMPP.setEnabled(condition);
        etBookDistributionMPP.setEnabled(condition);
        etTimeDedicationMPP.setEnabled(condition);
        etSelfCriticismMPP.setEnabled(condition);

        etTotalJamaatMPP.setEnabled(condition);

        etFamilyMeetingMPP.setEnabled(condition);
        etGeneralContactMPP.setEnabled(condition);

        if(condition == true){
            bSaveMPP.setVisibility(View.VISIBLE);
            bAddNewDawahFriendMPP.setVisibility(View.VISIBLE);
            bAddNewWorkerFriendMPP.setVisibility(View.VISIBLE);

        }else if(condition == false){
            bSaveMPP.setVisibility(View.GONE);
            bAddNewDawahFriendMPP.setVisibility(View.GONE);
            bAddNewWorkerFriendMPP.setVisibility(View.GONE);
        }
    }




    //################### methods dealing with date ###################################
    public void showDatePickerDialog() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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

            String sDate = ((new StringBuilder(sDay)).append(" - ").append(sMonth).
                    append(" - ").append(nYear)).toString();


            if(number == 1){            // number = 1: dl_etAdditionDateMPP;
                dl_etAdditionDateMPP.setText(sDate);

            }else if(number == 2){      // number == 2 dl_etMeetingDateMPP
                dl_etMeetingDateMPP.setText(sDate);
            }
        }
    }

}
