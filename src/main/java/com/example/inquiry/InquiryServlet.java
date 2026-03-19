package com.example.inquiry; 
 
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part; 
  
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 50, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 
1024 * 1024 * 50) 
public class InquiryServlet extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "uploads"; 
 
    @Override 
    public void init() throws ServletException { 
        String uploadPath = getServletContext().getRealPath("/uploads"); 
        File uploadDir = new File(uploadPath); 
        if (!uploadDir.exists()) { 
            uploadDir.mkdir(); 
        } 
    }
    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, 
IOException { 
        String action = req.getParameter("action"); 
        HttpSession session = req.getSession(); 
        /*PrintWriter out = resp.getWriter();*/
        if ("history".equals(action)) { 
            String url = System.getenv("DB_URL");
    		String user = System.getenv("DB_USER");
    		String password = System.getenv("DB_PASSWORD");
    		try (Connection connection = DriverManager.getConnection(url, user, password);
    				PreparedStatement statement = connection.prepareStatement("SELECT name, male, content, file, status, id, aaa FROM qqa WHERE namae = ? ORDER BY id ASC")){
    			List<Inquiry> inquiries = Collections.synchronizedList(new ArrayList<>()); 
    			String aoa = (String) session.getAttribute("namae2");
    			statement.setString(1, aoa);
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
    		} catch (SQLException e) {
    			generateCaptcha(req);
                /*out.println("Database exception: " + e.getMessage());*/
    		}catch (Exception e) {
    			generateCaptcha(req);
                /*out.println("Exception" + e.getMessage());*/
            }

            RequestDispatcher rd = req.getRequestDispatcher("/jsp/inquiry_history.jsp"); 
            rd.forward(req, resp); 
        } else if ("download".equals(action)) {
            String qqaKeyStr = req.getParameter("qqaKey");
            try {
                int qqaKey = Integer.parseInt(qqaKeyStr);
                String url = System.getenv("DB_URL");
        		String user = System.getenv("DB_USER");
        		String password = System.getenv("DB_PASSWORD");
                try (Connection connection = DriverManager.getConnection(url, user, password);
                     PreparedStatement statement = connection.prepareStatement("SELECT filepart, file FROM qqa WHERE id = ?")) {
                    statement.setInt(1, qqaKey);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                        	String aa = rs.getString("file");
                            byte[] fileBytes = rs.getBytes("filepart");
                            if (fileBytes != null) {
                                resp.setContentType("application/octet-stream");
                                resp.setHeader("Content-Disposition", "attachment; filename=\"" + aa + "\"");
                                resp.getOutputStream().write(fileBytes);
                                resp.getOutputStream().flush();
                                return;
                            }
                        }
                        req.setAttribute("errorMessage", "ファイルが見つかりません");
                        generateCaptcha(req);
                        RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                        rd.forward(req, resp);
                    }
                } catch (SQLException e) {
                    req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
                    generateCaptcha(req);
                    RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                    rd.forward(req, resp);
                }
            } catch (NumberFormatException e) {
                req.setAttribute("errorMessage", "無効なキー: " + e.getMessage());
                generateCaptcha(req);
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                rd.forward(req, resp);
            }
        } else { 
            generateCaptcha(req); 
            /*HttpSession session = req.getSession(); */
            String wa = (String) session.getAttribute("namae2");
            String url = System.getenv("DB_URL");
    		String user = System.getenv("DB_USER");
    		String password = System.getenv("DB_PASSWORD");
    		if (wa != null) {
    		try (Connection connection = DriverManager.getConnection(url, user, password);
    				PreparedStatement statement = connection.prepareStatement("SELECT name, mail, ope FROM hito WHERE namae = ?")){
    			statement.setString(1, wa);
    			ResultSet results = statement.executeQuery();
    			Inquiry inquiry = new Inquiry();
    		results.next();
    		String name = results.getString("name");
			String mail = results.getString("mail");
			String ope = results.getString("ope");
			if(ope == null) {
				session.setAttribute("op", "ope0");
			}else {
			if (ope.equals("123a5")) {
			session.setAttribute("op", "ope");
			}else if (ope.equals("123s5")) {
				session.setAttribute("op", "ope2");
			}else if (ope.equals("123d5")) {
				session.setAttribute("op", "ope3");
			}else if (ope.equals("123f5")) {
				session.setAttribute("op", "ope4");
			}else if (ope.equals("123g5")) {
				session.setAttribute("op", "ope5");
			}else if (ope.equals("123h5")) {
				session.setAttribute("op", "ope6");
			}else if (ope.equals("123j5")) {
				session.setAttribute("op", "ope7");
			}else {
				session.setAttribute("op", "ope0");
			}}
			inquiry.setName(name);
			inquiry.setEmail(mail);
			req.setAttribute("inquiry", inquiry);
			RequestDispatcher rd = req.getRequestDispatcher("/index.jsp"); 
            rd.forward(req, resp); 
    		} catch (SQLException e) {
    			generateCaptcha(req);
    			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
    			RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                rd.forward(req, resp);
    		}catch (Exception e) {
    			generateCaptcha(req);
                /*out.println("Exception" + e.getMessage());*/
    			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
    			RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                rd.forward(req, resp);
            }
    		}
    		else { RequestDispatcher rd = req.getRequestDispatcher("/index.jsp"); 
            rd.forward(req, resp); }
        } 
    } 
   /* if (file != null) {
		byte[] fileBytes = results.getBytes("file");
		resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"attachment_" + id + "\"");
        resp.getOutputStream().write(fileBytes);
        resp.getOutputStream().flush();
		}*/
    @Override 
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, 
IOException { 
        req.setCharacterEncoding("UTF-8"); 
        String action = req.getParameter("action"); 
        HttpSession session = req.getSession(); 
        /*PrintWriter out = resp.getWriter();*/
        if (action != null && action.equals("complete")) { 
            Inquiry inquiry = (Inquiry) session.getAttribute("inquiry"); 
            Part filePar = inquiry.getFileNamePart();
            if (inquiry != null) {  
                String url = System.getenv("DB_URL");
        		String user = System.getenv("DB_USER");
        		String password = System.getenv("DB_PASSWORD");
        		String sql = "INSERT INTO qqa (name, male, content, file, filepart, aaa, namae, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        		try (Connection connection = DriverManager.getConnection(url, user, password);
        				PreparedStatement statement = connection.prepareStatement(sql)){
        					statement.setString(1, inquiry.getName());
        		            statement.setString(2, inquiry.getEmail());
        		            statement.setString(3, inquiry.getContent());
        		            statement.setString(4, inquiry.getAttachmentFileName());
        		            statement.setString(6, inquiry.getAaa());
        		            statement.setString(8, "新規");
        		            String nata = (String) session.getAttribute("namae2");
        		            req.setAttribute("nata", nata);
        		            req.setAttribute("aa", "out");
        		            statement.setString(7, nata);
        		            byte[] fileBytes = null;
        	                if (filePar != null && filePar.getSize() > 0) {
        	                    try (InputStream inputStream = filePar.getInputStream()) {
        	                        fileBytes = inputStream.readAllBytes(); 
        	                    }
        	                }
        	                if (fileBytes != null) {
        	                    statement.setBytes(5, fileBytes); 
        	                } else {
        	                    statement.setNull(5, java.sql.Types.BINARY); 
        	                }
        		            statement.executeUpdate();
        		} catch (SQLException e) {
        			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
        			generateCaptcha(req);
                    RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                    rd.forward(req, resp);
        		}catch (Exception e) {
        			req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
        			generateCaptcha(req);
                    RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                    rd.forward(req, resp);
                }
            } 
            session.removeAttribute("inquiry"); 
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/complete.jsp"); 
            rd.forward(req, resp); 
        } else if (action != null && action.equals("updateStatus")) { 
            int index = Integer.parseInt(req.getParameter("index")); 
            String newStatus = req.getParameter("newStatus"); 
           String url = System.getenv("DB_URL");
    		String user = System.getenv("DB_USER");
    		String password = System.getenv("DB_PASSWORD");
    		String sql = "UPDATE qqa SET status = ? WHERE id = ?";
    		try (Connection connection = DriverManager.getConnection(url, user, password);
    				PreparedStatement statement = connection.prepareStatement(sql)){
    				statement.setString(1,  newStatus);
    				statement.setInt(2, index);
    		            statement.executeUpdate();
    		} catch (SQLException e) {
    			req.setAttribute("errorMessage", "データベースエラー: " + e.getMessage());
    			generateCaptcha(req);
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                rd.forward(req, resp);
    		}catch (Exception e) {
    			req.setAttribute("errorMessage", "予期せぬエラー: " + e.getMessage());
    			generateCaptcha(req);
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
                rd.forward(req, resp);
            }
            /*inquiryDAO.updateInquiryStatus(index, newStatus)*/; 
            resp.sendRedirect("inquiry?action=history"); 
        } else { 
            String name = req.getParameter("name"); 
            String email = req.getParameter("email"); 
            String content = req.getParameter("content");
            String newaaa = req.getParameter("newaaa");
            String captchaInput = req.getParameter("captcha"); 
            String fileName = null; 
            Part filePart = null;
            try { 
                filePart = req.getPart("attachment"); 
                if (filePart != null && filePart.getSize() > 0) { 
                    fileName = getFileName(filePart); 
                    String uploadPath = getServletContext().getRealPath("") + File.separator + 
UPLOAD_DIRECTORY; 
                    filePart.write(uploadPath + File.separator + fileName); 
                } 
            } catch (Exception e) { 
                req.setAttribute("errorMessage", "ファイルのアップロード中にエラーが発生しました: " + 
e.getMessage()); 
            } 
            Inquiry inquiry = new Inquiry(); 
            inquiry.setName(name); 
            inquiry.setEmail(email); 
            inquiry.setContent(content); 
            inquiry.setAttachmentFileName(fileName); 
            inquiry.setFileNamePart(filePart);
            inquiry.setAaa(newaaa);
            Map<String, String> errors = new HashMap<>(); 
            if (name == null || name.trim().isEmpty()) { 
                errors.put("name", "名前は必須です。"); 
            } 
            if (email == null || email.trim().isEmpty()) { 
                errors.put("email", "メールアドレスは必須です。"); 
            } else if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) { 
                errors.put("email", "有効なメールアドレスを入力してください。"); 
            } 
            if (content == null || content.trim().isEmpty()) { 
                errors.put("content", "内容は必須です。"); 
            } 
            if (!verifyCaptcha(req, captchaInput)) { 
                errors.put("captcha", "CAPTCHA が不正です。"); 
            } 
            if (!errors.isEmpty()) { 
                req.setAttribute("errors", errors); 
                req.setAttribute("inquiry", inquiry); 
                generateCaptcha(req); 
                RequestDispatcher rd = req.getRequestDispatcher("/index.jsp"); 
                rd.forward(req, resp); 
            } else { 
                session.setAttribute("inquiry", inquiry); 
                req.setAttribute("name", name); 
                req.setAttribute("email", email); 
                req.setAttribute("content", content); 
                req.setAttribute("attachmentFileName", fileName); 
                req.setAttribute("newaaa", newaaa);
                RequestDispatcher rd = req.getRequestDispatcher("/jsp/confirm.jsp"); 
                rd.forward(req, resp); 
            } 
        } 
    } 
 
    private void generateCaptcha(HttpServletRequest req) { 
        Random rand = new Random(); 
        int num1 = rand.nextInt(10) + 1; 
        int num2 = rand.nextInt(10) + 1; 
        int answer = num1 + num2; 
        req.getSession().setAttribute("captchaAnswer", answer); 
        req.setAttribute("captchaQuestion", num1 + " + " + num2 + " = ?"); 
    } 
 
    private boolean verifyCaptcha(HttpServletRequest req, String userAnswerStr) { 
        HttpSession session = req.getSession(false); 
        if (session == null) 
            return false; 
        Integer captchaAnswer = (Integer) session.getAttribute("captchaAnswer"); 
        if (captchaAnswer == null || userAnswerStr == null || userAnswerStr.isEmpty()) { 
            return false; 
        } 
        try { 
            int userAnswer = Integer.parseInt(userAnswerStr); 
            return captchaAnswer.equals(userAnswer); 
        } catch (NumberFormatException e) { 
            return false; 
        } 
    }
    private String getFileName(Part part) { 
        for (String content : part.getHeader("content-disposition").split(";")) { 
            if (content.trim().startsWith("filename")) { 
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", ""); 
            } 
        } 
        return null; 
    } 
}