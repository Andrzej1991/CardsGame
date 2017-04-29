package com.company.andrzej.rolki.cardsdeck.service;


import com.company.andrzej.rolki.cardsdeck.model.Card;
import com.company.andrzej.rolki.cardsdeck.model.Cards;
import com.company.andrzej.rolki.cardsdeck.model.Deck;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andrzej on 2017-04-28.
 */

public class CardService {
    public interface CardAPI {

        //GET NEW DECK
        @GET("new/shuffle")
        Observable<Deck> getDeck(@Query("deck_count") int deck_count);

        //GET CARDS
        @GET("{deck_id}/draw")
        Observable<Card> getCard(@Path("deck_id") String deck_id, @Query("count") int count);

        @GET("{deck_id}/draw")
        Observable<Cards> getCards(@Path("deck_id") String deck_id, @Query("count") int count);
    }
}
