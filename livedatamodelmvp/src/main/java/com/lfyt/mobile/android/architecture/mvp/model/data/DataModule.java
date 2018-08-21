package com.lfyt.mobile.android.architecture.mvp.model.data;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class DataModule {


    @Provides
    @Singleton
    DataComponent dataComponent(DataStateAPI dataStateAPI, Realm database, Application application){
        return new DataComponent(dataStateAPI, database, application);
    }
}
