/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.web.logics.dpi;

import com.ambimmort.uc.zfserver.bean.DPIConfiguredPolicyRepositoryBean;
import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.web.database.ZFDaoManager;
import com.j256.ormlite.dao.Dao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.apache.geronimo.mail.util.Base64;

/**
 *
 * @author 定巍
 */
@WebServlet(name = "SaveDPIConfiguredPolicyRepos", urlPatterns = {"/logic/dpi/saveDPIConfiguredPolicyRepos"})
public class SaveDPIConfiguredPolicyRepos extends HttpServlet {

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
            try {
                String devName = request.getParameter("devName");
                String config = request.getParameter("config");
                if(devName==null){
                    out.print("[]");
                    return;
                }
                
                Dao<DPIConfiguredPolicyRepositoryBean, String> dao = ZFDaoManager.getInstance().getDao(DPIConfiguredPolicyRepositoryBean.class);
                DPIConfiguredPolicyRepositoryBean bean = dao.queryForId(devName);
                config = new String(Base64.decode(config),"utf-8");
                bean.setValue(config);
                dao.update(bean);
                out.print("{\"code\":0}");
            } catch (SQLException ex) {
                Logger.getLogger(ListAllDPI.class.getName()).log(Level.SEVERE, null, ex);
                 out.print("{\"code\":0,\"explain\":\"Error\"}");
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
