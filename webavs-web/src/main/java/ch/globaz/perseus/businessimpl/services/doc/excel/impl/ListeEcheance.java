/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.lyra.process.LYAbstractExcelGenerator;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.CSFormationEnfant;
import ch.globaz.perseus.business.constantes.CSSexePersonne;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneType;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.models.echeance.EcheanceLibre;
import ch.globaz.perseus.business.models.echeance.EcheanceLibreSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author JSI
 * 
 */
public class ListeEcheance extends LYAbstractExcelGenerator {

    public final static String COL_CAISSE = "col01";

    public final static String COL_GESTIONNAIRE = "col02";
    public final static String COL_MOTIF = "col07";
    public final static String COL_REQUERANT_NSS = "col03";
    public final static String COL_TIERS_CONCERNE = "col04";
    public final static String COL_TIERS_CONCERNE_DATE_NAISSANCE = "col05";
    public final static String COL_TIERS_CONCERNE_SEXE = "col06";
    public final static String COL_TITRE_SUFFIXE = "head";
    public final static String MODEL_PATH = "perseus/doc/model/excelml/listeEcheances.xml";
    public final static String TITRE_PRINCIPAL = "mainTitle";
    CommonExcelmlContainer container = null;
    private String dateDebut;
    private String dateFin;
    private Demande dernierDemande = null;
    private String derniereCaisse = null;
    private String emailAdresse;
    private String forCsCaisse;
    private String idDossier;
    private String idEcheance;
    private boolean isAidesEtudes;
    private boolean isAllocations;
    private boolean isDecisionProjetSansChoix;
    private boolean isDemandesEnAttente3Mois;
    private boolean isDossierDateRevision;
    private boolean isEcheanceLibre;
    private boolean isEnfantDe16ans;
    private boolean isEnfantDe18ans;
    private boolean isEnfantDe25ans;
    private boolean isEnfantDe6ans;
    private boolean isEtudiantsEtApprentis;
    private boolean isFemmeRetraite;
    private boolean isHommeRetraite;
    private boolean isIndemnitesJournalieres;
    private boolean isRentePont;
    private ArrayList<String> key = new ArrayList<String>();

    private String tempCaisse;

    private String tempGestionnaire;
    private String tempMotif;
    private String tempRequerantNSS;
    private String tempTiersConcerne;
    private String tempTiersConcerneDateNaissance;
    private String tempTiersConcerneSexe;

    public ListeEcheance() {

    }

    public ListeEcheance(BSession session) {
        container = new CommonExcelmlContainer();
    }

    /**
     * Ajoute une ligne dans la liste Excel qui va être générée
     * 
     * @param requerantNSS
     * @param tiersConcerne
     * @param gestionnaire
     * @param motif
     */
    private void addLine(String requerantNSS, String tiersConcerne, String gestionnaire, String motif,
            String tiersConcerneDateNaissance, String tiersConcerneSexe, String caisse, String idDossier)
            throws Exception {

        if (!entryExists(requerantNSS, motif, tiersConcerne)) {
            container.put(ListeEcheance.COL_REQUERANT_NSS, requerantNSS);
            container.put(ListeEcheance.COL_TIERS_CONCERNE, tiersConcerne);
            container.put(ListeEcheance.COL_TIERS_CONCERNE_DATE_NAISSANCE, tiersConcerneDateNaissance);
            container.put(ListeEcheance.COL_TIERS_CONCERNE_SEXE, tiersConcerneSexe);
            container.put(ListeEcheance.COL_GESTIONNAIRE, gestionnaire);
            container.put(ListeEcheance.COL_MOTIF, motif);
            container.put(ListeEcheance.COL_CAISSE, BSessionUtil.getSessionFromThreadContext().getCodeLibelle(caisse));
        }
    }

