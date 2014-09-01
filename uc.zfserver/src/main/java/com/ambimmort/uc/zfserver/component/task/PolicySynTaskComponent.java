/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.component.task;

import com.ambimmort.uc.zfserver.component.ZFComponent;
import java.util.Timer;
import net.sf.json.JSONObject;

/**
 *
 * @author 定巍
 */
public class PolicySynTaskComponent extends ZFComponent{

    
    private JSONObject state = new JSONObject();
    private Timer timer = new Timer("PolicySynTaskComponent_timer");

    @Override
    protected void prestart() throws Throwable {
        
    }

    @Override
    protected void poststart() throws Throwable {
        timer.scheduleAtFixedRate(new PolicySynTask(), 0, 50000);
    }
    
    @Override
    public String getName() {
        return "PolicySynTaskComponent";
    }

    @Override
    public JSONObject getStates() {
        return state;
    }

    @Override
    protected void refreshState() {
        
    }
    
}
