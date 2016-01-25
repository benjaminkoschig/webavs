/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.rentesaccordees.REFactureARestituer;
import globaz.corvus.db.rentesaccordees.REFacturesARestituerManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author bsc
 * 
 */

public class REFactureARestituerListViewBean extends REFacturesARestituerManager {

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

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REFactureARestituer.TABLE_NAME_FACTURE_A_RESTITUER);

        // jointure entre table des tiers et prestations accordes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REFactureARestituer.TABLE_NAME_FACTURE_A_RESTITUER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REFactureARestituer.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des tiers et table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REFactureARestituer.TABLE_NAME_FACTURE_A_RESTITUER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REFactureARestituer.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des tiers et table des personne
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REFactureARestituer.TABLE_NAME_FACTURE_A_RESTITUER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REFactureARestituer.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            fromClauseBuffer.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                    + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                    + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS
                    + ")");
        }

        return fromClauseBuffer.toString();
    }

    /**
	 * 
	 */
    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = super._getWhere(statement);
        String schema = _getCollection();

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRenteJointDemande.TABLE_PERSONNE + "."
                    + REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE + "="
                    + _dbWriteDateAMJ(statement.getTransaction(), forDateNaissance);

        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + REDemandeRenteJointDemande.TABLE_PERSONNE + "."
                    + REDemandeRenteJointDemande.FIELDNAME_SEXE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsSexe);
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + REDemandeRenteJointDemande.TABLE_TIERS
                    + "."
                    + REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likeNom)
                            + "%");
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema
                    + REDemandeRenteJointDemande.TABLE_TIERS
                    + "."
                    + REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH
                    + " like "
                    + _dbWriteString(statement.getTransaction(), PRStringUtils.upperCaseWithoutSpecialChars(likePrenom)
                            + "%");
        }

        return sqlWhere;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REFactureARestituerViewBean();
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }
}
