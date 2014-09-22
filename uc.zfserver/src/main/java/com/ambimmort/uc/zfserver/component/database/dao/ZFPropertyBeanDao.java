/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database.dao;

import com.ambimmort.uc.zfserver.bean.ZFPropertyBean;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

/**
 *
 * @author 定巍
 */
public class ZFPropertyBeanDao {

    private static ZFPropertyBeanDao instance = null;
    private Dao<ZFPropertyBean, String> propertyBeanDao;

    private ZFPropertyBeanDao() throws SQLException {
        
        propertyBeanDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), ZFPropertyBean.class);
        init();
    }

    private void init() throws SQLException {
        newProperty("zfserver.port", 50000 + "");
        newProperty("zfserver.throttle", 1000 + "");
        newProperty("PolicyRepoServer.webserver.url", "http://localhost:9004");
    }

    public static ZFPropertyBeanDao getInstance() throws SQLException {
        if (instance == null) {
            instance = new ZFPropertyBeanDao();
        }
        return instance;
    }

    public boolean hasProperty(String key) throws SQLException {
        return propertyBeanDao.queryForId(key) != null;
    }

    public void setProperty(String key, String value) throws SQLException {
        ZFPropertyBean p = new ZFPropertyBean();
        p.setKey(key);
        p.setValue(value);
        propertyBeanDao.createOrUpdate(p);
    }

    public void newProperty(String key, String value) throws SQLException {
        ZFPropertyBean p = new ZFPropertyBean();
        p.setKey(key);
        p.setValue(value);
        propertyBeanDao.createIfNotExists(p);
    }

    public String getProperty(String key) throws SQLException {
        ZFPropertyBean p = propertyBeanDao.queryForId(key);
        if (p == null) {
            return "";
        } else {
            return p.getValue();
        }
    }
}
