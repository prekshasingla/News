package com.example.prekshasingla.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class ImageAdapter extends ArrayAdapter<Source>
{
    public ArrayList<Source> sourceList;

    public ImageAdapter(Activity context, ArrayList<Source> sourceList) {
        super(context, 0, sourceList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Source source = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view, parent, false);
        }


        ImageView posterView = (ImageView) convertView.findViewById(R.id.imageView);
          Picasso.with(getContext()).load(source.logoUrl).into(posterView);

        return convertView;
    }


}
