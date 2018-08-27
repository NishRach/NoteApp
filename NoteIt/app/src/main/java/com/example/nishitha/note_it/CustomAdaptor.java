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

public class CustomAdaptor extends ArrayAdapter {


    public CustomAdaptor(@NonNull Context context, String[] resource) {
        super(context,R.layout.custom, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflator = LayoutInflater.from(getContext());

        View customView = myInflator.inflate(R.layout.custom,parent,false);

        String singleItem = getItem(position).toString();
        TextView textView = customView.findViewById(R.id.customtextview);
        ImageView imageView = customView.findViewById(R.id.imageView);

        textView.setText(singleItem);
        imageView.setImageResource(R.drawable.checkbox_on_background);

        return customView;
    }
}

