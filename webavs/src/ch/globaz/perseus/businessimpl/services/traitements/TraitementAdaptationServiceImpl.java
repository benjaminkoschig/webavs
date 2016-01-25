/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.traitements;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.parametres.LienLocalite;
import ch.globaz.perseus.business.models.parametres.LienLocaliteSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.traitements.TraitementAdaptationService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author dde
 * 
 */
public class TraitementAdaptationServiceImpl extends PerseusAbstractServiceImpl implements TraitementAdaptationService {

    private Map<String, Decision> anciennesDecisions = new HashMap<String, Decision>();
    private StringBuilder casAvecRetenu = null;
    private StringBuilder casNonCalculable = null;
    private int demandeSansDateDeFin = 0;
    private int demandesFermees = 0;

    private Collection<String> dossiersOuverts = new ArrayList<String>();

    private StringBuilder listCasPartielRi = null;

    private List<Decision> listDecisionsReturn = new ArrayList<Decision>();

    private StringBuilder listeCasLocaliteHorsVaud = null;

    private ArrayList<String> listeLocalite = null;

    private int nbDecisionPartielEnOcroi = 0;

    private int nbDecisionRestePartielMaisChangementExcedent = 0;

    private List<String> nouvellesDemandes = new ArrayList<String>();

    private PCFAccordee calculerNouvelleDemande(Demande nouvelleDemande) throws Exception {
        // On fait le calcul
        return PerseusServiceLocator.getPCFAccordeeService().calculer(nouvelleDemande.getId());
    }

