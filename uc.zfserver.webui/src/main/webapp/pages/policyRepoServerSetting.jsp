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
                                                        view: "datatable",
                                                        id: "id_messageType",
                                                        select: "row",
                                                        autowidth: true,
                                                        onContext: {},
                                                        columns: [
                                                            {id: "typeName", header: [{text: "类型名称"}], width: 250},
                                                            {id: "typeNo", header: [{text: "类型编号"}]}
                                                        ],
                                                        data: [
                                                            {typeName: "通用参数设置策略", typeNo: "0x00"},
                                                            {typeName: "流量分析结果上报策略", typeNo: "0x01"},
                                                            {typeName: "Web类流量管理策略", typeNo: "0x02"},
                                                            {typeName: "用户/应用策略信息下发", typeNo: "0x85"}
                                                        ]
                                                    },
                                                    {view: "resizer"},
                                                    {
                                                        header: "仓库实例",
                                                        body: {
                                                            view: "datatable",
                                                            id: "id_instance",
                                                            select: "row",
                                                            autowidth: true,
                                                            onContext: {},
                                                            columns: [
                                                                {id: "instanceName", header: [{text: "实例名称"}], width: 250},
                                                                {id: "serialNo", header: [{text: "最新版本"}], width: 250}
                                                            ],
                                                            data: [
                                                            ]
                                                        }

                                                    }
                                                ]
                                            }
                                        },
                                        {
                                            rows: [
                                                {
                                                    header: "策略列表",
                                                    body: {
                                                        rows: [
                                                            {
                                                                view: "toolbar",
                                                                id: "myToolbar",
                                                                cols: [
                                                                    {view: "button", id: "updateBtn", value: "新建", width: 100, align: "left"},
                                                                    {view: "button", id: "testBtn", value: "删除", width: 100, align: "left"}
                                                                ]
                                                            }, {view: "datatable",
                                                                id: "id_policies",
                                                                select: "row",
                                                                width: "100",
                                                                onContext: {},
                                                                columns: [
                                                                    {id: "messageNo", header: [{text: "MessageNo"}], width: 100},
                                                                    {id: "serialNo", header: [{text: "SerialNo"}], width: 100},
                                                                    {id: "time", header: [{text: "时间"}], width: 350}
                                                                ],
                                                                data: [
                                                                ]}
                                                        ]

                                                    }
                                                }
                                            ]
                                        }

                                    ]
                                }
                            ]
                        }
                    ]
                }).show();

                var selectedMessageType = null;
                var selectedInstanceName = null;
                var selectedPolicyItem = null;
                $$('id_messageType').attachEvent('onItemClick', function(id) {
                    var typeName = $$("id_messageType").getItem(id).typeName;
                    var typeNo = $$("id_messageType").getItem(id).typeNo;
                    prClientInvoke("listRepositories", [typeNo], function(data) {
                        ret = JSON.parse(data);
                        console.log(ret);
                        var instanceArray = ret.rst[0];
                        console.log(instanceArray);
                        var mdata = [];

                        for (var i = 0; i < instanceArray.length; i++) {
                            mdata.push({instanceName: instanceArray[i].instance,serialNo:instanceArray[i].serialNo});
                        }
                        $$("id_instance").clearAll();
                        $$("id_instance").define("data", mdata);
                    });
                    selectedMessageType = $$("id_messageType").getItem(id).typeNo;
                    selectedInstanceName = null;
                    selectedPolicyItem = null;
                    $$("id_policies").clearAll();
                    webix.message("您选择了:" + $$("id_messageType").getItem(id).typeName);
                });

                $$('id_instance').attachEvent('onItemClick', function(id) {

                    var instanceName = $$("id_instance").getItem(id).instanceName;
                    selectedInstanceName = instanceName;

                    prClientInvoke("getHeadPoliciesWithLog", [selectedMessageType, instanceName, 100, 0], function(data) {
                        ret = JSON.parse(data);
                        itemArray = ret.rst[0];
                        console.log(itemArray);
                        var mdata = [];
                        for (var i = 0; i < itemArray.length; i++) {
                            mdata.push({serialNo: itemArray[i].id, messageNo: itemArray[i].svnFile.messageNo, time: webix.i18n.dateFormatStr(new Date(itemArray[i].createTime)) + " " + webix.i18n.timeFormatStr(new Date(itemArray[i].createTime)), content: itemArray[i].svnFile.content});
                        }

//                       console.log(mdata);
                        selectedPolicyItem = null;
                        $$("id_policies").clearAll();
                        $$("id_policies").define("data", mdata);
                    });
                });

                $$('id_policies').attachEvent('onItemClick', function(id) {
                    showPolicy($$("id_policies").getItem(id));
                    selectedPolicyItem = $$("id_policies").getItem(id);
                });

                function showPolicy(item) {
                    webix.message("messageNo:"+item.messageNo);
                    webix.ui({
                        view: "window",
                        id: "policyWindow",
                        height: 480,
                        width: 800,
                        position: "center",
                        move: true,
                        modal: true,
                        mydata: item,
                        head: {
                            view: "toolbar", cols: [
                                {view: "label", label: "MessageNo:" + item.messageNo},
                                {view: "button", label: '发送到剪贴板', width: 130, align: 'right', click: sendToPolicyCart},
                                {view: "button", label: 'XML', width: 100, align: 'right', click: showXMLForm},
                                {view: "button", label: '二进制', width: 100, align: 'right', click: showBinaryForm},
                                {view: "button", label: '保存/更新', width: 100, align: 'right', click: saveItem},
                                {view: "button", label: '关闭', width: 100, align: 'right', click: "$$('policyWindow').close();"}
                            ]
                        },
                        body: {
                            rows: [
                                {view: "text", id: "label_messageNo", value: item.messageNo, label: "MessageNo:", inputAlign: "left", labelWidth: 100},
                                {view: "text", id: "label_serialNo", value: item.serialNo, label: "SerialNo:", inputAlign: "left", labelWidth: 100, readonly: true},
                                {view: "text", id: "label_time", value: item.time, label: "最新时间:", inputAlign: "left", labelWidth: 100, readonly: true},
                                {
                                    rows: [{
                                            cells: [
                                                {view: "textarea", id: "label_content_xml_view", value: Base64.decode(item.content), inputAlign: "left", labelWidth: 100, readonly: false},
                                                {
                                                    id: "label_content_binary_view",
                                                    view: "textarea", value:"", inputAlign: "left", labelWidth: 100, readonly: false
                                                },
                                                {
                                                    id: "label_content_form_view",
                                                    template: "About the app"
                                                }
                                            ]
                                        }]
                                }
                            ]
                        }
                    }).show();
       
                }

                function showXMLForm(){
                    $$("label_content_xml_view").show();
                }

                function showBinaryForm() {
//                    messageNo,serialNo,messageType,fromDB,content,call
                    item = $$("policyWindow").config.mydata;
                    messageNo = item.messageNo;
                    serialNo = item.serialNo;
                    messageType = selectedMessageType;
                    instance = selectedInstanceName;
                    webix.ajax().post("./../system/util/BinaryFormPolicy", {messageNo: messageNo, serialNo: serialNo, messageType: messageType, instance: instance, fromDatabase: false, content:Base64.encode($$("label_content_xml_view").getValue())}, function(data) {
//                        console.log(data);
                        var td = JSON.parse(data);
                        var md = Base64.decode(td.rst);
//                  
//                        $$("label_content_xml_view").show();
                        $$("label_content_binary_view").define("value", md);
                        $$('label_content_binary_view').show();
                    });
                }

                function saveItem() {
                    webix.message("saving");
                    prClientInvoke("updatePolicy", [selectedMessageType, selectedInstanceName, new Number($$("label_messageNo").getValue()), Base64.encode($$("label_content_xml_view").getValue())], function(data) {
                        var ptr = JSON.parse(data);
                        ptr = ptr.rst[0];
                        var id = $$("id_policies").getSelectedId();
                        data = {messageNo: ptr.svnFile.messageNo, serialNo: ptr.id, time: webix.i18n.dateFormatStr(new Date(ptr.createTime)) + " " + webix.i18n.timeFormatStr(new Date(ptr.createTime)), content: Base64.decode(ptr.svnFile.content)};
                        $$("id_policies").updateItem(id, data);
                        $$('policyWindow').close();
                    });
                }
                
                function sendToPolicyCart(){
                    webix.ajax().post("./../logic/dpi/AddToPolicyCart", {messageType: selectedMessageType, instance: selectedInstanceName, messageNo:new Number($$("label_messageNo").getValue()), serialNo:new Number($$("label_serialNo").getValue())}, function(data){
                        
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
