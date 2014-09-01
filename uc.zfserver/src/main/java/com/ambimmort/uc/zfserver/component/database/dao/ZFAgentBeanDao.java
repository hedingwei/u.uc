/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database.dao;

import com.ambimmort.uc.zfserver.bean.ZFAgentBean;
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
public class ZFAgentBeanDao {

    private static ZFAgentBeanDao instance = null;
    private Dao<ZFAgentBean, String> zfAgentBeanDao;

    private ZFAgentBeanDao() throws SQLException {
        zfAgentBeanDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), ZFAgentBean.class);
        init();
    }
    public static ZFAgentBeanDao getInstance() throws SQLException {
        if (instance == null) {
            instance = new ZFAgentBeanDao();
        }
        return instance;
    }

    private void init() throws SQLException {
        ZFAgentBean bean = new ZFAgentBean();
        bean.setName("commander@ambimmort.com");
        bean.setConfiguration("");
        bean.setComment("commander");
        bean.setPresence("");
        zfAgentBeanDao.createIfNotExists(bean);
    }

    public Dao<ZFAgentBean, String> getZfAgentBeanDao() {
        return zfAgentBeanDao;
    }
    
    

}
