package com.hoopme.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoopme.database.DatabaseInterface;
import com.hoopme.logic.CourtMatcher;
import com.hoopme.logic.JSONUtility;
import com.hoopme.logic.LatLng;
import com.hoopme.objects.Court;

/**
 * Servlet implementation class LocationUpdater
 */
public class LocationUpdater extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final int CHECK_IN = 201;
    private static final int AT_COURT = 202;
    private static final int NO_MATCH = 203;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationUpdater() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(400, "POST Only");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = JSONUtility.toJSON(request.getReader());
        try {
            LatLng latlng = new LatLng(json.getDouble("lat"), json.getDouble("lng"));
            int playerId = json.getInt("playerId");
            int courtId = json.getInt("courtId");
            double duration = json.getDouble("duration");
            DatabaseInterface databaseInterface = DatabaseInterface.getInstance();
            if (databaseInterface.isConnected()) {
                if (courtId == -1) {
                    for (Court court : databaseInterface.getNearbyCourts(latlng.lat, latlng.lng)) {
                        if (CourtMatcher.isMatch(court, latlng)) {
                            courtId = court.id;
                            response.setStatus(AT_COURT); // this could
                                                          // potentially give
                                                          // location
                                                          // information...
                            break;
                        }
                    }
                }
                if (courtId == -1) {
                    response.setStatus(NO_MATCH);
                    databaseInterface.removePlayerFromCourt(playerId);
                } else {
                    databaseInterface.addPlayerToCourt(courtId, playerId, duration);
                }
                if (latlng.lat > 90) {
                    response.setStatus(CHECK_IN); // check based location update
                }
            } else {
                response.sendError(500, "Database Connection Failed");
            }
        } catch (JSONException e) {
            response.sendError(400, "missing key value pairs in JSON");
        }
    }
}
