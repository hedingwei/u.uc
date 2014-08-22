/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.db.util;

import com.ambimmort.uc.zfserver.db.entities.Property;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

/**
 *
 * @author 定巍
 */
public class PropertyUtil {
    
    private static PropertyUtil instance = null;
    private Dao<Property, String> propertyDao;

    private PropertyUtil() throws SQLException {
        TableUtils.createTableIfNotExists(DB.getInstance().getConnectionSource(), Property.class);
        propertyDao = DaoManager.createDao(DB.getInstance().getConnectionSource(), Property.class);
    }

    public static PropertyUtil getInstance() throws SQLException {
        if (instance == null) {
            instance = new PropertyUtil();
        }
        return instance;
    }
    
    public boolean hasProperty(String key) throws SQLException{
        return propertyDao.queryForId(key)!=null;
    }

    public void setProperty(String key, String value) throws SQLException {
        Property p = new Property();
        p.setKey(key);
        p.setValue(value);
        propertyDao.createOrUpdate(p);
    }
    
    public void newProperty(String key, String value) throws SQLException {
        Property p = new Property();
        p.setKey(key);
        p.setValue(value);
        propertyDao.createIfNotExists(p);
    }
    

    public String getProperty(String key) throws SQLException {
        Property p = propertyDao.queryForId(key);
        if (p == null) {
            return "";
        }else{
            return p.getValue();
        }
    }
}
