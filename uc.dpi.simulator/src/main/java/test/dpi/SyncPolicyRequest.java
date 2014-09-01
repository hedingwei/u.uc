/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.dpi;

import com.ambimmort.uc.dpi.simulator.server.core.UcRawMessage;
import com.ambimmort.ucserver.client.Connection;
import com.ambimmort.ucserver.ucmessages.uc.xC2;
import com.ambimmort.ucserver.util.HexDisplay;

/**
 *
 * @author Administrator
 */
public class SyncPolicyRequest {
    public static void main(String[] args) throws InterruptedException{
        UcRawMessage msg = new UcRawMessage();
        Connection con = new Connection("localhost", 50005);
        con.start();
        
        Thread.sleep(1000);
        
        
        xC2 m = new xC2("c:/program1/UCPolicy/");
        m.normalize();
        msg.setHeader(m.getHeader().getBytes());
        msg.setBody(m.getBodyBytes());
        System.out.println(HexDisplay.getHex(msg.getBytes()));
        con.getSession().write(msg);
        
    }
}
