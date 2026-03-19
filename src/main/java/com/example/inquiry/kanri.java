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
 * Servlet implementation class kanri
 */
public class kanri extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public kanri() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*resp.getWriter().append("Served at: ").append(req.getContextPath());*/
            String url = System.getenv("DB_URL");
    		String user = System.getenv("DB_USER");
    		String password = System.getenv("DB_PASSWORD");
    		String sql = "SELECT name, male, content, file, status, id, aaa FROM qqa WHERE aaa = ? ORDER BY id ASC";   		
    		HttpSession session = req.getSession(); 
    		String aoa = (String) session.getAttribute("op");
    		if (aoa==null) {
    			RequestDispatcher rd = req.getRequestDispatcher("/jsp/error2.jsp");
                rd.forward(req, resp);
    		}else {
    		if (aoa.equals("ope2")) {
    			sql = "SELECT name, male, content, file, status, id, aaa FROM qqa WHERE aaa IN (?, ?) ORDER BY id ASC";
    		}else if (aoa.equals("ope7")) {
    			sql = "SELECT name, male, content, file, status, id, aaa FROM qqa ORDER BY id ASC";
    		}
    		try (Connection connection = DriverManager.getConnection(url, user, password);
    				PreparedStatement statement = connection.prepareStatement(sql)){
    			List<Inquiry> inquiries = Collections.synchronizedList(new ArrayList<>()); 
    			switch (aoa) {
				case "ope":
					statement.setString(1, "ヘルプデスク");
					break;
				case "ope2":
					statement.setString(1, "先生へ");
					statement.setString(2, "宿題");
					break;
				case "ope3":
					statement.setString(1, "店舗へ");;
					break;
				case "ope4":
					statement.setString(1, "自治体へ");;
					break;
				case "ope5":
					statement.setString(1, "バグ");;
					break;
				case "ope6":
					statement.setString(1, "資料請求");;
					break;
				case "ope7":
					break;
				default:
					break;
				}
    			ResultSet results = statement.executeQuery();
    			while (results.next()) {
    				String name = results.getString("name");
    				String male = results.getString("male");
    				String content = results.getString("content");
    				String file = results.getString("file");
    				String newStatus = results.getString("status");
    				Integer id = results.getInt("id");
    				String aaa = results.getString("aaa");
    				Inquiry inquiry = new Inquiry(); 
    	            inquiry.setName(name); 
    	            inquiry.setEmail(male); 
    	            inquiry.setContent(content); 
    	            inquiry.setAttachmentFileName(file);
    	            inquiry.setStatus(newStatus);
    	            inquiry.setQqaKey(id);
    	            inquiry.setAaa(aaa);
    	            inquiries.add(inquiry);
    			}
    			req.setAttribute("inquiries", new ArrayList<>(inquiries)); 
    			RequestDispatcher rd = req.getRequestDispatcher("/jsp/kanri.jsp");
	            rd.forward(req, resp);
    		} catch (SQLException e) {
    			req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                rd.forward(req, resp);
                /*out.println("Database exception: " + e.getMessage());*/
    		}catch (Exception e) {
    			req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                rd.forward(req, resp);
                /*out.println("Exception" + e.getMessage());*/
            }
    		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 int index = Integer.parseInt(req.getParameter("index")); 
         String newStatus = req.getParameter("newStatus"); 
         String returnAnchor = req.getParameter("returnAnchor");
        String url = System.getenv("DB_URL");
 		String user = System.getenv("DB_USER");
 		String password = System.getenv("DB_PASSWORD");
 		String sql = "UPDATE qqa SET status = ? WHERE id = ?";
 		try (Connection connection = DriverManager.getConnection(url, user, password);
 				PreparedStatement statement = connection.prepareStatement(sql)){
 				statement.setString(1,  newStatus);
 				statement.setInt(2, index);
 		            statement.executeUpdate();
 		           resp.sendRedirect("kanri#" + returnAnchor); 
 		} catch (SQLException e) {
 			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
             RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
             rd.forward(req, resp);
 		}catch (Exception e) {
 			req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
             RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
             rd.forward(req, resp);
         }
         /*inquiryDAO.updateInquiryStatus(index, newStatus)*/; 
         /*resp.sendRedirect("inquiry?action=history"); */
	}

}
