package com.fantafeat.util;


import org.json.JSONObject;

public interface GetApiResult {
    void onSuccessResult(JSONObject responseBody, int code);
    void onFailureResult(String responseBody, int code);
}