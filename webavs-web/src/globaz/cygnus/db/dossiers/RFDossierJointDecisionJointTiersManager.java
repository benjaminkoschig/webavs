/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.db.dossiers;

import globaz.cygnus.services.RFMembreFamilleService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;

/**
 * @author jje
 */
public class RFDossierJointDecisionJointTiersManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    private String forCsEtatDossier = "";
    private String forCsSexe = "";

    private String forDateNaissance = "";
    private String forIdDecision = "";
    private String forIdDossiers = "";

    private String forIdGestionnaire = "";
    private String forIdTiers = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forOrderBy = "";
    private transient String fromClause = null;
    private String fromDateDebut = "";
    private String idTiersRechercheFamilleWhere = "";
    private boolean isRechercheFamille = false;
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";

    private String likePrenom = "";

    private int nbTiersFamille = 0;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossierJointTiersManager.
     */
    public RFDossierJointDecisionJointTiersManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode intervenant avant la recherche, recherchant le conjoint ou/et les enfants de l'assuré
     * 
     * @see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        if (isRechercheFamille) {

            boolean isPremierPassage = true;

            // Début de la création de la String
            idTiersRechercheFamilleWhere += "(";

            PRTiersWrapper tw = PRTiersHelper.getTiers(getSession(), likeNumeroAVS);
            String idTiersRequerant = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            if (tw != null) {

                // Recherche des membre familles
                try {
                    RFMembreFamilleService rfMembreFamilleService = new RFMembreFamilleService(transaction);

                    MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes = rfMembreFamilleService
                            .getMembreFamille(idTiersRequerant, "", false);
                    if (searchMembresFamilleRequerantDomaineRentes != null) {
                        for (MembreFamilleVO membreFamille : searchMembresFamilleRequerantDomaineRentes) {
                            if (membreFamille != null) {

                                if (membreFamille.getRelationAuRequerant().equals(
                                        ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT)
                                        || (membreFamille.getRelationAuRequerant().equals(
                                                ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT) && isRechercheFamille)
                                        || (membreFamille.getRelationAuRequerant().equals(
                                                ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT) && isRechercheFamille)) {

                                    // Pas d'idTiers, pas de rentes
                                    if (!JadeStringUtil.isIntegerEmpty(membreFamille.getIdTiers())
                                            && (idTiersRechercheFamilleWhere.indexOf(membreFamille.getIdTiers()) == -1)) {
                                        nbTiersFamille++;

                                        if (isPremierPassage) {
                                            idTiersRechercheFamilleWhere += membreFamille.getIdTiers();
                                            isPremierPassage = false;
                                        } else {
                                            idTiersRechercheFamilleWhere += ", " + membreFamille.getIdTiers();
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        nbTiersFamille++;
                        idTiersRechercheFamilleWhere += idTiersRequerant;
                    }

                    idTiersRechercheFamilleWhere += ")";

                } catch (Exception e) {
                    RFUtils.setMsgExceptionErreurViewBean(
                            (FWViewBeanInterface) getSession().getAttribute(FWServlet.VIEWBEAN), e.getMessage());
                }
            }
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFDossierJointDecisionJointTiers.createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))
                    && !isRechercheFamille) {
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

        if (isRechercheFamille && (nbTiersFamille > 0)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_IDTIERS + " IN "
                    + idTiersRechercheFamilleWhere);

        } else {

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

            if (!JadeStringUtil.isEmpty(forCsEtatDossier)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(schema + RFDossier.TABLE_NAME + "." + RFDossier.FIELDNAME_CS_ETAT_DOSSIER + "="
                        + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDossier));
            }

            if (!JadeStringUtil.isEmpty(fromDateDebut)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append("(" + RFDossier.FIELDNAME_DATE_DEBUT + ">="
                        + this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebut) + " OR "
                        + RFDossier.FIELDNAME_DATE_FIN + "=" + this._dbWriteNumeric(statement.getTransaction(), "0")
                        + ")");
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdDossiers)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(RFDossier.FIELDNAME_ID_DOSSIER);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossiers));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdGestionnaire)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(RFDossier.FIELDNAME_ID_GESTIONNAIRE);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdDecision)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_DECISION_ASS_DOS_DEC);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
            }
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFDossierJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDossierJointDecisionJointTiers();
    }

    public String getForCsEtatDossier() {
        return forCsEtatDossier;
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

    public String getForIdDossiers() {
        return forIdDossiers;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public boolean getIsRechercheFamille() {
        return isRechercheFamille;
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

    public void setForCsEtatDossier(String forCsEtatDossier) {
        this.forCsEtatDossier = forCsEtatDossier;
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

    public void setForIdDossiers(String forIdDossiers) {
        this.forIdDossiers = forIdDossiers;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setIsRechercheFamille(boolean isRechercheFamille) {
        this.isRechercheFamille = isRechercheFamille;
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