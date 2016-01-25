/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.decisions;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeMoy5_6_7;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * 
 * @author fha
 */
public class RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ALIAS_CHAMP_TIERS_DEMANDE = " aliasChampTiersDemande";
    private String forAnneeQD = "";
    private String forCsEtatDecision = "";
    private String forCsSexe = "";
    private String forDateNaissance = "";
    private String forIdDecision = "";
    private String forIdExecutionProcess = "";
    private String forIdSousTypeSoinExclu = "";
    private String[] forListeIdsDecisions = null;
    private String forNumeroDecision = "";
    private String forOrderBy = "";
    private String forPrepareFrom = "";
    private String forPreparePar = "";
    private String forValideFrom = "";
    private String forValidePar = "";
    private transient String fromClause = null;
    private String idGestionnaire = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";

    private String likePrenom = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        // Fields de RFDecisionJointDemandeJointMotifsRefusJointtiersValidation
        fields.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_NUM_AVS + ", ");
        fields.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_DATENAISSANCE + ", ");
        fields.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_DATEDECES + ", ");
        fields.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_SEXE + ", ");
        fields.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_NOM + ", ");
        fields.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_PRENOM + ", ");
        fields.append(_getCollection() + RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_TIERS + "."
                + RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI + ", ");
        fields.append(RFDemande.FIELDNAME_ID_DEMANDE + ", ");
        fields.append(RFDemande.FIELDNAME_ID_DEMANDE_PARENT + ",");
        fields.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS + ", ");
        fields.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME + ", ");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_FR + ", ");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_IT + ", ");
        fields.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_DE + ", ");
        fields.append(RFDemande.FIELDNAME_DATE_RECEPTION + ", ");
        fields.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT + ", ");
        fields.append(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT + ", ");
        fields.append(RFDemande.FIELDNAME_DATE_FACTURE + ", ");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN + ", ");
        fields.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN + ", ");
        fields.append(RFDemande.FIELDNAME_ID_FOURNISSEUR + ", ");
        fields.append(RFDemande.FIELDNAME_REMARQUE_FOURNISSEUR + ", ");
        fields.append(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS + ", ");
        fields.append(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_VERSE_OAI + ", ");

        fields.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.ALIAS_PR_DEMANDE_DEM + "."
                + PRDemande.FIELDNAME_IDTIERS
                + RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager.ALIAS_CHAMP_TIERS_DEMANDE + ", ");

        fields.append(RFCopieDecision.FIELDNAME_ID_COPIE + ", ");
        fields.append(RFCopieDecision.FIELDNAME_ID_TIERS + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_PAGE_GARDE + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_VERSEMENT + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_DECOMPTE + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_REMARQUE + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_MOYEN_DROIT + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_SIGNATURE + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_ANNEXES + ", ");
        fields.append(RFCopieDecision.FIELDNAME_HAS_COPIES + ", ");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AI + ", ");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AVS + ", ");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_INVALIDITE + ", ");
        fields.append(RFDemande.FIELDNAME_IS_PP + ", ");
        fields.append(RFDemande.FIELDNAME_IS_TEXTE_REDIRECTION + ", ");
        fields.append(RFDemande.FIELDNAME_IS_FORCER_PAIEMENT + ", ");
        fields.append(RFDemande.FIELDNAME_MONTANT_FACTURE + ", ");
        fields.append(RFDemande.FIELDNAME_MONTANT_A_PAYER + ", ");
        fields.append(RFDemande.FIELDNAME_MONTANT_MENSUEL + ", ");
        fields.append(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE + ", ");
        fields.append(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT + ", ");

        // Fields de RFDecision
        fields.append(RFDecision.FIELDNAME_ID_DECISION + ", ");
        fields.append(RFDecision.FIELDNAME_NUMERO_DECISION + ", ");
        fields.append(RFDecision.FIELDNAME_ANNEE_QD + ", ");
        fields.append(RFDecision.FIELDNAME_ID_ADRESSE_PAIEMENT + ", ");
        fields.append(RFDecision.FIELDNAME_ID_ADRESSE_DOMICILE + ", ");
        fields.append(RFDecision.FIELDNAME_ID_VALIDE_PAR + ", ");
        fields.append(RFDecision.FIELDNAME_DATE_VALIDATION + ", ");
        fields.append(RFDecision.FIELDNAME_ID_PREPARE_PAR + ", ");
        fields.append(RFDecision.FIELDNAME_DATE_PREPARATION + ", ");
        fields.append(RFDecision.FIELDNAME_DATE_SUR_DOCUMENT + ", ");
        fields.append(RFDecision.FIELDNAME_ETAT_DECISION + ", ");
        fields.append(RFDecision.FIELDNAME_ID_GESTIONNAIRE + ", ");
        fields.append(RFDecision.FIELDNAME_TEXTE_REMARQUE + ", ");
        fields.append(RFDecision.FIELDNAME_TEXTE_ANNEXE + ", ");
        fields.append(RFDecision.FIELDNAME_GENRE_DECISION + ", ");
        fields.append(RFDecision.FIELDNAME_INCITATION_DEPOT_NOUVELLE_DEMANDE + ", ");
        fields.append(RFDecision.FIELDNAME_RETOUR_BV + ", ");
        fields.append(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS + ", ");
        fields.append(RFDecision.FIELDNAME_DECOMPTE_FACTURE_RETOUR + ", ");
        fields.append(RFDecision.FIELDNAME_BULLETIN_VERSEMENT_RETOUR + ", ");
        fields.append(RFDecision.FIELDNAME_BORDEREAU_ACCOMPAGNEMENT + ", ");
        // Numeric a 2 décimals
        fields.append(RFDecision.FIELDNAME_MONTANT_COURANT_PARTIE_FUTURE + ", ");
        fields.append(RFDecision.FIELDNAME_MONTANT_COURANT_PARTIE_RETROACTIVE + ", ");
        fields.append(RFDecision.FIELDNAME_MONTANT_DEPASSEMENT_QD + ", ");
        fields.append(RFDecision.FIELDNAME_MONTANT_EXCEDENT_DE_RECETTE_PC + ", ");
        fields.append(RFDecision.FIELDNAME_MONTANT_TOTAL_RFM + ", ");
        fields.append(RFDecision.FIELDNAME_MONTANT_A_REMBOURSER_DSAS + ", ");
        fields.append(RFDecision.FIELDNAME_ID_QD_PRINICIPALE + ", ");
        fields.append(RFDecision.FIELDNAME_TYPE_PAIEMENT + ", ");
        fields.append(RFDecision.FIELDNAME_DATE_DEBUT_RETRO + ", ");
        fields.append(RFDecision.FIELDNAME_DATE_FIN_RETRO + ", ");

        fields.append(RFDecision.FIELDNAME_DATE_DERNIER_PAIEMENT);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFDecisionJointDemandeJointMotifRefusJointTiersValidation.createFromClause(_getCollection()));

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
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(idGestionnaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), idGestionnaire));
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
            sqlWhere.append(schema + "TITIERP."
                    + RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_NOM);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), likeNom));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + "TITIERP."
                    + RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_PRENOM);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), likePrenom));
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_DATENAISSANCE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_SEXE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isEmpty(forNumeroDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_NUMERO_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forNumeroDecision));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDecision.TABLE_NAME + "." + RFDecision.FIELDNAME_ETAT_DECISION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDecision));
        }

        if (!JadeStringUtil.isEmpty(forPreparePar)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_PREPARE_PAR);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forPreparePar));
        }

        if (!JadeStringUtil.isEmpty(forValidePar)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_VALIDE_PAR);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forValidePar));
        }

        if (!JadeStringUtil.isEmpty(forAnneeQD)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ANNEE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forAnneeQD));
        }

        if (!JadeStringUtil.isEmpty(forValideFrom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forValideFrom));
        }

        if (!JadeStringUtil.isEmpty(forPrepareFrom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_DATE_PREPARATION);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forPrepareFrom));
        }

        if (!JadeStringUtil.isEmpty(forIdSousTypeSoinExclu)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append(" != ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdSousTypeSoinExclu));
        }

        if (!JadeStringUtil.isEmpty(forIdExecutionProcess)) {
            if (!forIdExecutionProcess.equals("withAndWithoutIdProcess")) {

                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdExecutionProcess));
            }
        } else {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" (");
            sqlWhere.append(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" IS NULL");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" = 0");
            sqlWhere.append(") ");
        }

        if (forListeIdsDecisions != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            StringBuilder listeIds = new StringBuilder();
            int itr = 0;

            for (String id : forListeIdsDecisions) {
                itr++;
                listeIds.append(id);
                if (itr < forListeIdsDecisions.length) {
                    listeIds.append(",");
                }
            }

            sqlWhere.append(RFDecision.FIELDNAME_ID_DECISION);
            sqlWhere.append(" IN ");
            sqlWhere.append("(");
            sqlWhere.append(listeIds.toString());
            sqlWhere.append(")");

        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDecisionJointDemandeJointMotifRefusJointTiersValidation();
    }

    public String getForAnneeQD() {
        return forAnneeQD;
    }

    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdExecutionProcess() {
        return forIdExecutionProcess;
    }

    public String getForIdSousTypeSoinExclu() {
        return forIdSousTypeSoinExclu;
    }

    public String[] getForListeIdsDecisions() {
        return forListeIdsDecisions;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getForPrepareFrom() {
        return forPrepareFrom;
    }

    public String getForPreparePar() {
        return forPreparePar;
    }

    public String getForValideFrom() {
        return forValideFrom;
    }

    public String getForValidePar() {
        return forValidePar;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
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

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForAnneeQD(String forAnneeQD) {
        this.forAnneeQD = forAnneeQD;
    }

    public void setForCsEtatDecision(String forCsEtatDecision) {
        this.forCsEtatDecision = forCsEtatDecision;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdExecutionProcess(String forIdExecutionProcess) {
        this.forIdExecutionProcess = forIdExecutionProcess;
    }

    public void setForIdSousTypeSoinExclu(String forIdSousTypeSoinExclu) {
        this.forIdSousTypeSoinExclu = forIdSousTypeSoinExclu;
    }

    public void setForListeIdsDecisions(String[] forListeIdsDecisions) {
        this.forListeIdsDecisions = forListeIdsDecisions;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setForPrepareFrom(String forPrepareFrom) {
        this.forPrepareFrom = forPrepareFrom;
    }

    public void setForPreparePar(String forPreparePar) {
        this.forPreparePar = forPreparePar;
    }

    public void setForValideFrom(String forValideFrom) {
        this.forValideFrom = forValideFrom;
    }

    public void setForValidePar(String forValidePar) {
        this.forValidePar = forValidePar;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
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
