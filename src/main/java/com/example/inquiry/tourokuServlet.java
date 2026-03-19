package com.example.inquiry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class tourokuServlet
 */
public class tourokuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public tourokuServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.getWriter().append("Served at: ").append(req.getContextPath());
        String url = System.getenv("DB_URL");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");
		String sql = "SELECT namae FROM hito";
        try {
        	Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
        	req.setAttribute("errorMessage", "ドライバーロードエラー: " + e.getMessage());
        	RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
        	rd.forward(req, resp);
        	return;
        }
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        		ResultSet results = statement.executeQuery()) {
        	List<String> waoon = Collections.synchronizedList(new ArrayList<>());
        	while (results.next()) {
				String namae = results.getString("namae");
				waoon.add(namae);
			}
        	req.setAttribute("waoon", waoon); 
            	RequestDispatcher rd = req.getRequestDispatcher("/jsp/touroku.jsp");
            rd.forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
            rd.forward(req, resp);
        }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*doGet(req, resp);*/
        String namae = req.getParameter("namae");
        HttpSession session = req.getSession(); 
        String url = System.getenv("DB_URL");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");
		String sql = "INSERT INTO hito (namae) VALUES (?)";
		
		try {
        	Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
        	req.setAttribute("errorMessage", "ドライバーロードエラー: " + e.getMessage());
        	RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
        	rd.forward(req, resp);
        	return;
        }
		
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement statement = connection.prepareStatement(sql)){
					statement.setString(1, namae);
		            statement.executeUpdate();
		            /*RequestDispatcher rd = req.getRequestDispatcher("/jsp/touroku.jsp");
		            rd.forward(req, resp);*/
		            /*doGet(req, resp);*/
		            session.setAttribute("pal", "join");
		            session.setAttribute("namae2", namae);
		            resp.sendRedirect(req.getContextPath() + "/inquiry");
		} catch (SQLException e) {
			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
            rd.forward(req, resp);
		}catch (Exception e) {
			req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
            rd.forward(req, resp);
        }
	}

}
