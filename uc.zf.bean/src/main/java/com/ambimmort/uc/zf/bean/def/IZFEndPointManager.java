/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zf.bean.def;

import java.util.List;

/**
 *
 * @author Administrator
 */
public interface IZFEndPointManager {

    public IZFEndPoint createIZFEndPoint(String entityView);

    public IZFEndPoint deleteIZFEndPoint(String name);

    public IZFEndPoint getIZFEndPoint(String name);
    
    public List<IZFEndPoint> getAllIZFEndPoints();

    void init();
    
}
