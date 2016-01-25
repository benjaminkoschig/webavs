package globaz.corvus.db.prestations;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author bsc
 * 
 */

public class REPrestationsJointDemandeRenteManager extends REPrestationsManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCsSexe = "";
    private String forDateNaissance = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);

        // jointure entre table des prestation et table des demandes de rentes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestations.FIELDNAME_ID_DEMANDE_RENTE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        // jointure entre table des demandes et table des demandes de rentes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        // pour la recherche sur les champs du tiers
        if (!JadeStringUtil.isBlankOrZero(likeNumeroAVS) || !JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS)
                || !JadeStringUtil.isEmpty(likeNom) || !JadeStringUtil.isEmpty(likePrenom)
                || !JadeStringUtil.isIntegerEmpty(forDateNaissance) || !JadeStringUtil.isIntegerEmpty(forCsSexe)) {

            // jointure entre table des demandes et table des numeros AVS
            fromClauseBuffer.append(innerJoin);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_AVS);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(PRDemande.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_AVS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

            // jointure entre table table des numeros AVS et table des personnes
            fromClauseBuffer.append(innerJoin);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_PERSONNE);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_AVS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_PERSONNE);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

            // jointure entre table des personnes et table des tiers
            fromClauseBuffer.append(innerJoin);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_TIERS);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_PERSONNE);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(TABLE_TIERS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        }

        // pour la recherche sur le no AVS
        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            fromClauseBuffer.append(innerJoin);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(IPRTiers.TABLE_AVS_HIST);
            fromClauseBuffer.append(" AS ");
            fromClauseBuffer.append(IPRTiers.TABLE_AVS_HIST);
            fromClauseBuffer.append(" ON (");
            fromClauseBuffer.append(_getCollection() + IPRTiers.TABLE_AVS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(IPRTiers.FIELD_TI_IDTIERS);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(IPRTiers.TABLE_AVS_HIST);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(IPRTiers.FIELD_TI_IDTIERS);
            fromClauseBuffer.append(")");
        }

        return fromClauseBuffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = super._getWhere(statement);

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection()
                    + TABLE_TIERS
                    + "."
                    + FIELDNAME_NOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likeNom)
                            + "%");
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection()
                    + TABLE_TIERS
                    + "."
                    + FIELDNAME_PRENOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likePrenom)
                            + "%");
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + TABLE_PERSONNE + "." + FIELDNAME_DATENAISSANCE + "="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDateNaissance);

        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + TABLE_PERSONNE + "." + FIELDNAME_SEXE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsSexe);
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationsJointDemandeRente();
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

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getLikeNumeroAVS())) {
            return "";
        }

        if (getLikeNumeroAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
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

}
