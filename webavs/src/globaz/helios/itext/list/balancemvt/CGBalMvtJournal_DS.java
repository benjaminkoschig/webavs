package globaz.helios.itext.list.balancemvt;

// FRAM
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGExtendedBalMvtJournal;
import globaz.helios.db.comptes.CGExtendedBalMvtJournalManager;
import globaz.helios.itext.list.utils.CGGeneric_Bean;
import globaz.helios.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRCloneableDataSource;

/**
 * Insert the type's description here. Creation date: (04.07.2003 09:49:45)
 * 
 * @author: Administrator
 */
public class CGBalMvtJournal_DS extends CGExtendedBalMvtJournalManager implements JRCloneableDataSource {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private HashMap beans = new HashMap();
    private CGExtendedBalMvtJournal entity;
    private BProcess processCtx = null;
    private globaz.globall.db.BStatement statement;
    private globaz.globall.db.BTransaction transaction;

    /**
     * CGGrandLivre_DS constructor comment.
     */
    public CGBalMvtJournal_DS(BSession session) {
        setSession(session);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.03.2003 14:40:10)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#getContext()
     */
    public BProcess getContext() {
        return processCtx;
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 09:52:31)
     * 
     * @return globaz.helios.db.comptes.CGExtendedEcriture
     */
    public CGExtendedBalMvtJournal getEntity() {
        return entity;
    }

    /**
	 *
	 */

    @Override
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {

        String entityID = entity.getDate() + entity.getNumero();
        CGGeneric_Bean currentBean = (beans.containsKey(entityID)) ? (CGGeneric_Bean) beans.get(entityID)
                : new CGGeneric_Bean();

        // On renseigne la clé de tri
        currentBean.setSortKey(new Double(entity.getNumero()));

        // retourne chaque champ
        // Date d'écriture
        if (jrField.getName().equals("COL_1")) {
            currentBean.setCol("", 1, entity.getNumero());
            beans.put(entityID, currentBean);
            currentBean.setCol("", 2, entity.getLibelle());
            beans.put(entityID, currentBean);
            return entity.getNumero() + " " + entity.getLibelle();
        }
        // info d'id de journal et écriture
        if (jrField.getName().equals("COL_2")) {
            if ((entity.getDate() == null) || (entity.getDate().length() != 8)) {
                currentBean.setCol("", 3, entity.getDate());
                beans.put(entityID, currentBean);
                return entity.getDate();
            } else {
                currentBean.setCol("", 3, entity.getDate().substring(6, 8) + "." + entity.getDate().substring(4, 6)
                        + "." + entity.getDate().substring(0, 4));
                beans.put(entityID, currentBean);
                return entity.getDate().substring(6, 8) + "." + entity.getDate().substring(4, 6) + "."
                        + entity.getDate().substring(0, 4);
            }

        }
        // Info de contre écriture
        if (jrField.getName().equals("COL_3")) {
            if ((entity.getDateValeur() == null) || (entity.getDateValeur().length() != 8)) {
                currentBean.setCol("", 4, entity.getDateValeur());
                beans.put(entityID, currentBean);
                return entity.getDateValeur();
            } else {
                currentBean.setCol("", 4, entity.getDateValeur().substring(6, 8) + "."
                        + entity.getDateValeur().substring(4, 6) + "." + entity.getDateValeur().substring(0, 4));
                beans.put(entityID, currentBean);
                return entity.getDateValeur().substring(6, 8) + "." + entity.getDateValeur().substring(4, 6) + "."
                        + entity.getDateValeur().substring(0, 4);
            }
        }
        // Libellé de l'écriture
        if (jrField.getName().equals("COL_4")) {
            if ((entity.getMontant() == null) || (entity.getMontant().length() == 0)) {
                currentBean.setCol("", 5, "0");
                beans.put(entityID, currentBean);
                return new Double(0);
            } else {
                currentBean.setCol("", 5, entity.getMontant());
                beans.put(entityID, currentBean);
                return Double.valueOf(entity.getMontant());
            }
        }
        // Numéro de pièce
        if (jrField.getName().equals("COL_5")) {

            try {
                if ((entity.getIdEtat() == null) || (entity.getIdEtat().length() == 0)) {
                    currentBean.setCol("", 6, getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_SUPPRIME"));
                    beans.put(entityID, currentBean);
                    return getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_SUPPRIME");
                }
                currentBean.setCol("", 6, CodeSystem.getLibelle(getSession(), entity.getIdEtat()));
                beans.put(entityID, currentBean);
                return CodeSystem.getLibelle(getSession(), entity.getIdEtat());
            } catch (Exception e) {
                e.printStackTrace();
                currentBean.setCol("", 6, getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_SUPPRIME"));
                beans.put(entityID, currentBean);
                return getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_SUPPRIME");
            }
        }

        return null;
    }

    public ArrayList getListBeans() {
        return new ArrayList(beans.values());
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 09:52:58)
     * 
     * @return globaz.globall.db.BStatement
     */
    public globaz.globall.db.BStatement getStatement() {
        return statement;
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 09:53:12)
     * 
     * @return globaz.globall.db.BTransaction
     */
    public globaz.globall.db.BTransaction getTransaction() {
        return transaction;
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.
     */
    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {

        try {
            if ((processCtx != null) && processCtx.isAborted()) {
                // On met entity a null afin que la transaction soir correctement fermée dans le block finally
                entity = null;
                return false;
            }

            // Ouvre le curseur si c'est le statement est null -> donc pas
            // encore ouvert
            if (statement == null) { // Cursor not open
                transaction = new globaz.globall.db.BTransaction(getSession());
                transaction.openTransaction();
                statement = cursorOpen(transaction);
            }
            // lit le nouveau entity
            entity = (CGExtendedBalMvtJournal) cursorReadNext(statement);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.clearErrorBuffer();
            }
            e.printStackTrace();
        } finally {
            // Si l'entity est null donc on est à la fin du select -> on ferme
            // le curseur
            if (entity == null) { // Close cursor
                try {
                    if (statement != null) {
                        cursorClose(statement);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (transaction != null) {
                            transaction.closeTransaction();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }

    /**
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#setContext(BProcess)
     */
    public void setContext(BProcess process) {
        processCtx = process;
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 09:52:31)
     * 
     * @param newEntity
     *            globaz.helios.db.comptes.CGExtendedEcriture
     */
    public void setEntity(CGExtendedBalMvtJournal newEntity) {
        entity = newEntity;
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 09:52:58)
     * 
     * @param newStatement
     *            globaz.globall.db.BStatement
     */
    public void setStatement(globaz.globall.db.BStatement newStatement) {
        statement = newStatement;
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 09:53:12)
     * 
     * @param newTransaction
     *            globaz.globall.db.BTransaction
     */
    public void setTransaction(globaz.globall.db.BTransaction newTransaction) {
        transaction = newTransaction;
    }

}
