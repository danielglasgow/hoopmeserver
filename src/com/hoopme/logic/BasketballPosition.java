package com.hoopme.logic;

import java.util.HashMap;
import java.util.Map;

public enum BasketballPosition {
    CENTER("Center", "C"), SMALL_FORWARD("Small Forward", "SF"), POWER_FORWARD("Power Forward",
            "PF"), SHOOTING_GAURD("Shooting Gaurd", "SG"), POINT_GAURD("Point Gaurd", "PG");

    public final String name;
    public final String abbreviation;

    private BasketballPosition(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    private static final Map<String, BasketballPosition> POSITION_MAP = initializePositionMap();

    private static Map<String, BasketballPosition> initializePositionMap() {
        Map<String, BasketballPosition> positionMap = new HashMap<String, BasketballPosition>();
        for (BasketballPosition position : BasketballPosition.values()) {
            positionMap.put(position.abbreviation, position);
            positionMap.put(position.name, position);
        }
        return positionMap;
    }

    public static BasketballPosition getPosition(String position) {
        return POSITION_MAP.get(position);
    }
}
