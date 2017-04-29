package com.company.andrzej.rolki.cardsdeck;

import android.app.Application;

import com.company.andrzej.rolki.cardsdeck.Component.DaggerServiceComponent;
import com.company.andrzej.rolki.cardsdeck.Component.ServiceComponent;
import com.company.andrzej.rolki.cardsdeck.Module.ServiceModule;

/**
 * Created by Andrzej on 2017-04-29.
 */

public class MyApplication extends Application {

    private static MyApplication application;
    private ServiceComponent serviceComponent;
    String url = "https://deckofcardsapi.com/api/deck/";

    public static MyApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        injectServiceComponent();
    }

    private void injectServiceComponent() {
        serviceComponent = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(url))
                .build();
    }

    public ServiceComponent serviceComponent() {
        return serviceComponent;
    }
}
