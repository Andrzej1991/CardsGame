package com.company.andrzej.rolki.cardsdeck.component;

import com.company.andrzej.rolki.cardsdeck.MainActivity;
import com.company.andrzej.rolki.cardsdeck.module.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Andrzej on 2017-04-28.
 */

@Singleton
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {
    void inject(MainActivity activity);
}
