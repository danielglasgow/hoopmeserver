package com.hoopme.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.hoopme.logic.BasketballPosition;
import com.hoopme.logic.LatLng;
import com.hoopme.logic.UserValidity;
import com.hoopme.objects.Court;
import com.hoopme.objects.Player;
import com.hoopme.objects.PlayerView;

public class DatabaseInterface {

    private static DatabaseInterface INSTANCE = new DatabaseInterface();

    private final Connection connection;

    private DatabaseInterface() {
        this.connection = getConnection();
    }

    public static DatabaseInterface getInstance() {
        return INSTANCE;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public List<Court> getNearbyCourts(double lat, double lng) {
        return getNearbyCourts(lat, lng, 1);
    }

    private List<Court> getNearbyCourts(double latitude, double longitude, double radius) {
        String query = "Select * from Court where " + "power(abs(lat - " + latitude
                + "), 2) + power(abs(lng  - " + longitude + "), 2) < " + (radius * radius);
        return getCourtsFromResultSet(executeQuery(query));
    }

    public List<Court> getCourts() {
        String query = "SELECT * FROM Court";
        return getCourtsFromResultSet(executeQuery(query));
    }

    public TreeSet<Integer> getPlayerIds() {
        String query = "SELECT playerId FROM Profile";
        return getPlayerIds(executeQuery(query));

    }

    public void createPlayer(Player player) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        String insert = "INSERT INTO Profile VALUES (" + player.id + ", '" + player.username
                + "', '" + player.name + "', '" + player.password + "', '"
                + formatter.print(player.birthday) + "', " + player.skill + ", '"
                + player.position.abbreviation + "')";
        System.out.println(insert);
        updateQuery(insert);
    }

    public String getCourtName(int id) {
        String query = "SELECT name FROM Court WHERE id = " + id;
        ResultSet resultSet = executeQuery(query);
        try {
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPlayerToCourt(int courtId, int playerId, double duration) {
        removePlayerFromCourt(playerId);
        int hours = (int) duration;
        int minutes = (int) ((duration - hours) * 60);
        DateTime now = new DateTime();
        DateTime checkinFinish = now.plusMinutes(minutes).plusHours(hours);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh.mm.ss");
        String insert = "INSERT INTO AtCourt VALUES (" + playerId + ", " + courtId + ",'"
                + formatter.print(now) + "' ,'" + formatter.print(checkinFinish) + "')";
        updateQuery(insert);
    }

    public void removePlayerFromCourt(int playerId) {
        String delete = "DELETE FROM AtCourt WHERE playerId = " + playerId
                + " AND endCheckIn < now()";
        updateQuery(delete);
    }

    public void cleanAtCourt() {
        List<Integer> stalePlayerIds = getStalePlayerIds(AtCourtCleaner.SLEEP_TIME_MINUTES);
        for (Integer id : stalePlayerIds) {
            removePlayerFromCourt(id);
        }
    }

    public List<PlayerView> getPlayersAtCourt(int courtId) {
        String query = "SELECT IDS.playerId, username, name FROM Profile inner join (SELECT playerId FROM AtCourt WHERE courtId = "
                + courtId + ") AS IDS ON IDS.playerId = Profile.playerId";
        ResultSet resultSet = executeQuery(query);
        List<PlayerView> players = new ArrayList<PlayerView>();
        try {
            while (resultSet != null && resultSet.next()) {
                players.add(new PlayerView(Integer.parseInt(resultSet.getString(1)), resultSet
                        .getString(2), resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    public Player getPlayer(int playerId) {
        String query = "Select * FROM Profile WHERE playerId = " + playerId;
        ResultSet resultSet = executeQuery(query);
        try {
            while (resultSet != null && resultSet.next()) {
                return new Player(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4), new DateTime(
                                resultSet.getString(5)), resultSet.getInt(6),
                        BasketballPosition.getPosition(resultSet.getString(7)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserValidity validateUser(String username, String password) {
        String query1 = "Select count(*) FROM Profile WHERE username = '" + username + "'";
        ResultSet resultSet1 = executeQuery(query1);
        try {
            if (!resultSet1.next() || resultSet1.getInt(1) != 1) {
                return UserValidity.INVALID_USERNAME;
            } else {
                String query2 = "Select count(*) FROM Profile WHERE username = '" + username
                        + "' AND " + " password = '" + password + "'";
                ResultSet resultSet2 = executeQuery(query2);
                if (!resultSet2.next() || resultSet2.getInt(1) != 1) {
                    return UserValidity.INVALID_PASSWORD;
                }
                return UserValidity.VALID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return UserValidity.INVALID_USERNAME;
    }

    private List<Integer> getStalePlayerIds(int sleepTimeMinutes) {
        List<Integer> stalePlayerIds = new ArrayList<Integer>();
        DateTime staleTime = new DateTime().minusMinutes(sleepTimeMinutes);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh.mm.ss");
        String query = "SELECT playerId FROM AtCourt WHERE lastUpdate < " + "'"
                + formatter.print(staleTime) + "'";
        ResultSet resultSet = executeQuery(query);
        try {
            while (resultSet != null && resultSet.next()) {
                stalePlayerIds.add(Integer.parseInt(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stalePlayerIds;
    }

    private boolean updateQuery(String update) {
        try {
            return connection.prepareStatement(update).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private List<Court> getCourtsFromResultSet(ResultSet resultSet) {
        List<Court> courts = new ArrayList<Court>();
        if (resultSet == null) {
            return courts;
        }
        try {
            while (resultSet.next()) {
                String name = resultSet.getString(4);
                int id = Integer.parseInt(resultSet.getString(1));
                LatLng latlng = new LatLng(Double.parseDouble(resultSet.getString(2)),
                        Double.parseDouble(resultSet.getString(3)));
                courts.add(new Court(name, id, latlng));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courts;
    }

    private TreeSet<Integer> getPlayerIds(ResultSet resultSet) {
        TreeSet<Integer> ids = new TreeSet<Integer>();
        if (resultSet == null) {
            return ids;
        }
        try {
            while (resultSet.next()) {
                int id = Integer.parseInt(resultSet.getString(1));
                ids.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    private Connection getConnection() {
        Connection database = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("driver failed " + e);
                e.printStackTrace();
            }
            database = DriverManager.getConnection("jdbc:mysql://localhost/HoopMe", "root", "");
        } catch (SQLException e) {
            System.out.println("connection failed" + e);
            e.printStackTrace();
        }
        return database;
    }

}
