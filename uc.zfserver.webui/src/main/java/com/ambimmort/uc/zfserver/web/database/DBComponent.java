/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.web.database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import net.sf.json.JSONObject;

/**
 *
 * @author 定巍
 */
public class DBComponent {

    private static DBComponent instance = null;
    private JdbcPooledConnectionSource connectionSource = null;
    private JSONObject state = new JSONObject();

    private DBComponent() throws SQLException {
        connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/zfdb?zeroDateTimeBehavior=convertToNull", "root", "123456");
//        connectionSource = new JdbcPooledConnectionSource("jdbc:h2:tcp://localhost/./zfserver","ambimmort","ambi12341234");
        connectionSource.setMaxConnectionAgeMillis(5 * 60 * 1000);
        // change the check-every milliseconds from 30 seconds to 60
        connectionSource.setCheckConnectionsEveryMillis(60 * 1000);
        connectionSource.setTestBeforeGet(true);
    }

    public static DBComponent getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBComponent();
        }
        return instance;
    }

    public JdbcPooledConnectionSource getConnectionSource() {
        return connectionSource;
    }

   

    private void initTable(Class aClass) {
        try {
            TableUtils.createTableIfNotExists(DBComponent.getInstance().getConnectionSource(), aClass);
        } catch (Exception e) {
        }
    }



}
