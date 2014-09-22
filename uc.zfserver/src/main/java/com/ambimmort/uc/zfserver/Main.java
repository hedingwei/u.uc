/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;

import com.ambimmort.uc.zfserver.component.ZFComponentManager;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.ambimmort.uc.zfserver.component.messageDriven.MDEComponent;
import com.ambimmort.uc.zfserver.component.monitor.MonitorComponent;
import com.ambimmort.uc.zfserver.component.policyrepo.PolicyRepositoryComponent;
import com.ambimmort.uc.zfserver.component.task.PolicySynTaskComponent;
import com.ambimmort.uc.zfserver.component.webservice.WebServiceComponent;
import com.ambimmort.uc.zfserver.component.zfserver.ZFServerComponent;

/**
 *
 * @author 定巍
 */
public class Main {
    
    public static void main(String[] args) {

        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        
        try {
            //注册：数据库组件
            ZFComponentManager.getInstance().registerZFComponent(DBComponent.getInstance());
            //注册：消息驱动组件
            ZFComponentManager.getInstance().registerZFComponent(MDEComponent.getInstance());
            //注册：监控组件
            ZFComponentManager.getInstance().registerZFComponent(MonitorComponent.getInstance());
            //注册：ZFServer服务器组件
            ZFComponentManager.getInstance().registerZFComponent(ZFServerComponent.getInstance());
            //注册：Vysper XMPP消息组件
//            ZFComponentManager.getInstance().registerZFComponent(VysperComponent.getInstance());
            //注册：策略仓库组件
            ZFComponentManager.getInstance().registerZFComponent(PolicyRepositoryComponent.getInstance());
            
//            //注册：策略同步任务组件
            ZFComponentManager.getInstance().registerZFComponent( new PolicySynTaskComponent());
            
            //注册：策略Web服务组件
            ZFComponentManager.getInstance().registerZFComponent( new WebServiceComponent());
            
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        ZFComponentManager.getInstance().startAll();

//        Thread.sleep(5000);
    }
}
