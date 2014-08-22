/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages.exceptions;

/**
 *
 * @author 定巍
 */
public class UcTypeException extends Exception {

  
    public UcTypeException() {
    }

    public UcTypeException(String message) {
        super(message);
    }

    public static void main(String[] args){
        System.out.println(Short.MAX_VALUE);
        System.out.println(Short.MIN_VALUE);
        System.out.println(Short.MAX_VALUE-Short.MIN_VALUE);
        System.out.println(Short.SIZE);
        System.out.println((int)(Math.pow(2, 32)-1));
    }
    
}
