package globaz.osiris.db.journal.operation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class CASumOperationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String LABEL_JOURNAL_NON_RENSEIGNE = "7013";

    private static final String TEMP_TABLE_A = "a";
    private static final String TEMP_TABLE_B = "b";
    private static final String TEMP_TABLE_C = "c";

    private String forIdJournal = new String();

    public CASumOperationManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);

        if (JadeStringUtil.isIntegerEmpty(getForIdJournal())) {
            _addError(transaction, getSession().getLabel(CASumOperationManager.LABEL_JOURNAL_NON_RENSEIGNE));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String sql = "SELECT " + CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_IDCOMPTE + ", "
                + CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_IDCOMPTECOURANT + ", "
                + CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_CODEDEBITCREDIT + ", "
                + CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_PIECE + ", "
                + CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_IDJOURNAL + ", "
                + CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_IDCAISSEPROFESSIONNELLE + ", "
                + CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_MONTANT + ", "
                + CASumOperationManager.TEMP_TABLE_C + "." + CARubrique.FIELD_NUMCOMPTECG + ", "
                + CASumOperationManager.TEMP_TABLE_C + "." + CARubrique.FIELD_IDEXTERNE + " from ";
        sql += " ( ";
        sql += getSelectOperationGroupBy();
        sql += " ) " + CASumOperationManager.TEMP_TABLE_A + ", " + _getCollection() + CACompteCourant.TABLE_CACPTCP
                + " " + CASumOperationManager.TEMP_TABLE_B + ", " + _getCollection() + CARubrique.TABLE_CARUBRP + " "
                + CASumOperationManager.TEMP_TABLE_C;
        sql += " WHERE ";
        sql += CASumOperationManager.TEMP_TABLE_A + "." + CAOperation.FIELD_IDCOMPTECOURANT + " = "
                + CASumOperationManager.TEMP_TABLE_B + "." + CACompteCourant.FIELD_IDCOMPTECOURANT;
        sql += " AND " + CASumOperationManager.TEMP_TABLE_B + "." + CACompteCourant.FIELD_IDRUBRIQUE + " = "
                + CASumOperationManager.TEMP_TABLE_C + "." + CARubrique.FIELD_IDRUBRIQUE;
        return sql;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASumOperation();
    }

    /**
     * Return la collection en cours utilisée par l'application.
     * 
     * @return
     */
    public String getCollection() {
        // TODO Remplacer par méthode public du fw (quand cette dernière sera
        // implémentée)
        return _getCollection();
    }

    /**
     * @return
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Retourne le select pour les opérations groupés avec montant sommé
     * 
     * @return
     */
    private String getSelectOperationGroupBy() {
        String sql = "SELECT " + CAOperation.FIELD_IDCOMPTE + ", " + CAOperation.FIELD_IDCOMPTECOURANT + ", "
                + CAOperation.FIELD_IDJOURNAL + ", " + CAOperation.FIELD_CODEDEBITCREDIT + ", "
                + CAOperation.FIELD_PIECE + ", " + CAOperation.FIELD_IDCAISSEPROFESSIONNELLE + ", sum("
                + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT + " from " + _getCollection()
                + CAOperation.TABLE_CAOPERP + " where ";
        sql += " " + CAOperation.FIELD_IDJOURNAL + " = " + getForIdJournal() + " and "
                + CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' and "
                + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_PROVISOIRE;
        sql += " group by " + CAOperation.FIELD_IDCOMPTE + ", " + CAOperation.FIELD_IDCOMPTECOURANT + ", "
                + CAOperation.FIELD_IDJOURNAL + ", " + CAOperation.FIELD_CODEDEBITCREDIT + ", "
                + CAOperation.FIELD_PIECE + ", " + CAOperation.FIELD_IDCAISSEPROFESSIONNELLE;
        return sql;
    }

    /**
     * @param string
     */
    public void setForIdJournal(String string) {
        forIdJournal = string;
    }

}
