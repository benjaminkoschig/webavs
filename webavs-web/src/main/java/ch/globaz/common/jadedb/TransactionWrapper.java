package ch.globaz.common.jadedb;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.io.Closeable;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionWrapper implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionWrapper.class);
    private BTransaction btransaction;
    private boolean autonome = false;
    private JadeBusinessMessage[] logMessages;
    private String errorsInSession;
    private String warningsInSession;
    private BSession session;

    public TransactionWrapper() {
        resolveTransaction(BSessionUtil.getSessionFromThreadContext());
    }

    public TransactionWrapper(BSession session) {
        resolveTransaction(session);
    }

    /**
     * Le but de cette fonction est de permettre d'avoir une transaction autonome.
     * 
     * @param session
     * @return une nouvelle transaction
     */
    public static TransactionWrapper forforceCommit(BSession session) {
        JadeBusinessMessage[] logs = JadeThread.logMessages();
        String errors = session.getErrors().toString();
        String warnings = session.getWarnings().toString();
        JadeThread.logClear();
        TransactionWrapper transaction = new TransactionWrapper(session);
        transaction.autonome = true;
        transaction.errorsInSession = errors;
        transaction.warningsInSession = warnings;
        transaction.logMessages = logs;
        transaction.session = session;
        return transaction;
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

    public void forceCommit() {
        try {
            btransaction.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to force the commit transaction", e);

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
            if (autonome) {
                if (logMessages != null) {
                    for (JadeBusinessMessage message : logMessages) {
                        if (JadeBusinessMessageLevels.ERROR == (message.getLevel())) {
                            JadeThread.logError(message.getSource(), message.getMessageId(), message.getParameters());
                        } else if (JadeBusinessMessageLevels.WARN == (message.getLevel())) {
                            JadeThread.logWarn(message.getSource(), message.getMessageId(), message.getParameters());
                        } else if (JadeBusinessMessageLevels.INFO == (message.getLevel())) {
                            JadeThread.logInfo(message.getSource(), message.getMessageId(), message.getParameters());
                        }
                    }
                }
                if (session != null) {
                    // Pas top car on risque d'avoir des erreur ou warining à double dans le JadeThreadlog. Voir la
                    // fonciton addError ou addWarning
                    if (errorsInSession != null && !errorsInSession.isEmpty()) {
                        session.addError(errorsInSession);
                    }
                    if (warningsInSession != null && !warningsInSession.isEmpty()) {
                        session.addWarning(warningsInSession);
                    }
                }
            }
        }
    }

    public BTransaction getTransaction() {
        return btransaction;
    }
}
