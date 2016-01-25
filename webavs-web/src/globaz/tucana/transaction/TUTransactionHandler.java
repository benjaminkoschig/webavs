package globaz.tucana.transaction;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.common.JadeCodingUtil;

/**
 * Generateur de transaction
 * 
 * @author fgo date de création : 7 juin 06
 * @version : version 1.0
 * 
 */
public abstract class TUTransactionHandler {
    private boolean hasOpenedTransaction = false;
    private Object model = null;
    private BSession session = null;
    private BTransaction transaction = null;

    /**
     * Constructor for AITransactionHandler
     */
    public TUTransactionHandler(BSession _session) {
        super();
        session = _session;
    }

    /**
     * Constructor for AITransactionHandler
     */
    public TUTransactionHandler(BSession _session, Object _model) {
        super();
        session = _session;
        model = _model;
    }

    /**
     * Fermeture de la transaction
     * 
     * @param transaction
     */
    private void closeTransaction(BTransaction transaction) {
        if (hasOpenedTransaction) {
            if ((transaction != null) && (transaction.isOpened())) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    JadeCodingUtil.catchException(this, "closeTransaction", e);
                }
            }
        }
    }

    /**
     * Exécution de la transaction
     * 
     * @throws Exception
     */
    public final void execute() throws Exception {
        try {
            openTransaction();
            handleBean(transaction);
        } finally {
            closeTransaction(transaction);
        }
    }

    /**
     * Récupération du model
     * 
     * @return
     */
    public Object getModel() {
        return model;
    }

    /**
     * Récupération de la session
     * 
     * @return
     */
    public BSession getSession() {
        return session;
    }

    /**
     * Génération du bean
     * 
     * @param transaction
     */
    protected abstract void handleBean(BTransaction transaction) throws Exception;

    /**
     * Ouverture de la transaction
     * 
     * @return
     */
    private void openTransaction() throws Exception {
        transaction = session.getCurrentThreadTransaction();
        hasOpenedTransaction = false;
        if (transaction == null) {
            transaction = new BTransaction(session);
            transaction.openTransaction();
            hasOpenedTransaction = true;
        }
    }

}
