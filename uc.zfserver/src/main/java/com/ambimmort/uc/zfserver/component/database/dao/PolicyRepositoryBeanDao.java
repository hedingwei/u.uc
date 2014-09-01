/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database.dao;

import com.ambimmort.uc.zfserver.bean.DPIConfiguredPolicyRepositoryBean;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;

/**
 *
 * @author 定巍
 */
public class PolicyRepositoryBeanDao {
    
    private static PolicyRepositoryBeanDao instance = null;
    private Dao<DPIConfiguredPolicyRepositoryBean, String> policyRepositoryDao;

    private PolicyRepositoryBeanDao() throws SQLException {
        
        policyRepositoryDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), DPIConfiguredPolicyRepositoryBean.class);
    }

    public static PolicyRepositoryBeanDao getInstance() throws SQLException {
        if (instance == null) {
            instance = new PolicyRepositoryBeanDao();
        }
        return instance;
    }

    public Dao<DPIConfiguredPolicyRepositoryBean, String> getPolicyRepositoryDao() {
        return policyRepositoryDao;
    }
    
    
    
    
}
