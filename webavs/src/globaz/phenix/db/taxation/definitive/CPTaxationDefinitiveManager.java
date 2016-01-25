package globaz.phenix.db.taxation.definitive;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class CPTaxationDefinitiveManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_DECISION_ACOMPTE = "605007";
    public final static String CS_DECISION_CORRECTION = "605003";

    public final static String CS_DECISION_DEFINITIVE = "605002";
    public final static String CS_DECISION_PROVISOIRE = "605001";
    public final static String CS_DECISION_RECTIFICATIVE = "605004";
    public final static String CS_DECISION_REDUCTION = "605009";
    public final static String CS_DECISION_REMISE = "605008";
    public final static String CS_ETAT_PRESTATION_CONTROLE = "52006003";

    public final static String CS_ETAT_PRESTATION_DEFINITIF = "52006005";
    public final static String CS_ETAT_PRESTATION_MIS_LOT = "52006004";
    // Redéfinition des constantes pour éviter de rajouter des liens
    // inter-projet.
    public final static String CS_ETAT_PRESTATION_OUVERT = "52006001";
    public final static String CS_ETAT_PRESTATION_VALIDE = "52006002";
    public final static String CS_TYPE_AFF_NON_ACTIF = "804004";

    public final static String CS_TYPE_AFF_NON_ACTIF_PROVISOIRE = "804007";
    public final static String CS_TYPE_CALC_FORTUNE_TOTALE = "600007";
    public final static String CS_TYPE_CALC_REVENU_NET = "600019";
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private transient String fields = null;
    private String forEtatPrestation = "";

    private String forIdTiers = "";
    private String forIdTiersIn = "";

    private String forNoPassage = "";
    private String forTypeDecisionIn = "";

    private transient String fromClause = null;
    private String orderBy = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = CPTaxationDefinitive.createFields(_getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = CPTaxationDefinitive.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected final String _getOrder(BStatement statement) {
        if (JadeStringUtil.isBlank(orderBy)) {
            return getOrderByDefaut();
        } else {
            return orderBy;
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");

        if (!JadeStringUtil.isIntegerEmpty(forNoPassage)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection());
            sqlWhere.append(CPTaxationDefinitive.TABLE_DECISIONS);
            sqlWhere.append(".");
            sqlWhere.append(CPTaxationDefinitive.FIELDNAME_ID_PASSAGE_DECISION);
            sqlWhere.append("=");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forNoPassage));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection());
            sqlWhere.append(CPTaxationDefinitive.TABLE_TIERS);
            sqlWhere.append(".");
            sqlWhere.append(CPTaxationDefinitive.FIELDNAME_ID_TIERS);
            sqlWhere.append("=");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if (!JadeStringUtil.isEmpty(forIdTiersIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection());
            sqlWhere.append(CPTaxationDefinitive.TABLE_TIERS);
            sqlWhere.append(".");
            sqlWhere.append(CPTaxationDefinitive.FIELDNAME_ID_TIERS);
            sqlWhere.append(" IN (");
            sqlWhere.append(forIdTiersIn);
            sqlWhere.append(") ");
        }

        if (!JadeStringUtil.isIntegerEmpty(dateDebutPeriode)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection());
            sqlWhere.append(CPTaxationDefinitive.TABLE_PREST_APG);
            sqlWhere.append(".");
            sqlWhere.append(CPTaxationDefinitive.FIELDNAME_DATE_DEBUT_PRST);
            sqlWhere.append(">=");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), dateDebutPeriode));
        }

        if (!JadeStringUtil.isIntegerEmpty(dateFinPeriode)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection());
            sqlWhere.append(CPTaxationDefinitive.TABLE_PREST_APG);
            sqlWhere.append(".");
            sqlWhere.append(CPTaxationDefinitive.FIELDNAME_DATE_FIN_PRST);
            sqlWhere.append("<=");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), dateFinPeriode));
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtatPrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection());
            sqlWhere.append(CPTaxationDefinitive.TABLE_PREST_APG);
            sqlWhere.append(".");
            sqlWhere.append(CPTaxationDefinitive.FIELDNAME_ETAT_PRST);
            sqlWhere.append("=");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forEtatPrestation));
        }

        if (!JadeStringUtil.isBlankOrZero(forTypeDecisionIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            // Que les décision cot. pers de type : définitive ou rectificative
            sqlWhere.append(_getCollection());
            sqlWhere.append(CPTaxationDefinitive.TABLE_DECISIONS);
            sqlWhere.append(".");
            sqlWhere.append(CPTaxationDefinitive.FIELDNAME_TYPE_DECISION_DECISON);
            sqlWhere.append(" IN (").append(forTypeDecisionIn).append(")");

        }

        // Dans tous les cas....
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        // //Que les indépendants
        // sqlWhere.append(_getCollection());
        // sqlWhere.append(CPTaxationDefinitive.TABLE_SIT_PROF);
        // sqlWhere.append(".");
        // sqlWhere.append(CPTaxationDefinitive.FIELDNAME_IS_INDEPENDANT);
        // sqlWhere.append("=");
        // sqlWhere.append(_dbWriteString(statement.getTransaction(), "1"));
        // sqlWhere.append(" AND ");

        // Que les prestations durant la période de la décision cot pers.
        // définitive

        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_PREST_APG);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_DATE_DEBUT_PRST);
        sqlWhere.append(">=");
        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_DECISIONS);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_DATE_DEBUT_DECISION);

        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_PREST_APG);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_DATE_FIN_PRST);
        sqlWhere.append("<=");
        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_DECISIONS);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_DATE_FIN_DECISION);

        // AND WEBAVS.CPDECIP.IAACTI='1'
        // Que les décisions actives
        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_DECISIONS);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_IS_DECISION_ACTIVE);
        sqlWhere.append("=");
        sqlWhere.append(_dbWriteBoolean(statement.getTransaction(), Boolean.TRUE, BConstants.DB_TYPE_BOOLEAN_CHAR));

        // Type de calcul fortune ou revenu net
        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_DOCAP);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_TYPE_CALCUL_DOCAP);
        sqlWhere.append(" IN (").append(CPTaxationDefinitiveManager.CS_TYPE_CALC_FORTUNE_TOTALE);
        sqlWhere.append(",").append(CPTaxationDefinitiveManager.CS_TYPE_CALC_REVENU_NET);
        sqlWhere.append(") ");

        // Exclure les affiliations des non-actifs et non actif-provisoire
        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_AFFILIATIONS);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_TYPE_AFFILIATION);
        sqlWhere.append(" <> ");
        sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), CS_TYPE_AFF_NON_ACTIF));

        sqlWhere.append(" AND ");
        sqlWhere.append(_getCollection());
        sqlWhere.append(CPTaxationDefinitive.TABLE_AFFILIATIONS);
        sqlWhere.append(".");
        sqlWhere.append(CPTaxationDefinitive.FIELDNAME_TYPE_AFFILIATION);
        sqlWhere.append(" <> ");
        sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), CS_TYPE_AFF_NON_ACTIF_PROVISOIRE));

        return sqlWhere.toString();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPTaxationDefinitive();
    }

    public String getForEtatPrestation() {
        return forEtatPrestation;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTiersIn() {
        return forIdTiersIn;
    }

    public String getForNoPassage() {
        return forNoPassage;
    }

    public String getForTypeDecisionIn() {
        return forTypeDecisionIn;
    }

    /**
     * getter pour l'attribut order by
     * 
     * @return la valeur courante de l'attribut order by
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @return la valeur courante de l'attribut order by defaut
     **/
    public String getOrderByDefaut() {
        return CPTaxationDefinitive.FIELDNAME_NSS;
    }

    public void setForEtatPrestation(String forEtatPrestation) {
        this.forEtatPrestation = forEtatPrestation;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTiersIn(String forIdTiersIn) {
        this.forIdTiersIn = forIdTiersIn;
    }

    public void setForNoPassage(String forNoPassage) {
        this.forNoPassage = forNoPassage;
    }

    /**
     * setter pour l'attribut for periode.
     * 
     * @param dateDebutPeriode
     *            une nouvelle valeur pour cet attribut
     * @param dateFinPeriode
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPeriode(String dateDebutPeriode, String dateFinPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setForTypeDecisionIn(String forTypeDecisionIn) {
        this.forTypeDecisionIn = forTypeDecisionIn;
    }

    /**
     * setter pour l'attribut order by
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }
}
