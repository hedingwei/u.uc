package com.ambimmort.uc.zf.bean;

import com.ambimmort.uc.zf.bean.def.IZFEndPointManager;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * Hello world!
 *
 */
public class App {

    private static final String ADDRESS = "http://localhost:9000/";

    public static void main(String[] args) {
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(IZFEndPointManager.class);
        factory.setAddress(ADDRESS);
        Server server = factory.create();
        server.start();
        //http://localhost:9000/ZFEndPoint
    }
}