    private boolean checkConditionsToTraitementDemandeDecembre(Decision decision) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return isDateFinDecisionDecembre(decision);
    }

    private boolean checkConditionsToTraitementDemandeEnCours(Decision decision) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean isOkForTraitement = false;
        if (isDecisionOctroiCompletOrPartiel(decision) && isDemandeNoRenouvelle(decision) && hasNotDateFin(decision)) {
            isOkForTraitement = true;
        }
        return isOkForTraitement;
    }

    private Demande copierDemande(Decision decision) throws Exception {
        Demande demandeCopie = PerseusServiceLocator.getDemandeService().copier(decision.getDemande());
        nouvellesDemandes.add(demandeCopie.getId());
        anciennesDecisions.put(demandeCopie.getId(), decision);
        return demandeCopie;
    }

    private Decision createDecisionForNewDemande(Demande nouvelleDemande, Decision oldDecision, PCFAccordee pcfa,
            String texteDecision) throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DecisionException {
        Decision decision = new Decision();
        decision.setDemande(nouvelleDemande);
        // Service devrait faire ca mais bon je le fais
        decision.getSimpleDecision().setIdDemande(nouvelleDemande.getId());
        decision.setListCopies(oldDecision.getListCopies());
        if (Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant()) > 0) {
            decision.getSimpleDecision().setCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        } else {
            decision.getSimpleDecision().setCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
        }
        decision.getSimpleDecision().setDateDocument(JadeDateUtil.getGlobazFormattedDate(new Date()));
        decision.getSimpleDecision().setCsEtat(CSEtatDecision.PRE_VALIDE.getCodeSystem());
        decision.getSimpleDecision().setDatePreparation(nouvelleDemande.getSimpleDemande().getDateDebut());
        decision.getSimpleDecision().setUtilisateurPreparation(
                oldDecision.getSimpleDecision().getUtilisateurPreparation());
        decision.getSimpleDecision().setIdDomaineApplicatifAdresseCourrier(
                oldDecision.getSimpleDecision().getIdDomaineApplicatifAdresseCourrier());
        decision.getSimpleDecision().setIdDomaineApplicatifAdressePaiement(
                oldDecision.getSimpleDecision().getIdDomaineApplicatifAdressePaiement());
        decision.getSimpleDecision().setIdTiersAdresseCourrier(
                oldDecision.getSimpleDecision().getIdTiersAdresseCourrier());
        decision.getSimpleDecision().setIdTiersAdressePaiement(
                oldDecision.getSimpleDecision().getIdTiersAdressePaiement());
        decision.getSimpleDecision().setMontantToucheAuRI(oldDecision.getSimpleDecision().getMontantToucheAuRI());
        decision.getSimpleDecision().setNumeroDecision(
                PerseusServiceLocator.getDecisionService().getNumeroDemandeCalculee(
                        nouvelleDemande.getSimpleDemande().getDateDebut().substring(6)));
        decision.getSimpleDecision().setRemarquesGenerales(oldDecision.getSimpleDecision().getRemarquesGenerales());
        decision.getSimpleDecision().setRemarqueUtilisateur(texteDecision);

        return PerseusServiceLocator.getDecisionService().create(decision);
    }

    private String determinerTypeNouvelleDemande(Decision oldDecision) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Si la date de fin de la décision est le 31.12 de l'année précédent et de type partiel le
        // traitement, alors
        // type periodique
        String dateFinPartiel = JadeDateUtil.addYears("31.12." + getMoisActuel().substring(3), -1);
        if (dateFinPartiel.equals(oldDecision.getDemande().getSimpleDemande().getDateFin())
                && CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                        oldDecision.getSimpleDecision().getCsTypeDecision())) {
            return CSTypeDemande.REVISION_PERIODIQUE.getCodeSystem();
        } else {
            return CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem();
        }
    }

    private void doTraitementDemandeEnCours(Decision decision, String mois) throws Exception {
        if (isDecisionOctroiCompletOrPartielSansRi(decision)) {
            if (isLocaliteDansCantonVaud(decision.getDemande(), mois)) {
                String dateDebutDemande = decision.getDemande().getSimpleDemande().getDateDebut();
                decision.getDemande().getSimpleDemande().setDateDebut("01." + getMoisActuel());
                if (PerseusServiceLocator.getDemandeService().checkCalculable(decision.getDemande())) {
                    decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
                    Demande demandeCopie = copierDemande(decision);
                    listerCasSiRetenu(decision, demandeCopie);
                } else {
                    listInfoCas(casNonCalculable, decision.getDemande(), false, "", "");
                }

                decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
            }
            fermerAncienneDemande(decision);
        } else {
            listInfoCas(listCasPartielRi, decision.getDemande(), false, "", decision.getDemande().getSimpleDemande()
                    .getIdDemande());
        }
    }

    private void doTraitementDemandeWithDateFinDecembre(Decision decision, String mois) throws Exception {
        if (!decision.getDemande().getSimpleDemande().getFromRI()
                && CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                        decision.getSimpleDecision().getCsTypeDecision())
                && !dossiersOuverts.contains(decision.getDemande().getDossier().getId())) {
            String dateDebutDemande = decision.getDemande().getSimpleDemande().getDateDebut();
            decision.getDemande().getSimpleDemande().setDateDebut("01." + getMoisActuel());
            if (PerseusServiceLocator.getDemandeService().checkCalculable(decision.getDemande())) {
                decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
                if (isLocaliteDansCantonVaud(decision.getDemande(), mois)) {
                    copierDemande(decision);
                }
                dossiersOuverts.add(decision.getDemande().getDossier().getDossier().getIdDossier());
            } else {
                listInfoCas(casNonCalculable, decision.getDemande(), false, "", "");
            }
            decision.getDemande().getSimpleDemande().setDateDebut(dateDebutDemande);
        }
    }

    private void doTraitementReouvertureDemande(Demande demande, String texteDecision, BSession session)
            throws Exception {
        Decision oldDecision = anciennesDecisions.get(demande.getId());
        demande = updateDateEtTypeNouvelleDemande(demande, oldDecision);
        PCFAccordee pcfa = calculerNouvelleDemande(demande);
        demande = pcfa.getDemande();
        PCFAccordee oldPCFA = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                oldDecision.getDemande().getId());
        pcfa = repriseMesureCoaching(oldPCFA, pcfa);
        Decision decision = createDecisionForNewDemande(demande, oldDecision, pcfa, texteDecision);
        decision = PerseusServiceLocator.getDecisionService().valider(decision, session.getUserId());
        putDecisionInListIfImpression(oldDecision, decision, oldPCFA, pcfa);
    }

    @Override
    public List<Decision> executerTraitements(BSession session, JadeBusinessLogSession logSession, String mois,
            String texteDecision) throws PerseusException, JadePersistenceException, Exception {

        try {
            if (isValidationDecisionNoAuthorise(session) && isMoisTraitementEnCours(mois)) {

                DecisionSearchModel decisionSearchModel = loadDecisionValidate();
                initialiseStringBuilderAndList(mois);

                // Traitement des demandes en cours
                for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
                    Decision decision = (Decision) model;

                    if (checkConditionsToTraitementDemandeEnCours(decision)) {
                        dossiersOuverts.add(decision.getDemande().getDossier().getId());
                        doTraitementDemandeEnCours(decision, mois);
                    }

                    logSiError(decision.getDemande());
                }

                // Traitement des demandes fermées en décembre de l'année précédente
                for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
                    Decision decision = (Decision) model;

                    if (checkConditionsToTraitementDemandeDecembre(decision)) {
                        doTraitementDemandeWithDateFinDecembre(decision, mois);
                    }

                    logSiError(decision.getDemande());
                }

                PerseusServiceLocator.getPmtMensuelService().activerValidationDecision();

                // Traitement de réouverture des demandes
                for (String idDemande : nouvellesDemandes) {
                    Demande demande = PerseusServiceLocator.getDemandeService().read(idDemande);

                    if (isLocaliteDansCantonVaud(demande, mois)) {
                        doTraitementReouvertureDemande(demande, texteDecision, session);

                        logSiError(demande);
                    }

                }

                renseignerInfoMailSuiteTraitements(logSession);

            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PerseusException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_SERVICE_NON_DISPONIBLE")
                    + e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new PerseusException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_JADEAPPLICATIONEXCEPTION")
                    + e.toString(), e);
        }
        return listDecisionsReturn;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.traitements.TraitementAnnuelService#executerTraitementsAnnuels(globaz.globall
     * .db.BSession, globaz.jade.log.business.JadeBusinessLogSession)
     */
    private void fermerAncienneDemande(Decision decision) throws Exception {
        // Fermeture des demandes le dernier jour du mois précédent le lancement du traitement
        String dateFin = "01." + getMoisActuel();
        dateFin = JadeDateUtil.addDays(dateFin, -1);
        decision.getDemande().getSimpleDemande().setDateFin(dateFin);
        Demande demande = PerseusServiceLocator.getDemandeService().update(decision.getDemande());
        demandesFermees++;
    }

    private String getIdLocalite(Demande demande, String mois) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail adresseTiersDetail = null;
        adresseTiersDetail = PFUserHelper.getAdresseAssure(demande.getDossier().getDemandePrestation()
                .getPersonneEtendue().getTiers().getIdTiers(), IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "01." + mois);

        if (adresseTiersDetail.getFields() == null) {
            adresseTiersDetail = PFUserHelper.getAdresseAssure(demande.getDossier().getDemandePrestation()
                    .getPersonneEtendue().getTiers().getIdTiers(), IConstantes.CS_AVOIR_ADRESSE_COURRIER, "01." + mois);
            if (adresseTiersDetail.getFields() == null) {
                return "";
            }
        }
        String idLocalite = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
        return idLocalite;
    }

    private ArrayList<String> getListLocalite(String mois) throws ParametresException, JadePersistenceException {
        // Recherche des localite
        ArrayList<String> listeLocalite = new ArrayList<String>();
        LienLocaliteSearchModel lienLocaliteSearchModel = new LienLocaliteSearchModel();
        lienLocaliteSearchModel.setForDateValable("01." + mois);
        lienLocaliteSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        try {
            lienLocaliteSearchModel = PerseusServiceLocator.getLienLocaliteService().search(lienLocaliteSearchModel);
            for (JadeAbstractModel model : lienLocaliteSearchModel.getSearchResults()) {
                LienLocalite lien = (LienLocalite) model;
                listeLocalite.add(lien.getSimpleLienLocalite().getIdLocalite());
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametresException("Service not available : " + e.getMessage());
        }

        return listeLocalite;
    }

    private String getMoisActuel() throws PaiementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        return PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

    }

    private boolean hasDifferenceBetweenOldEtNewRetenuImpotSource(PCFAccordee oldPCFA, PCFAccordee newPCFA)
            throws RetenueException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean hasDifferenceIS = false;
        if (oldPCFA.getDemande().getSimpleDemande().getPermisB()) {
            // Recuperation de la retenue IS pour l'ancienne situatione et la nouvelle
            SimpleRetenue simpleRetenueOld = loadRetenueIS(oldPCFA.getSimplePCFAccordee().getIdPCFAccordee());
            SimpleRetenue simpleRetenueNew = loadRetenueIS(newPCFA.getSimplePCFAccordee().getIdPCFAccordee());
            if ((null != simpleRetenueOld) && (null != simpleRetenueNew)
                    && !simpleRetenueNew.getMontantRetenuMensuel().equals(simpleRetenueOld.getMontantRetenuMensuel())) {
                hasDifferenceIS = true;
            } else if (((null != simpleRetenueOld) && (null == simpleRetenueNew))
                    || ((null == simpleRetenueOld) && (null != simpleRetenueNew))) {
                hasDifferenceIS = true;
            }
        }

        return hasDifferenceIS;

    }

    private boolean hasNotDateFin(Decision decision) {
        boolean hasNotDatefin = false;

        if (JadeStringUtil.isEmpty(decision.getDemande().getSimpleDemande().getDateFin())) {
            demandeSansDateDeFin++;
            hasNotDatefin = true;
        }

        return hasNotDatefin;
    }

    private boolean hasRetenuNonIS(Decision decision) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, RetenueException {
        // indique si le cas est concerné par une retenue
        boolean isCasAvecRetenu = false;
        PCFAccordee pcf = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                decision.getDemande().getSimpleDemande().getId());

        if (null != pcf) {
            SimpleRetenueSearchModel retenueSearch = new SimpleRetenueSearchModel();
            retenueSearch.setForIdPcfAccordee(pcf.getSimplePCFAccordee().getIdPCFAccordee());
            retenueSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retenueSearch);

            for (JadeAbstractModel model : retenueSearch.getSearchResults()) {
                SimpleRetenue retenu = (SimpleRetenue) model;
                if (!CSTypeRetenue.IMPOT_SOURCE.getCodeSystem().equals(retenu.getCsTypeRetenue())) {
                    isCasAvecRetenu = true;
                }
            }
        }
        return isCasAvecRetenu;
    }

    private StringBuilder initialiseListCasLocaliteHorsVaud() {
        StringBuilder string = new StringBuilder();
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_CAS_LOCALITE_HORS_VAUD"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;
    }

    private StringBuilder initialiseListCasNonCalculable() {
        StringBuilder string = new StringBuilder();
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_DEMANDE_NON_CALCULABLE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;
    }

    private StringBuilder initialiseListCasPartielRi() {
        StringBuilder string = new StringBuilder();

        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_CAS_PARTIEL_TRAITEMENT_MANUEL"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;

    }

    private StringBuilder initialiseListCasRetenu() {
        StringBuilder string = new StringBuilder();
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_CAS_AVEC_RETENU"));
        string.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return string;
    }

    private void initialiseStringBuilderAndList(String mois) throws ParametresException, JadePersistenceException {
        listeLocalite = getListLocalite(mois);
        listCasPartielRi = initialiseListCasPartielRi();
        casAvecRetenu = initialiseListCasRetenu();
        listeCasLocaliteHorsVaud = initialiseListCasLocaliteHorsVaud();
        casNonCalculable = initialiseListCasNonCalculable();
    }

    private boolean isDateFinDecisionDecembre(Decision decision) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (JadeDateUtil.addYears("31.12." + getMoisActuel().substring(3), -1).equals(
                decision.getDemande().getSimpleDemande().getDateFin())
                && !dossiersOuverts.contains(decision.getDemande().getDossier().getDossier().getIdDossier())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDecisionOctroiCompletOrPartiel(Decision decision) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean isOctroiCompletOrPartiel = false;
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())
                || CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                        decision.getSimpleDecision().getCsTypeDecision())) {
            isOctroiCompletOrPartiel = true;
        } else {
            if (!JadeStringUtil.isEmpty(decision.getDemande().getSimpleDemande().getDateFin())) {
                if (JadeDateUtil.isDateAfter(decision.getDemande().getSimpleDemande().getDateFin(),
                        JadeDateUtil.addYears("31.12." + getMoisActuel().substring(3), -1))) {
                    dossiersOuverts.add(decision.getDemande().getDossier().getDossier().getIdDossier());
                }
            } else {
                dossiersOuverts.add(decision.getDemande().getDossier().getDossier().getIdDossier());
            }

        }
        return isOctroiCompletOrPartiel;
    }

    private boolean isDecisionOctroiCompletOrPartielSansRi(Decision decision) {
        // Détermine si le cas un un octroi complet ou un octroi partiel ne venant pas du RI
        boolean isOctroiComplet = CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(
                decision.getSimpleDecision().getCsTypeDecision());
        boolean isPartielSansRi = CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                decision.getSimpleDecision().getCsTypeDecision())
                && !decision.getDemande().getSimpleDemande().getFromRI();

        if (isOctroiComplet || isPartielSansRi) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDemandeNoRenouvelle(Decision decision) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (!getMoisActuel().equals(decision.getDemande().getSimpleDemande().getDateDebut().substring(3))) {
            return true;
        } else {
            // J'ajoute le dossier dans la liste des dossiers déjà traités car la demande est déjà renouvellée
            dossiersOuverts.add(decision.getDemande().getDossier().getDossier().getIdDossier());
            return false;
        }
    }

    /**
     * Détermine si la localité est dans le canton de Vaud, si ce n'est pas le cas, indique le cas dans l'e-mail de fin
     * de processus
     */
    private boolean isLocaliteDansCantonVaud(Demande demande, String mois)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        String idLocalite = getIdLocalite(demande, mois);
        boolean isLocaliteDansCantonVaud = false;
        if (!JadeStringUtil.isEmpty(idLocalite)) {
            isLocaliteDansCantonVaud = listeLocalite.contains(idLocalite);
        }

        if (!isLocaliteDansCantonVaud) {
            listInfoCas(listeCasLocaliteHorsVaud, demande, false, "", "");
        }

        return isLocaliteDansCantonVaud;

    }

    private boolean isMoisTraitementEnCours(String mois) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        String moisActuel = getMoisActuel();
        if (!moisActuel.equals(mois)) {
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel(
                            "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_DATE_PAIEMENT_MENSUEL")
                            + moisActuel);
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidationDecisionNoAuthorise(BSession session) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeNoBusinessLogSessionError {
        if (PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise(session)) {
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel(
                            "SERVICE_TRAITEMENT_ADAPTATION_ERREUR_INTERDICTION_VALIDATION_DECISION"));
            return false;
        } else {
            return true;
        }
    }

    private void listerCasSiRetenu(Decision decision, Demande demandeCopie) throws PCFAccordeeException,
            RetenueException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (hasRetenuNonIS(decision)) {
            listInfoCas(casAvecRetenu, decision.getDemande(), true, demandeCopie.getSimpleDemande().getIdDemande(),
                    decision.getDemande().getSimpleDemande().getIdDemande());
        }
    }

    private StringBuilder listInfoCas(StringBuilder listCas, Demande demande, boolean casRetenu,
            String idNouvelleDemande, String idAncienneDemande) {
        listCas.append(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                + " "
                + demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2()
                + ", "
                + demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel() + ", ");
        if (casRetenu) {
            listCas.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_NUMERO_ANCIENNE_DEMANDE")
                    + idAncienneDemande
                    + ", "
                    + BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_NUMERO_DEMANDE")
                    + idNouvelleDemande + ", ");
        }

        return renseigneCaisse(listCas, demande.getSimpleDemande().getCsCaisse());

    }

    private DecisionSearchModel loadDecisionValidate() throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
        return decisionSearchModel;
    }

    private SimpleRetenue loadRetenueIS(String idPCFAccordee) throws RetenueException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleRetenue simpleretenue = null;
        SimpleRetenueSearchModel retSearch = new SimpleRetenueSearchModel();
        retSearch.setForIdPcfAccordee(idPCFAccordee);
        retSearch.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
        retSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retSearch);
        for (JadeAbstractModel model : retSearch.getSearchResults()) {
            simpleretenue = (SimpleRetenue) model;
        }
        return simpleretenue;
    }

    private void logError(String nss) throws Exception {
        StringBuilder messageError = new StringBuilder();
        messageError.append(BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_ERREUR")
                + "\n");
        messageError.append(BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PF_RENTEPONT_R_NSS") + " : ");
        messageError.append(nss + "\n");
        for (int iMessage = 0; iMessage < JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR).length; iMessage++) {
            messageError.append(JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR)[iMessage]
                    .getContents(JadeThread.currentLanguage()));
        }
        throw new Exception(messageError.toString());
    }

    private void logSiError(Demande demande) throws Exception {
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            logError(demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel());
        }
    }

    private List<Decision> putDecisionInListIfImpression(Decision oldDecision, Decision decision, PCFAccordee oldPCFA,
            PCFAccordee pcfa) throws RetenueException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(oldDecision.getSimpleDecision().getCsTypeDecision())) {
            return putDecisionInListSiNouveauMontant(decision, oldPCFA, pcfa);
        } else {
            return putDecisionInListSiNouvelOctoiOuChangementExcedent(decision, oldPCFA, pcfa);
        }
    }

    private List<Decision> putDecisionInListSiChangementExcedent(Decision decision, PCFAccordee oldPCFA,
            PCFAccordee pcfa) throws RetenueException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            Float newExcedant = Float.parseFloat(pcfa.getSimplePCFAccordee().getExcedantRevenu());
            Float oldExcedant = Float.parseFloat(oldPCFA.getSimplePCFAccordee().getExcedantRevenu());
            if (!newExcedant.equals(oldExcedant)) {
                listDecisionsReturn.add(decision);
                nbDecisionRestePartielMaisChangementExcedent++;
            } else if (hasDifferenceBetweenOldEtNewRetenuImpotSource(oldPCFA, pcfa)) {
                listDecisionsReturn.add(decision);
            }
        }
        return listDecisionsReturn;
    }

    private List<Decision> putDecisionInListSiNouveauMontant(Decision decision, PCFAccordee oldPCFA, PCFAccordee pcfa)
            throws RetenueException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Float newMontant = Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        Float oldMontant = Float.parseFloat(oldPCFA.getSimplePCFAccordee().getMontant());
        if (!newMontant.equals(oldMontant)) {
            listDecisionsReturn.add(decision);
        } else if (hasDifferenceBetweenOldEtNewRetenuImpotSource(oldPCFA, pcfa)) {
            listDecisionsReturn.add(decision);
        }
        return listDecisionsReturn;
    }

    private List<Decision> putDecisionInListSiNouvelOctoiOuChangementExcedent(Decision decision, PCFAccordee oldPCFA,
            PCFAccordee pcfa) throws RetenueException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        // Si non c'est un partiel et on retourne la décision si elle est passé en octroi
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            listDecisionsReturn.add(decision);
            nbDecisionPartielEnOcroi++;
            return listDecisionsReturn;
        } else {
            return putDecisionInListSiChangementExcedent(decision, oldPCFA, pcfa);
        }
    }

    private StringBuilder renseigneCaisse(StringBuilder stringBuilder, String csCaisse) {
        if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
            stringBuilder.append(CSCaisse.CCVD);
        } else {
            stringBuilder.append(CSCaisse.AGENCE_LAUSANNE);
        }
        stringBuilder.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_RETOUR_LIGNE"));
        return stringBuilder;
    }

    private void renseignerInfoMailSuiteTraitements(JadeBusinessLogSession logSession) {
        logSession.info(
                this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel(
                        "SERVICE_TRAITEMENT_ADAPTATION_DEMANDE_VALIDE_SANS_DATE_FIN")
                        + demandeSansDateDeFin);
        logSession.info(this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_DEMANDES_FERMEES")
                        + demandesFermees);
        logSession.info(this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext()
                        .getLabel("SERVICE_TRAITEMENT_ADAPTATION_DEMANDES_REOUVERTES") + nouvellesDemandes.size());
        logSession.info(
                this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel(
                        "SERVICE_TRAITEMENT_ADAPTATION_DECISION_PARTIEL_EN_OCTROI")
                        + nbDecisionPartielEnOcroi);
        logSession.info(
                this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel(
                        "SERVICE_TRAITEMENT_ADAPTATION_DECISION_PARTIEL_EN_PARTIEL_CHANGEMENT_EXCEDENT")
                        + nbDecisionRestePartielMaisChangementExcedent);
        logSession.info(this.getClass().getName(),
                BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_DECISION_IMPRIMEES")
                        + listDecisionsReturn.size());
        logSession.info(this.getClass().getName(), listCasPartielRi.toString());
        logSession.info(this.getClass().getName(), casNonCalculable.toString());
        logSession.info(this.getClass().getName(), listeCasLocaliteHorsVaud.toString());
        logSession.info(this.getClass().getName(), casAvecRetenu.toString());

    }

    private PCFAccordee repriseMesureCoaching(PCFAccordee oldPCFA, PCFAccordee pcfa) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PerseusServiceLocator.getPCFAccordeeService().update(pcfa, "0", "0",
                oldPCFA.getCalcul().getDonneeString(OutputData.MESURE_COACHING));
    }

    private Demande updateDateEtTypeNouvelleDemande(Demande nouvelleDemande, Decision oldDecision) throws Exception {
        String dateDebut = "01." + getMoisActuel();
        nouvelleDemande.getSimpleDemande().setDateDebut(dateDebut);
        nouvelleDemande.getSimpleDemande().setDateFin("");
        nouvelleDemande.getSimpleDemande().setDateDepot(dateDebut);
        nouvelleDemande.getSimpleDemande().setTypeDemande(determinerTypeNouvelleDemande(oldDecision));

        return PerseusServiceLocator.getDemandeService().update(nouvelleDemande);
    }
}
