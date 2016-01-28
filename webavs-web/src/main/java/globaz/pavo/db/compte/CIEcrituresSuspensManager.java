package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CIEcrituresSuspensManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdJournal = null;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forIdTypeCompte = null;
    private String forRegistre = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        return CIEcrituresSuspens.FIELDNAME_ID_AFFILIATION + " ";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CIEcrituresSuspens.TABLE_NAME_ECRITURES + " INNER JOIN " + _getCollection()
                + CIEcrituresSuspens.TABLE_NAME_COMPTE_INDIVIDUEL + " ON " + _getCollection()
                + CIEcrituresSuspens.TABLE_NAME_ECRITURES + "." + CIEcrituresSuspens.FIELDNAME_ID_COMPTE_INDIVIDUEL
                + "=" + _getCollection() + CIEcrituresSuspens.TABLE_NAME_COMPTE_INDIVIDUEL + "."
                + CIEcrituresSuspens.FIELDNAME_ID_COMPTE_INDIVIDUEL;
    }

    @Override
    protected String _getGroupBy(BStatement statement) {
        return CIEcrituresSuspens.FIELDNAME_ID_AFFILIATION;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // Type compte
        if (!JadeStringUtil.isBlank(getForIdTypeCompte())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CIEcrituresSuspens.FIELDNAME_ID_TYPE_COMPTE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTypeCompte());
        }
        // Registre
        if (!JadeStringUtil.isBlank(getForRegistre())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CIEcrituresSuspens.FIELDNAME_REGISTRE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForRegistre());
        }
        // idjournal
        if (!JadeStringUtil.isBlank(getForIdJournal())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CIEcrituresSuspens.FIELDNAME_ID_JOURNAL + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        sqlWhere += " GROUP BY " + _getGroupBy(statement);
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIEcrituresSuspens();
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdTypeCompte() {
        return forIdTypeCompte;
    }

    public String getForRegistre() {
        return forRegistre;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setForIdTypeCompte(String forIdTypeCompte) {
        this.forIdTypeCompte = forIdTypeCompte;
    }

    public void setForRegistre(String forRegistre) {
        this.forRegistre = forRegistre;
    }
}
