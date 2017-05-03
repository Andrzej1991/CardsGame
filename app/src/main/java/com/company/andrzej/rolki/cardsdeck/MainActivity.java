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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.company.andrzej.rolki.cardsdeck.adapters.CardsRecyclerView;
import com.company.andrzej.rolki.cardsdeck.component.DaggerServiceComponent;
import com.company.andrzej.rolki.cardsdeck.component.ServiceComponent;
import com.company.andrzej.rolki.cardsdeck.model.Card;
import com.company.andrzej.rolki.cardsdeck.model.CardsArray;
import com.company.andrzej.rolki.cardsdeck.model.Deck;
import com.company.andrzej.rolki.cardsdeck.module.ServiceModule;
import com.company.andrzej.rolki.cardsdeck.service.CardService;

import java.util.ArrayList;
import java.util.Collections;
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
    @BindView(R.id.image_achievement_colorB)
    ImageView achieColorB;
    @BindView(R.id.image_achievement_colorR)
    ImageView achieColorR;
    @BindView(R.id.image_achievement_figures)
    ImageView achieFigures;
    @BindView(R.id.image_achievement_twins)
    ImageView achieTwins;

    ServiceComponent serviceComponent;

    @Inject
    Retrofit retrofit;

    private RecyclerView.Adapter cardsRecyclerView;
    private CardService.CardAPI cardApi;
    private String deckID;
    private List<Card> cardsArrayList = new ArrayList<>();
    private List<String> imgUrls = new ArrayList<>();
    private List<Integer> cardsValues = new ArrayList<>();
    private int cardsRemaining;
    private int checkFiguryRank = 0;
    private int checkCardsBlackInt = 0;
    private int checkCardsRedInt = 0;
    private boolean isCheckedForDuplicates = true;
    private boolean isCheckedForFigury = true;
    private boolean isCheckedColorRed = true;
    private boolean isCheckedColorBlack = true;

