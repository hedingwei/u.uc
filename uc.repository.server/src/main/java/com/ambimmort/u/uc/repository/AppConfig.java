/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository;

import com.ambimmort.u.uc.repository.bean.webservice.rmapi.RepositoryManagementWebServiceBean;
import com.ambimmort.u.uc.repository.webservice.rmapi.RepositoryManagementWebServiceBeanImpl;
import java.util.Arrays;
import javax.ws.rs.ext.RuntimeDelegate;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(destroyMethod = "shutdown" )
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    public Server jaxRsServer() {
//        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(jaxRsApiApplication(), JAXRSServerFactoryBean.class);
//        factory.setServiceBeans(Arrays.< Object>asList(new RepositoryManagementWebServiceBeanImpl()));
//        factory.setAddress('/' + factory.getAddress());
//        factory.setProviders(Arrays.< Object>asList(jsonProvider()));
//        return factory.create();
        return null;
    }

//    @Bean
//    public JaxRsApiApplication jaxRsApiApplication() {
//        return new JaxRsApiApplication();
//    }

    @Bean
    public RepositoryManagementWebServiceBean repositoryManagementWebServiceBean() {
        return new RepositoryManagementWebServiceBeanImpl();
    }
//
//    @Bean
//    public PeopleService peopleService() {
//        return new PeopleService();
//    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }
}
