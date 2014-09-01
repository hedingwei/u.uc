/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.monitor;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.component.database.DBComponent;
import com.ambimmort.uc.zfserver.component.messageDriven.MDEComponent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class DBRecordAlterationMonitor<K, T extends Object> extends TimerTask {

    public static final String EVENT_DATABASE_FIELD_CHANGED = "event.database.field.changed";

    private Timer checker = new Timer(DBRecordAlterationMonitor.class + "_" + System.currentTimeMillis());

    private Class<T> tableClass;

    private Map<K, T> cache = new HashMap<K, T>();

    private long interval = 5000;

    private ConnectionSource connectionSource;

    private Dao dao = null;

    public DBRecordAlterationMonitor(long interval, ConnectionSource connectionSource, Class<T> tableClass) {
        this.interval = interval;
        this.connectionSource = connectionSource;
        this.tableClass = tableClass;
        try {
            dao = DaoManager.createDao(connectionSource, tableClass);
        } catch (SQLException ex) {
            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() {
        try {
            List all = dao.queryForAll();
            T tmp = null;
            T cTmp = null;
            Class<K> k = null;
            K key = null;
            Field[] fields = null;
            Map<String, Object> args = new HashMap<String, Object>();
            Map<K, Boolean> flag = new HashMap<K, Boolean>();
            for (Object o : all) {

                tmp = (T) o;
                key = (K) dao.extractId(o);
                flag.put(key, Boolean.TRUE);
                if (!cache.containsKey(key)) {
                    // newly created
                    args.put("action", "insert");
                    args.put("new", tmp);
                    args.put("table", tableClass.getName());
                    args.put("beanClass", tableClass);
//                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                    cache.put(key, tmp);
                } else {
                    // compare fields
                    fields = tmp.getClass().getDeclaredFields();
                    cTmp = cache.get(key);
                    args = new HashMap<String, Object>();
                    for (Field f : fields) {
                        f.setAccessible(true);
                        try {
                            if (f.get(tmp) == null) {
                                if (f.get(cTmp) != null) {
                                    args.put("action", "update");
                                    args.put("old", cTmp);
                                    args.put("new", tmp);
                                    args.put("table", tableClass.getName());
                                    args.put("beanClass", tableClass);
//                                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                                }
                            } else {

                                if (f.get(cTmp) == null) {
                                    args.put("action", "update");
                                    args.put("old", cTmp);
                                    args.put("new", tmp);
                                    args.put("table", tableClass.getName());
                                    args.put("beanClass", tableClass);
//                                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                                } else {
                                    if (!f.get(tmp).equals(f.get(cTmp))) {
                                        args.put("action", "update");
                                        args.put("old", cTmp);
                                        args.put("new", tmp);
                                        args.put("table", tableClass.getName());
                                        args.put("beanClass", tableClass);
//                                        MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                                    }
                                }
                            }
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    cache.put(key, tmp);
                }
            }
            Iterator<K> it = cache.keySet().iterator();
            K kk = null;
            for (; it.hasNext();) {
                kk = it.next();
                if (!flag.containsKey(kk)) {
                    args.put("action", "delete");
                    args.put("obj", cache.get(kk));
                    args.put("table", tableClass.getName());
                    args.put("beanClass", tableClass);
//                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                    it.remove();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        checker.scheduleAtFixedRate(this, 10000, interval);
    }

    @Override
    public void run() {
        try {
            List all = dao.queryForAll();
            T tmp = null;
            T cTmp = null;
            Class<K> k = null;
            K key = null;
            Field[] fields = null;
            Map<String, Object> args = new HashMap<String, Object>();
            Map<K, Boolean> flag = new HashMap<K, Boolean>();
            for (Object o : all) {

                tmp = (T) o;
                key = (K) dao.extractId(o);
                flag.put(key, Boolean.TRUE);
                if (!cache.containsKey(key)) {
                    // newly created
                    args.put("action", "insert");
                    args.put("new", tmp);
                    args.put("table", tableClass.getName());
                    args.put("beanClass", tableClass);
                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                    cache.put(key, tmp);
                } else {
                    // compare fields
                    fields = tmp.getClass().getDeclaredFields();
                    cTmp = cache.get(key);
                    args = new HashMap<String, Object>();
                    for (Field f : fields) {
                        f.setAccessible(true);
                        try {
                            if (f.get(tmp) == null) {
                                if (f.get(cTmp) != null) {
                                    args.put("action", "update");
                                    args.put("old", cTmp);
                                    args.put("new", tmp);
                                    args.put("table", tableClass.getName());
                                    args.put("beanClass", tableClass);
                                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                                }
                            } else {

                                if (f.get(cTmp) == null) {
                                    args.put("action", "update");
                                    args.put("old", cTmp);
                                    args.put("new", tmp);
                                    args.put("table", tableClass.getName());
                                    args.put("beanClass", tableClass);
                                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                                } else {
                                    if (!f.get(tmp).equals(f.get(cTmp))) {
                                        args.put("action", "update");
                                        args.put("old", cTmp);
                                        args.put("new", tmp);
                                        args.put("table", tableClass.getName());
                                        args.put("beanClass", tableClass);
                                        MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                                    }
                                }
                            }
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    cache.put(key, tmp);
                }
            }
            Iterator<K> it = cache.keySet().iterator();
            K kk = null;
            for (; it.hasNext();) {
                kk = it.next();
                if (!flag.containsKey(kk)) {
                    args.put("action", "delete");
                    args.put("obj", cache.get(kk));
                    args.put("table", tableClass.getName());
                    args.put("beanClass", tableClass);
                    MDEComponent.getInstance().fire(EVENT_DATABASE_FIELD_CHANGED, args);
                    it.remove();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        try {
            DBRecordAlterationMonitor<String, DPIEndPointBean> monitor = new DBRecordAlterationMonitor<String, DPIEndPointBean>(1000, DBComponent.getInstance().getConnectionSource(), DPIEndPointBean.class);
            monitor.start();
        } catch (SQLException ex) {
            Logger.getLogger(DBRecordAlterationMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
