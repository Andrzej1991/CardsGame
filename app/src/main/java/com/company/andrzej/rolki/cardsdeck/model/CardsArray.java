package com.company.andrzej.rolki.cardsdeck.model;

import java.util.List;

/**
 * Created by Andrzej on 2017-04-29.
 */

public class CardsArray {

    public boolean isSucces() {
        return succes;
    }

    public void setSucces(boolean succes) {
        this.succes = succes;
    }

    public List<Card> getArrayCards() {
        return cards;
    }

    public void setArrayCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(String deck_id) {
        this.deck_id = deck_id;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    private boolean succes;
    private List<Card> cards;
    private String deck_id;
    private int remaining;
}
