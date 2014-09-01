/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.zfserver;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.ucdata.UcData_HeartBeatBean;
import com.ambimmort.uc.zfserver.bean.ucdata.UcData_DPIReportedPolicyVersionBean;
import com.ambimmort.uc.zfserver.bean.ZFComponentBean;
import com.ambimmort.uc.zfserver.bean.ZFPropertyBean;
import com.ambimmort.uc.zfserver.channel.server.ServerChannelHandler;
import com.ambimmort.uc.zfserver.channel.server.WhitelistFilter;
import com.ambimmort.uc.zfserver.codec.UcProtocolCodecFactory;
import com.ambimmort.uc.zfserver.component.ZFComponent;
import com.ambimmort.uc.zfserver.component.database.dao.DPIEndPointBeanDao;
import com.ambimmort.uc.zfserver.component.database.dao.HeartBeatBeanDao;
import com.ambimmort.uc.zfserver.component.database.dao.RuntimeDPIPolicyVersionBeanDao;
import com.ambimmort.uc.zfserver.component.database.dao.ZFComponentBeanDao;
import com.ambimmort.uc.zfserver.component.database.dao.ZFPropertyBeanDao;
import com.ambimmort.uc.zfserver.component.messageDriven.EventHandler;
import com.ambimmort.uc.zfserver.component.messageDriven.MDEComponent;
import com.ambimmort.uc.zfserver.component.monitor.DBRecordAlterationMonitor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;
import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.firewall.ConnectionThrottleFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author 定巍
 */
public class ZFServerComponent extends ZFComponent implements EventHandler {

    private IoAcceptor acceptor = null;
    private WhitelistFilter whiteListFilter = new WhitelistFilter();

    private ConnectionThrottleFilter connectionThrottleFilter = null;

    private static ZFServerComponent instance = null;

    private JSONObject state = new JSONObject();

    public static ZFServerComponent getInstance() {
        if (instance == null) {
            instance = new ZFServerComponent();
        }
        return instance;
    }

