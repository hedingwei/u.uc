/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.zfserver;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.component.database.dao.DPIEndPointBeanDao;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class DPIEndPointManager {

    private static DPIEndPointManager instance = null;

    private DPIEndPointManager() {}

    public static DPIEndPointManager getInstance() {
        if (instance == null) {
            instance = new DPIEndPointManager();
        }
        return instance;
    }

    private Map<String, DPIEndPoint> endPoints = new HashMap<String, DPIEndPoint>();

    public void refresh(){
        try {
            for(DPIEndPointBean b:DPIEndPointBeanDao.getInstance().getDpiEndPointDao().queryForAll()){
                if(!endPoints.containsKey(b.getIp())){
                    endPoints.put(b.getIp(), new DPIEndPoint(b));   
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPointManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public DPIEndPoint getByName(String devName){
        for(DPIEndPoint dpi: endPoints.values()){
            if(dpi.getDpiEndPointBean().getDevName().equals(devName)){
                return dpi;
            }
        }
        return null;
    }
  

    public Map<String, DPIEndPoint> getEndPoints() {
        return endPoints;
    }
    
    
    
    public static void main(String[] args){
        
    }
    
}
