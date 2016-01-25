/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.ArrayList;

/**
 * 
 * @author fha
 */
public class RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE = "CHAMP_ID_TIERS_BENFICIAIRE";
    public static final String ALIAS_CHAMP_MONTANT_FILS = "CHAMP_MONTANT_FILS";
    public static final String ALIAS_CHAMP_MONTANT_PARENT = "CHAMP_MONTANT_PARENT";
    public static final String ALIAS_CHAMP_SOURCE = "CHAMP_SOURCE";
    public static final String ALIAS_CHAMP_TYPE_PRESTATION_FILS = "CHAMP_TYPE_PRESTATION_FILS";
    public static final String ALIAS_CHAMP_TYPE_PRESTATION_PARENT = "CHAMP_TYPE_PRESTATION_PARENT";

    private String[] forCsSourcesRfmAccordee = null;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private ArrayList<String> forCsTypesPrestationsAccordees = null;
    private String forDateAugmentation = "";
    private String forDateDiminution = "";
    private boolean forIsAdaptation = false;
    private boolean forIsDiminutionsAndAdaptations = false;
    private String forOrderBy = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer fields = new StringBuffer();

        String point = ".";
        String virgule = ", ";

        // Fields pour récupérer les diminutions et les adaptations
        if (forIsDiminutionsAndAdaptations) {
            // récupération du nss
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_NUM_AVS);
            fields.append(virgule);

            // récupération du nom
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_NOM);
            fields.append(virgule);

            // récupération du prénom
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_PRENOM);
            fields.append(virgule);

            // récupération du montant de la prestation parent
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            fields.append(point);
            fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_MONTANT_PARENT);

            // récupération du montant de la prestation fils
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_FILS);
            fields.append(point);
            fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_MONTANT_FILS);

            // récupération de la source de la prestation
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fields.append(point);
            fields.append(RFPrestationAccordee.FIELDNAME_CS_SOURCE);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_SOURCE);

            // récupération du type de prestation du parent
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fields.append(point);
            fields.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_TYPE_PRESTATION_PARENT);

            // récupération du type de prestation du fils
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fields.append(point);
            fields.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_TYPE_PRESTATION_FILS);

        }
        // Fields pour récuprer les augmentations
        else {

            // récupération du nss
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_NUM_AVS);
            fields.append(virgule);

            // récupération du nom
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_NOM);
            fields.append(virgule);

            // récupération du prénom
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_PRENOM);
            fields.append(virgule);

            // récupération du montant de la prestation parent
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            fields.append(point);
            fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_MONTANT_PARENT);

            // récupération du montant de la prestation fils
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_FILS);
            fields.append(point);
            fields.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_MONTANT_FILS);

            // récupération de la source de la prestation
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fields.append(point);
            fields.append(RFPrestationAccordee.FIELDNAME_CS_SOURCE);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_SOURCE);

            // récupération du type de prestation du parent
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fields.append(point);
            fields.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_TYPE_PRESTATION_PARENT);

            // récupération du type de prestation du fils
            fields.append(virgule);
            fields.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fields.append(point);
            fields.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
            fields.append(" "
                    + RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_TYPE_PRESTATION_FILS);

        }

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = null;
        if (fromClause == null) {
            from = new StringBuffer(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.createFromClause(
                    _getCollection(), forIsDiminutionsAndAdaptations));

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
        String point = ".";
        String egal = " = ";

        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }

        if (forIsAdaptation) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            sqlWhere.append(point);
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), true, BConstants.DB_TYPE_BOOLEAN_CHAR));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            sqlWhere.append(point);
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION);
            sqlWhere.append(" IS NULL ");
            sqlWhere.append(")");

            sqlWhere.append(" AND ");

            sqlWhere.append("(");
            sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            sqlWhere.append(point);
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), true, BConstants.DB_TYPE_BOOLEAN_CHAR));

            sqlWhere.append(" OR ");
            sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            sqlWhere.append(point);
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_IS_ADAPTATION);
            sqlWhere.append(" IS NULL ");

            sqlWhere.append(")");

        }

        if (forIsDiminutionsAndAdaptations) {

            if (forCsTypesPrestationsAccordees != null) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
                sqlWhere.append(point);
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
                sqlWhere.append(" IN (");
                int inc = 0;
                for (String typePrestation : forCsTypesPrestationsAccordees) {
                    inc++;
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), typePrestation));
                    if (inc < forCsTypesPrestationsAccordees.size()) {
                        sqlWhere.append(",");
                    }
                }
                sqlWhere.append(")");

            }

            if (!JadeStringUtil.isEmpty(forDateDiminution)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
                sqlWhere.append(".");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_DIMINUTION);
                sqlWhere.append(egal);
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDiminution));

            }

        } else {

            if (!JadeStringUtil.isEmpty(forDateAugmentation)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
                sqlWhere.append(".");
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_AUGMENTATION);
                sqlWhere.append(egal);
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateAugmentation));

            }

            if (forCsTypesPrestationsAccordees != null) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
                sqlWhere.append(point);
                sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
                sqlWhere.append(" IN (");
                int inc = 0;
                for (String typePrestation : forCsTypesPrestationsAccordees) {
                    inc++;
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), typePrestation));
                    if (inc < forCsTypesPrestationsAccordees.size()) {
                        sqlWhere.append(",");
                    }
                }
                sqlWhere.append(")");
            }

            sqlWhere.append(" AND ");
            sqlWhere.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            sqlWhere.append(point);
            sqlWhere.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
            sqlWhere.append(" IS NULL ");
        }

        return sqlWhere.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation(forIsDiminutionsAndAdaptations);
    }

    public String[] getForCsSourceRfmAccordee() {
        return forCsSourcesRfmAccordee;
    }

    public ArrayList<String> getForCsTypesPrestationsAccordees() {
        return forCsTypesPrestationsAccordees;
    }

    public String getForDateDiminution() {
        return forDateDiminution;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        return null;
    }

    public boolean isForIsAdaptation() {
        return forIsAdaptation;
    }

    public boolean isForIsDiminutionsAndAdaptations() {
        return forIsDiminutionsAndAdaptations;
    }

    public void setForCsSourceRfmAccordee(String[] forCsSourcesRfmAccordee) {
        this.forCsSourcesRfmAccordee = forCsSourcesRfmAccordee;
    }

    public void setForCsTypesPrestationsAccordees(ArrayList<String> forCsTypesPrestationsAccordees) {
        this.forCsTypesPrestationsAccordees = forCsTypesPrestationsAccordees;
    }

    public void setForDateAugmentation(String forDateAugmentation) {
        this.forDateAugmentation = forDateAugmentation;
    }

    public void setForDateDiminution(String forDateDiminution) {
        this.forDateDiminution = forDateDiminution;
    }

    public void setForIsAdaptation(boolean forIsAdaptation) {
        this.forIsAdaptation = forIsAdaptation;
    }

    public void setForIsDiminutionsAndAdaptations(boolean forIsAugmentation) {
        forIsDiminutionsAndAdaptations = forIsAugmentation;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
