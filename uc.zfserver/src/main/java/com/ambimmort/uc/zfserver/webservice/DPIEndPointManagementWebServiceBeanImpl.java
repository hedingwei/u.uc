/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.webservice;

import com.ambimmort.uc.zfserver.DPIEndPointManager;
import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.RepositoryEntryBean;
import com.ambimmort.uc.zfserver.bean.webservice.DPIEndPointManagementWebServiceBean;
import com.j256.ormlite.dao.CloseableIterator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;

/**
 *
 * @author 定巍
 */
@WebService
public class DPIEndPointManagementWebServiceBeanImpl implements DPIEndPointManagementWebServiceBean {

    @Override
    public DPIEndPointBean createDPIEndPoint(String name, String clientChannelIp, String serverChannelIp, int serverChannelPort) {
        try {
            DPIEndPointBean bean = DPIEndPointManager.getInstance().getEndPointDao().queryForId(name);
            if (bean != null) {
                return bean;
            } else {
                bean = DPIEndPointManager.getInstance().createDPIEndPointBean(name, clientChannelIp, serverChannelIp, serverChannelPort);
                return bean;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public DPIEndPointBean updateDPIEndPoint(String name, String clientChannelIp, String serverChannelIp, int serverChannelPort) {
        try {
            DPIEndPointBean bean = DPIEndPointManager.getInstance().getEndPointDao().queryForId(name);
            if (bean == null) {
                return null;
            } else {
                bean.setClientChannelIP(clientChannelIp);
                bean.setServerChannelIP(serverChannelIp);
                bean.setServerChannelPort(serverChannelPort);
                DPIEndPointManager.getInstance().getEndPointDao().update(bean);
                return bean;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public DPIEndPointBean updateDPIEndPointName(String name, String newName) {
        try {
            DPIEndPointBean bean = DPIEndPointManager.getInstance().getEndPointDao().queryForId(name);
            if (bean == null) {
                return null;
            } else {
                bean.setName(name);
                DPIEndPointManager.getInstance().getEndPointDao().update(bean);
                return bean;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public DPIEndPointBean deleteDPIEndPoint(String name) {
        try {
            DPIEndPointBean bean = DPIEndPointManager.getInstance().getEndPointDao().queryForId(name);
            if (bean == null) {
                return null;
            } else {
                DPIEndPointManager.getInstance().getEndPointDao().delete(bean);
                return bean;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public DPIEndPointBean turnOnDPIEndPoint(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DPIEndPointBean turnOffDPIEndPoint(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DPIEndPointBean> getAllDPIEndPoints() {
        try {
            return DPIEndPointManager.getInstance().getEndPointDao().queryForAll();
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<RepositoryEntryBean> getAllRepositoryEntryBeans(String endPointName) {
        try {
            DPIEndPointBean bean = DPIEndPointManager.getInstance().getEndPointDao().queryForId(endPointName);
            if (bean == null) {
                return null;
            } else {
                List<RepositoryEntryBean> list = new ArrayList<RepositoryEntryBean>();
                CloseableIterator<RepositoryEntryBean> it = bean.getRepositories().closeableIterator();
                while (it.hasNext()) {
                    list.add(it.next());
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public DPIEndPointBean getDPIEndPoint(String endPointName) {
        try {
            DPIEndPointBean bean = DPIEndPointManager.getInstance().getEndPointDao().queryForId(endPointName);
            return bean;
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public RepositoryEntryBean addRepositoryEntryBean(String endPointName, String type, String name) {
        try {
            DPIEndPointBean bean = DPIEndPointManager.getInstance().getEndPointDao().queryForId(endPointName);
            if (bean == null) {
                return null;
            } else {
                RepositoryEntryBean eb = new RepositoryEntryBean();
                eb.setEndPoint(bean);
                eb.setType(type);
                eb.setName(name);
                DPIEndPointManager.getInstance().getRepoDao().create(eb);
                return eb;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public RepositoryEntryBean removeRepositoryEntryBean(long repositoryEntryBeanId) {
        try {
            RepositoryEntryBean bean = DPIEndPointManager.getInstance().getRepoDao().queryForId(repositoryEntryBeanId);
            DPIEndPointManager.getInstance().getRepoDao().delete(bean);
            return bean;
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
