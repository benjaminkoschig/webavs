package ch.globaz.vulpecula.services;

import globaz.al.process.generations.ALGenAffilieProcess;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.exceptions.business.ALDossierBusinessException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;
import ch.globaz.al.business.models.dossier.DossierListComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.business.models.af.DroitSearchModel;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.business.models.is.EntetePrestationSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.al.exception.TauxImpositionNotFoundException;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;

public class PTAFServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(PTAFServices.class);

    /**
     * Génération des prestations AF pour l'employeur et la période indiquée.
     * Cette action s'effectue à la validation d'un décompte uniquement sur un employeur qui n'est pas annuel.
     * 
     * @param employeur Employeur sur lequel lancer la génération
     * @param periode Période de génération
     */
    public static boolean genererPrestationsAFPeriodique(Employeur employeur, PeriodeMensuelle periode,
            TypeDecompte typeDecompte) {
        ProcessusPeriodiqueModel processusDirect = findProcessusPeriodique(ALCSPrestation.BONI_DIRECT);
        ProcessusPeriodiqueModel processusIndirect = findProcessusPeriodique(ALCSPrestation.BONI_INDIRECT);

        if (processusDirect == null || processusIndirect == null) {
            if (processusIndirect == null) {
                JadeThread.logError(PTAFServices.class.getName(), "vulpecula.af.aucun_processus_indirect_ouvert");
            }
            if (processusDirect == null) {
                JadeThread.logError(PTAFServices.class.getName(), "vulpecula.af.aucun_processus_direct_ouvert");
            }
            return false;
        }

        if (!employeur.isAnnuel() && TypeDecompte.PERIODIQUE.equals(typeDecompte)) {
            ALGenAffilieProcess genAffilieProcess = new ALGenAffilieProcess();
            genAffilieProcess.setNumAffilie(employeur.getAffilieNumero());
            genAffilieProcess.setPeriodeAGenerer(periode.getPeriodeFin().getMoisAnneeFormatte());
            genAffilieProcess.setSession(BSessionUtil.getSessionFromThreadContext());
            genAffilieProcess.run();

            try {
                List<RecapitulatifEntrepriseModel> recaps = findRecapitulatifs(employeur);
                for (RecapitulatifEntrepriseModel recap : recaps) {
                    if (ALCSPrestation.BONI_DIRECT.equals(recap.getBonification())) {
                        recap.setIdProcessusPeriodique(processusDirect.getId());
                    } else {
                        recap.setIdProcessusPeriodique(processusIndirect.getId());
                    }
                    JadePersistenceManager.update(recap);
                }
            } catch (Exception e) {
                LOGGER.error(e.toString());
            }

        }
        return true;
    }

    /**
     * Suppression des prestations relatives à l'employeur
     * Cette action s'effectue à la dévalidation du décompte uniquement sur un employeur qui n'est pas mensuel.
     * 
     * @param employeur Employeur sur lequel supprimer
     */
    public static void supprimerPrestationsAF(Decompte decompte) {
        final Employeur employeur = decompte.getEmployeur();
        if (!employeur.isAnnuel()) {
            try {
                List<RecapitulatifEntrepriseModel> recaps = findRecapitulatifs(employeur);
                for (RecapitulatifEntrepriseModel recap : recaps) {
                    EntetePrestationSearchModel searchModel = new EntetePrestationSearchModel();
                    searchModel.setForEtat(ALCSPrestation.ETAT_SA);
                    searchModel.setForIdRecap(recap.getIdRecap());
                    searchModel.setForPeriodeDe(decompte.getPeriodeDebutAsSwissValue());
                    searchModel.setForPeriodeA(decompte.getPeriodeFinAsSwissValue());
                    searchModel.setWhereKey("prestationPositiveExistantePeriodeExacte");
                    JadePersistenceManager.delete(searchModel);

                    if (getNbPrestationsInRecaps(recap) == 0) {
                        ALServiceLocator.getRecapitulatifEntrepriseModelService().delete(recap);
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.toString());
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
            }
        }
    }

    public static int getNbPrestationsInRecaps(RecapitulatifEntrepriseModel recap) {
        EntetePrestationSearchModel searchModel = new EntetePrestationSearchModel();
        searchModel.setForIdRecap(recap.getIdRecap());
        return RepositoryJade.searchForAndFetch(searchModel).size();
    }

    public static List<RecapitulatifEntrepriseModel> findRecapitulatifs(Employeur employeur) {
        List<RecapitulatifEntrepriseModel> liste = new ArrayList<RecapitulatifEntrepriseModel>();
        RecapitulatifEntrepriseSearchModel searchModel = new RecapitulatifEntrepriseSearchModel();
        searchModel.setForNumeroAffilie(employeur.getAffilieNumero());
        searchModel.setForEtatRecap(ALCSPrestation.ETAT_SA);
        RepositoryJade.searchFor(searchModel);
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            RecapitulatifEntrepriseModel recapitulatifEntrepriseModel = (RecapitulatifEntrepriseModel) model;
            liste.add(recapitulatifEntrepriseModel);
        }
        return liste;
    }

    public static ProcessusPeriodiqueModel findProcessusPeriodique(String bonification) {
        try {
            List<ProcessusPeriodiqueModel> liste = ALServiceLocator.getBusinessProcessusService()
                    .getUnlockProcessusPaiementForPeriode(bonification, ALCSAffilie.GENRE_ASSURANCE_PARITAIRE);
            if (!liste.isEmpty()) {
                for (ProcessusPeriodiqueModel processusPeriodiqueModel : liste) {
                    if (processusPeriodiqueModel.getIsPartiel()) {
                        return processusPeriodiqueModel;
                    }
                }
            }
            return null;
        } catch (Exception ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
    }

    /**
     * Compte le nombre de dossier actifs à une date donnée
     * 
     * @param date
     * @param idTiersAllocataire
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public static int countDossiersActifs(String date, String idTiersAllocataire) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#countDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.date.validity", new String[] { "date" }));
        }

        if (!JadeNumericUtil.isIntegerPositif(idTiersAllocataire)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#getDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.checkParam.common.param.NaN",
                            new String[] { "paramName", idTiersAllocataire }));
        }

        DossierComplexSearchModel search = new DossierComplexSearchModel();
        search.setForIdTiersAllocataire(idTiersAllocataire);
        search.setForDebutValidite(date);
        search.setForFinValidite(date);
        search.setWhereKey(DossierComplexSearchModel.SEARCH_DOSSIERS_ACTIF);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        int nbDossiers = JadePersistenceManager.count(search);

        return nbDossiers;
    }

    /**
     * Détermine si l'allocataire possède au moins un droit AF pour la période donnée
     * 
     * @param date
     * @param idTiersAllocataire
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public static boolean hasDroitsActifs(String date, String idTiersAllocataire) throws JadeApplicationException,
            JadePersistenceException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#countDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "al.checkParam.common.date.validity", new String[] { "date" }));
        }

        if (!JadeNumericUtil.isIntegerPositif(idTiersAllocataire)) {
            throw new ALDossierBusinessException("DossierBusinessServiceImpl#getDossiersActifs"
                    + JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), "al.checkParam.common.param.NaN",
                            new String[] { "paramName", idTiersAllocataire }));
        }

        // On boucle sur les dossiers pour récupérer les dossiers actifs
        DossierComplexSearchModel search = new DossierComplexSearchModel();
        search.setForIdTiersAllocataire(idTiersAllocataire);
        search.setForDebutValidite(date);
        search.setForFinValidite(date);
        search.setWhereKey(DossierComplexSearchModel.SEARCH_DOSSIERS_ACTIF);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = (DossierComplexSearchModel) JadePersistenceManager.search(search);

        for (int i = 0; i < search.getSize(); i++) {
            DossierComplexModel dossier = (DossierComplexModel) search.getSearchResults()[i];

            // On boucle sur les droits
            DroitSearchModel droitSearch = new DroitSearchModel();
            droitSearch.setForIdDossier(dossier.getId());
            droitSearch.setForDateDebut(date);
            droitSearch.setForDateFin(date);
            droitSearch = (DroitSearchModel) JadePersistenceManager.search(droitSearch);

            if (droitSearch.getSize() > 0) {
                return true;
            }

        }

        return false;
    }

    public static List<DroitComplexModel> getDroitsOrdresForIdTiers(String idTiers) {
        List<DroitComplexModel> droitsForIdTiers = getDroitsForIdTiers(idTiers);
        Comparator<DroitComplexModel> comp = new Comparator<DroitComplexModel>() {
            @Override
            public int compare(DroitComplexModel d1, DroitComplexModel d2) {
                DroitModel droitModel1 = d1.getDroitModel();
                DroitModel droitModel2 = d2.getDroitModel();

                Periode periode1 = new Periode(droitModel1.getDebutDroit(), droitModel1.getFinDroitForcee());
                Periode periode2 = new Periode(droitModel2.getDebutDroit(), droitModel2.getFinDroitForcee());
                return periode1.compareTo(periode2);
            }
        };
        Collections.sort(droitsForIdTiers, Collections.reverseOrder(comp));
        return droitsForIdTiers;
    }

    private static List<DroitComplexModel> getDroitsForIdTiers(String idTiers) {
        List<DroitComplexModel> droits = new ArrayList<DroitComplexModel>();
        List<String> idsDossiers = new ArrayList<String>();
        DossierListComplexSearchModel search = new DossierListComplexSearchModel();
        search.setForIdTiersAllocataire(idTiers);
        search.setWhereKey("AL0002");
        List<DossierListComplexModel> dossiers = RepositoryJade.searchForAndFetch(search);
        for (DossierListComplexModel dossier : dossiers) {
            idsDossiers.add(dossier.getIdDossier());
        }

        DroitComplexSearchModel searchDroit = new DroitComplexSearchModel();
        for (String idDossier : idsDossiers) {
            searchDroit.setForIdDossier(idDossier);
            List<DroitComplexModel> droitsComplexModel = RepositoryJade.searchForAndFetch(searchDroit);
            for (DroitComplexModel droitComplexModel : droitsComplexModel) {
                droits.add(droitComplexModel);
            }
        }

        return droits;
    }

    public static List<EntetePrestationComplexModel> getPrestationsForAlloc(String idTiers, Date dateDebut,
            Date dateFin, String idEmployeur) throws TauxImpositionNotFoundException {
        // On recherche l'allocataire d'après l'id Tiers pour rechercher les prestations
        AllocataireComplexSearchModel allocataireSearch = new AllocataireComplexSearchModel();
        allocataireSearch.setForIdTiers(idTiers);

        try {
            JadePersistenceManager.search(allocataireSearch);
        } catch (JadePersistenceException e) {
            return null;
        }

        if (allocataireSearch.getSize() > 0) {
            AllocataireComplexModel allocataire = (AllocataireComplexModel) allocataireSearch.getSearchResults()[0];
            EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
            searchModel.setForIdAllocataire(allocataire.getId());
            searchModel.setForPeriodeDeAfterOrEquals(dateDebut);
            searchModel.setForPeriodeDeBeforeOrEquals(dateFin);
            searchModel.setForEtat(ALCSPrestation.ETAT_CO);
            searchModel.setForAffiliationId(idEmployeur);
            List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
            return prestations;
        }

        return null;
    }
}
