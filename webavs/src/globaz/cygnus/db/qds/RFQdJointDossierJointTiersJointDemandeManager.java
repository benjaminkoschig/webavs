/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeJointDossierJointTiers;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.dossiers.RFDossierJointDecisionJointTiers;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author jje
 */
public class RFQdJointDossierJointTiersJointDemandeManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnneeQd = "";

    private String forCsEtatQd = "";
    private String forCsGenreQd = "";

    private String forCsSexe = "";
    private String forCsTypePC = "";
    private String forDateNaissance = "";
    private String forIdDemande = "";
    private String forIdDossier = "";
    private String forIdGestionnaire = "";
    private String forIdQd = "";

    private String forIdTiers = "";
    private String forNumeroDecision = "";
    private String forOrderBy = "";
    private transient String fromClause = null;
    private boolean isComprisDansCalcul = false;
    private String likeNom = "";
    private String likeNumeroAVS = "";

    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdJointDossierJointTiersJointDemandeManager.
     */
    public RFQdJointDossierJointTiersJointDemandeManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        String schema = _getCollection();

        StringBuffer fields = new StringBuffer();

        fields.append(RFQd.FIELDNAME_CS_GENRE_QD).append(",");
        fields.append(RFQd.FIELDNAME_CS_ETAT_QD).append(",");
        fields.append(RFQd.FIELDNAME_ID_QD).append(",");
        fields.append(RFQd.FIELDNAME_LIMITE_ANNUELLE).append(",");
        fields.append(RFQd.FIELDNAME_ANNEE_QD).append(",");
        fields.append(RFQd.FIELDNAME_ID_GESTIONNAIRE).append(",");
        fields.append(RFQd.FIELDNAME_MONTANT_CHARGE_RFM).append(",");
        fields.append(RFQd.FIELDNAME_CS_SOURCE).append(",");
        fields.append(RFQd.FIELDNAME_DATE_CREATION).append(",");
        fields.append(RFQd.FIELDNAME_IS_PLAFONNEE).append(",");
        fields.append(RFQd.FIELDNAME_MONTANT_INITIAL_CHARGE_RFM).append(",");
        fields.append(RFQd.FIELDNAME_CS_TYPE_QD).append(",");

        fields.append(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD).append(",");
        fields.append(RFQdAugmentation.FIELDNAME_ID_QD).append(",");
        fields.append(RFQdAugmentation.FIELDNAME_MONTANT_AUGMENTATION_QD).append(",");
        fields.append(RFQdAugmentation.FIELDNAME_TYPE_MODIF).append(",");
        fields.append(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION).append(",");

        fields.append(RFQdSoldeCharge.FIELDNAME_ID_QD).append(",");
        fields.append(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE).append(",");
        fields.append(RFQdSoldeCharge.FIELDNAME_MONTANT_SOLDE).append(",");
        fields.append(RFQdSoldeCharge.FIELDNAME_TYPE_MODIF).append(",");
        fields.append(RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION).append(",");

        fields.append(RFSousTypeDeSoin.FIELDNAME_CODE).append(",");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN).append(",");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN).append(",");

        fields.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN).append(",");
        fields.append(RFTypeDeSoin.FIELDNAME_CODE).append(",");

        fields.append(RFQdPrincipale.FIELDNAME_CS_GENRE_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_BENEFICIAIRE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_DATE_DEBUT_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_DATE_FIN_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_EXCEDENT_RECETTE_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_FAM_MOD_SOLDE_EXCEDENT_PRE_DEC).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_PC_ACCORDEE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_POT_DROIT_PC).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_IS_LAPRAMS).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_IS_RI).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_CONJOINT).append(",");
        fields.append(RFQdPrincipale.FIELDNAME_REMBOURSEMENT_REQUERANT).append(",");

        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION).append(",");
        fields.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION).append(",");

        fields.append(RFQdAssure.FIELDNAME_DATE_DEBUT).append(",");
        fields.append(RFQdAssure.FIELDNAME_DATE_FIN).append(",");
        fields.append(RFQdAssure.FIELDNAME_ID_POT_SOUS_TYPE_DE_SOIN).append(",");
        fields.append(RFQdAssure.FIELDNAME_ID_QD_ASSURE).append(",");

        fields.append(RFAssQdDossier.FIELDNAME_ID_ASS_QD_DOSSIER).append(",");
        fields.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER).append(",");
        fields.append(RFAssQdDossier.FIELDNAME_ID_QD).append(",");
        fields.append(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL).append(",");
        fields.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION).append(",");

        fields.append(RFDossier.FIELDNAME_CS_ETAT_DOSSIER).append(",");
        fields.append(RFDossier.FIELDNAME_CS_SOURCE).append(",");
        fields.append(RFDossier.FIELDNAME_DATE_DEBUT).append(",");
        fields.append(RFDossier.FIELDNAME_DATE_FIN).append(",");
        fields.append(RFDossier.FIELDNAME_ID_DOSSIER).append(",");
        fields.append(RFDossier.FIELDNAME_ID_GESTIONNAIRE).append(",");
        fields.append(RFDossier.FIELDNAME_ID_PRDEM).append(",");

        // details requerants
        fields.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_DATEDECES).append(",");
        fields.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_DATENAISSANCE).append(",");
        fields.append(schema + "TITIERP." + RFQdJointDossierJointTiersJointDemande.FIELDNAME_NATIONALITE).append(",");
        fields.append(schema + "TITIERP." + RFQdJointDossierJointTiersJointDemande.FIELDNAME_NOM).append(",");
        fields.append(schema + "TITIERP." + RFQdJointDossierJointTiersJointDemande.FIELDNAME_PRENOM).append(",");
        fields.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_SEXE).append(",");
        fields.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_NUM_AVS).append(",");
        fields.append(schema + "TITIERP." + RFQdJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);

        return fields.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFQdJointDossierJointTiersJointDemande.createFromClause(
                    _getCollection(), !JadeStringUtil.isBlankOrZero(getForIdDemande())));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                        + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                        + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS
                        + ")");
            }

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();

        sqlOrder.append(RFQd.FIELDNAME_ANNEE_QD);
        sqlOrder.append(" DESC ,");
        sqlOrder.append(RFQd.FIELDNAME_ID_QD);
        sqlOrder.append(",");
        sqlOrder.append(RFDemandeJointDossierJointTiers.FIELDNAME_NOM);
        sqlOrder.append(",");
        sqlOrder.append(RFDemandeJointDossierJointTiers.FIELDNAME_PRENOM);
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
        String schema = _getCollection();

        sqlWhere.append(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL);
        sqlWhere.append(" = ");
        sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), true, BConstants.DB_TYPE_BOOLEAN_CHAR));

        if (!JadeStringUtil.isBlankOrZero(forIdTiers)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS()));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema
                    + RFDossierJointDecisionJointTiers.TABLE_TIERS
                    + "."
                    + RFDossierJointDecisionJointTiers.FIELDNAME_NOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema
                    + RFDossierJointDecisionJointTiers.TABLE_TIERS
                    + "."
                    + RFDossierJointDecisionJointTiers.FIELDNAME_PRENOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDossierJointDecisionJointTiers.TABLE_PERSONNE + "."
                    + RFDossierJointDecisionJointTiers.FIELDNAME_DATENAISSANCE + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JAUtil.isDateEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDossierJointDecisionJointTiers.TABLE_PERSONNE + "."
                    + RFDossierJointDecisionJointTiers.FIELDNAME_SEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsGenreQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_GENRE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsGenreQd));
        }

        if (!JadeStringUtil.isBlankOrZero(forCsEtatQd)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatQd));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDossier)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDossier.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        if (!JadeStringUtil.isIntegerEmpty(forNumeroDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forNumeroDecision));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdGestionnaire)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFQd.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDemande)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDemande));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdQd)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFQd.FIELDNAME_ID_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdQd));
        }

        if (!JadeStringUtil.isIntegerEmpty(forAnneeQd)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFQd.FIELDNAME_ANNEE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forAnneeQd));
        }

        /*
         * if (sqlWhere.length() > 0) { sqlWhere.append(" AND "); }
         * 
         * 
         * sqlWhere.append("(" + RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD + " IN (SELECT b." +
         * RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD + " FROM " + schema + RFQdAugmentation.TABLE_NAME + " AS b " +
         * "WHERE  b." + RFQdAugmentation.FIELDNAME_ID_QD + " = " + RFQd.FIELDNAME_ID_QD +
         * " AND NOT EXISTS (SELECT * FROM " + schema + RFQdAugmentation.TABLE_NAME + " WHERE " +
         * RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION + " = b." +
         * RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION + " AND " + RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD
         * + " > b." + RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD + " AND " + RFQdAugmentation.FIELDNAME_ID_QD +
         * " = " + RFQd.FIELDNAME_ID_QD + ")) OR " + RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD + " is null)");
         * 
         * if (sqlWhere.length() > 0) { sqlWhere.append(" AND "); }
         * 
         * sqlWhere.append("(" + RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE + " IN (SELECT c." +
         * RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE + " FROM " + schema + RFQdSoldeCharge.TABLE_NAME + " AS c " +
         * "WHERE  c." + RFQdSoldeCharge.FIELDNAME_ID_QD + " = " + RFQd.FIELDNAME_ID_QD +
         * " AND NOT EXISTS (SELECT * FROM " + schema + RFQdSoldeCharge.TABLE_NAME + " WHERE " +
         * RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION + " = c." +
         * RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION + " AND " + RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE +
         * " > c." + RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE + " AND " + RFQdSoldeCharge.FIELDNAME_ID_QD + " = " +
         * RFQd.FIELDNAME_ID_QD + ")) OR " + RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE + " is null)");
         * 
         * if (sqlWhere.length() > 0) { sqlWhere.append(" AND "); }
         * 
         * sqlWhere.append("(" + RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE + " IN (SELECT d." +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE + " FROM " + schema +
         * RFPeriodeValiditeQdPrincipale.TABLE_NAME + " AS d " + "WHERE  d." +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE + " = " + RFQd.FIELDNAME_ID_QD +
         * " AND NOT EXISTS (SELECT * FROM " + schema + RFPeriodeValiditeQdPrincipale.TABLE_NAME + " WHERE " +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION + " = d." +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION + " AND " +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE + " > d." +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE + " AND " +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE + " = " + RFQd.FIELDNAME_ID_QD + ")) OR " +
         * RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE + " is null)");
         */

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFQdJointDossierJointTiersJointDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdJointDossierJointTiersJointDemande();
    }

    public String getForAnneeQd() {
        return forAnneeQd;
    }

    public String getForCsEtatQd() {
        return forCsEtatQd;
    }

    public String getForCsGenreQd() {
        return forCsGenreQd;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsTypePC() {
        return forCsTypePC;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForIdQd() {
        return forIdQd;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getForOrderBy() {
        return forOrderBy;
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

    public boolean isComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    public void setComprisDansCalcul(boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

    public void setForAnneeQd(String forAnneeQd) {
        this.forAnneeQd = forAnneeQd;
    }

    public void setForCsEtatQd(String forCsEtatQd) {
        this.forCsEtatQd = forCsEtatQd;
    }

    public void setForCsGenreQd(String forCsGenreQd) {
        this.forCsGenreQd = forCsGenreQd;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsTypePC(String forCsTypePC) {
        this.forCsTypePC = forCsTypePC;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForIdQd(String forIdQd) {
        this.forIdQd = forIdQd;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
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
