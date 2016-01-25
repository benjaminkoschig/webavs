package ch.globaz.perseus.process.traitementAdaptation;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
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
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasse;
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasseSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFProcessTraitementAdaptationPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<PFProcessTraitementAdaptationEnum>,
        JadeProcessPopulationCheckable<PFProcessTraitementAdaptationEnum> {

    private List<JadeProcessEntity> entites = null;
    private Map<PFProcessTraitementAdaptationEnum, String> properties = null;

    public PFProcessTraitementAdaptationPopulation() {
        entites = new ArrayList<JadeProcessEntity>();
    }

    @Override
    public void checker(Map<PFProcessTraitementAdaptationEnum, String> map) throws JadePersistenceException,
            JadeApplicationException {
    }

    // Cette méthode permet d'éviter d'exécuter plus d'un traitement par année avec les mêmes paramètres (voir pour les
    // traitements annuel avec le unique = true)
    @Override
    @BusinessKey(unique = false, messageKey = "perseus.traitementAdaptation.messagekey")
    public String getBusinessKey() {
        return properties.get(PFProcessTraitementAdaptationEnum.MOIS);
    }

    @Override
    public Class<PFProcessTraitementAdaptationEnum> getEnumForProperties() {
        return PFProcessTraitementAdaptationEnum.class;
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

        PerseusServiceLocator.getPmtMensuelService().desactiverValidationDecision();

        // Première population : population sans date de fin
        DemandeTraitementMasseSearchModel demandeTraitementMasseSearchModelPopulationSansDatefin = new DemandeTraitementMasseSearchModel();
        demandeTraitementMasseSearchModelPopulationSansDatefin.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        demandeTraitementMasseSearchModelPopulationSansDatefin
                .setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        List<String> forListCsTypes = new ArrayList<String>();
        forListCsTypes.add(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        forListCsTypes.add(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
        demandeTraitementMasseSearchModelPopulationSansDatefin.setForListCsTypes(forListCsTypes);

        demandeTraitementMasseSearchModelPopulationSansDatefin.setLessDateDebut(JadeDateUtil.addDays("01."
                + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt(), -1));

        demandeTraitementMasseSearchModelPopulationSansDatefin
                .setWhereKey(DemandeTraitementMasseSearchModel.POPULATION_SANS_DATE_DE_FIN);

        demandeTraitementMasseSearchModelPopulationSansDatefin = (DemandeTraitementMasseSearchModel) JadePersistenceManager
                .search(demandeTraitementMasseSearchModelPopulationSansDatefin);

        List<JadeProcessEntity> laPopulation = new ArrayList<JadeProcessEntity>();
        createArray(demandeTraitementMasseSearchModelPopulationSansDatefin, laPopulation, false);

        // Deuxième population : population avec date de fin au 31-12 de l'année précédente
        DemandeTraitementMasseSearchModel demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente = new DemandeTraitementMasseSearchModel();
        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente.setForCsEtat(CSEtatDecision.VALIDE
                .getCodeSystem());
        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente
                .setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente
                .setForCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());

        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente.setFromRI(false);

        // Date fin année précédente
        String dateFinAnnePassee = JadeDateUtil.addYears("31.12."
                + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt().substring(3), -1);
        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente.setForDateFin(dateFinAnnePassee);

        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente
                .setWhereKey(DemandeTraitementMasseSearchModel.POPULATION_DATE_DE_FIN_FIN_ANNEE);

        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente.setForDateFinMax(dateFinAnnePassee);

        demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente = (DemandeTraitementMasseSearchModel) JadePersistenceManager
                .search(demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente);

        createArray(demandeTraitementMasseSearchModelPopulationDateFinDecembreAnneePrecedente, laPopulation, true);

        return laPopulation;
    }

    @Override
    public void setProperties(Map<PFProcessTraitementAdaptationEnum, String> hashMap) {
        properties = hashMap;
    }

    private ArrayList<String> listIdDossier = new ArrayList<String>();

    private void createArray(DemandeTraitementMasseSearchModel demandeTraitementMasseSearchModel,
            List<JadeProcessEntity> entites, Boolean isDateFinDecembre) {
        // Les value1, les description et l'idRef sont immuable durant le traitement de masse
        for (JadeAbstractModel model : demandeTraitementMasseSearchModel.getSearchResults()) {
            DemandeTraitementMasse demande = (DemandeTraitementMasse) model;
            JadeProcessEntity entite = new JadeProcessEntity();

            Boolean ajoutListe = true;

            if (isDateFinDecembre) {
                if (listIdDossier.contains(demande.getSimpleDemande().getIdDossier())) {
                    ajoutListe = false;
                }
            } else {
                listIdDossier.add(demande.getSimpleDemande().getIdDossier());
            }

            if (ajoutListe) {
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
    }

}
