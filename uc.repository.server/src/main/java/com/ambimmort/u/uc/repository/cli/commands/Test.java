package com.ambimmort.u.uc.repository.cli.commands;

import java.net.MalformedURLException;
import java.net.URI;

import com.ambimmort.uc.repository.services.client.RepositoryManagementWebServiceBeanImplService;
import com.ambimmort.uc.repository.services.client.Rmapi;



public class Test {
	public static void main(String[] args) throws MalformedURLException{
		RepositoryManagementWebServiceBeanImplService service = new RepositoryManagementWebServiceBeanImplService(URI.create(args[0]).toURL());
		Rmapi api = service.getRepositoryManagementWebServiceBeanImplPort();
		if(args[1].equals("1")){
			System.out.println(api.test1());
		}else if(args[1].equals("2")){
			System.out.println(api.test2());
		}else if(args[1].equals("3")){
			System.out.println(api.test3());
		}
	}
}
