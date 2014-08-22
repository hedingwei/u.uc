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
        <title>JSP Page</title>
        <script type="text/javascript" charset="utf-8">

            webix.ready(function() {

                webix.ui({
                    view: "contextmenu",
                    id: "my_menu",
                    data: [
                        {
                            value: "Translate...",
                            submenu: ["English", "Slavic...", "German"]},
                        {},
                        {}
                    ]
                });

                webix.ui({
                    rows: [
                        {
                            view: "accordion",
                            multi: true,
                            height: "100%",
                            width:"100%",
                            cols: [
                                {
                                    header: "设备管理",
                                    body: {
                                        rows: [
                                            {
                                                view: "datatable",
                                                id: "datatable1",
                                                select: "row",
                                                multiselect: false,
                                                onContext: {},
                                                columns: [
                                                    { id:"deviceName", header:[{ text:"设备名"}, { content:"selectFilter" }]},
                                                    {id: "deviceType", header:[{ text:"设备类型"}, { content:"selectFilter" }]},
                                                    {id: "group", header:[{ text:"区域"}, { content:"selectFilter" }]},
                                                    {id: "deploySite", header:[{ text:"部署站点"}, { content:"selectFilter" }]},
                                                    {id: "mip", header: "主IP"},
                                                    {id: "sip", header: "备IP"},
                                                    {id: "aip", header: "活动"},
                                                    {id: "doc", header:[{ text:"D->通道"}, { content:"selectFilter" }]},
                                                    {id: "dic", header:[{ text:"D<-通道"}, { content:"selectFilter" }]},
                                                    {id: "pr", header:[{ text:"策略仓库"}, { content:"selectFilter" }]}
                                                ],
                                                data: [
                                                    {deviceName: "DPI1", deviceType: "EU",group:"绿网DPI分区-1", deploySite: "dfsdf", mip: "192.168.1.1",sip:"192.168.1.2",aip:"192.168.1.1",doc:"y",dic:"y",pr:"prr"}
                                                ]
                                            }
                                        ]
                                    }
                                },
                                {view: "resizer"},
                                {
                                    header: "设备详情",
                                    collapsed:true,
                                    body: {
                                        view:"accordion",
                                        multi:"mixde",
                                        rows:[
                                            {
                                                header:"基本信息",
                                                body:{}
                                            },
                                            {
                                                header:"主节点",
                                                body:{}
                                            },
                                            {
                                                header:"备节点",
                                                body:{}
                                            }
                                        ]
                                    }
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
                        "编辑DPI设备",
                        "查看DPI设备"
                    ],
                    on: {
                        onItemClick: function(id) {
                            var tid = $$('datatable1').getSelectedId(true, true);
                            console.log("id:" + id);
                            console.log($$('datatable1').getItem(tid));
                            console.log(tid);
                            webix.message("List item: <i>" + $$('datatable1').getItem(tid).deviceName + "</i> <br/>Context menu item: <i>" + this.getItem(id).value + "</i>");
                        }
                    }
                });
                $$('my_menu_device').attachTo($$('datatable1').$view);

            });
        </script>   
    </head>
    <body>
    </body>
</html>
