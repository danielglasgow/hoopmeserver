package com.hoopme.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoopme.database.DatabaseInterface;
import com.hoopme.logic.JSONUtility;
import com.hoopme.objects.Court;

/**
 * Servlet implementation class HelloWorld
 */
public class CourtFinder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourtFinder() {
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
        DatabaseInterface databaseConnection = DatabaseInterface.getInstance();
        if (!databaseConnection.isConnected()) {
            response.sendError(500, "Database Connection Failed");
        }
        List<Court> courts = new ArrayList<Court>();
        if (request.getParameter("lat") != null && request.getParameter("lng") != null) {
            double lat = Double.parseDouble(request.getParameter("lat"));
            double lng = Double.parseDouble(request.getParameter("lng"));
            courts.addAll(databaseConnection.getNearbyCourts(lat, lng));
        } else {
            // Returns all courts
            courts.addAll(databaseConnection.getCourts());
        }
        try {
            out.println(new JSONObject().put("courts", JSONUtility.toJSONArray(courts)));
        } catch (JSONException e) {
            e.printStackTrace();
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
