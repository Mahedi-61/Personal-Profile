package au.org.ipdc.model;

import android.content.Context;

import java.util.ArrayList;

public class MonthlyProfileCalculation {

    Context context;
    DatabaseHelper dbHelper;

    public MonthlyProfileCalculation(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public String getSumForAMonthProfile(String monthId, String appendixType) {
        double sum = 0.0D;
        ArrayList<String> allEntry = dbHelper.getEachColumnForMonthFromDatabase(monthId, appendixType);

        for (int i = 0; i < allEntry.size(); i++) {
            if ((allEntry.get(i) != null) && (!(allEntry.get(i)).equals("")) && (!(allEntry.get(i)).equals("."))) {
                sum = sum + Double.parseDouble(allEntry.get(i));
            }
        }

        return String.valueOf((int) sum);
    }
}
