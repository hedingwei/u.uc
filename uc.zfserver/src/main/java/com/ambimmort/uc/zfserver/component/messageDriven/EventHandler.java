/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.component.messageDriven;

import java.util.Map;

/**
 *
 * @author 定巍
 */
public interface EventHandler {
    public void onEvent(String name,Map<String,Object> args) throws Throwable;
}
