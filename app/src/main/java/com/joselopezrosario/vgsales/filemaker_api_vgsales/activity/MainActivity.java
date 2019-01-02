package com.joselopezrosario.vgsales.filemaker_api_vgsales.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joselopezrosario.vgsales.filemaker_api_vgsales.R;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.adapter.VideoGamesListAdapter;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.api.FMApi;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.data.VideoGameSale;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.service.MainActivityIntentService;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.PreferencesHelper;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView.LayoutManager mLayoutManager;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.vg_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        if ( prefs.loadString(FMApi.QUERY,null) == null){
            initPrefs();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent serviceIntent = new Intent(this, MainActivityIntentService.class);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new DownloadStateReceiver(), new IntentFilter(MainActivityIntentService.SERVICE_NAME));
        this.startService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() == null) {
                Utilities.showToast(context, "Could not fetch data", Toast.LENGTH_SHORT);
                return;
            }
            boolean statusLogin = intent.getExtras().getBoolean("statusLogin");
            if (!statusLogin) {
                Utilities.showToast(context, "Login error", Toast.LENGTH_SHORT);
                return;
            }
            String data = intent.getExtras().getString(FMApi.DATA);
            if (data == null) {
                Utilities.showToast(context, "No records found", Toast.LENGTH_SHORT);
                return;
            }
            try {
                JSONArray dataArray = new JSONArray(data);
                RecyclerView.Adapter mAdapter = new VideoGamesListAdapter(getApplicationContext(), dataArray);
                mRecyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                System.out.println("Parsing error");
            }
        }
    }

    /**
     * initPrefs
     */
    private void initPrefs() {
        try {
            JSONObject json = new JSONObject();
            JSONArray queryArray = new JSONArray();
            JSONObject pairs = new JSONObject()
                    .put(VideoGameSale.FIELD_PLATFORM, "=NES")
                    .put(VideoGameSale.FIELD_PUBLISHER, "=Nintendo")
                    .put(VideoGameSale.FIELD_GENRE, "=*")
                    .put(VideoGameSale.FIELD_NAME, "=*");
            queryArray.put(pairs);
            json.put(FMApi.QUERY, queryArray);
            json.put(FMApi.LIMIT, "25");
            json.put(FMApi.OFFSET, "1");
            PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
            prefs.save(VideoGameSale.FIELD_PLATFORM, "NES");
            prefs.save(VideoGameSale.FIELD_PUBLISHER, "Nintendo");
            prefs.save(VideoGameSale.FIELD_GENRE, "");
            prefs.save(VideoGameSale.FIELD_NAME, "");
            prefs.save(FMApi.QUERY, json.toString());
            prefs.save(FMApi.LIMIT, "25");
            prefs.save(FMApi.OFFSET, "1");
        } catch (JSONException e) {
            System.out.println(e.toString());

        }
    }
}
