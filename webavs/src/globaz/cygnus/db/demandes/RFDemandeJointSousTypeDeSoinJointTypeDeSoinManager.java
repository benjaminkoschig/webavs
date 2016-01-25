/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.db.demandes;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFDemandeJointSousTypeDeSoinJointTypeDeSoinManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCodeSousTypeDeSoin = "";

    private String forCodeTypeDeSoin = "";
    private String forDateDebutBetweenDateDebutDem = "";
    private String forDateFinBetweenDateDebutDem = "";
    private String forIdDemandeToIgnore = "";
    private String forIdFournisseur = "";
    private String forIdQdAssure = "";
    private String forIdQdPrincipale = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeManager.
     */
    public RFDemandeJointSousTypeDeSoinJointTypeDeSoinManager() {
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
                    RFDemandeJointSousTypeDeSoinJointTypeDeSoin.createFromClause(_getCollection()));
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
        // String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forIdQdPrincipale)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_QD_PRINCIPALE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdQdPrincipale));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdQdAssure)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_QD_ASSURE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdQdAssure));
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebutBetweenDateDebutDem)
                && !JadeStringUtil.isIntegerEmpty(forDateFinBetweenDateDebutDem)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
            sqlWhere.append(" >= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenDateDebutDem));
            sqlWhere.append(" OR ");
            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" <> 0 AND ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" >= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateDebutBetweenDateDebutDem));
            sqlWhere.append(" )) ");

            sqlWhere.append(" AND ");

            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_FACTURE);
            sqlWhere.append(" <= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenDateDebutDem));
            sqlWhere.append(" OR ");
            sqlWhere.append(" ( ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" <> 0 AND ");
            sqlWhere.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
            sqlWhere.append(" <= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateFinBetweenDateDebutDem));
            sqlWhere.append(" )) ");

        }

        if (!JadeStringUtil.isIntegerEmpty(forCodeTypeDeSoin)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forCodeTypeDeSoin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCodeSousTypeDeSoin)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forCodeSousTypeDeSoin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdFournisseur)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_FOURNISSEUR);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdFournisseur));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDemandeToIgnore)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" <> ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDemandeToIgnore));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeJointSousTypeDeSoinJointTypeDeSoin();
    }

    public String getForCodeSousTypeDeSoin() {
        return forCodeSousTypeDeSoin;
    }

    public String getForCodeTypeDeSoin() {
        return forCodeTypeDeSoin;
    }

    public String getForDateDebutBetweenDateDebutDem() {
        return forDateDebutBetweenDateDebutDem;
    }

    public String getForDateFinBetweenDateDebutDem() {
        return forDateFinBetweenDateDebutDem;
    }

    public String getForIdDemandeToIgnore() {
        return forIdDemandeToIgnore;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdQdAssure() {
        return forIdQdAssure;
    }

    public String getForIdQdPrincipale() {
        return forIdQdPrincipale;
    }

    public void setForCodeSousTypeDeSoin(String forCodeSousTypeDeSoin) {
        this.forCodeSousTypeDeSoin = forCodeSousTypeDeSoin;
    }

    public void setForCodeTypeDeSoin(String forCodeTypeDeSoin) {
        this.forCodeTypeDeSoin = forCodeTypeDeSoin;
    }

    public void setForDateDebutBetweenDateDebutDem(String forDateDebutBetweenDateDebutDem) {
        this.forDateDebutBetweenDateDebutDem = forDateDebutBetweenDateDebutDem;
    }

    public void setForDateFinBetweenDateDebutDem(String forDateFinBetweenDateDebutDem) {
        this.forDateFinBetweenDateDebutDem = forDateFinBetweenDateDebutDem;
    }

    public void setForIdDemandeToIgnore(String forIdDemandeToIgnore) {
        this.forIdDemandeToIgnore = forIdDemandeToIgnore;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdQdAssure(String forIdQdAssure) {
        this.forIdQdAssure = forIdQdAssure;
    }

    public void setForIdQdPrincipale(String forIdQdPrincipale) {
        this.forIdQdPrincipale = forIdQdPrincipale;
    }

}
