/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.web.logics.dpi;

import com.ambimmort.uc.zfserver.bean.UcMessageSendingLogBean;
import com.ambimmort.uc.zfserver.web.database.ZFDaoManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.MailDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.cxf.common.util.Base64Utility;

/**
 *
 * @author 定巍
 */
@WebServlet(name = "SearchMessageSendingLog", urlPatterns = {"/logic/dpi/SearchMessageSendingLog"})
public class SearchMessageSendingLog extends HttpServlet {

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
        try (PrintWriter out = response.getWriter()) {
            String devName = request.getParameter("devName");
            String ip = request.getParameter("IP");
            String messageType = request.getParameter("messageType");
            String messageNo = request.getParameter("messageNo");
            String limit = request.getParameter("limit");
            Dao<UcMessageSendingLogBean, Long> dao = ZFDaoManager.getInstance().getDao(UcMessageSendingLogBean.class);

            Map<String, String> eq = new HashMap<String, String>();

            if (devName != null) {
                if ("*".equals(devName.trim()) || devName.trim().isEmpty()) {
                } else {
                    eq.put("devName", devName);
                }
            }

            if (ip != null) {
                if ("*".equals(ip.trim()) || ip.trim().isEmpty()) {
                } else {
                    eq.put("ip", ip);
                }
            }

            if (messageType != null) {
                if ("*".equals(messageType.trim()) || messageType.trim().isEmpty()) {
                } else {
                    eq.put("messageType", messageType);
                }
            }

            if (messageNo != null) {
                if ("*".equals(messageNo.trim()) || messageNo.trim().isEmpty()) {
                } else {
                    eq.put("messageNo", messageNo);
                }
            }

            QueryBuilder<UcMessageSendingLogBean, Long> queryBuilder = dao.queryBuilder().limit(Long.parseLong(limit));

            int j=0;
            int size = eq.size()-1;
            for (String key : eq.keySet()) {
                if(j==size){
                    queryBuilder.where().eq(key, eq.get(key));
                }else{
                   queryBuilder.where().eq(key, eq.get(key)).and(); 
                }
                j++;
            }
    
            List<UcMessageSendingLogBean> beans = queryBuilder.query();
            JSONArray array = new JSONArray();
            for (int i = 0; i < beans.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("id", beans.get(i).getId());
                obj.put("ip", beans.get(i).getIp());
                obj.put("devName", beans.get(i).getDevName());
                obj.put("messageNo", beans.get(i).getMessageNo());
                obj.put("messageSequenceNo", beans.get(i).getMessageSequenceNo());
                obj.put("messageType", beans.get(i).getMessageType());
                obj.put("msg", Base64Utility.encode(beans.get(i).getMsg()));
                obj.put("ack", Base64Utility.encode(beans.get(i).getAck()));
                obj.put("sendTime",sdf.format(beans.get(i).getSendTime()));
                obj.put("ackTime",sdf.format(beans.get(i).getAckTime()));
                array.add(obj);
            }
          
            out.print(array.toString());
        } catch (SQLException ex) {
            Logger.getLogger(SearchMessageSendingLog.class.getName()).log(Level.SEVERE, null, ex);

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
