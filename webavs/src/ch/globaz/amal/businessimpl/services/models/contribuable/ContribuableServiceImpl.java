/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.contribuable;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.JadeTarget;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TIHistoriqueContribuableManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.contribuable.ContribuableService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.AdresseSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author CBU
 * 
 */
public class ContribuableServiceImpl implements ContribuableService {
    private static final String CODE_MERE = "42001201";
    private static final String CODE_PERE = "42001200";

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #count(
     * ch.globaz.amal.business.models.contribuable.ContribuableSearch)
     */
    @Override
    public int count(ContribuableSearch search) throws ContribuableException, JadePersistenceException {
        if (search == null) {
            throw new ContribuableException("Unable to count contribuables, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #create
     * (ch.globaz.amal.business.models.contribuable.Contribuable)
     */
    @Override
    public Contribuable create(Contribuable contribuable) throws JadePersistenceException, ContribuableException,
            FamilleException {
        if (contribuable == null) {
            throw new ContribuableException("Unable to create contribuable, the given model is null!");
        }
        try {
            // Création du tiers si nécessaire
            if (JadeStringUtil.isBlankOrZero(contribuable.getPersonneEtendue().getTiers().getIdTiers())
                    && !JadeStringUtil.isBlankOrZero(contribuable.getPersonneEtendue().getTiers().getDesignation1())
                    && !JadeStringUtil.isBlankOrZero(contribuable.getPersonneEtendue().getTiers().getDesignation2())) {

                AMGestionTiers gestionTiers = new AMGestionTiers();
                gestionTiers.createTiersContribuable(contribuable);
            }
            // Create Simple Contribuable
            SimpleContribuable simpleContribuable = contribuable.getContribuable();

            simpleContribuable.setIdTier(contribuable.getPersonneEtendue().getTiers().getIdTiers());
            simpleContribuable.setIsContribuableActif(true);
            simpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().create(simpleContribuable);
            contribuable.setContribuable(simpleContribuable);

            if (!contribuable.isContribuableHistorique()) {
                // Create Simple Famille
                SimpleFamille simpleFamille = contribuable.getFamille();
                simpleFamille.setIdContribuable(simpleContribuable.getIdContribuable());
                simpleFamille.setIdTier(contribuable.getPersonneEtendue().getTiers().getIdTiers());
                simpleFamille.setIsContribuable(true);
                if ("516002".equals(contribuable.getPersonneEtendue().getPersonne().getSexe())) {
                    simpleFamille.setPereMereEnfant(ContribuableServiceImpl.CODE_MERE);
                } else {
                    simpleFamille.setPereMereEnfant(ContribuableServiceImpl.CODE_PERE);
                }
                simpleFamille.setDateNaissance(contribuable.getPersonneEtendue().getPersonne().getDateNaissance());
                simpleFamille.setNomPrenom(contribuable.getPersonneEtendue().getTiers().getDesignation1() + " "
                        + contribuable.getPersonneEtendue().getTiers().getDesignation2());
                simpleFamille.setNomPrenomUpper(simpleFamille.getNomPrenom().toUpperCase());
                String noAVS = contribuable.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                if (!JadeStringUtil.isEmpty(noAVS) && (noAVS.indexOf(".") >= 0)) {
                    String noAVSWithoutPoints = "";
                    for (int i = 0; i < noAVS.length(); i++) {
                        if (noAVS.charAt(i) != '.') {
                            noAVSWithoutPoints += noAVS.charAt(i);
                        }
                    }
                    simpleFamille.setNoAVS(noAVSWithoutPoints);
                    simpleFamille.setNnssContribuable(noAVSWithoutPoints);
                }
                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().create(simpleFamille);
                contribuable.setFamille(simpleFamille);
            } else {
                SimpleFamilleSearch familleSearch = AmalImplServiceLocator.getSimpleFamilleService().getChefDeFamille(
                        contribuable.getId());
                SimpleFamille simpleFamille = (SimpleFamille) familleSearch.getSearchResults()[0];
                simpleFamille.setIdTier(contribuable.getPersonneEtendue().getTiers().getIdTiers());

                if ("516002".equals(contribuable.getPersonneEtendue().getPersonne().getSexe())) {
                    simpleFamille.setPereMereEnfant(ContribuableServiceImpl.CODE_MERE);
                } else {
                    simpleFamille.setPereMereEnfant(ContribuableServiceImpl.CODE_PERE);
                }

                if (JAUtil.isDateEmpty(simpleFamille.getDateNaissance())) {
                    simpleFamille.setDateNaissance(contribuable.getPersonneEtendue().getPersonne().getDateNaissance());
                }

                simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().update(simpleFamille);
                contribuable.setFamille(simpleFamille);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ContribuableException("Service not available - " + e.getMessage());
        }

        // PROPAGATION GED (création du dossier GED)
        try {
            if (JadeGedFacade.isInstalled()) {
                // TODO : target ged >> paramètre applicatif
                JadeTarget target = JadeGedFacade.getInstance().getTarget("gedos");
                if (globaz.jade.ged.adapter.gedos.JadeGedAdapter.class.isAssignableFrom(target.getClass())) {
                    Properties properties = new Properties();
                    String selectednoContribuable = JadeStringUtil.removeChar(contribuable.getPersonneEtendue()
                            .getPersonneEtendue().getNumContribuableActuel(), '.');
                    selectednoContribuable = JadeStringUtil.removeChar(selectednoContribuable, '/');
                    if (!JadeStringUtil.isEmpty(selectednoContribuable)) {
                        properties.setProperty("N_CTB", selectednoContribuable);
                        String selectedNSS = JadeStringUtil.removeChar(contribuable.getPersonneEtendue()
                                .getPersonneEtendue().getNumAvsActuel(), '.');
                        if (!JadeStringUtil.isEmpty(selectedNSS)) {
                            properties.setProperty("NNSS_", selectedNSS);
                        } else {
                            properties.setProperty("NNSS_", "");
                        }
                        JadeGedFacade.propagate(properties);
                    }
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error propagating GED : " + ex.getMessage());
            ex.printStackTrace();
            // throw new ContribuableException("Exception propagating GED - " + ex.getMessage());
        }

        return contribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #delete
     * (ch.globaz.amal.business.models.contribuable.Contribuable)
     */
    @Override
    public Contribuable delete(Contribuable contribuable) throws ContribuableException, JadePersistenceException {
        if (contribuable == null) {
            throw new ContribuableException("Unable to delete dossier, the given model is null!");
        } else {
            throw new ContribuableException("Unable to delete dossier, the given model is null!");
        }

        // return contribuable;
    }

    @Override
    public SimpleContribuableInfos deleteInfo(SimpleContribuableInfos contribuableInfo) throws ContribuableException,
            JadePersistenceException {
        if (contribuableInfo == null) {
            throw new ContribuableException("Unable to delete contribuableInfos, the given model is null!");
        }
        contribuableInfo.setIsTransfered(true);

        return (SimpleContribuableInfos) JadePersistenceManager.update(contribuableInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #getContribuableAdresse(java
     * .lang.String)
     */
    @Override
    public AdresseComplexModel getContribuableAdresse(String idTiers) throws JadePersistenceException {
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return null;
        }

        try {
            AdresseTiersDetail currentAdresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers,
                    true, JadeDateUtil.getGlobazFormattedDate(new Date()), AMGestionTiers.CS_DOMAINE_AMAL,
                    AMGestionTiers.CS_TYPE_COURRIER, null);

            if (!(currentAdresse.getAdresseFormate() == null)) {
                String idAdresse = currentAdresse.getFields().get(AdresseTiersDetail.ADRESSE_ID_ADRESSE);
                String designation1 = currentAdresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1);

                AdresseSearchComplexModel adresseSearch = new AdresseSearchComplexModel();
                adresseSearch.setForIdAdresseInterneUnique(idAdresse);
                adresseSearch.setForIdTiers(idTiers);
                adresseSearch = TIBusinessServiceLocator.getAdresseService().findAdresse(adresseSearch);
                for (int iAdresse = 0; iAdresse < adresseSearch.getSize(); iAdresse++) {
                    AdresseComplexModel currentAdresseComplex = (AdresseComplexModel) adresseSearch.getSearchResults()[iAdresse];
                    // CBU : Test retiré pour éviter qu'une adresse avec date de fin dans le futur ne soit pas prise en
                    // compte (I121015_000017)
                    // if (JadeStringUtil.isEmpty(currentAdresseComplex.getAvoirAdresse().getDateFinRelation())) {
                    return currentAdresseComplex;
                    // }

                }
            }
            // if (adresseSearch.getSize() == 1) {
            // return (AdresseComplexModel) adresseSearch.getSearchResults()[0];
            // }
        } catch (Exception ex) {
            JadeLogger.error(this, "Error loading adresse for tiers " + idTiers + " - " + ex.toString());
        }

        // Get the adresse of the contribuable
        // PersonneEtendueAdresseSearchComplexModel personneAdrSearch = new PersonneEtendueAdresseSearchComplexModel();
        // personneAdrSearch.setForIdTiers(idTiers);
        // personneAdrSearch.setForIdApplication(AMGestionTiers.CS_DOMAINE_AMAL);
        // personneAdrSearch.setForTypeAdresse(AMGestionTiers.CS_TYPE_COURRIER);
        // try {
        // personneAdrSearch = TIBusinessServiceLocator.getPersonneEtendueService().findAdresse(personneAdrSearch);
        // for (int iPersonneAdr = 0; iPersonneAdr < personneAdrSearch.getSize(); iPersonneAdr++) {
        // PersonneEtendueAdresseComplexModel currentPersonneAdr = (PersonneEtendueAdresseComplexModel)
        // personneAdrSearch
        // .getSearchResults()[iPersonneAdr];
        //
        // AdresseSearchComplexModel adrSearch = new AdresseSearchComplexModel();
        // adrSearch.setForIdAdresseInterneUnique(currentPersonneAdr.getAvoirAdresse().getIdAdresseIntUnique());
        // adrSearch = TIBusinessServiceLocator.getAdresseService().findAdresse(adrSearch);
        // if (adrSearch.getSize() > 0) {
        // return (AdresseComplexModel) adrSearch.getSearchResults()[0];
        // }
        //
        // }
        //
        // } catch (JadeApplicationException e) {
        // JadeLogger.error(this, "Error loading adresse for tiers " + idTiers);
        // }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService
     * #getContribuableHistoriqueNoContribuable(java .lang.String)
     */
    @Override
    public ArrayList<String> getContribuableHistoriqueNoContribuable(String idTiers) {

        ArrayList<String> returnArray = new ArrayList<String>();
        TIHistoriqueContribuableManager histComMng = new TIHistoriqueContribuableManager();
        histComMng.setForIdTiers(idTiers);
        try {
            histComMng.find();
            for (int i = 0; i < histComMng.size(); i++) {
                TIHistoriqueContribuable entity = ((TIHistoriqueContribuable) histComMng.getEntity(i));
                if (!returnArray.contains(entity.getNumContribuable())) {
                    returnArray.add(entity.getNumContribuable());
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error searching histo no contribuable (" + idTiers + ") : " + e.toString());
            e.printStackTrace();
        }

        return returnArray;
    }

    @Override
    public ContribuableHistoriqueRCListeSearch getDossierByAVS(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedAVS) {
        String currentAVS = searchedAVS;

        if (!JadeStringUtil.isEmpty(currentAVS) && (currentContribuableHistoriqueRCListeSearch != null)) {

            if (currentContribuableHistoriqueRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();
                currentContribuableHistoriqueRCListeSearch.setLikeNss(currentAVS);
                currentContribuableHistoriqueRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .searchHistoriqueRCListe(currentContribuableHistoriqueRCListeSearch);
                    return currentContribuableHistoriqueRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableHistoriqueRCListeSearch;
                }
            } else {
                JadeLogger.info(null,
                        "PLUSIEURS RESULTS HISTORIQUE" + currentContribuableHistoriqueRCListeSearch.getSize()
                                + "DEJA PRESENT POUR LE NUMERO AVS : " + currentAVS);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                    ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getNnssCtbInfo().equals(currentAVS)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableHistoriqueRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableHistoriqueRCListeSearch;
            }
        } else {
            return currentContribuableHistoriqueRCListeSearch;
        }
    }

    @Override
    public ContribuableRCListeSearch getDossierByAVS(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedAVS) {
        String currentAVS = searchedAVS;

        if (!JadeStringUtil.isEmpty(currentAVS) && (currentContribuableRCListeSearch != null)) {

            if (currentContribuableRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableRCListeSearch = new ContribuableRCListeSearch();
                currentContribuableRCListeSearch.setLikeNss(currentAVS);
                currentContribuableRCListeSearch.setIsContribuable(true);
                currentContribuableRCListeSearch.setForContribuableActif(true);
                currentContribuableRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().searchRCListe(
                            currentContribuableRCListeSearch);
                    return currentContribuableRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableRCListeSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS " + currentContribuableRCListeSearch.getSize()
                        + "DEJA PRESENT POUR LE NUMERO AVS : " + currentAVS);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                    ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getNumAvsActuel().equals(currentAVS)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableRCListeSearch;
            }
        } else {
            return currentContribuableRCListeSearch;
        }
    }

    @Override
    public ContribuableHistoriqueRCListeSearch getDossierByDateOfBirth(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String dateNaissanceYYYYMMDD) {
        String currentDateNaissance = dateNaissanceYYYYMMDD;
        if (!JadeDateUtil.isGlobazDate(currentDateNaissance)) {
            try {
                JADate currentJADate = JADate.newDateFromAMJ(currentDateNaissance);
                currentDateNaissance = JACalendar.format(currentJADate);
            } catch (Exception ex) {
                JadeLogger.error(null, "Error converting date : " + currentDateNaissance);
                currentDateNaissance = "";
            }
        }

        if (!JadeStringUtil.isEmpty(currentDateNaissance) && (currentContribuableHistoriqueRCListeSearch != null)) {

            if (currentContribuableHistoriqueRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();
                currentContribuableHistoriqueRCListeSearch.setForDateNaissance(currentDateNaissance);
                currentContribuableHistoriqueRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .searchHistoriqueRCListe(currentContribuableHistoriqueRCListeSearch);
                    return currentContribuableHistoriqueRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableHistoriqueRCListeSearch;
                }
            } else {
                JadeLogger.info(null,
                        "PLUSIEURS RESULTS HISTORIQUE" + currentContribuableHistoriqueRCListeSearch.getSize()
                                + " DEJA PRESENT POUR LA DATE NAISSANCE : " + currentDateNaissance);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                    ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getDateNaissanceCtbInfo().equals(currentDateNaissance)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableHistoriqueRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableHistoriqueRCListeSearch;
            }
        } else {
            return currentContribuableHistoriqueRCListeSearch;
        }
    }

    @Override
    public ContribuableRCListeSearch getDossierByDateOfBirth(
            ContribuableRCListeSearch currentContribuableRCListeSearch, String dateNaissanceYYYYMMDD) {
        String currentDateNaissance = dateNaissanceYYYYMMDD;

        if (!JadeDateUtil.isGlobazDate(currentDateNaissance)) {
            try {
                JADate currentJADate = JADate.newDateFromAMJ(currentDateNaissance);
                currentDateNaissance = JACalendar.format(currentJADate);
            } catch (Exception ex) {
                JadeLogger.error(null, "Error converting date : " + currentDateNaissance);
                currentDateNaissance = "";
            }
        }

        if (!JadeStringUtil.isEmpty(currentDateNaissance) && (currentContribuableRCListeSearch != null)) {

            if (currentContribuableRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableRCListeSearch = new ContribuableRCListeSearch();
                currentContribuableRCListeSearch.setForDateNaissance(currentDateNaissance);
                currentContribuableRCListeSearch.setIsContribuable(true);
                currentContribuableRCListeSearch.setForContribuableActif(true);
                currentContribuableRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().searchRCListe(
                            currentContribuableRCListeSearch);
                    return currentContribuableRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableRCListeSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS " + currentContribuableRCListeSearch.getSize()
                        + " DEJA PRESENT POUR LA DATE NAISSANCE : " + currentDateNaissance);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                    ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getDateNaissance().equals(currentDateNaissance)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableRCListeSearch;
            }
        } else {
            return currentContribuableRCListeSearch;
        }
    }

    @Override
    public ContribuableHistoriqueRCListeSearch getDossierByFamilyName(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedName) {
        String currentNom = searchedName.toUpperCase();

        if (!JadeStringUtil.isEmpty(currentNom) && (currentContribuableHistoriqueRCListeSearch != null)) {

            if (currentContribuableHistoriqueRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();
                currentContribuableHistoriqueRCListeSearch.setLikeNom(currentNom);
                currentContribuableHistoriqueRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .searchHistoriqueRCListe(currentContribuableHistoriqueRCListeSearch);
                    return currentContribuableHistoriqueRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableHistoriqueRCListeSearch;
                }
            } else {
                JadeLogger.info(null,
                        "PLUSIEURS RESULTS HISTORIQUE" + currentContribuableHistoriqueRCListeSearch.getSize()
                                + " DEJA PRESENT POUR LE NOM DE FAMILLE : " + currentNom);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                    ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getNomCtbInfoUpper().equals(currentNom)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableHistoriqueRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableHistoriqueRCListeSearch;
            }
        } else {
            return currentContribuableHistoriqueRCListeSearch;
        }
    }

    @Override
    public ContribuableRCListeSearch getDossierByFamilyName(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedName) {
        String currentNom = searchedName.toUpperCase();

        if (!JadeStringUtil.isEmpty(currentNom) && (currentContribuableRCListeSearch != null)) {

            if (currentContribuableRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableRCListeSearch = new ContribuableRCListeSearch();
                currentContribuableRCListeSearch.setLikeNom(currentNom);
                currentContribuableRCListeSearch.setIsContribuable(true);
                currentContribuableRCListeSearch.setForContribuableActif(true);
                currentContribuableRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().searchRCListe(
                            currentContribuableRCListeSearch);
                    return currentContribuableRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableRCListeSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS " + currentContribuableRCListeSearch.getSize()
                        + " DEJA PRESENT POUR LE NOM DE FAMILLE : " + currentNom);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                    ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getDesignationUpper1().equals(currentNom)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableRCListeSearch;
            }
        } else {
            return currentContribuableRCListeSearch;
        }
    }

    @Override
    public ContribuableHistoriqueRCListeSearch getDossierByGivenName(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedName) {
        String currentPrenom = searchedName.toUpperCase();

        if (!JadeStringUtil.isEmpty(currentPrenom) && (currentContribuableHistoriqueRCListeSearch != null)) {

            if (currentContribuableHistoriqueRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();
                currentContribuableHistoriqueRCListeSearch.setLikePrenom(currentPrenom);
                currentContribuableHistoriqueRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .searchHistoriqueRCListe(currentContribuableHistoriqueRCListeSearch);
                    return currentContribuableHistoriqueRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableHistoriqueRCListeSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS " + currentContribuableHistoriqueRCListeSearch.getSize()
                        + " DEJA PRESENT POUR LE PRENOM : " + currentPrenom);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                    ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getPrenomCtbInfoUpper().equals(currentPrenom)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableHistoriqueRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableHistoriqueRCListeSearch;
            }
        } else {
            return currentContribuableHistoriqueRCListeSearch;
        }
    }

    @Override
    public ContribuableRCListeSearch getDossierByGivenName(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedName) {
        String currentPrenom = searchedName.toUpperCase();

        if (!JadeStringUtil.isEmpty(currentPrenom) && (currentContribuableRCListeSearch != null)) {

            if (currentContribuableRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableRCListeSearch = new ContribuableRCListeSearch();
                currentContribuableRCListeSearch.setLikePrenom(currentPrenom);
                currentContribuableRCListeSearch.setIsContribuable(true);
                currentContribuableRCListeSearch.setForContribuableActif(true);
                currentContribuableRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().searchRCListe(
                            currentContribuableRCListeSearch);
                    return currentContribuableRCListeSearch;
                } catch (Exception ex) {
                    return currentContribuableRCListeSearch;
                }
            } else {
                JadeLogger.info(null, "PLUSIEURS RESULTS " + currentContribuableRCListeSearch.getSize()
                        + " DEJA PRESENT POUR LE PRENOM : " + currentPrenom);
                // Déjà des résultats, on affine
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                    ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getDesignationUpper2().equals(currentPrenom)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableRCListeSearch;
            }
        } else {
            return currentContribuableRCListeSearch;
        }
    }

    @Override
    public ContribuableHistoriqueRCListeSearch getDossierByNoContribuable(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch,
            String searchedNoContribuable) {
        String currentNoContribuable = searchedNoContribuable;

        if (!JadeStringUtil.isEmpty(currentNoContribuable) && (currentContribuableHistoriqueRCListeSearch != null)) {

            if (currentContribuableHistoriqueRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();
                currentContribuableHistoriqueRCListeSearch.setForNoContribuable(currentNoContribuable);
                currentContribuableHistoriqueRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .searchHistoriqueRCListe(currentContribuableHistoriqueRCListeSearch);
                    return currentContribuableHistoriqueRCListeSearch;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return currentContribuableHistoriqueRCListeSearch;
                }
            } else {
                // Déjà des résultats, on affine
                JadeLogger.info(null,
                        "PLUSIEURS RESULTS HISTORIQUE" + currentContribuableHistoriqueRCListeSearch.getSize()
                                + " DEJA PRESENT POUR LE NO CONTRIBUABLE : " + currentNoContribuable);
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                    ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getNumContribuableCtbInfo().equals(currentNoContribuable)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableHistoriqueRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableHistoriqueRCListeSearch;
            }
        } else {
            return currentContribuableHistoriqueRCListeSearch;
        }
    }

    @Override
    public ContribuableRCListeSearch getDossierByNoContribuable(
            ContribuableRCListeSearch currentContribuableRCListeSearch, String searchedNoContribuable) {
        String currentNoContribuable = searchedNoContribuable;

        if (!JadeStringUtil.isEmpty(currentNoContribuable) && (currentContribuableRCListeSearch != null)) {

            if (currentContribuableRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableRCListeSearch = new ContribuableRCListeSearch();
                currentContribuableRCListeSearch.setForNoContribuable(currentNoContribuable);
                currentContribuableRCListeSearch.setIsContribuable(true);
                currentContribuableRCListeSearch.setForContribuableActif(true);
                currentContribuableRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().searchRCListe(
                            currentContribuableRCListeSearch);
                    return currentContribuableRCListeSearch;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return currentContribuableRCListeSearch;
                }
            } else {
                // Déjà des résultats, on affine
                JadeLogger.info(null, "PLUSIEURS RESULTS " + currentContribuableRCListeSearch.getSize()
                        + " DEJA PRESENT POUR LE NO CONTRIBUABLE : " + currentNoContribuable);
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                    ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getNumAvsActuel().equals(currentNoContribuable)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableRCListeSearch;
            }
        } else {
            return currentContribuableRCListeSearch;
        }
    }

    @Override
    public ContribuableHistoriqueRCListeSearch getDossierByNSS(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedNSS) {
        String currentNSS = searchedNSS;

        if (!JadeStringUtil.isEmpty(currentNSS) && (currentContribuableHistoriqueRCListeSearch != null)) {

            if (currentContribuableHistoriqueRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();
                currentContribuableHistoriqueRCListeSearch.setLikeNss(currentNSS);
                currentContribuableHistoriqueRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .searchHistoriqueRCListe(currentContribuableHistoriqueRCListeSearch);
                    return currentContribuableHistoriqueRCListeSearch;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return currentContribuableHistoriqueRCListeSearch;
                }
            } else {
                // Déjà des résultats, on affine
                JadeLogger.info(null,
                        "PLUSIEURS RESULTS HISTORIQUE" + currentContribuableHistoriqueRCListeSearch.getSize()
                                + " DEJA PRESENT POUR LE NSS : " + currentNSS);
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                    ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getNnssCtbInfo().equals(currentNSS)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableHistoriqueRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableHistoriqueRCListeSearch;
            }
        } else {
            return currentContribuableHistoriqueRCListeSearch;
        }
    }

    @Override
    public ContribuableRCListeSearch getDossierByNSS(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedNSS) {
        String currentNSS = searchedNSS;

        if (!JadeStringUtil.isEmpty(currentNSS) && (currentContribuableRCListeSearch != null)) {

            if (currentContribuableRCListeSearch.getSize() <= 0) {
                // Pas de résultat dans le searchmodel, on part à 0
                currentContribuableRCListeSearch = new ContribuableRCListeSearch();
                currentContribuableRCListeSearch.setLikeNss(currentNSS);
                currentContribuableRCListeSearch.setIsContribuable(true);
                currentContribuableRCListeSearch.setForContribuableActif(true);
                currentContribuableRCListeSearch.setDefinedSearchSize(0);
                try {
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().searchRCListe(
                            currentContribuableRCListeSearch);
                    return currentContribuableRCListeSearch;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return currentContribuableRCListeSearch;
                }
            } else {
                // Déjà des résultats, on affine
                JadeLogger.info(null, "PLUSIEURS RESULTS " + currentContribuableRCListeSearch.getSize()
                        + " DEJA PRESENT POUR LE NSS : " + currentNSS);
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                    ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getNumAvsActuel().equals(currentNSS)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableRCListeSearch;
            }
        } else {
            return currentContribuableRCListeSearch;
        }
    }

    @Override
    public ContribuableHistoriqueRCListeSearch getDossierLastSubside(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch) {

        if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
            // Recherche des derniers subsides par dossiers
            SimpleDetailFamille lastSubside = null;
            for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                        .getSearchResults()[iResult];
                String contribuableId = currentContribuable.getIdContribuableInfo();
                // récupération du dernier subside pour le dossier en cours
                SimpleDetailFamilleSearch subsideSearch = new SimpleDetailFamilleSearch();
                subsideSearch.setForIdContribuable(contribuableId);
                subsideSearch.setDefinedSearchSize(0);
                subsideSearch.setForCodeActif(true);
                try {
                    subsideSearch = AmalServiceLocator.getDetailFamilleService().search(subsideSearch);
                    for (int iSubside = 0; iSubside < subsideSearch.getSize(); iSubside++) {
                        SimpleDetailFamille currentSubside = (SimpleDetailFamille) subsideSearch.getSearchResults()[iSubside];
                        if (lastSubside == null) {
                            lastSubside = currentSubside;
                        } else {
                            int iLastAnnee = Integer.parseInt(lastSubside.getAnneeHistorique());
                            int iCurrentAnnee = Integer.parseInt(currentSubside.getAnneeHistorique());
                            if (iCurrentAnnee > iLastAnnee) {
                                lastSubside = currentSubside;
                            }
                        }
                    }
                } catch (Exception ex) {
                    JadeLogger.error(this,
                            "Exception searching subsides for id : " + contribuableId + "\n" + ex.getMessage());
                }
            }

            // Détermination des subsides les plus récents >> retrouve l'id contribuable
            if (lastSubside == null) {
                return currentContribuableHistoriqueRCListeSearch;
            } else {
                String idContribuableWinner = lastSubside.getIdContribuable();
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableHistoriqueRCListeSearch.getSize(); iResult++) {
                    ContribuableHistoriqueRCListe currentContribuable = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getIdContribuableInfo().equals(idContribuableWinner)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableHistoriqueRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableHistoriqueRCListeSearch;
            }
        } else {
            return currentContribuableHistoriqueRCListeSearch;
        }
    }

    @Override
    public ContribuableRCListeSearch getDossierLastSubside(ContribuableRCListeSearch currentContribuableRCListeSearch) {
        if (currentContribuableRCListeSearch.getSize() > 1) {
            // Recherche des derniers subsides par dossiers
            SimpleDetailFamille lastSubside = null;
            for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                        .getSearchResults()[iResult];
                String contribuableId = currentContribuable.getIdContribuable();
                // récupération du dernier subside pour le dossier en cours
                SimpleDetailFamilleSearch subsideSearch = new SimpleDetailFamilleSearch();
                subsideSearch.setForIdContribuable(contribuableId);
                subsideSearch.setDefinedSearchSize(0);
                subsideSearch.setForCodeActif(true);
                try {
                    subsideSearch = AmalServiceLocator.getDetailFamilleService().search(subsideSearch);
                    for (int iSubside = 0; iSubside < subsideSearch.getSize(); iSubside++) {
                        SimpleDetailFamille currentSubside = (SimpleDetailFamille) subsideSearch.getSearchResults()[iSubside];
                        if (lastSubside == null) {
                            lastSubside = currentSubside;
                        } else {
                            int iLastAnnee = Integer.parseInt(lastSubside.getAnneeHistorique());
                            int iCurrentAnnee = Integer.parseInt(currentSubside.getAnneeHistorique());
                            if (iCurrentAnnee > iLastAnnee) {
                                lastSubside = currentSubside;
                            }
                        }
                    }
                } catch (Exception ex) {
                    JadeLogger.error(this,
                            "Exception searching subsides for id : " + contribuableId + "\n" + ex.getMessage());
                }
            }

            // Détermination des subsides les plus récents >> retrouve l'id contribuable
            if (lastSubside == null) {
                return currentContribuableRCListeSearch;
            } else {
                String idContribuableWinner = lastSubside.getIdContribuable();
                List<JadeAbstractModel> results = new ArrayList<JadeAbstractModel>();
                for (int iResult = 0; iResult < currentContribuableRCListeSearch.getSize(); iResult++) {
                    ContribuableRCListe currentContribuable = (ContribuableRCListe) currentContribuableRCListeSearch
                            .getSearchResults()[iResult];
                    if (currentContribuable.getIdContribuable().equals(idContribuableWinner)) {
                        results.add(currentContribuable);
                    }
                }
                JadeAbstractModel[] resultsArray = new JadeAbstractModel[results.size()];
                for (int iResult = 0; iResult < results.size(); iResult++) {
                    resultsArray[iResult] = results.get(iResult);
                }
                currentContribuableRCListeSearch.setSearchResults(resultsArray);
                return currentContribuableRCListeSearch;
            }
        } else {
            return currentContribuableRCListeSearch;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #read(java .lang.String)
     */
    @Override
    public Contribuable read(String idContribuable) throws JadePersistenceException, ContribuableException,
            FamilleException, JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(idContribuable)) {
            throw new ContribuableException("Unable to read Contribuable, the id passed is null!");
        }
        // Create Contribuable Object
        // ---------------------------------------------------------------------------
        Contribuable currentContribuable = new Contribuable();

        // Read Simple Contribuable
        // ---------------------------------------------------------------------------
        SimpleContribuable currentSimpleContribuable = new SimpleContribuable();
        currentSimpleContribuable = AmalImplServiceLocator.getSimpleContribuableService().read(idContribuable);
        // currentSimpleContribuable.setId(idContribuable);
        // currentSimpleContribuable = (SimpleContribuable) JadePersistenceManager.read(currentSimpleContribuable);
        currentContribuable.setContribuable(currentSimpleContribuable);

        // Set Personne Etendue
        // ---------------------------------------------------------------------------
        if (currentSimpleContribuable.getIdTier() != null) {
            // Set Personne Etendue
            PersonneEtendueComplexModel personne = new PersonneEtendueComplexModel();
            personne.setId(currentSimpleContribuable.getIdTier());
            personne.getTiers().setIdTiers(currentSimpleContribuable.getIdTier());
            personne = (PersonneEtendueComplexModel) JadePersistenceManager.read(personne);
            currentContribuable.setPersonneEtendueComplexModel(personne);
            // Set the adresse
            currentContribuable.setAdresseComplexModel(getContribuableAdresse(currentSimpleContribuable.getIdTier()));
            // Set the historique numero contribuable
            currentContribuable
                    .setHistoNumeroContribuable(getContribuableHistoriqueNoContribuable(currentSimpleContribuable
                            .getIdTier()));
        }

        // Set Famille
        // ---------------------------------------------------------------------------

        SimpleFamille currentFamille = new SimpleFamille();

        // Chercher famille du contribuable
        FamilleContribuableSearch familleSearch = new FamilleContribuableSearch();
        familleSearch.setForIdContribuable(currentSimpleContribuable.getIdContribuable());
        familleSearch.setForIdTier(currentSimpleContribuable.getIdTier());
        familleSearch = AmalServiceLocator.getFamilleContribuableService().search(familleSearch);

        if (familleSearch.getSearchResults().length > 0) {
            // Gets the family member
            for (Iterator it = Arrays.asList(familleSearch.getSearchResults()).iterator(); it.hasNext();) {
                FamilleContribuable familleMember = (FamilleContribuable) it.next();
                // Set contribuable (compare the id tiers of contribuable entity and current member)
                if ((null != familleMember)
                        && familleMember.getSimpleFamille().getIdTier().equals(currentSimpleContribuable.getIdTier())) {
                    currentFamille = familleMember.getSimpleFamille();
                    currentContribuable.setFamille(currentFamille);
                    currentContribuable.setDetailFamille(familleMember.getSimpleDetailFamille());
                    break;
                }
            }
        }

        // Set Detail Famille (set before, but...)
        // ---------------------------------------------------------------------------
        // NOT POSSIBLE, MULTITUDE

        return currentContribuable;
    }

    @Override
    public SimpleContribuableInfos readInfos(String idContribuable) throws JadePersistenceException,
            ContribuableException {
        if (JadeStringUtil.isEmpty(idContribuable)) {
            throw new ContribuableException("Unable to read ContribuableInfos, the id passed is null!");
        }
        SimpleContribuableInfos simpleContribuableInfos = new SimpleContribuableInfos();
        simpleContribuableInfos.setId(idContribuable);
        return (SimpleContribuableInfos) JadePersistenceManager.read(simpleContribuableInfos);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #search
     * (ch.globaz.amal.business.models.contribuable.ContribuableSearch)
     */
    @Override
    public ContribuableSearch search(ContribuableSearch contribuableSearch) throws JadePersistenceException,
            ContribuableException {
        if (contribuableSearch == null) {
            throw new ContribuableException("Unable to search contribuable, the search model passed is null!");
        }
        return (ContribuableSearch) JadePersistenceManager.search(contribuableSearch);
    }

    @Override
    public ContribuableSearch searchFusion(ContribuableSearch contribuableSearch) throws JadePersistenceException,
            ContribuableException {
        if (contribuableSearch == null) {
            throw new ContribuableException("Unable to search contribuable, the search model passed is null!");
        }
        contribuableSearch.setForIsContribuable(new Boolean(true));
        contribuableSearch.setForContribuableActif(new Boolean(true));
        return (ContribuableSearch) JadePersistenceManager.search(contribuableSearch);
    }

    @Override
    public ContribuableHistoriqueRCListeSearch searchHistoriqueRCListe(
            ContribuableHistoriqueRCListeSearch contribuableHistoriqueRCListeSearch) throws JadePersistenceException,
            ContribuableException {
        if (contribuableHistoriqueRCListeSearch == null) {
            throw new ContribuableException("Unable to search contribuableRCListe, the search model passed is null!");
        }
        return (ContribuableHistoriqueRCListeSearch) JadePersistenceManager.search(contribuableHistoriqueRCListeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.ContribuableService#searchRCListe(ch.globaz.amal.business
     * .models.contribuable.ContribuableRCListeSearch)
     */
    @Override
    public ContribuableRCListeSearch searchRCListe(ContribuableRCListeSearch contribuableRCListeSearch)
            throws JadePersistenceException, ContribuableException {
        if (contribuableRCListeSearch == null) {
            throw new ContribuableException("Unable to search contribuableRCListe, the search model passed is null!");
        }
        return (ContribuableRCListeSearch) JadePersistenceManager.search(contribuableRCListeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.ContribuableService #update
     * (ch.globaz.amal.business.models.contribuable.Contribuable)
     */
    @Override
    public Contribuable update(Contribuable contribuable) throws JadePersistenceException, ContribuableException {
        // TODO Auto-generated method stub
        return null;
    }

}
