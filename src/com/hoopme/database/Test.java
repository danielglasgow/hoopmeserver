package com.hoopme.database;

public class Test {

    public static void main(String[] args) {
        System.out.println("start test");
        DatabaseInterface db = DatabaseInterface.getInstance();
        // db.addPlayerToCourt(1, 2, 2);
        // db.addPlayerToCourt(1, 1, 0);
        db.removePlayerFromCourt(1);
        db.removePlayerFromCourt(2);
        System.out.println("end test");
    }

}