    private void addTitres() {
        container.put(ListeEcheance.TITRE_PRINCIPAL,
                BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_PRINCIPAL") + " "
                        + dateDebut.substring(3));
        container.put(ListeEcheance.COL_REQUERANT_NSS + ListeEcheance.COL_TITRE_SUFFIXE, BSessionUtil
                .getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_REQUERANT_NSS"));
        container.put(ListeEcheance.COL_TIERS_CONCERNE + ListeEcheance.COL_TITRE_SUFFIXE, BSessionUtil
                .getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_TIERS_CONCERNE"));
        container.put(ListeEcheance.COL_TIERS_CONCERNE_DATE_NAISSANCE + ListeEcheance.COL_TITRE_SUFFIXE, BSessionUtil
                .getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_TIERS_CONCERNE_DATE_NAISSANCE"));
        container.put(ListeEcheance.COL_TIERS_CONCERNE_SEXE + ListeEcheance.COL_TITRE_SUFFIXE, BSessionUtil
                .getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_TIERS_CONCERNE_SEXE"));
        container.put(ListeEcheance.COL_GESTIONNAIRE + ListeEcheance.COL_TITRE_SUFFIXE, BSessionUtil
                .getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_GESTIONNAIRE"));
        container.put(ListeEcheance.COL_MOTIF + ListeEcheance.COL_TITRE_SUFFIXE, BSessionUtil
                .getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_MOTIF"));
        container.put(ListeEcheance.COL_CAISSE + ListeEcheance.COL_TITRE_SUFFIXE, BSessionUtil
                .getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_TITRE_CAISSE"));
    }

    public ExcelmlWorkbook createDoc(String idEcheance) throws DocException {
        if (idEcheance == null) {
            throw new DocException("Unable to execute createDoc, the idEcheance is null!");
        }
        this.idEcheance = idEcheance;
        return this.createDoc();
    }

    public String createDocAndSave(String idEcheance) throws Exception {
        ExcelmlWorkbook wk = this.createDoc(idEcheance);
        return save(wk);
    }

    /**
     * Vérifie qu'on a pas déjà une ligne identique dans notre liste
     * 
     * @param requerantNSS
     * @param motif
     * @param tiersConcerne
     * @return true si une ligne identique existe déjà
     */
    private boolean entryExists(String requerantNSS, String motif, String tiersConcerne) {
        if (key.contains(requerantNSS + "," + motif + "," + tiersConcerne)) {
            return true;
        } else {
            key.add(requerantNSS + "," + motif + "," + tiersConcerne);
            return false;
        }
    }

    private void getCaisse(Demande demande) {
        derniereCaisse = demande.getSimpleDemande().getCsCaisse();
    }

    private void getCaisseRP(RentePont rp) {
        derniereCaisse = rp.getSimpleRentePont().getCsCaisse();
    }

    /**
     * Pour un dossier, retourne le code système de la caisse correspondant à la demande du dossier qui est validée et
     * qui n'a pas de date de fin. Retourne un texte vide si cette demande n'existe pas.
     * 
     * @param idDossier
     * @return code système de la caisse
     * @throws Exception
     */
    private String getCsCaisse(String idDossier) throws Exception {

        DemandeSearchModel demandeSearch = new DemandeSearchModel();
        demandeSearch.setForIdDossier(idDossier);
        demandeSearch.setForCsCaisse(forCsCaisse);
        demandeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        demandeSearch = PerseusServiceLocator.getDemandeService().search(demandeSearch);
        if (demandeSearch.getSize() > 0) {
            for (JadeAbstractModel abstractModel : demandeSearch.getSearchResults()) {
                Demande demande = (Demande) abstractModel;
                if (JadeStringUtil.isBlank(demande.getSimpleDemande().getDateFin())) {
                    return demande.getSimpleDemande().getCsCaisse();
                }
            }
        } else {
            RentePontSearchModel rpsearchModel = new RentePontSearchModel();
            rpsearchModel.setForIdDossier(idDossier);
            rpsearchModel.setOrderKey(RentePontSearchModel.WITHOUT_DATE_FIN);
            rpsearchModel = PerseusServiceLocator.getRentePontService().search(rpsearchModel);
            for (JadeAbstractModel abstractModel : rpsearchModel.getSearchResults()) {
                RentePont rp = (RentePont) abstractModel;
                return rp.getSimpleRentePont().getCsCaisse();
            }
        }

        return "";
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public String getForCsCaisse() {
        return forCsCaisse;
    }

    public String getIdEcheance() {
        return idEcheance;
    }

    @Override
    public String getModelPath() {
        return ListeEcheance.MODEL_PATH;
    }

    @Override
    public String getOutputName() {
        return "liste_Echeances";
    }

    /**
     * Retourne le champ de texte pour la case décrivant le tiers concerné, avec ses nom, prénom(s), date de naissance
     * et sexe
     * 
     * @param personneEtendue
     * @return texte descriptif
     */
    private String getTiersConcerne(PersonneEtendueComplexModel personneEtendue) {
        String tiersDetail = personneEtendue.getTiers().getDesignation1();
        tiersDetail += " " + personneEtendue.getTiers().getDesignation2();
        return tiersDetail;
    }

    /**
     * Retourne la date de naissance d'une personne étendue
     * 
     * @param personneEtendue
     * @return date de naissance
     */
    private String getTiersConcerneDateNaissance(PersonneEtendueComplexModel personneEtendue) {
        return personneEtendue.getPersonne().getDateNaissance();
    }

    /**
     * Retourne le sexe de la personne étendue
     * 
     * @param personneEtendue
     * @return "M" ou "F"
     */
    private String getTiersConcerneSexe(PersonneEtendueComplexModel personneEtendue) {
        String tiersDetail;
        if (CSSexePersonne.MALE.getCodeSystem().equals(personneEtendue.getPersonne().getSexe())) {
            tiersDetail = "M";
        } else {
            tiersDetail = "F";
        }
        return tiersDetail;
    }

    private boolean hasDateFinDernierDemandePCF(String idDossier) throws Exception {
        boolean hasDateFin = true;
        dernierDemande = PerseusServiceLocator.getDemandeService().getDerniereDemande(idDossier);
        if ((null != dernierDemande) && (JadeStringUtil.isEmpty(dernierDemande.getSimpleDemande().getDateFin()))) {
            hasDateFin = false;
            getCaisse(dernierDemande);
        }
        return hasDateFin;
    }

    private boolean hasDateFinDernierDemandeRP(String idDossier) throws Exception {
        boolean hasDateFin = true;
        RentePontSearchModel rpsearchModel = new RentePontSearchModel();
        rpsearchModel.setForIdDossier(idDossier);
        rpsearchModel.setWhereKey(RentePontSearchModel.WITHOUT_DATE_FIN);
        rpsearchModel = PerseusServiceLocator.getRentePontService().search(rpsearchModel);
        for (JadeAbstractModel abstractModel : rpsearchModel.getSearchResults()) {
            RentePont rp = (RentePont) abstractModel;
            hasDateFin = false;
            getCaisseRP(rp);
            break;
        }
        return hasDateFin;
    }

    /**
     * Retourne true/false pour indiquer si la dernière demande est cloturé par une date de fin
     * 
     * @param idDossier
     * @return boolean, true si présence d'une date de fin, false si pas de date de fin présente
     * @throws Exception
     */
    private boolean hasDateFinDerniereDemande(String idDossier) throws Exception {
        boolean hasDateFin = true;
        dernierDemande = PerseusServiceLocator.getDemandeService().getDerniereDemande(idDossier);
        if ((null != dernierDemande) && (JadeStringUtil.isEmpty(dernierDemande.getSimpleDemande().getDateFin()))) {
            hasDateFin = false;
            getCaisse(dernierDemande);
        } else {
            RentePontSearchModel rpsearchModel = new RentePontSearchModel();
            rpsearchModel.setForIdDossier(idDossier);
            rpsearchModel.setWhereKey(RentePontSearchModel.WITHOUT_DATE_FIN);
            rpsearchModel = PerseusServiceLocator.getRentePontService().search(rpsearchModel);
            for (JadeAbstractModel abstractModel : rpsearchModel.getSearchResults()) {
                RentePont rp = (RentePont) abstractModel;
                hasDateFin = false;
                getCaisseRP(rp);
                break;
            }

        }
        return hasDateFin;
    }

    public boolean isAidesEtudes() {
        return isAidesEtudes;
    }

    public boolean isAllocations() {
        return isAllocations;
    }

    public boolean isDecisionProjetSansChoix() {
        return isDecisionProjetSansChoix;
    }

    public boolean isDemandesEnAttente3Mois() {
        return isDemandesEnAttente3Mois;
    }

    public boolean isDossierDateRevision() {
        return isDossierDateRevision;
    }

    public boolean isEcheanceLibre() {
        return isEcheanceLibre;
    }

    public boolean isEnfantDe16ans() {
        return isEnfantDe16ans;
    }

    public boolean isEnfantDe18ans() {
        return isEnfantDe18ans;
    }

    public boolean isEnfantDe25ans() {
        return isEnfantDe25ans;
    }

    public boolean isEnfantDe6ans() {
        return isEnfantDe6ans;
    }

    public boolean isEtudiantsEtApprentis() {
        return isEtudiantsEtApprentis;
    }

    public boolean isFemmeRetraite() {
        return isFemmeRetraite;
    }

    public boolean isHommeRetraite() {
        return isHommeRetraite;
    }

    public boolean isIndemnitesJournalieres() {
        return isIndemnitesJournalieres;
    }

    private boolean isNAnsDansMoisDeTraitement(String dateNaiss, int nbAnnee) {

        try {

            int annee = new JADate(dateDebut).getYear() - new JADate(dateNaiss).getYear();
            int mois = new JADate(dateDebut).getMonth() - new JADate(dateNaiss).getMonth();

            if ((annee == nbAnnee) && (mois == 0)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // this.getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
            // "REListerEcheancesProcess - isNAnsDansMoisDeTraitement");
            return false;
        }
    }

    public boolean isRentePont() {
        return isRentePont;
    }

    /**
     * Ajoute les échéances qui, parmi les données financières, ont des aides aux études dont la date de fin arrive
     * durant le mois choisi
     * 
     * @throws Exception
     */
    private void loadAidesEtudes() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_AIDES_ETUDES");
        FortuneSearchModel fortuneSearch = new FortuneSearchModel();
        fortuneSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        fortuneSearch.setForCsCaisse(forCsCaisse);
        fortuneSearch.setBetweenDateFinDebut(dateDebut);
        fortuneSearch.setBetweenDateFinFin(dateFin);
        ArrayList<String> types = new ArrayList<String>(1);
        types.add(RevenuType.AIDE_FORMATION.getId().toString());
        fortuneSearch.setInType(types);
        fortuneSearch = PerseusServiceLocator.getFortuneService().search(fortuneSearch);
        for (JadeAbstractModel abstractModel : fortuneSearch.getSearchResults()) {
            Fortune fortune = (Fortune) abstractModel;
            if (!hasDateFinDerniereDemande(fortune.getDemande().getDossier().getId())) {
                tempRequerantNSS = fortune.getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                tempTiersConcerne = getTiersConcerne(fortune.getMembreFamille().getPersonneEtendue());
                tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(fortune.getMembreFamille()
                        .getPersonneEtendue());
                tempTiersConcerneSexe = getTiersConcerneSexe(fortune.getMembreFamille().getPersonneEtendue());
                tempGestionnaire = fortune.getDemande().getDossier().getDossier().getGestionnaire();
                tempCaisse = fortune.getDemande().getSimpleDemande().getCsCaisse();
                idDossier = fortune.getDemande().getDossier().getId();
                addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                        tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
            } else {
                continue;
            }
        }
    }

    /**
     * Ajoute les échéances qui, parmi les données financières, ont des allocations dont la date de fin arrive durant le
     * mois choisi
     * 
     * @throws Exception
     */
    private void loadAllocations() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_ALLOCATIONS");
        FortuneSearchModel fortuneSearch = new FortuneSearchModel();
        fortuneSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        fortuneSearch.setForCsCaisse(forCsCaisse);
        fortuneSearch.setBetweenDateFinDebut(dateDebut);
        fortuneSearch.setBetweenDateFinFin(dateFin);
        ArrayList<String> types = new ArrayList<String>(1);
        types.add(RevenuType.ALLOCATION_CANTONALE_MATERNITE.getId().toString());
        types.add(RevenuType.ALLOCATIONS_AMINH.getId().toString());
        fortuneSearch.setInType(types);
        fortuneSearch = PerseusServiceLocator.getFortuneService().search(fortuneSearch);
        for (JadeAbstractModel abstractModel : fortuneSearch.getSearchResults()) {
            Fortune fortune = (Fortune) abstractModel;
            if (!hasDateFinDerniereDemande(fortune.getDemande().getDossier().getId())) {
                tempRequerantNSS = fortune.getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                tempTiersConcerne = getTiersConcerne(fortune.getMembreFamille().getPersonneEtendue());
                tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(fortune.getMembreFamille()
                        .getPersonneEtendue());
                tempTiersConcerneSexe = getTiersConcerneSexe(fortune.getMembreFamille().getPersonneEtendue());
                tempGestionnaire = fortune.getDemande().getDossier().getDossier().getGestionnaire();
                tempCaisse = fortune.getDemande().getSimpleDemande().getCsCaisse();
                idDossier = fortune.getDemande().getDossier().getId();
                addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                        tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
            } else {
                continue;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.businessimpl.services.doc.excel.impl.PerseusAbstractExcelServiceImpl#loadResults()
     */
    @Override
    public IMergingContainer loadData() throws Exception {
        addTitres();
        if (isDossierDateRevision) {
            loadDossierDateRevision();
            LoadRPDateRevision();
        }
        if (isEnfantDe6ans) {
            loadEnfantDe6ans();
        }
        if (isEnfantDe16ans) {
            loadEnfantDe16ans();
        }
        if (isEnfantDe18ans) {
            loadEnfantDe18ans();
        }
        if (isEnfantDe25ans) {
            loadEnfantDe25ans();
        }
        if (isEtudiantsEtApprentis) {
            loadEtudiantsEtApprentis();
        }
        if (isFemmeRetraite) {
            loadRetraite(CSSexePersonne.FEMELLE.getCodeSystem());
        }
        if (isHommeRetraite) {
            loadRetraite(CSSexePersonne.MALE.getCodeSystem());
        }
        if ("01".equals(dateDebut.substring(3, 5))) {
            // On ne controle ça qu'en janvier
            loadDateCession();
        }
        if (isIndemnitesJournalieres) {
            loadIndemnitesJournalieres();
        }
        if (isAllocations) {
            loadAllocations();
        }
        if (isDecisionProjetSansChoix) {
            loadDecisionProjetSansChoix();
        }
        if (isEcheanceLibre) {
            loadEcheancesLibres();
        }
        if (isDemandesEnAttente3Mois) {
            loadDemandesEnAttente3Mois();
        }
        if (isRentePont) {
            loadRentePont();
        }
        if (isAidesEtudes) {
            loadAidesEtudes();
        }

        return container;
    }

    /**
     * Ajoute les échéances des données financières dont la date de cession de biens tombe durant le mois choisi
     * 
     * @throws Exception
     */
    private void loadDateCession() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_DATE_CESSION");
        FortuneSearchModel fortuneSearch = new FortuneSearchModel();
        fortuneSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        fortuneSearch.setForCsCaisse(forCsCaisse);
        ArrayList<String> types = new ArrayList<String>(1);
        types.add(FortuneType.CESSION.getId().toString());
        fortuneSearch.setInType(types);
        fortuneSearch = PerseusServiceLocator.getFortuneService().search(fortuneSearch);
        for (JadeAbstractModel abstractModel : fortuneSearch.getSearchResults()) {
            Fortune fortune = (Fortune) abstractModel;
            if (!hasDateFinDerniereDemande(fortune.getDemande().getDossier().getId())) {
                tempRequerantNSS = fortune.getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                tempTiersConcerne = getTiersConcerne(fortune.getMembreFamille().getPersonneEtendue());
                tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(fortune.getMembreFamille()
                        .getPersonneEtendue());
                tempTiersConcerneSexe = getTiersConcerneSexe(fortune.getMembreFamille().getPersonneEtendue());
                tempGestionnaire = fortune.getDemande().getDossier().getDossier().getGestionnaire();
                tempCaisse = fortune.getDemande().getSimpleDemande().getCsCaisse();
                idDossier = fortune.getDemande().getDossier().getId();
                addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                        tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
            } else {
                continue;
            }
        }
    }

    /**
     * Récupère et ajoute aux échéances les projets de décisions dont aucun choix n'a été indiqué un mois après leur
     * date de validation
     * 
     * @throws Exception
     */
    private void loadDecisionProjetSansChoix() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                "EXCEL_PF_ECHEANCES_MOTIF_PROJET_DECISION_SANS_CHOIX");
        DecisionSearchModel decisionSearch = new DecisionSearchModel();
        decisionSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionSearch.setForCsCaisse(forCsCaisse);
        decisionSearch.setForCsTypeDecision(CSTypeDecision.PROJET.getCodeSystem());
        decisionSearch.setForCsChoix("0");
        decisionSearch.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        // decisionSearch.setBetweenDateValidationDebut(JadeDateUtil.addMonths(this.dateDebut, -1));
        decisionSearch.setBetweenDateValidationFin(JadeDateUtil.addMonths(dateFin, -1));
        decisionSearch = PerseusServiceLocator.getDecisionService().search(decisionSearch);
        for (JadeAbstractModel abstractModel : decisionSearch.getSearchResults()) {
            Decision decision = (Decision) abstractModel;
            if (!hasDateFinDerniereDemande(decision.getDemande().getDossier().getId())) {
                tempRequerantNSS = decision.getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                tempTiersConcerne = getTiersConcerne(decision.getDemande().getSituationFamiliale().getRequerant()
                        .getMembreFamille().getPersonneEtendue());
                tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(decision.getDemande()
                        .getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue());
                tempTiersConcerneSexe = getTiersConcerneSexe(decision.getDemande().getSituationFamiliale()
                        .getRequerant().getMembreFamille().getPersonneEtendue());
                tempGestionnaire = decision.getDemande().getDossier().getDossier().getGestionnaire();
                tempCaisse = decision.getDemande().getSimpleDemande().getCsCaisse();
                idDossier = decision.getDemande().getDossier().getId();
                addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                        tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
            } else {
                continue;
            }
        }
    }

    /**
     * Récupère et ajoute aux échéances les demandes non validées depuis plus de trois mois
     * 
     * @throws Exception
     */
    private void loadDemandesEnAttente3Mois() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                "EXCEL_PF_ECHEANCES_MOTIF_DEMANDE_ATTENTE_3_MOIS");
        DemandeSearchModel demandeSearch = new DemandeSearchModel();
        demandeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        demandeSearch.setForCsCaisse(forCsCaisse);
        demandeSearch.setNotCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
        demandeSearch.setBetweenDateDepotFin(JadeDateUtil.addMonths(dateFin, -3));
        demandeSearch = PerseusServiceLocator.getDemandeService().search(demandeSearch);
        for (JadeAbstractModel abstractModel : demandeSearch.getSearchResults()) {
            Demande demande = (Demande) abstractModel;
            tempRequerantNSS = demande.getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue()
                    .getPersonneEtendue().getNumAvsActuel();
            tempTiersConcerne = getTiersConcerne(demande.getSituationFamiliale().getRequerant().getMembreFamille()
                    .getPersonneEtendue());
            tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(demande.getSituationFamiliale()
                    .getRequerant().getMembreFamille().getPersonneEtendue());
            tempTiersConcerneSexe = getTiersConcerneSexe(demande.getSituationFamiliale().getRequerant()
                    .getMembreFamille().getPersonneEtendue());
            tempGestionnaire = demande.getDossier().getDossier().getGestionnaire();
            tempCaisse = demande.getSimpleDemande().getCsCaisse();
            idDossier = demande.getDossier().getId();
            addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif, tempTiersConcerneDateNaissance,
                    tempTiersConcerneSexe, tempCaisse, idDossier);
        }
    }

    /**
     * Ajoute aux échéances les dossiers dont la date de révision tombe durant le mois choisi
     * 
     * @throws Exception
     */
    private void loadDossierDateRevision() throws Exception {
        DossierSearchModel dossierSearch = new DossierSearchModel();
        dossierSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        dossierSearch.setBetweenDateRevisionDebut(dateDebut.substring(3));
        dossierSearch.setBetweenDateRevisionFin(dateFin.substring(3));

        dossierSearch = PerseusServiceLocator.getDossierService().search(dossierSearch);
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                "EXCEL_PF_ECHEANCES_MOTIF_DATE_REVISION_DOSSIER");
        for (JadeAbstractModel abstractModel : dossierSearch.getSearchResults()) {
            Dossier dossier = (Dossier) abstractModel;
            if (!hasDateFinDernierDemandePCF(dossier.getId())) {
                if (null != dernierDemande) {
                    DecisionSearchModel decisionSearchMode = new DecisionSearchModel();
                    decisionSearchMode.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                    decisionSearchMode.setForIdDemande(dernierDemande.getSimpleDemande().getIdDemande());
                    decisionSearchMode.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                    decisionSearchMode.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());

                    if ((PerseusServiceLocator.getDecisionService().count(decisionSearchMode) == 0)) {
                        continue;
                    }
                }

                if ((JadeStringUtil.isEmpty(forCsCaisse)) || (forCsCaisse.equals(derniereCaisse))) {
                    tempTiersConcerne = getTiersConcerne(dossier.getDemandePrestation().getPersonneEtendue());
                    tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(dossier.getDemandePrestation()
                            .getPersonneEtendue());
                    tempTiersConcerneSexe = getTiersConcerneSexe(dossier.getDemandePrestation().getPersonneEtendue());
                    tempRequerantNSS = dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel();
                    tempGestionnaire = dossier.getDossier().getGestionnaire();
                    tempCaisse = getCsCaisse(dossier.getId());
                    idDossier = dossier.getId();
                    addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                            tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
                } else {
                    derniereCaisse = "";
                }
            } else {
                continue;
            }
        }
    }

    /**
     * Ajoute aux échéances les échéances libres créées à partir des dossiers des requérants
     * 
     * @throws Exception
     */
    private void loadEcheancesLibres() throws Exception {
        EcheanceLibreSearchModel echeanceLibreSearch = new EcheanceLibreSearchModel();
        echeanceLibreSearch.setForDateButoire(dateDebut.substring(3));
        echeanceLibreSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        echeanceLibreSearch = PerseusServiceLocator.getEcheanceLibreService().search(echeanceLibreSearch);
        for (JadeAbstractModel abstractModel : echeanceLibreSearch.getSearchResults()) {
            EcheanceLibre echeanceLibre = (EcheanceLibre) abstractModel;
            if (!hasDateFinDerniereDemande(echeanceLibre.getDossier().getId())) {
                if ((JadeStringUtil.isEmpty(forCsCaisse)) || (forCsCaisse.equals(derniereCaisse))) {
                    tempGestionnaire = echeanceLibre.getDossier().getDossier().getGestionnaire();
                    tempMotif = echeanceLibre.getSimpleEcheanceLibre().getMotif();
                    tempRequerantNSS = echeanceLibre.getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel();
                    tempTiersConcerne = getTiersConcerne(echeanceLibre.getDossier().getDemandePrestation()
                            .getPersonneEtendue());
                    tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(echeanceLibre.getDossier()
                            .getDemandePrestation().getPersonneEtendue());
                    tempTiersConcerneSexe = getTiersConcerneSexe(echeanceLibre.getDossier().getDemandePrestation()
                            .getPersonneEtendue());
                    tempCaisse = getCsCaisse(echeanceLibre.getDossier().getId());
                    idDossier = echeanceLibre.getDossier().getId();
                    addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                            tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
                } else {
                    derniereCaisse = "";
                }
            } else {
                continue;
            }

        }
    }

    /**
     * Ajoute aux échéances tous les enfants qui ont leur anniversaire (un age donné) durant le mois choisi
     * 
     * @param age
     * @throws Exception
     */
    private void loadEnfant(int age) throws Exception {
        this.loadEnfant(age, false, false, false);
    }

    /**
     * Ajoute aux échéances tous les enfants qui ont leur anniversaire (un age donné) durant le mois choisi, en
     * spécifiant quel(s) type(s) de formation on prend en compte
     * 
     * @param age
     * @param etudiants
     * @param ecoliers
     * @param apprentis
     * @throws Exception
     */
    private void loadEnfant(int age, boolean etudiants, boolean ecoliers, boolean apprentis) throws Exception {
        EnfantFamilleSearchModel enfantSearch = new EnfantFamilleSearchModel();
        if (age >= 0) {
            enfantSearch.setBetweenDateNaissanceDebut(JadeDateUtil.addYears(dateDebut, -age));
            enfantSearch.setBetweenDateNaissanceFin(JadeDateUtil.addYears(dateFin, -age));
        }
        // String formations = "";
        ArrayList<String> formations = new ArrayList<String>();

        if (etudiants) {
            formations.add(CSFormationEnfant.ETUDIANT.getCodeSystem());
        }
        if (ecoliers) {
            formations.add(CSFormationEnfant.ECOLIER.getCodeSystem());
        }
        if (apprentis) {
            formations.add(CSFormationEnfant.APPRENTI.getCodeSystem());
        }

        enfantSearch.setForCsFormationIn(formations);
        enfantSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        enfantSearch = PerseusServiceLocator.getEnfantFamilleService().search(enfantSearch);
        for (JadeAbstractModel abstractModel : enfantSearch.getSearchResults()) {
            EnfantFamille enfantFamille = (EnfantFamille) abstractModel;

            tempTiersConcerne = getTiersConcerne(enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue());
            tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(enfantFamille.getEnfant().getMembreFamille()
                    .getPersonneEtendue());
            tempTiersConcerneSexe = getTiersConcerneSexe(enfantFamille.getEnfant().getMembreFamille()
                    .getPersonneEtendue());

            DemandeSearchModel demandeSearch = new DemandeSearchModel();
            demandeSearch.setForCsCaisse(forCsCaisse);
            demandeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            demandeSearch.setForIdSituationFamiliale(enfantFamille.getSimpleEnfantFamille().getIdSituationFamiliale());
            demandeSearch = PerseusServiceLocator.getDemandeService().search(demandeSearch);
            if (demandeSearch.getSize() > 0) {
                Demande demande = (Demande) demandeSearch.getSearchResults()[0];
                if (!hasDateFinDerniereDemande(demande.getDossier().getId())) {
                    tempRequerantNSS = demande.getSituationFamiliale().getRequerant().getMembreFamille()
                            .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                    tempGestionnaire = demande.getDossier().getDossier().getGestionnaire();
                    tempCaisse = demande.getSimpleDemande().getCsCaisse();
                    idDossier = demande.getDossier().getId();
                    addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                            tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
                } else {
                    continue;
                }

            }
        }
    }

    /**
     * Ajoute les enfants passant à 16 ans durant le mois choisi à la liste d'échéances
     * 
     * @throws Exception
     */
    private void loadEnfantDe16ans() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_ENFANT_16ANS");
        this.loadEnfant(16);
    }

    /**
     * Ajoute les enfants passant à 18 ans durant le mois choisi à la liste d'échéances
     * 
     * @throws Exception
     */
    private void loadEnfantDe18ans() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_ENFANT_18ANS");
        this.loadEnfant(18);
    }

