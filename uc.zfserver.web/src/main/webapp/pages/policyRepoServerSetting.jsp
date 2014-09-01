<%-- 
    Document   : dpi_endpoint
    Created on : 2014-8-17, 17:11:48
    Author     : 定巍
--%>

<%@page import="com.ambimmort.uc.zfserver.db.util.PropertyUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../webix/webix.css" type="text/css" media="screen" charset="utf-8">
        <script src="../webix/webix.js" type="text/javascript" charset="utf-8"></script>
        <script src="../webix/base64.js" type="text/javascript" charset="utf-8"></script>
        <title>JSP Page</title>
        <script type="text/javascript" charset="utf-8">


            webix.ready(function() {

                var big_film_set = [
                    {"id": 1, "title": "通用参数设置策略", "messageType": "0x00"},
                    {"id": 2, "title": "流量分析结果上报策略", "messageType": "0x01"},
                    {"id": 3, "title": "Web类流量管理策略", "messageType": "0x02"},
                    {"id": 4, "title": "用户/应用策略信息下发", "messageType": "0x85"}
                ];


                webix.ui({
                    width: "100%",
                    rows: [
                        {
                            view: "toolbar",
                            id: "myToolbar",
                            cols: [
                                {view: "button", id: "updateBtn", value: "更新", width: 100, align: "left"},
                                {view: "button", id: "testBtn", value: "测试", width: 100, align: "left"}
                            ]
                        },
                        {
                            rows: [
                                {
                                    view: "accordion",
                                    height: "100%",
                                    multi: true,
                                    cols: [
                                        {
                                            header: "策略仓库（MessageType）",
                                            body: {
                                                rows: [
                                                    {
                                                        id: "id_messageType",
                                                        view: "list",
                                                        template: "(#messageType#). #title#",
                                                        select: true,
                                                        data: big_film_set
                                                    },
                                                    {view: "resizer"},
                                                    {
                                                        header: "仓库实例",
                                                        body: {
                                                            view: "list",
                                                            template: "(#messageType#). #title#",
                                                            select: true,
                                                            data: big_film_set
                                                        }

                                                    }
                                                ]
                                            }
                                        },
                                        {
                                            header: "策略列表",
                                            body: {
                                                view: "list",
                                                template: "(#messageType#). #title#",
                                                select: true,
                                                data: big_film_set
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }).show();


                $$('id_messageType').attachEvent('onItemClick', function(id, e, node) {
//                    prClientInvoke("getHeadPoliciesCount", [$$('navigator').getItem(id).pname, $$('repocitory').getSelectedItem().pid], function(data) {
//                        ret = JSON.parse(data);
//                        $$("pager1").config.count = ret.rst[0];
//                        webix.message("查询到:" + ret.rst[0] + "条记录");
//                    });
                    webix.message( $$('id_messageType').getSelectedItem());
                    return false;
                });

                function prClientInvoke(method, paras, call) {
                    var p = Base64.encode(JSON.stringify(paras));
                    webix.ajax().post("./../system/setting/PolicyRepoClientCall", {method: method, paras: p}, call);
                }

                function prsClientInvoke(method, paras, call) {
                    var p = Base64.encode(JSON.stringify(paras));
                    webix.ajax().post("./../system/setting/PolicyRepoServerClient", {method: method, paras: p}, call);
                }

                function fireEvent(ev, args) {
                    var p = Base64.encode(JSON.stringify(args));
                    webix.ajax().post("./../system/setting/FireMessage", {event: ev, args: p}, function() {
                    });
                }
            });
        </script>   
    </head>
    <body>
    </body>
</html>
