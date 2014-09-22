<%-- 
    Document   : dpi_endpoint
    Created on : 2014-8-17, 17:11:48
    Author     : 定巍
--%>

<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.List"%>

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

            <%
                Object obj = session.getAttribute("temp.cart.policies");
                String cart = null;
                if (obj == null) {
                    session.setAttribute("temp.cart.policies", new JSONArray());
                    cart = "[]";
                } else {
                    JSONArray array = (JSONArray) session.getAttribute("temp.cart.policies");
                    cart = array.toString();
                }
            %>

            webix.ready(function() {
                var grid_data = [
                    {messageNo: 1, serialNo: 1, lastUpdateTime: 1123123, action: ""}
                ];

                webix.ui({
                    rows: [
                        {
                            view: "toolbar", cols: [
                                {view: "label", label: "MessageNo:"},
                                
                                {view: "button", label: '关闭', width: 100, align: 'right'}
                            ]
                        },
                        {
                            view: "datatable",
                            id: "id_policies",
                            select: "row",
                            multiselect: false,
                            autoConfig:true,
                            onContext: {},
                            columns: [
                                {id: "messageType", header: [{text: "类型"}, {content: "selectFilter"}],width:150},
                                {id: "instance", header: [{text: "实例"}, {content: "selectFilter"}],width:200},
                                {id: "messageNo", header: [{text: "MessageNo"}, {content: "selectFilter"}],width:150},
                                {id: "serialNo", header: [{text: "SerialNo"}, {content: "selectFilter"}],width:150}
                            ],
                            data: eval(<%=cart%>)
                        }
                    ]
                }).show();


                $$("id_policies").attachEvent("onItemDblClick", function(id, e, node) {
                    showPolicy($$("datatable1").getItem(id));
                });

                function prClientInvoke(method, paras, call) {
                    var p = Base64.encode(JSON.stringify(paras));
                    webix.ajax().post("./../../system/setting/PolicyRepoClientCall", {method: method, paras: p}, call);
                }

                function pageselect(pagecount, page) {
                    if (undefined === $$('repocitory').getSelectedItem()) {
                        webix.message({type: "error", text: "操作有误：请选择一个策略仓库"});
                    } else {
                        prClientInvoke("getHeadPoliciesWithLog", [$$('navigator').getSelectedItem().pname, $$('repocitory').getSelectedItem().pid, pagecount, page], function(data) {
//                            console.log(data);
                            var obj = JSON.parse(data);
                            var rawset = obj.rst[0];
                            var dataset = [];
//                            {messageNo: 1, serialNo:1, lastUpdateTime:1123123, action:""}
                            for (var i = 0; i < rawset.length; i++) {
                                tmp = {};
                                tmp.serialNo = rawset[i].id;
                                tmp.messageNo = rawset[i].svnFile.messageNo;
                                tmp.lastUpdateTime = webix.i18n.dateFormatStr(new Date(rawset[i].createTime)) + " " + webix.i18n.timeFormatStr(new Date(rawset[i].createTime));
                                tmp.content = Base64.decode(rawset[i].svnFile.content);
                                dataset.push(tmp);
                            }

                            $$("datatable1").clearAll();
                            $$("datatable1").define("data", dataset);
                        });
                    }
                }

                function showPolicy(item) {
                    webix.ui({
                        view: "window",
                        id: "policyWindow",
                        height: 480,
                        width: 640,
                        position: "center",
                        move: true,
                        modal: true,
                        mydata: item,
                        head: {
                            view: "toolbar", cols: [
                                {view: "label", label: "MessageNo:" + item.messageNo},
                                {view: "button", label: '生成二进制', width: 100, align: 'right', click: showBinaryForm},
                                {view: "button", label: '保存/更新', width: 100, align: 'right', click: saveItem},
                                {view: "button", label: '关闭', width: 100, align: 'right', click: "$$('policyWindow').close();"}
                            ]
                        },
                        body: {
                            rows: [
                                {view: "text", id: "label_messageNo", value: item.messageNo, label: "MessageNo:", inputAlign: "left", labelWidth: 100},
                                {view: "text", id: "label_serialNo", value: item.serialNo, label: "SerialNo:", inputAlign: "left", labelWidth: 100, readonly: true},
                                {view: "text", id: "label_time", value: item.lastUpdateTime, label: "最新时间:", inputAlign: "left", labelWidth: 100, readonly: true},
                                {
                                    rows: [{
                                            cells: [
                                                {view: "textarea", id: "label_content_xml_view", value: item.content, inputAlign: "left", labelWidth: 100, readonly: false},
                                                {
                                                    id: "label_content_binary_view",
                                                    view: "textarea", value: item.content, inputAlign: "left", labelWidth: 100, readonly: true
                                                },
                                                {
                                                    id: "label_content_form_view",
                                                    template: "About the app"
                                                }
                                            ]
                                        },
                                        {
                                            view: "tabbar",
                                            type: "bottom",
                                            multiview: true,
                                            options: [
                                                {value: "<span class='webix_icon fa-film'></span><span style='padding-left: 4px'>XML</span>", id: 'label_content_xml_view'},
                                                {value: "<span class='webix_icon fa-comments'></span><span style='padding-left: 4px'>二进制</span>", id: 'label_content_binary_view'},
                                                {value: "<span class='webix_icon fa-info'></span><span style='padding-left: 1px'>表单</span>", id: 'label_content_form_view'}
                                            ],
                                            height: 50
                                        }]
                                }
                            ]
                        }
                    }).show();
                }


                function saveItem() {
                    prClientInvoke("updatePolicy", [$$('navigator').getSelectedItem().pname, $$('repocitory').getSelectedItem().pid, new Number($$("label_messageNo").getValue()), Base64.encode($$("label_content_xml_view").getValue())], function(data) {
                        var ptr = JSON.parse(data);
                        ptr = ptr.rst[0];
                        var id = $$("datatable1").getSelectedId();
                        data = {messageNo: ptr.svnFile.messageNo, serialNo: ptr.id, lastUpdateTime: webix.i18n.dateFormatStr(new Date(ptr.createTime)) + " " + webix.i18n.timeFormatStr(new Date(ptr.createTime)), content: Base64.decode(ptr.svnFile.content)};
                        $$("datatable1").updateItem(id, data);
                        $$('policyWindow').close();
                    });
                }

                function showBinaryForm() {
//                    messageNo,serialNo,messageType,fromDB,content,call
                    item = $$("policyWindow").config.mydata;
                    messageNo = item.messageNo;
                    serialNo = item.serialNo;
                    messageType = $$('navigator').getSelectedItem().pname;
                    instance = $$('repocitory').getSelectedItem().pid;
                    webix.message("messageType:" + messageType);
                    webix.message("instance:" + instance);
                    webix.message("serialNo:" + serialNo);
                    webix.message("messageNo:" + messageNo);

                    webix.ajax().post("./../../system/util/BinaryFormPolicy", {messageNo: messageNo, serialNo: serialNo, messageType: messageType, instance: instance, fromDatabase: false, content: Base64.encode($$("label_content_xml_view").getValue())}, function(data) {
                        var td = JSON.parse(data);
                        var md = Base64.decode(td.rst);
                        $$("label_content_binary_view").define("value", md);
                        $$('label_content_binary_view').show();
                    });
                }

            });
        </script>   
    </head>
    <body>
    </body>
</html>
