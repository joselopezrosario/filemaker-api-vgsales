package com.joselopezrosario.vgsales.filemaker_api_vgsales.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.joselopezrosario.vgsales.filemaker_api_vgsales.R;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.api.FMApi;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.PreferencesHelper;
import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.Utilities;

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
       SaveQueryResult sqr = saveQuery();
       if ( !sqr.isSuccess()){
           Utilities.showToast(getApplicationContext(),sqr.getMessage(), Toast.LENGTH_SHORT);
       }else{
           finish();
       }
    }

    private SaveQueryResult saveQuery() {
        SaveQueryResult sqr = new SaveQueryResult();
        JSONObject json;
        vPlatform = findViewById(R.id.platform);
        vPublisher = findViewById(R.id.publisher);
        vGenre = findViewById(R.id.genre);
        vName = findViewById(R.id.name);
        vLimit = findViewById(R.id.limit);
        vOffset = findViewById(R.id.offset);
        String platform = null;
        String publisher = null;
        String genre = null;
        String name = null;
        String limit = null;
        String offset = null;
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
        if ( limit == null || limit.isEmpty() || limit.equals("0")){
            sqr.setSuccess(false);
            sqr.setMessage("The limit value must be at least 1");
            return sqr;
        }
        if ( offset == null || offset.isEmpty() || offset.equals("0")){
            sqr.setSuccess(false);
            sqr.setMessage("The offset value must be at least 1");
            return sqr;
        }
        try {
            json = new JSONObject();
            JSONArray queryArray = new JSONArray();
            JSONObject pairs = new JSONObject()
                    .put(FMApi.FIELD_PLATFORM, "=" + fmqString(platform))
                    .put(FMApi.FIELD_PUBLISHER, "=" + fmqString(publisher))
                    .put(FMApi.FIELD_GENRE, "=" + fmqString(genre))
                    .put(FMApi.FIELD_NAME, "=" + fmqString(name));
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
            sqr.setSuccess(true);
            return sqr;
        } catch (JSONException e) {
            System.out.println(e.toString());
            sqr.setSuccess(false);
            sqr.setMessage(e.toString());
            return sqr;
        }
    }

    /**
     * fmqString (Filemaker Query String)
     * If a value for the query is empty or null, convert it to an asterisk (*)
     * In FileMaker, the * operator is used to search for all values
     * @param value the strings to be used in a query
     * @return if the value is empty or null, return a *, else return back the value
     */
    private String fmqString(String value){
        if ( value == null || value.equals("") ){
            return "*";
        }else{
            return value;
        }
    }

    private class SaveQueryResult{
        boolean success;
        String message;
        SaveQueryResult() {
        }

        boolean isSuccess() {
            return success;
        }

        void setSuccess(boolean success) {
            this.success = success;
        }

        String getMessage() {
            return message;
        }

        void setMessage(String message) {
            this.message = message;
        }
    }
}
