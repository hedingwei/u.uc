/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 定巍
 */
public class MyDaoManager {

    private static MyDaoManager instance = null;
    
    private final Map<Class, Dao> daos = new HashMap<Class, Dao>();

    private MyDaoManager() {
    }

    public static MyDaoManager getInstance() {
        if (instance == null) {
            instance = new MyDaoManager();
        }
        return instance;
    }

    public synchronized <D extends Dao<T, ?>, T extends Object> D getDao(Class<T> clazz) throws SQLException {
        if(daos.containsKey(clazz)){
            return (D) daos.get(clazz);
        }else{
            Dao d =  DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), clazz);
            daos.put(clazz, d);
            return (D) d;
        }
    }
}
