/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zf.bean.webservice;

import com.ambimmort.uc.zf.bean.ZFEndPointManager;
import com.ambimmort.uc.zf.bean.def.IZFEndPoint;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class ZFWebServiceBeanImpl implements ZFWebServiceBean {

    public void createIZFEndPoint(String entityView) {
        ZFEndPointManager.getInstance().createIZFEndPoint(entityView);
    }

    public void deleteIZFEndPoint(String name) {
        ZFEndPointManager.getInstance().deleteIZFEndPoint(name);
    }

    public void updateIZFEndPoint(String name, String entityView) {
        IZFEndPoint zfendpoint = ZFEndPointManager.getInstance().getIZFEndPoint(name);
        zfendpoint.setEntityView(JSONObject.fromObject(entityView));
        zfendpoint.persist();
    }

    public String getIZFEndPoint(String name) {
        return ZFEndPointManager.getInstance().getIZFEndPoint(name).getEntityView().toString();
    }

    public List<String> getAllIZFEndPoints() {
        List<String> list = new ArrayList<String>();
        List<IZFEndPoint> arr = ZFEndPointManager.getInstance().getAllIZFEndPoints();
        for (int i = 0; i < arr.size(); i++) {
            list.add(arr.get(i).getEntityView().toString());
        }
        return list;
    }
}
