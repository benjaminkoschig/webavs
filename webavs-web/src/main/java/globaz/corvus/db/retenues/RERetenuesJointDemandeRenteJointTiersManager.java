package globaz.corvus.db.retenues;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

public class RERetenuesJointDemandeRenteJointTiersManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatDemandeRenteIN = "";
    private String forCsTypeRetenues = "";
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

        if (!JadeStringUtil.isBlankOrZero(forCsEtatDemandeRenteIN)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRente.TABLE_NAME_DEMANDE_RENTE + "." + REDemandeRente.FIELDNAME_CS_ETAT
                    + " IN (" + forCsEtatDemandeRenteIN + ")";
        }

        if (!JadeStringUtil.isBlankOrZero(forCsTypeRetenues)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + RERetenuesPaiement.TABLE_NAME_RETENUES + "."
                    + RERetenuesPaiement.FIELDNAME_TYPE_RETENU + " =" + forCsTypeRetenues;
        }

        if (!JadeStringUtil.isBlankOrZero(forIdCanton)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + RERetenuesPaiement.TABLE_NAME_RETENUES + "."
                    + RERetenuesPaiement.FIELDNAME_CANTON_IMPOSITION + " =" + forIdCanton;
        }

        // la date de début ou date de fin doit se situer dans la période de la
        // retenue
        if (!JadeStringUtil.isBlankOrZero(forDateDebut) && !JadeStringUtil.isBlankOrZero(forDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            forDateDebut = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDateDebut);
            forDateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDateFin);

            sqlWhere += schema + RERetenuesPaiement.TABLE_NAME_RETENUES + "."
                    + RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE;
            sqlWhere += "<=";
            sqlWhere += forDateFin;
            sqlWhere += " AND (";
            sqlWhere += schema + RERetenuesPaiement.TABLE_NAME_RETENUES + "."
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE;
            sqlWhere += "=";
            sqlWhere += "0";
            sqlWhere += " OR ";
            sqlWhere += schema + RERetenuesPaiement.TABLE_NAME_RETENUES + "."
                    + RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE;
            sqlWhere += ">=";
            sqlWhere += forDateDebut;
            sqlWhere += ")";

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
        return new RERetenuesJointDemandeRenteJointTiers();
    }

    public String getForCsEtatDemandeRenteIN() {
        return forCsEtatDemandeRenteIN;
    }

    public String getForCsTypeRetenues() {
        return forCsTypeRetenues;
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
        return RERetenuesJointDemandeRenteJointTiers.FIELDNAME_NOM + ","
                + RERetenuesJointDemandeRenteJointTiers.FIELDNAME_PRENOM;
    }

    public void setForCsEtatDemandeRenteIN(String forCsEtatDemandeRenteIN) {
        this.forCsEtatDemandeRenteIN = forCsEtatDemandeRenteIN;
    }

    public void setForCsTypeRetenues(String forCsTypeRetenues) {
        this.forCsTypeRetenues = forCsTypeRetenues;
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
