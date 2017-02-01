package au.org.ipdc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import au.org.ipdc.model.AllData;
import au.org.ipdc.personalprofile.R;

public class AdapterForAddedNewWork extends BaseAdapter {

    private ArrayList<HashMap<String, String>> myMap;
    private Context context;

    public AdapterForAddedNewWork(ArrayList<HashMap<String, String>> myMap, Context context) {
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
                    inflate(R.layout.li_added_new_work, null);
            holder = new ViewHolder(row);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        HashMap<String,String> data = myMap.get(i);
        holder.li_tvDateANW.setText(data.get(AllData.MPR_DL_DATE));
        holder.li_tvProgDescriptionANW.setText(data.get(AllData.MPR_DL_PR0G_DESCRIPTION));
        holder.li_tvTimeANW.setText(data.get(AllData.MPR_DL_TIME));

        holder.li_tvUnitANW.setText(data.get(AllData.MPR_DL_UNIT));
        holder.li_tvCommentANW.setText(data.get(AllData.MPR_DL_COMMENT));

        return row;
    }


    private class ViewHolder {
        public TextView  li_tvDateANW, li_tvProgDescriptionANW, li_tvTimeANW, li_tvUnitANW, li_tvCommentANW;

        public ViewHolder(View view) {
            //study -- 4
            li_tvDateANW = (TextView) view.findViewById(R.id.li_tvDateANW);
            li_tvProgDescriptionANW = (TextView) view.findViewById(R.id.li_tvProgDescriptionANW);
            li_tvTimeANW = (TextView) view.findViewById(R.id.li_tvTimeANW);
            li_tvUnitANW = (TextView) view.findViewById(R.id.li_tvUnitANW);
            li_tvCommentANW = (TextView) view.findViewById(R.id.li_tvCommentANW);
        }
    }
}

