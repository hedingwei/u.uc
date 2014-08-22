/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.web.logics.utils;

import com.ambimmort.uc.zfserver.wsclient.reposerver.PolicyRepoClient;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.ucserver.util.HexDisplay;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.geronimo.mail.util.Base64;

/**
 *
 * @author 定巍
 */
@WebServlet(name = "BinaryFormPolicy", urlPatterns = {"/system/util/BinaryFormPolicy"})
public class BinaryFormPolicy extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String messageNo = request.getParameter("messageNo");
        String messageType = request.getParameter("messageType");
        String serialNo = request.getParameter("serialNo");
        String instance = request.getParameter("instance");
        String fromDatabase = request.getParameter("fromDatabase");
        String content = "";
        if(!fromDatabase.equals("true")){
            content = new String(Base64.decode(request.getParameter("content")),"utf-8");
        }else{
            PolicyRepoClient client = PolicyRepoClient.getInstance();
            try {
                Object[] rs = PolicyRepoClient.getInstance().invoke("getMessage", messageType,instance,Integer.parseInt(messageNo));
                JSONObject obj = JSONObject.fromObject(rs[0]);
                content = new String(Base64.decode(obj.getJSONObject("svnFile").getString("content")),"utf-8");
            } catch (Exception ex) {
                Logger.getLogger(BinaryFormPolicy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JSONObject rst = new JSONObject();
        if(messageType.equals("0x00")){
            UcMsg.x00 x00 = new UcMsg.x00();
            try {
                x00.setSerialNo(UcType.newUINT4(Long.parseLong(serialNo)));
            } catch (UcTypeException ex) {
                Logger.getLogger(BinaryFormPolicy.class.getName()).log(Level.SEVERE, null, ex);
            }
            x00.parseXML(content);
            try {
                x00.getHeader().setMessageNo(UcType.newUINT2(Integer.parseInt(messageNo)));
            } catch (UcTypeException ex) {
                Logger.getLogger(BinaryFormPolicy.class.getName()).log(Level.SEVERE, null, ex);
            }
            String hex = HexDisplay.getHex(x00.toBytes());
            hex = new String(Base64.encode(hex.getBytes("utf-8")),"utf-8");
            rst.put("code", 1);
            rst.put("rst", hex);
        }

        try {
            out.print(rst.toString());
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
