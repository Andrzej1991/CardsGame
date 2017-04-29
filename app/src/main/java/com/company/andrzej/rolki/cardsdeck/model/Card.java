package com.company.andrzej.rolki.cardsdeck.model;

/**
 * Created by Andrzej on 2017-04-28.
 */

public class Card  {

    private String image;
    private int value;
    private String suit;
    private String code;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
