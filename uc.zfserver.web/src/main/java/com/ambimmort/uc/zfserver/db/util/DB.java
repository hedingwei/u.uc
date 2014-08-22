/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.db.util;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import java.sql.SQLException;

/**
 *
 * @author 定巍
 */
public class DB {

    private static DB instance = null;
    private JdbcPooledConnectionSource connectionSource = null;

    private DB() throws SQLException {
        connectionSource = new JdbcPooledConnectionSource("jdbc:h2:tcp://localhost/./zfserver","ambimmort","ambi12341234");
        connectionSource.setMaxConnectionAgeMillis(5 * 60 * 1000);
        // change the check-every milliseconds from 30 seconds to 60
        connectionSource.setCheckConnectionsEveryMillis(60 * 1000);
        connectionSource.setTestBeforeGet(true);
    }

    public static DB getInstance() throws SQLException {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    public JdbcPooledConnectionSource getConnectionSource() {
        return connectionSource;
    }

}
