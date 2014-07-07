/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.RepositoryEntryBean;
import com.j256.ormlite.dao.CloseableIterable;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class DPIEndPointManager {

    private static DPIEndPointManager instance = null;

    private static String dbDir = "C:\\Users\\定巍\\Downloads\\dbtest";
    private JdbcPooledConnectionSource connectionSource = null;

    private Dao<RepositoryEntryBean, Long> repoDao = null;
    private Dao<DPIEndPointBean, String> endPointDao = null;

    private DPIEndPointManager() {

    }

    public Dao<RepositoryEntryBean, Long> getRepoDao() {
        return repoDao;
    }

    public void setRepoDao(Dao<RepositoryEntryBean, Long> repoDao) {
        this.repoDao = repoDao;
    }

    public Dao<DPIEndPointBean, String> getEndPointDao() {
        return endPointDao;
    }

    public void setEndPointDao(Dao<DPIEndPointBean, String> endPointDao) {
        this.endPointDao = endPointDao;
    }

    
    
    public void initDB(boolean initialize) {
        try {
            new File(dbDir).mkdirs();
            connectionSource = new JdbcPooledConnectionSource("jdbc:h2:" + dbDir+"/db");
            connectionSource.setMaxConnectionAgeMillis(5 * 60 * 1000);
            connectionSource.setCheckConnectionsEveryMillis(60 * 1000);
            connectionSource.setTestBeforeGet(true);
            initDPIEndPointBean(initialize);
            initRepositoryEntryBean(initialize);
        } catch (SQLException ex) {

        }
    }

    private void initRepositoryEntryBean(boolean initialize) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, RepositoryEntryBean.class);
            repoDao = DaoManager.createDao(connectionSource, RepositoryEntryBean.class);
            if (initialize) {
                repoDao.deleteBuilder().delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }

    private void initDPIEndPointBean(boolean initialize) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, DPIEndPointBean.class);
            endPointDao = DaoManager.createDao(connectionSource, DPIEndPointBean.class);
            if (initialize) {
                endPointDao.deleteBuilder().delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }

    public static DPIEndPointManager getInstance() {
        if (instance == null) {
            instance = new DPIEndPointManager();
        }
        return instance;
    }

    private Map<String, DPIEndPoint> serverDPIEndPoints = new HashMap<String, DPIEndPoint>();
    private Map<String, DPIEndPoint> clientDPIEndPoints = new HashMap<String, DPIEndPoint>();

    public DPIEndPointBean createDPIEndPointBean(String name, String clientChannelIp, String serverChannelIp, int serverChannelPort){
        DPIEndPointBean bean = new DPIEndPointBean();
        bean.setName(name);
        bean.setClientChannelIP(clientChannelIp);
        bean.setServerChannelIP(serverChannelIp);
        bean.setServerChannelPort(serverChannelPort);
        bean.setIsOn(false);
        try {
            endPointDao.create(bean);
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return bean;
    }
    
    
    
    public RepositoryEntryBean createRepositoryEntryBean(DPIEndPointBean endPointBean,String type, String name){
        RepositoryEntryBean bean = new RepositoryEntryBean();
        bean.setEndPoint(endPointBean);
        bean.setName(name);
        bean.setType(type);
        try {
            repoDao.create(bean);
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }
    
    public void updateDPIEndPointBean(DPIEndPointBean bean){
        try {
            endPointDao.update(bean);
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void dumpDPIEndPointBean(){
        try {
            for(DPIEndPointBean b:endPointDao.queryForAll()){
                System.out.println(b);
                CloseableIterator<RepositoryEntryBean> bs = b.getRepositories().closeableIterator();
                while(bs.hasNext()){
                    System.out.println("\t"+bs.next());
                }
                bs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
//        DPIEndPointManager.getInstance().initDB(true);
//        DPIEndPoint bean = DPIEndPointManager.getInstance().createDPIEndPoint("GN-A", "192.168.1.1", "192.168.1.1", 50000);
//        RepositoryEntryBean rb = DPIEndPointManager.getInstance().createRepositoryEntryBean(bean.getDpiEndPointBean(), "type1", "instance1");
//        RepositoryEntryBean rb1 = DPIEndPointManager.getInstance().createRepositoryEntryBean(bean.getDpiEndPointBean(), "type1", "instance2");
//        RepositoryEntryBean rb2 = DPIEndPointManager.getInstance().createRepositoryEntryBean(bean.getDpiEndPointBean(), "type2", "instance1");
//        DPIEndPointManager.getInstance().dumpDPIEndPointBean();
    }
    
}
