package com.company.andrzej.rolki.cardsdeck.presenters;

import android.content.Context;
import android.os.Bundle;

import com.company.andrzej.rolki.cardsdeck.MainActivity;
import com.company.andrzej.rolki.cardsdeck.component.DaggerServiceComponent;
import com.company.andrzej.rolki.cardsdeck.component.ServiceComponent;
import com.company.andrzej.rolki.cardsdeck.model.Deck;
import com.company.andrzej.rolki.cardsdeck.module.ServiceModule;
import com.company.andrzej.rolki.cardsdeck.service.CardService;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Andrzej on 2017-05-02.
 */

public class MainActivityPresenter implements com.company.andrzej.rolki.cardsdeck.BaseAdapter {

    private Context context;
    private MainActivity mainActivity;
    ServiceComponent serviceComponent;
    private CardService.CardAPI cardApi;
    String url = "https://deckofcardsapi.com/api/deck/";

    @Inject
    Retrofit retrofit;

    public void onAttachActivity(Bundle savedInstanceState, MainActivity activity) {
        mainActivity = activity;
        injectServiceComponent();
    }

    public MainActivityPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void start() {
    }

    public void injectServiceComponent() {
        serviceComponent = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(url))
                .build();
        serviceComponent.inject(this);
        cardApi = retrofit.create(CardService.CardAPI.class);
    }

    public void shuffleDeck(final String deck_id) {
        cardApi.shuffleDeck(deck_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Deck>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Deck deck) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
