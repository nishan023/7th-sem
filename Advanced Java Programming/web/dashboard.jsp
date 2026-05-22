<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard - Nishan Dhakal</title>
</head>
<body>
<%
    String username = null;
    Cookie[] cookies = request.getCookies();
    if(cookies != null){
        for(Cookie c : cookies){
            if(c.getName().equals("username")){
                username = c.getValue();
                break;
            }
        }
    }
    if(username == null) {
        // Not logged in or session expired
        response.sendRedirect("login.html");
    } else {
%>
    <h2>Welcome to your Dashboard, <%= username %>! (Nishan Dhakal)</h2>
    <p>You have successfully logged in via Servlet + JDBC and we are managing your session using Cookies!</p>
    
    <ul>
        <li><a href="feedback.html">Go to JSP Feedback Form</a></li>
        <li><a href="LogoutServlet">Logout</a></li>
    </ul>
<%
    }
%>
</body>
</html>