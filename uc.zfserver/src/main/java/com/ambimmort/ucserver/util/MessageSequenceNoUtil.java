/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.ucserver.util;

import com.ambimmort.uc.zfserver.bean.SequenceNoPtrBean;
import com.ambimmort.uc.zfserver.channel.client.ClientChannel;
import com.ambimmort.uc.zfserver.component.database.MyDaoManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class MessageSequenceNoUtil {
   public static synchronized long getAUsableMessageSequenceNo(int messageType) throws SQLException {
       Dao<SequenceNoPtrBean, Integer> sequenceDao = MyDaoManager.getInstance().getDao(SequenceNoPtrBean.class);
        System.out.println("messageType:" + messageType + "\t");
        long mn = 0;
        try {
            SequenceNoPtrBean bean = sequenceDao.queryForId(messageType);
            System.out.println("bean:" + bean);
            if (bean == null) {
                bean = new SequenceNoPtrBean();
                bean.setMessageType(messageType);
                bean.setSequenceNo(1);
                sequenceDao.create(bean);
                mn = 1;
            } else {
                mn = bean.getSequenceNo() + 1;
                bean.setSequenceNo(mn);
                sequenceDao.update(bean);
            }
            return mn;
        } catch (SQLException ex) {
            Logger.getLogger(ClientChannel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mn;
    }
}
