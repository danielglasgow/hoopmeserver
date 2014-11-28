package com.hoopme.objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoopme.logic.LatLng;
import com.hoopme.logic.ToJSON;

public class Court implements ToJSON {

    public final String name;
    public final int id;
    public final LatLng latlng;

    public Court(String name, int id, LatLng latlng) {
        this.name = name;
        this.id = id;
        this.latlng = latlng;
    }

    @Override
    public JSONObject toJSON() {
        try {
            return new JSONObject().put("id", id).put("name", name).put("lat", latlng.lat)
                    .put("lng", latlng.lng);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
