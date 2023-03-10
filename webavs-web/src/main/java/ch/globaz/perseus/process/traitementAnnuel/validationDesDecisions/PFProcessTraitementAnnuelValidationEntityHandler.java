package ch.globaz.perseus.process.traitementAnnuel.validationDesDecisions;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.process.traitementAnnuel.PFProcessTraitementAnnuelEnum;

public class PFProcessTraitementAnnuelValidationEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PFProcessTraitementAnnuelEnum>,
        JadeProcessEntityDataFind<PFProcessTraitementAnnuelEnum>, JadeProcessEntityNeedProperties {

    private String id;
    private Map<PFProcessTraitementAnnuelEnum, String> mapSavedValue = new HashMap<PFProcessTraitementAnnuelEnum, String>();
    private Map<PFProcessTraitementAnnuelEnum, String> mapValueToSave = new HashMap<PFProcessTraitementAnnuelEnum, String>();
    private String idDecision;
    private Map<Enum<?>, String> properties;

    public PFProcessTraitementAnnuelValidationEntityHandler() {

    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        if (mapSavedValue.get(PFProcessTraitementAnnuelEnum.TYPE_POPULATION).equals(
                PFProcessTraitementAnnuelEnum.OCTROI.toString())) {
            PCFAccordee oldPCFA = PerseusServiceLocator.getPCFAccordeeService().readForDemande(id);

            PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                    mapSavedValue.get(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE));

            pcfa = repriseMesureCoaching(oldPCFA, pcfa);

            Demande demandeCopie = PerseusServiceLocator.getDemandeService().read(
                    mapSavedValue.get(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE));

            Decision oldDecision = PerseusServiceLocator.getDecisionService().read(idDecision);

            String textDecision = properties.get(PFProcessTraitementAnnuelEnum.TEXTE_DECISION);
            Decision decision = createDecisionForNewDemande(demandeCopie, oldDecision, pcfa, textDecision);

            BSession session = BSessionUtil.getSessionFromThreadContext();

            decision = PerseusServiceLocator.getDecisionService().valider(decision, session.getUserId());

            // Pr?paration pour impression des d?cisions
            putDecisionInListIfImpression(oldDecision, decision, oldPCFA, pcfa);
        }
    }

    private void putDecisionInListIfImpression(Decision oldDecision, Decision decision, PCFAccordee oldPCFA,
            PCFAccordee pcfa) throws RetenueException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        Float newExcedant = Float.parseFloat(pcfa.getSimplePCFAccordee().getExcedantRevenu());
        Float oldExcedant = Float.parseFloat(oldPCFA.getSimplePCFAccordee().getExcedantRevenu());
        Float newMontant = Float.parseFloat(pcfa.getSimplePCFAccordee().getMontant());
        Float oldMontant = Float.parseFloat(oldPCFA.getSimplePCFAccordee().getMontant());

        if (!newMontant.equals(oldMontant) || !newExcedant.equals(oldExcedant)) {
            mapValueToSave.put(PFProcessTraitementAnnuelEnum.DECISION_IMPRIMER, decision.getId());
        } else if (hasDifferenceBetweenOldEtNewRetenuImpotSource(oldPCFA, pcfa)) {
            mapValueToSave.put(PFProcessTraitementAnnuelEnum.DECISION_IMPRIMER, decision.getId());
        }
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

    private PCFAccordee repriseMesureCoaching(PCFAccordee oldPCFA, PCFAccordee pcfa) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PerseusServiceLocator.getPCFAccordeeService().update(pcfa, "0", "0",
                oldPCFA.getCalcul().getDonneeString(OutputData.MESURE_COACHING));
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

    /**
     * Chargement des valeurs sauvegard?s lors de la g?n?ration de la population
     */
    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        id = entity.getIdRef();
        idDecision = entity.getValue1();
    }

    /**
     * Valeurs ? sauvegarder pour le prochain step.
     */
    @Override
    public Map<PFProcessTraitementAnnuelEnum, String> getValueToSave() {
        return mapValueToSave;
    }

    /**
     * Chargement des valeurs sauvegard?s lors du dernier step.
     */
    @Override
    public void setData(Map<PFProcessTraitementAnnuelEnum, String> hashMap) {
        mapSavedValue = hashMap;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;

    }
}
