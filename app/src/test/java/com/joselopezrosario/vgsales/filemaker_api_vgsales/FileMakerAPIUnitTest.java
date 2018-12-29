package com.joselopezrosario.vgsales.filemaker_api_vgsales;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import okhttp3.ResponseBody;

@RunWith(RobolectricTestRunner.class)
public class FileMakerAPIUnitTest extends Robolectric {
    private static String token = null;

    /**
     * setToken
     * Call the DataAPI's login method to set the FileMaker Data API Authorization token
     */
    @BeforeClass
    public static void setToken() {
        token = DataAPI.login("Jose", "ErS9WeQKa3BVJk5t");
    }

    /**
     * validateToken
     * Test to check if we successfully received a token from the FileMaker Data API
     */
    @Test
    public void validateToken() {
        assert token != null;
    }

    /**
     * getAllRecords
     * Test to check if we successfully received all the records from the vgsales layout
     */
    @Test
    public void getAllRecords() {
        ResponseBody responseBody = DataAPI.showAllRecords(token, "vgsales", "");
        assert responseBody != null;
    }

    /**
     * logOut
     * After running all the tests, log out from the FileMaker Data API
     */
    @AfterClass
    public static void logOut() {
        boolean logout = DataAPI.logOut(token);
        assert logout;
    }
}