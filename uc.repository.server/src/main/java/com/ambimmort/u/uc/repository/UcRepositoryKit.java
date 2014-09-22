/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ambimmort.u.uc.repository.bean.MessageNoPoolBean;
import com.ambimmort.u.uc.repository.bean.PolicyBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationLogBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

/**
 *
 * @author 定巍
 */
public class UcRepositoryKit {

    private UcPolicyRepository repository = null;

    public UcRepositoryKit(UcPolicyRepository repository) {
        this.repository = repository;
    }

    public RepositoryOperationLogBean create(final String content, final String comment) {
        try {

            final Dao<PolicyBean, Long> svnFileDao = repository.getSvnFileDao();
            final Dao<RepositoryOperationLogBean, Long> svnLogDao = repository.getSvnLogDao();

            final PolicyBean file = new PolicyBean();
            file.setContent(content);
            file.setMessageNo(getMessageNo());
            file.setIsDeleted(false);
            file.setIsNewest(true);
            file.setComment(comment);
            svnFileDao.create(file);

            final RepositoryOperationLogBean log = new RepositoryOperationLogBean();
            log.setCreateTime(System.currentTimeMillis());
            log.setOperation(RepositoryOperationLogBean.ADD);
            log.setSvnFile(file);
            svnLogDao.create(log);

            return log;
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RepositoryOperationLogBean update(final int messageNo, final String content, final String comment) {
        try {
            final Dao<PolicyBean, Long> svnFileDao = repository.getSvnFileDao();
            final Dao<RepositoryOperationLogBean, Long> svnLogDao = repository.getSvnLogDao();
            UpdateBuilder<PolicyBean, Long> ub = svnFileDao.updateBuilder();
            ub.updateColumnValue("isNewest", false);
            ub.where().eq("messageNo", messageNo);
            ub.update();
            final PolicyBean file = new PolicyBean();
            file.setContent(content);
            file.setMessageNo(messageNo);
            file.setIsNewest(true);
            file.setComment(comment);
            final RepositoryOperationLogBean log = new RepositoryOperationLogBean();
            log.setCreateTime(System.currentTimeMillis());
            log.setOperation(RepositoryOperationLogBean.UPDATE);
            log.setSvnFile(file);
            TransactionManager.callInTransaction(repository.getConnectionSource(),
                    new Callable<RepositoryOperationLogBean>() {
                        public RepositoryOperationLogBean call() throws Exception {
                            svnFileDao.create(file);
                            svnLogDao.create(log);
                            return log;
                        }
                    });

            return log;

        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RepositoryOperationLogBean delete(final int messageNo) {
        try {
            final Dao<PolicyBean, Long> svnFileDao = repository.getSvnFileDao();
            final Dao<RepositoryOperationLogBean, Long> svnLogDao = repository.getSvnLogDao();
            final Dao<MessageNoPoolBean, Long> messageNoDao = repository.getMessageNoDao();
            RepositoryOperationLogBean log1 = TransactionManager.callInTransaction(repository.getConnectionSource(),
                    new Callable<RepositoryOperationLogBean>() {
                        public RepositoryOperationLogBean call() throws Exception {
                            QueryBuilder<PolicyBean, Long> qb = svnFileDao.queryBuilder();
                            qb.orderBy("id", false);
                            qb.where().eq("messageNo", messageNo);
                            PolicyBean file = qb.queryForFirst();
                            if (file == null) {
                                return null;
                            } else {
                                UpdateBuilder<PolicyBean, Long> ub = svnFileDao.updateBuilder();
                                ub.updateColumnValue("isDeleted", true).where().eq("messageNo", messageNo).and().eq("isDeleted", false);
                                ub.updateColumnValue("isNewest", false);
                                ub.update();
                                MessageNoPoolBean msg = new MessageNoPoolBean();
                                msg.setNo(messageNo);
                                messageNoDao.createIfNotExists(msg);
                                final RepositoryOperationLogBean log = new RepositoryOperationLogBean();
                                log.setCreateTime(System.currentTimeMillis());
                                log.setOperation(RepositoryOperationLogBean.DELETE);
                                log.setSvnFile(file);
                                svnLogDao.create(log);
                                returnMessageNo(file.getMessageNo());
                                return log;
                            }

                        }
                    });
            return log1;
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public PolicyBean get(final int messageNo) {
        try {
            final Dao<PolicyBean, Long> svnFileDao = repository.getSvnFileDao();

            QueryBuilder<PolicyBean, Long> qb = svnFileDao.queryBuilder();
            qb.orderBy("id", false);
            qb.where().eq("messageNo", messageNo);
            return qb.queryForFirst();
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public RepositoryOperationLogBean getMessageWithLog(final int messageNo) {
        try {
            final Dao<PolicyBean, Long> svnFileDao = repository.getSvnFileDao();
            final Dao<RepositoryOperationLogBean, Long> svnLogDao = repository.getSvnLogDao();
            QueryBuilder<RepositoryOperationLogBean, Long> logQB = svnLogDao.queryBuilder();
            QueryBuilder<PolicyBean, Long> fileQB = svnFileDao.queryBuilder();
            fileQB.where().eq("messageNo", messageNo);
            fileQB.orderBy("id", false);
            logQB.join(fileQB);
            return logQB.queryForFirst();
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public long getHeadVersionNumber() {
        try {
            return repository.getSvnLogDao().queryRawValue("select MAX(id) from SVNLOG");
        } catch (SQLException ex) {
            org.apache.log4j.Logger.getLogger(UcPolicyRepository.class.getName()).log(org.apache.log4j.Level.ERROR, null, ex);
            return 0l;
        }
    }

    public RepositoryOperationLogBean getHeadVersion() {
        try {
            final Dao<RepositoryOperationLogBean, Long> svnLogDao = repository.getSvnLogDao();
            QueryBuilder<RepositoryOperationLogBean, Long> logQB = svnLogDao.queryBuilder();
            logQB.orderBy("id", false);

            return logQB.queryForFirst();
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<RepositoryOperationLogBean> getMessageLogs(final int messageNo) {
        try {
            final Dao<PolicyBean, Long> svnFileDao = repository.getSvnFileDao();
            final Dao<RepositoryOperationLogBean, Long> svnLogDao = repository.getSvnLogDao();
            QueryBuilder<RepositoryOperationLogBean, Long> logQB = svnLogDao.queryBuilder();
            logQB.orderBy("id", false);
            QueryBuilder<PolicyBean, Long> fileQB = svnFileDao.queryBuilder();
            fileQB.where().eq("messageNo", messageNo);
            logQB.join(fileQB);
            return logQB.query();
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList();
        }
    }

    public void dumpSVNFile() {
        try {
            final Dao<PolicyBean, Long> svnFileDao = repository.getSvnFileDao();
            for (PolicyBean f : svnFileDao.queryForAll()) {
                System.out.println(f);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dumpSVNLog() {
        try {

            final Dao<RepositoryOperationLogBean, Long> svnLogDao = repository.getSvnLogDao();
            for (RepositoryOperationLogBean f : svnLogDao.queryForAll()) {
                System.out.println(f);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dumpMessageNoPool() {
        try {

            final Dao<MessageNoPoolBean, Long> messageNoDao = repository.getMessageNoDao();
            for (MessageNoPoolBean f : messageNoDao.queryForAll()) {
                System.out.println(f);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void returnMessageNo(int messageNo) {
        try {
            try {
                Dao<MessageNoPoolBean, Long> dao = repository.getMessageNoDao();
                MessageNoPoolBean msg = new MessageNoPoolBean();
                msg.setNo(messageNo);
                dao.delete(msg);
//                dao.createIfNotExists(msg);
            } finally {

            }
        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getMessageNo() throws SQLException {
        try {
            try {
                final Dao<MessageNoPoolBean, Long> dao = repository.getMessageNoDao();
                long tmp = dao.queryRawValue("select min(65535-no) as tno from MESSAGENOPOOL");
                if (tmp == 65535) {
                    MessageNoPoolBean msg = new MessageNoPoolBean();
                    msg.setNo(1);
                    dao.createIfNotExists(msg);
                    return msg.getNo();
                } else {
                    long max = dao.queryRawValue("select max(no) as tno from MESSAGENOPOOL");
                    long tt = 65535 - tmp;
                    if (tt < max) {
                        MessageNoPoolBean msg = new MessageNoPoolBean();
                        msg.setNo((int) tt);
                        dao.createIfNotExists(msg);
                        return msg.getNo();
                    } else {
                        MessageNoPoolBean msg = new MessageNoPoolBean();
                        msg.setNo((int) (max + 1));
                        dao.createIfNotExists(msg);
                        return (int) max;
                    }
                }

            } finally {
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public TreeMap<Integer, RepositoryOperationBean> update(long startVersion, long endVersion) {
        final Dao<RepositoryOperationLogBean, Long> dao = repository.getSvnLogDao();
        QueryBuilder<RepositoryOperationLogBean, Long> qb = dao.queryBuilder();
        try {
            qb.orderBy("id", true).where().le("id", endVersion).and().gt("id", startVersion);
            List<RepositoryOperationLogBean> logs = qb.query();
            Map<Integer, Vector<RepositoryOperationLogBean>> map = new TreeMap<Integer, Vector<RepositoryOperationLogBean>>();
            for (RepositoryOperationLogBean log : logs) {
                if (map.containsKey(log.getSvnFile().getMessageNo())) {
                    map.get(log.getSvnFile().getMessageNo()).add(log);
                } else {
                    Vector<RepositoryOperationLogBean> operationStack = new Vector<RepositoryOperationLogBean>();
                    operationStack.add(log);
                    map.put(log.getSvnFile().getMessageNo(), operationStack);
                }
            }

            TreeMap<Integer, RepositoryOperationBean> acks = new TreeMap<Integer, RepositoryOperationBean>();
            Vector<RepositoryOperationLogBean> opr = null;
            RepositoryOperationLogBean tmp = null;
            RepositoryOperationLogBean first = null;
            RepositoryOperationLogBean last = null;
            for (Integer num : map.keySet()) {
                opr = map.get(num);
                if (opr.isEmpty()) {
                    continue;
                } else if (opr.size() == 1) {
                    RepositoryOperationBean up = new RepositoryOperationBean();
                    tmp = opr.firstElement();
                    up.setMessageNo(tmp.getSvnFile().getMessageNo());
                    up.setOperation(tmp.getOperation());
                    up.setContent(tmp.getSvnFile().getContent());
                    acks.put(num, up);
                } else if (opr.size() > 1) {
                    first = opr.firstElement();
                    last = opr.lastElement();
                    if (first.getOperation() == RepositoryOperationLogBean.DELETE && last.getOperation() == RepositoryOperationLogBean.ADD) {
                        RepositoryOperationBean up = new RepositoryOperationBean();
                        up.setMessageNo(last.getSvnFile().getMessageNo());
                        up.setOperation(RepositoryOperationLogBean.UPDATE);
                        up.setContent(last.getSvnFile().getContent());
                        acks.put(num, up);
                    }
                    if (first.getOperation() == RepositoryOperationLogBean.DELETE && last.getOperation() == RepositoryOperationLogBean.UPDATE) {
                        RepositoryOperationBean up = new RepositoryOperationBean();
                        up.setMessageNo(last.getSvnFile().getMessageNo());
                        up.setOperation(RepositoryOperationLogBean.UPDATE);
                        up.setContent(last.getSvnFile().getContent());
                        acks.put(num, up);
                    } else if (first.getOperation() == RepositoryOperationLogBean.ADD && last.getOperation() == RepositoryOperationLogBean.UPDATE) {
                        RepositoryOperationBean up = new RepositoryOperationBean();
                        up.setMessageNo(last.getSvnFile().getMessageNo());
                        up.setOperation(RepositoryOperationLogBean.UPDATE);
                        up.setContent(last.getSvnFile().getContent());
                        acks.put(num, up);
                    } else if (first.getOperation() == RepositoryOperationLogBean.ADD && last.getOperation() == RepositoryOperationLogBean.DELETE) {
                        continue;
                    } else if (first.getOperation() == RepositoryOperationLogBean.UPDATE && last.getOperation() == RepositoryOperationLogBean.DELETE) {
                        RepositoryOperationBean up = new RepositoryOperationBean();
                        up.setMessageNo(last.getSvnFile().getMessageNo());
                        up.setOperation(RepositoryOperationLogBean.DELETE);
                        up.setContent(last.getSvnFile().getContent());
                        acks.put(num, up);
                    }
                }
            }

            return acks;

        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return new TreeMap<Integer, RepositoryOperationBean>();
        } finally {
		}
    }

    public List<PolicyBean> checkOutHEAD() {
        try {
            final Dao<PolicyBean, Long> dao = repository.getSvnFileDao();

            QueryBuilder<PolicyBean, Long> qb = dao.queryBuilder();
            
            qb.groupBy("messageNo").groupBy("id").where().eq("isDeleted", "false").and().eq("isNewest", "true");
            
            return qb.query();

        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<PolicyBean>();
        } finally {
        }
    }
    
    public long checkOutHEADCount() {
        try {
            final Dao<PolicyBean, Long> dao = repository.getSvnFileDao();

            QueryBuilder<PolicyBean, Long> qb = dao.queryBuilder();
            
            long count = qb.groupBy("messageNo").groupBy("id").where().eq("isDeleted", "false").and().eq("isNewest", "true").query().size();
            
            return count;

        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
        }
    }
    
    public List<PolicyBean> checkOutHEAD(int pageCount, int page) {
        try {
            final Dao<PolicyBean, Long> dao = repository.getSvnFileDao();

            QueryBuilder<PolicyBean, Long> qb = dao.queryBuilder();
            qb.offset((long)(page-1)*pageCount);
            qb.limit((long)pageCount);
            qb.groupBy("messageNo").groupBy("id").where().eq("isDeleted", "false").and().eq("isNewest", "true");

            return qb.query();

        } catch (SQLException ex) {
            Logger.getLogger(UcRepositoryKit.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<PolicyBean>();
        } finally {
        }
    }

    public static void main(String[] args) {

//        SVNFileDao.init();
//        SVNFileDao.create("abc");
//        SVNFileDao.create("bcd");
//        SVNFileDao.create("123");
//        SVNFileDao.dumpSVNFile();
//        SVNFileDao.dumpSVNLog();
    }
}
