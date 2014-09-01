/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 定巍
 */
public class ZFComponentManager {

    private static ZFComponentManager instance = null;

    private Map<Class, ZFComponent> components = new HashMap<Class,ZFComponent>();
    private List<ZFComponent> zFComponents = new ArrayList<ZFComponent>();
    
    private ZFComponentManager() {
        
    }

    public static ZFComponentManager getInstance() {
        if (instance == null) {
            instance = new ZFComponentManager();
        }
        return instance;
    }
    
    public void registerZFComponent(ZFComponent comp){
        zFComponents.add(comp);
        components.put(comp.getClass(), comp);
    }
    
    public <D extends ZFComponent>  D lookup(Class<D> clazz){
        return (D)components.get(clazz);
    }
    
    
    
    public void startAll(){
        for(ZFComponent comp:zFComponents){
            comp.start();
            System.out.println(comp.getName()+" initialized.");
        }
        System.out.println("All components initialized.");
    }
    
}
