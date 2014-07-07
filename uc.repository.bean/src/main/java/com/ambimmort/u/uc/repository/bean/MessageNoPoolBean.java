/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author 定巍
 */
@DatabaseTable(tableName = "MESSAGENOPOOL")
public class MessageNoPoolBean {

    @DatabaseField(id = true)
    private int no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "MessageNoPool{" + "no=" + no + '}';
    }
    
    

}
