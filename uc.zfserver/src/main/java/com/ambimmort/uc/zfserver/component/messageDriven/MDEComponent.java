/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.messageDriven;

import com.ambimmort.uc.zfserver.bean.ZFComponentBean;
import com.ambimmort.uc.zfserver.component.ZFComponent;
import com.ambimmort.uc.zfserver.component.database.dao.ZFComponentBeanDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.sf.json.JSONObject;

/**
 *
 * @author 定巍
 */
public class MDEComponent extends ZFComponent{

    private static MDEComponent instance = null;

    private ThreadPoolExecutor service = null;

    private Map<String, List<EventHandler>> tops = new HashMap<String, List<EventHandler>>();
    
    private JSONObject states = new JSONObject();

    private MDEComponent() {
        service = new ThreadPoolExecutor(3, 500, 5 , TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(20));
    }

    public static MDEComponent getInstance() {
        if (instance == null) {
            instance = new MDEComponent();
        }
        return instance;
    }

    public void listen(String event, EventHandler listener) {
        if (tops.containsKey(event)) {
            tops.get(event).add(listener);
        } else {
            List<EventHandler> l = new ArrayList<EventHandler>();
            l.add(listener);
            tops.put(event, l);
        }
    }

    public void fire(final String event, final Map<String, Object> args) {
        if (tops.containsKey(event)) {
            for (final EventHandler el : tops.get(event)) {
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                        el.onEvent(event, args);
                        }catch(Throwable e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public String getName() {
        return "MessageDrivenEngine";
    }


    @Override
    public JSONObject getStates() {
        return states;
    }

    @Override
    protected void refreshState() {
        try {
            states.put("ActiveCount", service.getActiveCount());
            states.put("CompletedTaskCount", service.getCompletedTaskCount());
            states.put("CorePoolSize", service.getCorePoolSize());
            states.put("LargestPoolSize", service.getLargestPoolSize());
            states.put("MaximumPoolSize", service.getMaximumPoolSize());
            states.put("PoolSize", service.getPoolSize());
            states.put("TaskCount", service.getTaskCount());
            ZFComponentBean bean = new ZFComponentBean();
            bean.setName(getName());
            bean.setStates(states.toString(4));
            ZFComponentBeanDao.getInstance().getZfComponentDao().createOrUpdate(bean);
        } catch(Exception e){
            e.printStackTrace();
        }
 
    }

}
