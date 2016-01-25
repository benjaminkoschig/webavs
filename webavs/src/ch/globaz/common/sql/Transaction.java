package ch.globaz.common.sql;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import ch.globaz.common.business.exceptions.CommonTechnicalException;

final class Transaction {
    private boolean newTransactionCreated = false;
    private BTransaction transaction;

    public Transaction(BSession session) {
        super();
        resolveTransaction(session);
    }

    private BTransaction resolveTransaction(BSession session) {
        if (session == null) {
            throw new CommonTechnicalException("Unable to proceed to the query execution with a null session");
        }

        transaction = session.getCurrentThreadTransaction();
        if (transaction == null) {
            try {
                // on récupère la connexion
                transaction = (BTransaction) session.newTransaction();
            } catch (Exception e) {
                throw new CommonTechnicalException("An error happened while trying to get the transaction", e);
            }
        }

        if (transaction.getConnection() == null) {
            try {
                transaction.openTransaction();
                transaction.isOpened();
                newTransactionCreated = true;
            } catch (Exception e) {
                closeOpenedTransaction();
                throw new CommonTechnicalException("An error happened while trying to open a new JDBC connection", e);
            }
        }
        return transaction;
    }

    /**
     * Cette méthode permet de clôturer une transaction dans le cas ou l'on a créé un nouvelle
     * 
     * @param transaction
     */
    void closeOpenedTransaction() {
        if (newTransactionCreated) {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    transaction.rollback();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CommonTechnicalException("an error happened while trying to rollback errors");
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    throw new CommonTechnicalException("an error happened while trying to close transaction");
                }
            }

        }
    }

    public BTransaction getTransaction() {
        return transaction;
    }

}
