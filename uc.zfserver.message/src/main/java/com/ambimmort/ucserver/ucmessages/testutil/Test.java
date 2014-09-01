/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.ucserver.ucmessages.testutil;

import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.util.*;

/**
 *
 * @author 定巍
 */
public class Test {
    public static void main(String[] args){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<msg_0x00 xmlns=\"http://www.ambimmort.com/msg_0x00\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
"	xsi:schemaLocation=\"http://www.ambimmort.com/msg_0x00 msg_0x00.xsd\"\n" +
"	messageNo=\"0\" messageSequenceNo=\"1\" messageSerialNo=\"1\">\n" +
"	<Web_Hit_Threshold>0</Web_Hit_Threshold>\n" +
"	<KW_Threholds>0</KW_Threholds>\n" +
"	<SearchEngines>\n" +
"		<SearchEngine>\n" +
"			<SEName>www.baidu.com</SEName>\n" +
"		</SearchEngine>\n" +
"		<SearchEngine>\n" +
"			<SEName>www.baidu.com</SEName>\n" +
"		</SearchEngine>\n" +
"	</SearchEngines>\n" +
"	<Cookie_Hosts>\n" +
"		<Cookie_Host>\n" +
"			<name>dfsd</name>\n" +
"			<key>sdfasdf123123</key>\n" +
"		</Cookie_Host>\n" +
"                <Cookie_Host>\n" +
"			<name>dfsd1</name>\n" +
"			<key>sdfasdfddd</key>\n" +
"		</Cookie_Host>\n" +
"	</Cookie_Hosts>\n" +
"	<MessageSerialNo>111</MessageSerialNo>\n" +
"</msg_0x00>";
        UcMsg msg = UcMsg.buildUcMsg("0x00", xml);
        HexDisplay.print(msg.toBytes());
    }
}
