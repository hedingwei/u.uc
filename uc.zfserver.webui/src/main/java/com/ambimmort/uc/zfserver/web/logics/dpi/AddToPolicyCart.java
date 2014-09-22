/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.web.logics.dpi;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author 定巍
 */
@WebServlet(name = "AddToPolicyCart", urlPatterns = {"/logic/dpi/AddToPolicyCart"})
public class AddToPolicyCart extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
           String messageType = request.getParameter("messageType");
           String instance = request.getParameter("instance");
           String messageNo = request.getParameter("messageNo");
           String serialNo = request.getParameter("serialNo");
           Object obj = request.getSession().getAttribute("temp.cart.policies");
           if(obj == null){
               JSONArray array = new JSONArray();
               JSONObject p = new JSONObject();
               p.put("messageType", messageType);
               p.put("instance",instance);
               p.put("messageNo",messageNo);
               p.put("serialNo", serialNo);
               array.add(p);
               request.getSession().setAttribute("temp.cart.policies", array);
           }else{
               JSONArray array = (JSONArray)obj;
               JSONObject p = new JSONObject();
               p.put("messageType", messageType);
               p.put("instance",instance);
               p.put("messageNo",messageNo);
               p.put("serialNo", serialNo);
               array.add(p);
               request.getSession().setAttribute("temp.cart.policies", array);
           }
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
