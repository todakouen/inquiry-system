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
 * Servlet implementation class nyusitu
 */
public class nyusitu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public nyusitu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.getWriter().append("Served at: ").append(req.getContextPath());
		RequestDispatcher rd = req.getRequestDispatcher("/jsp/nyusitu.jsp");
        rd.forward(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

        String namae = req.getParameter("namae");
        HttpSession session = req.getSession(); 
        String url = System.getenv("DB_URL");
		String user = "a";
		String password = "78459_ki";
		String sql = "SELECT * FROM hito WHERE namae = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement statement = connection.prepareStatement(sql)){
					statement.setString(1, namae);
					ResultSet results = statement.executeQuery();
		            /*RequestDispatcher rd = req.getRequestDispatcher("/jsp/touroku.jsp");
		            rd.forward(req, resp);*/
		            /*doGet(req, resp);*/
					results.next();
					String pa = results.getString("pass");
					if (pa == null) {
		            session.setAttribute("pal", "join");
		            session.setAttribute("namae2", namae);
		            resp.sendRedirect(req.getContextPath() + "/inquiry");
					}else {
						req.setAttribute("namae", namae);
						RequestDispatcher rd = req.getRequestDispatcher("/jsp/password.jsp");
			            rd.forward(req, resp);
					}
		} catch (SQLException e) {
			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/error2.jsp");
            rd.forward(req, resp);
		}catch (Exception e) {
			req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
            rd.forward(req, resp);
        }
	}

}
