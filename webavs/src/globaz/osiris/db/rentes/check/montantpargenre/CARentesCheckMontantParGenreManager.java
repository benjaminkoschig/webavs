package globaz.osiris.db.rentes.check.montantpargenre;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contrôle que le total des opérations par rubrique (genre de rente) après l'exécution des Rentes est égal au montant
 * fournit par les rentes pour les journaux concernés.
 * 
 * @author DDA
 */
public class CARentesCheckMontantParGenreManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList forIdJournalIn = new ArrayList();

    @Override
    protected String _getSql(BStatement statement) {
        String sql = "SELECT SUM(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT + ", ";
        sql += "b." + CARubrique.FIELD_IDEXTERNE + ", b." + CARubrique.FIELD_IDRUBRIQUE;
        sql += " FROM " + _getCollection() + CAOperation.TABLE_CAOPERP + " a,";
        sql += _getCollection() + CARubrique.TABLE_CARUBRP + " b WHERE ";
        sql += "a." + CAOperation.FIELD_IDCOMPTE + " = " + "b." + CARubrique.FIELD_IDRUBRIQUE + " AND ";
        sql += CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' AND ";
        sql += CAOperation.FIELD_ETAT + " IN (" + APIOperation.ETAT_COMPTABILISE + ", " + APIOperation.ETAT_PROVISOIRE
                + ") ";

        if (!getForIdJournalIn().isEmpty()) {
            sql += "AND " + CAOperation.FIELD_IDJOURNAL + " IN ";

            String tmp = "(";
            Iterator iter = getForIdJournalIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() > 1) {
                    tmp += ", ";
                }

                tmp += this._dbWriteNumeric(statement.getTransaction(), element);
            }

            sql += tmp + ")";
        }
        sql += " group by b." + CARubrique.FIELD_IDEXTERNE + ", b." + CARubrique.FIELD_IDRUBRIQUE;

        return sql;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CARentesCheckMontantParGenre();
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
