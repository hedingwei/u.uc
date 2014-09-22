/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.vysper;

import com.ambimmort.uc.zfserver.bean.ZFAgentBean;
import com.ambimmort.uc.zfserver.component.ZFComponent;
import com.ambimmort.uc.zfserver.component.database.MyDaoManager;
import com.ambimmort.uc.zfserver.component.messageDriven.EventHandler;
import java.io.File;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.vysper.mina.TCPEndpoint;
import org.apache.vysper.storage.StorageProviderRegistry;
import org.apache.vysper.storage.inmemory.MemoryStorageProviderRegistry;
import org.apache.vysper.xmpp.addressing.EntityImpl;
import org.apache.vysper.xmpp.authorization.AccountManagement;
import org.apache.vysper.xmpp.modules.extension.xep0049_privatedata.PrivateDataModule;
import org.apache.vysper.xmpp.modules.extension.xep0054_vcardtemp.VcardTempModule;
import org.apache.vysper.xmpp.modules.extension.xep0119_xmppping.XmppPingModule;
import org.apache.vysper.xmpp.modules.extension.xep0202_entity_time.EntityTimeModule;
import org.apache.vysper.xmpp.modules.roster.RosterModule;
import org.apache.vysper.xmpp.server.XMPPServer;
import org.jivesoftware.smack.SmackConfiguration;

/**
 *
 * @author 定巍
 */
public class VysperComponent extends ZFComponent implements EventHandler {

    public static final String EVENT_RELOAD = "VysperComponent.events.reload";

    private static VysperComponent instance = null;

    private XMPPServer server = null;

    private JSONObject states = new JSONObject();

    private VysperComponent() {
        server = new XMPPServer("ambimmort.com");

    }

    public static VysperComponent getInstance() {
        if (instance == null) {
            instance = new VysperComponent();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "MessageDrivenEngine";
    }

    @Override
    public JSONObject getStates() {
        return states;
    }

    @Override
    protected void prestart() throws Throwable {
        StorageProviderRegistry providerRegistry = new MemoryStorageProviderRegistry();
        server.setStorageProviderRegistry(providerRegistry);
        final AccountManagement accountManagement = (AccountManagement) providerRegistry.retrieve(AccountManagement.class);

        for (ZFAgentBean bean : MyDaoManager.getInstance().getDao(ZFAgentBean.class).queryForAll()) {
            if (!accountManagement.verifyAccountExists(EntityImpl.parse(bean.getName()))) {
                accountManagement.addUser(EntityImpl.parse(bean.getName()), bean.getPassword());
            }
        }
    }

    @Override
    protected void poststart() throws Throwable {

        server.addEndpoint(new TCPEndpoint());
//        server.getServerRuntimeContext().get
        server.setTLSCertificateInfo(new File("src/main/resources/bogus_mina_tls.cert"), "boguspw");
        server.start();

        server.addModule(new EntityTimeModule());
        server.addModule(new XmppPingModule());
        server.addModule(new RosterModule());
        server.addModule(new PrivateDataModule());
        
        Commander.getInstance().login();
    }

    @Override
    protected void refreshState() {
//        try {
//            states.put("ActiveCount", service.getActiveCount());
//            states.put("CompletedTaskCount", service.getCompletedTaskCount());
//            states.put("CorePoolSize", service.getCorePoolSize());
//            states.put("LargestPoolSize", service.getLargestPoolSize());
//            states.put("MaximumPoolSize", service.getMaximumPoolSize());
//            states.put("PoolSize", service.getPoolSize());
//            states.put("TaskCount", service.getTaskCount());
//            ZFComponentBean bean = new ZFComponentBean();
//            bean.setName(getName());
//            bean.setStates(states.toString(4));
//            ZFComponentBeanDao.getInstance().getZfComponentDao().createOrUpdate(bean);
//        } catch(Exception e){
//            e.printStackTrace();
//        }

    }

    @Override
    public void onEvent(String name, Map<String, Object> args) throws Throwable {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
