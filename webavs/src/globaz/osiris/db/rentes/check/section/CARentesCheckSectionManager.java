package globaz.osiris.db.rentes.check.section;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contrôle que le total des sections après l'exécution des Rentes est à zéro pour les journaux concernés.
 * 
 * @author DDA
 */
public class CARentesCheckSectionManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList forIdJournalIn = new ArrayList();

    @Override
    protected String _getFields(BStatement statement) {
        return "SUM(" + CASection.FIELD_SOLDE + ") AS " + CASection.FIELD_SOLDE + ", SUM(" + CASection.FIELD_BASE
                + ") AS " + CASection.FIELD_BASE + ", SUM(" + CASection.FIELD_PMTCMP + ") AS " + CASection.FIELD_PMTCMP;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CASection.TABLE_CASECTP;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!getForIdJournalIn().isEmpty()) {
            sqlWhere += CAOperation.FIELD_IDJOURNAL + " IN ";

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
        return new CASection();
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
