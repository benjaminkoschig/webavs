/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author bsc
 * 
 */

public class REPrestationAccordeeManager extends PRAbstractManager {

    // ~ Static fields/initializers -----------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String FIELDNAME_LEVEL = " LEVEL ";

    private static final String LEVEL_1 = " 1 ";
    private static final String LEVEL_1_IN_CODE = " IN ('10', '13', '20', '23', '50', '70', '72') ";
    private static final String LEVEL_2 = " 2 ";
    private static final String LEVEL_2_IN_CODE = " IN ('81', '82', '83', '84', '85', '86', '87', '88', '89', '91', '92', '93', '94', '95', '96', '97') ";

    private static final String LEVEL_4 = " 4 ";
    private static final String LEVEL_4_IN_CODE = " IN ('33', '53', '73') ";
    private static final String LEVEL_5 = " 5 ";
    private static final String LEVEL_5_IN_CODE = " IN ('14', '15', '16', '24', '25', '26', '34', '35', '36', '45', '55', '74', '75') ";

    private static final String REPRSTACC_FIELDS = " ZTIPRA, ZTITBE,  ZTMPRE," + " ZTDDDR, ZTDFDR, ZTTGEN, "
            + " ZTTETA, ZTIDPA, ZTLCPR, " + " ZTIICT, ZTLRFP, ZTBRET, ZTICIM";

    private final static String EQUALS = "=";
    private final static String DIFFRENT = "<>";
    private final static String LESS_OR_EQUALS = "<=";
    private final static String GREATHER_OR_EQUALS = ">=";

    // ~ I nstance fields
    // ------------------------------------------------------------------------------------------------

    private String forCodesPrestationsIn = "";
    private String forCsEtat = "";
    private String forCsEtatDifferentDe = "";
    private String forCsEtatIn = "";
    private String forCsEtatNotIn = "";
    private String forEchuesADate = "";
    private String forEnCoursAtMois = "";
    private String forIdCalulInteretMoratoire = "";
    private String forIdRenteAccordee = "";
    private String forIdsRentesAccordees = "";
    private String forIdTiersBeneficiaire = "";
    private String forIdTiersBeneficiaireIn = "";
    private Boolean forIsAttenteMajBlocage = null;
    private Boolean forIsPrestationBloquee = null;
    private String forGenreRentes = null;

    public String getForGenreRentes() {
        return forGenreRentes;
    }

    public void setForGenreRentes(String forGenreRentes) {
        this.forGenreRentes = forGenreRentes;
    }

    private Boolean forRenteAccordeeWithoutDateFin = null;
    private String fromDateFin = "";
    private String toDateDebut = "";

