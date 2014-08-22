/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author 定巍
 */
public class Test {
    public static void main(String[] args){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            IOUtils.copy(new FileInputStream("C:\\Users\\定巍\\Downloads\\uc5.4.3扩展\\0x40or0x46.dat"), os);
            IOUtils.copy(new FileInputStream("C:\\Users\\定巍\\Downloads\\uc5.4.3扩展\\0x45.dat"), os);
//            os.write(2);
            IOUtils.copy(new FileInputStream("C:\\Users\\定巍\\Downloads\\uc5.4.3.dat"), os);
//            IOUtils.copy(new FileInputStream("C:\\Users\\定巍\\Downloads\\uc5.4.3扩展\\0x40or0x46.dat"), os);
            System.out.println( Arrays.toString(os.toByteArray()));
            System.out.println("length="+os.toByteArray().length);
            Socket socket = new Socket("localhost", 50009);
            socket.getOutputStream().write(os.toByteArray());
            socket.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
