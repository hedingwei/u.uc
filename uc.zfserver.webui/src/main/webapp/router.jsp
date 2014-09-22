<%-- 
    Document   : router
    Created on : 2014-9-11, 23:13:45
    Author     : 定巍
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%
            String route = request.getParameter("route");
            route = route.replace("\\.", "/");
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="webix/webix.css" type="text/css" media="screen" charset="utf-8">
        <script src="webix/webix.js" type="text/javascript" charset="utf-8"></script>
        <script src="webix/base64.js" type="text/javascript" charset="utf-8"></script>
        <script src="pages/<%=route+".js"%>" type="text/javascript" charset="utf-8"></script>
        <script type="text/javascript" charset="utf-8">

            webix.ready(function() {
                init();
            });
        </script>
    </head>
    <body>

    </body>
</html>
