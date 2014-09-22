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

                var smalltreedata = [
                    {id: "root", value: "测试用户组A", open: false, data: [
                            {id: "a.1", value: "账号用户"},
                            {id: "a.2", value: "IP地址段用户"},
                            {id: "a.3", value: "链路用户"},
                            {id: "a.4", value: "BRAS归属"}
                        ]}
                ];


                webix.ui({
                    rows: [
                        {view: "template", //optional
                            template: "用户组管理",
                            type: "header"
                        },
                        {
                            cols: [
                                {
                                    view: "grouplist",
                                    scroll: false,
                                    data: smalltreedata,
                                    select: true
                                },
                                {
                                    type: "wide", rows: [
//                                        {type: "header", template: "XXX"},
                                        {view: "tabview", cells: [
                                                {
                                                    header: "操作日志",
                                                    body: {
                                                        template: "Place for the form control", height: 300
                                                    }
                                                },
                                                {
                                                    header: "详情",
                                                    body: {
                                                        view: "list",
                                                        template: "#rank#. #title# <div style='padding-left:18px'> Year:#year#, votes:#votes# </div>",
                                                        type: {
                                                            height: 50
                                                        },
                                                        select: true,
                                                        data: smalltreedata
                                                    }
                                                }
                                            ], multiview: {fitBiggest: true}}
                                    ]
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
