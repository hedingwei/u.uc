/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.component.monitor;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.ZFComponentBean;
import com.ambimmort.uc.zfserver.bean.ZFPropertyBean;
import com.ambimmort.uc.zfserver.component.ZFComponent;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.ambimmort.uc.zfserver.component.database.dao.ZFComponentBeanDao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

/**
 *
 * @author 定巍
 */
public class MonitorComponent extends ZFComponent{
    private static MonitorComponent instance = null;
    
    private JSONObject state = new JSONObject();
    
    private DBRecordAlterationMonitor<String,ZFPropertyBean> zfPropertyBeanMonitor = null;
    
    private DBRecordAlterationMonitor<String,DPIEndPointBean> dpiEndoPointBeanMonitor = null;
    
    private MonitorComponent(){
        try {
            zfPropertyBeanMonitor = new DBRecordAlterationMonitor<String, ZFPropertyBean>(30000, DBComponent.getInstance().getConnectionSource(), ZFPropertyBean.class);
            zfPropertyBeanMonitor.start();
            System.out.println("-----------");
            dpiEndoPointBeanMonitor = new DBRecordAlterationMonitor<String, DPIEndPointBean>(30000, DBComponent.getInstance().getConnectionSource(), DPIEndPointBean.class);
            dpiEndoPointBeanMonitor.start();
            
        } catch (SQLException ex) {
            Logger.getLogger(MonitorComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static MonitorComponent getInstance(){
        if(instance == null){
            instance = new MonitorComponent();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "MonitorComponent";
    }

    @Override
    public JSONObject getStates() {
        return state;
    }

    @Override
    protected void refreshState() {
        try {
            state.put("zfPropertyBeanMonitor.scheduledExecutionTime",zfPropertyBeanMonitor.scheduledExecutionTime());
            state.put("dpiEndoPointBeanMonitor.scheduledExecutionTime",dpiEndoPointBeanMonitor.scheduledExecutionTime());
            ZFComponentBean bean = new ZFComponentBean();
            bean.setName(getName());
            bean.setStates(state.toString(4));
            ZFComponentBeanDao.getInstance().getZfComponentDao().createOrUpdate(bean);
        
        }  catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
