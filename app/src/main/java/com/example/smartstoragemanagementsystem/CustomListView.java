package com.example.smartstoragemanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomListView extends ArrayAdapter<String> {


    private String[] proID;
    private String[] proName;
    private Activity context ;
    public CustomListView(Activity context, String[] proName,String[] proID) {

        super(context, R.layout.layout,proID);

        this.context =context;
        this.proID = proID;
        this.proName = proName;
    }

    public View getView(int position , @NonNull View convertView, @NonNull ViewGroup parent) {

        View r = convertView;

        ViewHolder viewHolder = null;
        if (r==null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();

            r = layoutInflater.inflate(R.layout.layout,null,true);

            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder)r.getTag();
        }

        viewHolder.textView1.setText(proName[position]);
        viewHolder.textView2.setText(proID[position]);

        return r;

    }

    class ViewHolder{
        TextView textView1;
        TextView textView2;


        ViewHolder(View v){

            textView1 = (TextView)v.findViewById(R.id.productName);
            textView2 = (TextView)v.findViewById(R.id.productID);

        }
    }
}
