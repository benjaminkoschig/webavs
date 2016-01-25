package globaz.cygnus.process.adaptationAnnuelle.regime;

import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REInfoCompta;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFTypesDemandeJointDossierJointTiers;
import globaz.cygnus.db.demandes.RFTypesDemandeJointDossierJointTiersManager;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefusManager;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.services.adaptationJournaliere.RFAdaptationJournaliereContext;
import globaz.cygnus.services.preparerDecision.RFDecisionData;
import globaz.cygnus.services.preparerDecision.RFGenererDecisionsEntitesService;
import globaz.cygnus.services.preparerDecision.RFGenererPaiementService;
import globaz.cygnus.services.preparerDecision.RFImputationDemandesData;
import globaz.cygnus.services.preparerDecision.RFImputationDemandesService;
import globaz.cygnus.services.preparerDecision.RFImputationQdsData;
import globaz.cygnus.services.preparerDecision.RFPersistanceImputationDemandesDecisionsService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;
import com.google.gson.Gson;

public class RFProcessAdaptationAnnuelleRegimeHandlerEntity implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    private final String IDDEMANDEVIRTUELLE = "IDDEMANDEVIRTUELLE";
    private List<String[]> logsList = null;
    DecisionPcVO pcVOs = null;
    private Map<Enum<?>, String> properties = null;

    public RFProcessAdaptationAnnuelleRegimeHandlerEntity(List<String[]> logsList) {
        this.logsList = logsList;
    }

    private BigDecimal calculMontantInitialSelonPrestationAccordee(String dateDebutTraitement,
            String dateFinTraitement, String montantMensuel) throws Exception {

        if (JadeStringUtil.isBlankOrZero(dateFinTraitement)) {
            dateFinTraitement = "31.12." + String.valueOf(JACalendar.today().getYear() + 1);
        }

        // nombre de mois rétroactif - attention la date de dernier paiement est à prendre en compte
        int nbMois = JadeDateUtil.getNbMonthsBetween(dateDebutTraitement, dateFinTraitement);

        // montant mensuel : montant accepté / nombre de mois
        BigDecimal montantMensuelBigDec = new BigDecimal(montantMensuel);

        return montantMensuelBigDec.multiply(new BigDecimal(nbMois));
    }

    private BigDecimal calculMontantFacture(String dateDebutTraitement, String dateFinTraitement,
            BigDecimal montantMensuel) throws Exception {

        if (JadeStringUtil.isBlankOrZero(dateFinTraitement)) {

            dateFinTraitement = "31.12." + dateDebutTraitement.substring(6);

        } else {

            String anneeAdaptation = String.valueOf(JACalendar.today().getYear() + 1);

            // Si la date de fin concerne l'année de l'adaptation, on choisi la date de fin de traitement
            if ((dateFinTraitement.length() == 7 || dateFinTraitement.length() == 10)
                    && dateFinTraitement.substring(dateFinTraitement.length() - 4).equals(anneeAdaptation)) {

                if (dateFinTraitement.length() == 10) {

                    dateFinTraitement = getDateFinDeMois_JJMMAAAA(dateFinTraitement.substring(3,
                            dateFinTraitement.length()));

                } else if (dateFinTraitement.length() == 7) {

                    dateFinTraitement = getDateFinDeMois_JJMMAAAA(dateFinTraitement);

                } else {
                    throw new Exception(
                            "RFProcessAdaptationAnuelleRegimeHandlerEntity.calculMontantFacture(): format date de fin de régime incohérant");
                }

            } else {
                dateFinTraitement = "31.12." + dateDebutTraitement.substring(6);
            }

        }

        BigDecimal nbDeMoisBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(dateDebutTraitement,
                dateFinTraitement));

        return montantMensuel.multiply(nbDeMoisBigDec);
    }

    private BigDecimal calculMontantMensuelInitial(String dateDebutTraitement, String dateFinTraitement,
            String montantInitial) throws Exception {

        // On détermine le montant mensuel initial de la demande. Comme un régime peut être relancé sur plusieurs
        // années, on s'assure de calculer son montant mensuel initial pour une année car le montant accepté d'une
        // demande est valable pour une année.
        String dateDebutRegime = "";
        String anneeCourante = String.valueOf(JACalendar.today().getYear());
        if (dateDebutTraitement.length() == 10) {
            dateDebutRegime = "01." + dateDebutTraitement.substring(3, 5) + "." + anneeCourante;
        } else if (dateDebutTraitement.length() == 7) {
            dateDebutRegime = "01." + dateDebutTraitement.substring(0, 2) + "." + anneeCourante;
        } else {
            throw new Exception(
                    "RFProcessAdaptationAnuelleRegimeHandlerEntity.calculMontantMensuelInitial(): format date de début de régime incohérant");
        }

        String dateFinRegime = "";
        if (JadeStringUtil.isBlankOrZero(dateFinTraitement)) {

            dateFinRegime = "31.12." + dateDebutRegime.substring(6);

        } else {

            // Si la date de fin concerne l'année courante, c'est impossible
            if ((dateFinTraitement.length() == 7 || dateFinTraitement.length() == 10)
                    && dateFinTraitement.substring(dateFinTraitement.length() - 4).equals(anneeCourante)) {

                throw new Exception(
                        "RFProcessAdaptationAnuelleRegimeHandlerEntity.calculMontantMensuelInitial(): La date de fin de régime concerne l'année courante");

            } else {// Sinon on choisi la fin de l'année courante

                dateFinRegime = "31.12." + dateDebutRegime.substring(6);

            }
        }

        BigDecimal nbDeMoisBigDec = new BigDecimal(JadeDateUtil.getNbMonthsBetween(dateDebutRegime, dateFinRegime));
        BigDecimal montantInitialBigDec = new BigDecimal(montantInitial.replace("'", ""));

        BigDecimal montantMensuelBigDec = montantInitialBigDec.divide(nbDeMoisBigDec, 2, RoundingMode.HALF_UP);

        return new BigDecimal(Math.ceil(montantMensuelBigDec.doubleValue() * 20) / 20)
                .setScale(2, RoundingMode.HALF_UP);

    }

    private void creationDemandeNonCalculeEnBd(Map<String, RFImputationDemandesData> demandesAimputerMap,
            RFPrestationAccordeeJointREPrestationAccordee rfPrestAccCourante,
            RFAdaptationJournaliereContext contextCourant) throws Exception {

        String idDemande = "";

        try {

            RFImputationDemandesData demData = (RFImputationDemandesData) demandesAimputerMap.values().toArray()[0];

            RFDemande rfDemande = new RFDemande();
            rfDemande.setSession(BSessionUtil.getSessionFromThreadContext());
            rfDemande.setDateDebutTraitement(demData.getDateDebutTraitement());
            rfDemande.setDateFacture(demData.getDateFacture());
            rfDemande.setDateFinTraitement(demData.getDateFinTraitement());
            rfDemande.setDateReception(demData.getDateDebutTraitement());
            rfDemande.setIdAdressePaiement(rfPrestAccCourante.getIdTiers());
            rfDemande.setIdDossier(RFUtils.getDossierJointPrDemande(rfPrestAccCourante.getIdTiers(),
                    BSessionUtil.getSessionFromThreadContext()).getIdDossier());
            rfDemande.setIdFournisseur("");
            rfDemande.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
            rfDemande.setIdSousTypeDeSoin(demData.getCsSousTypeDeSoin());
            rfDemande.setIsForcerPaiement(false);
            rfDemande.setIsContratDeTravail(false);
            rfDemande.setIsPP(false);

            // C'est la nouvelle demande -> on réutilise le calcul de la demande demData
            rfDemande.setMontantAPayer(demData.getMontantAccepte());
            rfDemande.setMontantFacture(demData.getMontantAPayerInitial());

            rfDemande.setNumeroFacture("");
            rfDemande.setMontantMensuel(demData.getMontantMensuel());
            rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
            rfDemande.setCsSource(IRFDemande.ADAPTATION);
            rfDemande.setCsStatut(IRFDemande.ACCEPTE);

            rfDemande.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

            idDemande = rfDemande.getIdDemande();
            demData.setIdDemande(idDemande);

        } catch (Exception e) {
            if (BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction() != null) {
                BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().setRollbackOnly();
            }
            throw e;
        } finally {
            // TODO: Vérifier si pas d'effet de bord
            if (BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction() != null) {
                try {
                    if (BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().hasErrors()
                            || BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction()
                                    .isRollbackOnly()) {

                        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().rollback();

                        RFUtils.ajouterLogAdaptation(
                                FWViewBeanInterface.ERROR,
                                contextCourant.getIdAdaptationJournaliere(),
                                contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(),
                                contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(),
                                "RFProcessAdaptationAnuelleRegimeHandlerEntity.creationDemandeNonCalculeEnBd(): impossible de créer la demande",
                                getLogsList());
                    } else {
                        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction().commit();

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING,
                                contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(), "Création de la demande de régime N° "
                                        + idDemande, getLogsList());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

    }

    private void diminuerDemandeExistante(String idDemande, RFAdaptationJournaliereContext contextCourant)
            throws Exception {

        RFDemande demandeADiminuer = new RFDemande();
        demandeADiminuer.setSession(BSessionUtil.getSessionFromThreadContext());
        demandeADiminuer.setIdDemande(idDemande);

        demandeADiminuer.retrieve();

        if (!demandeADiminuer.isNew()) {

            demandeADiminuer.setDateFinTraitement("31.12." + String.valueOf(JACalendar.today().getYear()));
            demandeADiminuer.update(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Diminution de la demande N° " + idDemande, getLogsList());

        } else {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Impossible de retrouver la demande N° " + idDemande, getLogsList());

            throw new Exception(
                    "RFProcessAdaptationAnuelleRegimeHandlerEntity.diminuerDemandeExistante(): Impossible de retrouver la demande N° "
                            + idDemande);
        }

    }

    private void diminuerPrestationAccordee(String idPrestationAccordee, RFAdaptationJournaliereContext contextCourant)
            throws Exception {

        String dateDiminution = "12." + String.valueOf(JACalendar.today().getYear());

        RFPrestationAccordee rfPrestationAcc = new RFPrestationAccordee();
        rfPrestationAcc.setSession(BSessionUtil.getSessionFromThreadContext());
        rfPrestationAcc.setIdRFMAccordee(idPrestationAccordee);

        rfPrestationAcc.retrieve();

        if (!rfPrestationAcc.isNew()) {

            rfPrestationAcc.setDateDiminution("01." + dateDiminution);
            rfPrestationAcc.setIsAdaptation(true);
            rfPrestationAcc.update(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

            REPrestationsAccordees rePrestationAccordee = new REPrestationsAccordees();
            rePrestationAccordee.setSession(BSessionUtil.getSessionFromThreadContext());
            rePrestationAccordee.setIdPrestationAccordee(idPrestationAccordee);

            rePrestationAccordee.retrieve();

            if (!rePrestationAccordee.isNew()) {

                rePrestationAccordee.setDateFinDroit(dateDiminution);
                rePrestationAccordee.update(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "Diminution de la la prestation accordée N°" + idPrestationAccordee, getLogsList());

            } else {
                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "Impossible de retrouver la prestation accordée RE N°" + idPrestationAccordee, getLogsList());

                throw new Exception(
                        "RFProcessAdaptationAnuelleRegimeHandlerEntity.diminuerPrestationAccordee(): impossible de retrouver la demande, mgr.size != 0");
            }
        } else {

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Impossible de retrouver la prestation accordée RFM N°" + idPrestationAccordee, getLogsList());

            throw new Exception(
                    "RFProcessAdaptationAnuelleRegimeHandlerEntity.diminuerPrestationAccordee(): impossible de retrouver la demande, mgr.size != 0");

        }
    }

    private String getDateFinDeMois_JJMMAAAA(String date_MMAAAA) {

        String dateFinDeTraitementJJMMAAAA = "01." + date_MMAAAA;
        dateFinDeTraitementJJMMAAAA = JadeDateUtil.addMonths(dateFinDeTraitementJJMMAAAA, 1);
        dateFinDeTraitementJJMMAAAA = JadeDateUtil.addDays(dateFinDeTraitementJJMMAAAA, -1);

        return dateFinDeTraitementJJMMAAAA;
    }

    private String getDescriptionMotifDeRefus(RFImputationDemandesData demande) throws Exception {

        Set<String> idsMotifRefusSystemSet = new HashSet<String>();
        StringBuffer descStrBuf = new StringBuffer();

        for (String[] motifCourant : demande.getMotifsDeRefus()) {
            idsMotifRefusSystemSet.add(motifCourant[0]);
        }

        RFMotifsDeRefusManager rfMotifRefusMgr = new RFMotifsDeRefusManager();
        rfMotifRefusMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfMotifRefusMgr.setForIdsMotifRefus(idsMotifRefusSystemSet);

        rfMotifRefusMgr.find();

        Iterator<RFMotifsDeRefus> rfMotifRefusItr = rfMotifRefusMgr.iterator();
        int i = 0;
        while (rfMotifRefusItr.hasNext()) {
            RFMotifsDeRefus motifCourant = rfMotifRefusItr.next();
            if (i == 0) {
                descStrBuf.append(motifCourant.getDescriptionFR());
            } else {
                descStrBuf.append(", " + motifCourant.getDescriptionFR());
            }
            i++;
        }

        return descStrBuf.toString();

    }

    public List<String[]> getLogsList() {
        return logsList;
    }

    private RFImputationDemandesData initDemandeACreer(RFPrestationAccordeeJointREPrestationAccordee rfPrestAccCourante)
            throws Exception {

        String dateDebutDroit = "01.01." + String.valueOf(JACalendar.today().getYear() + 1);

        RFImputationDemandesData demandeCourante = new RFImputationDemandesData();

        demandeCourante.setIdDemande(IDDEMANDEVIRTUELLE);
        demandeCourante.setIdDemandeParent("");
        demandeCourante.setIdGestionnaire(BSessionUtil.getSessionFromThreadContext().getUserId());
        demandeCourante.setCodeTypeDeSoin(IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE);

        if (rfPrestAccCourante.getCs_source().equals(IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME)) {
            demandeCourante.setCodeSousTypeDeSoin(IRFCodeTypesDeSoins.SOUS_TYPE_2_1_REGIME_ALIMENTAIRE);
            demandeCourante.setCsSousTypeDeSoin(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE);
        } else if (rfPrestAccCourante.getCs_source().equals(IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME_DIABETIQUE)) {
            demandeCourante.setCodeSousTypeDeSoin(IRFCodeTypesDeSoins.SOUS_TYPE_2_2_REGIME_DIABETIQUE);
            demandeCourante.setCsSousTypeDeSoin(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE_DIABETIQUE);
        }

        demandeCourante.setDateReception(dateDebutDroit);
        demandeCourante.setDateFacture(dateDebutDroit);
        demandeCourante.setIdTiers(rfPrestAccCourante.getIdTiers());
        demandeCourante.setIdDossier(RFUtils.getDossierJointPrDemande(rfPrestAccCourante.getIdTiers(),
                BSessionUtil.getSessionFromThreadContext()).getIdDossier());

        demandeCourante.setDateDebutTraitement(dateDebutDroit);
        demandeCourante.setDateFinTraitement(rfPrestAccCourante.getDateFinDroit());
        demandeCourante.setMontantMensuel(rfPrestAccCourante.getMontantPrestation());

        String montantInitialStr = calculMontantInitialSelonPrestationAccordee(dateDebutDroit,
                demandeCourante.getDateFinTraitement(), demandeCourante.getMontantMensuel()).toString();

        demandeCourante.setMontantAccepte(montantInitialStr);
        demandeCourante.setMontantAPayerInitial(montantInitialStr);

        demandeCourante.setIdFournisseur("");
        demandeCourante.setIdAdressePaiement(rfPrestAccCourante.getIdTiers());
        demandeCourante.setCsEtat(IRFDemande.ENREGISTRE);
        demandeCourante.setMontantFacture44("");
        // demandeCourante.setMontantFacture(rfTypDemJointDosJointTie.getMontantFacture().replace("'",
        // ""));
        demandeCourante.setMontantVerseOAI("");
        demandeCourante.setDateDemande(dateDebutDroit);
        demandeCourante.setCsSource(IRFDemande.SYSTEME);

        /*
         * if (!JadeStringUtil.isBlankOrZero(rfTypDemJointDosJointTie.getIdQdPrincipale())) {
         * demandeCourante.setIdQdPrincipale(rfTypDemJointDosJointTie.getIdQdPrincipale());
         * demandeCourante.setIsForcerImputation(true); idQdsImputationForcee.add(demandeCourante.getIdQdPrincipale());
         * }
         */

        demandeCourante.setForcerPaiement(false);
        demandeCourante.setPP(false);

        // Recherche si la demande concerne un paiement mensuel
        demandeCourante.setPaiementMensuel(true);
        // Recherche du type de paiement
        demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_FUTURE);

        return demandeCourante;
    }

    private RFImputationDemandesData initDemandeExistante(RFTypesDemandeJointDossierJointTiers rfDem,
            Map<String, Set<String>> forcePaiementGestsIdsDemandes) throws Exception {

        String dateDebutDroit = "01.01." + String.valueOf(JACalendar.today().getYear() + 1);

        RFImputationDemandesData demandeCourante = new RFImputationDemandesData();

        demandeCourante.setIdDemande(rfDem.getIdDemande());
        demandeCourante.setIdDemandeParent(rfDem.getIdDemandeParent());
        demandeCourante.setIdGestionnaire(rfDem.getIdGestionnaire());
        demandeCourante.setCodeTypeDeSoin(rfDem.getCodeTypeDeSoin());
        demandeCourante.setCodeSousTypeDeSoin(rfDem.getCodeSousTypeDeSoin());
        demandeCourante.setCsSousTypeDeSoin(rfDem.getIdSousTypeDeSoin());

        demandeCourante.setMontantAccepteAvantAdaptation(rfDem.getMontantAPayer());
        demandeCourante.setMontantMensuelAccepteAvantAdaptation(rfDem.getMontantMensuel());

        demandeCourante.setDateReception(rfDem.getDateReception());
        demandeCourante.setDateFacture(rfDem.getDateFacture());
        demandeCourante.setDateDeces(rfDem.getDateDeces());
        demandeCourante.setIdTiers(rfDem.getIdTiers());
        demandeCourante.setIdDossier(rfDem.getIdDossier());
        demandeCourante.setDateDebutTraitement(dateDebutDroit);
        demandeCourante.setDateFinTraitement(rfDem.getDateFinTraitement());
        demandeCourante.setIdFournisseur(rfDem.getIdFournisseur());
        demandeCourante.setIdAdressePaiement(rfDem.getIdAdressePaiement());
        demandeCourante.setCsEtat(rfDem.getCsEtat());
        demandeCourante.setMontantFacture44(rfDem.getMontantFacture44().replace("'", ""));
        // demandeCourante.setMontantFacture(rfTypDemJointDosJointTie.getMontantFacture().replace("'",
        // ""));
        demandeCourante.setMontantVerseOAI(rfDem.getMontantVerseOAI().replace("'", ""));

        demandeCourante.setDateDemande(dateDebutDroit);

        BigDecimal montantMensuelInitialBigDec = calculMontantMensuelInitial(rfDem.getDateDebutTraitement(),
                rfDem.getDateFinTraitement(), rfDem.getMontantFacture());

        demandeCourante.setMontantMensuel(montantMensuelInitialBigDec.toString());

        BigDecimal montantFacture = calculMontantFacture(dateDebutDroit, demandeCourante.getDateFinTraitement(),
                montantMensuelInitialBigDec);

        demandeCourante.setMontantAccepte(montantFacture.toString());
        demandeCourante.setMontantAPayerInitial(montantFacture.toString());

        demandeCourante.setCsSource(rfDem.getCsSource());

        /*
         * if (!JadeStringUtil.isBlankOrZero(rfTypDemJointDosJointTie.getIdQdPrincipale())) {
         * demandeCourante.setIdQdPrincipale(rfTypDemJointDosJointTie.getIdQdPrincipale());
         * demandeCourante.setIsForcerImputation(true); idQdsImputationForcee.add(demandeCourante.getIdQdPrincipale());
         * }
         */

        demandeCourante.setForcerPaiement(rfDem.getIsForcerPaiement().booleanValue());

        if (rfDem.getIsForcerPaiement().booleanValue()) {
            if (forcePaiementGestsIdsDemandes.containsKey(demandeCourante.getIdGestionnaire())) {
                Set<String> idsDemande = forcePaiementGestsIdsDemandes.get(demandeCourante.getIdGestionnaire());
                idsDemande.add(demandeCourante.getIdDemande());
            } else {
                Set<String> idsDemande = new HashSet<String>();
                idsDemande.add(demandeCourante.getIdDemande());
                forcePaiementGestsIdsDemandes.put(demandeCourante.getIdGestionnaire(), idsDemande);
            }
        }

        demandeCourante.setPP(rfDem.getIsPP().booleanValue());

        // Recherche si la demande concerne un paiement mensuel
        demandeCourante.setPaiementMensuel(true);

        // Recherche du type de paiement
        demandeCourante.setTypeDePaiment(IRFTypePaiement.PAIEMENT_FUTURE);

        return demandeCourante;
    }

    private void majEntitesDemandeExistante(RFAdaptationJournaliereContext contextCourant,
            Set<RFDecisionData> decisions, RFPrestationAccordeeJointREPrestationAccordee rfPrestAccCourante,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            HashMap<String, Set<String[]>> idQdIdsDossierMap) throws Exception {

        RFImputationDemandesData demCourante = (RFImputationDemandesData) demandesAimputerMap.values().toArray()[0];

        // Si la demande est refusée
        if (JadeStringUtil.isBlankOrZero(((RFDecisionData) decisions.toArray()[0]).getMontantTotalAPayer())) {

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(), "Régime refusé: "
                            + getDescriptionMotifDeRefus((RFImputationDemandesData) demandesAimputerMap.values()
                                    .toArray()[0]), getLogsList());

            // On diminue la prestation accordée
            diminuerPrestationAccordee(rfPrestAccCourante.getIdRFMAccordee(), contextCourant);

            // On diminue la demande
            diminuerDemandeExistante(demCourante.getIdDemande(), contextCourant);

        }// Et que la demande est acceptée ou partiellement acceptée
        else {

            // Si la demande calculé n'a pas changée, on laisse la RFMAccordée courir
            if (demCourante.getMontantMensuelAccepteAvantAdaptation().equals(demCourante.getMontantMensuel())) {

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "La demande n'a pas changée, la RFM accordée N° " + rfPrestAccCourante.getIdRFMAccordee()
                                + " n'a pas été modifiée", getLogsList());

                // Persistance des maps demandes et qds mais on ne modifie pas la décision
                RFPersistanceImputationDemandesDecisionsService rfPersistanceImputationDemandesDecisionsService = new RFPersistanceImputationDemandesDecisionsService(
                        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(),
                        BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                                .getUserId(), demandesAimputerMap, qdsAimputerMap, decisions, idQdIdsDossierMap, true);

                rfPersistanceImputationDemandesDecisionsService.majImputation(false);

            }// Sinon on diminue la RFMAccordée et la demande existante et on créé la nouvelle demande et décision
            else {
                diminuerPrestationAccordee(rfPrestAccCourante.getIdRFMAccordee(), contextCourant);

                // On diminue la demande existante
                diminuerDemandeExistante(demCourante.getIdDemande(), contextCourant);

                // On crée la nouvelle demande en Bd dans l'état enregistré (permet de ne pas modifier
                // RFPersistanceImputationDemandesDecisionsService)
                creationDemandeNonCalculeEnBd(demandesAimputerMap, rfPrestAccCourante, contextCourant);

                // Persistance des maps demandes, qds et décisions
                RFPersistanceImputationDemandesDecisionsService rfPersistanceImputationDemandesDecisionsService = new RFPersistanceImputationDemandesDecisionsService(
                        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(),
                        BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                                .getUserId(), demandesAimputerMap, qdsAimputerMap, decisions, idQdIdsDossierMap, true);

                rfPersistanceImputationDemandesDecisionsService.majImputation(true);

                RFGenererPaiementService rfGenererPaiementService = new RFGenererPaiementService(BSessionUtil
                        .getSessionFromThreadContext().getCurrentThreadTransaction(),
                        BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                                .getUserId(), decisions, demandesAimputerMap, qdsAimputerMap, "", idQdIdsDossierMap,
                        true, rfPrestAccCourante.getIdRFMAccordee(), false, null);

                String[] idRfmAccordeeInfoCompta = rfGenererPaiementService.genererPaiement();

                REInformationsComptabilite ic2 = new REInformationsComptabilite();
                ic2.setIdInfoCompta(idRfmAccordeeInfoCompta[1]);
                ic2.setSession(BSessionUtil.getSessionFromThreadContext());
                ic2.retrieve(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

                if (JadeStringUtil.isBlankOrZero(ic2.getIdCompteAnnexe())) {
                    REInfoCompta.initCompteAnnexe_noCommit(BSessionUtil.getSessionFromThreadContext(), BSessionUtil
                            .getSessionFromThreadContext().getCurrentThreadTransaction(), contextCourant
                            .getIdTiersBeneficiaire(), ic2, IREValidationLevel.VALIDATION_LEVEL_ALL);
                }

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "Création de la prestation accordée N° " + idRfmAccordeeInfoCompta[0], getLogsList());
            }

        }

    }

    private void majEntitesDemandeNonCree(RFAdaptationJournaliereContext contextCourant, Set<RFDecisionData> decisions,
            RFPrestationAccordeeJointREPrestationAccordee rfPrestAccCourante,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            HashMap<String, Set<String[]>> idQdIdsDossierMap) throws Exception {

        // Si la demande est refusée
        if (JadeStringUtil.isBlankOrZero(((RFDecisionData) decisions.toArray()[0]).getMontantTotalAPayer())) {

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(), "Régime refusé: "
                            + getDescriptionMotifDeRefus((RFImputationDemandesData) demandesAimputerMap.values()
                                    .toArray()[0]), getLogsList());

            // On diminue la prestation accordéé
            diminuerPrestationAccordee(rfPrestAccCourante.getIdRFMAccordee(), contextCourant);

        }// Si la demande est acceptée ou partiellement acceptée
        else {

            // Création de la demande en Bd dans l'état enregistré (permet de ne pas modifier
            // RFPersistanceImputationDemandesDecisionsService)
            creationDemandeNonCalculeEnBd(demandesAimputerMap, rfPrestAccCourante, contextCourant);

            // Persistance des maps demandes, qds et décisions
            RFPersistanceImputationDemandesDecisionsService rfPersistanceImputationDemandesDecisionsService = new RFPersistanceImputationDemandesDecisionsService(
                    BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(),
                    BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext().getUserId(),
                    demandesAimputerMap, qdsAimputerMap, decisions, idQdIdsDossierMap, true);

            String idDecision = rfPersistanceImputationDemandesDecisionsService.majImputation(true);

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Création de la décision N° " + idDecision, getLogsList());

            RFImputationDemandesData demCourante = (RFImputationDemandesData) demandesAimputerMap.values().toArray()[0];

            // Si la demande calculé n'a pas changée, on laisse courir la RFMAccordée
            if (demCourante.getMontantAPayerInitial().equals(demCourante.getMontantAccepte())) {

                // Màj du numéro de décision de la prestation accordée
                majIdDecisionRfAccordee(idDecision, rfPrestAccCourante.getIdRFMAccordee());

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "La demande n'a pas changée, la RFM accordée N° " + rfPrestAccCourante.getIdRFMAccordee()
                                + " n'a pas été modifiée", getLogsList());

            }// Sinon on diminue la RFMAccordée existante et on créé la nouvelle
            else {

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "Régime partiellement accepté: "
                                + getDescriptionMotifDeRefus((RFImputationDemandesData) demandesAimputerMap.values()
                                        .toArray()[0]), getLogsList());

                diminuerPrestationAccordee(rfPrestAccCourante.getIdRFMAccordee(), contextCourant);

                RFGenererPaiementService rfGenererPaiementService = new RFGenererPaiementService(BSessionUtil
                        .getSessionFromThreadContext().getCurrentThreadTransaction(),
                        BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                                .getUserId(), decisions, demandesAimputerMap, qdsAimputerMap, "", idQdIdsDossierMap,
                        true, rfPrestAccCourante.getIdRFMAccordee(), false, null);

                String[] idRfmAccordeeInfoCompta = rfGenererPaiementService.genererPaiement();

                REInformationsComptabilite ic2 = new REInformationsComptabilite();
                ic2.setIdInfoCompta(idRfmAccordeeInfoCompta[1]);
                ic2.setSession(BSessionUtil.getSessionFromThreadContext());
                ic2.retrieve(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

                if (JadeStringUtil.isBlankOrZero(ic2.getIdCompteAnnexe())) {
                    REInfoCompta.initCompteAnnexe_noCommit(BSessionUtil.getSessionFromThreadContext(), BSessionUtil
                            .getSessionFromThreadContext().getCurrentThreadTransaction(), contextCourant
                            .getIdTiersBeneficiaire(), ic2, IREValidationLevel.VALIDATION_LEVEL_ALL);
                }

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "Création de la prestation accordée N° " + idRfmAccordeeInfoCompta[0], getLogsList());
            }
        }

    }

    private void majIdDecisionRfAccordee(String idDecision, String idRfmAccordee) throws Exception {

        RFPrestationAccordee rfPrestationAcc = new RFPrestationAccordee();
        rfPrestationAcc.setSession(BSessionUtil.getSessionFromThreadContext());
        rfPrestationAcc.setIdRFMAccordee(idRfmAccordee);

        rfPrestationAcc.retrieve();

        if (!rfPrestationAcc.isNew()) {

            rfPrestationAcc.setIdDecision(idDecision);
            rfPrestationAcc.update(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        }
    }

    private void majIdsQdPrincipaleDemandeCourante(Map<String, RFImputationDemandesData> demandesAImputerMap,
            RFAdaptationJournaliereContext contextCourant) throws Exception {

        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
        rfQdJointPerValJointDosJointTieJointDemMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForNotCsEtatQd(IRFQd.CS_ETAT_QD_CLOTURE);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(pcVOs.getIdTiersBeneficiaire());

        RFImputationDemandesData demandeCourante = (RFImputationDemandesData) demandesAImputerMap.values().toArray()[0];

        if (JadeStringUtil.isBlankOrZero(demandeCourante.getDateDebutTraitement())) {

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAA(demandeCourante.getDateFacture()));

            rfQdJointPerValJointDosJointTieJointDemMgr.setForDateDebutBetweenPeriode(demandeCourante.getDateFacture());
        } else {

            if (demandeCourante.getDateDebutTraitement().length() == 7) {
                demandeCourante.setDateDebutTraitement("01." + demandeCourante.getDateDebutTraitement());
            }

            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAA(demandeCourante.getDateDebutTraitement()));

            rfQdJointPerValJointDosJointTieJointDemMgr.setForDateDebutBetweenPeriode(demandeCourante
                    .getDateDebutTraitement());
        }
        rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
        rfQdJointPerValJointDosJointTieJointDemMgr.find(BSessionUtil.getSessionFromThreadContext()
                .getCurrentThreadTransaction());

        if (rfQdJointPerValJointDosJointTieJointDemMgr.size() == 0) {
            demandeCourante.setIdQdPrincipale("");
        } else if (rfQdJointPerValJointDosJointTieJointDemMgr.size() == 1) {
            demandeCourante
                    .setIdQdPrincipale(((RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande) rfQdJointPerValJointDosJointTieJointDemMgr
                            .getFirstEntity()).getIdQdPrincipale());
        } else {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Plusieurs grandes Qds trouvées pour la même date", getLogsList());

            throw new Exception("RFProcessAdaptationAnuelleRegimeHandlerEntitymajIdsQdPrincipaleDemandeCourante(): ");
        }

    }

    private RFImputationDemandesData rechercheDemandeLieeRfmAccordee(RFAdaptationJournaliereContext contextCourant,
            RFPrestationAccordeeJointREPrestationAccordee rfPrestAccCourante,
            Map<String, Set<String>> forcePaiementGestsIdsDemandes) throws Exception {

        RFTypesDemandeJointDossierJointTiersManager rfDemMgr = new RFTypesDemandeJointDossierJointTiersManager();
        rfDemMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfDemMgr.setForIdDecision(rfPrestAccCourante.getIdDecision());
        rfDemMgr.changeManagerSize(0);
        rfDemMgr.find();

        if (rfDemMgr.size() == 1) {

            RFTypesDemandeJointDossierJointTiers rfDem = (RFTypesDemandeJointDossierJointTiers) rfDemMgr
                    .getFirstEntity();

            // Recherche si la demande concerne un paiement mensuel
            if (null != rfDem) {

                if (IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE.equals(rfDem.getCodeTypeDeSoin())) {

                    RFImputationDemandesData demandeCourante = initDemandeExistante(rfDem,
                            forcePaiementGestsIdsDemandes);

                    return demandeCourante;

                } else {
                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR,
                            contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                            contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                            contextCourant.getNumeroDecisionPc(), "La demande ne concerne pas un régime", getLogsList());

                    throw new Exception(
                            "RFProcessAdaptationAnuelleRegimeHandlerEntity.rechercheDemandeLieeRfmAccordee(): La demande ne concerne pas un régime");

                }
            } else {
                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                        "impossible de retrouver la demande", getLogsList());

                throw new Exception(
                        "RFProcessAdaptationAnuelleRegimeHandlerEntity.rechercheDemandeLieeRfmAccordee(): impossible de retrouver la demande");
            }
        } else {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "impossible de retrouver la demande, mgr.size != 1", getLogsList());

            throw new Exception(
                    "RFProcessAdaptationAnuelleRegimeHandlerEntity.rechercheDemandeLieeRfmAccordee(): impossible de retrouver la demande, mgr.size != 1");
        }
    }

    private void calculerDemande(RFAdaptationJournaliereContext contextCourant,
            Map<String, RFImputationDemandesData> demandesAimputerMap, boolean hasDecisionDemande,
            RFPrestationAccordeeJointREPrestationAccordee rfPrestAccCourante, FWMemoryLog memoryLog) throws Exception {

        try {

            Map<String, RFImputationQdsData> qdsAimputerMap = new HashMap<String, RFImputationQdsData>();

            RFGenererDecisionsEntitesService rfGenererDecisionsEntitesService = new RFGenererDecisionsEntitesService(
                    BSessionUtil.getSessionFromThreadContext().getUserId(), demandesAimputerMap, "",
                    BSessionUtil.getSessionFromThreadContext(), BSessionUtil.getSessionFromThreadContext()
                            .getCurrentThreadTransaction());

            Object[] genererDecisionsEntiteObj = rfGenererDecisionsEntitesService.genererDecisionsEntite();

            HashMap<String, Set<String[]>> idQdIdsDossierMap = (HashMap<String, Set<String[]>>) genererDecisionsEntiteObj[1];
            Set<RFDecisionData> decisions = (Set<RFDecisionData>) genererDecisionsEntiteObj[0];

            majIdsQdPrincipaleDemandeCourante(demandesAimputerMap, contextCourant);

            // Calcul
            RFImputationDemandesService rfImputationDemandesService = new RFImputationDemandesService(BSessionUtil
                    .getSessionFromThreadContext().getCurrentThreadTransaction(),
                    BSessionUtil.getSessionFromThreadContext(), demandesAimputerMap, decisions, qdsAimputerMap,
                    memoryLog, true);

            rfImputationDemandesService.imputerDemandes();

            // Si la décision et la demande n'existent pas
            if (!hasDecisionDemande) {

                majEntitesDemandeNonCree(contextCourant, decisions, rfPrestAccCourante, demandesAimputerMap,
                        qdsAimputerMap, idQdIdsDossierMap);

            }// Si la décision et la demande existent
            else {

                majEntitesDemandeExistante(contextCourant, decisions, rfPrestAccCourante, demandesAimputerMap,
                        qdsAimputerMap, idQdIdsDossierMap);

            }

        } catch (Exception e) {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(), e.getMessage(),
                    getLogsList());

            throw new Exception("RFProcessAdaptationAnuelleRegimeHandlerEntity.calculerDemande():" + e.getMessage());
        }

    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        try {

            RFAdaptationJournaliereContext contextCourant = RFUtils.initContextAdaptation(pcVOs, BSessionUtil
                    .getSessionFromThreadContext().getUserId());

            FWMemoryLog memoryLog = new FWMemoryLog();

            // Recherche si le bénéficiaire possède une RFAccordée
            RFPrestationAccordeeJointREPrestationAccordeeManager rfPreAccJoiRePreAccMgr = new RFPrestationAccordeeJointREPrestationAccordeeManager();
            rfPreAccJoiRePreAccMgr.setSession(BSessionUtil.getSessionFromThreadContext());
            rfPreAccJoiRePreAccMgr.setForIdTiersBeneficiaire(pcVOs.getIdTiersBeneficiaire());
            rfPreAccJoiRePreAccMgr.setForEnCoursAtMois("01." + String.valueOf(JACalendar.today().getYear() + 1));
            rfPreAccJoiRePreAccMgr
                    .setForCsSourceRfmAccordee(new String[] { IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME,
                            IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME_DIABETIQUE });
            rfPreAccJoiRePreAccMgr.changeManagerSize(0);
            rfPreAccJoiRePreAccMgr.find();

            Iterator<RFPrestationAccordeeJointREPrestationAccordee> rfPreAccJoiRePreAccItr = rfPreAccJoiRePreAccMgr
                    .iterator();

            while (rfPreAccJoiRePreAccItr.hasNext()) {

                RFPrestationAccordeeJointREPrestationAccordee rfPrestAccCourante = rfPreAccJoiRePreAccItr.next();

                // On contrôle si les conditions du bénéficiaire n'ont pas changées

                // Initilalisation de la map contenant les demandes à imputer
                Map<String, RFImputationDemandesData> demandesAimputerMap = new LinkedHashMap<String, RFImputationDemandesData>();
                // Initilalisation de la map contenant les Qds à imputer
                // Initilalisation de la map contenant les Qds dont on a forcé l'imputation
                // Set<String> idQdsImputationForcee = new HashSet<String>();
                // Initilalisation de la map contenant les ids des Qd principales concernées par le calcul
                // Set<String> idQdPrincipaleSet = new HashSet<String>();
                // Initialisation d'une Map<idGest,tableau de numéros de demandes> contenant les utilisateurs ayant
                // forcé le paiement
                Map<String, Set<String>> forcePaiementGestsIdsDemandes = new HashMap<String, Set<String>>();

                boolean hasDecisionDemande = false;
                RFImputationDemandesData demandeCourante = null;

                // Recherche de la demande liée à la RFMAccordée
                if (!JadeStringUtil.isBlankOrZero(rfPrestAccCourante.getIdDecision())) {

                    hasDecisionDemande = true;
                    demandeCourante = rechercheDemandeLieeRfmAccordee(contextCourant, rfPrestAccCourante,
                            forcePaiementGestsIdsDemandes);

                    demandesAimputerMap.put(demandeCourante.getIdDemande(), demandeCourante);

                }// Si pas de décisions ni de demandes on initialise la demande
                else {
                    demandeCourante = initDemandeACreer(rfPrestAccCourante);
                    demandesAimputerMap.put(demandeCourante.getIdDemande(), demandeCourante);
                }

                calculerDemande(contextCourant, demandesAimputerMap, hasDecisionDemande, rfPrestAccCourante, memoryLog);

            }// Si pas de RFAccordée, on fait rien
        } catch (Exception e) {
            throw new JadePersistenceException(e.getMessage().toString());
        }

    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        Gson gson = new Gson();
        pcVOs = gson.fromJson(entity.getValue1(), DecisionPcVO.class);
    }

    public void setLogsList(List<String[]> logsList) {
        this.logsList = logsList;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

}
