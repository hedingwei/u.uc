/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database.dao;

import com.ambimmort.uc.zfserver.bean.DPIConfiguredPolicyRepositoryBean;
import com.ambimmort.uc.zfserver.bean.ucdata.UcData_DPIReportedPolicyVersionBean;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;

/**
 *
 * @author 定巍
 */
public class RuntimeDPIPolicyVersionBeanDao {
    
    private static RuntimeDPIPolicyVersionBeanDao instance = null;
    private Dao<UcData_DPIReportedPolicyVersionBean, String> runtimeDPIPolicyVersionBeanDao;

    private RuntimeDPIPolicyVersionBeanDao() throws SQLException {
        
        runtimeDPIPolicyVersionBeanDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), UcData_DPIReportedPolicyVersionBean.class);
    }

    public static RuntimeDPIPolicyVersionBeanDao getInstance() throws SQLException {
        if (instance == null) {
            instance = new RuntimeDPIPolicyVersionBeanDao();
        }
        return instance;
    }

    public Dao<UcData_DPIReportedPolicyVersionBean, String> getPolicyRepositoryDao() {
        return runtimeDPIPolicyVersionBeanDao;
    }
    
    
    
    
}
