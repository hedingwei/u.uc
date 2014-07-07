/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.bean.webservice;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.RepositoryEntryBean;
import java.util.List;
import javax.jws.WebService;

/**
 *
 * @author 定巍
 */
@WebService
public interface DPIEndPointManagementWebServiceBean {
    
    public DPIEndPointBean createDPIEndPoint(String name, String clientChannelIp, String serverChannelIp, int serverChannelPort);
    
    public DPIEndPointBean updateDPIEndPoint(String name, String clientChannelIp, String serverChannelIp, int serverChannelPort);
            
    public DPIEndPointBean updateDPIEndPointName(String name, String newName);
    
    public DPIEndPointBean deleteDPIEndPoint(String name);
    
    public DPIEndPointBean turnOnDPIEndPoint(String name);
    
    public DPIEndPointBean turnOffDPIEndPoint(String name);
    
    public List<DPIEndPointBean> getAllDPIEndPoints();
    
    public List<RepositoryEntryBean> getAllRepositoryEntryBeans(String endPointName);
    
    public DPIEndPointBean getDPIEndPoint(String endPointName);
    
    public RepositoryEntryBean addRepositoryEntryBean(String endPointName,String type, String name);
    
    public RepositoryEntryBean removeRepositoryEntryBean(long repositoryEntryBeanId);
    
}
