/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function init() {
    var tree_data = [
        {id: "status", value: "平台状态", open: true, data: [
                {id: "status.1", value: "Web服务器状态", page: "pages/policyRepoServerSetting.jsp"},
                {id: "status.2", value: "策略服务器状态", page: "pages/policies/publish_standard.jsp"},
                {id: "status.3", value: "Ud-Socket服务器状态", page: "pages/policies/management.jsp"},
                {id: "status.4", value: "Ud-FTP服务器状态", page: "pages/policies/management.jsp"},
                {id: "status.5", value: "数据库服务器状态", page: "pages/policies/publish_standard.jsp"}
            ]},
        {id: "dpi-uc", value: "设备管理", open: true, data: [
                {id: "dpi-uc.2.1", value: "DPI策略设备管理", page: "pages/UInterface/U_DPI/dpi_endpoint.jsp"},
                {id: "dpi-uc.2.2", value: "Ud-Socket服务器管理", page: "pages/UInterface/U_DPI/dpi_endpoint.jsp"},
                {id: "dpi-uc.2.3", value: "Ud-FTP服务器管理", page: "pages/UInterface/U_DPI/dpi_endpoint.jsp"}
            ]},
        {id: "policy", value: "测试工具", open: true, data: [
                {id: "policyRepoServerSetting", value: "策略设置", page: "pages/policyRepoServerSetting.jsp"},
                {id: "policy.2", value: "下发日志", page: "pages/policies/publish_standard.jsp"},
                {id: "policy.1", value: "策略剪贴板", page: "pages/policies/management.jsp"}
            ]},
        {id: "usergroup", value: "用户归属", open: true, data: [
                {id: "ug.1", value: "用户组设置", page: "pages/usergroup/usergroupSetting.jsp"},
                {id: "ug.2", value: "用户归属查询", page: "pages/policies/publish_standard.jsp"},
                {id: "ug.3", value: "AAA信息查询", page: "pages/policies/management.jsp"}
            ]},
        {id: "business", value: "业务设置", open: true, data: [
                {id: "b.1", value: "流量分析结果上报", page: "pages/business/taupload.jsp"},
                {id: "b.2", value: "Web类流量管理", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.3", value: "通用类流量管理", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.4", value: "通用类流量标记", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.5", value: "指定应用的用户统计", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.6", value: "协议特征库", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.7", value: "IP地址库", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.8", value: "Web分类库", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.9", value: "应用名称对应", page: "pages/policyRepoServerSetting.jsp"},
                {id: "b.10", value: "指定应用的用户统计", page: "pages/policyRepoServerSetting.jsp"}
            ]},
        {id: "report", value: "全业务分析", open: true, data: [
                {id: "report.1", open: false, value: "通用类流量分析", data: [
                        {id: "report.1.1", value: "趋势分析"},
                        {id: "report.1.2", value: "占比分析"},
                        {id: "report.1.3", value: "数据详单"}
                    ]},
                {id: "report.2", open: false, value: "Web类流量分析", data: [
                        {id: "report.2.1", value: "趋势分析"},
                        {id: "report.2.2", value: "占比分析"},
                        {id: "report.2.3", value: "数据详单"}
                    ]},
                {id: "report.3", open: false, value: "流量流向分析", data: [
                        {id: "report.3.1", value: "趋势分析"},
                        {id: "report.3.2", value: "占比分析"},
                        {id: "report.3.3", value: "数据详单"}
                    ]},
                {id: "report.4", open: false, value: "DDos分析", data: [
                        {id: "report.4.1", value: "趋势分析"},
                        {id: "report.4.2", value: "占比分析"},
                        {id: "report.4.3", value: "数据详单"}
                    ]},
                {id: "report.5", open: false, value: "CP/SP分析", data: [
                        {id: "report.5.1", value: "趋势分析"},
                        {id: "report.5.2", value: "占比分析"},
                        {id: "report.5.3", value: "数据详单"}
                    ]},
                {id: "report.6", open: false, value: "1-N业务分析", data: [
                        {id: "report.6.1", value: "趋势分析"},
                        {id: "report.6.2", value: "占比分析"},
                        {id: "report.6.3", value: "数据详单"}
                    ]},
                {id: "report.7", open: false, value: "非法路由业务分析", data: [
                        {id: "report.7.1", value: "趋势分析"},
                        {id: "report.7.2", value: "占比分析"},
                        {id: "report.7.3", value: "数据详单"}
                    ]}
            ]}
    ];

    webix.ui({
        rows: [

            {
                view: "accordion",
                cols: [
                    {
                        header: "导航",
                        id: "navpanel",
                        autowidth: true,
                        gravity: 0.25,
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

}


