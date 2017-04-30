package com.company.andrzej.rolki.cardsdeck;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.andrzej.rolki.cardsdeck.Component.DaggerServiceComponent;
import com.company.andrzej.rolki.cardsdeck.Component.ServiceComponent;
import com.company.andrzej.rolki.cardsdeck.Module.ServiceModule;
import com.company.andrzej.rolki.cardsdeck.adapters.ImageAdapter;
import com.company.andrzej.rolki.cardsdeck.model.Card;
import com.company.andrzej.rolki.cardsdeck.model.Cards;
import com.company.andrzej.rolki.cardsdeck.model.Deck;
import com.company.andrzej.rolki.cardsdeck.service.CardService;

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

/**
 * Created by Andrzej on 2017-04-28.
 */

public class MainActivity extends AppCompatActivity {

    String url = "https://deckofcardsapi.com/api/deck/";

    @BindView(R.id.spinnerDecks)
    Spinner spinnerDecks;
    @BindView(R.id.usage_example_gridview)
    GridView grid;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.linear_main)
    LinearLayout linearMain;
    @BindView(R.id.linear_second)
    LinearLayout linearSecond;

    ServiceComponent serviceComponent;

    @Inject
    Retrofit retrofit;

    private CardService.CardAPI cardApi;
    private String deckID;
    private List<Card> cardsArray = new ArrayList<>();
    private List<String> imgUrls = new ArrayList<>();
    private int cardsRemaining;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureSpinnerData();
        serviceComponent = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(url))
                .build();
        serviceComponent.inject(this);
        cardApi = retrofit.create(CardService.CardAPI.class);
        imageAdapter = new ImageAdapter(getApplicationContext(), imgUrls);
        grid.setAdapter(imageAdapter);
    }

    private void configureSpinnerData() {
        Integer[] items = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        spinnerDecks.setAdapter(adapter);
    }

    private void getCards(final String deck_id, final int count) {
        cardApi.getCards(deck_id, count)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Cards>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Cards cards) {
                        cardsArray = cards.getArrayCards();
                        for (Card item : cardsArray) {
                            imgUrls.add(item.getImage());
                        }
                        cardsRemaining = cards.getRemaining();
                        imageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void getDecks(final int count) {
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
                        getCards(deckID, 5);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void shuffleDeck(final String deck_id) {
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


    @OnClick(R.id.buttonStart)
    public void openGameFragment() {
        if (!checkInternetConnection()) {
            Toast.makeText(getApplicationContext(), "Please check internet connetion...", Toast.LENGTH_SHORT).show();
        } else {
            int count = (int) spinnerDecks.getSelectedItem();
            getDecks(count);
            configureRetrofitRequest();
        }
    }

    private void createAlertDialogForShuffle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setMessage("Are you sure to shuffle the decks?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                shuffleDeck(deckID);
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    @OnClick(R.id.get_next_card)
    public void nextCard() {
        if (cardsRemaining == 0) {
            cardsRemaining = 1;
            createAlertDialogForShuffle();
            Toast.makeText(getApplicationContext(), "Please shuffle  the decks..", Toast.LENGTH_SHORT).show();
        } else {
            getCards(deckID, 1);
            cardsRemaining--;
        }
    }

    private void configureRetrofitRequest() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                linearMain.setVisibility(View.GONE);
                linearSecond.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);
    }
}
