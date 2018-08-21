package com.lfyt.mobile.android.architecture.mvp.model.data;

import android.support.annotation.CallSuper;

import com.lfyt.mobile.android.frameworkmvp.archtecture.L;
import com.lfyt.mobile.android.frameworkmvp.archtecture.mvp.model.LiveModelMVP;
import com.squareup.otto.Subscribe;

import io.realm.Realm;
import io.realm.RealmModel;


/**
 * Live Data Model is an extension of @{@link LiveModelMVP}
 * that can:
 * 		*post event to subscribers
 * 		*inject its dependencies and subscribe to it
 * 		*auto manage its data
*/
public abstract class LiveDataModelMVP<T extends RealmModel> extends LiveModelMVP {


	/**
	 * Constructor of data model
	 * @param liveDataModelComponent
	 */
	protected LiveDataModelMVP(LiveDataModelComponent liveDataModelComponent){
		super(liveDataModelComponent.getApplication());
		setupLiveDataModel(liveDataModelComponent);
	}


	///////////////////////////////////////////////////////////////////////////
	// State Auto Management
	///////////////////////////////////////////////////////////////////////////

	/**
	 * The current data of the model
	 * Realm class with attributes that is auto-loaded && auto-saved
	 */
	public T data;

	/**
	 * The data manager, that manages the auto-saving
	 */
	private LiveDataModelComponent liveDataModelComponent;




	///////////////////////////////////////////////////////////////////////////
	// Setup Data Auto Management
	///////////////////////////////////////////////////////////////////////////
	private void setupLiveDataModel(LiveDataModelComponent liveDataModelComponent) {

		//Set instance of data manager
		this.liveDataModelComponent = liveDataModelComponent;

		//Load the last data
		loadData();

		//Setup Auto Save Instance Code
		liveDataModelComponent.getDataStateAPI().subscribe(new Object(){

			@Subscribe
			public void onSaveStateEvent(LiveDataModelStateAPI.SaveDataEvent event){
				saveData();
			}

		});

	}




	///////////////////////////////////////////////////////////////////////////
	// Load, Save && Reset Data
	///////////////////////////////////////////////////////////////////////////

	public final void load(){
		loadData();
	}


	public void save(){
		saveData();
	}


	public void reset(){
		resetData();
	}


	///////////////////////////////////////////////////////////////////////////
	// METHODS
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Auto-load the data from database
	 * if doesn't exist, load from default instance
	 */
	private void loadData(){

		//Get Database Instance
		Realm database = liveDataModelComponent.getDatabase();

		//Setup default data instance
		data = setupDefaultData();


		//Get data instance from db
		T databaseObjectInstance = null;

		try{
			//Get first instance of data class
			databaseObjectInstance = (T) database.where(data.getClass()).findFirst();
		}catch (Exception e){
			L.EXP(this, e);
		}

		//If doesn't have data, it was loaded from default instance
		if( databaseObjectInstance == null )
		{
			onLoadedFromDefaultInstance();
			L.D(this, "==> DATA LOADED | %s from ~> DEFAULT-INSTANCE <~", data.getClass().getSimpleName());
			return;
		}

		//If has data, then copy realm object to java object from db
		data = database.copyFromRealm(databaseObjectInstance);
		L.D(this, "==> DATA LOADED | %s from ~> DATABASE <~", data.getClass().getSimpleName());

	}


	/**
	 * Save the current data to the realm database
	 */
	private final void saveData() {

		Realm database = liveDataModelComponent.getDatabase();

		//Execute the transaction
		database.beginTransaction();
		database.insertOrUpdate(data);
		database.commitTransaction();

		L.D(this, " DATA ==> SAVED");
	}


	/**
	 * reset the data to the default instance
	 */
	private final void resetData(){
		data = setupDefaultData();
		onLoadedFromDefaultInstance();
	}









	///////////////////////////////////////////////////////////////////////////
	// Setup Default Instance
	///////////////////////////////////////////////////////////////////////////
	/**
	 * The class that extend this class must implement this method
	 * An provide a default instance for the data ( new DataA(); data.a=1 )
	 * @return an instance of the default data
	 */
	protected abstract T setupDefaultData();

	protected boolean isLoadedFromDefaultInstace = false;

	@CallSuper
	protected void onLoadedFromDefaultInstance() {
		isLoadedFromDefaultInstace = true;
	}




}

