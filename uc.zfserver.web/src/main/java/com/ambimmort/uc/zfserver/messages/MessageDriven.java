/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author 定巍
 */
public class MessageDriven {

    private static MessageDriven instance = null;

    private ExecutorService service = Executors.newFixedThreadPool(10);

    private Map<String, List<EventHandler>> tops = new HashMap<String, List<EventHandler>>();

    private MessageDriven() {
    }

    public static MessageDriven getInstance() {
        if (instance == null) {
            instance = new MessageDriven();
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

    public void fire(final String event,final Map<String, Object> args) {
        for (final EventHandler el : tops.get(event)) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    el.onEvent(event, args);
                }
            });
        }

    }

}
