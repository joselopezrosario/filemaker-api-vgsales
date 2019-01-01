package com.joselopezrosario.vgsales.filemaker_api_vgsales.api;


import android.support.annotation.Nullable;

import com.joselopezrosario.vgsales.filemaker_api_vgsales.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressWarnings("SameParameterValue")
public final class FMApi {
    private static final String ENDPOINT = "https://192.168.0.7/fmi/data/v1/databases/VideoGameSales";
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
    public final static String QUERY = "query";
    public final static String LIMIT = "limit";
    public final static String OFFSET = "offset";

    private static final String RESPONSE = "response";
    private static final String DATA = "data";
    private static final String POST = "POST";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "delete";
    private static final String SESSIONS = "/sessions";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";
    private static final String BEARER = "Bearer ";
    private static final String FM_TOKEN_ELEMENT = "X-FM-Data-Access-Token";

    /**
     * login
     * POST METHOD
     *
     * @param accountName the FileMaker Account with fmrest privileges
     * @param password    the FileMaker account's password
     * @return an FMApiResponse object
     * See the FileMaker Data API documentation at yourhost/fmi/data/apidoc/#api-Authentication-Login
     */
    public static FMApiResponse login(String accountName, String password) {
        FMApiResponse fmar = new FMApiResponse();
        if (accountName == null || password == null) {
            return fmar;
        }
        String url = ENDPOINT + SESSIONS;
        Headers headers = getBasicHeaders(accountName, password);
        if (headers == null) {
            return fmar;
        }
        RequestBody body = getBody(null);
        fmar = runCall(POST, url, headers, body);
        if (!fmar.isSuccess()) {
            return fmar;
        }
        fmar.setFmToken(fmar.getHttpHeaders().get(FM_TOKEN_ELEMENT));
        return fmar;
    }

