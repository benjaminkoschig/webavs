/*
 * Créé le 5 juil. 07
 */

package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author HPE
 * 
 */

public class RESituationFamilialeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final String ALIAL_TI_AVS = RESituationFamiliale.TABLE_AVS;
    protected static final String ALIAS_TI_AVS_HIST = RESituationFamiliale.TABLE_AVS_HIST;
    protected static final String SECONDARY_ORDER_BY = " ,WGITIE";

    private String forCsDomainesIn = new String();
    private String forCsPays = new String();
    private String forCsSexe = new String();
    private String forDateNaissance = new String();
    private String forIdMembreFamille = new String();
    private String forIdTiers = "";
    private String likeNoAvs = new String();
    private String likeNoAvsNNSS = "";

    private String likeNom = new String();
    private String likePrenom = new String();
    private boolean wantConjointInconnu = false;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer from = new StringBuffer(RESituationFamiliale.createFromClause(_getCollection()));

        if ((!JadeStringUtil.isBlankOrZero(likeNoAvsNNSS) && !JadeStringUtil.isEmpty(getLikeNoAvs()))) {
            from.append(" LEFT JOIN " + _getDbSchema() + "." + RESituationFamiliale.TABLE_AVS_HIST + " AS "
                    + ALIAS_TI_AVS_HIST + " ON (" + ALIAL_TI_AVS + "." + RESituationFamiliale.FIELD_TI_IDTIERS + " = "
                    + ALIAS_TI_AVS_HIST + "." + RESituationFamiliale.FIELD_TI_IDTIERS + ")");
        }

        return from.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer order = new StringBuffer(RESituationFamiliale.TABLE_AVS + "."
                + RESituationFamiliale.FIELD_AVS_NOAVS);

        if ((!JadeStringUtil.isBlankOrZero(likeNoAvsNNSS) && !JadeStringUtil.isEmpty(getLikeNoAvs()))) {
            order.append(" ,WGITIE");
        }

        return order.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // Pour l'instant, cela produit l'effet suivant :
        // si recherche par NNSS, retourne que des NNSS
        // si recherche par NAVS, retourne toutes les sortes de n° AVS
        // Problème ?
        // --> N'affiche pas tous les membres de la famille quand la case est
        // cochée
        // si certains membres ont des NNSS et d'autres des NAVS
        // --> uniquement dans le cas d'une recherche avec NNSS, sinon tout est
        // affiché
        if ((!JadeStringUtil.isBlankOrZero(likeNoAvsNNSS) && !JadeStringUtil.isEmpty(getLikeNoAvs()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNoAvs(), getlikeNoAvsNNSS());
        }

        if (!JadeStringUtil.isEmpty(getLikeNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "("
                    + RESituationFamiliale.TABLE_TIERS
                    + "."
                    + RESituationFamiliale.FIELD_TI_NOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(getLikeNom()) + "%")
                    + " OR ("
                    + RESituationFamiliale.TABLE_NAME
                    + "."
                    + RESituationFamiliale.FIELD_NOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(getLikeNom()) + "%") + " AND "
                    + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_IDTIERS + " = 0) )";
        }

        if (!JadeStringUtil.isEmpty(getLikePrenom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "("
                    + RESituationFamiliale.TABLE_TIERS
                    + "."
                    + RESituationFamiliale.FIELD_TI_PRENOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(getLikePrenom()) + "%")
                    + " OR ("
                    + RESituationFamiliale.TABLE_NAME
                    + "."
                    + RESituationFamiliale.FIELD_PRENOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(getLikePrenom()) + "%") + " AND "
                    + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_IDTIERS + " = 0) )";
        }

        if (!JAUtil.isDateEmpty(getForDateNaissance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + RESituationFamiliale.TABLE_PERS + "." + RESituationFamiliale.FIELD_PERS_DATENAI + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateNaissance()) + " OR ("
                    + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_DATENAISSANCE + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateNaissance()) + " AND "
                    + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_IDTIERS + " = 0) )";
        }

        if (!JadeStringUtil.isEmpty(getForCsSexe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + RESituationFamiliale.TABLE_PERS + "." + RESituationFamiliale.FIELD_PERS_SEX + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsSexe()) + " OR ("
                    + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_SEX + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsSexe()) + " AND "
                    + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_IDTIERS + " = 0) )";
        }

        if (!JadeStringUtil.isEmpty(getForIdMembreFamille())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_IDMEMBREFAMILLE
                    + " = " + _dbWriteNumeric(statement.getTransaction(), getForIdMembreFamille()) + ")";
        }

        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_IDTIERS + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiers()) + ")";
        }

        if (!JadeStringUtil.isEmpty(getForCsDomainesIn())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += RESituationFamiliale.TABLE_NAME + "." + RESituationFamiliale.FIELD_CS_DOMAINE + " IN ("
                    + getForCsDomainesIn() + ") ";
        }

        if (!wantConjointInconnu) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " WGIMEF < " + _dbWriteNumeric(statement.getTransaction(), "999999999999");
        }

        return sqlWhere;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RESituationFamiliale();
    }

    public String getForCsDomainesIn() {
        return forCsDomainesIn;
    }

    /**
     * @return
     */
    public String getForCsPays() {
        return forCsPays;
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
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
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
    public String getLikeNoAvs() {
        return likeNoAvs;
    }

    /**
     * @return
     */
    public String getlikeNoAvsNNSS() {
        return likeNoAvsNNSS;
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
    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * @return
     */
    public boolean isWantConjointInconnu() {
        return wantConjointInconnu;
    }

    public void setForCsDomainesIn(String forCsDomainesIn) {
        this.forCsDomainesIn = forCsDomainesIn;
    }

    /**
     * @param string
     */
    public void setForCsPays(String string) {
        forCsPays = string;
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
    public void setForIdMembreFamille(String string) {
        forIdMembreFamille = string;
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
    public void setLikeNoAvs(String string) {
        likeNoAvs = string;
    }

    /**
     * @param string
     */
    public void setlikeNoAvsNNSS(String string) {
        likeNoAvsNNSS = string;
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
    public void setLikePrenom(String string) {
        likePrenom = string;
    }

    /**
     * @param b
     */
    public void setWantConjointInconnu(boolean b) {
        wantConjointInconnu = b;
    }

}
