package globaz.aquila.db.access.operation;

import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author SEL <br>
 *         Date : 1 sept. 2010
 */
public class COOperationContentieuxManager extends COBManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtat = "";
    private String forIdContrePartie = "";
    private String forIdJournal = "";
    private String forIdSection = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");

        if (getForIdJournal().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDJOURNAL
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal()));
        }

        if (getForIdSection().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDSECTION
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        if (getForEtat().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForEtat()));
        }

        if (!JadeStringUtil.isEmpty(forIdContrePartie)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }
            // ((IDCONTREPARTIE=31 and idtypeoperation='D') or
            // idtypeoperation='e')
            sqlWhere.append("((" + CAOperation.FIELD_IDCONTREPARTIE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdContrePartie));
            sqlWhere.append(COBManager.AND + CAOperation.FIELD_IDTYPEOPERATION + "="
                    + this._dbWriteString(statement.getTransaction(), "D"));
            sqlWhere.append(")");
            sqlWhere.append(COBManager.OR + CAOperation.FIELD_IDTYPEOPERATION + "="
                    + this._dbWriteString(statement.getTransaction(), "E"));
            sqlWhere.append(")");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOperation();
    }

    /**
     * @return the forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return the forIdContrePartie
     */
    public String getForIdContrePartie() {
        return forIdContrePartie;
    }

    /**
     * @return the forIdJournal
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * @return the forIdSection
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @param forEtat
     *            the forEtat to set
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @param forIdContrePartie
     *            the forIdContrePartie to set
     */
    public void setForIdContrePartie(String forIdContrePartie) {
        this.forIdContrePartie = forIdContrePartie;
    }

    /**
     * @param forIdJournal
     *            the forIdJournal to set
     */
    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    /**
     * @param forIdSection
     *            the forIdSection to set
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }
}
