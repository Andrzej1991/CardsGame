package com.company.andrzej.rolki.cardsdeck;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.company.andrzej.rolki.cardsdeck.Component.DaggerServiceComponent;
import com.company.andrzej.rolki.cardsdeck.Component.ServiceComponent;
import com.company.andrzej.rolki.cardsdeck.Module.ServiceModule;
import com.company.andrzej.rolki.cardsdeck.adapters.CardsRecyclerView;
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
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.linear_main)
    LinearLayout linearMain;
    @BindView(R.id.linear_second)
    LinearLayout linearSecond;
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    ServiceComponent serviceComponent;

    @Inject
    Retrofit retrofit;


    private RecyclerView.Adapter cardsRecyclerView;
    private CardService.CardAPI cardApi;
    private String deckID;
    private List<Card> cardsArray = new ArrayList<>();
    private List<String> imgUrls = new ArrayList<>();
    private int cardsRemaining;
    private int checkFiguryRank = 0;
    private int checkCardsBlackInt = 0;
    private int checkCardsRedInt = 0;

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
        configureRecyclerView();
    }

    private void configureRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 5);
        recyclerView.setLayoutManager(layoutManager);
        cardsRecyclerView = new CardsRecyclerView(getApplicationContext(), imgUrls);
        recyclerView.setAdapter(cardsRecyclerView);
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
                        cardsRecyclerView.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(cardsRecyclerView.getItemCount());
                        checkFiguryAchievement();
                        checkColorAchievement();


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void checkSchodkiAchievement(){
        Card card = new Card();

    }

    private void checkColorAchievement() {
        for (int i = 0; i < cardsArray.size(); i++) {
            String checkCardsBlack = cardsArray.get(i).getCode();
            String checkCardsRed = cardsArray.get(i).getCode();
            if (checkCardsRed.contains("D") || checkCardsRed.contains("H")) {
                checkCardsRedInt++;
                if (checkCardsRedInt == 3) {
                    Utils.showToast(getApplicationContext(), "COLOR RED");
                }
            }
            if (checkCardsBlack.contains("C") || checkCardsBlack.contains("S")) {
                checkCardsBlackInt++;
                if (checkCardsBlackInt == 3) {
                    Utils.showToast(getApplicationContext(), "COLOR BLACK");
                }
            }
        }
    }

    private void checkFiguryAchievement() {
        for (int i = 0; i < cardsArray.size(); i++) {
            int cardRankFigury = cardsArray.get(i).getRank();
            if (cardRankFigury >= 11)
                checkFiguryRank++;
            if (checkFiguryRank == 3) {
                checkFiguryRank = 4;
                Utils.showToast(getApplicationContext(), "FIGURY");
            }
        }
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
        if (!Utils.checkInternetConnection(this.getApplicationContext())) {
            Utils.showToast(this.getApplicationContext(), "Please check internet connetion...");
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
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    @OnClick(R.id.get_next_card)
    public void nextCard() {
        if (cardsRemaining == 0) {
            cardsRemaining = 2;
            createAlertDialogForShuffle();
            Utils.showToast(this.getApplicationContext(), "Please shuffle  the decks..");
        } else {
            getCards(deckID, 1);
            cardsRemaining--;
        }
    }

    private void configureSpinnerData() {
        Integer[] items = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        spinnerDecks.setAdapter(adapter);
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
