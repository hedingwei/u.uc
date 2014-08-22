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
               
                var pulish_standard = {
                    cols: [
                        {
                            view: "accordion",
                            multi: true,
                            cols: [
                                {
                                    header: "任务一览",
                                    body: {
                                        view: "datatable",
                                        id: "datatable1",
                                        select: "row",
                                        multiselect: false,
                                        gravity: 0.3,
                                        columns: [
                                            {id: "time", header: "时间", width: 100},
                                            {id: "repo", header: "DPI", width: 100},
                                            {id: "repo", header: "策略仓库", width: 100},
                                            {id: "taskid", header: "任务ID", width: 80},
                                            {id: "comment", header: "描述", width: 180}
                                        ],
                                        data: []
                                    }
                                }
                                ,
                                {view: "resizer"}
                                ,
                                {
                                    header: "任务详情",
                                    body: {
                                        view: "accordion",
                                        multi: true,
                                        rows: [
                                            {
                                                view: "datatable",
                                                id: "datatable2",
                                                select: "row",
                                                width: "100%",
                                                multiselect: false,
                                                columns: [
                                                    {id: "messageType", header: "MessageType", width: 100},
                                                    {id: "versionStart", header: "起始版本", width: 80},
                                                    {id: "versionEnd", header: "终止版本", width: 180},
                                                    {id: "messageCount", header: "消息数", width: 180}
                                                ],
                                                data: []
                                            }, {view: "resizer"},
                                            {
                                                header: "消息交互日志",
                                                id: "subbody",
                                                body: {
                                                    view: "datatable",
                                                    id: "datatable2",
                                                    select: "row",
                                                    width: "100%",
                                                    multiselect: false,
                                                    columns: [
                                                        {id: "messageNo", header: "MessageNo", width: 100},
                                                        {id: "timeSend", header: "下发时间", width: 80},
                                                        {id: "ackOK", header: "ACK", width: 180},
                                                        {id: "errorInfo", header: "消息数", width: 180}
                                                    ],
                                                    data: []
                                                }
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
                                    body: pulish_standard
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
