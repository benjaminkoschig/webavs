/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author scr
 * 
 */

public class RERenteAccordeeJoinInfoComptaJoinPrstDuesManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatPrstDue = "";
    private String forCsEtatRA = "";
    private String forCsEtatRADifferentDe = "";
    private String forCsTypePrstDue = "";
    private String forIdBaseCalcul = "";

    private String forIdRenteAccordee = "";
    private String forIdTiersBeneficiaire = "";

    private Boolean forIsEmptyDateFin = null;
    private String fromDateDebutDroit = "";
    private String fromDateDebutPmt = "";

    private String fromDateFinPmt = "";

    private String periodeIncluseDans = "";

    private String untilDateDebutDroit = "";
    private String untilDateDebutPmt = "";

    private String untilDateFinPmt = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return RERenteAccordeeJoinInfoComptaJoinPrstDues.createFromClause(_getCollection());
    }

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

        if (!JadeStringUtil.isBlank(forIdRenteAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdRenteAccordee);
        }

        if (!JadeStringUtil.isBlank(forIdTiersBeneficiaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdTiersBeneficiaire);
        }

        if (!JadeStringUtil.isBlankOrZero(forCsEtatRA)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatRA);
        }

        if (!JadeStringUtil.isBlankOrZero(forCsEtatRADifferentDe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_CS_ETAT + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatRADifferentDe);
        }

        if (!JadeStringUtil.isBlank(forIdBaseCalcul)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RERenteAccordee.FIELDNAME_ID_BASE_CALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdBaseCalcul);
        }

        if (!JadeStringUtil.isBlankOrZero(forCsEtatPrstDue)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatPrstDue);
        }

        if (!JadeStringUtil.isBlankOrZero(forCsTypePrstDue)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_CS_TYPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsTypePrstDue);
        }

        if (!JadeStringUtil.isBlankOrZero(fromDateDebutPmt)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), fromDateDebutPmt);
        }

        if (!JadeStringUtil.isBlankOrZero(fromDateDebutDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), fromDateDebutDroit);
        }

        if (!JadeStringUtil.isBlankOrZero(untilDateDebutDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + "<="
                    + this._dbWriteNumeric(statement.getTransaction(), untilDateDebutDroit);
        }

        if (!JadeStringUtil.isBlankOrZero(fromDateFinPmt)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND (";
            }

            sqlWhere += REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), fromDateFinPmt) + " OR "
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + "IS NULL ) ";
        }

        if (!JadeStringUtil.isBlankOrZero(untilDateDebutPmt)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + "<="
                    + this._dbWriteNumeric(statement.getTransaction(), untilDateDebutPmt);
        }

        if (!JadeStringUtil.isBlankOrZero(untilDateFinPmt)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + "<="
                    + this._dbWriteNumeric(statement.getTransaction(), untilDateFinPmt);
        }

        if (!JadeStringUtil.isBlankOrZero(periodeIncluseDans)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // AND YMDDPA<=200706
            // AND (YMDFPA=0 OR YMDFPA>=200706)
            sqlWhere += REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + "<="
                    + this._dbWriteNumeric(statement.getTransaction(), periodeIncluseDans) + " AND ("
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " = 0 OR "
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), periodeIncluseDans) + ") ";
        }

        if (forIsEmptyDateFin != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND (";
            }

            if (forIsEmptyDateFin.booleanValue()) {
                sqlWhere += REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " = 0 OR "
                        + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " IS NULL ) ";
            } else {
                sqlWhere += REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " > 0 OR "
                        + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " IS NOT NULL ) ";
            }
        }

        return sqlWhere;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteAccordeeJoinInfoComptaJoinPrstDues();
    }

    /**
     * @return the forCsEtatPrstDue
     */
    public String getForCsEtatPrstDue() {
        return forCsEtatPrstDue;
    }

    /**
     * @return the forCsEtatRA
     */
    public String getForCsEtatRA() {
        return forCsEtatRA;
    }

    /**
     * @return the forCsEtatRADifferentDe
     */
    public String getForCsEtatRADifferentDe() {
        return forCsEtatRADifferentDe;
    }

    /**
     * @return the forCsTypePrstDue
     */
    public String getForCsTypePrstDue() {
        return forCsTypePrstDue;
    }

    /**
     * @return the forIdBaseCalcul
     */
    public String getForIdBaseCalcul() {
        return forIdBaseCalcul;
    }

    /**
     * @return
     */
    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    /**
     * @return the forIdTiersBeneficiaire
     */
    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    /**
     * @return the forIsEmptyDateFin
     */
    public Boolean getForIsEmptyDateFin() {
        return forIsEmptyDateFin;
    }

    public String getFromDateDebutDroit() {
        return fromDateDebutDroit;
    }

    /**
     * @return the fromDateDebutPmt
     */
    public String getFromDateDebutPmt() {
        return fromDateDebutPmt;
    }

    /**
     * @return the fromDateFinPmt
     */
    public String getFromDateFinPmt() {
        return fromDateFinPmt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
    }

    /**
     * @return the periodeIncluseDans
     */
    public String getPeriodeIncluseDans() {
        return periodeIncluseDans;
    }

    public String getUntilDateDebutDroit() {
        return untilDateDebutDroit;
    }

    /**
     * @return the untilDateDebutPmt
     */
    public String getUntilDateDebutPmt() {
        return untilDateDebutPmt;
    }

    /**
     * @return the untilDateFinPmt
     */
    public String getUntilDateFinPmt() {
        return untilDateFinPmt;
    }

    /**
     * @param forCsEtatPrstDue
     *            the forCsEtatPrstDue to set
     */
    public void setForCsEtatPrstDue(String forCsEtatPrstDue) {
        this.forCsEtatPrstDue = forCsEtatPrstDue;
    }

    /**
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtatRA(String forCsEtat) {
        forCsEtatRA = forCsEtat;
    }

    /**
     * @param forCsEtatRADifferentDe
     *            the forCsEtatDifferentDe to set
     */
    public void setForCsEtatRADifferentDe(String forCsEtatDifferentDe) {
        forCsEtatRADifferentDe = forCsEtatDifferentDe;
    }

    /**
     * @param forCsTypePrstDue
     *            the forCsTypePrstDue to set
     */
    public void setForCsTypePrstDue(String forCsTypePrstDue) {
        this.forCsTypePrstDue = forCsTypePrstDue;
    }

    /**
     * @param forIdBaseCalcul
     *            the forIdBaseCalcul to set
     */
    public void setForIdBaseCalcul(String forIdBaseCalcul) {
        this.forIdBaseCalcul = forIdBaseCalcul;
    }

    /**
     * @param string
     */
    public void setForIdRenteAccordee(String string) {
        forIdRenteAccordee = string;
    }

    /**
     * @param forIdTiersBeneficiaire
     *            the forIdTiersBeneficiaire to set
     */
    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    /**
     * @param forIsEmptyDateFin
     *            the forIsEmptyDateFin to set
     */
    public void setForIsEmptyDateFin(Boolean forIsEmptyDateFin) {
        this.forIsEmptyDateFin = forIsEmptyDateFin;
    }

    public void setFromDateDebutDroit(String fromDateDebutDroit) {
        this.fromDateDebutDroit = fromDateDebutDroit;
    }

    /**
     * @param fromDateDebutPmt
     *            the fromDateDebutPmt to set
     */
    public void setFromDateDebutPmt(String fromDateDebutPmt) {
        this.fromDateDebutPmt = fromDateDebutPmt;
    }

    /**
     * @param fromDateFinPmt
     *            the fromDateFinPmt to set
     */
    public void setFromDateFinPmt(String fromDateFinPmt) {
        this.fromDateFinPmt = fromDateFinPmt;
    }

    /**
     * @param periodeIncluseDans
     *            the periodeIncluseDans to set
     */
    public void setPeriodeIncluseDans(String periodeIncluseDans) {
        this.periodeIncluseDans = periodeIncluseDans;
    }

    public void setUntilDateDebutDroit(String untilDateDebutDroit) {
        this.untilDateDebutDroit = untilDateDebutDroit;
    }

    /**
     * @param untilDateDebutPmt
     *            the untilDateDebutPmt to set
     */
    public void setUntilDateDebutPmt(String untilDateDebutPmt) {
        this.untilDateDebutPmt = untilDateDebutPmt;
    }

    /**
     * @param untilDateFinPmt
     *            the untilDateFinPmt to set
     */
    public void setUntilDateFinPmt(String untilDateFinPmt) {
        this.untilDateFinPmt = untilDateFinPmt;
    }

}
