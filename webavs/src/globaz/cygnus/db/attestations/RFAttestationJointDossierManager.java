/*
 * Créé le 31 août 2011
 */
package globaz.cygnus.db.attestations;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * author fha
 */
public class RFAttestationJointDossierManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_BENEFICIAIRES_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String csTypeAttestation = "";
    private String dateCreation = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String forIdDossier = "";
    private String forIdSousTypeSoin = "";
    private String forOrderBy = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager
     */
    public RFAttestationJointDossierManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFAttestationJointDossier.createFromClause(_getCollection()));

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

        // code systeme
        if (!JadeStringUtil.isEmpty(csTypeAttestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAttestation.FIELDNAME_TYPE_DOCUMENT);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), csTypeAttestation));
        }

        if (!JadeStringUtil.isEmpty(dateCreation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAttestation.FIELDNAME_DATE_CREATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), dateCreation));
        }

        /****************/

        if (!JadeStringUtil.isBlankOrZero(dateDebut)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            if (!JadeStringUtil.isBlankOrZero(dateFin)) {
                sqlWhere.append(" (( ");
            } else {
                sqlWhere.append(" ( ");
            }
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), dateDebut));
            sqlWhere.append(" AND (");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), dateDebut));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ) ) ");
        }

        if (!JadeStringUtil.isBlankOrZero(dateFin)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" OR ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            if (RFUtils.MAX_DATE_VALUE.equals(dateFin)) {
                sqlWhere.append(dateFin);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), dateFin));
            }
            sqlWhere.append(" AND (");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            if (RFUtils.MAX_DATE_VALUE.equals(dateFin)) {
                sqlWhere.append(dateFin);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), dateFin));
            }
            sqlWhere.append(" OR ");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ))");

            sqlWhere.append(" OR ");
            sqlWhere.append(" ( ");

            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" >= ");
            if (RFUtils.MAX_DATE_VALUE.equals(dateDebut)) {
                sqlWhere.append(dateDebut);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), dateDebut));
            }
            sqlWhere.append(" AND (");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" <= ");
            if (RFUtils.MAX_DATE_VALUE.equals(dateFin)) {
                sqlWhere.append(dateFin);
            } else {
                sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), dateFin));
            }
            sqlWhere.append(" AND ");
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN);
            sqlWhere.append(" <> 0 ))");
            sqlWhere.append(" ) ");
        }

        /*
         * if (!JadeStringUtil.isEmpty(this.dateFin) && !JadeStringUtil.isEmpty(this.dateDebut)) { if (sqlWhere.length()
         * != 0) { sqlWhere.append(" AND "); } sqlWhere.append("NOT(");
         * sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_FIN); sqlWhere.append(" < ");
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.dateDebut));
         * 
         * sqlWhere.append(" OR ");
         * 
         * sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), this.dateFin)); sqlWhere.append(" < ");
         * sqlWhere.append(RFAssAttestationDossier.FIELDNAME_DATE_DEBUT); sqlWhere.append(")"); }
         */

        if (!JadeStringUtil.isEmpty(forIdDossier)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        if (!JadeStringUtil.isEmpty(codeSousTypeDeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssAttestationDossier.FIELDNAME_ID_SOUS_TYPE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), codeSousTypeDeSoin));
        }

        if (!JadeStringUtil.isEmpty(codeTypeDeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), codeTypeDeSoin));
        }

        if (!JadeStringUtil.isEmpty(forIdSousTypeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdSousTypeSoin));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFSousTypeDeSoinJointAssPeriodeJointPotAssure)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAttestationJointDossier();
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCsTypeAttestation() {
        return csTypeAttestation;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdSousTypeSoin() {
        return forIdSousTypeSoin;
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

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCsTypeAttestation(String csTypeAttestation) {
        this.csTypeAttestation = csTypeAttestation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdSousTypeSoin(String forIdSousTypeSoin) {
        this.forIdSousTypeSoin = forIdSousTypeSoin;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
