package com.lfyt.mobile.android.architecture.mvp.model.data;

import com.lfyt.mobile.android.frameworkmvp.archtecture.L;
import com.lfyt.mobile.android.frameworkmvp.archtecture.application.ApplicationLifecycleAPI;
import com.lfyt.mobile.android.livemodel.Event;
import com.lfyt.mobile.android.livemodel.LiveModel;
import com.squareup.otto.Subscribe;

public class DataStateAPI extends LiveModel {

	public DataStateAPI(ApplicationLifecycleAPI applicationStateAPI) {
		applicationStateAPI.subscribe(mApplicationStateModelSubscriptionCode);
		/*webServiceStateAPI.subscribe(mWebServiceStateModelSubscriptionCode);*/
	}



    ///////////////////////////////////////////////////////////////////////////
    // Save Data
    ///////////////////////////////////////////////////////////////////////////

    public final void saveApplicationData(){
        post(new SaveDataEvent());
    }

	public class SaveDataEvent extends Event { }




	///////////////////////////////////////////////////////////////////////////
	// Listen for states where the app should auto save the data
	///////////////////////////////////////////////////////////////////////////

    private void autoSaveData() {
        saveApplicationData();
    }


    //Application Stopped
	private final Object mApplicationStateModelSubscriptionCode = new Object(){

		@Subscribe
		public void onApplicationStoppedWrapper(ApplicationLifecycleAPI.ApplicationStopped applicationStopped){
			onApplicationStopped();
		}

	};

	private void onApplicationStopped() {
		L.D(this, "Application STOPPED ~> SAVING STATE <~");
		autoSaveData();
	}


	//Last Web Service Request Received
	/*
	private final Object mWebServiceStateModelSubscriptionCode = new Object(){

		@Subscribe
		public void onWebServiceFinishedRequestsWrapper(WebServiceStateAPI.WebServiceFinishedRequests lastRequestResponseEvent){
			if( applicationStateAPI.isStopped() ){
				onWebServiceFinishedRequests();
			}
		}
	};
	*/

	private void onWebServiceFinishedRequests() {
		L.D(this, "WebServiceAPI response AFTER Application STOP ~> SAVING STATE <~");
		autoSaveData();
	}




}
