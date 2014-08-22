/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author 定巍
 */
public class HexDisplay {

    public static String getHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                if (i % 16 == 0) {
                    sb.append("\n");
                }
            }
            {
                tmp = Integer.toHexString(data[i]);
                if (tmp.length() == 1) {
                    tmp = "0" + tmp;
                }
                sb.append(tmp + "\t");
            }

        }
      
        return sb.toString();
    }

    public static void main(String[] args) {
        byte[] data = "dadsfasdfasdfasdfasdfasdfasdkljfhadklfjhadklfjadskljfhadf".getBytes();
        data[0] = 3;
        System.out.println(HexDisplay.getHex(data));
        
    }

}