    /**
     * getRecords
     * GET METHOD
     *
     * @param token  the FileMaker Data API Authorization token
     * @param layout the FileMaker layout
     * @param params additional parameters for offset, limit, sort, and portals
     * @return an FMApiResponse object
     */
    public static FMApiResponse getRecords(String token, String layout, String params) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records?" + params;
        Headers headers = getBearerHeaders(token);
        String GET = "GET";
        fmar = runCall(GET, url, headers, null);
        if (!fmar.isSuccess()) {
            return fmar;
        }
        ResponseBody responseBody = fmar.getHttpResponseBody();
        if (responseBody == null) {
            return fmar;
        }
        try {
            JSONObject responseBodyObject = new JSONObject(responseBody.string());
            JSONObject fmResponse = responseBodyObject.getJSONObject(RESPONSE);
            JSONArray fmData = fmResponse.getJSONArray(DATA);
            fmar.setFmResponse(fmResponse);
            fmar.setFmData(fmData);
            fmar.setSuccess(true);
            return fmar;
        } catch (IOException | JSONException e) {
            return fmar;
        }
    }

    /**
     * findRecords
     * POST METHOD
     *
     * @param token  the FileMaker Data API Authorization token
     * @param layout the FileMaker layout
     * @param query  a stringify JSON objects containing field-find criteria pairs
     * @return an FMApiResponse object
     * For more information see yourhost/fmi/data/apidoc/#api-Find-Find
     */
    public static FMApiResponse findRecords(String token, String layout, RequestBody query) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null || query == null) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/_find";
        Headers headers = getBearerHeaders(token);
        fmar = runCall(POST, url, headers, query);
        if (!fmar.isSuccess()) {
            return fmar;
        }
        ResponseBody responseBody = fmar.getHttpResponseBody();
        if (responseBody == null) {
            return fmar;
        }
        try {
            JSONObject responseBodyObject = new JSONObject(responseBody.string());
            JSONObject fmResponse = responseBodyObject.getJSONObject(RESPONSE);
            JSONArray fmData = fmResponse.getJSONArray(DATA);
            fmar.setFmResponse(fmResponse);
            fmar.setFmData(fmData);
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
     * @param token  the FileMaker Data API Authorization token
     * @param layout the FileMaker layout
     * @param body   a stringify JSON objects containing field-value pairs
     * @return an FMApiResponse object
     * For more information see yourhost/fmi/data/apidoc/#api-Record-createRecord
     */
    public static FMApiResponse createRecord(String token, String layout, RequestBody body) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null || body == null) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records?";
        Headers headers = getBearerHeaders(token);
        fmar = runCall(POST, url, headers, body);
        if (!fmar.isSuccess()) {
            return fmar;
        }
        ResponseBody responseBody = fmar.getHttpResponseBody();
        if (responseBody == null) {
            return fmar;
        }
        try {
            JSONObject responseBodyObject = new JSONObject(responseBody.string());
            JSONObject fmResponse = responseBodyObject.getJSONObject(RESPONSE);
            String recordId = fmResponse.getString("recordId");
            fmar.setFmResponse(fmResponse);
            fmar.setFmRecordId(recordId);
            fmar.setSuccess(true);
            return fmar;
        } catch (IOException | JSONException e) {
            return fmar;
        }
    }

    /**
     * editRecord
     * PATCH METHOD
     *
     * @param token    the FileMaker Data API Authorization token
     * @param layout   the FileMaker layout
     * @param recordId the record's id to edit
     * @param body     a stringify JSON objects containing field-value pairs
     * @return an FMApiResponse object
     * For more information see yourhost/fmi/data/apidoc/#api-Record-editRecord
     */
    public static FMApiResponse editRecord(String token, String layout, String recordId, RequestBody body) {
        FMApiResponse fmar = new FMApiResponse();
        if (token == null || layout == null || recordId.isEmpty()) {
            return fmar;
        }
        String url = ENDPOINT + "/layouts/" + layout + "/records/" + recordId;
        Headers headers = getBearerHeaders(token);
        return runCall(PATCH, url, headers, body);
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
        Headers headers = getBearerHeaders(token);
        return runCall(DELETE, url, headers, null);
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
        Headers headers = getBearerHeaders(token);
        return runCall(DELETE, url, headers, null);
    }

    /**
     * runCall
     * Wrapper method to handle all HTTP connections and return an FMApiResponse object
     *
     * @param method  the HTTP method (GET, POST, PATCH, or DELETE)
     * @param url     the API url
     * @param headers the OKHttp Header object
     * @param body    the OKHttp RequestBody object
     * @return an FMApiResponse object
     */
    private static FMApiResponse runCall(String method, String url, Headers headers, RequestBody body) {
        FMApiResponse fmar = new FMApiResponse();
        OkHttpClient client = UnsecuredOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .method(method, body)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            fmar.setCustomMessage(e.toString());
            return fmar;
        }
        int code = response.code();
        if (code != 200) {
            fmar.setHttpResponseCode(code);
            fmar.setHttpHeaders(response.headers());
            return fmar;
        }
        fmar.setHttpHeaders(response.headers());
        fmar.setHttpResponseBody(response.body());
        fmar.setHttpResponseCode(code);
        fmar.setSuccess(true);
        return fmar;
    }

    /**
     * getBasicHeaders
     * Helper method to get the FileMaker Data API Basic Authorization Headers
     *
     * @param accountName the FileMaker account name with FMRest privileges
     * @param password    the FileMaker account's password
     * @return an OkHTTP Header object with Base64 encoded account name and password
     */
    private static Headers getBasicHeaders(String accountName, String password) {
        String encodedCredentials = Utilities.encodeFileMakerCredentials(accountName, password);
        if (encodedCredentials == null) {
            return null;
        }
        return new Headers.Builder()
                .add(CONTENT_TYPE, APPLICATION_JSON)
                .add(AUTHORIZATION, BASIC + encodedCredentials)
                .build();
    }

    /**
     * getBearerHeaders
     * Helper method to get the FileMaker Data API Bearer Authorization Headers
     *
     * @param token the FileMaker Data API token
     * @return an OkHTTP Headers object with the Data API token
     */
    private static Headers getBearerHeaders(String token) {
        return new Headers.Builder()
                .add(CONTENT_TYPE, APPLICATION_JSON)
                .add(AUTHORIZATION, BEARER + token)
                .build();
    }

    /**
     * getBody
     * Helper method to build an OkHTTP RequestBody object from the provided content string
     *
     * @param content the body's content
     * @return an OKHttp RequestBody object
     */
    private static RequestBody getBody(@Nullable String content) {
        final MediaType postDataMediaType = MediaType.parse("");
        if (content == null) {
            return RequestBody.create(postDataMediaType, "{}");
        } else {
            return RequestBody.create(postDataMediaType, content);
        }
    }
}
