/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository;

import com.ambimmort.u.uc.repository.bean.MessageNoPoolBean;
import com.ambimmort.u.uc.repository.bean.PolicyBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationLogBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.h2.store.fs.FileUtils;
import org.opensaml.xml.util.Base64;

/**
 *
 * @author 定巍
 */
public class UcPolicyRepository {

    private long createTime;
    private RepositoryEntry entry;
    private static String dir = "repo";
    private JdbcPooledConnectionSource connectionSource = null;
    private Dao<MessageNoPoolBean, Long> messageNoDao = null;
    private Dao<PolicyBean, Long> svnFileDao = null;
    private Dao<RepositoryOperationLogBean, Long> svnLogDao = null;
    private UcRepositoryKit svnKit = null;

    private static Map<RepositoryEntry, UcPolicyRepository> instances = new HashMap<RepositoryEntry, UcPolicyRepository>();

    public static UcPolicyRepository getInstance(RepositoryEntry entry, boolean initialize) {
        if (instances.containsKey(entry)) {
            return instances.get(entry);
        } else {
            UcPolicyRepository repo = new UcPolicyRepository(entry, initialize);
            instances.put(entry, repo);
            return repo;
        }
    }

    public static List<UcPolicyRepository> getAllRepositorys() {
        List<UcPolicyRepository> list = new ArrayList<UcPolicyRepository>();
        for (UcPolicyRepository i : instances.values()) {
            list.add(i);
        }
        return list;
    }

