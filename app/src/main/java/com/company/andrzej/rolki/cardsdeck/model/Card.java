package com.company.andrzej.rolki.cardsdeck.model;

import android.support.annotation.NonNull;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Andrzej on 2017-04-28.
 */

public final class Card implements Comparable<Card> {

    public String getImage() {
        return image;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public String getCode() {
        return code;
    }

    @SerializedName("image")
    private final String image = null;
    @SerializedName("value")
    @JsonAdapter(RankTypeAdapter.class)
    private final int rank = 0;
    @SerializedName("suit")
    private final Suit suit = null;
    @SerializedName("code")
    private final String code = null;

    @Override
    public int compareTo(@NonNull Card other) {
        if(this.rank == ((Card) other).rank){
            return 0;
        }else if((this.rank) > ((Card) other).rank){
            return 1;
        }else
            return -1;
    }
}

enum Suit {
    @SuppressWarnings("SPADES")SPADES,
    @SerializedName("HEARTS")HEARTS,
    @SuppressWarnings("DIAMONDS")DIAMONDS,
    @SerializedName("CLUBS")CLUBS
}

final class RankTypeAdapter
        extends TypeAdapter<Integer> {

    private static final List<String> ranks = asList("ACE", "2", "3", "4", "5", "6", "7", "8",
            "9", "10", "JACK", "QUEEN", "KING");

    private RankTypeAdapter() {
    }

    @Override
    public void write(final JsonWriter out, final Integer value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer read(final JsonReader in)
            throws IOException {
        final String rank = in.nextString();
        final int index = ranks.indexOf(rank);
        if (index == -1) {
            throw new MalformedJsonException("Cannot parse: " + rank + " at " + in);
        }
        return index + 1;
    }
}
