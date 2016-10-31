package ch.globaz.common.jadedb;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import java.io.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionWrapper implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionWrapper.class);
    private BTransaction btransaction;

    public TransactionWrapper() {
        resolveTransaction(BSessionUtil.getSessionFromThreadContext());
    }

    public TransactionWrapper(BSession session) {
        resolveTransaction(session);
    }

    private BTransaction resolveTransaction(BSession session) {
        if (session == null) {
            throw new NullPointerException("Unable to create a transaction with a null session");
        }
        try {
            btransaction = (BTransaction) session.newTransaction();
        } catch (Exception e) {
            throw new RuntimeException("An error happened while trying to get the transaction", e);
        }

        if (btransaction.getConnection() == null) {
            try {
                btransaction.openTransaction();
                btransaction.isOpened();
            } catch (Exception e) {
                close();
                throw new RuntimeException("An error happened while trying to open a new JDBC connection", e);
            }
        }
        return btransaction;
    }

    public void commit() {
        try {
            btransaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Unable to commit the transaction", e);
        }
    }

    public void rollback() {
        try {
            btransaction.rollback();
        } catch (Exception e) {
            throw new RuntimeException("Unable to rollback the transaction", e);
        }
    }

    @Override
    public void close() {
        try {
            if ((btransaction.hasErrors()) || (btransaction.isRollbackOnly())) {
                rollback();
            } else {
                commit();
            }
        } finally {
            try {
                btransaction.closeTransaction();
            } catch (Exception e) {
                LOG.error("Unable to close the bTransaction", e);
            }
        }
    }

    public BTransaction getTransaction() {
        return btransaction;
    }
}
