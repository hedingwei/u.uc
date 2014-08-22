/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zf.bean;

import com.ambimmort.uc.zf.bean.def.IZFEndPoint;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.Observable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class ZFEndPointImpl implements IZFEndPoint {

    private JSONObject entityView = null;
    private String name = null;

    public ZFEndPointImpl(JSONObject entityView) {
        this.entityView = entityView;
        this.name = entityView.getString("name");
    }

    public void setStart(boolean shouldStart) {
        this.entityView.getJSONObject("status").put("isStarted", shouldStart);
        persist();
    }

    public boolean isStarted() {
        return this.entityView.getJSONObject("status").getBoolean("isStarted");
    }

    public void setClientChannelConnected(String ip, boolean isConnected) {
        JSONArray arr = this.entityView.getJSONArray("channels");
        for (int i = 0; i < arr.size(); i++) {
            JSONObject one = arr.getJSONObject(i).getJSONObject("client");
            if (one.getString("ip").equals(ip)) {
                one.put("isConnected", isConnected);
            }
        }
        persist();
    }

    public void setServerChannelConnected(String ip, boolean isConnected) {
        JSONArray arr = this.entityView.getJSONArray("channels");
        for (int i = 0; i < arr.size(); i++) {
            JSONObject one = arr.getJSONObject(i).getJSONObject("server");
            if (one.getString("ip").equals(ip)) {
                one.put("isConnected", isConnected);
            }
        }
        persist();
    }

    public JSONObject getEntityView() {
        return this.entityView;
    }

    public void setEntityView(JSONObject entityView) {
        this.entityView = entityView;
    }

    public void setStatus(String property, Object value) {
        if (property.equals("isStarted")) {
            this.entityView.getJSONObject("status").put(property, value);
        } else if (property.equals("isConnected")) {
            JSONArray arr = this.entityView.getJSONArray("channel");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject one = arr.getJSONObject(i);
                one.put("isConnected", value);
            }
        }
        persist();
    }

    public Object getStatus(String property) {
        return this.entityView.getJSONObject("status").get(property);
    }

    public void persist() {
        DBObject endpoint = new BasicDBObject("name", this.name);
        DBObject endpointupdate = (DBObject) JSON.parse(this.entityView.toString());
        ZFEndPointManager.getInstance().getZfendpoints().update(endpoint, endpointupdate);
        ZFEndPointManager.getInstance().getMap().put(name, this);
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof JSONObject) {
            JSONObject event = (JSONObject) arg;
            System.out.println(event);
            if (event.getString("type").equals("updateStatus")) {
                setStatus(event.getString("property"), event.getString("value"));
            } else if (event.getString("type").equals("channelStatus")) {
                setStatus(event.getString("property"), event.getString("value"));
            }
        }
    }

    public static void main(String[] args) {
        String view = "{\"name\":\"GN-03\",\"channels\":[{\"client\":{\"ip\":\"10.8.1.1\",\"port\":25000,\"isConnected\":true},\"server\":{\"ip\":\"10.8.1.1\",\"port\":50000,\"isConnected\":true}},{\"client\":{\"ip\":\"10.8.1.1\",\"port\":25000,\"isConnected\":true},\"server\":{\"ip\":\"10.8.1.1\",\"port\":50000,\"isConnected\":true}}],\"status\":{\"isStarted\":true,\"lastHeartBeat\":1283739292,\"lastAAA\":234234232,\"lastPolicySync\":3242342342342}}}}ue,\"lastHeartBeat\":1283739292,\"lastAAA\":234234232,\"lastPolicySync\":3242342342342}}}}}}}}";
        String up = "{\"type\":\"updateStatus\",\"property\":\"isConnected\",\"value\":\"false\"}";
        ZFEndPointImpl zf = new ZFEndPointImpl(JSONObject.fromObject(view));
        zf.setStart(false);
        System.out.println(zf.getEntityView());
    }
}
