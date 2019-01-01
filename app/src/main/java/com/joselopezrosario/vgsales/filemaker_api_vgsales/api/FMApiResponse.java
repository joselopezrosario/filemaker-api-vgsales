package com.joselopezrosario.vgsales.filemaker_api_vgsales.api;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Headers;
import okhttp3.ResponseBody;

@SuppressWarnings("unused")
public class FMApiResponse {
    private int httpResponseCode;
    private ResponseBody httpResponseBody;
    private Headers httpHeaders;
    private String fmToken;
    private JSONObject fmResponse;
    private JSONArray fmData;
    private String fmRecordId;
    private boolean success;
    private String customMessage;

    public FMApiResponse() {
        this.httpResponseCode = 0;
        this.httpResponseBody = null;
        this.httpHeaders = null;
        this.fmToken = null;
        this.fmResponse = null;
        this.fmData = null;
        this.fmRecordId = null;
        this.success = false;
        this.customMessage = null;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public ResponseBody getHttpResponseBody() {
        return httpResponseBody;
    }

    public void setHttpResponseBody(ResponseBody httpResponseBody) {
        this.httpResponseBody = httpResponseBody;
    }

    public Headers getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Headers httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public String getFmToken() {
        return fmToken;
    }

    public void setFmToken(String fmToken) {
        this.fmToken = fmToken;
    }

    public JSONObject getFmResponse() {
        return fmResponse;
    }

    public void setFmResponse(JSONObject fmResponse) {
        this.fmResponse = fmResponse;
    }

    public JSONArray getFmData() {
        return fmData;
    }

    public void setFmData(JSONArray fmData) {
        this.fmData = fmData;
    }

    public String getFmRecordId() {
        return fmRecordId;
    }

    public void setFmRecordId(String fmRecordId) {
        this.fmRecordId = fmRecordId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public void clear() {
        this.httpResponseCode = 0;
        this.httpResponseBody = null;
        this.httpHeaders = null;
        this.fmToken = null;
        this.fmResponse = null;
        this.fmData = null;
        this.fmRecordId = null;
        this.success = false;
        this.customMessage = null;
    }
}