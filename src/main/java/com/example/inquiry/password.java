package com.example.inquiry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class password
 */
public class password extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public password() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession(); 
		String url = System.getenv("DB_URL");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");
		String sql = "SELECT * FROM hito WHERE namae = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
	             PreparedStatement statement = connection.prepareStatement(sql)) {
			String aaaa = req.getParameter("namae");
			statement.setString(1, aaaa);
			ResultSet results = statement.executeQuery();
	        		String passw = req.getParameter("password");
	        		results.next();
	        		String pass = results.getString("pass");
	        		if (pass.equals(passw)) {
	        			session.setAttribute("pal", "join");
			            session.setAttribute("namae2", aaaa);
			            resp.sendRedirect(req.getContextPath() + "/inquiry");
	        		}else {
	        			req.setAttribute("namae", aaaa);
	        			req.setAttribute("a", "password違う");
	        			RequestDispatcher rd = req.getRequestDispatcher("/jsp/password.jsp");
	    	            rd.forward(req, resp);
	            	/*RequestDispatcher rd = req.getRequestDispatcher("/jsp/settei.jsp");
	            rd.forward(req, resp);*/
	        		}
	        } catch (SQLException e) {
	            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
	            RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
	            rd.forward(req, resp);
	        }
	}

}
