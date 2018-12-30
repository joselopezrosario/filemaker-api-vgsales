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

import com.joselopezrosario.vgsales.filemaker_api_vgsales.service.IntentServiceAPI;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.R;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.Utilities;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.adapter.VideoGamesListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    RecyclerView.Adapter mAdapter;
    RecyclerView mRecyclerView;

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

        Intent serviceIntent = new Intent(this, IntentServiceAPI.class);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new DownloadStateReceiver(), new IntentFilter(IntentServiceAPI.SERVICE_NAME));
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() == null) {
                Utilities.showToast(context, "LOL", Toast.LENGTH_SHORT);
                return;
            }
            boolean statusLogin = intent.getExtras().getBoolean("statusLogin");
            if (!statusLogin) {
                Utilities.showToast(context, "Login error", Toast.LENGTH_SHORT);
                return;
            }
            String data = intent.getExtras().getString("data");
            if (data == null) {
                Utilities.showToast(context, "No records found", Toast.LENGTH_SHORT);
                return;
            }
            try {
                JSONArray dataArray = new JSONArray(data);
                mAdapter = new VideoGamesListAdapter(getApplicationContext(), dataArray);
                mRecyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                System.out.println("Parsing error");
            }
        }
    }
}
