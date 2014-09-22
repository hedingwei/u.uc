<%-- 
    Document   : dpi_endpoint
    Created on : 2014-8-17, 17:11:48
    Author     : 定巍
--%>

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
                    rows: [
                        {
                            cols: [
                                {view: "text", id: "id_devName", label: "设备名：", value:"*"},
                                {view: "text", id: "id_ip", label: "IP：", value:"*"},
                                {view: "text", id: "id_messageType", label: "消息类型：", value:"*"},
                                {view: "text", id: "id_messageNo", label: "消息号：", value:"*"},
                                {view: "text", id: "id_limit", label: "条数：", value: "10"},
                                {view: "button", value: "查找", click:search}
                            ]
                        },
                        {
                            view: "datatable",
                            id: "datatable1",
                            select: "row",
                            multiselect: false,
                            columns: [
                                {id: "messageType", header: "消息类型", width: 120},
                                {id: "messageNo", header: "消息号", width: 120},
                                {id: "messageSequenceNo", header: "序列号", width: 120},
                                {id: "id", header: "日志ID", width: 100},
                                {id: "devName", header: "设备名", width: 100},
                                {id: "ip", header: "IP", width: 100},
                                {id: "sendTime", header: "发送时间", width: 180},
                                {id: "ackTime", header: "Ack时间", width: 180}
                            ],
                            data: []
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
                
                function search(){
                    webix.ajax().post("./../../logic/dpi/SearchMessageSendingLog", {limit:$$("id_limit").getValue(),devName:$$("id_devName").getValue(),messageType:$$("id_messageType").getValue(), messageNo:$$("id_messageNo").getValue(),  ip: $$("id_ip").getValue()}, function(data){
                        var mdata = JSON.parse(data);
                        $$("datatable1").clearAll();
                        $$("datatable1").define("data", mdata);
                    });
                }
                
            });
        </script>   
    </head>
    <body>
    </body>
</html>
