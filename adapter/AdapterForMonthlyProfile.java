package au.org.ipdc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import au.org.ipdc.model.AllData;
import au.org.ipdc.model.MonthlyProfileCalculation;
import au.org.ipdc.personalprofile.R;
import au.org.ipdc.model.DatabaseHelper;

public class AdapterForMonthlyProfile extends BaseAdapter {

    private ArrayList<String> allDayIdList;
    private String monthId;
    private Context context;
    private MonthlyProfileCalculation calculation;
    private DatabaseHelper dbHelper;
    private HashMap<String, String> myMap, monthlyPlan;

    public AdapterForMonthlyProfile(Context context, ArrayList<String> allDayIdList, String monthId) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        this.allDayIdList = allDayIdList;
        calculation = new MonthlyProfileCalculation(context);
        this.monthId = monthId;
    }


    public int getCount() {
        return allDayIdList.size() + 2;
    }


    public Object getItem(int i) {
        return null;
    }


    public long getItemId(int i) {
        return 0L;
    }


    public View getView(int i, View convertView, ViewGroup viewgroup) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.li_monthly_profile, null);
            holder = new ViewHolder(row);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        // (i == 0) In first row monthly plan will be displayed
        if(i == 0){
            monthlyPlan = dbHelper.getAllContentFromTableMonthlyPlan(monthId);
            holder.tvDateMP.setText("প্ল্যান");
            //study -- 4
            holder.tvQuranReciteMP.setText(monthlyPlan.get(AllData.MPP_QURAN_RECITE));
            holder.tvQuranStudyMP.setText(monthlyPlan.get(AllData.MPP_QURAN_STUDY));
            holder.tvHadithStudyMP.setText(monthlyPlan.get(AllData.MPP_HADITH_STUDY));
            holder.tvIslamiLiteratureStudyMP.setText(monthlyPlan.get(AllData.MPP_ISLAMI_LITERATURE_STUDY));

            //namaj -- 1
            holder.tvNamajJamaatMP.setText(monthlyPlan.get(AllData.MPP_NAMAJ_JAMAAT));

            //contacts -- 6
            int friend_num = (dbHelper.getContentFromTablePeopleAddition(monthId, "1")).size();
            holder.tvFriendDawahMP.setText(String.valueOf(friend_num));

            int target_num = (dbHelper.getContentFromTablePeopleAddition(monthId, "2")).size();
            holder.tvTargetedWorkerContactMP.setText(String.valueOf(target_num));

            holder.tvWorkerContactMP.setText(monthlyPlan.get(AllData.MPP_WORKER_CONTACT));
            holder.tvBookDistributionMP.setText(monthlyPlan.get(AllData.MPP_BOOK_DISTRIBUTION));
            holder.tvTimeDedicationMP.setText(monthlyPlan.get(AllData.MPP_TIME_DEDICATION));
            holder.tvGeneralContactMP.setText(monthlyPlan.get(AllData.MPP_GENERAL_CONTACT));

            //miscellaneous -- 2
            holder.cbFamilyMeetingMP.setVisibility(View.GONE);
            holder.cbSelfCriticismMP.setVisibility(View.GONE);

            holder.tvTotalFamilyMeetingMP.setVisibility(View.VISIBLE);
            holder.tvTotalSelfCriticismMP.setVisibility(View.VISIBLE);

            holder.tvTotalFamilyMeetingMP.setText(monthlyPlan.get(AllData.MPP_FAMILY_MEETING));
            holder.tvTotalSelfCriticismMP.setText(monthlyPlan.get(AllData.MPP_SELF_CRITICISM));

        }else if(i == (allDayIdList.size() + 1)){
            holder.tvDateMP.setText("মোট");
            //study -- 4
            holder.tvQuranReciteMP.setText(calculation.getSumForAMonthProfile(monthId, AllData.DP_QURAN_RECITE));
            holder.tvQuranStudyMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_QURAN_STUDY));
            holder.tvHadithStudyMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_HADITH_STUDY));
            holder.tvIslamiLiteratureStudyMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_ISLAMI_LITERATURE_STUDY));

            //namaj -- 1
            holder.tvNamajJamaatMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_NAMAJ_JAMAAT));

            //contacts -- 6
            holder.tvFriendDawahMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_FRIEND_DAWAH));
            holder.tvTargetedWorkerContactMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_TARGETED_WORKER_CONTACT));
            holder.tvWorkerContactMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_WORKER_CONTACT));
            holder.tvBookDistributionMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_BOOK_DISTRIBUTION));
            holder.tvTimeDedicationMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_TIME_DEDICATION));
            holder.tvGeneralContactMP.setText(calculation.getSumForAMonthProfile(monthId,AllData.DP_GENERAL_CONTACT));

            //miscellaneous -- 2
            holder.cbFamilyMeetingMP.setVisibility(View.GONE);
            holder.cbSelfCriticismMP.setVisibility(View.GONE);

            holder.tvTotalFamilyMeetingMP.setVisibility(View.VISIBLE);
            holder.tvTotalSelfCriticismMP.setVisibility(View.VISIBLE);

            holder.tvTotalFamilyMeetingMP.setText(calculation.getSumForAMonthProfile(monthId, AllData.DP_FAMILY_MEETING));
            holder.tvTotalSelfCriticismMP.setText(calculation.getSumForAMonthProfile(monthId, AllData.DP_SELF_CRITICISM));

        } else{
            myMap = dbHelper.getDailyProfileFromDatabase(allDayIdList.get(i - 1));
            holder.tvDateMP.setText(i + "");

            //study -- 4
            holder.tvQuranReciteMP.setText(myMap.get(AllData.DP_QURAN_RECITE));
            holder.tvQuranStudyMP.setText(myMap.get(AllData.DP_QURAN_STUDY));
            holder.tvHadithStudyMP.setText(myMap.get(AllData.DP_HADITH_STUDY));
            holder.tvIslamiLiteratureStudyMP.setText(myMap.get(AllData.DP_ISLAMI_LITERATURE_STUDY));

            //namaj -- 1
            holder.tvNamajJamaatMP.setText(myMap.get(AllData.DP_NAMAJ_JAMAAT));

            //contacts -- 6
            holder.tvFriendDawahMP.setText(myMap.get(AllData.DP_FRIEND_DAWAH));
            holder.tvTargetedWorkerContactMP.setText(myMap.get(AllData.DP_TARGETED_WORKER_CONTACT));
            holder.tvWorkerContactMP.setText(myMap.get(AllData.DP_WORKER_CONTACT));
            holder.tvBookDistributionMP.setText(myMap.get(AllData.DP_BOOK_DISTRIBUTION));
            holder.tvTimeDedicationMP.setText(myMap.get(AllData.DP_TIME_DEDICATION));
            holder.tvGeneralContactMP.setText(myMap.get(AllData.DP_GENERAL_CONTACT));

            //miscellaneous -- 2
            holder.cbFamilyMeetingMP.setVisibility(View.VISIBLE);
            holder.cbSelfCriticismMP.setVisibility(View.VISIBLE);

            holder.tvTotalFamilyMeetingMP.setVisibility(View.GONE);
            holder.tvTotalSelfCriticismMP.setVisibility(View.GONE);

            if (myMap.get(AllData.DP_FAMILY_MEETING) != null) {
                if (Integer.parseInt(myMap.get(AllData.DP_FAMILY_MEETING)) == 1) {
                    holder.cbFamilyMeetingMP.setChecked(true);
                } else {
                    holder.cbFamilyMeetingMP.setChecked(false);
                }
            } else {
                holder.cbFamilyMeetingMP.setChecked(false);
            }


            if (myMap.get(AllData.DP_SELF_CRITICISM) != null) {
                if (Integer.parseInt(myMap.get(AllData.DP_SELF_CRITICISM)) == 1) {
                    holder.cbSelfCriticismMP.setChecked(true);
                } else {
                    holder.cbSelfCriticismMP.setChecked(false);
                }
            } else {
                holder.cbSelfCriticismMP.setChecked(false);
            }
        } //end of else
        return row;
    }


    private class ViewHolder {

        public CheckBox  cbFamilyMeetingMP, cbSelfCriticismMP;
        public TextView tvDateMP, tvQuranReciteMP, tvQuranStudyMP, tvHadithStudyMP,
                tvIslamiLiteratureStudyMP, tvNamajJamaatMP,
                tvFriendDawahMP,tvTargetedWorkerContactMP, tvWorkerContactMP, tvGeneralContactMP,
                tvBookDistributionMP, tvTimeDedicationMP;

        public TextView tvTotalFamilyMeetingMP, tvTotalSelfCriticismMP;


        public ViewHolder(View view) {
            tvDateMP = (TextView) view.findViewById(R.id.tvDateMP);

            //study -- 4
            tvQuranReciteMP = (TextView) view.findViewById(R.id.tvQuranReciteMP);
            tvQuranStudyMP = (TextView) view.findViewById(R.id.tvQuranStudyMP);
            tvHadithStudyMP = (TextView) view.findViewById(R.id.tvHadithStudyMP);
            tvIslamiLiteratureStudyMP = (TextView) view.findViewById(R.id.tvIslamiLiteratureStudyMP);


            //namaj -- 1
            tvNamajJamaatMP = (TextView) view.findViewById(R.id.tvNamajJamaatMP);

            //contacts -- 6
            tvFriendDawahMP = (TextView) view.findViewById(R.id.tvFriendDawahMP);
            tvTargetedWorkerContactMP = (TextView) view.findViewById(R.id.tvTargetedWorkerContactMP);
            tvWorkerContactMP = (TextView) view.findViewById(R.id.tvWorkerContactMP);
            tvBookDistributionMP = (TextView) view.findViewById(R.id.tvBookDistributionMP);
            tvTimeDedicationMP = (TextView) view.findViewById(R.id.tvTimeDedicationMP);
            tvGeneralContactMP = (TextView) view.findViewById(R.id.tvGeneralContactMP);

            //miscellaneous -- 2
            tvTotalFamilyMeetingMP = (TextView) view.findViewById(R.id.tvTotalFamilyMeetingMP);
            tvTotalSelfCriticismMP = (TextView) view.findViewById(R.id.tvTotalSelfCriticismMP);

            cbFamilyMeetingMP = (CheckBox) view.findViewById(R.id.cbFamilyMeetingMP);
            cbSelfCriticismMP = (CheckBox) view.findViewById(R.id.cbSelfCriticismMP);
        }
    }
}

