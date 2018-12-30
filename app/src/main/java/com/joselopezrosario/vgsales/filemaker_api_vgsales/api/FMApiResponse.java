package com.joselopezrosario.vgsales.filemaker_api_vgsales.api;

import org.json.JSONArray;

public class FMApiResponse {
        private int responseCode;
        private boolean success;
        private String token;
        private JSONArray data;
        private String message;
        private String recordId;


    FMApiResponse() {
        this.success = false;
    }

    public int getResponseCode() {
        return responseCode;
    }

    void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isSuccess() {
        return success;
    }

    void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    void setToken(String token) {
        this.token = token;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
