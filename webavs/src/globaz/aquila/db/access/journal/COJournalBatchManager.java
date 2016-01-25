package globaz.aquila.db.access.journal;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COJournalBatchManager extends BManager {

    private static final long serialVersionUID = -3291549787129769140L;

    private String forCsEtat;
    private String forLibelleLike;
    private String fromNumero;

    /**
     * retourne la clause ORDER BY de la requete SQL (la table). Order by idjournal, datecreation
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return COJournalBatch.FNAME_ID_JOURNAL + " DESC, " + COJournalBatch.FNAME_DATE_CREATION + " DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "";

        if (!JadeStringUtil.isBlank(getFromNumero())) {
            if (where.length() != 0) {
                where += " AND ";
            }

            where += COJournalBatch.FNAME_ID_JOURNAL + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNumero());
        }

        if (!JadeStringUtil.isBlank(getForCsEtat())) {
            if (where.length() != 0) {
                where += " AND ";
            }

            where += COJournalBatch.FNAME_CS_ETAT + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCsEtat());
        }

        if (!JadeStringUtil.isBlank(getForLibelleLike())) {
            if (where.length() != 0) {
                where += " AND ";
            }

            where += "UPPER(" + COJournalBatch.FNAME_LIBELLE + ") like UPPER("
                    + this._dbWriteString(statement.getTransaction(), "%" + getForLibelleLike() + "%") + ")";
        }

        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COJournalBatch();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForLibelleLike() {
        return forLibelleLike;
    }

    public String getFromNumero() {
        return fromNumero;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForLibelleLike(String forLibelleLike) {
        this.forLibelleLike = forLibelleLike;
    }

    public void setFromNumero(String fromNumero) {
        this.fromNumero = fromNumero;
    }

}
