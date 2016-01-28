/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.hera.tools.SFStringUtils;
import globaz.hera.tools.SFUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFMembreFamilleManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final String ALIAL_TI_AVS = SFMembreFamille.TABLE_AVS;
    protected static final String ALIAS_TI_AVS_HIST = SFMembreFamille.TABLE_AVS_HIST;
    protected static final String SECONDARY_ORDER_BY = " ,WGITIE";

    private String forCsDomaineApplication = "";
    private String forCsPays = new String();
    private String forCsSexe = new String();
    private String forDateNaissance = new String();
    private String forIdMembreFamille = new String();
    private String forIdTiers = "";
    private String likeNom = new String();
    private String likeNumeroAvs = new String();

    private String likeNumeroAvsNNSS = "";
    private String likePrenom = new String();
    private boolean wantConjointInconnu = false;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        // TODOA debugger pour voir si requete est correct !!!

        StringBuffer from = new StringBuffer(SFMembreFamille.createFromClause(_getCollection()));

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAvsNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAvs()))) {
            from.append(" LEFT JOIN " + _getDbSchema() + "." + SFMembreFamille.TABLE_AVS_HIST + " AS "
                    + ALIAS_TI_AVS_HIST + " ON (" + ALIAL_TI_AVS + "." + SFMembreFamille.FIELD_TI_IDTIERS + " = "
                    + ALIAS_TI_AVS_HIST + "." + SFMembreFamille.FIELD_TI_IDTIERS + ")");
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

        StringBuffer order = new StringBuffer(SFMembreFamille.TABLE_AVS + "." + SFMembreFamille.FIELD_AVS_NOAVS);

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAvsNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAvs()))) {
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
        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAvsNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAvs()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += SFUtil.getWhereNSS(_getCollection(), getLikeNumeroAvs(), getlikeNumeroAvsNNSS());
        }

        if (!JadeStringUtil.isEmpty(getLikeNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "("
                    + SFMembreFamille.TABLE_TIERS
                    + "."
                    + SFMembreFamille.FIELD_TI_NOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            SFStringUtils.upperCaseWithoutSpecialChars(getLikeNom()) + "%")
                    + " OR ("
                    + SFMembreFamille.TABLE_NAME
                    + "."
                    + SFMembreFamille.FIELD_NOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            SFStringUtils.upperCaseWithoutSpecialChars(getLikeNom()) + "%") + " AND "
                    + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDTIERS + " = 0) )";
        }

        if (!JadeStringUtil.isEmpty(getLikePrenom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "("
                    + SFMembreFamille.TABLE_TIERS
                    + "."
                    + SFMembreFamille.FIELD_TI_PRENOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            SFStringUtils.upperCaseWithoutSpecialChars(getLikePrenom()) + "%")
                    + " OR ("
                    + SFMembreFamille.TABLE_NAME
                    + "."
                    + SFMembreFamille.FIELD_PRENOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(),
                            SFStringUtils.upperCaseWithoutSpecialChars(getLikePrenom()) + "%") + " AND "
                    + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDTIERS + " = 0) )";
        }

        if (!JAUtil.isDateEmpty(getForDateNaissance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.TABLE_PERS + "." + SFMembreFamille.FIELD_PERS_DATENAI + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateNaissance()) + " OR ("
                    + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_DATENAISSANCE + " = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateNaissance()) + " AND "
                    + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDTIERS + " = 0) )";
        }

        if (!JadeStringUtil.isEmpty(getForCsSexe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.TABLE_PERS + "." + SFMembreFamille.FIELD_PERS_SEX + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsSexe()) + " OR ("
                    + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_SEX + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsSexe()) + " AND "
                    + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDTIERS + " = 0) )";
        }

        if (!JadeStringUtil.isEmpty(getForIdMembreFamille())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDMEMBREFAMILLE + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMembreFamille()) + ")";
        }

        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_IDTIERS + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiers()) + ")";
        }

        if (!JadeStringUtil.isEmpty(getForCsDomaineApplication())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + SFMembreFamille.TABLE_NAME + "." + SFMembreFamille.FIELD_DOMAINE_APPLICATION + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsDomaineApplication()) + ")";
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
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFMembreFamille();
    }

    public String getForCsDomaineApplication() {
        return forCsDomaineApplication;
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
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    /**
     * @return
     */
    public String getlikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
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

    public void setForCsDomaineApplication(String forCsDomaineApplication) {
        this.forCsDomaineApplication = forCsDomaineApplication;
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
    public void setLikeNom(String string) {
        likeNom = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = string;
    }

    /**
     * @param string
     */
    public void setlikeNumeroAvsNNSS(String string) {
        likeNumeroAvsNNSS = string;
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
