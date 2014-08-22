/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.dpi.simulator.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ho.yaml.Yaml;

/**
 *
 * @author 定巍
 */
public class UcServerConfig {

    private HashMap map = null;

    public HashMap getMap() {
        return map;
    }

    public static HashMap getConfig() {
        try {
            if (new File("etc/ucserver.yml").exists()) {
                return (HashMap) Yaml.load(new File("etc/ucserver.yml"));
            } else if (new File("src/main/resources/ucserver.yml").exists()) {
                return (HashMap) Yaml.load(new File("src/main/resources/ucserver.yml"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        HashMap c = UcServerConfig.getConfig();
        System.out.println(((Map)c.get("server")).get("port"));
    }
}