    public static void init() {
        File baseDirFile = new File(dir);
        if (!baseDirFile.exists()) {
            baseDirFile.mkdirs();
        }
        for (File typeDir : baseDirFile.listFiles()) {
            String typeName = typeDir.getName();
            if (typeDir.isDirectory()) {
                for (File instanceFile : typeDir.listFiles()) {
                    UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry(dir, typeName, instanceFile.getName()), false);
                }
            }
        }

    }

    public UcPolicyRepository(RepositoryEntry entry, boolean initialize) {
        this.entry = entry;
        this.createTime = System.currentTimeMillis();

        File baseDirFile = new File(entry.baseDir);
        baseDirFile.mkdirs();
        File repoTypeDirFile = new File(baseDirFile, entry.getMessageType());
        repoTypeDirFile.mkdirs();
        File repoDirFile = new File(repoTypeDirFile, entry.instanceName);
        repoDirFile.mkdirs();
        if (initialize) {
            for (File f : repoDirFile.listFiles()) {
                System.out.println(f);
                FileUtils.delete(f.getAbsolutePath());
            }
        }

        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:h2:" + repoDirFile.getAbsolutePath() + "/" + entry.instanceName);
            connectionSource.setMaxConnectionAgeMillis(5 * 60 * 1000);
            // change the check-every milliseconds from 30 seconds to 60
            connectionSource.setCheckConnectionsEveryMillis(60 * 1000);
            connectionSource.setTestBeforeGet(true);
            initSVNMessageNoPool(initialize);
            initSVNFile(initialize);

            initSVNLog(initialize);

            svnKit = new UcRepositoryKit(this);

        } catch (SQLException ex) {
            Logger.getLogger(UcPolicyRepository.class.getName()).log(Level.ERROR, null, ex);
        }
    }

    public Dao<MessageNoPoolBean, Long> getMessageNoDao() {
        return messageNoDao;
    }

    public void setMessageNoDao(Dao<MessageNoPoolBean, Long> messageNoDao) {
        this.messageNoDao = messageNoDao;
    }

    public Dao<PolicyBean, Long> getSvnFileDao() {
        return svnFileDao;
    }

    public void setSvnFileDao(Dao<PolicyBean, Long> svnFileDao) {
        this.svnFileDao = svnFileDao;
    }

    public Dao<RepositoryOperationLogBean, Long> getSvnLogDao() {
        return svnLogDao;
    }

    public void setSvnLogDao(Dao<RepositoryOperationLogBean, Long> svnLogDao) {
        this.svnLogDao = svnLogDao;
    }

    public UcRepositoryKit getSvnKit() {
        return svnKit;
    }

    public void setSvnKit(UcRepositoryKit svnKit) {
        this.svnKit = svnKit;
    }

    private void initSVNMessageNoPool(boolean initialize) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MessageNoPoolBean.class);
            messageNoDao = DaoManager.createDao(connectionSource, MessageNoPoolBean.class);
            if (initialize) {
                messageNoDao.deleteBuilder().delete();
                messageNoDao.callBatchTasks(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        StringBuilder sb = new StringBuilder();
                        sb.append("INSERT INTO `MESSAGENOPOOL` (`no` ) VALUES ");
                        for (int i = 0; i < 1; i++) {
                            sb.append("(").append(i).append("),");
                            if (i % 2000 == 0) {
                                sb.deleteCharAt(sb.length() - 1);
                                messageNoDao.executeRawNoArgs(sb.toString());
                                sb = new StringBuilder();
                                sb.append("INSERT INTO `MESSAGENOPOOL` (`no` ) VALUES ");
                            }
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        messageNoDao.executeRawNoArgs(sb.toString());
                        return null;
                    }
                });
            }

        } catch (Exception ex) {
            Logger.getLogger(UcPolicyRepository.class.getName()).log(Level.ERROR, null, ex);
        } finally {

        }
    }

    private void initSVNLog(boolean initialize) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, RepositoryOperationLogBean.class);
            svnLogDao = DaoManager.createDao(connectionSource, RepositoryOperationLogBean.class);
            if (initialize) {
                svnLogDao.deleteBuilder().delete();
            }
        } catch (Exception ex) {
            Logger.getLogger(UcPolicyRepository.class.getName()).log(Level.ERROR, null, ex);
        } finally {
        }
    }

    private void initSVNFile(boolean initialize) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, PolicyBean.class);
            svnFileDao = DaoManager.createDao(connectionSource, PolicyBean.class);
            if (initialize) {
                svnFileDao.deleteBuilder().delete();
            }
        } catch (Exception ex) {
            Logger.getLogger(UcPolicyRepository.class.getName()).log(Level.ERROR, null, ex);
        } finally {
        }
    }

    public void close() {
        try {
            connectionSource.close();
            instances.remove(this.entry);
        } catch (SQLException ex) {
            Logger.getLogger(UcPolicyRepository.class.getName()).log(Level.ERROR, null, ex);
        }
    }

    public static RepositoryEntry repositoryEntry(String baseDir, String messageType, String instanceName) {
        return new RepositoryEntry(baseDir, instanceName, messageType);
    }

    public static class RepositoryEntry {

        private String instanceName;
        private String messageType;
        private String baseDir = "./";

        public RepositoryEntry(String baseDir, String instanceName, String messageType) {
            this.instanceName = instanceName;
            this.messageType = messageType;
            this.baseDir = baseDir;
        }

        public RepositoryEntry(String instanceName, String messageType) {
            this.instanceName = instanceName;
            this.messageType = messageType;
        }

        public RepositoryEntry() {
        }

        public String getBaseDir() {
            return baseDir;
        }

        public void setBaseDir(String baseDir) {
            this.baseDir = baseDir;
        }

        public String getInstanceName() {
            return instanceName;
        }

        public void setInstanceName(String instanceName) {
            this.instanceName = instanceName;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.instanceName);
            hash = 59 * hash + Objects.hashCode(this.messageType);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final RepositoryEntry other = (RepositoryEntry) obj;
            if (!Objects.equals(this.instanceName, other.instanceName)) {
                return false;
            }
            if (!Objects.equals(this.messageType, other.messageType)) {
                return false;
            }
            return true;
        }
    }

    public long getHEAD() {
        try {
            return this.getSvnLogDao().queryRawValue("select MAX(id) from SVNLOG");
        } catch (SQLException ex) {
            Logger.getLogger(UcPolicyRepository.class.getName()).log(Level.ERROR, null, ex);
            return 0l;
        }
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public RepositoryEntry getEntry() {
        return entry;
    }

    public void setEntry(RepositoryEntry entry) {
        this.entry = entry;
    }

    public JdbcPooledConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public void setConnectionSource(JdbcPooledConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        FileAppender fa = new FileAppender();
        fa.setName("FileLogger");
        fa.setFile("mylog.log");
        fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        fa.setThreshold(Level.ERROR);
        fa.setAppend(true);
        fa.activateOptions();
        Logger.getRootLogger().addAppender(fa);

        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", "0x01", "GreeNet-1"), false);
        UcRepositoryKit kit = repo.getSvnKit();
       
        

        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<msg_0x01 messageNo=\"2\" messageSequenceNo=\"0\" messageSerialNo=\"0\"\n" +
"	xmlns=\"http://www.ambimmort.com/msg_0x01\" xmlns:u=\"http://www.ambimmort.com/UType\"\n" +
"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
"	xsi:schemaLocation=\"http://www.ambimmort.com/msg_0x01 msg_0x01.xsd \">\n" +
"	<PacketType>1</PacketType>\n" +
"	<PacketSubtype>2</PacketSubtype>\n" +
"	<R_StartTime>0</R_StartTime>\n" +
"	<R_EndTime>0</R_EndTime>\n" +
"	<R_Freq>1</R_Freq>\n" +
"	<R_DestIP>\n" +
"		<u:IPType>IPv4</u:IPType>\n" +
"		<u:IPv4Address>111.228.250.171</u:IPv4Address>\n" +
"	</R_DestIP>\n" +
"	<R_DestPort>60002</R_DestPort>\n" +
"	<R_Method>1</R_Method>\n" +
"	<UserName></UserName>\n" +
"	<Password></Password>\n" +
"	<MessageSerialNo>1</MessageSerialNo>\n" +
"</msg_0x01>\n" +
"";

        RepositoryOperationLogBean log1 = kit.create(Base64.encodeBytes(s.getBytes("utf-8")),"ddd");
        System.out.println(log1);
//        RepositoryOperationLogBean log1 = kit.create(Base64.encodeBytes("abc".getBytes("utf-8")));
//
//        kit.update(log1.getSvnFile().getMessageNo(), Base64.encodeBytes("abd".getBytes("utf-8")));
//
//        RepositoryOperationLogBean log = kit.create(Base64.encodeBytes("ddd".getBytes("utf-8")));
//
//        kit.update(log.getSvnFile().getMessageNo(),Base64.encodeBytes("ccc".getBytes("utf-8")));
//
//        log = kit.create(Base64.encodeBytes("recreate1".getBytes("utf-8")));
//        log = kit.create(Base64.encodeBytes("recreate2".getBytes("utf-8")));
//
//        kit.dumpSVNFile();
//        kit.dumpSVNLog();
//
//        for (PolicyBean f : kit.checkOutHEAD()) {
//            System.out.println(f);
//        }
//
//        System.out.println(repo.getHEAD());
//        Map<Integer, RepositoryOperationBean> acks = kit.update(2, repo.getHEAD());
//        for (Integer f : acks.keySet()) {
//            switch (f) {
//                case 0:
//                    System.out.println("ADD " + acks.get(f));
//                    break;
//                case 1:
//                    System.out.println("UPDATE " + acks.get(f));
//                    break;
//                case 2:
//                    System.out.println("DELETE " + acks.get(f));
//                    break;
//                default:
//                    break;
//            }
//        }
//        System.out.println("--------------------");
//        acks = kit.update(3, repo.getHEAD());
//        for (Integer f : acks.keySet()) {
//            switch (f) {
//                case 0:
//                    System.out.println("ADD " + acks.get(f));
//                    break;
//                case 1:
//                    System.out.println("UPDATE " + acks.get(f));
//                    break;
//                case 2:
//                    System.out.println("DELETE " + acks.get(f));
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        System.out.println("=====================");
////        System.out.println(kit.getMessageWithLog(0));
//        System.out.println(kit.getMessageWithLog(1));
//        System.out.println(kit.getMessageWithLog(2));
//        System.out.println("=====================");
//        for (RepositoryOperationLogBean vlog : kit.getMessageLogs(1)) {
//            System.out.println(vlog);
//        }
        repo.close();
    }

}
