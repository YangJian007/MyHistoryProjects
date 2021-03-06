package com.mapuni.mobileenvironment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

import java.util.List;

/**
 * Created by Mai on 2016/11/25.
 */

public class WaterListAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public WaterListAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder=null;
        if(view==null){
            view=View.inflate(context, R.layout.item_listview_water_ac,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) view.findViewById(R.id.tv);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(i));
        if(i%2==0){
            viewHolder.textView.setBackgroundColor(Color.parseColor("#D9E8FF"));
        }
        return view;
    }
    class ViewHolder{
        TextView textView;
    }

}
