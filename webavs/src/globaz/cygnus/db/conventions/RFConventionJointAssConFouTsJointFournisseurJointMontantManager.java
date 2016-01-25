// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.conventions.IRFConventions;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * author jje
 */
public class RFConventionJointAssConFouTsJointFournisseurJointMontantManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_BENEFICIAIRES_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean forActif = Boolean.FALSE;
    private String forCodeSousTypeDeSoin = "";
    private String forCodeTypeDeSoin = "";
    private String forCsGenrePc = "";
    private String forCsTypeBeneficiaire = "";
    private boolean forCsTypeBeneficiairePourTous = false;
    private String forCsTypePc = "";
    private String forDateMontant = "";
    private String forIdFournisseur = "";
    private String forIdSousTypeSoin = "";
    private String forOrderBy = "";

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la
     * classeRFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontantManager .
     */
    public RFConventionJointAssConFouTsJointFournisseurJointMontantManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFConventionJointAssConFouTsJointFournisseurJointMontant.createFromClause(_getCollection()));
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

        if (!JadeStringUtil.isEmpty(forCodeTypeDeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeTypeDeSoin));
        }

        if (!JadeStringUtil.isEmpty(forCodeSousTypeDeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeSousTypeDeSoin));
        }

        if (!JadeStringUtil.isEmpty(forIdSousTypeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdSousTypeSoin));
        }

        if (!JadeStringUtil.isEmpty(forIdFournisseur)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdFournisseur));
        }

        if (forActif.equals(Boolean.TRUE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConvention.FIELDNAME_BOOL_ACTIF);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), "1"));// 1=>vrai
        }

        if (!JadeStringUtil.isEmpty(forDateMontant)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateMontant));
            sqlWhere.append(" AND (");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateMontant));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFMontantsConvention.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 ) ");
        }

        if (!JadeStringUtil.isEmpty(forCsTypeBeneficiaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE);
            if (!forCsTypeBeneficiairePourTous) {
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeBeneficiaire));
            } else {
                sqlWhere.append(" IN (");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeBeneficiaire));
                sqlWhere.append(",");
                sqlWhere.append(IRFTypesBeneficiairePc.POUR_TOUS);
                sqlWhere.append(")");
            }
        }

        if (!JadeStringUtil.isEmpty(forCsTypePc)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFMontantsConvention.FIELDNAME_TYPE_PC);
            /*
             * if (!this.) { sqlWhere.append(" = "); sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
             * this.forCsTypePc)); } else {
             */
            sqlWhere.append(" IN (");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypePc));
            sqlWhere.append(",");
            sqlWhere.append(IRFConventions.CS_TYPE_PC_TOUS);
            sqlWhere.append(")");
        }

        if (!JadeStringUtil.isEmpty(forCsGenrePc)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFMontantsConvention.FIELDNAME_GENRE_PC);
            /*
             * if (!this.) { sqlWhere.append(" = "); sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
             * this.forCsGenrePc)); } else {
             */
            sqlWhere.append(" IN (");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsGenrePc));
            sqlWhere.append(",");
            sqlWhere.append(IRFConventions.CS_GENRE_PC_TOUS);
            sqlWhere.append(")");
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité ( RFConventionJointAssConFouTsJointConventionAssureJointFournisseurJointMontant )
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFConventionJointAssConFouTsJointFournisseurJointMontant();
    }

    public Boolean getForActif() {
        return forActif;
    }

    public String getForCodeSousTypeDeSoin() {
        return forCodeSousTypeDeSoin;
    }

    public String getForCodeTypeDeSoin() {
        return forCodeTypeDeSoin;
    }

    public String getForCsGenrePc() {
        return forCsGenrePc;
    }

    public String getForCsTypeBeneficiaire() {
        return forCsTypeBeneficiaire;
    }

    public String getForCsTypePc() {
        return forCsTypePc;
    }

    public String getForDateMontant() {
        return forDateMontant;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdSousTypeSoin() {
        return forIdSousTypeSoin;
    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isForCsTypeBeneficiairePourTous() {
        return forCsTypeBeneficiairePourTous;
    }

    public void setForActif(Boolean forActif) {
        this.forActif = forActif;
    }

    public void setForCodeSousTypeDeSoin(String forCodeSousTypeDeSoin) {
        this.forCodeSousTypeDeSoin = forCodeSousTypeDeSoin;
    }

    public void setForCodeTypeDeSoin(String forCodeTypeDeSoin) {
        this.forCodeTypeDeSoin = forCodeTypeDeSoin;
    }

    public void setForCsGenrePc(String forCsGenrePc) {
        this.forCsGenrePc = forCsGenrePc;
    }

    public void setForCsTypeBeneficiaire(String forCsTypeBeneficiaire) {
        this.forCsTypeBeneficiaire = forCsTypeBeneficiaire;
    }

    public void setForCsTypeBeneficiairePourTous(boolean forCsTypeBeneficiairePourTous) {
        this.forCsTypeBeneficiairePourTous = forCsTypeBeneficiairePourTous;
    }

    public void setForCsTypePc(String forCsTypePc) {
        this.forCsTypePc = forCsTypePc;
    }

    public void setForDateMontant(String forDateMontant) {
        this.forDateMontant = forDateMontant;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdSousTypeSoin(String forIdSousTypeSoin) {
        this.forIdSousTypeSoin = forIdSousTypeSoin;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
