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
            <%
                String webservice = PropertyUtil.getInstance().getProperty("PolicyRepoServer.webserver.url");
            %>

            webix.ready(function() {

                var form = [
                    {view: "text", id: "input_WebService", value: '<%=webservice%>', label: "策略仓库服务Web服务:", inputAlign: "left", labelWidth: 170},
                    {view: "text", id: "label_WebServiceServer", label: "服务器状态:", value: "服务正常", inputAlign: "left", labelWidth: 170, readonly: true}
                ];
                webix.ui({
                    width: "100%",
                    rows: [
                        {
                            view: "toolbar",
                            id: "myToolbar",
                            cols: [
                                {view: "button", id: "updateBtn", click: update, value: "更新", width: 100, align: "left"},
                                {view: "button", id: "testBtn", click: test,value: "测试", width: 100, align: "left"}
                            ]
                        },
                        {
                            rows: [
                                {
                                    view: "form",
                                    scroll: false,
                                    elements: form
                                },
                                {
                                    view: "accordion",
                                    rows: [
                                        {
                                            header: "策略服务测试",
                                            body: {
                                                rows: [
                                                    {view: "text", id: "label_test1", label: "测试项目1:", value: "服务正常", inputAlign: "left", readonly: true},
                                                    {view: "text", id: "label_test2", label: "测试项目2:", value: "服务正常", inputAlign: "left", readonly: true},
                                                    {view: "text", id: "label_test3", label: "测试项目3:", value: "服务正常", inputAlign: "left", readonly: true}
                                                ]
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }).show();

                function update() {
                    $$('label_WebServiceServer').setValue("服务器设置已经更改，等待测试......");
                    webix.ajax().post("./../system/setting/SetProperty", {key: "PolicyRepoServer.webserver.url", value: $$('input_WebService').getValue()}, function(data) {
                        fireEvent("rpurl.changed",{});
                        var rst = JSON.parse(data);
                        if (rst.code === 1) {
                            webix.message("Web服务地址更新成功");
                        }
                        prsClientInvoke("start", [$$('input_WebService').getValue()], function(data) {
                            var rst = JSON.parse(data);
                            if (rst.code === 1) {
                                webix.message("Web服务已经重新启动");
                                $$('label_WebServiceServer').setValue("服务正常");
                            }
                            prsClientInvoke("isStarted", [], function(data) {
                                var rst = JSON.parse(data);
                                if (rst.code === 1) {
                                    if (rst.rst === 0) {
                                        webix.message("Web服务器状态监测挖鼻：已经重新启动");
                                        $$('label_WebServiceServer').setValue("服务正常");
                                    }
                                }
                            });
                        });
                    });
                }

                function test() {
                    $$('label_test1').setValue("策略服务测试中，等待......");
                    $$('label_test2').setValue("策略服务测试中，等待......");
                    $$('label_test3').setValue("策略服务测试中，等待......");
                    prClientInvoke("test1", [], function(data) {
                        var rst = JSON.parse(data);
                        if (rst.code === 1) {
                            if (rst.rst[0] === "ok1") {
                                webix.message("策略服务测试1：通过");
                                $$('label_test1').setValue("服务正常");
                            }else{
                            $$('label_test1').setValue("测试未通过");
                        }
                        }else{
                            $$('label_test1').setValue("测试未通过");
                        }
                    });
                    prClientInvoke("test2", [], function(data) {
                        var rst = JSON.parse(data);
                        if (rst.code === 1) {
                            if (rst.rst[0] === "ok2") {
                                webix.message("策略服务测试2：通过");
                                $$('label_test2').setValue("服务正常");
                            }else{
                                $$('label_test2').setValue("测试未通过");
                            }
                        }else{
                            $$('label_test2').setValue("测试未通过");
                        }
                    });
                    prClientInvoke("test3", [], function(data) {
                        var rst = JSON.parse(data);
                        if (rst.code === 1) {
                            if (rst.rst[0] === "ok3") {
                                webix.message("策略服务测试3：通过");
                                $$('label_test3').setValue("服务正常");
                            }else{
                                $$('label_test3').setValue("测试未通过");
                            }
                        }else{
                            $$('label_test3').setValue("测试未通过");
                        }
                    });
                }

                function prClientInvoke(method, paras, call) {
                    var p = Base64.encode(JSON.stringify(paras));
                    webix.ajax().post("./../system/setting/PolicyRepoClientCall", {method: method, paras: p}, call);
                }

                function prsClientInvoke(method, paras, call) {
                    var p = Base64.encode(JSON.stringify(paras));
                    webix.ajax().post("./../system/setting/PolicyRepoServerClient", {method: method, paras: p}, call);
                }
                
                function fireEvent(ev,args){
                    var p = Base64.encode(JSON.stringify(args));
                    webix.ajax().post("./../system/setting/FireMessage", {event: ev, args:p},function(){});
                }
            });
        </script>   
    </head>
    <body>
    </body>
</html>
