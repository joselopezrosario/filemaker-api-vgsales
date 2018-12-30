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

import okhttp3.MediaType;
import okhttp3.RequestBody;

@RunWith(RobolectricTestRunner.class)
public class FileMakerAPIUnitTest extends Robolectric {
    private static String token = null;
    private static JSONArray getRecords = null;
    private static final String emptyFieldData = "{\"fieldData\": {}}";
    private static final String fieldData =
            "{\"fieldData\": {" +
                    "\"" + DataAPI.FIELD_RANK + "\":0" + "," +
                    "\"" + DataAPI.FIELD_NAME + "\":\"Jose's Game\"" + "," +
                    "\"" + DataAPI.FIELD_PLATFORM + "\":\"Best Platform\"" + "," +
                    "\"" + DataAPI.FIELD_YEAR + "\":\"2018\"" + "," +
                    "\"" + DataAPI.FIELD_GENRE + "\":\"Arcade\"" + "," +
                    "\"" + DataAPI.FIELD_PUBLISHER + "\":\"Best Publisher\"" + "," +
                    "\"" + DataAPI.FIELD_NA_SALES + "\":10.0" + "," +
                    "\"" + DataAPI.FIELD_EU_SALES + "\":11.0" + "," +
                    "\"" + DataAPI.FIELD_JP_SALES + "\":12.0" + "," +
                    "\"" + DataAPI.FIELD_OTHER_SALES + "\":13.0" + "," +
                    "\"" + DataAPI.FIELD_GLOBAL_SALES + "\":46.0" +
                    "}}";
    /**
     * setUp
     * Set the FileMaker Data API token and get a foundset of 10,000 records
     */
    @BeforeClass
    public static void setup() {
        token = DataAPI.login(DataAPI.ACCOUNTNAME, DataAPI.PASSWORD);
        getRecords = DataAPI.getRecords(token, DataAPI.LAYOUT_VGSALES, "_limit=10000");
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
    public void validateGetRecords() {
        assert getRecords != null;
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
            int max = getRecords.length();
            int randomNum = ThreadLocalRandom.current().nextInt(0, max);
            record = getRecords.getJSONObject(randomNum).getJSONObject("fieldData");
            int id = record.getInt(DataAPI.FIELD_ID);
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
            if (id > 0) {
                score++;
            }
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
            System.out.print("ID: " + id + "\n");
            System.out.print("Name: " + name + "\n");
            assert score == 12;
        } catch (JSONException e) {
            assert false;
        }
    }

    /**
     * createAndDeleteRecord
     * Create a record and then delete it
     * The test passes if both actions are successful
     */
    @Test
    public void createAndDeleteRecord() {
        int newRecordId = DataAPI.createRecord(token, DataAPI.LAYOUT_VGSALES,
                RequestBody.create(MediaType.parse(""), fieldData),
                "");
        boolean delete = false;
        if (newRecordId > 0) {
            delete =
                    DataAPI.deleteRecord(token,
                            DataAPI.LAYOUT_VGSALES,
                            String.valueOf(newRecordId),
                            "");
        }
        assert newRecordId > 0 && delete;
    }

    /**
     * createEditAndDeleteRecord
     * Create a blank record, edit it, then delete it
     * The test passes if all the actions are successful
     */
    @Test
    public void createEditAndDeleteRecord() {
        RequestBody createRequestBody = RequestBody.create(MediaType.parse(""), emptyFieldData);
        int newRecordId = DataAPI.createRecord(token, DataAPI.LAYOUT_VGSALES, createRequestBody, "");
        boolean edit = false;
        RequestBody editRequestBody = RequestBody.create(MediaType.parse(""), fieldData);
        if (newRecordId > 0) {
            edit = DataAPI.editRecord(token, DataAPI.LAYOUT_VGSALES,
                    String.valueOf(newRecordId), editRequestBody, "");
        }
        boolean delete = false;
        if (newRecordId > 0) {
            delete = DataAPI.deleteRecord(token, DataAPI.LAYOUT_VGSALES, String.valueOf(newRecordId), "");
        }
        assert newRecordId > 0 && edit && delete;
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