package com.joselopezrosario.vgsales.filemaker_api_vgsales.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.joselopezrosario.vgsales.filemaker_api_vgsales.R;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.api.FMApi;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.PreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    TextInputEditText vPlatform;
    TextInputEditText vPublisher;
    TextInputEditText vGenre;
    TextInputEditText vName;
    TextInputEditText vLimit;
    TextInputEditText vOffset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        vPlatform = findViewById(R.id.platform);
        vPublisher = findViewById(R.id.publisher);
        vGenre = findViewById(R.id.genre);
        vName = findViewById(R.id.name);
        vLimit = findViewById(R.id.limit);
        vOffset = findViewById(R.id.offset);
        PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
        vPlatform.setText(prefs.loadString(FMApi.FIELD_PLATFORM,""));
        vPublisher.setText(prefs.loadString(FMApi.FIELD_PUBLISHER,""));
        vGenre.setText(prefs.loadString(FMApi.FIELD_GENRE,""));
        vName.setText(prefs.loadString(FMApi.FIELD_NAME,""));
        vLimit.setText(prefs.loadString("limit","1"));
        vOffset.setText(prefs.loadString("offset","1"));
        final Button findButton = findViewById(R.id.find);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });
    }

    public void find() {
       saveQuery();
       finish();
    }

    private void saveQuery() {
        JSONObject json;
        vPlatform = findViewById(R.id.platform);
        vPublisher = findViewById(R.id.publisher);
        vGenre = findViewById(R.id.genre);
        vName = findViewById(R.id.name);
        vLimit = findViewById(R.id.limit);
        vOffset = findViewById(R.id.offset);
        String platform = "";
        String publisher = "";
        String genre = "";
        String name = "";
        String limit = "";
        String offset = "";
        if (vPlatform.getText() != null) {
            platform = vPlatform.getText().toString();
        }
        if (vPublisher.getText() != null) {
            publisher = vPublisher.getText().toString();
        }
        if (vGenre.getText() != null) {
            genre = vGenre.getText().toString();
        }
        if (vName.getText() != null) {
            name = vName.getText().toString();
        }
        if (vLimit.getText() != null) {
            limit = vLimit.getText().toString();
        }
        if (vOffset.getText() != null) {
            offset = vOffset.getText().toString();
        }
        try {
            json = new JSONObject();
            JSONArray queryArray = new JSONArray();
            JSONObject pairs = new JSONObject()
                    .put(FMApi.FIELD_PLATFORM, "=" + platform)
                    .put(FMApi.FIELD_PUBLISHER, "=" + publisher)
                    .put(FMApi.FIELD_GENRE, "=" + genre)
                    .put(FMApi.FIELD_NAME, "=" + name);
            queryArray.put(pairs);
            json.put("query", queryArray);
            json.put("limit", limit);
            json.put("offset", offset);
            PreferencesHelper prefs = new PreferencesHelper(getApplicationContext());
            prefs.save(FMApi.FIELD_PLATFORM, platform);
            prefs.save(FMApi.FIELD_PUBLISHER, publisher);
            prefs.save(FMApi.FIELD_GENRE, genre);
            prefs.save(FMApi.FIELD_NAME, name);
            prefs.save("query", json.toString());
            prefs.save("limit", limit);
            prefs.save("offset", offset);
        } catch (JSONException e) {
            System.out.println(e.toString());
        }
    }
}
