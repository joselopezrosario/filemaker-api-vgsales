package com.joselopezrosario.vgsales.filemaker_api_vgsales.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.joselopezrosario.vgsales.filemaker_api_vgsales.api.FMApi;

import org.json.JSONArray;

public class MainActivityIntentService extends IntentService {
    public static final String SERVICE_NAME = "MainActivityIntentService";

    public MainActivityIntentService() {
        super(SERVICE_NAME);
    }

    protected void onHandleIntent(Intent intent) {
        String token = FMApi.login(FMApi.ACCOUNTNAME, FMApi.PASSWORD).getFmToken();
        if ( token == null ){
            intent.putExtra("statusLogin", false);
            callBack(intent);
            return;
        }else{
            intent.putExtra("statusLogin", true);
        }
        JSONArray data = FMApi.getRecords(token, FMApi.LAYOUT_VGSALES, "_limit=25").getFmData();
        if ( data == null ){
            intent.putExtra("statusGetData", false);
            callBack(intent);
            return;
        } else{
            intent.putExtra("data", data.toString());
        }
        boolean logout = FMApi.logOut(token).isSuccess();
        intent.putExtra("statusLogout", logout);
        callBack(new Intent(SERVICE_NAME).putExtras(intent));
    }

    private void callBack(Intent intent){
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(new Intent(SERVICE_NAME).putExtras(intent));
    }
}
