package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 09:26:37)
 * 
 * @author: Administrator
 */
public class CAJournalISODetailManager extends BManager implements Serializable {

    private static final long serialVersionUID = -991198190749386906L;

    private String forIdJournal = null;

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAJournalISODetail.TABLE_CAJOISO;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Traitement du positionnement depuis un numéro
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournalISODetail.FIELD_IDJOURNAL + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAJournalISODetail();
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }
}
