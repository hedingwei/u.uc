/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.web.logics.dpi;

import com.ambimmort.uc.zfserver.bean.DPIConfiguredPolicyRepositoryBean;
import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.ambimmort.uc.zfserver.web.database.ZFDaoManager;
import com.j256.ormlite.dao.Dao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.cxf.common.util.Base64Utility;

/**
 *
 * @author 定巍
 */
@WebServlet(name = "UpdateDPIEndPoint", urlPatterns = {"/logic/dpi/UpdateDPIEndPoint"})
public class UpdateDPIEndPoint extends HttpServlet {

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
            String devName = request.getParameter("devName");
            String ip = request.getParameter("ip");
            String idcHouseId = request.getParameter("idcHouseId");
            String deploySiteName = request.getParameter("deploySiteName");
            String probeType = request.getParameter("probeType");
            String policy = "{}";
            policy = new String(Base64Utility.decode(request.getParameter("policy")), "utf-8");
            Dao<DPIEndPointBean, String> dao = ZFDaoManager.getInstance().getDao(DPIEndPointBean.class);
            DPIEndPointBean bean = dao.queryBuilder().where().eq("ip", ip).or().eq("devName", devName).queryForFirst();
            if(bean!=null){
                bean.setDevName(devName);
                bean.setDeploySiteName(deploySiteName);
                bean.setIdcHouseId(idcHouseId);
                bean.setIp(ip);
                bean.setName(devName);
                bean.setPort(50000);
                bean.setProbeType(Byte.parseByte(probeType));
                dao.update(bean);
                Dao<DPIConfiguredPolicyRepositoryBean,String> pdao = ZFDaoManager.getInstance().getDao(DPIConfiguredPolicyRepositoryBean.class);
                DPIConfiguredPolicyRepositoryBean pb = new DPIConfiguredPolicyRepositoryBean();
                pb.setName(devName);
                pb.setValue(policy);
                pdao.createOrUpdate(pb);
                out.print("{\"code\":0}");
            }else{
                out.print("{\"code\":1}");
            }
        } catch (Exception ex) {
            Logger.getLogger(AddNewDPIEndPoint.class.getName()).log(Level.SEVERE, null, ex);
            
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
