/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.db.util;

import com.ambimmort.uc.zfserver.db.entities.PolicyRepositoryBean;
import com.ambimmort.uc.zfserver.db.entities.Property;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 定巍
 */
public class PolicyRepositoryUtil {
    
    private static PolicyRepositoryUtil instance = null;
    private Dao<PolicyRepositoryBean, String> propertyDao;

    private PolicyRepositoryUtil() throws SQLException {
        TableUtils.createTableIfNotExists(DB.getInstance().getConnectionSource(), PolicyRepositoryBean.class);
        propertyDao = DaoManager.createDao(DB.getInstance().getConnectionSource(), PolicyRepositoryBean.class);
    }

    public static PolicyRepositoryUtil getInstance() throws SQLException {
        if (instance == null) {
            instance = new PolicyRepositoryUtil();
        }
        return instance;
    }

    public Dao<PolicyRepositoryBean, String> getPropertyDao() {
        return propertyDao;
    }
    
    public List<PolicyRepositoryBean> listAllRepositories() throws SQLException{
        return propertyDao.queryForAll();
    }
    
}
