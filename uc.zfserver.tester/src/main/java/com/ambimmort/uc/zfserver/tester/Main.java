/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.tester;

/**
 * @author 定巍
 */
public class Main {
    public static void main(String[] args){
      MySimpleTester tester = new MySimpleTester("localhost", 50000);
      tester.add("0x01", "src/main/resources/xml/流量流向/msg_0x01.xml");
      tester.add("0x45", "src/main/resources/xml/流量流向/msg_0x45.xml");
      tester.add("0xCA", "src/main/resources/xml/流量流向/msg_0xca.xml");
      tester.start();
    }
}
