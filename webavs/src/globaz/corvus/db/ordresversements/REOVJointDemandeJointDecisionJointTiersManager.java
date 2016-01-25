package globaz.corvus.db.ordresversements;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class REOVJointDemandeJointDecisionJointTiersManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatDemandeRenteIN = "";
    private String forCsTypeOV = "";
    private String forDateDebut = "";
    private String forDateFin = "";
    private String forIdCanton = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forCsEtatDemandeRenteIN)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRente.TABLE_NAME_DEMANDE_RENTE + "." + REDemandeRente.FIELDNAME_CS_ETAT
                    + " IN (" + forCsEtatDemandeRenteIN + ")";
        }

        if (!JadeStringUtil.isBlank(forCsTypeOV)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS + "."
                    + REOrdresVersements.FIELDNAME_TYPE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsTypeOV);
        }

        if (!JadeStringUtil.isBlank(forIdCanton)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REOVJointDemandeJointDecisionJointTiers.TABLE_LOCALITE + "."
                    + REOVJointDemandeJointDecisionJointTiers.FIELDNAME_ID_CANTON + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdCanton);
        }

        // la date de validation de la décision doit se situer entre les dates
        // de début et de fin
        if (!JadeStringUtil.isBlankOrZero(forDateDebut) && !JadeStringUtil.isBlankOrZero(forDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(" + schema + REDecisionEntity.TABLE_NAME_DECISIONS + "."
                    + REDecisionEntity.FIELDNAME_DATE_VALIDATION + " BETWEEN " + forDateDebut + " AND " + forDateFin
                    + ")";

        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REOVJointDemandeJointDecisionJointTiers();
    }

    public String getForCsEtatDemandeRenteIN() {
        return forCsEtatDemandeRenteIN;
    }

    public String getForCsTypeOV() {
        return forCsTypeOV;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdCanton() {
        return forIdCanton;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REOVJointDemandeJointDecisionJointTiers.FIELDNAME_NOM + ","
                + REOVJointDemandeJointDecisionJointTiers.FIELDNAME_PRENOM;
    }

    public void setForCsEtatDemandeRenteIN(String forCsEtatDemandeRenteIN) {
        this.forCsEtatDemandeRenteIN = forCsEtatDemandeRenteIN;
    }

    public void setForCsTypeOV(String forCsTypeOV) {
        this.forCsTypeOV = forCsTypeOV;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdCanton(String forIdCanton) {
        this.forIdCanton = forIdCanton;
    }

}
