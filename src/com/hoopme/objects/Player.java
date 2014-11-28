package com.hoopme.objects;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.hoopme.logic.BasketballPosition;
import com.hoopme.logic.ToJSON;

public class Player implements ToJSON {

    public final int id;
    public final String name;
    public String password;
    public DateTime birthday;
    public int skill;
    public BasketballPosition position;
    public String username;

    public Player(int id, String username, String name, String password, DateTime birthday,
            int skill, BasketballPosition position) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.birthday = birthday;
        this.skill = skill;
        this.position = position;
        this.username = username;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("name", name);
            json.put("password", password);
            json.put("birthday", birthday);
            json.put("skill", skill);
            json.put("position", position.abbreviation);
            json.put("username", username);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Player fromJSON(JSONObject json) {
        try {
            return new Player(json.getInt("id"), json.getString("username"),
                    json.getString("name"), json.getString("password"), new DateTime(
                            json.getString("birthday")), json.getInt("skill"),
                    BasketballPosition.getPosition(json.getString("position")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
