/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zf.bean;

import com.ambimmort.uc.zf.bean.def.IZFEndPointManager;
import com.ambimmort.uc.zf.bean.def.IZFEndPoint;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class ZFEndPointManager implements IZFEndPointManager {

    private Mongo mg = null;
    private DB db;
    private DBCollection zfendpoints;
    private static ZFEndPointManager instance;
    private Map<String, IZFEndPoint> map = new HashMap<String, IZFEndPoint>();

    private ZFEndPointManager() {
        init();
    }

    public static ZFEndPointManager getInstance() {
        if (instance == null) {
            instance = new ZFEndPointManager();
        }
        return instance;
    }

    @Override
    public IZFEndPoint getIZFEndPoint(String name) {
        return map.get(name);
    }

    @Override
    public IZFEndPoint createIZFEndPoint(String entityView) {
        JSONObject one = JSONObject.fromObject(entityView);
        String name = one.getString("name");
        if (!map.containsKey(name)) {
            DBObject endpoint = (DBObject) JSON.parse(entityView);
            zfendpoints.save(endpoint);
            JSONObject db = JSONObject.fromObject(zfendpoints.find(new BasicDBObject("name", name)).next().toString());
            IZFEndPoint zfendpoint = new ZFEndPointImpl(db);
            map.put(name, zfendpoint);
        }
        return map.get(name);
    }

    @Override
    public IZFEndPoint deleteIZFEndPoint(String name) {
        IZFEndPoint zfendpoint = getIZFEndPoint(name);
        if (map.containsKey(name)) {
            DBObject endpoint = new BasicDBObject("name", name);
            zfendpoints.remove(endpoint);
            map.remove(name);
        }
        return zfendpoint;
    }

    public Map<String, IZFEndPoint> getMap() {
        return map;
    }

    @Override
    public void init() {
        try {
            mg = new Mongo();
            //mg = new Mongo("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        //获取temp DB；如果默认没有创建，mongodb会自动创建
        db = mg.getDB("temp");
        //获取users DBCollection；如果默认没有创建，mongodb会自动创建
        zfendpoints = db.getCollection("zfendpoints");
        //已存在数据库中的数据 需要加载到Map
        DBCursor cur = zfendpoints.find();
        while (cur.hasNext()) {
            DBObject one = cur.next();
            JSONObject view = JSONObject.fromObject(one.toString());
            IZFEndPoint zfendpoint = new ZFEndPointImpl(view);
            String name = view.getString("name");
            map.put(name, zfendpoint);
        }
    }

    public void destory() {
        if (mg != null) {
            mg.close();
        }
        mg = null;
        db = null;
        zfendpoints = null;
        System.gc();
    }

    public void print(Object o) {
        System.out.println(o);
    }

    private void queryAll() {
        print("查询ZFEndPointManager的所有数据：");
        //db游标
        DBCursor cur = zfendpoints.find(new BasicDBObject("name", "GN-01"));
        while (cur.hasNext()) {
            DBObject ob = cur.next();
            JSONObject one = JSONObject.fromObject(ob.toString());
//            print(cur.next());
            print(one);
        }
    }

    public DBCollection getZfendpoints() {
        return zfendpoints;
    }

    public List<IZFEndPoint> getAllIZFEndPoints() {
        List<IZFEndPoint> list = new ArrayList<IZFEndPoint>();
        for (String name : map.keySet()) {
            list.add(map.get(name));
        }
        return list;
    }

    public static void main(String[] args) {
        ZFEndPointManager.getInstance().queryAll();
//        String view = "{\"name\":\"GN-03\",\"channels\":[{\"client\":{\"ip\":\"10.8.1.1\",\"port\":25000,\"isConnected\":true},\"server\":{\"ip\":\"10.8.1.1\",\"port\":50000,\"isConnected\":true}},{\"client\":{\"ip\":\"10.8.1.1\",\"port\":25000,\"isConnected\":true},\"server\":{\"ip\":\"10.8.1.1\",\"port\":50000,\"isConnected\":true}}],\"status\":{\"isStarted\":true,\"lastHeartBeat\":1283739292,\"lastAAA\":234234232,\"lastPolicySync\":3242342342342}}}}ue,\"lastHeartBeat\":1283739292,\"lastAAA\":234234232,\"lastPolicySync\":3242342342342}}}}}}}}";
//        ZFEndPointManager.getInstance().createIZFEndPoint(view);
//        ZFEndPointManager.getInstance().deleteIZFEndPoint("GN-01");
    }
}
