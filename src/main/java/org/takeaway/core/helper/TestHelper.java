package org.takeaway.core.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.json.JSONObject;


public class TestHelper {

    private static JsonObject getJsonFromResponse(Response res) {
        var gson = new Gson();
        return gson.fromJson(new JSONObject(res.getBody().asString()).toString(), JsonObject.class);
    }

    public static <T> T deserializeJson(Response res, Class<T> requiredClass) {
        var json = getJsonFromResponse(res);
        var gson = new Gson();
        return gson.fromJson(json, requiredClass);
    }

    public static String serializeToJson(Object requiredClass) {
        var gson = new Gson();
        return gson.toJson(requiredClass);
    }
}
