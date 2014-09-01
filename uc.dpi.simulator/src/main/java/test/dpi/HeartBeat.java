/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dpi;

import com.ambimmort.uc.dpi.simulator.server.core.UcRawMessage;
import com.ambimmort.ucserver.client.Connection;
import com.ambimmort.ucserver.ucmessages.uc.xC1;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.HexDisplay;

/**
 *
 * @author Administrator
 */
public class HeartBeat {

    public static void main(String[] args) {
        UcRawMessage msg = new UcRawMessage();
        Connection con = new Connection("localhost", 50005);
        con.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HeartBeat.class.getName()).log(Level.SEVERE, null, ex);
        }

        xC1 m = new xC1("GreeNet DPI");
        m.normalize();
        msg.setHeader(m.getHeader().getBytes());
        msg.setBody(m.getBodyBytes());
        
        

        while (true) {
            try {
                System.out.println(HexDisplay.getHex(msg.getBytes()));
                con.getSession().write(msg);
            } catch (Throwable a) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HeartBeat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HeartBeat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
