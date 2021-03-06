package com.lfyt.mobile.android.architecture.mvp.model.data;

import android.app.Application;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Component need to create the live data model
 * with a more simple setup
 */
public class DataComponent {

	private final DataStateAPI dataStateAPI;
	private final Realm database;
	private final Application application;

	public DataComponent(DataStateAPI dataStateAPI, Realm database, Application application) {
		this.dataStateAPI = dataStateAPI;
		this.database = database;
		this.application =  application;
	}

	public DataStateAPI getDataStateAPI() {
		return dataStateAPI;
	}

	public Realm getDatabase() {
		return database;
	}

	public Application getApplication(){
		return application;
	}
}