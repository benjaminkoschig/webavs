/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import java.util.List;

/**
 * 
 * @author fha
 */
public class RFPrestationAccordeeJointREPrestationAccordeeManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String[] forCsSourcesRfmAccordee = null;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // champ permettant de gérer les recheches avec date d'adaptation (PC)
    private String forDateAdaptation = "";
    private String forDateDiminution = "";
    private String forEnCoursAtMois = "";
    private String forIdDecision = "";
    private String forIdTiersBeneficiaire = "";
    private String forGenrePrestaAccordee = "";
    private boolean forIsDateFinDroitNull = Boolean.FALSE;
    // liste pour permettre la recheche in des tiers
    private List<String> forIdTiersBeneFiciairesIn = null;
    private Boolean forIsAdaptation = Boolean.FALSE;
    private String forOrderBy = "";
    private transient String fromClause = null;
    private boolean isDateDiminutionNull = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFPrestationAccordeeJointREPrestationAccordeeManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer fields = new StringBuffer();

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = null;
        if (fromClause == null) {
            from = new StringBuffer(RFPrestationAccordeeJointREPrestationAccordee.createFromClause(_getCollection()));

            fromClause = from.toString();
        }
        return fromClause;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        // on prend que le requerant
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }

        if (!JadeStringUtil.isEmpty(forDateDiminution)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDiminution));
        }

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isEmpty(forIdTiersBeneficiaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiersBeneficiaire));
        }
        // Si l'idTier pas setter, on regarde la liste des tiers
        else if ((null != forIdTiersBeneFiciairesIn) && (forIdTiersBeneFiciairesIn.size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sqlWhere.append(" IN (");
            int inc = 0;
            for (String idTiers : forIdTiersBeneFiciairesIn) {
                inc++;
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), idTiers));
                if (inc < forIdTiersBeneFiciairesIn.size()) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(")");
        }

        if (!JadeStringUtil.isIntegerEmpty(forEnCoursAtMois)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            // si pas de date de fin ou si date de fin > date donnee
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + "<="
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois) + " AND ( "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL " + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " >= "
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forEnCoursAtMois) + ")");
        }
        // pour adaptataion PC, on recherche les regimes avec date de fin vide et ceux avec date de fin egale a
        // dateAdaptation-1(fin année)
        if (!JadeStringUtil.isIntegerEmpty(forDateAdaptation)) {
            String datePeriodePrecedente = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(forDateAdaptation,
                    -1));

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            // date de fin de droit vide, ou date de fin de droit egale a mois precedent adaptation
            sqlWhere.append(" ( ( " + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = 0" + " OR "
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " IS NULL) OR "
                    // date de fin egale a dateAdaptationAnciennePriode
                    + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " = "
                    + PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(datePeriodePrecedente) + ")");
        }

        if ((null != forCsSourcesRfmAccordee) && (forCsSourcesRfmAccordee.length > 0)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_SOURCE);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String id : forCsSourcesRfmAccordee) {
                inc++;
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), id));
                if (inc < forCsSourcesRfmAccordee.length) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(") ");

        }

        if (isDateDiminutionNull) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION);
            sqlWhere.append(" IS NULL OR ");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION);
            sqlWhere.append(" = 0");
            sqlWhere.append(") ");
        }

        if (forIsAdaptation) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), true, BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if (!JadeStringUtil.isEmpty(forGenrePrestaAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);
            sqlWhere.append(" = ");
            sqlWhere.append(forGenrePrestaAccordee);
        }

        if (forIsDateFinDroitNull) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" IS NULL OR ");
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere.append(" = 0");
            sqlWhere.append(") ");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPrestationAccordeeJointREPrestationAccordee();
    }

    public String[] getForCsSourceRfmAccordee() {
        return forCsSourcesRfmAccordee;
    }

    public String getForDateAdaptation() {
        return forDateAdaptation;
    }

    public String getForDateDiminution() {
        return forDateDiminution;
    }

    public String getForEnCoursAtMois() {
        return forEnCoursAtMois;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public List<String> getForIdTiersBeneFiciaireIn() {
        return forIdTiersBeneFiciairesIn;
    }

    public Boolean getForIsAdaptation() {
        return forIsAdaptation;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getForGenrePrestaAccordee() {
        return forGenrePrestaAccordee;
    }

    public boolean getForIsDateFinDroitNull() {
        return forIsDateFinDroitNull;
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        return null;
    }

    public boolean isDateDiminutionNull() {
        return isDateDiminutionNull;
    }

    public void setDateDiminutionNull(boolean isDateDiminutionNull) {
        this.isDateDiminutionNull = isDateDiminutionNull;
    }

    public void setForCsSourceRfmAccordee(String[] forCsSourcesRfmAccordee) {
        this.forCsSourcesRfmAccordee = forCsSourcesRfmAccordee;
    }

    public void setForDateAdaptation(String forDateAdaptation) {
        this.forDateAdaptation = forDateAdaptation;
    }

    public void setForDateDiminution(String forDateDiminution) {
        this.forDateDiminution = forDateDiminution;
    }

    public void setForEnCoursAtMois(String forEnCoursAtMois) {
        this.forEnCoursAtMois = forEnCoursAtMois;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public void setForIdTiersBeneFiciairesIn(List<String> forIdTiersbeneFiciaireIn) {
        forIdTiersBeneFiciairesIn = forIdTiersbeneFiciaireIn;
    }

    public void setForIsAdaptation(Boolean forIsAdaptation) {
        this.forIsAdaptation = forIsAdaptation;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setForGenrePrestaAccordee(String forGenrePrestaAccordee) {
        this.forGenrePrestaAccordee = forGenrePrestaAccordee;
    }

    public void setForIsDateFinDroitNull(boolean forIsDateFinDroitNull) {
        this.forIsDateFinDroitNull = forIsDateFinDroitNull;
    }

}
