package com.joselopezrosario.vgsales.filemaker_api_vgsales.api;


import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.Utilities;

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
public final class FMApi {
    private static String ENDPOINT = "https://192.168.0.7/fmi/data/v1/databases/VideoGameSales";

    public final static String ACCOUNTNAME = "Jose";
    public final static String PASSWORD = "ErS9WeQKa3BVJk5t";
    public final static String LAYOUT_VGSALES = "vgsales";
    public final static String FIELD_ID = "ID";
    public final static String FIELD_RANK = "Rank";
    public final static String FIELD_NAME = "Name";
    public final static String FIELD_PLATFORM = "Platform";
    public final static String FIELD_YEAR = "Year";
    public final static String FIELD_GENRE = "Genre";
    public final static String FIELD_PUBLISHER = "Publisher";
    public final static String FIELD_NA_SALES = "NA_Sales";
    public final static String FIELD_EU_SALES = "EU_Sales";
    public final static String FIELD_JP_SALES = "JP_Sales";
    public final static String FIELD_OTHER_SALES = "Other_Sales";
    public final static String FIELD_GLOBAL_SALES = "Global_Sales";

    private static String RESPONSE = "response";
    private static String DATA = "data";

    private static String SESSIONS = "/sessions";
    private static String CONTENT_TYPE = "Content-Type";
    private static String APPLICATION_JSON = "application/json";
    private static String AUTHORIZATION = "Authorization";
    private static String BASIC = "Basic ";
    private static String BEARER = "Bearer ";

    public FMApi() {
        throw new AssertionError("No API instances for you!");
    }

    /**
     * login
     * POST METHOD
     *
     * @param accountName the FileMaker Account with fmrest privileges
     * @param password    the FileMaker account's password
     * @return the response token
     * See the FileMaker Data API documentation at yourhost/fmi/data/apidoc/#api-Authentication-Login
     */
    public static FMApiResponse login(String accountName, String password) {
        FMApiResponse fmar = new FMApiResponse();
        if (accountName == null || password == null) {
            return fmar;
        }
        final MediaType postDataMediaType = MediaType.parse("");
        String encodedCredentials = Utilities.
                encodeFileMakerCredentials(accountName, password);
        if (encodedCredentials == null) {
            return fmar;
        }
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(ENDPOINT + SESSIONS)
                .post(RequestBody.create(postDataMediaType, "{}"))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, BASIC + encodedCredentials)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.print("login IOException: " + e.toString());
            return fmar;
        }
        int code = response.code();
        if (code != 200) {
            System.out.print("login failed: " + response.code());
            fmar.setResponseCode(code);
            fmar.setSuccess(true);
            return fmar;
        }
        fmar.setToken(response.header("X-FM-Data-Access-Token"));
        return fmar;
    }

    /**
     * getRecords
     * GET METHOD
     *
     * @param token  the FileMaker Data API Authorization token
     * @param layout the FileMaker layout
     * @param params additional parameters for offset, limit, sort, and portals
     * @return the ResponseBody of the API call or null
     */
    public static FMApiResponse getRecords(String token, String layout, String params) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records?" + params;
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, BEARER + token)
                .build();
        Response APIResponse;
        try {
            APIResponse = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.print("getRecords IOException: " + e.toString());
            return fmar;
        }
        int code = APIResponse.code();
        if (code != 200) {
            fmar.setResponseCode(code);
            return fmar;
        }
        ResponseBody APIResponseBody = APIResponse.body();
        if (APIResponseBody == null) {
            return fmar;
        }
        try {
            JSONObject result = new JSONObject(APIResponseBody.string());
            JSONObject response = result.getJSONObject(RESPONSE);
            fmar.setData(response.getJSONArray(DATA));
            fmar.setSuccess(true);
            return fmar;
        } catch (IOException | JSONException e) {
            return fmar;
        }
    }

    /**
     * createRecord
     * POST METHOD
     *
     * @param token     the FileMaker Data API Authorization token
     * @param layout    the FileMaker layout
     * @param fieldData a stringify JSON objects containing field-value pairs
     * @return the new record's id
     * For more information see yourhost/fmi/data/apidoc/#api-Record-createRecord
     */
    public static FMApiResponse createRecord(String token, String layout, RequestBody fieldData) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null || fieldData == null) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records?";
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
            return fmar;
        }
        int code = response.code();
        if (code != 200) {
            fmar.setResponseCode(code);
            return fmar;
        }
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return fmar;
        }
        String responseString;
        try {
            responseString = responseBody.string();
            JSONObject responseObject = new JSONObject(responseString).getJSONObject("response");
            fmar.setSuccess(true);
            fmar.setRecordId(responseObject.getString("recordId"));
            return fmar;
        } catch (IOException | JSONException e) {
            return fmar;
        }
    }

    /**
     * editRecord
     * PATCH METHOD
     *
     * @param token     the FileMaker Data API Authorization token
     * @param layout    the FileMaker layout
     * @param recordId  the record's id to edit
     * @param fieldData a stringify JSON objects containing field-value pairs
     * @return true if edit was successul, false if not
     */
    public static FMApiResponse editRecord(String token, String layout, String recordId, RequestBody fieldData) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null || recordId.isEmpty()) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records/" + recordId;
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, BEARER + token)
                .patch(fieldData)
                .build();
        Response APIResponse;
        try {
            APIResponse = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.print("editRecord IOException: " + e.toString());
            return fmar;
        }
        fmar.setSuccess(true);
        fmar.setResponseCode(APIResponse.code());
        return fmar;
    }

    /**
     * deleteRecord
     * DELETE METHOD
     *
     * @param token    the FileMaker Data API Authorization token
     * @param layout   the FileMaker layout
     * @param recordId the id of the record to delete
     * @return true if the record is deleted, false if there's an error
     * For more information see yourhost/fmi/data/apidoc/#api-Record-editRecord
     */
    public static FMApiResponse deleteRecord(String token, String layout, String recordId) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null || recordId.isEmpty()) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records/" + recordId;
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, BEARER + token)
                .delete()
                .build();
        Response APIResponse;
        try {
            APIResponse = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.print("deleteRecord IOException: " + e.toString());
            return fmar;
        }
        fmar.setSuccess(true);
        fmar.setResponseCode(APIResponse.code());
        return fmar;
    }

    /**
     * logout
     * DELETE METHOD
     *
     * @param token the FileMaker Data API Authorization token
     * @return true if the API response code = 200, false for all other errors
     */
    public static FMApiResponse logOut(String token) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null) {
            return fmar;
        }
        String url = ENDPOINT + "/sessions/" + token;
        OkHttpClient client = UnsecureOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, BEARER + token)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            fmar.setResponseCode(response.code());
            fmar.setSuccess(true);
            return fmar;
        } catch (IOException e) {
            System.out.print("logout IOException: " + e.toString());
            return fmar;
        }
    }

}
