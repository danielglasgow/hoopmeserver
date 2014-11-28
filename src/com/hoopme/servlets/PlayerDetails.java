package com.hoopme.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoopme.database.DatabaseInterface;
import com.hoopme.objects.Player;

/**
 * Servlet implementation class PlayerDetails
 */
public class PlayerDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlayerDetails() {
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
        String playerId = request.getParameter("id");
        if (playerId != null) {
            DatabaseInterface databaseInterface = DatabaseInterface.getInstance();
            if (databaseInterface.isConnected()) {
                Player player = databaseInterface.getPlayer(Integer.parseInt(playerId));
                if (player != null) {
                    out.println(player.toJSON());
                } else {
                    response.sendError(400, "Invlid Player Id");
                }
            } else {
                response.sendError(500, "Database Connection Failed");
            }
        } else {
            response.sendError(400, "Player Id Not Specified");
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
