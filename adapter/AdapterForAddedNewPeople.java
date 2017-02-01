package au.org.ipdc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import au.org.ipdc.model.AllData;
import au.org.ipdc.personalprofile.R;

public class AdapterForAddedNewPeople extends BaseAdapter {

    private ArrayList<HashMap<String, String>> myMap;
    private Context context;

    public AdapterForAddedNewPeople(ArrayList<HashMap<String, String>> myMap, Context context) {
        this.myMap = myMap;
        this.context = context;
    }


    public int getCount() {
        return myMap.size();
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
                    inflate(R.layout.li_added_new_people, null);
            holder = new ViewHolder(row);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        HashMap<String,String> data = myMap.get(i);
        holder.li_tvAddedPersonNameANP.setText(data.get(AllData.MPP_DL_FRIEND_NAME));
        holder.li_tvAdditionDateANP.setText(data.get(AllData.MPP_DL_ADDITION_DATE));
        holder.li_tvMeetingDateANP.setText(data.get(AllData.MPP_DL_MEETING_DATE));

        return row;
    }


    private class ViewHolder {
        public TextView  li_tvAddedPersonNameANP, li_tvAdditionDateANP, li_tvMeetingDateANP;

        public ViewHolder(View view) {
            //study -- 4
            li_tvAddedPersonNameANP = (TextView) view.findViewById(R.id.li_tvAddedPersonNameANP);
            li_tvAdditionDateANP = (TextView) view.findViewById(R.id.li_tvAdditionDateANP);
            li_tvMeetingDateANP = (TextView) view.findViewById(R.id.li_tvMeetingDateANP);
        }
    }
}

