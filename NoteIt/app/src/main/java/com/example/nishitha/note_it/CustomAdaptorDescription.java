package com.example.nishitha.note_it;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomAdaptorDescription extends ArrayAdapter {
    String[] descriptionArray;
    String[] dateArray;

    public CustomAdaptorDescription(@NonNull Context context,String[] ArrayDescription, String[] ArrayDate) {
        super(context,R.layout.customdescription,R.id.description,ArrayDescription);
        this.descriptionArray=ArrayDescription;
        this.dateArray=ArrayDate;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater myInflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = myInflator.inflate(R.layout.customdescription,parent,false);

        TextView datetv = customView.findViewById(R.id.date);
        TextView des = customView.findViewById(R.id.description);
        datetv.setText(dateArray[position]);
        des.setText(descriptionArray[position]);

        return customView;
    }
}
