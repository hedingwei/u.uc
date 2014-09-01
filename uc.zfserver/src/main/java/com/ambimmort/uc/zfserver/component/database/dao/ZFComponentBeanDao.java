/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database.dao;

import com.ambimmort.uc.zfserver.bean.ZFComponentBean;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

/**
 *
 * @author 定巍
 */
public class ZFComponentBeanDao {
    
    private static ZFComponentBeanDao instance = null;
    private Dao<ZFComponentBean, String> zfComponentDao;

    private ZFComponentBeanDao() throws SQLException {
        
        zfComponentDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), ZFComponentBean.class);
    }

    public static ZFComponentBeanDao getInstance() throws SQLException {
        if (instance == null) {
            instance = new ZFComponentBeanDao();
        }
        return instance;
    }

    public Dao<ZFComponentBean, String> getZfComponentDao() {
        return zfComponentDao;
    }
    
    
    
}
