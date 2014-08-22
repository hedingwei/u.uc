<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="webix/webix.css" type="text/css" media="screen" charset="utf-8">
        <script src="webix/webix.js" type="text/javascript" charset="utf-8"></script>

        <title>省级DPI汇聚平台</title>
        <script type="text/javascript" charset="utf-8">

            webix.ready(function() {

                var tree_data = [
                    {id: "dpi-uc", value: "系统设置", open: true, data: [
                            {id: "policyRepoServerSetting", value: "策略仓库设置", page: "pages/policyRepoServerSetting.jsp"},
                            {id: "dpi-uc.1.3", value: "数据接收设置", page: "pages/UInterface/U_ZF/setting.jsp"},
                            {id: "dpi-uc.2.1", value: "DPI设备管理", page: "pages/UInterface/U_DPI/dpi_endpoint.jsp"},
                            {id: "dpi-uc.1.2", value: "DPI-UD白名单", page: "pages/UInterface/U_ZF/ud_whitelist.jsp"}
                        ]},
                    {id: "policy", value: "策略", open: true, data: [
                            {id: "policy.1", value: "策略管理", page: "pages/policies/management.jsp"},
                            {id: "policy.2", value: "策略下发详情", page: "pages/policies/publish_standard.jsp"},
                            {id: "policy.3", value: "手动下发", page: "pages/policies/publish_manu.jsp"},
                            {id: "policy.4", value: "DPI策略同步详情", page: "pages/policies/publish_dpi.jsp"}
                        ]},
                    {id: "report", value: "报表", open: true, data: [
                            {id: "report.1", open: false, value: "流量分析", data: [
                                    {id: "report.1.1", value: "UC节点设置"},
                                    {id: "report.1.2", value: "UD节点设置"}
                                ]},
                            {id: "report.2", open: false, value: "DPI-U接口", data: [
                                    {id: "report.2.1", value: "UC对接点"}
                                ]}
                        ]}
                ];
                var grid_data = [
                    {id: 1, title: "The Shawshank Redemption", year: 1994, votes: 678790, rating: 9.2, rank: 1},
                    {id: 2, title: "The Godfather", year: 1972, votes: 511495, rating: 9.2, rank: 2},
                    {id: 3, title: "The Godfather: Part II", year: 1974, votes: 319352, rating: 9.0, rank: 3},
                    {id: 4, title: "The Good, the Bad and the Ugly", year: 1966, votes: 213030, rating: 8.9, rank: 4},
                    {id: 5, title: "My Fair Lady", year: 1964, votes: 533848, rating: 8.9, rank: 5},
                    {id: 6, title: "12 Angry Men", year: 1957, votes: 164558, rating: 8.9, rank: 6}
                ];
                webix.ui({
                    rows: [
                        {view: "template", //optional
                            template: "综分系统",
                            type: "header"
                        },
                        {
                            view: "accordion",
                            cols: [
                                {
                                    header: "导航",
                                    id: "navpanel",
                                    autowidth: true,
                                    gravity: 0.2,
                                    body: {view: "tree", id: "navigator", data: tree_data, select: true}
                                },
                                {view: "resizer"},
                                {
                                    body: {view: "iframe", id: "frame-body", src: "pages/welcome.jsp"}
                                }
                            ]
                        }
                    ]
                }).show();

                $$('navigator').attachEvent('onSelectChange', function(id) {
                    var page = $$('navigator').getItem(id).page;
                    if (undefined === page) {

                    } else {
//                        webix.message($$('navigator').config.collapsed);
//                        $$('navpanel').define("collapsed",true);
                        $$('frame-body').load(page);
                    }

                });

            });
        </script>            
    </head>
    <body>

    </body>
</html>
