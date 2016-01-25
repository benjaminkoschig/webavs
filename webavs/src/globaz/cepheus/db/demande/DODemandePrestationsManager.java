/*
 * Créé le 26 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.db.demande;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DODemandePrestationsManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsSexe = "";
    private String forDateNaissance = "";
    private String forEtatDemande = "";
    private String forIdTiers = "";
    private String forTypeDemande = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer from = new StringBuffer(DODemandePrestations.createFromClause(_getCollection()));

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS " + IPRTiers.TABLE_AVS_HIST
                    + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "." + IPRTiers.FIELD_TI_IDTIERS + " = "
                    + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS + ")");
        }

        return from.toString();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS()));
        }

        if (!JadeStringUtil.isBlank(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema
                    + DODemandePrestations.TABLE_TIERS
                    + "."
                    + DODemandePrestations.FIELDNAME_NOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likeNom)
                            + "%"));
        }

        if (!JadeStringUtil.isBlank(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema
                    + DODemandePrestations.TABLE_TIERS
                    + "."
                    + DODemandePrestations.FIELDNAME_PRENOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likePrenom)
                            + "%"));
        }

        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection() + DODemandePrestations.TABLE_TIERS_DETAIL + "."
                    + DODemandePrestations.FIELDNAME_DATE_NAISSANCE + "="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JAUtil.isDateEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection() + DODemandePrestations.TABLE_TIERS_DETAIL + "."
                    + DODemandePrestations.FIELDNAME_SEXE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JadeStringUtil.isBlank(forTypeDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_TYPE_DEMANDE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forTypeDemande));
        }

        if (!JadeStringUtil.isBlank(forEtatDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_ETAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forEtatDemande));
        }

        if (!JadeStringUtil.isBlank(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + DODemandePrestations.TABLE_AVS + "." + DODemandePrestations.FIELDNAME_ID_TIERS_TI
                    + "=" + _dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new DODemandePrestations();
    }

    /**
     * @return
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * @return
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return
     */
    public String getForEtatDemande() {
        return forEtatDemande;
    }

    /**
     * @return
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return
     */
    public String getForTypeDemande() {
        return forTypeDemande;
    }

    /**
     * @return
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return
     */
    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    /**
     * @return
     */
    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    /**
     * @return
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderBy()
     */
    @Override
    public String getOrderBy() {
        // TODO Raccord de méthode auto-généré
        return super.getOrderBy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return PRDemande.FIELDNAME_IDTIERS;
    }

    /**
     * @param string
     */
    public void setForCsSexe(String string) {
        forCsSexe = string;
    }

    /**
     * @param string
     */
    public void setForDateNaissance(String string) {
        forDateNaissance = string;
    }

    /**
     * @param string
     */
    public void setForEtatDemande(String string) {
        forEtatDemande = string;
    }

    /**
     * @param string
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    /**
     * @param string
     */
    public void setForTypeDemande(String string) {
        forTypeDemande = string;
    }

    /**
     * @param string
     */
    public void setLikeNom(String string) {
        likeNom = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAVS(String string) {
        likeNumeroAVS = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAVSNNSS(String string) {
        likeNumeroAVSNNSS = string;
    }

    /**
     * @param string
     */
    public void setLikePrenom(String string) {
        likePrenom = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#setOrderBy(java.lang.String)
     */
    @Override
    public void setOrderBy(String string) {
        // TODO Raccord de méthode auto-généré
        super.setOrderBy(string);
    }

}
