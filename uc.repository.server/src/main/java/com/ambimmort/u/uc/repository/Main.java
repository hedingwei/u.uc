/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository;


/**
 *
 * @author 定巍
 */
public class Main {

    private static final String ADDRESS = "http://localhost:9004/?wsdl";

    public static void main(String[] args) {
        UcPolicyRepository.init();
        UcPolicyRepositoryServer.getInstance().setAddress(ADDRESS);
        UcPolicyRepositoryServer.getInstance().start();
    }
}
