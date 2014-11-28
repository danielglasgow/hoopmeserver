package com.hoopme.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoopme.database.DatabaseInterface;
import com.hoopme.logic.JSONUtility;
import com.hoopme.objects.PlayerView;

/**
 * Servlet implementation class CourtDetails
 */
public class CourtDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourtDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String courtId = request.getParameter("id");
        if (courtId != null) {
            DatabaseInterface databaseInterface = DatabaseInterface.getInstance();
            if (databaseInterface.isConnected()) {
                List<PlayerView> players = databaseInterface.getPlayersAtCourt(Integer
                        .parseInt(courtId));
                String courtName = databaseInterface.getCourtName(Integer.parseInt(courtId));
                try {
                    out.println(new JSONObject().put("players", JSONUtility.toJSONArray(players))
                            .put("name", courtName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                response.sendError(500, "Database Connection Failed");
            }
        } else {
            response.sendError(400, "Court Id Not Specified");
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
