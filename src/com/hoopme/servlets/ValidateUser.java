package com.hoopme.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoopme.database.DatabaseInterface;
import com.hoopme.logic.UserValidity;

/**
 * Servlet implementation class ValidateUser
 */
public class ValidateUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final int STATUS_VALID = 201;
    private static final int STATUS_INVALID_USERNAME = 202;
    private static final int STATUS_INVALID_PASSWORD = 203;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValidateUser() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null) {
            response.sendError(400, "Username Not Specified");
        } else if (password == null) {
            response.sendError(400, "Password Not Specified");
        } else {
            DatabaseInterface database = DatabaseInterface.getInstance();
            if (database.isConnected()) {
                UserValidity validity = database.validateUser(username, password);
                System.out.println(validity);
                switch (validity) {
                case VALID:
                    response.setStatus(STATUS_VALID);
                    break;
                case INVALID_USERNAME:
                    response.setStatus(STATUS_INVALID_USERNAME);
                    break;
                case INVALID_PASSWORD:
                    response.setStatus(STATUS_INVALID_PASSWORD);
                    break;
                }
            } else {
                response.sendError(500, "Error Connecting to Database");
            }

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
