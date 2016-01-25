package globaz.cygnus.db.dossiers;

import globaz.cygnus.db.decisions.RFAssDossierDecision;
import globaz.cygnus.services.RFMembreFamilleService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.globall.util.JAVector;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;

/**
 * <p>
 * Manager dédié à l'écran de recherche des dossier RFM (PRF0002)
 * </p>
 * <p>
 * Ce manager agrège les données de la requête dans la méthode {@link #_afterFind(BTransaction)}, car le résultat de la
 * requête peut retourner plusieurs fois le même dossier, avec chaque fois une contribution d'assistance AI différente.
 * </p>
 * 
 * @author PBA
 */
public class RFDossierJointTiersManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static enum OrdreDeTri {
        DateDebut("DateDebut"),
        NomPrenom("NomPrenom"),
        SansTri("");

        private String ordre;

        private OrdreDeTri(String ordre) {
            this.ordre = ordre;
        }

        public String getOrdre() {
            return ordre;
        }
    }

    private String forCsEtatDossier = null;

    private String forCsSexe = null;
    private String forDateNaissance = null;
    private String forIdDecision = null;
    private String forIdDossiers = null;
    private String forIdGestionnaire = null;
    private String forIdTiers = null;
    private String forOrderBy = null;
    private String fromDateDebut = null;
    private Collection<String> idTiersFamille = null;
    private boolean isRechercheFamille = false;
    private String likeNom = null;
    private String likeNumeroAVS = null;
    private String likeNumeroAVSNNSS = null;
    private String likePrenom = null;

    public RFDossierJointTiersManager() {
        super();

        wantCallMethodAfterFind(true);
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {

        Map<String, RFDossierJointTiers> dossiersParIdDossier = new HashMap<String, RFDossierJointTiers>();

        for (int i = 0; i < getSize(); i++) {
            RFDossierJointTiers unDossier = (RFDossierJointTiers) get(i);

            if (dossiersParIdDossier.containsKey(unDossier.getIdDossier())) {
                RFDossierJointTiers dossierDejaExistant = dossiersParIdDossier.get(unDossier.getIdDossier());
                if (dossierDejaExistant.getPeriodesCAAI() == null) {
                    dossierDejaExistant.setPeriodesCAAI(new ArrayList<RFPeriodeCAAIWrapper>());
                }
                dossierDejaExistant.getPeriodesCAAI().add(
                        creerNouvellePeridoe(unDossier.getIdContributionAssistanceAI(),
                                unDossier.getDateDebutPeriodeCAAI(), unDossier.getDateFinPeriodeCAAI()));
            } else {
                if (JadeDateUtil.isGlobazDate(unDossier.getDateDebutPeriodeCAAI())) {
                    List<RFPeriodeCAAIWrapper> periodeCAAIDuDossier = new ArrayList<RFPeriodeCAAIWrapper>();
                    periodeCAAIDuDossier.add(creerNouvellePeridoe(unDossier.getIdContributionAssistanceAI(),
                            unDossier.getDateDebutPeriodeCAAI(), unDossier.getDateFinPeriodeCAAI()));
                    unDossier.setPeriodesCAAI(periodeCAAIDuDossier);
                }
                dossiersParIdDossier.put(unDossier.getIdDossier(), unDossier);
            }
        }

        JAVector newContainer = new JAVector();
        // on re-trie les entités, comme le fait l'order by de la requête, grâce à l'utilisation d'un liste triée
        // (RFDossierJointTiers étend Comparable)
        newContainer.addAll(new TreeSet<RFDossierJointTiers>(dossiersParIdDossier.values()));
        setContainer(newContainer);
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        if (isRechercheFamille) {

            idTiersFamille = new ArrayList<String>();

            PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), likeNumeroAVS);
            String idTiersRequerant = tiers.getIdTiers();

            if (tiers != null) {
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
                                            && (!idTiersFamille.contains(membreFamille.getIdTiers()))) {
                                        idTiersFamille.add(membreFamille.getIdTiers());
                                    }
                                }
                            }
                        }
                    } else {
                        idTiersFamille.add(idTiersRequerant);
                    }
                } catch (Exception e) {
                    RFUtils.setMsgExceptionErreurViewBean(
                            (FWViewBeanInterface) getSession().getAttribute(FWServlet.VIEWBEAN), e.getMessage());
                }
            }
        }
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String schema = _getCollection();
        String tableDossierRFM = schema + RFDossier.TABLE_NAME;
        String tableAssociationDossierDecision = schema + RFAssDossierDecision.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(super._getFrom(statement));

        if (!JadeStringUtil.isBlankOrZero(forIdDecision)) {
            sql.append(" INNER JOIN ").append(tableAssociationDossierDecision);
            sql.append(" ON ").append(tableDossierRFM).append(".").append(RFDossier.FIELDNAME_ID_DOSSIER).append("=")
                    .append(tableAssociationDossierDecision).append(".")
                    .append(RFAssDossierDecision.FIELDNAME_ID_DOSSIER);
        }

        return sql.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {

        OrdreDeTri ordreDeTri = OrdreDeTri.valueOf(forOrderBy);
        if (ordreDeTri == null) {
            return null;
        }
        String schema = _getCollection();
        switch (OrdreDeTri.valueOf(forOrderBy)) {
            case NomPrenom:
                String tableTiers = schema + ITITiersDefTable.TABLE_NAME;

                return tableTiers + "." + ITITiersDefTable.DESIGNATION_1 + "," + tableTiers + "."
                        + ITITiersDefTable.DESIGNATION_2;

            case DateDebut:
                String tableDossier = schema + RFDossier.TABLE_NAME;

                return tableDossier + "." + RFDossier.FIELDNAME_DATE_DEBUT + " DESC";
            default:
                return null;
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String schema = _getCollection();
        String tableDossier = schema + RFDossier.TABLE_NAME;
        String tableAssociationDossierDecision = schema + RFAssDossierDecision.TABLE_NAME;
        String tableDemandePrestation = schema + PRDemande.TABLE_NAME;
        String tableTiers = schema + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = schema + ITIPersonneDefTable.TABLE_NAME;

        if (isRechercheFamille && (idTiersFamille != null) && (idTiersFamille.size() > 0)) {
            sql.append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS).append(" IN (");
            for (Iterator<String> iterator = idTiersFamille.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        } else {
            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(likeNumeroAVS))) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(PRNSSUtil.getWhereNSS(_getCollection(), likeNumeroAVS, likeNumeroAVSNNSS, true));
            }

            if (!JadeStringUtil.isEmpty(likeNom)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableTiers)
                        .append(".")
                        .append(ITITiersDefTable.DESIGNATION_1_MAJ)
                        .append(" LIKE ")
                        .append(this._dbWriteString(statement.getTransaction(),
                                PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
            }

            if (!JadeStringUtil.isEmpty(likePrenom)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableTiers)
                        .append(".")
                        .append(ITITiersDefTable.DESIGNATION_2_MAJ)
                        .append(" LIKE ")
                        .append(this._dbWriteString(statement.getTransaction(),
                                PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
            }

            if (!JAUtil.isDateEmpty(forDateNaissance)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append("=")
                        .append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
            }

            if (!JAUtil.isDateEmpty(forCsSexe)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
            }

            if (!JadeStringUtil.isEmpty(forCsEtatDossier)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_CS_ETAT_DOSSIER).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatDossier));
            }

            if (!JadeStringUtil.isEmpty(fromDateDebut)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append("(");
                sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_DATE_DEBUT).append(">=")
                        .append(this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebut));
                sql.append(" OR ");
                sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_DATE_FIN).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), "0"));
                sql.append(")");
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdDossiers)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_ID_DOSSIER).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), forIdDossiers));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdGestionnaire)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_ID_GESTIONNAIRE).append("=")
                        .append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdDecision)) {
                if (sql.length() > 0) {
                    sql.append(" AND ");
                }
                sql.append(tableAssociationDossierDecision).append(".")
                        .append(RFAssDossierDecision.FIELDNAME_ID_DECISION).append("=")
                        .append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
            }
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDossierJointTiers();
    }

    private RFPeriodeCAAIWrapper creerNouvellePeridoe(String idContributionAssistanceAI, String dateDebutPeriodeCAAI,
            String dateFinPeriodeCAAI) {
        RFPeriodeCAAIWrapper nouvellePeriode = new RFPeriodeCAAIWrapper();
        nouvellePeriode.setIdContributionAssistanceAI(idContributionAssistanceAI);
        nouvellePeriode.setDateDebutCAAI(dateDebutPeriodeCAAI);
        nouvellePeriode.setDateFinCAAI(dateFinPeriodeCAAI);

        return nouvellePeriode;
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

    public String getFromDateDebut() {
        return fromDateDebut;
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

    public boolean isRechercheFamille() {
        return isRechercheFamille;
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

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setIsRechercheFamille(boolean isRechercheFamille) {
        setRechercheFamille(isRechercheFamille);
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

    public void setRechercheFamille(boolean isRechercheFamille) {
        this.isRechercheFamille = isRechercheFamille;
    }
}