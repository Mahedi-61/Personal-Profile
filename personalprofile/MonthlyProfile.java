package au.org.ipdc.personalprofile;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import au.org.ipdc.adapter.AdapterForMonthlyProfile;

public class MonthlyProfile extends Activity {

    private ArrayList<String> allDayIdList;
    private ImageView ibNextMonth, ibPreviousMonth;
    private ListView lvMonthlyProfile;
    private TextView tvTitleMP;
    private Calendar currentMonth, month;
    private String monthId = "", dayId = "";


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ac_monthly_profile);

        currentMonth = Calendar.getInstance();
        month = Calendar.getInstance();

        tvTitleMP = (TextView) findViewById(R.id.tvTitleMP);
        lvMonthlyProfile = (ListView) findViewById(R.id.lvMonthlyProfileMP);
        refreshCalendar();

        ibNextMonth = (ImageView) findViewById(R.id.ibNextMP);
        ibPreviousMonth = (ImageView) findViewById(R.id.ibPreviousMP);

        ibNextMonth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (setNextMonth()) {
                    refreshCalendar();
                }
            }
        });


        ibPreviousMonth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (setPreviousMonth()) {
                    refreshCalendar();
                }
            }
        });
    }


    public ArrayList getAllDataForDailyDairy() {
        allDayIdList = new ArrayList<>();
        int i = 1;

        do {
            if (i > month.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                return allDayIdList;
            }

            month.set(Calendar.DAY_OF_MONTH, i);
            dayId = DateFormat.format("EE, dd MMMM yyyy", month).toString();
            allDayIdList.add(dayId);

            if (month.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR) &&
                    month.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                    month.get(Calendar.DAY_OF_MONTH) == currentMonth.get(Calendar.DAY_OF_MONTH)) {

                return allDayIdList;
            }
            i++;
        } while (true);
    }


    public boolean setNextMonth() {
        if (month.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                month.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)) {

            Toast.makeText(this, "You can't see next month profile !", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set(month.get(Calendar.YEAR) + 1, month.getActualMinimum(Calendar.MONTH), 1);

        } else {
            month.add(Calendar.MONTH, 1);
        }
        return true;
    }


    public boolean setPreviousMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set(month.get(Calendar.YEAR) - 1, month.getActualMaximum(Calendar.MONTH), 31);
            return true;

        } else {
            month.add(Calendar.MONTH, -1);
            return true;
        }
    }


    private void  refreshCalendar(){
        allDayIdList = getAllDataForDailyDairy();
        monthId = DateFormat.format("MMMM yyyy", month).toString();
        tvTitleMP.setText((new StringBuilder("Monthly Profile - ")).append(monthId).toString());

        lvMonthlyProfile.setAdapter(new AdapterForMonthlyProfile(this, allDayIdList, monthId));
    }
}
