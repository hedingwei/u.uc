/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.webservice;

/**
 *
 * @author 定巍
 */
public class ABean {

    private String a = "b";
    private String b = "a";

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "ABean{" + "a=" + a + ", b=" + b + '}';
    }

}
