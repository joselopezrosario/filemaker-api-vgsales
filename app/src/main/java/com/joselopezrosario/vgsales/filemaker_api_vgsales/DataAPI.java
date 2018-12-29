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
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                token = response.header("X-FM-Data-Access-Token");
                return token;
            } else {
                System.out.print("login failed: " + response.code());
                return null;
            }
        } catch (IOException e) {
            System.out.print("login IOException: " + e.toString());
            return null;
        }
    }

    /**
     * showAllRecords
     *
     * @param token  the FileMaker Data API Authorization token
     * @param layout the FileMaker layout
     * @param params additional parameters for offset, limit, sort, and portals
     * @return the ResponseBody of the API call or null
     */
    static JSONArray showAllRecords(String token, String layout, String params) {
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
        try (Response APIResponse = client.newCall(request).execute()) {
            if (APIResponse.code() == 200) {
                ResponseBody APIResponseBody = APIResponse.body();
                if (APIResponseBody != null) {
                    try {
                        JSONObject result = new JSONObject(APIResponseBody.string());
                        JSONObject response = result.getJSONObject("response");
                        JSONArray data = response.getJSONArray("data");
                        return data;
                    } catch (JSONException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            System.out.print("showAllRecords IOException: " + e.toString());
            return null;
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
