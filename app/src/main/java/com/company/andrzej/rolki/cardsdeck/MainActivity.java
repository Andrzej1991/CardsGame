package com.company.andrzej.rolki.cardsdeck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.andrzej.rolki.cardsdeck.model.Card;
import com.company.andrzej.rolki.cardsdeck.model.Cards;
import com.company.andrzej.rolki.cardsdeck.model.Deck;
import com.company.andrzej.rolki.cardsdeck.service.CardService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andrzej on 2017-04-28.
 */

public class MainActivity extends AppCompatActivity {

    String url = "https://deckofcardsapi.com/api/deck/";

    @BindView(R.id.spinnerDecks)
    Spinner spinnerDecks;
    @BindView(R.id.image)
    ImageView image;

    @Inject
    Retrofit retrofit;

    CardService.CardAPI cardApi;
    String deckID;
    List<Card> cardsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureSpinnerData();
    }


    private void configureSpinnerData() {
        Integer[] items = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        spinnerDecks.setAdapter(adapter);
    }

    private void getCards(final String deck_id, final int count) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        cardApi = retrofit.create(CardService.CardAPI.class);
        cardApi.getCards(deck_id, count)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Cards>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Cards cards) {
                        Log.d("ARRRRRRRRRRRRAYYYYYYY", String.valueOf(cards));
                        cardsArray = cards.getArrayCards();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    Log.d("FADSFAFASDFAFS", String.valueOf(e));
                    }

                    @Override
                    public void onComplete() {

                        Toast.makeText(getApplicationContext(), String.valueOf(cardsArray), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void getDecks(final int count) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        cardApi = retrofit.create(CardService.CardAPI.class);
        cardApi.getDeck(count)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Deck>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Deck deck) {
                        deckID = deck.getDeck_id();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.decks_ammount)
    public void getDecks() {
        int count = (int) spinnerDecks.getSelectedItem();
        getDecks(count);

    }

    @OnClick(R.id.buttonStart)
    public void openGameFragment() {
//        Fragment newFragment = new GameFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.main_relative, newFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();

        getCards(deckID, 5);
    }
}
