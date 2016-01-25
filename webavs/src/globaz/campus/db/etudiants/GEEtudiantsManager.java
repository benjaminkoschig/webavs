package globaz.campus.db.etudiants;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class GEEtudiantsManager extends BManager {
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdEtudiant = null;
    private String forIdTiersEcole = null;
    private String forIdTiersEtudiant = null;
    private String forNumImmatriculation = null;
    private Boolean forNumImmatriculationVide = new Boolean(false);
    private String orderBy = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + GEEtudiants.TABLE_NAME_ETUDIANT;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdEtudiant())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_ID_ETUDIANT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdEtudiant());
        }
        if (!JadeStringUtil.isBlank(getForIdTiersEtudiant())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_ID_TIERS_ETUDIANT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiersEtudiant());
        }
        if (!JadeStringUtil.isBlank(getForIdTiersEcole())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_ID_TIERS_ECOLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiersEcole());
        }
        if (!JadeStringUtil.isBlank(getForNumImmatriculation())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_NUM_IMMATRICULATION + "="
                    + _dbWriteString(statement.getTransaction(), getForNumImmatriculation());
        }
        if (forNumImmatriculationVide.booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + GEEtudiants.FIELDNAME_NUM_IMMATRICULATION + "="
                    + _dbWriteString(statement.getTransaction(), "") + " OR "
                    + GEEtudiants.FIELDNAME_NUM_IMMATRICULATION + "=" + _dbWriteString(statement.getTransaction(), "0")
                    + ")";
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GEEtudiants();
    }

    public String getForIdEtudiant() {
        return forIdEtudiant;
    }

    public String getForIdTiersEcole() {
        return forIdTiersEcole;
    }

    public String getForIdTiersEtudiant() {
        return forIdTiersEtudiant;
    }

    public String getForNumImmatriculation() {
        return forNumImmatriculation;
    }

    public Boolean getForNumImmatriculationVide() {
        return forNumImmatriculationVide;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setForIdEtudiant(String forIdEtudiant) {
        this.forIdEtudiant = forIdEtudiant;
    }

    public void setForIdTiersEcole(String forIdTiersEcole) {
        this.forIdTiersEcole = forIdTiersEcole;
    }

    public void setForIdTiersEtudiant(String forIdTiersEtudiant) {
        this.forIdTiersEtudiant = forIdTiersEtudiant;
    }

    public void setForNumImmatriculation(String forNumImmatriculation) {
        this.forNumImmatriculation = forNumImmatriculation;
    }

    public void setForNumImmatriculationVide(Boolean forNumImmatriculationVide) {
        this.forNumImmatriculationVide = forNumImmatriculationVide;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
