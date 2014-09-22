/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.web.logics.utils;

import com.ambimmort.uc.zfserver.wsclient.reposerver.PolicyRepoClient;
import com.ambimmort.uc.zfserver.wsclient.reposerver.PolicyRepoServerClient;
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
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;

/**
 *
 * @author 定巍
 */
@WebServlet(name = "PolicyRepoClientCall", urlPatterns = {"/system/setting/PolicyRepoClientCall"})
public class PolicyRepoClientCall extends HttpServlet {

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
        String method = request.getParameter("method");
        String parasRaw = request.getParameter("paras");
        System.out.println("method:"+method);
        try {
            parasRaw = new String(Base64Utility.decode(parasRaw),"utf-8");
            System.out.println("parasRaw:"+parasRaw);
        } catch (Base64Exception ex) {
            Logger.getLogger(PolicyRepoClientCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrintWriter out = response.getWriter();
        try {
            JSONArray array = JSONArray.fromObject(parasRaw);
            
            Object[] ps = new Object[array.size()];
            for(int i=0;i<array.size();i++){
                ps[i] = array.get(i);
            }
            try {
                Object[] rs = PolicyRepoClient.getInstance().invoke(method, ps);
                JSONArray rst = new JSONArray();
                for(Object o:rs){
                    rst.add(o);
                }
                JSONObject obj = new JSONObject();
                obj.put("code", 1);
                obj.put("rst", rst);
                out.print(obj.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JSONObject obj = new JSONObject();
                obj.put("code", 1);
                obj.put("rst", ex.toString());
                out.print(obj.toString());
         
                return;
            }
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
