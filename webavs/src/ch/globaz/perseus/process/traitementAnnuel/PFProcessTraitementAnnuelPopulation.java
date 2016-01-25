package ch.globaz.perseus.process.traitementAnnuel;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.annotation.BusinessKey;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationCheckable;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasse;
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasseSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFProcessTraitementAnnuelPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<PFProcessTraitementAnnuelEnum>,
        JadeProcessPopulationCheckable<PFProcessTraitementAnnuelEnum> {

    private List<JadeProcessEntity> entites = null;
    private Map<PFProcessTraitementAnnuelEnum, String> properties = null;

    public PFProcessTraitementAnnuelPopulation() {
        entites = new ArrayList<JadeProcessEntity>();
    }

    @Override
    public void checker(Map<PFProcessTraitementAnnuelEnum, String> map) throws JadePersistenceException,
            JadeApplicationException {
    }

    // Cette méthode permet d'éviter d'exécuter plus d'un traitement par année avec les mêmes paramètres (voir pour les
    // traitements annuel avec le unique = true)
    @Override
    @BusinessKey(unique = false, messageKey = "perseus.traitementAdaptation.messagekey")
    public String getBusinessKey() {
        return properties.get(PFProcessTraitementAnnuelEnum.MOIS);
    }

    @Override
    public Class<PFProcessTraitementAnnuelEnum> getEnumForProperties() {
        return PFProcessTraitementAnnuelEnum.class;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        // url entité
        return "idDecision=" + entity.getValue1() + "&idDemande=" + entity.getIdRef();
    }

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {

        List<JadeProcessEntity> laPopulation = new ArrayList<JadeProcessEntity>();

        if (isJanvier()) {
            PerseusServiceLocator.getPmtMensuelService().desactiverValidationDecision();

            // Première population : population sans date de fin
            DemandeTraitementMasseSearchModel demandeTraitementMasseSearchModelPopulationSansDatefin = new DemandeTraitementMasseSearchModel();
            demandeTraitementMasseSearchModelPopulationSansDatefin.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());

            demandeTraitementMasseSearchModelPopulationSansDatefin
                    .setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            // demandeTraitementMasseSearchModelPopulationSansDatefin.setDefinedSearchSize(20);
            // demandeTraitementMasseSearchModelPopulationSansDatefin.setForIdDossier("3735");

            List<String> forListCsTypes = new ArrayList<String>();
            forListCsTypes.add(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
            forListCsTypes.add(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
            demandeTraitementMasseSearchModelPopulationSansDatefin.setForListCsTypes(forListCsTypes);

            demandeTraitementMasseSearchModelPopulationSansDatefin.setLessDateDebut(JadeDateUtil.addDays("01."
                    + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt(), -1));

            demandeTraitementMasseSearchModelPopulationSansDatefin.setWhereKey("populationSansDateDeFin");

            demandeTraitementMasseSearchModelPopulationSansDatefin = (DemandeTraitementMasseSearchModel) JadePersistenceManager
                    .search(demandeTraitementMasseSearchModelPopulationSansDatefin);

            createArray(demandeTraitementMasseSearchModelPopulationSansDatefin, laPopulation);
        }
        return laPopulation;
    }

    @Override
    public void setProperties(Map<PFProcessTraitementAnnuelEnum, String> hashMap) {
        properties = hashMap;
    }

    private void createArray(DemandeTraitementMasseSearchModel demandeTraitementMasseSearchModel,
            List<JadeProcessEntity> entites) {
        // Les value1, les description et l'idRef sont immuable durant le traitement de masse
        for (JadeAbstractModel model : demandeTraitementMasseSearchModel.getSearchResults()) {
            DemandeTraitementMasse demande = (DemandeTraitementMasse) model;
            JadeProcessEntity entite = new JadeProcessEntity();

            // Id demande
            entite.setIdRef(model.getId());
            // Description
            String noAncienneDemande = BSessionUtil.getSessionFromThreadContext().getLabel(
                    "SERVICE_TRAITEMENT_NUMERO_ANCIENNE_DEMANDE");

            String caisse;
            if (CSCaisse.CCVD.getCodeSystem().equals(demande.getSimpleDemande().getCsCaisse())) {
                caisse = CSCaisse.CCVD.toString();
            } else {
                caisse = CSCaisse.AGENCE_LAUSANNE.toString();
            }

            entite.setDescription(demande.getNom() + " " + demande.getPrenom() + ", " + demande.getNss() + ", "
                    + noAncienneDemande + " " + model.getId() + ", " + caisse);

            // Id de la décision
            entite.setValue1(demande.getIdDecision());
            entites.add(entite);
        }
    }

    private boolean isJanvier() throws PaiementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        boolean isJanvier = true;
        String moisActuel = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        if (!moisActuel.substring(0, 2).equals("01")) {
            isJanvier = false;
            JadeThread.logError(this.getClass().getName(), "perseus.traitementAnnuel.error.paiement.janvier");

            throw new RuntimeException(JadeThread.getMessage("perseus.traitementAnnuel.error.paiement.janvier"));
        }
        return isJanvier;
    }
}
