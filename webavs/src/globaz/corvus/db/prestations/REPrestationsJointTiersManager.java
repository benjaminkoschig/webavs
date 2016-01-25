/*
 * Créé le 13 juin 2012 by LGA BUGFIX 13.06.2012 : LGA correcting BZ 6868 pour la recherche sur le no AVS Suppression de
 * l'héritage depuis REDecisionJointDemandeRenteManager pour avoir un manager indépendant.
 */
package globaz.corvus.db.prestations;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;

/**
 * @author LGA
 */
public class REPrestationsJointTiersManager extends BManager {

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
    public static final String TABLE_AVS_HISTO_FIELD_NUMERO_AVS = "HVNAVS";
    public static final String TABLE_PERSONNE = "TIPERSP";

    public static final String TABLE_TIERS = "TITIERP";

    private String forCsEtatDecisions = "";

    private String forCsSexe = "";
    private String forCsTypeDecision = "";
    private String forDateNaissance = "";
    private String forIdLot = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    @Override
    protected String _getFrom(BStatement statement) {
        String prestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String renteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;

        StringBuilder sql = new StringBuilder();
        String superFrom = super._getFrom(statement);

        // TODO
        // if (!"*".equals(superFrom)) {
        // sql.append(superFrom);
        // }
        sql.append(superFrom);

        sql.append(" INNER JOIN ");
        sql.append(prestationAccordee);
        sql.append(" ON ");
        sql.append(prestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        sql.append("=");
        sql.append(renteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();
        String schema = _getCollection();
        String superWhere = super._getWhere(statement);
        if (!JadeStringUtil.isBlank(superWhere)) {
            sql.append(superWhere);
        }

        // For id lot
        if (!JadeStringUtil.isIntegerEmpty(getForIdLot())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REPrestations.FIELDNAME_ID_LOT);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdLot()));
        }

        // Like n° AVS
        if ((!JadeStringUtil.isEmpty(getLikeNumeroAVS())) && (castStringAsBooleanValue(getLikeNumeroAVSNNSS()) != null)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(getWhereNSSWithSchema(schema, getLikeNumeroAVS(),
                    castStringAsBooleanValue(getLikeNumeroAVSNNSS())));
        }

        // For sexe
        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(IPRConstantesExternes.TABLE_PERSONNE).append(".")
                    .append(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_SEXE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        // Like nom
        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(IPRConstantesExternes.TABLE_TIERS).append(".")
                    .append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM_FOR_SEARCH);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        // Like prénom
        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(IPRConstantesExternes.TABLE_TIERS).append(".")
                    .append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM_FOR_SEARCH);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        // For date de naissance
        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema + REDemandeRenteJointDemande.TABLE_PERSONNE).append(".")
                    .append(REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE).append("=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));

        }
        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationsJointTiers();
    }

    /**
     * Test la valeur reçue et retourne la valeur booléenne correspondante
     * 
     * @param value Valeur à caster
     * @return TRUE or FALSE si la valeur correspond à une valeur booléenne sinon null
     */
    private Boolean castStringAsBooleanValue(String value) {
        if (String.valueOf(Boolean.TRUE).equals(value.toLowerCase())) {
            return true;
        }
        if (String.valueOf(Boolean.FALSE).equals(value.toLowerCase())) {
            return false;
        }
        return null;
    }

    /*********************** COMMON GETTER AND SETTER ******************************/

    public final String getForCsEtatDecisions() {
        return forCsEtatDecisions;
    }

    public final String getForCsSexe() {
        return forCsSexe;
    }

    public final String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    public final String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public final String getLikeNom() {
        return likeNom;
    }

    public final String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public final String getLikePrenom() {
        return likePrenom;
    }

    /**
     * Retourne la clause WHERE avec le schema de la DB pour la recherche par n° AVS
     * 
     * @param schema Le schema voulut
     * @param likeNoAvs
     * @param isNewAVSNumberFormat Nouveau numéro AVS (true) ou ancien (false)
     * @return La clause WHERE complète pour la recherche par numéro AVS
     */
    public String getWhereNSSWithSchema(String schema, String likeNoAvs, boolean isNewAVSNumberFormat) {
        String noAvsForLike = JAStringFormatter.deformatAvs(likeNoAvs);
        int nbCaractereACompleter = 17 - noAvsForLike.length();
        for (int i = 0; i < nbCaractereACompleter; i++) {
            noAvsForLike += "_";
        }

        // si nouveau : 756_ _ _ _ _ _ _ _ _ _
        if (isNewAVSNumberFormat) {
            noAvsForLike = NSUtil.formatAVSNew(noAvsForLike, true);
        }
        // si ancien : 251_ _ _ _ _ _ _ _
        else {
            noAvsForLike = NSUtil.formatAVSNew(noAvsForLike, false);
        }
        return "(" + schema + REPrestationsJointTiersManager.TABLE_AVS_HISTO + "."
                + REPrestationsJointTiersManager.TABLE_AVS_HISTO_FIELD_NUMERO_AVS + " LIKE '" + noAvsForLike + "')";
    }

    public final void setForCsEtatDecisions(String forCsEtatDecisions) {
        this.forCsEtatDecisions = forCsEtatDecisions;
    }

    public final void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public final void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    public final void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public final void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public final void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public final void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

}
