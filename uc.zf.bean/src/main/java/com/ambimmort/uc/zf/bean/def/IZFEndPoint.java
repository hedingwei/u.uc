/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zf.bean.def;

import java.util.Observer;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public interface IZFEndPoint extends Observer {

    public void setStart(boolean shouldStart);

    public boolean isStarted();

    public void setClientChannelConnected(String ip, boolean isConnected);

    public void setServerChannelConnected(String ip, boolean isConnected);

    public JSONObject getEntityView();

    public void setEntityView(JSONObject entityView);

    public void setStatus(String property, Object value);

    public Object getStatus(String property);

    public void persist();
}
