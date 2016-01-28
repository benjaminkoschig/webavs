// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * author fha
 */
public class RFConventionJointAssConFouTsJointConventionAssureManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_BENEFICIAIRES_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forDateDebut = null;
    private String forDateFin = null;
    private String forIdAssure = null;
    private String forIdCofos = null;
    private String forIdConas = null;
    private String forIdConvention = null;
    private String forIdFournisseur = null;
    private String forIdSousTypeSoin = null;
    private String forNSS = null;

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public RFConventionJointAssConFouTsJointConventionAssureManager() {
        super();
        wantCallMethodBeforeFind(false);
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
                    RFConventionJointAssConFouTsJointConventionAssure.createFromClause(_getCollection()));

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

        if (!JadeStringUtil.isEmpty(forIdConvention)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConvention.FIELDNAME_ID_CONVENTION);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdConvention));
        }

        if (!JadeStringUtil.isEmpty(forIdConas)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConventionAssure.FIELDNAME_ID_CONVENTION_ASSURE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdConas));
        }

        if (!JadeStringUtil.isEmpty(forIdCofos)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_CONVFOUSTS);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdCofos));
        }

        if (!JadeStringUtil.isEmpty(forIdFournisseur)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_FOURNISSEUR);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdFournisseur));
        }

        if (!JadeStringUtil.isEmpty(forIdSousTypeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssConventionFournisseurSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdSousTypeSoin));
        }

        if (!JadeStringUtil.isEmpty(forIdAssure)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConventionAssure.FIELDNAME_ID_ASSURE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdAssure));
        }

        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConventionAssure.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
        }

        if (!JadeStringUtil.isEmpty(forDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConventionAssure.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateFin));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LIDossiersJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFConventionJointAssConFouTsJointConventionAssure();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdAssure() {
        return forIdAssure;
    }

    public String getForIdCofos() {
        return forIdCofos;
    }

    public String getForIdConas() {
        return forIdConas;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdSousTypeSoin() {
        return forIdSousTypeSoin;
    }

    public String getForNSS() {
        return forNSS;
    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return "";
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdAssure(String forIdAssure) {
        this.forIdAssure = forIdAssure;
    }

    public void setForIdCofos(String forIdCofos) {
        this.forIdCofos = forIdCofos;
    }

    public void setForIdConas(String forIdConas) {
        this.forIdConas = forIdConas;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdSousTypeSoin(String forIdSousTypeSoin) {
        this.forIdSousTypeSoin = forIdSousTypeSoin;
    }

    public void setForNSS(String forNSS) {
        this.forNSS = forNSS;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
