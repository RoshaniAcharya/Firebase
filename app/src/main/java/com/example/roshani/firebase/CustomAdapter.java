package com.example.roshani.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context c;
    List<Module> data=new ArrayList<>();
    public CustomAdapter(Dashboard dashboard, List<Module> list) {
        c=dashboard;
        data=list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(c).inflate(R.layout.newlayout,null);
        TextView list1=convertView.findViewById(R.id.rolltv);
        list1.setText(data.get(position).getRoll()+"");
        TextView list2=convertView.findViewById(R.id.nametv);
        list2.setText(data.get(position).getName());
        return convertView;

    }
}
