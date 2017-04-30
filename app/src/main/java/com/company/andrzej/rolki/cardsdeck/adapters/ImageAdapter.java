package com.company.andrzej.rolki.cardsdeck.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.company.andrzej.rolki.cardsdeck.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andrzej on 2017-04-28.
 */

public class ImageAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private List<String> imageUrls;

    public ImageAdapter(Context context, List<String> imageUrls) {
        super(context, R.layout.imt_image, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.imt_image, parent, false);
        }

        Picasso
                .with(context)
                .load(imageUrls.get(position))
                .fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }
}
