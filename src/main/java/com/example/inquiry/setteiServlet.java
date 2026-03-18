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
 * Servlet implementation class setteiServlet
 */
public class setteiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public setteiServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.getWriter().append("Served at: ").append(req.getContextPath());
		 HttpSession session = req.getSession(); 
		String url = System.getenv("DB_URL");
		String user = "a";
		String password = "78459_ki";
		String sql = "SELECT * FROM hito WHERE namae = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
	             PreparedStatement statement = connection.prepareStatement(sql)) {
	        		String aaaa = (String) session.getAttribute("namae2");
	        		statement.setString(1, aaaa);
	        		ResultSet results = statement.executeQuery();
	        		results.next();
	        		String namae = results.getString("namae");
	        		req.setAttribute("namae", namae);
	        		String name = results.getString("name");
	        		req.setAttribute("name", name);
	        		String mail = results.getString("mail");
	        		req.setAttribute("mail", mail);
	        		String ope =results.getString("ope");
	        		req.setAttribute("ope", ope);
	            	RequestDispatcher rd = req.getRequestDispatcher("/jsp/settei.jsp");
	            rd.forward(req, resp);
	            session.removeAttribute("tigau");
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
		HttpSession session = req.getSession(); 
        String url = System.getenv("DB_URL");
		String user = "a";
		String password = "78459_ki";
		String sql = "";
		try (Connection connection = DriverManager.getConnection(url, user, password))
			{
					String ae = req.getParameter("namae");
					String aa = req.getParameter("name");
					String ai = req.getParameter("mail");
					String au = req.getParameter("ope");
					String as = req.getParameter("pass0");
					String at = req.getParameter("pass");
		            /*RequestDispatcher rd = req.getRequestDispatcher("/jsp/touroku.jsp");
		            rd.forward(req, resp);*/
		            /*doGet(req, resp);*/
					if(at!=null) {
		            if (!at.trim().isEmpty()) {
		            	sql = "SELECT pass FROM hito WHERE namae = ?";
		            	PreparedStatement aStatement = connection.prepareStatement(sql);{
		            		aStatement.setString(1, ae);
		            		ResultSet results = aStatement.executeQuery();
		            		results.next();
		            		String ak = results.getString("pass");
		            		if (ak != null) {
		            		if (!as.equals(ak)) {
		            			session.setAttribute("tigau", "パスワード違う");
		            			resp.sendRedirect(req.getContextPath() + "/settei");
		            			return;
		            		}
		            		}
		            	}
		            }else {
		            	at = null;
		            }
		            }else {
		            	at = null;
		            }
					if (au !=null) {
		            if (au.trim().isEmpty()) {
		            	au = null;
		            }
					}
					if(at != null) {
		            sql = "UPDATE hito SET name = ?, mail = ?, ope = ?, pass = ? WHERE namae = ?";
					}else {
						sql = "UPDATE hito SET name = ?, mail = ?, ope = ? WHERE namae = ?";
					}
		            PreparedStatement statement = connection.prepareStatement(sql);{
		            statement.setString(1, aa);
					statement.setString(2, ai);
					statement.setString(3, au);
					if(at != null) {
					statement.setString(4, at);
					statement.setString(5, ae);
					}else {
						statement.setString(4, ae);
					}
		            statement.executeUpdate();
		            }
		            resp.sendRedirect(req.getContextPath() + "/settei");
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
