package com.joselopezrosario.vgsales.filemaker_api_vgsales;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;

public class IntentServiceAPI extends IntentService {
    public static final String SERVICE_NAME = "IntentServiceAPI";

    public IntentServiceAPI() {
        super(SERVICE_NAME);
    }

    protected void onHandleIntent(Intent intent) {
        String token = DataAPI.login(DataAPI.ACCOUNTNAME, DataAPI.PASSWORD);
        if ( token == null ){
            intent.putExtra("statusLogin", false);
            callBack(intent);
            return;
        }else{
            intent.putExtra("statusLogin", true);
        }
        JSONArray data = DataAPI.getRecords(token, DataAPI.LAYOUT_VGSALES, "_limit=25");
        if ( data == null ){
            intent.putExtra("statusGetData", false);
            callBack(intent);
            return;
        } else{
            intent.putExtra("data", data.toString());
        }
        boolean logout = DataAPI.logOut(token);
        intent.putExtra("statusLogout", logout);
        callBack(new Intent(SERVICE_NAME).putExtras(intent));
    }

    private void callBack(Intent intent){
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(new Intent(SERVICE_NAME).putExtras(intent));
    }
}
