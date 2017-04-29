package com.company.andrzej.rolki.cardsdeck.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.company.andrzej.rolki.cardsdeck.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andrzej on 2017-04-28.
 */

public class GameFragment extends Fragment {

    @BindView(R.id.image_Cards)
    ImageView imageCard;


    public GameFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this,v);

        return v;
    }




}
