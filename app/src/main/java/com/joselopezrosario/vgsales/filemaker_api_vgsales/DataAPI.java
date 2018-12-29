package com.joselopezrosario.vgsales.filemaker_api_vgsales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressWarnings("SameParameterValue")
public final class DataAPI {
    final static String FIELD_ID = "ID";
    final static String FIELD_RANK = "Rank";
    final static String FIELD_NAME = "Name";
    final static String FIELD_PLATFORM = "Platform";
    final static String FIELD_YEAR = "Year";
    final static String FIELD_GENRE = "Genre";
    final static String FIELD_PUBLISHER = "Publisher";
    final static String FIELD_NA_SALES = "NA_Sales";
    final static String FIELD_EU_SALES = "EU_Sales";
    final static String FIELD_JP_SALES = "JP_Sales";
    final static String FIELD_OTHER_SALES = "Other_Sales";
    final static String FIELD_GLOBAL_SALES = "Global_Sales";

    private static String ENDPOINT = "https://192.168.0.7/fmi/data/v1/databases/VideoGameSales";

    public DataAPI() {
        throw new AssertionError("No API instances for you!");
    }

    /**
     * login
     *
     * @param accountName the FileMaker Account with fmrest privileges
     * @param password    the FileMaker account's password
     * @return the response token
     * See the FileMaker Data API documentation at yourhost/fmi/data/apidoc/#api-Authentication-Login
     */
    static String login(String accountName, String password) {
        final MediaType postDataMediaType = MediaType.parse("");
        String encodedCredentials = Utilities.
                encodeFileMakerCredentials(accountName, password);
        if (encodedCredentials == null) {
            return null;
        }
        String token;
        // Create an OkHTTPClient and call the FileMaker Data API
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(ENDPOINT + "/sessions")
                .post(RequestBody.create(postDataMediaType, "{}"))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ("Basic " + encodedCredentials))
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.print("login IOException: " + e.toString());
            return null;
        }
        if (response.code() != 200) {
            System.out.print("login failed: " + response.code());
            return null;
        }
        token = response.header("X-FM-Data-Access-Token");
        return token;
    }

    /**
     * getFoundSet
     *
     * @param token  the FileMaker Data API Authorization token
     * @param layout the FileMaker layout
     * @param params additional parameters for offset, limit, sort, and portals
     * @return the ResponseBody of the API call or null
     */
    static JSONArray getRecords(String token, String layout, String params) {
        if (token == null || layout == null) {
            return null;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records?" + params;
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ("Bearer " + token))
                .build();
        Response APIResponse;
        try {
            APIResponse = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.print("showAllRecords IOException: " + e.toString());
            return null;
        }
        if (APIResponse.code() != 200) {
            return null;
        }
        ResponseBody APIResponseBody = APIResponse.body();
        if (APIResponseBody == null) {
            return null;
        }
        try {
            JSONObject result = new JSONObject(APIResponseBody.string());
            JSONObject response = result.getJSONObject("response");
            return response.getJSONArray("data");
        } catch (IOException | JSONException e) {
            return null;
        }
    }

    /**
     * createRecord
     *
     * @param token     the FileMaker Data API Authorization token
     * @param layout    the FileMaker layout
     * @param fieldData a stringify JSON objects containing field-value pairs
     * @param params    optional parameters
     * @return the new record's id
     * For more information see yourhost/fmi/data/apidoc/#api-Record-createRecord
     */
    static int createRecord(String token, String layout, RequestBody fieldData, String params) {
        if (token == null || layout == null || fieldData == null) {
            return 0;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records?" + params;
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ("Bearer " + token))
                .post(fieldData)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.print("logout IOException: " + e.toString());
            return 0;
        }
        int code = response.code();
        if (code != 200) {
            return 0;
        }
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return 0;
        }
        String responseString;
        try {
            responseString = responseBody.string();
            JSONObject responseArray = new JSONObject(responseString);
            JSONObject responseObject = responseArray.getJSONObject("response");
            return responseObject.getInt("recordId");
        } catch (IOException | JSONException e) {
            return 0;
        }
    }

    /**
     * logout
     *
     * @param token the FileMaker Data API Authorization token
     * @return true if the API response code = 200, false for all other errors
     */
    static boolean logOut(String token) {
        if (token == null) {
            return false;
        }
        String url = ENDPOINT + "/sessions/" + token;
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ("Bearer " + token))
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.code() == 200;
        } catch (IOException e) {
            System.out.print("logout IOException: " + e.toString());
            return false;
        }
    }
}
