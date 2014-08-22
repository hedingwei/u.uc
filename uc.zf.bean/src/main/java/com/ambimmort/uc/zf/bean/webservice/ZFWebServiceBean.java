/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zf.bean.webservice;

import java.util.List;
import javax.jws.WebService;

/**
 *
 * @author Administrator
 */
@WebService(name="zfweb")
public interface ZFWebServiceBean {
    
    public void createIZFEndPoint(String entityView);

    public void deleteIZFEndPoint(String name);

    public void updateIZFEndPoint(String name, String entityView);
    
    public String getIZFEndPoint(String name);
    
    public List<String> getAllIZFEndPoints();
}
