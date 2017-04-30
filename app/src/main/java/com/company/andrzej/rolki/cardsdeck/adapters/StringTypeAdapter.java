package com.company.andrzej.rolki.cardsdeck.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by Andrzej on 2017-04-29.
 */

public class StringTypeAdapter extends TypeAdapter<String> {


    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (Objects.equals(value, "ACE")) {
            return;
        }
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        if(in.peek() == JsonToken.NULL){
            in.nextNull();
            return null;
        }
        String stringValue = in.nextString();
        try{
            return String.valueOf(stringValue);
        }catch(NumberFormatException e){
            return null;
        }
    }
}
