/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.ucdata.UcData_HeartBeatBean;
import com.ambimmort.uc.zfserver.bean.DPIConfiguredPolicyRepositoryBean;
import com.ambimmort.uc.zfserver.bean.RepositoryEntryBean;
import com.ambimmort.uc.zfserver.bean.SequenceNoPtrBean;
import com.ambimmort.uc.zfserver.bean.UcMessageSendingLogBean;
import com.ambimmort.uc.zfserver.bean.ucdata.UcData_DPIReportedPolicyVersionBean;
import com.ambimmort.uc.zfserver.bean.ZFAgentBean;
import com.ambimmort.uc.zfserver.bean.ZFComponentBean;
import com.ambimmort.uc.zfserver.bean.ZFPropertyBean;
import com.ambimmort.uc.zfserver.bean.ZFTask_PolicySynchronization;
import com.ambimmort.uc.zfserver.component.ZFComponent;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import net.sf.json.JSONObject;

/**
 *
 * @author 定巍
 */
public class DBComponent extends ZFComponent {

    private static DBComponent instance = null;
    private JdbcPooledConnectionSource connectionSource = null;
    private final JSONObject state = new JSONObject();

    private DBComponent() throws SQLException {
        connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/zfdb?zeroDateTimeBehavior=convertToNull", "root", "123456");
//        connectionSource = new JdbcPooledConnectionSource("jdbc:h2:tcp://localhost/./zfserver","ambimmort","ambi12341234");
        connectionSource.setMaxConnectionAgeMillis(5 * 60 * 1000);
        // change the check-every milliseconds from 30 seconds to 60
        connectionSource.setCheckConnectionsEveryMillis(60 * 1000);
        connectionSource.setTestBeforeGet(true);
    }

    public static DBComponent getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBComponent();
        }
        return instance;
    }

    public JdbcPooledConnectionSource getConnectionSource() {
        return connectionSource;
    }

    @Override
    public String getName() {
        return "DBComponent";
    }

    @Override
    protected void refreshState() {
        try {
            state.put("db.openCount", connectionSource.getOpenCount());
            state.put("db.closeCount", connectionSource.getCloseCount());
            state.put("db.currentConnectionsManaged", connectionSource.getCurrentConnectionsManaged());
            state.put("db.currentConnectionsFree", connectionSource.getCurrentConnectionsFree());
            state.put("db.maxConnectionsEverUsed", connectionSource.getMaxConnectionsEverUsed());
            state.put("db.releaseCount", connectionSource.getReleaseCount());
            state.put("db.isOpen", connectionSource.isOpen());
            ZFComponentBean bean = new ZFComponentBean();
            bean.setName(getName());
            bean.setStates(state.toString(4));
            MyDaoManager.getInstance().getDao(ZFComponentBean.class).createOrUpdate(bean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initTable(Class aClass) throws SQLException {
        TableUtils.createTableIfNotExists(DBComponent.getInstance().getConnectionSource(), aClass);
    }

    @Override
    protected void prestart() throws Throwable {
            initTable(DPIConfiguredPolicyRepositoryBean.class);
            initTable(DPIEndPointBean.class);
            initTable(RepositoryEntryBean.class);
            initTable(ZFAgentBean.class);
            initTable(ZFComponentBean.class);
            initTable(ZFPropertyBean.class);
            initTable(ZFTask_PolicySynchronization.class);

            initTable(UcData_DPIReportedPolicyVersionBean.class);
            initTable(UcData_HeartBeatBean.class);

            initTable(UcMessageSendingLogBean.class);
            initTable(SequenceNoPtrBean.class);
    }

    @Override
    public JSONObject getStates() {
        return state;
    }

}
