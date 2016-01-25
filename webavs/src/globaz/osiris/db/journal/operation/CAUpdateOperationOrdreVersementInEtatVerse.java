package globaz.osiris.db.journal.operation;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author sch Mise � jour des op�rations de type ordre de versement d'un journal en mode vers�.
 */
public class CAUpdateOperationOrdreVersementInEtatVerse {
    private static final String LABEL_JOURNAL_NON_RENSEIGNE = "7013";

    private String forIdJournal = new String();

    /**
     * @return
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Return la query a �x�cuter pour mettre � jour l'�tat des op�rations de type ordre de versement en mode vers�.
     * 
     * @return
     */
    private String getUpdateQuery() {
        String updateQuery = "UPDATE " + new CASumOperationManager().getCollection() + CAOperation.TABLE_CAOPERP;
        updateQuery += " SET " + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_VERSE;

        updateQuery += " WHERE " + CAOperation.FIELD_IDJOURNAL + " = " + getForIdJournal() + " ";

        updateQuery += " AND " + CAOperation.FIELD_IDTYPEOPERATION + " = '" + APIOperation.CAOPERATIONORDREVERSEMENT
                + "' ";

        return updateQuery;
    }

    /**
     * @param string
     */
    public void setForIdJournal(String string) {
        forIdJournal = string;
    }

    /**
     * Ex�cute la mise � jour des op�rations de type ordre de versement d'un journal en mode vers�.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public boolean update(BSession session, BTransaction transaction) throws Exception {
        if (!validate(session, transaction)) {
            return false;
        }

        BStatement s = new BStatement(transaction);
        s.createStatement();

        s.execute(getUpdateQuery());

        s.closeStatement();

        return true;
    }

    /**
     * Valide les donn�es n�cessaires (transaction et forIdJournal) � l'�x�cution de l'update des op�rations.
     * 
     * @param session
     * @param transaction
     * @return
     */
    private boolean validate(BSession session, BTransaction transaction) {
        if (transaction == null) {
            return false;
        }

        if (JadeStringUtil.isIntegerEmpty(getForIdJournal())) {
            transaction.addErrors(session
                    .getLabel(CAUpdateOperationOrdreVersementInEtatVerse.LABEL_JOURNAL_NON_RENSEIGNE));
            return false;
        }

        return true;
    }

}
