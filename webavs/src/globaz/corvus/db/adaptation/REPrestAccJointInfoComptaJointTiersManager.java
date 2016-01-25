package globaz.corvus.db.adaptation;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * 
 * @author HPE
 * 
 */
public class REPrestAccJointInfoComptaJointTiersManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAncienMontant = "";
    private String forCodePrestation = "";
    private String forCsEtatIn = "";
    private String forDateDebutBefore = "";
    private String forEnCoursAtMois = "";
    private String forFractionRente = "";
    private String forIdRenteAccordee = "";
    private Boolean forIsPrestationBloquee = null;
    private String forNSS = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forEnCoursAtMois)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + "<="
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois) + " AND ( "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL " + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " >= "
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois) + ")";
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebutBefore)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT
                    + " < "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDateDebutBefore));
        }

        if (!JadeStringUtil.isIntegerEmpty(forNSS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestAccJointInfoComptaJointTiers.FIELDNAME_NUM_AVS + " = "
                    + this._dbWriteString(statement.getTransaction(), forNSS);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCodePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + " = "
                    + this._dbWriteString(statement.getTransaction(), forCodePrestation);
        }

        if (!JadeStringUtil.isIntegerEmpty(forAncienMontant)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), forAncienMontant);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtatIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_CS_ETAT + " IN ( " + forCsEtatIn + ") ";
        }

        if (!JadeStringUtil.isIntegerEmpty(forFractionRente)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_FRACTION_RENTE + " = "
                    + this._dbWriteString(statement.getTransaction(), forFractionRente);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRenteAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), forIdRenteAccordee);
        }

        if (getForIsPrestationBloquee() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForIsPrestationBloquee().booleanValue()) {
                sqlWhere += REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE
                        + "="
                        + this._dbWriteBoolean(statement.getTransaction(), getForIsPrestationBloquee(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR);

            } else {
                sqlWhere += "("
                        + REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE
                        + "="
                        + this._dbWriteBoolean(statement.getTransaction(), getForIsPrestationBloquee(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR);

                sqlWhere += " OR " + REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE + "= '0' OR "
                        + REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE + " IS NULL )";

            }
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestAccJointInfoComptaJointTiers();
    }

    public String getForAncienMontant() {
        return forAncienMontant;
    }

    public String getForCodePrestation() {
        return forCodePrestation;
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForDateDebutBefore() {
        return forDateDebutBefore;
    }

    public String getForEnCoursAtMois() {
        return forEnCoursAtMois;
    }

    public String getForFractionRente() {
        return forFractionRente;
    }

    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public Boolean getForIsPrestationBloquee() {
        return forIsPrestationBloquee;
    }

    public String getForNSS() {
        return forNSS;
    }

    @Override
    public String getOrderByDefaut() {
        return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
    }

    public void setForAncienMontant(String forAncienMontant) {
        this.forAncienMontant = forAncienMontant;
    }

    public void setForCodePrestation(String forCodePrestation) {
        this.forCodePrestation = forCodePrestation;
    }

    public void setForCsEtatIn(String forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForDateDebutBefore(String forDateDebutBefore) {
        this.forDateDebutBefore = forDateDebutBefore;
    }

    public void setForEnCoursAtMois(String forEnCoursAtMois) {
        this.forEnCoursAtMois = forEnCoursAtMois;
    }

    public void setForFractionRente(String forFractionRente) {
        this.forFractionRente = forFractionRente;
    }

    public void setForIdRenteAccordee(String forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }

    public void setForIsPrestationBloquee(Boolean forIsPrestationBloquee) {
        this.forIsPrestationBloquee = forIsPrestationBloquee;
    }

    public void setForNSS(String forNSS) {
        this.forNSS = forNSS;
    }

}
