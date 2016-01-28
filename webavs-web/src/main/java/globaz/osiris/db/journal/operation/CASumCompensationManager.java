/**
 *
 */
package globaz.osiris.db.journal.operation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author SEL
 * 
 */
public class CASumCompensationManager extends BManager {

    // SELECT SUM(MONTANT)
    // FROM ciciweb.CAOPERP
    // INNER JOIN (
    // select ciciweb.CARUBRP.idrubrique from ciciweb.CARUBRP --ON ciciweb.CARUBRP.IDRUBRIQUE = ciciweb.CAOPERP.IDCOMPTE
    // inner join ciciweb.carerup ref on ref.idrubrique=ciciweb.CARUBRP.IDRUBRIQUE and ref.idcodereference<>237033
    // WHERE ciciweb.CARUBRP.NATURERUBRIQUE = 200007
    // group by ciciweb.CARUBRP.idrubrique
    // ) a ON a.IDRUBRIQUE = ciciweb.CAOPERP.IDCOMPTE
    // where ciciweb.CAOPERP.IDJOURNAL = 10694 AND ciciweb.CAOPERP.ETAT <> 205005;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdJournal = "";

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(CAOperation.TABLE_CAOPERP);
        sqlFrom.append(" INNER JOIN (");
        sqlFrom.append("SELECT " + _getCollection()).append(CARubrique.TABLE_CARUBRP).append(".")
                .append(CARubrique.FIELD_IDRUBRIQUE);
        sqlFrom.append(" FROM ").append(_getCollection()).append(CARubrique.TABLE_CARUBRP);
        sqlFrom.append(" INNER JOIN ").append(_getCollection()).append(CAReferenceRubrique.TABLE_CARERUP);
        sqlFrom.append(" ON ").append(_getCollection()).append(CAReferenceRubrique.TABLE_CARERUP).append(".")
                .append(CAReferenceRubrique.FIELD_IDRUBRIQUE);
        sqlFrom.append(" = ").append(_getCollection()).append(CARubrique.TABLE_CARUBRP).append(".")
                .append(CARubrique.FIELD_IDRUBRIQUE);
        sqlFrom.append(" AND ");
        sqlFrom.append(_getCollection()).append(CAReferenceRubrique.TABLE_CARERUP).append(".")
                .append(CAReferenceRubrique.FIELD_IDCODEREFERENCE);
        sqlFrom.append(" <> ");
        sqlFrom.append(this._dbWriteNumeric(statement.getTransaction(), APIReferenceRubrique.MONTANT_MINIME));

        sqlFrom.append(" WHERE ");
        sqlFrom.append(_getCollection()).append(CARubrique.TABLE_CARUBRP).append(".")
                .append(CARubrique.FIELD_NATURERUBRIQUE).append(" = ").append(APIRubrique.COMPTE_COMPENSATION);
        sqlFrom.append(" GROUP BY ").append(_getCollection()).append(CARubrique.TABLE_CARUBRP).append(".")
                .append(CARubrique.FIELD_IDRUBRIQUE);

        sqlFrom.append(") a ON ").append("a.").append(CARubrique.FIELD_IDRUBRIQUE);
        sqlFrom.append(" = ").append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(".")
                .append(CAOperation.FIELD_IDCOMPTE);

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_ETAT);
        sqlWhere.append(" <> ");
        sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), APIOperation.ETAT_INACTIF));

        if (!JadeStringUtil.isBlank(getForIdJournal())) {
            sqlWhere.append(" AND ");
            sqlWhere.append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(".")
                    .append(CAOperation.FIELD_IDJOURNAL);
            sqlWhere.append(" = ").append(this._dbWriteNumeric(statement.getTransaction(), getForIdJournal()));
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    /**
     * @return the forIdJournal
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * @param forIdJournal
     *            the forIdJournal to set
     */
    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

}