//    można dodatkowo przenieść z MainActivity do presentera, ale jest to mały projekt
//    nie udało mi się zrobić podzadania "schodki" zostawiłem metody, którymi
//    próbowałem to zrobić

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureSpinnerData();
        configureRecyclerView();
        injectServiceComponent();
    }

    public void injectServiceComponent() {
        serviceComponent = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(url))
                .build();
        serviceComponent.inject(this);
        cardApi = retrofit.create(CardService.CardAPI.class);
    }

    private void fetchCards(final String deck_id, final int count) {
        cardApi.fetchCardsFromDeckID(deck_id, count)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardsArray>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull CardsArray cardsArray) {
                        cardsArrayList = cardsArray.getArrayCards();
                        for (Card item : cardsArrayList) {
                            imgUrls.add(item.getImage());
                            cardsValues.add(item.getRank());
                        }
                        cardsRemaining = cardsArray.getRemaining();
                        cardsRecyclerView.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(cardsRecyclerView.getItemCount());
                        if (isCheckedForFigury) {
                            checkFiguryAchievement();
                        }
                        if (isCheckedColorBlack || isCheckedColorRed) {
                            checkColorAchievement();
                        }
//                        checkAscendingOrDescendingAchievement();
                        if (isCheckedForDuplicates) {
                            checkDuplicateAchievement();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void fetchDeck(final int count) {
        cardApi.fetchDeck(count)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Deck>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Deck deck) {
                        deckID = deck.getDeck_id();
                        fetchCards(deckID, 5);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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

    private void checkDuplicateAchievement() {
        Collections.sort(cardsValues);
        countItem(cardsValues);
    }

    public void countItem(List<Integer> list) {
        List<Integer> notDupes = new ArrayList<>();
        List<Integer> duplicates = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!notDupes.contains(list.get(i))) {
                notDupes.add(list.get(i));
                continue;
            }
            duplicates.add(list.get(i));
            Collections.sort(duplicates);
            findDuplicates(duplicates);
        }
    }

    public void findDuplicates(List<Integer> listContainingDuplicates) {
        int previous = listContainingDuplicates.get(0) - 1;
        int dupCount = 0;
        for (int i = 0; i < listContainingDuplicates.size(); ++i) {
            if (listContainingDuplicates.get(i) == previous) {
                ++dupCount;
            } else {
                previous = listContainingDuplicates.get(i);
            }
        }
        if (dupCount == 1) {
            Utils.showToast(getApplicationContext(), this.getString(R.string.blizniaki));
            achieTwins.setVisibility(View.VISIBLE);
            isCheckedForDuplicates = false;
        }
    }

    private void checkAscendingOrDescendingAchievement() {
        int position = 0;
        Card card1 = cardsArrayList.get(position);
        Card card2 = cardsArrayList.get(position);
        if (card1.isBigger(card2)) {
            Utils.showToast(getApplicationContext(), "Bigger");
        }
        if (card1.isLesser(card2)) {
            Utils.showToast(getApplicationContext(), "Lesser");
        }
    }

    private void checkColorAchievement() {
        for (int i = 0; i < cardsArrayList.size(); i++) {
            String checkCardsBlack = cardsArrayList.get(i).getCode();
            String checkCardsRed = cardsArrayList.get(i).getCode();
            if (checkCardsRed.contains("D") || checkCardsRed.contains("H")) {
                checkCardsRedInt++;
                if (checkCardsRedInt == 3) {
                    Utils.showToast(getApplicationContext(), this.getString
                            (R.string.kolor_czerwony));
                    achieColorR.setVisibility(View.VISIBLE);
                    isCheckedColorRed = false;
                }
            }
            if (checkCardsBlack.contains("C") || checkCardsBlack.contains("S")) {
                checkCardsBlackInt++;
                if (checkCardsBlackInt == 3) {
                    Utils.showToast(getApplicationContext(), this.getString(R.string.kolor_czarny));
                    achieColorB.setVisibility(View.VISIBLE);
                    isCheckedColorBlack = false;
                }
            }
        }
    }

    private void checkFiguryAchievement() {
        for (int i = 0; i < cardsArrayList.size(); i++) {
            int cardRankFigury = cardsArrayList.get(i).getRank();
            if (cardRankFigury >= 11)
                checkFiguryRank++;
            if (checkFiguryRank == 3) {
                Utils.showToast(getApplicationContext(), this.getString(R.string.figury));
                achieFigures.setVisibility(View.VISIBLE);
                isCheckedForFigury = false;
            }
        }
    }

    private void createAlertDialogForShuffle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper
                (this, R.style.myDialog));
        builder.setMessage(this.getString(R.string.are_you_sure_shuffle));
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> shuffleDeck(deckID));
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> finish());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }

    @OnClick(R.id.buttonStart)
    public void startGame() {
        if (!Utils.checkInternetConnection(this.getApplicationContext())) {
            Utils.showToast(this.getApplicationContext(), this.getString(R.string.internet_check));
        } else {
            int count = (int) spinnerDecks.getSelectedItem();
            fetchDeck(count);
            configureHandlerForGetDecks();
        }
    }

    @OnClick(R.id.get_next_card)
    public void nextCard() {
        cardsValues.size();
        if (cardsRemaining == 0) {
            cardsRemaining = 2;
            createAlertDialogForShuffle();
            Utils.showToast(this.getApplicationContext(), this.getString(R.string.please_shuffle));
        } else {
            fetchCards(deckID, 1);
            cardsRemaining--;
        }
    }

    private void configureSpinnerData() {
        Integer[] items = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        spinnerDecks.setAdapter(adapter);
    }

    private void configureHandlerForGetDecks() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            linearMain.setVisibility(View.GONE);
            linearSecond.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }, 2500);
    }

    private void configureRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager
                (getApplicationContext(), 5);
        recyclerView.setLayoutManager(layoutManager);
        cardsRecyclerView = new CardsRecyclerView(getApplicationContext(), imgUrls);
        recyclerView.setAdapter(cardsRecyclerView);
    }
}
