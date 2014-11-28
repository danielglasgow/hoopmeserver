package com.hoopme.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoopme.database.DatabaseInterface;
import com.hoopme.logic.IdGenerator;
import com.hoopme.logic.JSONUtility;
import com.hoopme.objects.Player;

/**
 * Servlet implementation class PlayerCreator
 */
public class PlayerCreator extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlayerCreator() {
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
        int id = IdGenerator.getNewId();
        try {
            out.println(new JSONObject().put("id", id));
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
        JSONObject json = JSONUtility.toJSON(request.getReader());
        DatabaseInterface database = DatabaseInterface.getInstance();
        Player player = Player.fromJSON(json);
        database.createPlayer(player);
    }

}