    private ZFServerComponent() {
        acceptor = new NioSocketAcceptor();
        connectionThrottleFilter = new ConnectionThrottleFilter();
        try {
            connectionThrottleFilter.setAllowedInterval(Integer.parseInt(ZFPropertyBeanDao.getInstance().getProperty("zfserver.throttle")));
        } catch (SQLException ex) {
            Logger.getLogger(ZFServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        }

        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("whiteList", whiteListFilter);
//        acceptor.getFilterChain().addLast("ConnectionThrottleFilter", connectionThrottleFilter);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UcProtocolCodecFactory()));

        acceptor.setHandler(new ServerChannelHandler());
    }

    public WhitelistFilter getWhiteListFilter() {
        return whiteListFilter;
    }

    public void bind() {
        try {
            acceptor.bind(new InetSocketAddress(Integer.parseInt(ZFPropertyBeanDao.getInstance().getProperty("zfserver.port"))));
        } catch (IOException ex) {
            Logger.getLogger(ZFServerComponent.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("%%%%%%%%%%");
        } catch (SQLException ex) {
            Logger.getLogger(ZFServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void bind(int port) {
        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException ex) {
            Logger.getLogger(ZFServerComponent.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("%%%%%%%%%%");
        }
    }

    public void unbind() {
        acceptor.unbind();
    }

    public static void main(String[] args) {
        try {
            DPIEndPointBeanDao.getInstance();
            DPIEndPointManager.getInstance().refresh();
            ZFServerComponent server = new ZFServerComponent();
            server.bind();
        } catch (SQLException ex) {
            Logger.getLogger(ZFServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "ZFServerComponent";
    }

    @Override
    public JSONObject getStates() {
        return state;
    }

    @Override
    protected void refreshState() {
        try {
            state.put("ActivationTime", acceptor.getActivationTime());
            state.put("ManagedSessionCount", acceptor.getManagedSessionCount());
            state.put("ScheduledWriteBytes", acceptor.getScheduledWriteBytes());
            state.put("ScheduledWriteMessages", acceptor.getScheduledWriteMessages());
            state.put("Statistics", JSONObject.fromObject(acceptor.getStatistics()));
            ZFComponentBean bean = new ZFComponentBean();
            bean.setName(getName());
            bean.setStates(state.toString(4));
            ZFComponentBeanDao.getInstance().getZfComponentDao().createOrUpdate(bean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void poststart() throws Throwable {

    }

    @Override
    protected void prestart() throws Throwable {

        MDEComponent.getInstance().listen(DBRecordAlterationMonitor.EVENT_DATABASE_FIELD_CHANGED, this);
        MDEComponent.getInstance().listen("u.0xc1", this);
        MDEComponent.getInstance().listen("u.0xc2", this);
        bind();
    }

    @Override
    public void onEvent(final String name, final Map<String, Object> args) throws Throwable {
        if (name.equals(DBRecordAlterationMonitor.EVENT_DATABASE_FIELD_CHANGED)) {
            if (args.containsKey("table") && args.containsKey("action")) {
                if (args.get("table").equals(ZFPropertyBean.class.getName())) {
                    if (args.get("action").equals("update")) {
                        ZFPropertyBean bean = (ZFPropertyBean) args.get("new");
                        if (bean.getKey().equals("zfserver.port")) {
                            acceptor.unbind();
                            bind(Integer.parseInt(bean.getValue()));
                        } else if (bean.getKey().equals("zfserver.throttle")) {
                            long thro = Long.parseLong(bean.getValue());
                            connectionThrottleFilter.setAllowedInterval(thro);
                        } else if (bean.getKey().equals("zfserver.log4j.config")) {
                            Properties p = new Properties();
                            p.load(new ByteArrayInputStream(bean.getValue().getBytes("utf-8")));
                            PropertyConfigurator.configure(p);
                        }
                    } else if (args.get("action").equals("insert")) {
                        ZFPropertyBean bean = (ZFPropertyBean) args.get("new");
                        if (bean.getKey().equals("zfserver.log4j.config")) {
                            Properties p = new Properties();
                            if (bean.getValue() != null) {
                                p.load(new ByteArrayInputStream(bean.getValue().getBytes("utf-8")));
                                PropertyConfigurator.configure(p);
                            }

                        }
                    }
                } else if (args.get("table").equals(DPIEndPointBean.class.getName())) {
                    if (args.get("action").equals("insert")) {
                        DPIEndPointBean bean = (DPIEndPointBean) args.get("new");
                        whiteListFilter.allow(InetAddress.getByName(bean.getIp()));

                        DPIEndPoint endPoint = new DPIEndPoint(bean);
                        DPIEndPointManager.getInstance().getEndPoints().put(bean.getIp(), endPoint);

                    } else if (args.get("action").equals("delete")) {
                        DPIEndPointBean bean = (DPIEndPointBean) args.get("obj");
                        whiteListFilter.disallow(InetAddress.getByName(bean.getIp()));
                        if (DPIEndPointManager.getInstance().getEndPoints().containsKey(bean.getIp())) {
                            DPIEndPoint ep = DPIEndPointManager.getInstance().getEndPoints().remove(bean.getIp());
                            ep.getClientChannel().stop();
                            ep.getServerChannel().stopMe();
                        }
                    } else if (args.get("action").equals("update")) {
                        DPIEndPointBean oldBean = (DPIEndPointBean) args.get("old");
                        DPIEndPointBean newBean = (DPIEndPointBean) args.get("new");
                        whiteListFilter.disallow(InetAddress.getByName(oldBean.getIp()));
                        whiteListFilter.allow(InetAddress.getByName(newBean.getIp()));

                        if (DPIEndPointManager.getInstance().getEndPoints().containsKey(oldBean.getIp())) {
                            DPIEndPoint ep = DPIEndPointManager.getInstance().getEndPoints().remove(oldBean.getIp());
                            ep.getClientChannel().stop();
                            ep.getServerChannel().stopMe();
                            DPIEndPoint endPoint = new DPIEndPoint(newBean);
                            DPIEndPointManager.getInstance().getEndPoints().put(newBean.getIp(), endPoint);
                        }
                    }
                }
            }
        } else if ("u.0xc1".equals(name)) {
//            DPIEndPoint endPoint = (DPIEndPoint) args.get("endPoint");
            String dpiName = (String) args.get("name");
            Long time = (Long) args.get("time");
            UcData_HeartBeatBean bean = new UcData_HeartBeatBean();
            byte[] data = (byte[]) args.get("data");
            bean.setName(dpiName);
            bean.setTime(new Date(time));
            bean.setData(data);
            HeartBeatBeanDao.getInstance().getHeartBeatBeanDao().createOrUpdate(bean);
        } else if ("u.0xc2".equals(name)) {
            final DPIEndPoint endPoint = (DPIEndPoint) args.get("endPoint");
            RuntimeDPIPolicyVersionBeanDao.getInstance().getPolicyRepositoryDao().callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    JSONObject dd = (JSONObject) args.get("v");
                    String mt = null;
                    for (Object k : dd.keySet()) {
                        mt = (String) k;
                        UcData_DPIReportedPolicyVersionBean bean = new UcData_DPIReportedPolicyVersionBean();

                        bean.setDevName(endPoint.getDpiEndPointBean().getDevName());

                        bean.setMessageType(mt);
                        bean.setMessageSerialNo(dd.getLong(mt));
                        System.out.println(bean);
                        try {
                            RuntimeDPIPolicyVersionBeanDao.getInstance().getPolicyRepositoryDao().createOrUpdate(bean);
                        } catch (Exception e) {
                            RuntimeDPIPolicyVersionBeanDao.getInstance().getPolicyRepositoryDao().update(bean);
                        }

                    }
                    return null;
                }
            });

        }

        System.out.println("-------" + name + "---------1111111111");
        System.out.println(args);
    }

}