    /**
     * Ajoute les enfants passant à 25 ans durant le mois choisi à la liste d'échéances
     * 
     * @throws Exception
     */
    private void loadEnfantDe25ans() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_ENFANT_25ANS");
        this.loadEnfant(25);
    }

    /**
     * Ajoute les enfants passant à 6 ans durant le mois choisi à la liste d'échéances
     * 
     * @throws Exception
     */
    private void loadEnfantDe6ans() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_ENFANT_6ANS");
        this.loadEnfant(6);
    }

    /**
     * Ajoute aux échéances tous les étudiants et tous les apprentis
     * 
     * @throws Exception
     */
    private void loadEtudiantsEtApprentis() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_ETUDIANTS_APPRENTIS");
        this.loadEnfant(-1, true, false, true);
    }

    /**
     * Ajoute les échéances qui, parmi les données financières, ont des indemnités journalières dont la date de fin
     * arrive durant le mois choisi
     * 
     * @throws Exception
     */
    private void loadIndemnitesJournalieres() throws Exception {
        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                "EXCEL_PF_ECHEANCES_MOTIF_INDEMNITES_JOURNALIERES");
        FortuneSearchModel fortuneSearch = new FortuneSearchModel();
        fortuneSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        fortuneSearch.setBetweenDateFinDebut(dateDebut);
        fortuneSearch.setBetweenDateFinFin(dateFin);
        fortuneSearch.setForCsCaisse(forCsCaisse);
        ArrayList<String> types = new ArrayList<String>(1);
        types.add(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS.getId().toString());
        types.add(RevenuType.INDEMNITES_JOURNALIERES_AI.getId().toString());
        types.add(RevenuType.INDEMNITES_JOURNALIERES_APG.getId().toString());
        types.add(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE.getId().toString());
        types.add(RevenuType.INDEMNITES_JOURNALIERES_MALADIE.getId().toString());
        types.add(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE.getId().toString());
        fortuneSearch.setInType(types);
        fortuneSearch = PerseusServiceLocator.getFortuneService().search(fortuneSearch);
        for (JadeAbstractModel abstractModel : fortuneSearch.getSearchResults()) {
            Fortune fortune = (Fortune) abstractModel;
            if (!hasDateFinDerniereDemande(fortune.getDemande().getDossier().getId())) {
                tempRequerantNSS = fortune.getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                tempTiersConcerne = getTiersConcerne(fortune.getMembreFamille().getPersonneEtendue());
                tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(fortune.getMembreFamille()
                        .getPersonneEtendue());
                tempTiersConcerneSexe = getTiersConcerneSexe(fortune.getMembreFamille().getPersonneEtendue());
                tempGestionnaire = fortune.getDemande().getDossier().getDossier().getGestionnaire();
                tempCaisse = fortune.getDemande().getSimpleDemande().getCsCaisse();
                idDossier = fortune.getDemande().getDossier().getId();
                addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                        tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
            } else {
                continue;
            }
        }
    }

    /**
     * Methode permettant de parcourri l'ensemble des rentes ponts qui n'ont pas de date de fin pour en récupérer les
     * personnes passant à l'age de la retraite
     * 
     * Traitement en fonction du sexe du requerant et du conjoint + Traitement en fonction de l'age de la retraite du
     * requérant et du conjoint
     * 
     * @throws Exception
     */

    private void loadRentePont() throws Exception {
        RentePontSearchModel rentePontSearch = new RentePontSearchModel();
        rentePontSearch.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
        rentePontSearch.setWhereKey(RentePontSearchModel.WITHOUT_DATE_FIN);
        rentePontSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        rentePontSearch = PerseusServiceLocator.getRentePontService().search(rentePontSearch);

        for (JadeAbstractModel model : rentePontSearch.getSearchResults()) {
            RentePont rentePont = (RentePont) model;

            if (forCsCaisse.equals(rentePont.getSimpleRentePont().getCsCaisse())) {

                // REQUERANT
                // Traitement en fonction du sexe du requérant
                int ageRetraite;
                if (CSSexePersonne.MALE.getCodeSystem().equals(
                        rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne().getSexe())) {
                    ageRetraite = IPFConstantes.AGE_RETRAITE_HOMME;
                    tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                            "EXCEL_PF_ECHEANCES_MOTIF_HOMME_RETRAITE");
                } else {
                    ageRetraite = IPFConstantes.AGE_RETRAITE_FEMME;
                    tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                            "EXCEL_PF_ECHEANCES_MOTIF_FEMME_RETRAITE");
                }

                // Récupération des dates du jour et de naissance
                String dateDeNaissance = rentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                        .getPersonne().getDateNaissance();

                // Comparaison des dates pour voir si il y a 65 ans ou plus
                if (isNAnsDansMoisDeTraitement(rentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                        .getPersonne().getDateNaissance(), ageRetraite)) {
                    // Si oui, récupération des informations du tiers
                    tempTiersConcerne = getTiersConcerne(rentePont.getDossier().getDemandePrestation()
                            .getPersonneEtendue());
                    tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(rentePont.getDossier()
                            .getDemandePrestation().getPersonneEtendue());
                    tempTiersConcerneSexe = getTiersConcerneSexe(rentePont.getDossier().getDemandePrestation()
                            .getPersonneEtendue());

                    tempRequerantNSS = rentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel();
                    tempGestionnaire = rentePont.getDossier().getDossier().getGestionnaire();
                    tempCaisse = rentePont.getSimpleRentePont().getCsCaisse();
                    idDossier = rentePont.getDossier().getId();

                    // Ajout de la ligne dans la liste
                    addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                            tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);

                }

                // CONJOINT
                if (!JadeStringUtil.isEmpty(rentePont.getSituationFamiliale().getConjoint().getId())) {
                    // Traitement en fonction du sexe du conjoint
                    int ageRetraiteConjoint;
                    if (CSSexePersonne.MALE.getCodeSystem().equals(
                            rentePont.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                                    .getPersonne().getSexe())) {
                        ageRetraiteConjoint = IPFConstantes.AGE_RETRAITE_HOMME;
                        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                                "EXCEL_PF_ECHEANCES_MOTIF_HOMME_RETRAITE");
                    } else {
                        ageRetraiteConjoint = IPFConstantes.AGE_RETRAITE_FEMME;
                        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                                "EXCEL_PF_ECHEANCES_MOTIF_FEMME_RETRAITE");
                    }

                    // Comparaison des dates pour voir si il y a 65 ans ou plus
                    if (isNAnsDansMoisDeTraitement(rentePont.getSituationFamiliale().getConjoint().getMembreFamille()
                            .getPersonneEtendue().getPersonne().getDateNaissance(), ageRetraiteConjoint)) {
                        // Si oui, récupération des informations du tiers

                        tempTiersConcerne = getTiersConcerne(rentePont.getSituationFamiliale().getConjoint()
                                .getMembreFamille().getPersonneEtendue());
                        tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(rentePont
                                .getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue());
                        tempTiersConcerneSexe = getTiersConcerneSexe(rentePont.getSituationFamiliale().getConjoint()
                                .getMembreFamille().getPersonneEtendue());

                        tempRequerantNSS = rentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                                .getPersonneEtendue().getNumAvsActuel();
                        tempGestionnaire = rentePont.getDossier().getDossier().getGestionnaire();
                        tempCaisse = rentePont.getSimpleRentePont().getCsCaisse();
                        idDossier = rentePont.getDossier().getId();

                        // Ajout de la ligne dans la liste
                        addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                                tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);

                    }
                }
            }
        }
    }

    /**
     * Ajoute à la liste tous les dossiers ont un requérant ou un conjoint qui part à la retraite durant le mois, en
     * fonction de son sexe (et donc de l'âge lié)
     * 
     * @param csSexe
     * @throws Exception
     */
    private void loadRetraite(String csSexe) throws Exception {
        int age;
        if (CSSexePersonne.MALE.getCodeSystem().equals(csSexe)) {
            tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_HOMME_RETRAITE");
            age = IPFConstantes.AGE_RETRAITE_HOMME;
        } else {
            tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel("EXCEL_PF_ECHEANCES_MOTIF_FEMME_RETRAITE");
            age = IPFConstantes.AGE_RETRAITE_FEMME;
        }
        DemandeSearchModel demandeSearch = new DemandeSearchModel();
        demandeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        demandeSearch.setForCsCaisse(forCsCaisse);
        demandeSearch.setBetweenAgeRetraiteConjointDebut(JadeDateUtil.addYears(dateDebut, -age));
        demandeSearch.setBetweenAgeRetraiteConjointFin(JadeDateUtil.addYears(dateFin, -age));
        demandeSearch.setForCsSexeConjoint(csSexe);
        demandeSearch.setBetweenAgeRetraiteRequerantDebut(JadeDateUtil.addYears(dateDebut, -age));
        demandeSearch.setBetweenAgeRetraiteRequerantFin(JadeDateUtil.addYears(dateFin, -age));
        demandeSearch.setForCsSexeRequerant(csSexe);
        demandeSearch = PerseusServiceLocator.getDemandeService().search(demandeSearch);

        for (JadeAbstractModel abstractModel : demandeSearch.getSearchResults()) {
            Demande demande = (Demande) abstractModel;
            if (!hasDateFinDerniereDemande(demande.getDossier().getId())) {
                if (isNAnsDansMoisDeTraitement(demande.getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getPersonne().getDateNaissance(), age)) {
                    tempTiersConcerne = getTiersConcerne(demande.getSituationFamiliale().getRequerant()
                            .getMembreFamille().getPersonneEtendue());
                    tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(demande.getSituationFamiliale()
                            .getRequerant().getMembreFamille().getPersonneEtendue());
                    tempTiersConcerneSexe = getTiersConcerneSexe(demande.getSituationFamiliale().getRequerant()
                            .getMembreFamille().getPersonneEtendue());
                    tempRequerantNSS = demande.getSituationFamiliale().getRequerant().getMembreFamille()
                            .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                    tempGestionnaire = demande.getDossier().getDossier().getGestionnaire();
                    tempCaisse = demande.getSimpleDemande().getCsCaisse();
                    idDossier = demande.getDossier().getId();
                    addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                            tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
                }
                if (isNAnsDansMoisDeTraitement(demande.getSituationFamiliale().getConjoint().getMembreFamille()
                        .getPersonneEtendue().getPersonne().getDateNaissance(), age)) {
                    tempTiersConcerne = getTiersConcerne(demande.getSituationFamiliale().getConjoint()
                            .getMembreFamille().getPersonneEtendue());
                    tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(demande.getSituationFamiliale()
                            .getConjoint().getMembreFamille().getPersonneEtendue());
                    tempTiersConcerneSexe = getTiersConcerneSexe(demande.getSituationFamiliale().getConjoint()
                            .getMembreFamille().getPersonneEtendue());
                    tempRequerantNSS = demande.getSituationFamiliale().getRequerant().getMembreFamille()
                            .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                    tempGestionnaire = demande.getDossier().getDossier().getGestionnaire();
                    tempCaisse = demande.getSimpleDemande().getCsCaisse();
                    idDossier = demande.getDossier().getId();
                    addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                            tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
                }
            } else {
                continue;
            }
        }
    }

    /**
     * Ajoute à la liste toutes les demandes de rentes-pont dont la date de début commence à la date de l'échéance et
     * qui n'ont pas de demande précédente. C'est la date de début de la demande qui est utilisé pour la révision et non
     * la date du dossier
     * 
     * @param
     * @throws Exception
     */
    private void LoadRPDateRevision() throws Exception {

        RentePontSearchModel rpsearch = new RentePontSearchModel();
        rpsearch.setDateDebut(JadeDateUtil.addMonths(dateDebut, -12));
        rpsearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        rpsearch.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
        rpsearch = PerseusServiceLocator.getRentePontService().search(rpsearch);

        tempMotif = BSessionUtil.getSessionFromThreadContext().getLabel(
                "EXCEL_PF_ECHEANCES_MOTIF_DATE_REVISION_DOSSIER");

        for (JadeAbstractModel abstractModel : rpsearch.getSearchResults()) {
            RentePont rp = (RentePont) abstractModel;

            RentePontSearchModel rpPrecedenteSearch = new RentePontSearchModel();
            rpPrecedenteSearch.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
            rpPrecedenteSearch.setBeforeDateDebut(rp.getSimpleRentePont().getDateDebut());
            rpPrecedenteSearch.setForIdDossier(rp.getDossier().getDossier().getIdDossier());

            if (0 == PerseusServiceLocator.getRentePontService().count(rpPrecedenteSearch)) {
                if (!hasDateFinDernierDemandeRP(rp.getDossier().getDossier().getIdDossier())) {
                    if ((JadeStringUtil.isEmpty(forCsCaisse)) || (forCsCaisse.equals(derniereCaisse))) {
                        tempTiersConcerne = getTiersConcerne(rp.getDossier().getDemandePrestation()
                                .getPersonneEtendue());
                        tempTiersConcerneDateNaissance = getTiersConcerneDateNaissance(rp.getDossier()
                                .getDemandePrestation().getPersonneEtendue());
                        tempTiersConcerneSexe = getTiersConcerneSexe(rp.getDossier().getDemandePrestation()
                                .getPersonneEtendue());
                        tempRequerantNSS = rp.getDossier().getDemandePrestation().getPersonneEtendue()
                                .getPersonneEtendue().getNumAvsActuel();
                        tempGestionnaire = rp.getDossier().getDossier().getGestionnaire();
                        tempCaisse = getCsCaisse(rp.getDossier().getId());
                        idDossier = rp.getDossier().getId();
                        addLine(tempRequerantNSS, tempTiersConcerne, tempGestionnaire, tempMotif,
                                tempTiersConcerneDateNaissance, tempTiersConcerneSexe, tempCaisse, idDossier);
                    } else {
                        derniereCaisse = "";
                    }
                }
            }
        }

    }

    public void setAidesEtudes(boolean isAidesEtudes) {
        this.isAidesEtudes = isAidesEtudes;
    }

    public void setAllocations(boolean isAllocations) {
        this.isAllocations = isAllocations;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDecisionProjetSansChoix(boolean isDecisionProjetSansChoix) {
        this.isDecisionProjetSansChoix = isDecisionProjetSansChoix;
    }

    public void setDemandesEnAttente3Mois(boolean isDemandesEnAttente3Mois) {
        this.isDemandesEnAttente3Mois = isDemandesEnAttente3Mois;
    }

    public void setDossierDateRevision(boolean isDossierDateRevision) {
        this.isDossierDateRevision = isDossierDateRevision;
    }

    public void setEcheanceLibre(boolean isEcheanceLibre) {
        this.isEcheanceLibre = isEcheanceLibre;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public void setEnfantDe16ans(boolean isEnfantDe16ans) {
        this.isEnfantDe16ans = isEnfantDe16ans;
    }

    public void setEnfantDe18ans(boolean isEnfantDe18ans) {
        this.isEnfantDe18ans = isEnfantDe18ans;
    }

    public void setEnfantDe25ans(boolean isEnfantDe25ans) {
        this.isEnfantDe25ans = isEnfantDe25ans;
    }

    public void setEnfantDe6ans(boolean isEnfantDe6ans) {
        this.isEnfantDe6ans = isEnfantDe6ans;
    }

    public void setEtudiantsEtApprentis(boolean isEtudiantsEtApprentis) {
        this.isEtudiantsEtApprentis = isEtudiantsEtApprentis;
    }

    public void setFemmeRetraite(boolean isFemmeRetraite) {
        this.isFemmeRetraite = isFemmeRetraite;
    }

    public void setForCsCaisse(String forCsCaisse) {
        this.forCsCaisse = forCsCaisse;
    }

    public void setHommeRetraite(boolean isHommeRetraite) {
        this.isHommeRetraite = isHommeRetraite;
    }

    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    public void setIndemnitesJournalieres(boolean isIndemnitesJournalieres) {
        this.isIndemnitesJournalieres = isIndemnitesJournalieres;
    }

    public void setRentePont(boolean isRentePont) {
        this.isRentePont = isRentePont;
    }

}
