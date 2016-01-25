/*
 * Créé le 3 aout 2011
 */
package globaz.cygnus.db.attestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * author fha
 * 
 * @revision JJE 18.08.2011
 */
public class RFAttestationJointAssAttestationDossierManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_BENEFICIAIRES_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forDate = "";
    private String forIdDossier = "";

    private String forIdSousTypeDeSoin = "";
    private String forOrderBy = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager
     */
    public RFAttestationJointAssAttestationDossierManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append(RFAssAttestationDossier.FIELDNAME_ID_ASSOC_ATTESTATION_DOSSIER).append(",");
        fields.append(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT).append(",");
        fields.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN).append(",");
        fields.append(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER).append(",");
        fields.append(RFAssAttestationDossier.FIELDNAME_ID_ATTESTATION).append(",");
        fields.append(RFAssAttestationDossier.FIELDNAME_ID_SOUS_TYPE_SOIN);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFAttestationJointAssAttestationDossier.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
        return sqlOrder.toString();
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forDate)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDate));
            sqlWhere.append(" AND (");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDate));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = ");
            sqlWhere.append("0)");
        }

        if (!JadeStringUtil.isEmpty(forIdDossier)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        if (!JadeStringUtil.isEmpty(forIdSousTypeDeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_ID_SOUS_TYPE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdSousTypeDeSoin));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFSousTypeDeSoinJointAssPeriodeJointPotAssure)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAttestationJointAssAttestationDossier();
    }

    public String getForDate() {
        return forDate;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdSousTypeDeSoin() {
        return forIdSousTypeDeSoin;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdSousTypeDeSoin(String forIdSousTypeDeSoin) {
        this.forIdSousTypeDeSoin = forIdSousTypeDeSoin;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
