package globaz.osiris.db.rentes.check.operation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contrôle que le total des opérations après l'exécution des Rentes est à zéro pour les journaux concernés.
 * 
 * @author DDA
 */
public class CARentesCheckOperationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList forIdJournalIn = new ArrayList();

    @Override
    protected String _getFields(BStatement statement) {
        return "SUM(" + CAOperation.FIELD_MONTANT + ") AS " + CAOperation.FIELD_MONTANT;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' AND ";
        sqlWhere += CAOperation.FIELD_ETAT + " IN (" + APIOperation.ETAT_COMPTABILISE + ", "
                + APIOperation.ETAT_PROVISOIRE + ") ";

        if (!getForIdJournalIn().isEmpty()) {
            sqlWhere += "AND " + CAOperation.FIELD_IDJOURNAL + " IN ";

            String tmp = "(";
            Iterator iter = getForIdJournalIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() > 1) {
                    tmp += ", ";
                }

                tmp += this._dbWriteNumeric(statement.getTransaction(), element);
            }

            sqlWhere += tmp + ")";
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOperation();
    }

    public void addForIdJournal(String idJournal) {
        forIdJournalIn.add(idJournal);
    }

    public ArrayList getForIdJournalIn() {
        return forIdJournalIn;
    }

    public void setForIdJournalIn(ArrayList forIdJournalIn) {
        this.forIdJournalIn = forIdJournalIn;
    }
}
