package com.company.andrzej.rolki.cardsdeck.model;

/**
 * Created by Andrzej on 2017-04-28.
 */

public class Card  {

    private String imageUrl;
    private String value;
    private String suit;
    private String code;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
