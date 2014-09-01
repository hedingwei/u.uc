/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

/**
 * ZFComponent 代表整个系统中的组件模块。每个模块是一个单件。通常有数据库模块、消息驱动模块等。
 * 每个ZFComponent会定期更新自己的状态。
 * @author 定巍
 */
public abstract class ZFComponent {
    
    private Timer timer;
    
    
    /**
     * 每个组件都有一个名字
     * @return 
     */
    public abstract String getName();
    
    /**
     * 获取本组件的状态。状态由一个JSONObject描述。JSONObject的结构没有做规定，由应用层自定。
     * @return 
     */
    public abstract JSONObject getStates();
    
    public void start(){
        try {
            prestart();
            timer = new Timer("timer_"+getName());
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    refreshState();
                }
            }, 0, 5000);
            poststart();
        } catch (Throwable ex) {
            Logger.getLogger(ZFComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 在start()方法调用之前调用
     * @throws Throwable 
     */
    protected  void prestart() throws Throwable{}
    
    /**
     * 在start()方法调用之后调用
     * @throws Throwable 
     */
    protected  void poststart() throws Throwable{}
    
    /**
     * 刷新本组件的状态，由子类实现
     */
    protected abstract void refreshState();

    
    
    
}
