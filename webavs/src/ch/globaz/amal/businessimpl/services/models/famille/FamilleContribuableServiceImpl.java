/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.famille;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuableView;
import ch.globaz.amal.business.models.famille.FamilleContribuableViewSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.famille.FamilleContribuableService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;

/**
 * @author CBU
 * 
 */
public class FamilleContribuableServiceImpl implements FamilleContribuableService {

    /*
     * Contrôle de la date de fin définitive d'un membre de famille Si renseigné, contrôle si nous devons clôturer des
     * droits ouverts
     */
    private void checkDateFin(FamilleContribuable currentFamilleContribuable) {
        try {
            // Récupération des dates de fin et code de fin
            String currentDateFin = currentFamilleContribuable.getSimpleFamille().getFinDefinitive();
            String currentCodeFin = currentFamilleContribuable.getSimpleFamille().getCodeTraitementDossier();

            // Contrôle si renseigné
            if (JadeStringUtil.isBlankOrZero(currentDateFin)) {
                return;
            }
            // récupération année de subside et recherche des subsides du membre pour l'année recherchée
            String anneeHistorique = currentDateFin.substring(3);
            SimpleDetailFamilleSearch subsidesSearch = new SimpleDetailFamilleSearch();
            subsidesSearch.setForAnneeHistorique(anneeHistorique);
            subsidesSearch.setForIdFamille(currentFamilleContribuable.getSimpleFamille().getIdFamille());
            subsidesSearch.setForIdContribuable(currentFamilleContribuable.getSimpleFamille().getIdContribuable());
            subsidesSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(subsidesSearch);
            for (int iSubside = 0; iSubside < subsidesSearch.getSize(); iSubside++) {
                SimpleDetailFamille currentSubside = (SimpleDetailFamille) subsidesSearch.getSearchResults()[iSubside];
                boolean createStopAnnonceSedex = false;
                // Traitement des subsides qui n'ont pas de date de fin
                if (JadeStringUtil.isBlankOrZero(currentSubside.getFinDroit())) {
                    // Contrôle sur le code de fin définitive
                    if (currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.DEPART_AUTRE_CANTON
                            .getValue())) {
                        if (currentSubside.getTypeDemande().equals(
                                IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue())
                                || currentSubside.getTypeDemande().equals(
                                        IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
                            // Date de fin de subside à la date de fin définitive
                            currentSubside.setFinDroit(currentDateFin);
                            if (currentDateFin.indexOf("12.") == 0) {
                                // Pas d'annonce CM si date fin == 12.xxxx
                            } else {
                                currentSubside.setAnnonceCaisseMaladie(false);
                                createStopAnnonceSedex = true;
                            }
                            if (JadeDateUtil.isDateMonthYearAfter(currentSubside.getFinDroit(),
                                    currentSubside.getDebutDroit())
                                    || currentSubside.getFinDroit().equals(currentSubside.getDebutDroit())) {
                                AmalImplServiceLocator.getSimpleDetailFamilleService().update(currentSubside);
                            } else {
                                currentSubside.setFinDroit(null);
                                currentSubside.setCodeActif(false);
                                AmalImplServiceLocator.getSimpleDetailFamilleService().update(currentSubside);
                            }
                        } else {
                            // Date de fin de subside à 12.xxxx
                            currentSubside.setFinDroit("12." + anneeHistorique);
                            // currentSubside.setAnnonceCaisseMaladie(false);
                            if (JadeDateUtil.isDateMonthYearAfter(currentSubside.getFinDroit(),
                                    currentSubside.getDebutDroit())
                                    || currentSubside.getFinDroit().equals(currentSubside.getDebutDroit())) {
                                AmalImplServiceLocator.getSimpleDetailFamilleService().update(currentSubside);
                            }
                        }
                    } else if (currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.CHGT_ETAT_CIVIL
                            .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.CHGT_ETAT_CIVIL_MERE
                                    .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.MODIF_SELON_FISC
                                    .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.PLUS_CHARGE_PARENTS
                                    .getValue())) {
                        // Date de fin de subside à 12.xxxx
                        currentSubside.setFinDroit("12." + anneeHistorique);
                        // currentSubside.setAnnonceCaisseMaladie(false);
                        if (JadeDateUtil.isDateMonthYearAfter(currentSubside.getFinDroit(),
                                currentSubside.getDebutDroit())
                                || currentSubside.getFinDroit().equals(currentSubside.getDebutDroit())) {
                            AmalImplServiceLocator.getSimpleDetailFamilleService().update(currentSubside);
                        }
                    } else if (currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.DECES.getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.DEPART.getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.DISPENSE.getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.SUSPENSION
                                    .getValue())
                            || currentCodeFin
                                    .equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.EXEMPTION.getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.RENONCEMENT
                                    .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.ADRESSE_INCONNUE
                                    .getValue())
                            || currentCodeFin
                                    .equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.ATTRIBUTION_AUTRE_PARENT
                                            .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.DEPART_ETRANGER
                                    .getValue())
                            || currentCodeFin
                                    .equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.ATTRIBUTION_AUTRE_CANTON
                                            .getValue())
                            || currentCodeFin
                                    .equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.ATTRIBUTION_PROPRE_NO
                                            .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.CHARGE_AUTRE_PARENT
                                    .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.ATTRIBUTION_AUTRE_NO
                                    .getValue())
                            || currentCodeFin.equals(IAMCodeSysteme.AMCodeTraitementDossierFamille.FIN_FORMATION
                                    .getValue())) {
                        // Date de fin de subside à fin définitive
                        currentSubside.setFinDroit(currentDateFin);
                        if (currentDateFin.indexOf("12.") == 0) {
                            // Pas d'annonce CM si date fin == 12.xxxx
                        } else {
                            currentSubside.setAnnonceCaisseMaladie(false);
                            createStopAnnonceSedex = true;
                        }
                        if (JadeDateUtil.isDateMonthYearAfter(currentSubside.getFinDroit(),
                                currentSubside.getDebutDroit())
                                || currentSubside.getFinDroit().equals(currentSubside.getDebutDroit())) {
                            AmalImplServiceLocator.getSimpleDetailFamilleService().update(currentSubside);
                        } else {
                            currentSubside.setFinDroit(null);
                            currentSubside.setCodeActif(false);
                            AmalImplServiceLocator.getSimpleDetailFamilleService().update(currentSubside);
                        }
                    }

                    if (createStopAnnonceSedex) {
                        // update sedex message en état initial
                        try {
                            AmalImplServiceLocator.getAnnoncesRPService().initAnnonceInterruption(
                                    currentSubside.getIdContribuable(), currentSubside.getIdDetailFamille(),
                                    currentSubside.getCodeActif());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            // On recherche ensuite les subsides des années suivantes pour les désactiver
            SimpleDetailFamilleSearch subsidesAnneeSuivanteSearch = new SimpleDetailFamilleSearch();
            int iNextAnneeHistorique = Integer.parseInt(anneeHistorique) + 1;
            subsidesAnneeSuivanteSearch.setForGOEAnneeHistorique(String.valueOf(iNextAnneeHistorique));
            subsidesAnneeSuivanteSearch.setForIdFamille(currentFamilleContribuable.getSimpleFamille().getIdFamille());
            subsidesAnneeSuivanteSearch.setForIdContribuable(currentFamilleContribuable.getSimpleFamille()
                    .getIdContribuable());
            subsidesAnneeSuivanteSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(
                    subsidesAnneeSuivanteSearch);
            for (int iSubside = 0; iSubside < subsidesAnneeSuivanteSearch.getSize(); iSubside++) {
                SimpleDetailFamille nextSubside = (SimpleDetailFamille) subsidesAnneeSuivanteSearch.getSearchResults()[iSubside];
                nextSubside.setCodeActif(false);
                // update sedex message en état initial
                try {
                    AmalImplServiceLocator.getAnnoncesRPService().initAnnonceInterruption(
                            nextSubside.getIdContribuable(), nextSubside.getIdDetailFamille(),
                            nextSubside.getCodeActif());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                AmalImplServiceLocator.getSimpleDetailFamilleService().update(nextSubside);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.famille.FamilleContribuableService#count(ch.globaz.amal.business.models
     * .famille.FamilleContribuableSearch)
     */
    @Override
    public int count(FamilleContribuableSearch search) throws FamilleException, JadePersistenceException {
        if (search == null) {
            throw new FamilleException("Unable to search familleContribuable, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public int count(SimpleFamilleSearch search) throws FamilleException, JadePersistenceException {
        if (search == null) {
            throw new FamilleException("Unable to search familleContribuable, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.famille.FamilleContribuableService#create(ch.globaz.amal.business.models
     * .famille.FamilleContribuable)
     */
    @Override
    public FamilleContribuable create(FamilleContribuable familleContribuable) throws JadePersistenceException,
            FamilleException {
        if (familleContribuable == null) {
            throw new FamilleException("Unable to create familleContribuable, the given model is null!");
        }
        try {
            // Création du tiers si nécessaire
            if (JadeStringUtil.isBlankOrZero(familleContribuable.getPersonneEtendue().getTiers().getIdTiers())
                    && !JadeStringUtil.isBlankOrZero(familleContribuable.getPersonneEtendue().getTiers()
                            .getDesignation1())
                    && !JadeStringUtil.isBlankOrZero(familleContribuable.getPersonneEtendue().getTiers()
                            .getDesignation2())) {

                AMGestionTiers gestionTiers = new AMGestionTiers();

                try {
                    gestionTiers.createTiersContribuable(familleContribuable);
                } catch (ContribuableException e) {
                    throw new FamilleException(e.getMessage());
                }
            }

            // Create Simple Famille
            SimpleFamille simpleFamille = familleContribuable.getSimpleFamille();
            simpleFamille.setIdTier(familleContribuable.getPersonneEtendue().getTiers().getIdTiers());
            simpleFamille.setIsContribuable(false);
            simpleFamille.setDateNaissance(familleContribuable.getPersonneEtendue().getPersonne().getDateNaissance());
            simpleFamille.setNomPrenom(familleContribuable.getPersonneEtendue().getTiers().getDesignation1() + " "
                    + familleContribuable.getPersonneEtendue().getTiers().getDesignation2());
            simpleFamille.setNomPrenomUpper(simpleFamille.getNomPrenom().toUpperCase());
            String noAVS = familleContribuable.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
            if (noAVS == null) {
                noAVS = "";
            }
            String noAVSWithoutPoints = "";
            for (int i = 0; i < noAVS.length(); i++) {
                if (noAVS.charAt(i) != '.') {
                    noAVSWithoutPoints += noAVS.charAt(i);
                }
            }
            simpleFamille.setNoAVS(noAVSWithoutPoints);
            simpleFamille.setNnssContribuable(noAVSWithoutPoints);
            simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
            familleContribuable.setSimpleFamille(simpleFamille);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FamilleException("Service not available - " + e.getMessage());
        }
        // Contrôle si code fin et application aux subsides y relatifs
        checkDateFin(familleContribuable);

        return familleContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.famille.FamilleContribuableService#delete(ch.globaz.amal.business.models
     * .famille.FamilleContribuable)
     */
    @Override
    public FamilleContribuable delete(FamilleContribuable familleContribuable) throws FamilleException,
            JadePersistenceException {
        if (familleContribuable == null) {
            throw new FamilleException("Unable to delete FamilleContribuable, the given model is null!");
        }

        try {
            AmalImplServiceLocator.getSimpleFamilleService().delete(familleContribuable.getSimpleFamille());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FamilleException("Service not available - " + e.getMessage());
            // e.printStackTrace();
        }
        return familleContribuable;
    }

    @Override
    public ArrayList<Contribuable> famillyListSubside(String idContribuable, String year)
            throws JadePersistenceException, FamilleException {
        ContribuableSearch search = new ContribuableSearch();
        search.setForIdContribuable(idContribuable);
        try {
            search = AmalServiceLocator.getContribuableService().search(search);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FamilleException("Service not available - " + e.getMessage());
        } catch (ContribuableException e) {
            throw new FamilleException(
                    "FamilleContribuableServiceImpl.famillyListSubside() : Error while searching familly member's ==> "
                            + e.getMessage());
        }

        // Create new empty table
        ArrayList<Contribuable> returnArray = new ArrayList<Contribuable>();
        // Gets the family member
        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            // Fill the famille
            Contribuable contribuable = (Contribuable) it.next();
            returnArray.add(contribuable);
        }
        return returnArray;
    }

    @Override
    public SimpleFamilleSearch getFamilleByAVS(SimpleFamilleSearch currentFamilleSearch, String searchedAVS) {
        String currentAVS = searchedAVS;

        if (!JadeStringUtil.isEmpty(currentAVS) && (currentFamilleSearch != null)) {

            if (currentFamilleSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentFamilleSearch = new SimpleFamilleSearch();
                currentFamilleSearch.setLikeNoAVS(JadeStringUtil.change(currentAVS, ".", ""));
                currentFamilleSearch.setDefinedSearchSize(0);
                try {
                    currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(
                            currentFamilleSearch);
                    return currentFamilleSearch;
                } catch (Exception ex) {
                    return currentFamilleSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS FAMILLE " + currentFamilleSearch.getSize()
                        + "DEJA PRESENT POUR LE NUMERO AVS : " + currentAVS);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentFamilleSearch.getSize(); iResult++) {
                    SimpleFamille currentFamille = (SimpleFamille) currentFamilleSearch.getSearchResults()[iResult];
                    if (currentFamille.getNoAVS().equals(currentAVS)) {
                        results.add(currentFamille);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentFamilleSearch.setSearchResults(resultsArray);
                return currentFamilleSearch;
            }
        } else {
            return currentFamilleSearch;
        }
    }

    @Override
    public SimpleFamilleSearch getFamilleByDateOfBirth(SimpleFamilleSearch currentFamilleSearch,
            String dateNaissanceYYYYMMDD) {
        String currentDateNaissance = dateNaissanceYYYYMMDD;
        try {
            JADate currentJADate = JADate.newDateFromAMJ(currentDateNaissance);
            currentDateNaissance = JACalendar.format(currentJADate);
        } catch (Exception ex) {
            JadeLogger.error(null, "Error converting date : " + currentDateNaissance);
            currentDateNaissance = "";
        }
        if (!JadeStringUtil.isEmpty(currentDateNaissance) && (currentFamilleSearch != null)) {

            if (currentFamilleSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentFamilleSearch = new SimpleFamilleSearch();
                currentFamilleSearch.setForDateNaissance(currentDateNaissance);
                currentFamilleSearch.setDefinedSearchSize(0);
                try {
                    currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(
                            currentFamilleSearch);
                    return currentFamilleSearch;
                } catch (Exception ex) {
                    return currentFamilleSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS FAMILLE " + currentFamilleSearch.getSize()
                        + "DEJA PRESENT POUR LA DATE DE NAISSANCE : " + currentDateNaissance);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentFamilleSearch.getSize(); iResult++) {
                    SimpleFamille currentFamille = (SimpleFamille) currentFamilleSearch.getSearchResults()[iResult];
                    if (currentFamille.getDateNaissance().equals(currentDateNaissance)) {
                        results.add(currentFamille);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentFamilleSearch.setSearchResults(resultsArray);
                return currentFamilleSearch;
            }
        } else {
            return currentFamilleSearch;
        }
    }

    @Override
    public SimpleFamilleSearch getFamilleByFamilyNameGivenName(SimpleFamilleSearch currentFamilleSearch,
            String searchedFamilyName, String searchedGivenName) {
        String currentName = "";

        if (!JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            currentName = searchedFamilyName + " " + searchedGivenName;
        }
        if (!JadeStringUtil.isEmpty(currentName) && (currentFamilleSearch != null)) {

            if (currentFamilleSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentFamilleSearch = new SimpleFamilleSearch();
                currentFamilleSearch.setLikeNomPrenom(currentName);
                currentFamilleSearch.setDefinedSearchSize(0);
                try {
                    currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(
                            currentFamilleSearch);
                    return currentFamilleSearch;
                } catch (Exception ex) {
                    return currentFamilleSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS FAMILLE " + currentFamilleSearch.getSize()
                        + "DEJA PRESENT POUR LE NOM : " + currentName);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentFamilleSearch.getSize(); iResult++) {
                    SimpleFamille currentFamille = (SimpleFamille) currentFamilleSearch.getSearchResults()[iResult];
                    if (currentFamille.getNomPrenom().toUpperCase().equals(currentName.toUpperCase())) {
                        results.add(currentFamille);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentFamilleSearch.setSearchResults(resultsArray);
                return currentFamilleSearch;
            }
        } else {
            return currentFamilleSearch;
        }
    }

    @Override
    public SimpleFamilleSearch getFamilleByNSS(SimpleFamilleSearch currentFamilleSearch, String searchedNSS) {
        String currentNSS = searchedNSS;

        if (!JadeStringUtil.isEmpty(currentNSS) && (currentFamilleSearch != null)) {

            if (currentFamilleSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentFamilleSearch = new SimpleFamilleSearch();
                currentFamilleSearch.setLikeNoAVS(JadeStringUtil.change(currentNSS, ".", ""));
                currentFamilleSearch.setDefinedSearchSize(0);
                try {
                    currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(
                            currentFamilleSearch);
                    return currentFamilleSearch;
                } catch (Exception ex) {
                    return currentFamilleSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS FAMILLE " + currentFamilleSearch.getSize()
                        + "DEJA PRESENT POUR LE NUMERO NSS : " + currentNSS);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentFamilleSearch.getSize(); iResult++) {
                    SimpleFamille currentFamille = (SimpleFamille) currentFamilleSearch.getSearchResults()[iResult];
                    if (currentFamille.getNoAVS().equals(currentNSS)) {
                        results.add(currentFamille);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentFamilleSearch.setSearchResults(resultsArray);
                return currentFamilleSearch;
            }
        } else {
            return currentFamilleSearch;
        }
    }

    @Override
    public ArrayList<FamilleContribuableView> getListSubsideMember(String year, String idContribuable)
            throws JadePersistenceException, FamilleException, JadeApplicationServiceNotAvailableException {

        // List<FamilleContribuableView> listeFamilleContribuableView = new ArrayList<FamilleContribuableView>();
        // Get all subsides informations by year
        Map<String, List<FamilleContribuableView>> listeFamilleContribuableViewAnnee = new HashMap<String, List<FamilleContribuableView>>();
        // Get all subsides informations by family member
        // Map<String, List<FamilleContribuableView>> listeFamilleContribuableViewMember = new HashMap<String,
        // List<FamilleContribuableView>>();
        // Retrieve Detail Famille
        FamilleContribuableViewSearch famillesearch = new FamilleContribuableViewSearch();
        // Set searched parameters
        famillesearch.setForIdContribuable(idContribuable);
        famillesearch.setForAnneeHistorique(year);
        // Set order by AnneeHistorique DESC, PereMereEnfant ASC
        famillesearch.setOrderKey("revenuHistorique");
        // Perform search
        famillesearch = AmalServiceLocator.getFamilleContribuableService().search(famillesearch);

        ArrayList<FamilleContribuableView> arrayListMembreSubsides = new ArrayList<FamilleContribuableView>();
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();

        for (JadeAbstractModel model : famillesearch.getSearchResults()) {
            FamilleContribuableView familleContribuableView = (FamilleContribuableView) model;
            String codeTraitementDossier = familleContribuableView.getCodeTraitementDossier();
            String noModele = familleContribuableView.getNoModeles();
            String typeDemande = familleContribuableView.getTypeDemande();
            familleContribuableView.setCodeTraitementDossierLibelleAJAX(currentSession
                    .getCodeLibelle(codeTraitementDossier));
            familleContribuableView.setCodeTraitementDossierCodeAJAX(currentSession.getCode(codeTraitementDossier));
            familleContribuableView.setNoModelesLibelleAJAX(currentSession.getCodeLibelle(noModele));
            String idCode = currentSession.getCode(noModele);
            familleContribuableView.setNoModelesCodeAJAX(idCode);
            familleContribuableView.setTypeDemandeLibelleAJAX(currentSession.getCodeLibelle(typeDemande));
            familleContribuableView.setTypeDemandeCodeAJAX(currentSession.getCode(typeDemande));

            try {
                Double montantBase = Double.valueOf(familleContribuableView.getMontantContribution());
                Double supplExtra = Double.valueOf(familleContribuableView.getSupplExtra());
                familleContribuableView.setMontantTotalSubsideAJAX(JANumberFormatter.fmt(
                        String.valueOf(montantBase + supplExtra), true, true, false, 2));
            } catch (NumberFormatException nfe) {
                familleContribuableView.setMontantTotalSubsideAJAX(String.valueOf(familleContribuableView
                        .getMontantContribution()) + " / " + String.valueOf(familleContribuableView.getSupplExtra()));
            }

            if (!JadeStringUtil.isBlankOrZero(idCode)) {
                try {
                    FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

                    cm.setSession(currentSession);
                    cm.setForCodeUtilisateur(idCode);
                    cm.setForIdGroupe("AMMODELES");
                    cm.setForIdLangue(currentSession.getIdLangue());
                    cm.find();

                    FWParametersCode code = (FWParametersCode) cm.getEntity(0);
                    familleContribuableView.setNoModelesAbreviationAJAX(code.getLibelle());

                } catch (Exception e) {
                    familleContribuableView.setNoModelesAbreviationAJAX("");
                }
            } else {
                familleContribuableView.setNoModelesAbreviationAJAX("");
            }

            // //
            try {
                ComplexControleurEnvoiDetailSearch complexControleurEnvoiDetailSearch = new ComplexControleurEnvoiDetailSearch();
                complexControleurEnvoiDetailSearch.setForAnneeHistorique(familleContribuableView.getAnneeHistorique());
                complexControleurEnvoiDetailSearch.setForIdFamille(familleContribuableView.getFamilleId());
                ArrayList<String> inStatusEnvoi = new ArrayList<String>();
                inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue());
                inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
                inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
                inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
                complexControleurEnvoiDetailSearch.setInStatusEnvoi(inStatusEnvoi);
                complexControleurEnvoiDetailSearch = AmalServiceLocator.getControleurEnvoiService().search(
                        complexControleurEnvoiDetailSearch);

                if (complexControleurEnvoiDetailSearch.getSize() > 0) {
                    int size = complexControleurEnvoiDetailSearch.getSize();
                    ComplexControleurEnvoiDetail complexControleurEnvoiDetail = (ComplexControleurEnvoiDetail) complexControleurEnvoiDetailSearch
                            .getSearchResults()[size - 1];

                    String noModeleTemp = complexControleurEnvoiDetail.getNumModele();

                    String idCodeTemp = currentSession.getCode(noModeleTemp);

                    if (!JadeStringUtil.isBlankOrZero(idCodeTemp)) {
                        try {
                            FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

                            cm.setSession(currentSession);
                            cm.setForCodeUtilisateur(idCodeTemp);
                            cm.setForIdGroupe("AMMODELES");
                            cm.setForIdLangue(currentSession.getIdLangue());
                            cm.find();

                            FWParametersCode code = (FWParametersCode) cm.getEntity(0);
                            familleContribuableView.setNoModelesTemporaireAbreviationAJAX(code.getLibelle());

                        } catch (Exception e) {
                            familleContribuableView.setNoModelesTemporaireAbreviationAJAX("");
                        }
                    } else {
                        familleContribuableView.setNoModelesTemporaireAbreviationAJAX("");
                    }

                    familleContribuableView.setNoModelesTemporaireCodeAJAX(idCodeTemp);
                    familleContribuableView.setNoModelesTemporaireLibelleAJAX(currentSession
                            .getCodeLibelle(noModeleTemp));
                    familleContribuableView.setIdDetailFamilleAJAX(complexControleurEnvoiDetail.getIdDetailFamille());
                } else {
                    familleContribuableView.setNoModelesTemporaireCodeAJAX("");
                    familleContribuableView.setNoModelesTemporaireLibelleAJAX("");
                    familleContribuableView.setNoModelesTemporaireAbreviationAJAX("");
                    familleContribuableView.setIdDetailFamilleAJAX("");
                }
            } catch (Exception e) {
                // On ne fait rien, pas bloquant
            }
            // //

            arrayListMembreSubsides.add(familleContribuableView);
        }

        return arrayListMembreSubsides;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.famille.FamilleContribuableService#read(java.lang.String)
     */
    @Override
    public FamilleContribuable read(String idFamilleContribuable) throws JadePersistenceException, FamilleException {
        if (JadeStringUtil.isEmpty(idFamilleContribuable)) {
            throw new FamilleException("Unable to read familleContribuable, the id passed is null!");
        }
        FamilleContribuable familleContribuable = new FamilleContribuable();
        familleContribuable.setId(idFamilleContribuable);
        familleContribuable = (FamilleContribuable) JadePersistenceManager.read(familleContribuable);
        // Synchronize with tiers : read simple famille
        try {
            SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                    familleContribuable.getSimpleFamille().getId());
            familleContribuable.setSimpleFamille(currentFamille);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return familleContribuable;
        // return (FamilleContribuable) JadePersistenceManager.read(familleContribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#search(ch
     * .globaz.amal.business.models.revenu.RevenuSearch)
     */
    @Override
    public FamilleContribuableSearch search(FamilleContribuableSearch familleContribuableSearch)
            throws JadePersistenceException, FamilleException {
        if (familleContribuableSearch == null) {
            throw new FamilleException("Unable to search dossier, the search model passed is null!");
        }
        return (FamilleContribuableSearch) JadePersistenceManager.search(familleContribuableSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#search(ch
     * .globaz.amal.business.models.revenu.RevenuSearch)
     */
    @Override
    public FamilleContribuableViewSearch search(FamilleContribuableViewSearch familleContribuableViewSearch)
            throws JadePersistenceException, FamilleException {
        if (familleContribuableViewSearch == null) {
            throw new FamilleException("Unable to search dossier, the search model passed is null!");
        }
        return (FamilleContribuableViewSearch) JadePersistenceManager.search(familleContribuableViewSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.famille.FamilleContribuableService#search(ch.globaz.amal.business.models
     * .famille.SimpleFamilleSearch)
     */
    @Override
    public SimpleFamilleSearch search(SimpleFamilleSearch simpleFamilleSearch) throws JadePersistenceException,
            FamilleException {
        if (simpleFamilleSearch == null) {
            throw new FamilleException("Unable to search simpleFamille, the given model is null!");
        }

        try {
            return AmalImplServiceLocator.getSimpleFamilleService().search(simpleFamilleSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FamilleException("Service not available - " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.famille.FamilleContribuableService#update(ch.globaz.amal.business.models
     * .famille.FamilleContribuable)
     */
    @Override
    public FamilleContribuable update(FamilleContribuable familleContribuable) throws JadePersistenceException,
            FamilleException {
        if (familleContribuable == null) {
            throw new FamilleException("Unable to update FamilleContribuable, the given model is null!");
        }

        // Création du tiers si nécessaire
        if (JadeStringUtil.isBlankOrZero(familleContribuable.getPersonneEtendue().getTiers().getIdTiers())
                && !JadeStringUtil.isBlankOrZero(familleContribuable.getPersonneEtendue().getTiers().getDesignation1())
                && !JadeStringUtil.isBlankOrZero(familleContribuable.getPersonneEtendue().getTiers().getDesignation2())) {

            AMGestionTiers gestionTiers = new AMGestionTiers();
            try {
                gestionTiers.createTiersContribuable(familleContribuable);
            } catch (ContribuableException e) {
                throw new FamilleException("Error while creating Tiers in FamilleContribuable().update() - "
                        + e.getMessage());
            }
        }

        try {
            if (!familleContribuable.getSimpleFamille().getIdTier()
                    .equals(familleContribuable.getPersonneEtendue().getTiers().getIdTiers())) {
                familleContribuable.getSimpleFamille().setIdTier(
                        familleContribuable.getPersonneEtendue().getTiers().getIdTiers());
                if (familleContribuable.getSimpleFamille().getIsContribuable()) {
                    SimpleContribuable contribuable = new SimpleContribuable();
                    contribuable = AmalImplServiceLocator.getSimpleContribuableService().read(
                            familleContribuable.getSimpleFamille().getIdContribuable());
                    if (!contribuable.isNew()) {
                        contribuable.setIdTier(familleContribuable.getPersonneEtendue().getTiers().getIdTiers());
                        contribuable = AmalImplServiceLocator.getSimpleContribuableService().update(contribuable);
                    }
                }
            }
        } catch (Exception cex) {
            throw new FamilleException("Error while update idTiers of contribuable - " + cex.getMessage());
        }

        SimpleFamille simpleFamille = new SimpleFamille();
        simpleFamille = familleContribuable.getSimpleFamille();

        if (JadeStringUtil.isBlank(simpleFamille.getNomPrenom())) {
            simpleFamille.setNomPrenom(familleContribuable.getPersonneEtendue().getTiers().getDesignation1() + ' '
                    + familleContribuable.getPersonneEtendue().getTiers().getDesignation2());
            simpleFamille.setNomPrenomUpper(familleContribuable.getPersonneEtendue().getTiers().getDesignation1()
                    .toUpperCase()
                    + ' ' + familleContribuable.getPersonneEtendue().getTiers().getDesignation2().toUpperCase());
        }

        if (JadeStringUtil.isBlank(simpleFamille.getDateNaissance())) {
            simpleFamille.setDateNaissance(familleContribuable.getPersonneEtendue().getPersonne().getDateNaissance());
        }

        if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getIdTier())) {
            familleContribuable.getSimpleFamille().setIdTier(
                    familleContribuable.getPersonneEtendue().getTiers().getIdTiers());
        }

        try {
            // On récupère son état initial pour vérifier si il était contribuable principal à la base
            SimpleFamilleSearch sfSearchRead = new SimpleFamilleSearch();
            sfSearchRead.setForIdFamille(simpleFamille.getIdFamille());
            sfSearchRead = AmalImplServiceLocator.getSimpleFamilleService().search(sfSearchRead);

            if (sfSearchRead.getSize() != 1) {
                throw new JadeApplicationServiceNotAvailableException("Error searching familly member no : "
                        + simpleFamille.getIdFamille() + "!");
            }
            SimpleFamille sfRead = (SimpleFamille) sfSearchRead.getSearchResults()[0];
            boolean wasContribuablePrincipal = sfRead.getIsContribuable();

            // Si il l'était et qu'il ne l'est plus, erreur, il doit y avoir un contribuable principal
            if (wasContribuablePrincipal && !simpleFamille.getIsContribuable()) {
                throw new FamilleException(
                        "Il doit y avoir au moins 1 contribuable principal ! Pour changer de contribuable principal, il faut déclarer un autre membre comme étant le contribuable principal");
            }

            // Si il ne l'était pas et qu'il l'est désormais...
            if (!wasContribuablePrincipal && simpleFamille.getIsContribuable()) {
                SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
                simpleFamilleSearch.setForIdContribuable(simpleFamille.getIdContribuable());
                simpleFamilleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(simpleFamilleSearch);

                for (JadeAbstractModel model : simpleFamilleSearch.getSearchResults()) {
                    SimpleFamille sf = (SimpleFamille) model;

                    // On set tout les autres comme n'étant pas le contribuable principal
                    if (!sf.getIdFamille().equals(simpleFamille.getIdFamille())) {
                        sf.setIsContribuable(false);
                        AmalImplServiceLocator.getSimpleFamilleService().update(sf);
                    }
                }

                SimpleContribuable contri = new SimpleContribuable();
                try {
                    contri = AmalImplServiceLocator.getSimpleContribuableService().read(
                            simpleFamille.getIdContribuable());
                    contri.setIdTier(simpleFamille.getIdTier());
                    contri = AmalImplServiceLocator.getSimpleContribuableService().update(contri);
                } catch (ContribuableException e) {
                    throw new FamilleException("Error reading contribuable for update contribuable.IdTiers ");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FamilleException("Service not available (Looking for all principals contribuables) - "
                    + e.getMessage());
        }

        try {
            AmalImplServiceLocator.getSimpleFamilleService().update(familleContribuable.getSimpleFamille());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FamilleException("Service not available - " + e.getMessage());
        }
        // Contrôle si code fin et application aux subsides y relatifs
        checkDateFin(familleContribuable);

        return familleContribuable;
    }
}
