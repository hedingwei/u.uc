/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.database.dao;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.RepositoryEntryBean;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

/**
 *
 * @author 定巍
 */
public class DPIEndPointBeanDao {
    
    private static DPIEndPointBeanDao instance = null;
    private Dao<DPIEndPointBean, String> dpiEndPointDao;
    private Dao<RepositoryEntryBean,String> repositoryEntryDao;

    private DPIEndPointBeanDao() throws SQLException {
        
        dpiEndPointDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), DPIEndPointBean.class);
        repositoryEntryDao = DaoManager.createDao(DBComponent.getInstance().getConnectionSource(), RepositoryEntryBean.class);
        
    }

    public static DPIEndPointBeanDao getInstance() throws SQLException {
        if (instance == null) {
            instance = new DPIEndPointBeanDao();
        }
        return instance;
    }

    public Dao<DPIEndPointBean, String> getDpiEndPointDao() {
        return dpiEndPointDao;
    }

    public Dao<RepositoryEntryBean, String> getRepositoryEntryDao() {
        return repositoryEntryDao;
    }
    

    public void setDpiEndPointDao(Dao<DPIEndPointBean, String> dpiEndPointDao) {
        this.dpiEndPointDao = dpiEndPointDao;
    }

    public void setRepositoryEntryDao(Dao<RepositoryEntryBean, String> repositoryEntryDao) {
        this.repositoryEntryDao = repositoryEntryDao;
    }

    
    

}
