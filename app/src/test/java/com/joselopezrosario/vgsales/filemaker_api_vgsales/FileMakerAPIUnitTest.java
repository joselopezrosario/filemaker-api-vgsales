package com.joselopezrosario.vgsales.filemaker_api_vgsales;

import android.util.Base64;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RunWith(RobolectricTestRunner.class)

/**
 * FileMakerAPIUnitTest
 * A series of tests against the FileMaker API Data API
 */

public class FileMakerAPIUnitTest {
    private static String ENDPOINT = "https://192.168.0.7/fmi/data/v1/databases/VideoGameSales";
    private static String token = null;
    private static ResponseBody jsonResponse = null;

    @BeforeClass
    public static void setToken() {
        final MediaType MEDIA_TYPE = MediaType.parse("");
        // The FileMaker user account and password with fmRest extended privilege set
        String credentials = "Jose:ErS9WeQKa3BVJk5t";
        // Encode the credentials in Base64
        String encodedCredentials = null;
        byte[] credentialBytes;
        try {
            credentialBytes = credentials.getBytes("UTF-8");
            encodedCredentials = Base64.encodeToString(credentialBytes, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            System.out.print("Could not encode credentials: " + e.toString());
        }
        // Create an OkHTTPClient and call the FileMaker Data API
        OkHttpClient client = DevOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(ENDPOINT + "/sessions")
                .post(RequestBody.create(MEDIA_TYPE, "{}"))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ("Basic " + encodedCredentials).trim())
                .build();
        try (Response response = client.newCall(request).execute()) {
            // If the response is 200, pass the test and set the token
            if (response.code() == 200) {
                token = response.header("X-FM-Data-Access-Token");
                System.out.print("Successful post to get token!\n");
                System.out.print("Token: " + token);

            } else {
                System.out.print("Unsuccessful post to get token, code " + response.code());
            }
        } catch (IOException e) {
            System.out.print(e.toString());
        }
    }

    /**
     * getToken
     * Test to check if we successfully received a token from the FileMaker Data API
     */
    @Test
    public void validateToken() {
        if (token != null) {
            assert true;
        } else {
            assert false;
        }
    }

    /**
     * getAllRecords
     * Test to check if we successfully received all the records from the vgsales layout
     */
    @Test
    public void getAllRecords() {
        String url = ENDPOINT + "/layouts/vgsales/records";
        OkHttpClient client = DevOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ("Bearer " + token))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                jsonResponse = response.body();
                if ( jsonResponse != null ){
                    System.out.print("Successful Get to get all records!");
                    assert true;
                }else{
                    System.out.print("The JSON response is null");
                    assert false;
                }
            } else {
                System.out.print("Unsuccessful get to get all records, code " + response.code());
                assert false;
            }
        } catch (IOException e) {
            System.out.print(e.toString());
            assert false;
        }
    }

    /**
     * logOut
     * After running all the tests, log out from the FileMaker Data API
     */
    @AfterClass
    public static void logOut(){
        String url = ENDPOINT + "/sessions/" + token;
        OkHttpClient client = DevOkHTTPClient.trustAllSslClient(new OkHttpClient());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", ("Bearer " + token))
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                System.out.print("Successfully logged out!");
                assert true;
            } else {
                System.out.print("Could not log out, code " + response.code());
                assert false;
            }
        } catch (IOException e) {
            System.out.print(e.toString());
            assert false;
        }
    }

}