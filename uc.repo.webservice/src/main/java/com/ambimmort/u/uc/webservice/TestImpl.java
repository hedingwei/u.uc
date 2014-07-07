/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.u.uc.webservice;

import javax.jws.WebService;

/**
 *
 * @author 定巍
 */
@WebService()
public class TestImpl implements Test{
    public String sayHello(String name){
        return name+" hello";
    }

    @Override
    public ABean getBean() {
        return new ABean();
    }
}
