package com.hoopme.logic;

import java.util.TreeSet;

import com.hoopme.database.DatabaseInterface;

public class IdGenerator {

    private static DatabaseInterface databaseConnection = DatabaseInterface.getInstance();
    private static int maxId;
    private static TreeSet<Integer> freeIds;

    static {
        TreeSet<Integer> ids = databaseConnection.getPlayerIds();
        maxId = 0;
        freeIds = new TreeSet<Integer>();
        if (!ids.isEmpty()) {
            maxId = ids.last();
            for (int i = 1; i <= maxId; i++) {
                freeIds.add(i);
            }
            for (Integer id : ids) {
                freeIds.remove(id);
            }
        }
    }

    public static synchronized int getNewId() {
        if (freeIds.isEmpty()) {
            maxId++;
            return maxId;
        }
        int id = freeIds.first();
        freeIds.remove((id));
        return id;
    }

}
