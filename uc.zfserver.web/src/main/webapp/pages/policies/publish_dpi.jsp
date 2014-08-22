<%-- 
    Document   : dpi_endpoint
    Created on : 2014-8-17, 17:11:48
    Author     : 定巍
--%>

<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.ambimmort.uc.zfserver.db.entities.PolicyRepositoryBean"%>
<%@page import="java.util.List"%>
<%@page import="com.ambimmort.uc.zfserver.db.util.PolicyRepositoryUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../webix/webix.css" type="text/css" media="screen" charset="utf-8">
        <script src="../../webix/webix.js" type="text/javascript" charset="utf-8"></script>
        <script src="../../webix/base64.js" type="text/javascript" charset="utf-8"></script>
        <title>JSP Page</title>
        <script type="text/javascript" charset="utf-8">

            webix.ready(function() {
                
               
                var publish_dpistatus = {
                    cols: [
                        {
                            view: "accordion",
                            multi: true,
                            cols: [
                                {
                                    header: "DPI区域列表",
                                    gravity: 0.3,
                                    body: {}
                                },
                                {view: "resizer"},
                                {
                                    header: "DPI区域详情",
                                    body: {
                                        view: "accordion",
                                        multi: true,
                                        rows: [
                                            {
                                                body: {}
                                            },
                                            {view: "resizer"}
                                            ,
                                            {
                                                header: "DPI设备策略同步日志",
                                                body: {}
                                            }
                                        ]
                                    }
                                }
                            ]
                        }
                    ]
                };

                webix.ui({
                    rows: [
                        {
                            view: "accordion",
                            multi: true,
                            cols: [
                                {
                                    id: "subbody1",
                                    body: publish_dpistatus
                                }
                            ]
                        }
                    ]
                }).show();

            });
        </script>   
    </head>
    <body>
    </body>
</html>
