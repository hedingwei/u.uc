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
                var grid_data = [
                    {messageNo: 1, serialNo: 1, lastUpdateTime: 1123123, action: ""}
                ];
                var p_n = {id: "root", value: "", open: true, data: [
                        {id: "1", open: true, value: "基于应用的管理策略", data: [
                                {id: "1.1", value: "通用参数设置", pname: "0x00"},
                                {id: "1.2", value: "流量识别结果上报策略"},
                                {id: "1.3", value: "Web类流量管理策略"},
                                {id: "1.4", value: "VoIP类流量管理策略"},
                                {id: "1.5", value: "通用流量管理策略"},
                                {id: "1.6", value: "通用流量标记策略"},
                                {id: "1.7", value: "访问指定应用的用户统计策略"},
                                {id: "1.8", value: "流量镜像策略"},
                                {id: "1.9", value: "应用特征自定义策略"}
                            ]},
                        {id: "2", open: true, value: "基于用户的管理策略", data: [
                                {id: "2.1", value: "用户组归属分配策略"},
                                {id: "2.2", value: "Web 信息推送策略"},
                                {id: "2.3", value: "一拖 N 用户管理策略"},
                                {id: "2.4", value: "应用层 DDoS 异常流量管理策略"}
                            ]},
                        {id: "3", open: true, value: "用户/应用绑定管理策略", data: [
                                {id: "3.1", value: "IP 地址段用户信息下发"},
                                {id: "3.2", value: "用户/应用策略信息下发"}
                            ]},
                        {id: "4", open: true, value: "DPI通用设备管理策略", data: [
                                {id: "4.1", value: "DPI 设备通用信息下发"},
                                {id: "4.2", value: "DPI 设备状态查询"}
                            ]}
                    ]};

                webix.ui({
                    rows: [
                        {
                            view: "treetable",
                            columns: [
                                {id: "id", header: "", css: {"text-align": "right"}, width: 50},
                                {id: "repo", header: "策略仓库", width: 250, template: "{common.treetable()} #value#"},
                                {id: "messageType", header: "MessageType", width: 200},
                                {id: "messageCount", header: "策略数量", width: 200},
                                {id: "serialNo", header: "最新版本", width: 200},
                                {id: "c", header: "", width: 200}
                            ],
                            autoheight: true,
                            autowidth: true,
                            select: "row",
                            multiselect: true,
                            drag: true,
                            data: [
                                {"id": "1", "repo": "基于应用的管理策略", "open": true, "data": [
                                        {"id": "1.1", "repo": "通用参数设置", "messageType": "0x00", "open": true, "data": [
                                                {"id": "1.1.1", "repo": "GN-1", "messageCount": 10, "serialNo": 1},
                                                {"id": "1.1.2", "repo": "GN-2", "messageCount": 10, "serialNo": 2}
                                            ]}

                                    ]},
                                {"id": "2", "repo": "基于应用的管理策略1", "open": true, "data": [
                                        {"id": "2.1", "repo": "通用参数设置1", "messageType": "0x00", "messageCount": 10, "serialNo": 1},
                                        {"id": "2.2", "repo": "通用参数设置2", "messageType": "0x01", "messageCount": 10, "serialNo": 2}
                                    ]}
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
