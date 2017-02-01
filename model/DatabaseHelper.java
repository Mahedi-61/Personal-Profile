package au.org.ipdc.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "personal_profile";
    public static final int DB_VERSION = 1;
    public static final String TABLE_1 = "tb_daily_profile";
    public static final String TABLE_2 = "tb_profile_report";
    public static final String TABLE_3 = "tb_monthly_plan";
    public static final String TABLE_4 = "tb_mpp_people_addition";
    public static final String TABLE_5 = "tb_mpr_work_addition";

    public static SQLiteDatabase db;
    private final Context context;
    private String db_absolute_path;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        db_absolute_path = context.getDatabasePath(DB_NAME).getPath();
    }


    private boolean checkDataBase() {
        return (new File(db_absolute_path)).exists();
    }


    private void copyDataBase() throws IOException {
        InputStream inputstream = context.getAssets().open(DB_NAME);
        FileOutputStream fileoutputstream = new FileOutputStream(db_absolute_path);
        byte abyte0[] = new byte[1024];
        do {
            int i = inputstream.read(abyte0);
            if (i <= 0) {
                fileoutputstream.flush();
                fileoutputstream.close();
                inputstream.close();
                return;
            }
            fileoutputstream.write(abyte0, 0, i);
        } while (true);
    }


    public void openDataBase() throws SQLException {
        db = SQLiteDatabase.openDatabase(db_absolute_path, null, 0);
    }

    
    public void close() {
        db.close();
    }


    public void onCreate(SQLiteDatabase sqlitedatabase) {
    }


    public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
    }
    

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            getReadableDatabase().close();
            try {
                copyDataBase();
                return;

            } catch (IOException ioexception) {
                throw new Error("Error copying database");
            }
        } else {
            Log.e("allah help me", "database is already exits");
            getReadableDatabase().close();
            return;
        }
    }


    //########################## table 1 ################################
    public int isThisMonthProfileReportExist(String monthId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT DISTINCT _id FROM " +
                TABLE_2 + " WHERE md_month = '")).append(monthId).append("'").toString(), null);

        int i = 0;
        if (myCursor != null) {
            if (myCursor.getCount() > 0) {
                myCursor.moveToFirst();
                i = 61;
            }
        }
        myCursor.close();
        sqlitedatabase.close();
        return i;
    }


    public ContentValues setAllContentValuesForTableDailyProfile (HashMap<String, String> dailyDiary) {
        ContentValues contentvalues = new ContentValues();
        
        contentvalues.put("day_id", dailyDiary.get("day_id"));
        contentvalues.put("month_id", dailyDiary.get("month_id"));

        //study -- 4
        contentvalues.put(AllData.DP_QURAN_RECITE,           dailyDiary.get(AllData.DP_QURAN_RECITE));
        contentvalues.put(AllData.DP_QURAN_STUDY,            dailyDiary.get(AllData.DP_QURAN_STUDY));
        contentvalues.put(AllData.DP_HADITH_STUDY,           dailyDiary.get(AllData.DP_HADITH_STUDY));
        contentvalues.put(AllData.DP_ISLAMI_LITERATURE_STUDY,dailyDiary.get(AllData.DP_ISLAMI_LITERATURE_STUDY));

        //namaj -- 1
        contentvalues.put(AllData.DP_NAMAJ_JAMAAT,           dailyDiary.get(AllData.DP_NAMAJ_JAMAAT));

        //contact -- 6
        contentvalues.put(AllData.DP_FRIEND_DAWAH,           dailyDiary.get(AllData.DP_FRIEND_DAWAH));
        contentvalues.put(AllData.DP_TARGETED_WORKER_CONTACT,dailyDiary.get(AllData.DP_TARGETED_WORKER_CONTACT));
        contentvalues.put(AllData.DP_WORKER_CONTACT,         dailyDiary.get(AllData.DP_WORKER_CONTACT));
        contentvalues.put(AllData.DP_BOOK_DISTRIBUTION,      dailyDiary.get(AllData.DP_BOOK_DISTRIBUTION));
        contentvalues.put(AllData.DP_TIME_DEDICATION,        dailyDiary.get(AllData.DP_TIME_DEDICATION));
        contentvalues.put(AllData.DP_GENERAL_CONTACT,        dailyDiary.get(AllData.DP_GENERAL_CONTACT));

        //miscellaneous -- 3
        contentvalues.put(AllData.DP_FAMILY_MEETING,         dailyDiary.get(AllData.DP_FAMILY_MEETING));
        contentvalues.put(AllData.DP_SELF_CRITICISM,         dailyDiary.get(AllData.DP_SELF_CRITICISM));
        contentvalues.put(AllData.DP_SUGGESTION,             dailyDiary.get(AllData.DP_SUGGESTION));
        return contentvalues;
    }


    public HashMap<String, String> getDailyProfileFromDatabase(String dayId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        HashMap<String, String> hashmap = new HashMap();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT * FROM " + TABLE_1 + " WHERE day_id = '"))
                .append(dayId).append("' ").toString(), null);

        if (myCursor != null && myCursor.getCount() > 0) {
            myCursor.moveToFirst();

            //study -- 4
            hashmap.put(AllData.DP_QURAN_RECITE,             myCursor.getString(3));
            hashmap.put(AllData.DP_QURAN_STUDY,              myCursor.getString(4));
            hashmap.put(AllData.DP_HADITH_STUDY,             myCursor.getString(5));
            hashmap.put(AllData.DP_ISLAMI_LITERATURE_STUDY,  myCursor.getString(6));

            //namaj -- 1
            hashmap.put(AllData.DP_NAMAJ_JAMAAT,             myCursor.getString(7));

            //contact -- 6
            hashmap.put(AllData.DP_FRIEND_DAWAH,             myCursor.getString(8));
            hashmap.put(AllData.DP_TARGETED_WORKER_CONTACT,  myCursor.getString(9));
            hashmap.put(AllData.DP_WORKER_CONTACT,           myCursor.getString(10));
            hashmap.put(AllData.DP_BOOK_DISTRIBUTION,        myCursor.getString(11));
            hashmap.put(AllData.DP_TIME_DEDICATION,          myCursor.getString(12));
            hashmap.put(AllData.DP_GENERAL_CONTACT,          myCursor.getString(13));

            //miscellaneous -- 3
            hashmap.put(AllData.DP_FAMILY_MEETING,           myCursor.getString(14));
            hashmap.put(AllData.DP_SELF_CRITICISM,           myCursor.getString(15));
            hashmap.put(AllData.DP_SUGGESTION,               myCursor.getString(16));
            
        }
        myCursor.close();
        sqlitedatabase.close();
        return hashmap;
    }


    public String getEachColumnForDayFromDatabase(String dayId, String column) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        String data = "";
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT ")).append(column).append(" FROM ")
                .append(TABLE_1).append(" WHERE ").append("day_id").append(" = '").append(dayId).append("'").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                data = myCursor.getString(0);

            } while (myCursor.moveToNext());
        }
        myCursor.close();
        sqlitedatabase.close();
        return data;
    }


    public ArrayList<String> getEachColumnForMonthFromDatabase(String monthId, String column) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<String> arraylist = new ArrayList();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT ")).append(column).append(" FROM ").append(TABLE_1)
                .append(" WHERE ").append("month_id").append(" = '").append(monthId).append("'").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                arraylist.add(myCursor.getString(0));
            } while (myCursor.moveToNext());
        }
        myCursor.close();
        sqlitedatabase.close();
        return arraylist;
    }


    public int getIdFromDayId(String dayId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT DISTINCT _id FROM " + TABLE_1 + " WHERE day_id = '"))
                .append(dayId).append("'").toString(), null);

        int i = 0;
        if (myCursor != null) {
            if (myCursor.getCount() > 0) {
                myCursor.moveToFirst();
                i = myCursor.getInt(0);
            }
        }
        myCursor.close();
        sqlitedatabase.close();
        return i;
    }


    public ArrayList<Integer> getTotalNumberOfProfileKeepingInfoForMonth(String monthId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList<Integer> arraylist = new ArrayList();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT keeping_dairy FROM " + TABLE_1 +
                " WHERE month_id = '")).append(monthId).append("'").toString(), null);

        if (myCursor.moveToFirst()) {
            do {
                arraylist.add(myCursor.getInt(0));
            } while (myCursor.moveToNext());
        }
        myCursor.close();
        sqlitedatabase.close();
        return arraylist;
    }


    public int getWhetherProfileKeepsOrNotFromDayId(String dayId) {

        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        Cursor cursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT DISTINCT keeping_dairy FROM " +
                TABLE_1 + " WHERE day_id = '")).append(dayId).append("'").toString(), null);

        int i = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                i = cursor.getInt(0);
            }
        }
        cursor.close();
        sqlitedatabase.close();
        return i;
    }


    public void insertRowInTableDailyProfile(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.insert(TABLE_1, null, setAllContentValuesForTableDailyProfile(hashmap));
        sqlitedatabase.close();
    }
    

    public void updateRowInTableDailyProfile(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.update(TABLE_1, setAllContentValuesForTableDailyProfile(hashmap),
                "_id = ?", new String[]{ hashmap.get("_id")});
        sqlitedatabase.close();
    }


    //########################### table 2 #########################################
    public ContentValues settAllContentValuesForTableProfileReprot(HashMap<String, String> profileReport) {
        ContentValues contentvalues = new ContentValues();

        contentvalues.put(AllData.MPR_MONTH_ID,                profileReport.get(AllData.MPR_MONTH_ID));
        //first part -- 8
        contentvalues.put(AllData.MPR_INTRO_DIST,              profileReport.get(AllData.MPR_INTRO_DIST));
        contentvalues.put(AllData.MPR_READING_BOOKS,           profileReport.get(AllData.MPR_READING_BOOKS));
        contentvalues.put(AllData.MPR_MEMBER_INCREASE,         profileReport.get(AllData.MPR_MEMBER_INCREASE));

        contentvalues.put(AllData.MPR_EYANOT_PAYMENT_DATE,     profileReport.get(AllData.MPR_EYANOT_PAYMENT_DATE));
        contentvalues.put(AllData.MPR_BAITULMAL_INCREASE,      profileReport.get(AllData.MPR_BAITULMAL_INCREASE));
        contentvalues.put(AllData.MPR_SOCIAL_WORK,             profileReport.get(AllData.MPR_SOCIAL_WORK));

        contentvalues.put(AllData.MPR_PAITENT_CARE,            profileReport.get(AllData.MPR_PAITENT_CARE));
        contentvalues.put(AllData.MPR_GENERAL_DAWAH,           profileReport.get(AllData.MPR_GENERAL_DAWAH));

        //last part -- 1
        contentvalues.put(AllData.MPR_COMMENT_SUGEGSTION,      profileReport.get(AllData.MPR_COMMENT_SUGEGSTION));
        return contentvalues;
    }


    public HashMap<String, String> getAllContentFromTableProfileReport(String monthId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        HashMap<String, String> hashmap = new HashMap();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT * FROM " + TABLE_2 +
                " WHERE " + AllData.MPR_MONTH_ID + " = '")).append(monthId).append("'").toString(), null);

        if (myCursor != null && myCursor.getCount() > 0) {
            myCursor.moveToFirst();

            hashmap.put(AllData.MPR_INTRO_DIST,             myCursor.getString(2));
            hashmap.put(AllData.MPR_READING_BOOKS,          myCursor.getString(3));
            hashmap.put(AllData.MPR_MEMBER_INCREASE,        myCursor.getString(4));
            hashmap.put(AllData.MPR_EYANOT_PAYMENT_DATE,    myCursor.getString(5));
            hashmap.put(AllData.MPR_BAITULMAL_INCREASE,     myCursor.getString(6));
            hashmap.put(AllData.MPR_SOCIAL_WORK,            myCursor.getString(7));

            hashmap.put(AllData.MPR_PAITENT_CARE,           myCursor.getString(8));
            hashmap.put(AllData.MPR_GENERAL_DAWAH,          myCursor.getString(9));
            hashmap.put(AllData.MPR_COMMENT_SUGEGSTION,     myCursor.getString(10));
        }
        myCursor.close();
        sqlitedatabase.close();
        return hashmap;
    }


    public void updateRowInTableProfileReport(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.update(TABLE_2, settAllContentValuesForTableProfileReprot(hashmap),
                AllData.MPR_MONTH_ID + " = ?", new String[]{ hashmap.get(AllData.MPR_MONTH_ID)});

        sqlitedatabase.close();
    }


    public void insertRowInTableProfileReport(HashMap<String, String> hashmap) {

        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.insert(TABLE_2, null, settAllContentValuesForTableProfileReprot(hashmap));
        sqlitedatabase.close();
    }

    
    //############################### table 3 ##############################3
    public ContentValues setAllContentValuesForTableMonthlyPlan(HashMap<String, String> monthlyPlan) {
        ContentValues contentvalues = new ContentValues();

        contentvalues.put(AllData.MPP_MONTH_ID,                     monthlyPlan.get(AllData.MPP_MONTH_ID));

        contentvalues.put(AllData.MPP_QURAN_RECITE,                 monthlyPlan.get(AllData.MPP_QURAN_RECITE));
        contentvalues.put(AllData.MPP_QURAN_STUDY,                  monthlyPlan.get(AllData.MPP_QURAN_STUDY));
        contentvalues.put(AllData.MPP_HADITH_STUDY,                 monthlyPlan.get(AllData.MPP_HADITH_STUDY));
        contentvalues.put(AllData.MPP_ISLAMI_LITERATURE_STUDY,      monthlyPlan.get(AllData.MPP_ISLAMI_LITERATURE_STUDY));
        contentvalues.put(AllData.MPP_DARSUL_QURAN_NOTE,            monthlyPlan.get(AllData.MPP_DARSUL_QURAN_NOTE));
        contentvalues.put(AllData.MPP_DARSUL_HADITH_NOTE,           monthlyPlan.get(AllData.MPP_DARSUL_HADITH_NOTE));
        contentvalues.put(AllData.MPP_TOPIC_BASED_BOOK_NOTE,        monthlyPlan.get(AllData.MPP_TOPIC_BASED_BOOK_NOTE));
        contentvalues.put(AllData.MPP_MEMORIZE_AYAT,                monthlyPlan.get(AllData.MPP_MEMORIZE_AYAT));
        contentvalues.put(AllData.MPP_MEMORIZE_HADITH,              monthlyPlan.get(AllData.MPP_MEMORIZE_HADITH));

        contentvalues.put(AllData.MPP_NAMAJ_JAMAAT,                 monthlyPlan.get(AllData.MPP_NAMAJ_JAMAAT));

        contentvalues.put(AllData.MPP_WORKER_CONTACT,               monthlyPlan.get(AllData.MPP_WORKER_CONTACT));
        contentvalues.put(AllData.MPP_BOOK_DISTRIBUTION,            monthlyPlan.get(AllData.MPP_BOOK_DISTRIBUTION));
        contentvalues.put(AllData.MPP_TIME_DEDICATION,              monthlyPlan.get(AllData.MPP_TIME_DEDICATION));
        contentvalues.put(AllData.MPP_GENERAL_CONTACT,              monthlyPlan.get(AllData.MPP_GENERAL_CONTACT));

        contentvalues.put(AllData.MPP_FAMILY_MEETING,               monthlyPlan.get(AllData.MPP_FAMILY_MEETING));
        contentvalues.put(AllData.MPP_SELF_CRITICISM,               monthlyPlan.get(AllData.MPP_SELF_CRITICISM));
        return contentvalues;
    }



    public HashMap<String, String> getAllContentFromTableMonthlyPlan(String monthId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        HashMap<String, String> hashmap = new HashMap();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT * FROM " + TABLE_3 +
                " WHERE " + AllData.MPP_MONTH_ID + " = '")).append(monthId).append("'").toString(), null);

        if (myCursor != null && myCursor.getCount() > 0) {
            myCursor.moveToFirst();

            hashmap.put(AllData.MPP_MONTH_ID,                      myCursor.getString(2));
            hashmap.put(AllData.MPP_QURAN_RECITE,                  myCursor.getString(3));
            hashmap.put(AllData.MPP_QURAN_STUDY,                   myCursor.getString(4));
            hashmap.put(AllData.MPP_HADITH_STUDY,                  myCursor.getString(5));
            hashmap.put(AllData.MPP_ISLAMI_LITERATURE_STUDY,       myCursor.getString(6));
            hashmap.put(AllData.MPP_DARSUL_QURAN_NOTE,             myCursor.getString(7));
            hashmap.put(AllData.MPP_DARSUL_HADITH_NOTE,            myCursor.getString(8));
            hashmap.put(AllData.MPP_TOPIC_BASED_BOOK_NOTE,         myCursor.getString(9));
            hashmap.put(AllData.MPP_MEMORIZE_AYAT,                 myCursor.getString(10));
            hashmap.put(AllData.MPP_MEMORIZE_HADITH,               myCursor.getString(11));

            hashmap.put(AllData.MPP_NAMAJ_JAMAAT,                  myCursor.getString(12));

            hashmap.put(AllData.MPP_WORKER_CONTACT,                myCursor.getString(13));
            hashmap.put(AllData.MPP_BOOK_DISTRIBUTION,             myCursor.getString(14));
            hashmap.put(AllData.MPP_TIME_DEDICATION,               myCursor.getString(15));
            hashmap.put(AllData.MPP_GENERAL_CONTACT,               myCursor.getString(16));

            hashmap.put(AllData.MPP_FAMILY_MEETING,                myCursor.getString(17));
            hashmap.put(AllData.MPP_SELF_CRITICISM,                myCursor.getString(18));

        }
        myCursor.close();
        sqlitedatabase.close();
        return hashmap;
    }
    


    public void insertRowInTableMonthlyPlan(HashMap<String, String> hashmap) {

        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.insert(TABLE_3, null, setAllContentValuesForTableMonthlyPlan(hashmap));
        sqlitedatabase.close();
    }



    public void updateRowInTableMonthlyPlan(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.update(TABLE_3, setAllContentValuesForTableMonthlyPlan(hashmap),
                AllData.MPP_MONTH_ID + " = ?", new String[]{ hashmap.get(AllData.MPP_MONTH_ID)});

        sqlitedatabase.close();
    }


    public int checkMonthlyPlanExistOrNot(String monthId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        Cursor myCursor = sqlitedatabase.rawQuery((new StringBuilder("SELECT  "+
                AllData.MPP_KEEPING_PLAN + " FROM " + TABLE_3 + " WHERE " +
                AllData.MPP_MONTH_ID + " = '")).append(monthId).append("'").toString(), null);

        int i = 0;
        if (myCursor != null) {
            if (myCursor.getCount() > 0) {
                myCursor.moveToFirst();
                i = myCursor.getInt(0);
            }
        }
        myCursor.close();
        sqlitedatabase.close();
        return i;
    }


    //############################### table 4 ##############################3
    public ContentValues setAllContentValuesForTablePeopleAddition(HashMap<String, String> hashMap) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(AllData.MPP_MONTH_ID, hashMap.get(AllData.MPP_MONTH_ID));
        contentValues.put(AllData.MPP_DL_FRIEND_NAME,   hashMap.get(AllData.MPP_DL_FRIEND_NAME));
        contentValues.put(AllData.MPP_DL_ADDITION_DATE, hashMap.get(AllData.MPP_DL_ADDITION_DATE));
        contentValues.put(AllData.MPP_DL_MEETING_DATE,  hashMap.get(AllData.MPP_DL_MEETING_DATE));
        contentValues.put(AllData.MPP_DL_FRIEND_TYPE,   hashMap.get(AllData.MPP_DL_FRIEND_TYPE));

        return contentValues;
    }



    public void insertRowInTablePeopleAddition(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.insert(TABLE_4, null, setAllContentValuesForTablePeopleAddition(hashmap));
        sqlitedatabase.close();
    }



    public ArrayList<HashMap<String, String>> getContentFromTablePeopleAddition(String monthId, String friendType) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList myList = new ArrayList<>();
        HashMap<String, String> hashmap = new HashMap();

        Cursor myCursor = sqlitedatabase.rawQuery(new StringBuilder("SELECT * FROM " + TABLE_4 +
                    " WHERE " + AllData.MPP_MONTH_ID + " = '" + monthId + "'" + " AND " +
                    AllData.MPP_DL_FRIEND_TYPE + " = '" + friendType).append("'").toString(), null);

        if (myCursor != null && myCursor.getCount() > 0) {
            myCursor.moveToFirst();
            do {
                hashmap = new HashMap<>();
                hashmap.put(AllData.MPP_DL_FRIEND_NAME,    myCursor.getString(2));
                hashmap.put(AllData.MPP_DL_ADDITION_DATE,  myCursor.getString(3));
                hashmap.put(AllData.MPP_DL_MEETING_DATE,   myCursor.getString(4));
                myList.add(hashmap);

            } while (myCursor.moveToNext());
        }

        myCursor.close();
        sqlitedatabase.close();
        return myList;
    }


    //############################### table 5 ##############################3
    public ContentValues setAllContentValuesForTableWrokAddition(HashMap<String, String> hashMap) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(AllData.MPR_MONTH_ID,            hashMap.get(AllData.MPR_MONTH_ID));
        contentValues.put(AllData.MPR_DL_DATE,             hashMap.get(AllData.MPR_DL_DATE));
        contentValues.put(AllData.MPR_DL_PR0G_DESCRIPTION, hashMap.get(AllData.MPR_DL_PR0G_DESCRIPTION));

        contentValues.put(AllData.MPR_DL_TIME,             hashMap.get(AllData.MPR_DL_TIME));
        contentValues.put(AllData.MPR_DL_UNIT,             hashMap.get(AllData.MPR_DL_UNIT));
        contentValues.put(AllData.MPR_DL_COMMENT,          hashMap.get(AllData.MPR_DL_COMMENT));
        return contentValues;
    }



    public void insertRowInTableWorkAddition(HashMap<String, String> hashmap) {
        SQLiteDatabase sqlitedatabase = getWritableDatabase();
        sqlitedatabase.insert(TABLE_5, null, setAllContentValuesForTableWrokAddition(hashmap));
        sqlitedatabase.close();
    }



    public ArrayList<HashMap<String, String>> getContentFromTableWorkAddition(String monthId) {
        SQLiteDatabase sqlitedatabase = getReadableDatabase();
        ArrayList myList = new ArrayList<>();
        HashMap<String, String> hashmap = new HashMap();

        Cursor myCursor = sqlitedatabase.rawQuery(new StringBuilder("SELECT * FROM " + TABLE_5 +
                " WHERE " + AllData.MPR_MONTH_ID + " = '" + monthId).append("'").toString(), null);

        if (myCursor != null && myCursor.getCount() > 0) {
            myCursor.moveToFirst();
            do {
                hashmap = new HashMap<>();
                hashmap.put(AllData.MPR_DL_DATE,               myCursor.getString(2));
                hashmap.put(AllData.MPR_DL_PR0G_DESCRIPTION,   myCursor.getString(3));
                hashmap.put(AllData.MPR_DL_TIME,               myCursor.getString(4));

                hashmap.put(AllData.MPR_DL_UNIT,               myCursor.getString(5));
                hashmap.put(AllData.MPR_DL_COMMENT,            myCursor.getString(6));
                myList.add(hashmap);

            } while (myCursor.moveToNext());
        }

        myCursor.close();
        sqlitedatabase.close();
        return myList;
    }
}
