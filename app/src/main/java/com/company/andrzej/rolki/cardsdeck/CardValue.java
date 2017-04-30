package com.company.andrzej.rolki.cardsdeck;

/**
 * Created by Andrzej on 2017-04-29.
 */

public enum CardValue {
    ACE("ACE",1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("JACK", 11),
    QUEEN("QUEEN", 12),
    KING("KING", 13);

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    private String value;
    private int cardValue;

     CardValue(String value, int cardValue) {
        this.value = value;
        this.cardValue = cardValue;
    }
}
