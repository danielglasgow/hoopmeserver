package com.hoopme.objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoopme.logic.ToJSON;

public class PlayerView implements ToJSON {

    private final int id;
    private final String username;
    private final String name;

    public PlayerView(int id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject().put("id", id).put("username", username).put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}