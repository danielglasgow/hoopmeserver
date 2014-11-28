package com.hoopme.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtility {

    public static JSONArray toJSONArray(List<? extends ToJSON> list) {
        List<JSONObject> toJSONList = new ArrayList<JSONObject>();
        for (ToJSON object : list) {
            toJSONList.add(object.toJSON());
        }
        return new JSONArray(toJSONList);
    }

    public static JSONObject toJSON(BufferedReader bufferedReader) {
        int charRead = 0;
        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((charRead = bufferedReader.read(buffer)) > 0) {
                stringBuilder.append(buffer, 0, charRead);
            }
            bufferedReader.close();
            return new JSONObject(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
