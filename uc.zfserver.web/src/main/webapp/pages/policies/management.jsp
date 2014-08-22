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
            <%
                List<PolicyRepositoryBean> beans = PolicyRepositoryUtil.getInstance().listAllRepositories();
                JSONArray array = new JSONArray();
                int i = 1;
                for (PolicyRepositoryBean bean : beans) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", "1." + i);
                    obj.put("value", bean.getName());
                    obj.put("pid", bean.getId());
                    array.add(obj);
                }
                String dataStr = array.toString();
                System.out.println(dataStr);
            %>
                var p_r = {id: "root", value: "", open: true, data: JSON.parse('<%=dataStr%>')};
                webix.ui({
                    rows: [
                        {
                            view: "accordion",
                            multi: true,
                            cols: [
                                {
                                    header: "策略仓库",
                                    autowidth: true,
                                    gravity: 0.2,
                                    body: {view: "grouplist", id: "repocitory", data: p_r, select: true}
                                },
                                {
                                    header: "策略导航",
                                    autowidth: true,
                                    gravity: 0.3,
                                    body: {view: "grouplist", id: "navigator", data: p_n, select: true}
                                },
                                {view: "resizer"},
                                {
                                    body: {
                                        rows: [
                                            {
                                                view: "toolbar",
                                                id: "myToolbar",
                                                cols: [
                                                    {view: "button", id: "createBtn", value: "创建", width: 100, align: "left"}
                                                ]
                                            },
                                            {
                                                view: "datatable",
                                                id: "datatable1",
                                                select: "row",
                                                multiselect: false,
                                                columns: [
                                                    {id: "messageNo", header: "MessageNo", width: 100},
                                                    {id: "serialNo", header: "SerialNo", width: 80},
                                                    {id: "lastUpdateTime", header: "最新修改时间", width: 180}
                                                ],
                                                data: []
                                            },
                                            {
                                                view: 'pager',
                                                id: "pager1",
                                                template: '{common.first()} {common.prev()} {common.pages()} {common.next()} {common.last()} ',
                                                master: false,
                                                size: 15,
                                                count: 1,
                                                on: {
                                                    onItemClick: function(id, e, node) {
                                                        pageselect(15, $$('pager1').config.page)

                                                    }
                                                }
                                            }
                                        ]
                                    }
                                }
                            ]
                        }
                    ]
                }).show();
                $$('navigator').attachEvent('onSelectChange', function(id) {

                    if (undefined === $$('repocitory').getSelectedItem()) {
                        webix.message({type: "error", text: "操作有误：请选择一个策略仓库"});
                    } else {

                        prClientInvoke("getHeadPoliciesCount", [$$('navigator').getItem(id).pname, $$('repocitory').getSelectedItem().pid], function(data) {

                            ret = JSON.parse(data);
                            $$("pager1").config.count = ret.rst[0];
                            webix.message("查询到:" + ret.rst[0] + "条记录");
                        });

                        prClientInvoke("getHeadPoliciesWithLog", [$$('navigator').getItem(id).pname, $$('repocitory').getSelectedItem().pid, 15, 1], function(data) {
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
//                            
                        });
                    }
                });

                $$("datatable1").attachEvent("onItemDblClick", function(id, e, node) {
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
                        mydata:item,
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
                
                function showBinaryForm(){
//                    messageNo,serialNo,messageType,fromDB,content,call
                    item = $$("policyWindow").config.mydata;
                    messageNo = item.messageNo;
                    serialNo = item.serialNo;
                    messageType = $$('navigator').getSelectedItem().pname;
                    instance = $$('repocitory').getSelectedItem().pid;
                    webix.message("messageType:"+messageType);
                    webix.message("instance:"+instance);
                    webix.message("serialNo:"+serialNo);
                    webix.message("messageNo:"+messageNo);
                   
                    webix.ajax().post("./../../system/util/BinaryFormPolicy", {messageNo:messageNo,serialNo:serialNo,messageType:messageType,instance:instance,fromDatabase:false,content:Base64.encode($$("label_content_xml_view").getValue())}, function(data){
                        var td = JSON.parse(data);
                        var md = Base64.decode(td.rst);
                        $$("label_content_binary_view").define("value",md);
                        $$('label_content_binary_view').show();
                    });
                }

            });
        </script>   
    </head>
    <body>
    </body>
</html>
