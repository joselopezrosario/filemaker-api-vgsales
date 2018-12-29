package com.joselopezrosario.vgsales.filemaker_api_vgsales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.ThreadLocalRandom;

@RunWith(RobolectricTestRunner.class)
public class FileMakerAPIUnitTest extends Robolectric {
    private static String token = null;
    private static JSONArray allRecords = null;

    /**
     * setToken
     * Call the DataAPI's login method to set the FileMaker Data API Authorization token
     */
    @BeforeClass
    public static void setup() {
        token = DataAPI.login("Jose", "ErS9WeQKa3BVJk5t");
        allRecords = DataAPI.showAllRecords(token, "vgsales", "_limit=10000");
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
     * validateAllRecords
     * Test to check if we successfully received all the records from the vgsales layout
     */
    @Test
    public void validateFoundSet() {
        assert allRecords != null;
    }

    /**
     * validateRandomRecord
     * Test to get the first record from the foundset, parse out a random record,
     * and validate that all numeric values are zero or higher, and that all text values are not empty
     */
    @Test
    public void validateRandomRecord() {
        JSONObject record;
        try {
            int max = allRecords.length();
            int randomNum = ThreadLocalRandom.current().nextInt(0, max );
            record = allRecords.getJSONObject(randomNum).getJSONObject("fieldData");
            int rank = record.getInt(DataAPI.FIELD_RANK);
            String name = record.getString(DataAPI.FIELD_NAME);
            String platform = record.getString(DataAPI.FIELD_PLATFORM);
            String year = record.getString(DataAPI.FIELD_YEAR);
            String genre = record.getString(DataAPI.FIELD_GENRE);
            String publisher = record.getString(DataAPI.FIELD_PUBLISHER);
            Double na_sales = record.getDouble(DataAPI.FIELD_NA_SALES);
            Double eu_sales = record.getDouble(DataAPI.FIELD_EU_SALES);
            Double jp_sales = record.getDouble(DataAPI.FIELD_JP_SALES);
            Double other_sales = record.getDouble(DataAPI.FIELD_OTHER_SALES);
            Double global_sales = record.getDouble(DataAPI.FIELD_GLOBAL_SALES);
            int score = 0;
            if (rank > 0) {
                score++;
            }
            if (!name.isEmpty()) {
                score++;
            }
            if (!platform.isEmpty()) {
                score++;
            }
            if (!year.isEmpty()) {
                score++;
            }
            if (!genre.isEmpty()) {
                score++;
            }
            if (!publisher.isEmpty()) {
                score++;
            }
            if (na_sales >= 0) {
                score++;
            }
            if (eu_sales >= 0) {
                score++;
            }
            if (jp_sales >= 0) {
                score++;
            }
            if (other_sales >= 0) {
                score++;
            }
            if (global_sales >= 0) {
                score++;
            }
            System.out.print("Rank: " + rank+"\n");
            System.out.print("Name: " + name+"\n");
            assert score == 11;
        } catch (JSONException e) {
            assert false;
        }
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