/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database.dao;

import com.ambimmort.uc.zfserver.bean.ucdata.UcData_HeartBeatBean;
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
public class HeartBeatBeanDao {

    private static HeartBeatBeanDao instance = null;
    private Dao<UcData_HeartBeatBean, String> heartBeatBeanDao;

    private HeartBeatBeanDao() throws SQLException {
        heartBeatBeanDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), UcData_HeartBeatBean.class);
        init();
    }

    private void init() throws SQLException {

    }

    public static HeartBeatBeanDao getInstance() throws SQLException {
        if (instance == null) {
            instance = new HeartBeatBeanDao();
        }
        return instance;
    }

    public Dao<UcData_HeartBeatBean, String> getHeartBeatBeanDao() {
        return heartBeatBeanDao;
    }

}
