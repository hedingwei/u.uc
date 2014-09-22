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
        <link rel="stylesheet" href="../../../webix/webix.css" type="text/css" media="screen" charset="utf-8">
        <script src="../../../webix/webix.js" type="text/javascript" charset="utf-8"></script>
        <script src="../../../webix/base64.js" type="text/javascript" charset="utf-8"></script>
        <title>JSP Page</title>
        <script type="text/javascript" charset="utf-8">

            webix.ready(function() {
                var dpiArray = [];
                webix.ajax().post("../../../logic/dpi/list", {}, function(data) {
                    dpiArray = JSON.parse(data);
                    webix.ui({
                        rows: [
                            {
                                view: "accordion",
                                multi: true,
                                height: "100%",
                                width: "100%",
                                rows: [
                                    {
                                        cols: [
                                            {
                                                header: "设备管理",
                                                body: {
                                                    rows: [
                                                        {
                                                            view: "datatable",
                                                            id: "id_dpis",
                                                            select: "row",
                                                            multiselect: false,
                                                            onContext: {},
                                                            columns: [
                                                                {id: "devName", header: [{text: "设备名"}, {content: "selectFilter"}]},
                                                                {id: "probeType", header: [{text: "设备类型"}, {content: "selectFilter"}]},
                                                                {id: "idcHouseId", header: [{text: "区域"}, {content: "selectFilter"}]},
                                                                {id: "deploySiteName", header: [{text: "部署站点"}, {content: "selectFilter"}]},
                                                                {id: "ip", header: [{text: "IP"}, {content: "selectFilter"}]},
                                                                {id: "clientChannelState", header: [{text: "客户端通道"}, {content: "selectFilter"}]},
                                                                {id: "serverChannelState", header: [{text: "服务端通道"}, {content: "selectFilter"}]}

                                                            ],
                                                            data: dpiArray
                                                        }
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }).show();
                    webix.ui({
                        view: "contextmenu",
                        id: "my_menu_device",
                        data: [
                            "添加DPI设备",
                            "删除DPI设备",
                            "更新DPI设备"
                        ],
                        on: {
                            onItemClick: function(id) {
                                var tid = $$('id_dpis').getSelectedId(true, true);
                                console.log("id:" + id);
                                console.log($$('id_dpis').getItem(tid));
                                console.log(tid);
                                if (this.getItem(id).value === "配置策略") {
                                    showConfiguredPolicy($$('id_dpis').getItem(tid));
                                } else if (this.getItem(id).value === "添加DPI设备") {
                                    showAddDPIEndPointWindow();
                                } else if (this.getItem(id).value === "更新DPI设备") {
                                    showEditDPIEndPointWindow($$('id_dpis').getItem(tid));
                                } else if (this.getItem(id).value === "删除DPI设备") {
                                    deleteDPIEndPoint($$('id_dpis').getItem(tid));
                                }

//                                webix.message("List item: <i>" + $$('id_dpis').getItem(tid).devName + "</i> <br/>Context menu item: <i>" + this.getItem(id).value + "</i>");
                            }
                        }
                    });
                    $$('my_menu_device').attachTo($$('id_dpis').$view);
                });

                function showAddDPIEndPointWindow() {
                    webix.ui({
                        view: "window",
                        id: "popup_dpiAdd",
                        height: 480,
                        width: 640,
                        position: "center",
                        move: true,
                        modal: true,
                        head: {
                            view: "toolbar", cols: [
                                {view: "label", id: "label_devName", label: "添加新设备"},
                                {view: "button", label: '添加', width: 60, align: 'right', click: createDPIEndPoint},
                                {view: "button", label: '取消', width: 60, align: 'right', click: "$$('popup_dpiAdd').close();"}
                            ]
                        },
                        body: {
                            view: "form", elements: [
                                {cols: [
                                        {rows: [
                                                {view: "text", id: "id_devName", value: "Device-1", label: "设备名称：", inputAlign: "left", labelWidth: 100},
                                                {view: "text", id: "id_ip", value: "10.8.1.1", label: "IP地址：", inputAlign: "left", labelWidth: 100},
                                                {view: "select", id: "id_probeType", label: "设备类型：", value: 0, options: [
                                                        {id: 0, value: "DPI"}, // the initially selected value
                                                        {id: 1, value: "EU"}
                                                    ], labelAlign: 'left', labelWidth: 100
                                                },
                                                {view: "text", id: "id_deploySiteName", value: "No_HouseID", label: "部署站点：", inputAlign: "left", labelWidth: 100},
                                                {view: "text", id: "id_idcHouseId", value: "海淀", label: "区域：", inputAlign: "left", labelWidth: 100},
                                                {view: "textarea", id: "id_policy", value: "", label: "策略配置：", inputAlign: "left", labelWidth: 100, readonly: false}
                                            ]}
                                    ]}
                            ]
                        }
                    }).show();

                    function createDPIEndPoint() {
                        webix.ajax().post("../../../logic/dpi/AddNewDPIEndPoint", {
                            devName: $$("id_devName").getValue(),
                            ip: $$("id_ip").getValue(),
                            idcHouseId: $$("id_idcHouseId").getValue(),
                            deploySiteName: $$("id_deploySiteName").getValue(),
                            probeType: $$("id_probeType").getValue(),
                            policy: Base64.encode($$("id_policy").getValue())
                        }, function(data) {
                            webix.message(data);
                        });
                    }
                }

                function showEditDPIEndPointWindow(item) {
                    webix.ajax().post("../../../logic/dpi/getDPIConfiguredPolicyRepos", {devName: item.devName}, function(data) {
                        webix.ui({
                            view: "window",
                            id: "popup_dpiEdit",
                            height: 480,
                            width: 640,
                            position: "center",
                            move: true,
                            modal: true,
                            head: {
                                view: "toolbar", cols: [
                                    {view: "label", id: "label_devName", label: item.devName},
                                    {view: "button", label: '保存', width: 60, align: 'right', click: updateDPIEndPoint},
                                    {view: "button", label: '取消', width: 60, align: 'right', click: "$$('popup_dpiEdit').close();"}
                                ]
                            },
                            body: {
                                view: "form", elements: [
                                    {cols: [
                                            {rows: [
                                                    {view: "text", id: "id_e_devName", value: item.devName, label: "设备名称：", inputAlign: "left", labelWidth: 100},
                                                    {view: "text", id: "id_e_ip", value: item.ip, label: "IP地址：", inputAlign: "left", labelWidth: 100},
                                                    {view: "select", id: "id_e_probeType", label: "设备类型：", value: item.probeType, options: [
                                                            {id: 0, value: "DPI"}, // the initially selected value
                                                            {id: 1, value: "EU"}
                                                        ], labelAlign: 'left', labelWidth: 100
                                                    },
                                                    {view: "text", id: "id_e_deploySiteName", value: item.deploySiteName, label: "部署站点：", inputAlign: "left", labelWidth: 100},
                                                    {view: "text", id: "id_e_idcHouseId", value: item.idcHouseId, label: "区域：", inputAlign: "left", labelWidth: 100},
                                                    {view: "textarea", id: "id_e_policy", value: data, label: "策略配置：", inputAlign: "left", labelWidth: 100, readonly: false}
                                                ]}
                                        ]}
                                ]
                            }
                        }).show();
                    });


                }

                function updateDPIEndPoint() {
                    webix.ajax().post("../../../logic/dpi/UpdateDPIEndPoint", {
                        devName: $$("id_e_devName").getValue(),
                        ip: $$("id_e_ip").getValue(),
                        idcHouseId: $$("id_e_idcHouseId").getValue(),
                        deploySiteName: $$("id_e_deploySiteName").getValue(),
                        probeType: $$("id_e_probeType").getValue(),
                        policy: Base64.encode($$("id_e_policy").getValue())
                    }, function(data) {
                        webix.message(data);
                    });
                }
                
                function deleteDPIEndPoint(item) {
                   webix.message("删除设备："+item.devName);
                    webix.ajax().post("../../../logic/dpi/DeleteDPIEndPoint", {
                        devName: item.devName
                    }, function(data) {
                        webix.message(data);
                    });
                }

                function showConfiguredPolicy(item) {
                    webix.ajax().post("../../../logic/dpi/getDPIConfiguredPolicyRepos", {devName: item.devName}, function(data) {
                        webix.ui({
                            view: "window",
                            id: "popup_policyConfig",
                            height: 480,
                            width: 640,
                            position: "center",
                            move: true,
                            modal: true,
                            mydata: item,
                            head: {
                                view: "toolbar", cols: [
                                    {view: "label", id: "label_devName", label: "设备:" + item.devName, devName: item.devName},
                                    {view: "button", label: '保存', width: 100, align: 'right', click: saveItem},
                                    {view: "button", label: '关闭', width: 100, align: 'right', click: "$$('popup_policyConfig').close();"}
                                ]
                            },
                            body: {
                                rows: [
                                    {view: "textarea", id: "label_content_xml_view", value: data, inputAlign: "left", labelWidth: 100, readonly: false}
                                ]
                            }
                        }).show();
                    });
                }

                function saveItem() {
                    webix.ajax().post("../../../logic/dpi/saveDPIConfiguredPolicyRepos", {devName: $$("label_devName").config.devName, config: Base64.encode($$("label_content_xml_view").getValue())}, function(data) {
                        console.log($$("label_devName").devName);
                        console.log($$("label_devName").config);
                        console.log(data);
                        $$('popup_policyConfig').close();
                    });
                }


            });
        </script>   
    </head>
    <body>
    </body>
</html>
