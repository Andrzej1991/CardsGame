package com.company.andrzej.rolki.cardsdeck.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.company.andrzej.rolki.cardsdeck.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andrzej on 2017-05-01.
 */

public class CardsRecyclerView extends RecyclerView.Adapter<CardsRecyclerView.ViewHolder> {

    private List<String> cards;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(ImageView v) {
            super(v);
            imageView = v;
        }
    }

    @Override
    public CardsRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.imt_image, parent, false);
        return new ViewHolder(v);
    }

    public CardsRecyclerView(Context context, List<String> cards) {
        this.context = context;
        this.cards = cards;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CardsRecyclerView.ViewHolder holder, int position) {
        Picasso.with(context).load(cards.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
