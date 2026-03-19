package com.example.inquiry;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class csv
 */
public class csv extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public csv() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        String aString = req.getParameter("aoaoa");
        resp.setContentType("text/csv");
        if (aString.equals("a")) {
        resp.setHeader("Content-Disposition", "attachment; filename=\"qqa_export.csv\"");
        }else if (aString.equals("i")) {
        	resp.setHeader("Content-Disposition", "attachment; filename=\"hito_export.csv\"");
        }
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        String oString = (String) session.getAttribute("op");
        if (oString.equals("ope7")) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            CopyManager copyManager = new CopyManager((BaseConnection) connection);
            String sql = "a";
            if (aString.equals("a")) {
            sql = "COPY qqa TO STDOUT WITH (FORMAT CSV, HEADER TRUE, DELIMITER ',')";
            }else if (aString.equals("i")) {
            	sql = "COPY hito TO STDOUT WITH (FORMAT CSV, HEADER TRUE, DELIMITER ',')";
            }
            copyManager.copyOut(sql, out);
            out.flush();

        } catch (SQLException e) {
            req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
            req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
            req.getRequestDispatcher("/jsp/error.jsp").forward(req, resp);
        } finally {
            if (out != null) out.close();
        }
        }else {
        	resp.sendRedirect("kanri");
        }
	}

}