    protected boolean wantLevelField = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(final BStatement statement) {
        if (wantLevelField) {
            StringBuffer sqlFields = new StringBuffer();

            // les champs de la tables des rentes accordees
            sqlFields.append(REPrestationAccordeeManager.REPRSTACC_FIELDS);

            // le champs LEVEL
            sqlFields.append(", CASE ");
            sqlFields.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                    + REPrestationAccordeeManager.LEVEL_1_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REPrestationAccordeeManager.LEVEL_1);
            sqlFields.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                    + REPrestationAccordeeManager.LEVEL_2_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REPrestationAccordeeManager.LEVEL_2);
            sqlFields.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                    + REPrestationAccordeeManager.LEVEL_4_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REPrestationAccordeeManager.LEVEL_4);
            sqlFields.append(" WHEN " + REPrestationsAccordees.FIELDNAME_CODE_PRESTATION
                    + REPrestationAccordeeManager.LEVEL_5_IN_CODE);
            sqlFields.append(" THEN ");
            sqlFields.append(REPrestationAccordeeManager.LEVEL_5);

            sqlFields.append(" END AS " + REPrestationAccordeeManager.FIELDNAME_LEVEL);

            return sqlFields.toString();
        } else {
            return super._getFields(statement);
        }
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
    protected String _getWhere(final BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder("");

        if (!JadeStringUtil.isBlank(forGenreRentes)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE).append(EQUALS)
                    .append(_dbWriteNumeric(statement.getTransaction(), forGenreRentes));

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRenteAccordee)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(EQUALS)
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdRenteAccordee));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(EQUALS)
                    .append(this._dbWriteNumeric(statement.getTransaction(), forCsEtat));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtatDifferentDe)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(DIFFRENT)
                    .append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatDifferentDe));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdCalulInteretMoratoire)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE).append(EQUALS)
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdCalulInteretMoratoire));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBeneficiaire)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(EQUALS)
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdTiersBeneficiaire));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersBeneficiaireIn)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(" IN (")
                    .append(forIdTiersBeneficiaireIn).append(") ");
        }

        if (!JadeStringUtil.isBlankOrZero(forEnCoursAtMois)) {

            appendAndInWherClauseIfNeeded(sqlWhere);

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(LESS_OR_EQUALS)
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois)).append(" AND ( ")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" = 0").append(" OR ")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL ").append(" OR ")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(GREATHER_OR_EQUALS)
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois)).append(")");

        }

        if ((forRenteAccordeeWithoutDateFin != null) && forRenteAccordeeWithoutDateFin.booleanValue()) {

            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(" ( " + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" = 0").append(" OR ")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL ").append(")");
        }

        if (!JadeStringUtil.isBlankOrZero(fromDateFin)) {

            appendAndInWherClauseIfNeeded(sqlWhere);

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere.append(" (" + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(">=")
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(fromDateFin)).append(" OR ")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" = 0").append(" OR ")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL ) ");
        }

        if (!JadeStringUtil.isBlankOrZero(toDateDebut)) {

            appendAndInWherClauseIfNeeded(sqlWhere);

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append("<=")
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(toDateDebut));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatIn)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" IN (").append(forCsEtatIn)
                    .append(" )");
        }

        if (!JadeStringUtil.isEmpty(forCsEtatNotIn)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" NOT IN (")
                    .append(forCsEtatNotIn + " )");
        }

        if (!JadeStringUtil.isEmpty(forCodesPrestationsIn)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" IN (")
                    .append(forCodesPrestationsIn).append(" )");
        }

        if (!JadeStringUtil.isEmpty(forIdsRentesAccordees)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                    .append(" IN (" + forIdsRentesAccordees + " )");
        }

        if (getForIsPrestationBloquee() != null) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            if (getForIsPrestationBloquee().booleanValue()) {
                sqlWhere.append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                        .append(EQUALS)
                        .append(this._dbWriteBoolean(statement.getTransaction(), getForIsPrestationBloquee(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR));

            } else {
                sqlWhere.append("(")
                        .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                        .append("=")
                        .append(this._dbWriteBoolean(statement.getTransaction(), getForIsPrestationBloquee(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR));

                sqlWhere.append(" OR ").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                        .append("= '0' OR ").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                        .append(" IS NULL )");

            }
        }

        if (getForIsAttenteMajBlocage() != null) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            if (getForIsAttenteMajBlocage().booleanValue()) {
                sqlWhere.append(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE)
                        .append(EQUALS)
                        .append(this._dbWriteBoolean(statement.getTransaction(), getForIsAttenteMajBlocage(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR));

            } else {
                sqlWhere.append("(")
                        .append(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE)
                        .append(EQUALS)
                        .append(this._dbWriteBoolean(statement.getTransaction(), getForIsAttenteMajBlocage(),
                                BConstants.DB_TYPE_BOOLEAN_CHAR));

                sqlWhere.append(" OR ").append(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE)
                        .append("= '0' OR ").append(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE)
                        .append(" IS NULL )");

            }
        }

        if (!JadeStringUtil.isBlankOrZero(forEchuesADate)) {
            appendAndInWherClauseIfNeeded(sqlWhere);

            sqlWhere.append(("(")).append((REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE)).append(" < ")
                    .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEchuesADate)).append((" AND "))
                    .append(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE).append(" <> 0 AND ")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE).append(" IS NOT NULL").append(") ");

        }

        return sqlWhere.toString();
    }

    private void appendAndInWherClauseIfNeeded(StringBuilder whereClause) {
        if (whereClause.length() != 0) {
            whereClause.append(" AND ");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationsAccordees();
    }

    public String getForCodesPrestationsIn() {
        return forCodesPrestationsIn;
    }

    /**
     * @return the forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * @return the forCsEtatDifferentDe
     */
    public String getForCsEtatDifferentDe() {
        return forCsEtatDifferentDe;
    }

    /**
     * @return
     */
    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsEtatNotIn() {
        return forCsEtatNotIn;
    }

    public String getForEchuesADate() {
        return forEchuesADate;
    }

    /**
     * @return
     */

    public String getForIdCalulInteretMoratoire() {
        return forIdCalulInteretMoratoire;
    }

    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public String getForIdsRentesAccordees() {
        return forIdsRentesAccordees;
    }

    /**
     * @return the forIdTiersBeneficiaire
     */
    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public String getForIdTiersBeneficiaireIn() {
        return forIdTiersBeneficiaireIn;
    }

    public Boolean getForIsAttenteMajBlocage() {
        return forIsAttenteMajBlocage;
    }

    public Boolean getForIsPrestationBloquee() {
        return forIsPrestationBloquee;
    }

    public Boolean getForRenteAccordeeWithoutDateFin() {
        return forRenteAccordeeWithoutDateFin;
    }

    public String getFromDateFin() {
        return fromDateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        if (wantLevelField) {
            return REPrestationAccordeeManager.FIELDNAME_LEVEL;
        } else {
            return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
        }
    }

    public String getToDateDebut() {
        return toDateDebut;
    }

    public boolean isWantLevelField() {
        return wantLevelField;
    }

    public void setForCodesPrestationsIn(final String forCodesPrestationsIn) {
        this.forCodesPrestationsIn = forCodesPrestationsIn;
    }

    /**
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(final String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    /**
     * @param forCsEtatDifferentDe
     *            the forCsEtatDifferentDe to set
     */
    public void setForCsEtatDifferentDe(final String forCsEtatDifferentDe) {
        this.forCsEtatDifferentDe = forCsEtatDifferentDe;
    }

    /**
     * @param string
     */
    public void setForCsEtatIn(final String string) {
        forCsEtatIn = string;
    }

    public void setForCsEtatNotIn(final String forCsEtatNotIn) {
        this.forCsEtatNotIn = forCsEtatNotIn;
    }

    /**
     * 
     * @param forEchuesADate
     *            format : mm.aaaa
     */
    public void setForEchuesADate(final String forEchuesADate) {
        this.forEchuesADate = forEchuesADate;
    }

    /**
     * @param string
     */
    public void setForEnCoursAtMois(final String string) {
        forEnCoursAtMois = string;
    }

    public void setForIdCalulInteretMoratoire(final String forIdCalulInteretMoratoire) {
        this.forIdCalulInteretMoratoire = forIdCalulInteretMoratoire;
    }

    /**
     * @param string
     */
    public void setForIdRenteAccordee(final String string) {
        forIdRenteAccordee = string;
    }

    public void setForIdsRentesAccordees(final String forIdsRentesAccordees) {
        this.forIdsRentesAccordees = forIdsRentesAccordees;
    }

    /**
     * @param forIdTiersBeneficiaire
     *            the forIdTiersBeneficiaire to set
     */
    public void setForIdTiersBeneficiaire(final String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public void setForIdTiersBeneficiaireIn(final String forIdTiersBeneficiaireIn) {
        this.forIdTiersBeneficiaireIn = forIdTiersBeneficiaireIn;
    }

    public void setForIsAttenteMajBlocage(final Boolean forIsAttenteMajBlocage) {
        this.forIsAttenteMajBlocage = forIsAttenteMajBlocage;
    }

    public void setForIsPrestationBloquee(final Boolean forIsPrestationBloquee) {
        this.forIsPrestationBloquee = forIsPrestationBloquee;
    }

    public void setForRenteAccordeeWithoutDateFin(final Boolean forRenteAccordeeWithoutDateFin) {
        this.forRenteAccordeeWithoutDateFin = forRenteAccordeeWithoutDateFin;
    }

    public void setFromDateFin(final String fromDateFin) {
        this.fromDateFin = fromDateFin;
    }

    public void setToDateDebut(final String toDateDebut) {
        this.toDateDebut = toDateDebut;
    }

    /**
     * @param b
     */
    public void setWantLevelField(final boolean b) {
        wantLevelField = b;
    }

    /**
     * @return
     */
    public boolean wantLevelField() {
        return wantLevelField;
    }
}
