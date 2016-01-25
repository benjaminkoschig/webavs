/**
 * 
 */
package ch.globaz.amal.process.repriseSourciers.step1;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.util.CommonNSSFormater;
import globaz.pyxis.util.TINSSFormater;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.amal.process.repriseSourciers.AMProcessRepriseSourciersCsvEntity;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.PersonneEtendueService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseSourciersEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    private AMProcessRepriseSourciersCsvEntity currentCsvEntity = null;
    private JadeProcessEntity currentProcessEntity = null;
    private Map<Enum<?>, String> data = null;

    private String capitalize(String stringToCapitalize) {
        if (JadeStringUtil.isEmpty(stringToCapitalize)) {
            return stringToCapitalize;
        } else {
            try {
                String[] allString = stringToCapitalize.split("\\s+");
                String returnString = "";
                for (int iString = 0; iString < allString.length; iString++) {
                    String currentString = allString[iString];
                    returnString += Character.toUpperCase(currentString.charAt(0));
                    returnString += currentString.substring(1).toLowerCase();
                    returnString += " ";
                }
                return returnString;
            } catch (Exception ex) {
                return stringToCapitalize;
            }
            // return Character.toUpperCase(stringToCapitalize.charAt(0)) +
            // stringToCapitalize.substring(1).toLowerCase();
        }
    }

    private PersonneEtendueComplexModel createTiers(String nom, String prenom, String dateNaissance, String noAVS,
            String gender, String titre) {
        AMGestionTiers gestionTiers = new AMGestionTiers();
        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());

        // Set personne etendue
        PersonneEtendueComplexModel personneEtendue = new PersonneEtendueComplexModel();
        if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
            personneEtendue.getPersonne().setDateNaissance(dateNaissance);
        }
        if (!JadeStringUtil.isBlankOrZero(noAVS)) {
            personneEtendue.getPersonneEtendue().setNumAvsActuel(noAVS);
        }
        if (!JadeStringUtil.isBlankOrZero(gender)) {
            personneEtendue.getPersonne().setSexe(gender);
        }
        if (!JadeStringUtil.isBlankOrZero(titre)) {
            personneEtendue.getTiers().setTitreTiers(titre);
        }
        personneEtendue.getTiers().setDesignation1(nom);
        personneEtendue.getTiers().setDesignation2(prenom);
        personneEtendue.getTiers().setPersonnePhysique(true);

        // ---------------------------------------------------------------------------
        // Recherche dans TIERS
        // ---------------------------------------------------------------------------
        // Log
        try {
            // Search tiers
            PersonneEtendueSearchComplexModel tiersSearch = gestionTiers.findTiers(personneEtendue);
            if ((tiersSearch != null) && (tiersSearch.getSize() == 1)) {
                personneEtendue = (PersonneEtendueComplexModel) tiersSearch.getSearchResults()[0];
                Boolean personneEtendueNeedUpdate = false;
                // Tiers trouvé, mettons-le à jour
                // NSS
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumAvsActuel())) {
                    if (!JadeStringUtil.isBlankOrZero(noAVS)) {
                        personneEtendue.getPersonneEtendue().setNumAvsActuel(noAVS);
                        personneEtendue.setDateModifAvs(dateToday);
                        personneEtendue.setMotifModifAvs(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Date de naissance
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {
                    if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
                        personneEtendue.getPersonne().setDateNaissance(dateNaissance);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Sexe
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getSexe())) {
                    if (!JadeStringUtil.isBlankOrZero(gender)) {
                        personneEtendue.getPersonne().setSexe(gender);
                        personneEtendueNeedUpdate = true;
                    }
                }
                // Titre
                if (JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getTitreTiers())) {
                    if (!JadeStringUtil.isBlankOrZero(titre)) {
                        personneEtendue.getTiers().setTitreTiers(titre);
                        personneEtendue.setDateModifTitre(dateToday);
                        personneEtendue.setMotifModifTitre(PersonneEtendueService.motifsModification.MOTIF_CREATION);
                        personneEtendueNeedUpdate = true;
                    }
                }
                if (personneEtendueNeedUpdate) {
                    try {
                        personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                        personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().update(personneEtendue);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        personneEtendue = null;
                    }
                }
            } else if ((tiersSearch != null) && (tiersSearch.getSize() > 1)) {
                personneEtendue = null;
            } else if (tiersSearch == null) {
                // pas trouvé, à créer
                try {
                    personneEtendue.getTiers().setTypeTiers(IConstantes.CS_TIERS_TYPE_TIERS);
                    personneEtendue.getTiers().setLangue(IConstantes.CS_TIERS_LANGUE_FRANCAIS);
                    personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService().create(personneEtendue);
                } catch (Exception ex) {
                    personneEtendue = null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            personneEtendue = null;
        }
        // Update rôle
        try {
            if (!TIBusinessServiceLocator.getRoleService().hasRole(personneEtendue.getTiers().getIdTiers(),
                    AMGestionTiers.CS_ROLE_AMAL)) {
                TIBusinessServiceLocator.getRoleService().beginRole(personneEtendue.getTiers().getIdTiers(),
                        AMGestionTiers.CS_ROLE_AMAL);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            personneEtendue = null;
        }

        return personneEtendue;
    }

    /**
     * Création du tiers conjoint
     * 
     * @return
     */
    private PersonneEtendueComplexModel createTiersConjoint() {
        // Get nom - prenom, datenaissance, noAVS
        String nom = capitalize(currentCsvEntity.getNomConjoint());
        String prenom = capitalize(currentCsvEntity.getPrenomConjoint());
        String dateNaissance = currentCsvEntity.getDateNaissanceConjoint();
        try {
            JADate currentJADate = JADate.newDateFromAMJ(dateNaissance);
            dateNaissance = JACalendar.format(currentJADate);
        } catch (Exception ex) {
            dateNaissance = "";
        }
        String noAVS = getCurrentValidFormattedNSSConjoint();
        if (JadeStringUtil.isEmpty(noAVS)) {
            noAVS = getCurrentValidFormattedAVSConjoint();
        }
        String gender = currentCsvEntity.getSexeConjoint();
        String titre = gender;
        if (gender.equals("M")) {
            gender = "516001";
            titre = "502001";
        } else if (gender.equals("F")) {
            gender = "516002";
            titre = "502002";
        }
        // Check noAVS avant de le mettre ...
        boolean nAVSError = false;
        if (!JadeStringUtil.isEmpty(noAVS)) {
            try {
                String typeNoAVS = TINSSFormater.findType(noAVS);
                if (typeNoAVS == TINSSFormater.TYPE_NAVS) {
                    if (gender.equals("516001")) {
                        JAUtil.checkAvs(noAVS, 1);
                    } else if (gender.equals("516002")) {
                        JAUtil.checkAvs(noAVS, 2);
                    } else {
                        noAVS = "";
                    }
                } else {
                    CommonNSSFormater currentFormater = new CommonNSSFormater();
                    currentFormater.checkNss(JadeStringUtil.removeChar(noAVS, '.'));
                }
            } catch (Exception ex) {
                noAVS = "";
                nAVSError = true;
            }
        }

        if (JadeStringUtil.isEmpty(nom) || JadeStringUtil.isEmpty(prenom) || JadeStringUtil.isEmpty(dateNaissance)) {
            return null;
        } else {
            PersonneEtendueComplexModel toReturn = createTiers(nom, prenom, dateNaissance, noAVS, gender, titre);
            if (nAVSError) {
                JadeThread.logInfo("CREATION TIERS",
                        "NOAVS MAL FORMATTE CONJOINT;" + noAVS + ";" + currentCsvEntity.toString());
            }
            return toReturn;
        }

    }

    /**
     * Création du tiers sourcier
     * 
     * @return
     */
    private PersonneEtendueComplexModel createTiersSourcier() {

        // Get nom - prenom, datenaissance, noAVS
        String nom = capitalize(currentCsvEntity.getNomSourcier());
        String prenom = capitalize(currentCsvEntity.getPrenomSourcier());
        String dateNaissance = currentCsvEntity.getDateNaissanceSourcier();
        try {
            JADate currentJADate = JADate.newDateFromAMJ(dateNaissance);
            dateNaissance = JACalendar.format(currentJADate);
        } catch (Exception ex) {
            dateNaissance = "";
        }
        String noAVS = getCurrentValidFormattedNSS();
        if (JadeStringUtil.isEmpty(noAVS)) {
            noAVS = getCurrentValidFormattedAVS();
        }
        String gender = currentCsvEntity.getSexeSourcier();
        String titre = gender;
        if (gender.equals("M")) {
            gender = "516001";
            titre = "502001";
        } else if (gender.equals("F")) {
            gender = "516002";
            titre = "502002";
        }
        // Check noAVS avant de le mettre ...
        boolean nAVSError = false;
        if (!JadeStringUtil.isEmpty(noAVS)) {
            try {
                String typeNoAVS = TINSSFormater.findType(noAVS);
                if (typeNoAVS == TINSSFormater.TYPE_NAVS) {
                    if (gender.equals("516001")) {
                        JAUtil.checkAvs(noAVS, 1);
                    } else if (gender.equals("516002")) {
                        JAUtil.checkAvs(noAVS, 2);
                    } else {
                        noAVS = "";
                    }
                } else {
                    CommonNSSFormater currentFormater = new CommonNSSFormater();
                    currentFormater.checkNss(JadeStringUtil.removeChar(noAVS, '.'));
                }
            } catch (Exception ex) {
                nAVSError = true;
                noAVS = "";
            }
        }
        if (JadeStringUtil.isEmpty(nom) || JadeStringUtil.isEmpty(prenom) || JadeStringUtil.isEmpty(dateNaissance)) {
            return null;
        } else {
            PersonneEtendueComplexModel toReturn = createTiers(nom, prenom, dateNaissance, noAVS, gender, titre);
            if (nAVSError) {
                JadeThread.logInfo("CREATION TIERS", "NOAVS MAL FORMATTE;" + noAVS + ";" + currentCsvEntity.toString());
            }
            return toReturn;
        }
    }

    /**
     * Recherche du contribuable dans les dossiers actifs
     * 
     * @param searchedNSS
     * @param searchedAVS
     * @param searchedFamilyName
     * @param searchedGivenName
     * @param searchedDateOfBirth
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    private ContribuableRCListe getContribuable(String searchedNSS, String searchedAVS, String searchedFamilyName,
            String searchedGivenName, String searchedDateOfBirth) throws JadeApplicationServiceNotAvailableException {

        ContribuableRCListe currentContribuableRCListe = null;
        ContribuableRCListeSearch currentContribuableRCListeSearch = new ContribuableRCListeSearch();

        boolean bFound = false;
        boolean bSearchByNoNSS = true;
        boolean bSearchByNoAVS = true;
        boolean bSearchByName = true;

        JadeLogger.info(null, "----------------------------------------------------------------------------------");
        // Recherche par NSS, puis date de naissance
        if (bSearchByNoNSS && !JadeStringUtil.isBlankOrZero(searchedNSS)) {
            JadeLogger.info(null, "RECHERCHE PAR NSS : " + searchedNSS);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByNSS(
                    currentContribuableRCListeSearch, searchedNSS);
            if (currentContribuableRCListeSearch.getSize() == 1) {
                bSearchByNoAVS = false;
                bSearchByName = false;
                // si dispo, comparaison avec la date de naissance, doublon NSS peuvent exister dans fichier BPM
                ContribuableRCListe currentDossier = (ContribuableRCListe) currentContribuableRCListeSearch
                        .getSearchResults()[0];
                // On formatte la date pour la recherche
                String currentDateNaissance = searchedDateOfBirth;
                if (!JadeDateUtil.isGlobazDate(currentDateNaissance)) {
                    try {
                        JADate currentJADate = JADate.newDateFromAMJ(currentDateNaissance);
                        currentDateNaissance = JACalendar.format(currentJADate);
                    } catch (Exception ex) {
                        JadeLogger.error(null, "Error converting date : " + currentDateNaissance);
                        currentDateNaissance = "";
                    }
                }
                if (!JadeStringUtil.isEmpty(currentDateNaissance)
                        && !JadeStringUtil.isEmpty(currentDossier.getDateNaissance())) {
                    if (currentDateNaissance.equals(currentDossier.getDateNaissance())) {
                        bFound = true;
                    } else {
                        bFound = false;
                    }
                } else {
                    bFound = true;
                }
            } else if (currentContribuableRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByNoAVS = false;
                bSearchByName = false;
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByDateOfBirth(
                        currentContribuableRCListeSearch, searchedDateOfBirth);
                if (currentContribuableRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableRCListeSearch);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par AVS, puis date de naissance
        if (bSearchByNoAVS && !JadeStringUtil.isBlankOrZero(searchedAVS)) {
            JadeLogger.info(null, "RECHERCHE PAR AVS : " + searchedAVS);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByAVS(
                    currentContribuableRCListeSearch, searchedAVS);
            if (currentContribuableRCListeSearch.getSize() == 1) {
                bSearchByName = false;
                // si dispo, comparaison avec la date de naissance, doublon NSS peuvent exister dans fichier BPM
                ContribuableRCListe currentDossier = (ContribuableRCListe) currentContribuableRCListeSearch
                        .getSearchResults()[0];
                // On formatte la date pour la recherche
                String currentDateNaissance = searchedDateOfBirth;
                if (!JadeDateUtil.isGlobazDate(currentDateNaissance)) {
                    try {
                        JADate currentJADate = JADate.newDateFromAMJ(currentDateNaissance);
                        currentDateNaissance = JACalendar.format(currentJADate);
                    } catch (Exception ex) {
                        JadeLogger.error(null, "Error converting date : " + currentDateNaissance);
                        currentDateNaissance = "";
                    }
                }
                if (!JadeStringUtil.isEmpty(currentDateNaissance)
                        && !JadeStringUtil.isEmpty(currentDossier.getDateNaissance())) {
                    if (currentDateNaissance.equals(currentDossier.getDateNaissance())) {
                        bFound = true;
                    } else {
                        bFound = false;
                    }
                } else {
                    bFound = true;
                }
            } else if (currentContribuableRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByName = false;
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByDateOfBirth(
                        currentContribuableRCListeSearch, searchedDateOfBirth);
                if (currentContribuableRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableRCListeSearch);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par date de naissance, puis nom prénom
        if (bSearchByName && !JadeStringUtil.isBlankOrZero(searchedDateOfBirth)
                && !JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            JadeLogger.info(null, "RECHERCHE PAR DOB : " + searchedDateOfBirth);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByDateOfBirth(
                    currentContribuableRCListeSearch, searchedDateOfBirth);
            if (currentContribuableRCListeSearch.getSize() >= 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR NOM : " + searchedFamilyName);
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByFamilyName(
                        currentContribuableRCListeSearch, searchedFamilyName);
                if (currentContribuableRCListeSearch.getSize() >= 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR PRENOM : " + searchedGivenName);
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierByGivenName(currentContribuableRCListeSearch, searchedGivenName);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    } else if (currentContribuableRCListeSearch.getSize() > 1) {
                        JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                        currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                                .getDossierLastSubside(currentContribuableRCListeSearch);
                        if (currentContribuableRCListeSearch.getSize() == 1) {
                            bFound = true;
                        }
                    }
                }
            }
        }

        if (bFound) {
            currentContribuableRCListe = (ContribuableRCListe) currentContribuableRCListeSearch.getSearchResults()[0];
            String idContribuable = currentContribuableRCListe.getIdContribuable();
            JadeLogger.info(null, "FOUND : " + currentContribuableRCListe.getIsContribuable() + " - " + idContribuable);
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return currentContribuableRCListe;
        } else {
            JadeLogger.info(null, "DOSSIER ACTIF NOT FOUND");
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return null;
        }
    }

    /**
     * Recherche du contribuable dans les dossiers historiques
     * 
     * @param searchedNSS
     * @param searchedAVS
     * @param searchedFamilyName
     * @param searchedGivenName
     * @param searchedDateOfBirth
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    private SimpleContribuableInfos getContribuableHistorique(String searchedNSS, String searchedAVS,
            String searchedFamilyName, String searchedGivenName, String searchedDateOfBirth)
            throws JadeApplicationServiceNotAvailableException {

        ContribuableHistoriqueRCListe currentContribuableHistoriqueRCListe = null;
        ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();

        boolean bFound = false;
        boolean bSearchByNoNSS = true;
        boolean bSearchByNoAVS = true;
        boolean bSearchByName = true;

        JadeLogger.info(null, "----------------------------------------------------------------------------------");
        // Recherche par NSS, puis date de naissance
        if (bSearchByNoNSS && !JadeStringUtil.isBlankOrZero(searchedNSS)) {
            JadeLogger.info(null, "RECHERCHE PAR NSS : " + searchedNSS);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByNSS(
                    currentContribuableHistoriqueRCListeSearch, searchedNSS);
            if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                bSearchByNoAVS = false;
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByNoAVS = false;
                bSearchByName = false;
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
                if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par AVS, puis date de naissance
        if (bSearchByNoAVS && !JadeStringUtil.isBlankOrZero(searchedAVS)) {
            JadeLogger.info(null, "RECHERCHE PAR AVS : " + searchedAVS);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService().getDossierByAVS(
                    currentContribuableHistoriqueRCListeSearch, searchedAVS);
            if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByName = false;
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
                if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par date de naissance, puis nom prénom
        if (bSearchByName && !JadeStringUtil.isBlankOrZero(searchedDateOfBirth)
                && !JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            JadeLogger.info(null, "RECHERCHE PAR DOB : " + searchedDateOfBirth);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
            if (currentContribuableHistoriqueRCListeSearch.getSize() >= 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR NOM : " + searchedFamilyName);
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByFamilyName(currentContribuableHistoriqueRCListeSearch, searchedFamilyName);
                if (currentContribuableHistoriqueRCListeSearch.getSize() >= 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR PRENOM : " + searchedGivenName);
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierByGivenName(currentContribuableHistoriqueRCListeSearch, searchedGivenName);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                        JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                        currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                                .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                        if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                            bFound = true;
                        }
                    }
                }
            }
        }

        if (bFound) {
            currentContribuableHistoriqueRCListe = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                    .getSearchResults()[0];
            String idContribuable = currentContribuableHistoriqueRCListe.getIdContribuableInfo();
            JadeLogger.info(null, "FOUND HISTORIQUE : " + idContribuable);
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            try {
                return AmalServiceLocator.getContribuableService().readInfos(idContribuable);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            JadeLogger.info(null, "DOSSIER HISTORIQUE NOT FOUND");
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return null;
        }

    }

    /**
     * Récupération no avs formatté
     * 
     * @return
     */
    private String getCurrentValidFormattedAVS() {
        String currentAVS = "";

        if (!JadeStringUtil.isEmpty(currentCsvEntity.getNoAVSSourcier())) {
            try {
                String avsFormatted = TINSSFormater.format(currentCsvEntity.getNoAVSSourcier(),
                        TINSSFormater.findType(currentCsvEntity.getNoAVSSourcier()));
                currentAVS = avsFormatted;
            } catch (Exception e) {
                currentAVS = "";
            }
        } else {
            currentAVS = "";
        }
        return currentAVS;
    }

    /**
     * Récupération no avs formatté
     * 
     * @return
     */
    private String getCurrentValidFormattedAVSConjoint() {
        String currentAVS = "";

        if (!JadeStringUtil.isEmpty(currentCsvEntity.getNoAVSConjoint())) {
            try {
                String avsFormatted = TINSSFormater.format(currentCsvEntity.getNoAVSConjoint(),
                        TINSSFormater.findType(currentCsvEntity.getNoAVSConjoint()));
                currentAVS = avsFormatted;
            } catch (Exception e) {
                currentAVS = "";
            }
        } else {
            currentAVS = "";
        }
        return currentAVS;
    }

    /**
     * Récupération du nss formatté
     * 
     * @return
     */
    private String getCurrentValidFormattedNSS() {
        String currentNSS = "";
        // ------------------------------------------------------------------------------------
        // Récupération du noAVS
        // ------------------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(currentCsvEntity.getNoNSSSourcier())) {
            try {
                String nssFormatted = TINSSFormater.format(currentCsvEntity.getNoNSSSourcier(),
                        TINSSFormater.findType(currentCsvEntity.getNoNSSSourcier()));
                currentNSS = nssFormatted;
            } catch (Exception e) {
                currentNSS = "";
            }
        } else {
            currentNSS = "";
        }
        return currentNSS;
    }

    /**
     * Récupération du nss formatté conjoint
     * 
     * @return
     */
    private String getCurrentValidFormattedNSSConjoint() {
        String currentNSS = "";
        // ------------------------------------------------------------------------------------
        // Récupération du noAVS
        // ------------------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(currentCsvEntity.getNoNSSConjoint())) {
            try {
                String nssFormatted = TINSSFormater.format(currentCsvEntity.getNoNSSConjoint(),
                        TINSSFormater.findType(currentCsvEntity.getNoNSSConjoint()));
                currentNSS = nssFormatted;
            } catch (Exception e) {
                currentNSS = "";
            }
        } else {
            currentNSS = "";
        }
        return currentNSS;
    }

    /**
     * Recherche de l'élément famille dans les différents dossiers
     * 
     * @param searchedNSS
     * @param searchedAVS
     * @param searchedFamilyName
     * @param searchedGivenName
     * @param searchedDateOfBirth
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    private SimpleFamille getFamille(String searchedNoSourcier, String searchedNSS, String searchedAVS,
            String searchedFamilyName, String searchedGivenName, String searchedDateOfBirth)
            throws JadeApplicationServiceNotAvailableException {

        SimpleFamilleSearch currentFamilleSearch = new SimpleFamilleSearch();
        SimpleFamille currentFamille = null;

        boolean bFound = false;
        boolean bSearchByNoNSS = true;
        boolean bSearchByNoAVS = true;
        boolean bSearchByName = true;

        JadeLogger.info(null, "----------------------------------------------------------------------------------");
        // Recherche basique par noPersonnel
        if (!JadeStringUtil.isEmpty(searchedNoSourcier) && !JadeStringUtil.isBlankOrZero(searchedNoSourcier)) {
            currentFamilleSearch.setForNoPersonne(searchedNoSourcier);
            ArrayList<String> inPereMereEnfant = new ArrayList<String>();
            inPereMereEnfant.add(IAMCodeSysteme.CS_TYPE_PERE);
            inPereMereEnfant.add(IAMCodeSysteme.CS_TYPE_MERE);
            currentFamilleSearch.setInPereMereEnfant(inPereMereEnfant);
            try {
                currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(currentFamilleSearch);
                if (currentFamilleSearch.getSize() == 1) {
                    bSearchByNoNSS = false;
                    bSearchByNoAVS = false;
                    bSearchByName = false;
                    bFound = true;
                } else if (currentFamilleSearch.getSize() > 1) {
                    JadeLogger.error(null, "MANY RESULTS FOUND FOR NO SOURCIER : " + searchedNoSourcier);
                    currentFamilleSearch = new SimpleFamilleSearch();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Recherche par NSS, puis date de naissance
        if (bSearchByNoNSS && !JadeStringUtil.isBlankOrZero(searchedNSS)) {
            JadeLogger.info(null, "RECHERCHE PAR NSS : " + searchedNSS);
            currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().getFamilleByNSS(
                    currentFamilleSearch, searchedNSS);
            if (currentFamilleSearch.getSize() == 1) {
                bSearchByNoAVS = false;
                bSearchByName = false;
                // si dispo, comparaison avec la date de naissance, doublon NSS peuvent exister dans fichier BPM
                SimpleFamille currentDossier = (SimpleFamille) currentFamilleSearch.getSearchResults()[0];
                // On formatte la date pour la recherche
                String currentDateNaissance = searchedDateOfBirth;
                if (!JadeDateUtil.isGlobazDate(currentDateNaissance)) {
                    try {
                        JADate currentJADate = JADate.newDateFromAMJ(currentDateNaissance);
                        currentDateNaissance = JACalendar.format(currentJADate);
                    } catch (Exception ex) {
                        JadeLogger.error(null, "Error converting date : " + currentDateNaissance);
                        currentDateNaissance = "";
                    }
                }
                if (!JadeStringUtil.isEmpty(currentDateNaissance)
                        && !JadeStringUtil.isEmpty(currentDossier.getDateNaissance())) {
                    if (currentDateNaissance.equals(currentDossier.getDateNaissance())) {
                        bFound = true;
                    } else {
                        bFound = false;
                    }
                } else {
                    bFound = true;
                }
            } else if (currentFamilleSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByNoAVS = false;
                bSearchByName = false;
                currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().getFamilleByDateOfBirth(
                        currentFamilleSearch, searchedDateOfBirth);
                if (currentFamilleSearch.getSize() == 1) {
                    bFound = true;
                }
            }
        }
        // Recherche par AVS, puis date de naissance
        if (bSearchByNoAVS && !JadeStringUtil.isBlankOrZero(searchedAVS)) {
            JadeLogger.info(null, "RECHERCHE PAR AVS : " + searchedAVS);
            currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().getFamilleByAVS(
                    currentFamilleSearch, searchedAVS);
            if (currentFamilleSearch.getSize() == 1) {
                bSearchByName = false;
                // si dispo, comparaison avec la date de naissance, doublon NSS peuvent exister dans fichier BPM
                SimpleFamille currentDossier = (SimpleFamille) currentFamilleSearch.getSearchResults()[0];
                // On formatte la date pour la recherche
                String currentDateNaissance = searchedDateOfBirth;
                if (!JadeDateUtil.isGlobazDate(currentDateNaissance)) {
                    try {
                        JADate currentJADate = JADate.newDateFromAMJ(currentDateNaissance);
                        currentDateNaissance = JACalendar.format(currentJADate);
                    } catch (Exception ex) {
                        JadeLogger.error(null, "Error converting date : " + currentDateNaissance);
                        currentDateNaissance = "";
                    }
                }
                if (!JadeStringUtil.isEmpty(currentDateNaissance)
                        && !JadeStringUtil.isEmpty(currentDossier.getDateNaissance())) {
                    if (currentDateNaissance.equals(currentDossier.getDateNaissance())) {
                        bFound = true;
                    } else {
                        bFound = false;
                    }
                } else {
                    bFound = true;
                }
            } else if (currentFamilleSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByName = false;
                currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().getFamilleByDateOfBirth(
                        currentFamilleSearch, searchedDateOfBirth);
                if (currentFamilleSearch.getSize() == 1) {
                    bFound = true;
                }
            }
        }
        // Recherche par date de naissance, puis nom prénom
        if (bSearchByName && !JadeStringUtil.isBlankOrZero(searchedDateOfBirth)
                && !JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            JadeLogger.info(null, "RECHERCHE PAR DOB : " + searchedDateOfBirth);
            currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService().getFamilleByDateOfBirth(
                    currentFamilleSearch, searchedDateOfBirth);
            if (currentFamilleSearch.getSize() >= 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR NOM PRENOM : " + searchedFamilyName);
                currentFamilleSearch = AmalServiceLocator.getFamilleContribuableService()
                        .getFamilleByFamilyNameGivenName(currentFamilleSearch, searchedFamilyName, searchedGivenName);
                if (currentFamilleSearch.getSize() == 1) {
                    bFound = true;
                }
            }
        }

        if (bFound) {
            try {
                currentFamille = (SimpleFamille) currentFamilleSearch.getSearchResults()[0];
                if (!currentFamille.getIsContribuable()) {
                    JadeLogger.info(null, "FOUND FAMILLE BUT IS NOT CONTRIBUABLE");
                    JadeLogger.info(null,
                            "----------------------------------------------------------------------------------");
                    return null;
                }
                JadeLogger.info(
                        null,
                        "FOUND FAMILLE: " + currentFamille.getIdContribuable() + " "
                                + currentFamille.getIsContribuable());
                JadeLogger.info(null,
                        "----------------------------------------------------------------------------------");
                return currentFamille;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            JadeLogger.info(null, "DOSSIER FAMILLE NOT FOUND");
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#run()
     */
    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        // ---------------------------------------------------------------------------------------------
        // 0 - CHECK VALIDITE DE L'ENTITE CSV
        // ---------------------------------------------------------------------------------------------
        if (currentCsvEntity == null) {
            JadeThread.logError("CSV Entity not loaded", "CSV Entity not loaded");
            return;
        } else {
            JadeLogger.info(null, "SEARCHING : " + currentCsvEntity.toString());
        }

        String searchedNSS = getCurrentValidFormattedNSS().trim();
        String searchedAVS = getCurrentValidFormattedAVS().trim();
        String searchedFamilyName = capitalize(currentCsvEntity.getNomSourcier()).trim();
        String searchedGivenName = capitalize(currentCsvEntity.getPrenomSourcier()).trim();
        String searchedDateOfBirth = currentCsvEntity.getDateNaissanceSourcier().trim();
        String searchedNoSourcier = currentCsvEntity.getNoSourcier().trim();
        String searchedSexeSourcier = currentCsvEntity.getSexeSourcier().trim();

        String searchedNSSConjoint = getCurrentValidFormattedNSSConjoint().trim();
        String searchedAVSConjoint = getCurrentValidFormattedAVSConjoint().trim();
        String searchedFamilyNameConjoint = capitalize(currentCsvEntity.getNomConjoint()).trim();
        String searchedGivenNameConjoint = capitalize(currentCsvEntity.getPrenomConjoint()).trim();
        String searchedDateOfBirthConjoint = currentCsvEntity.getDateNaissanceConjoint().trim();
        String searchedNoConjoint = currentCsvEntity.getNoConjoint().trim();
        String searchedSexeConjoint = currentCsvEntity.getSexeConjoint().trim();

        ContribuableRCListe currentContribuable = null;
        SimpleContribuableInfos currentContribuableHistorique = null;
        SimpleFamille currentFamille = null;
        String idContribuableDossier = null;
        String idContribuableDossierHistorique = null;
        // ---------------------------------------------------------------------------------------------
        // 1 - RECHERCHE DOSSIER ACTIF - SOURCIER
        // ---------------------------------------------------------------------------------------------
        try {
            currentContribuable = getContribuable(searchedNSS, searchedAVS, searchedFamilyName, searchedGivenName,
                    searchedDateOfBirth);
            if ((currentContribuable != null) && (currentContribuable.getIsContribuable() == false)) {
                currentFamille = AmalServiceLocator.getFamilleContribuableService()
                        .read(currentContribuable.getIdFamille()).getSimpleFamille();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // ---------------------------------------------------------------------------------------------
        // 2 - RECHERCHE DOSSIER HISTORIQUE - SOURCIER
        // ---------------------------------------------------------------------------------------------
        try {
            if (currentContribuable == null) {
                currentContribuableHistorique = getContribuableHistorique(searchedNSS, searchedAVS, searchedFamilyName,
                        searchedGivenName, searchedDateOfBirth);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // ---------------------------------------------------------------------------------------------
        // 3 - RECHERCHE DANS FAMILLE -SOURCIER
        // ---------------------------------------------------------------------------------------------
        try {
            if ((currentContribuable == null) && (currentContribuableHistorique == null)) {
                currentFamille = getFamille(searchedNoSourcier, searchedNSS, searchedAVS, searchedFamilyName,
                        searchedGivenName, searchedDateOfBirth);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // ---------------------------------------------------------------------------------------------
        // FIN DES RECHERCHES SOURCIER PRINCIPAL, CHECK SUR CONJOINT SI PAS TROUVE ET DISPO
        // ---------------------------------------------------------------------------------------------
        if ((currentFamille == null) && (currentContribuable == null) && (currentContribuableHistorique == null)
                && !JadeStringUtil.isEmpty(currentCsvEntity.getNoConjoint())) {
            // ---------------------------------------------------------------------------------------------
            // 1 - RECHERCHE DOSSIER ACTIF - CONJOINT
            // ---------------------------------------------------------------------------------------------
            try {
                currentContribuable = getContribuable(searchedNSSConjoint, searchedAVSConjoint,
                        searchedFamilyNameConjoint, searchedGivenNameConjoint, searchedDateOfBirthConjoint);
                if ((currentContribuable != null) && (currentContribuable.getIsContribuable() == false)) {
                    currentFamille = AmalServiceLocator.getFamilleContribuableService()
                            .read(currentContribuable.getIdFamille()).getSimpleFamille();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // ---------------------------------------------------------------------------------------------
            // 2 - RECHERCHE DOSSIER HISTORIQUE - CONJOINT
            // ---------------------------------------------------------------------------------------------
            try {
                if (currentContribuable == null) {
                    currentContribuableHistorique = getContribuableHistorique(searchedNSSConjoint, searchedAVSConjoint,
                            searchedFamilyNameConjoint, searchedGivenNameConjoint, searchedDateOfBirthConjoint);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // ---------------------------------------------------------------------------------------------
            // 3 - RECHERCHE DANS FAMILLE - CONJOINT
            // ---------------------------------------------------------------------------------------------
            try {
                if ((currentContribuable == null) && (currentContribuableHistorique == null)) {
                    currentFamille = getFamille(searchedNoConjoint, searchedNSSConjoint, searchedAVSConjoint,
                            searchedFamilyNameConjoint, searchedGivenNameConjoint, searchedDateOfBirthConjoint);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // ---------------------------------------------------------------------------------------------
        // 4 - CONCLUSION DOSSIER TROUVE OU NON
        // ---------------------------------------------------------------------------------------------
        if ((currentFamille == null) && (currentContribuable == null) && (currentContribuableHistorique == null)) {
            JadeThread.logInfo("DOSSIER NON TROUVE", "DOSSIER NON TROUVE ;" + currentCsvEntity.toString());
        } else {
            if (currentFamille != null) {
                // Famille active ?
                ContribuableRCListeSearch currentSearch = new ContribuableRCListeSearch();
                currentSearch.setForIdContribuable(currentFamille.getIdContribuable());
                currentSearch.setIsContribuable(true);
                currentSearch.setForContribuableActif(true);
                currentSearch = AmalServiceLocator.getContribuableService().searchRCListe(currentSearch);
                if (currentSearch.getSize() == 1) {
                    idContribuableDossier = currentFamille.getIdContribuable();
                    JadeThread.logInfo(
                            "DOSSIER FAMILLE TROUVE",
                            "DOSSIER FAMILLE TROUVE;" + currentFamille.getIdContribuable() + ";"
                                    + currentCsvEntity.toString());
                } else if (currentSearch.getSize() > 1) {
                    JadeThread.logWarn(null, "ERROR, MULTIPLE RESULTATS ACTIFS " + currentFamille.getIdContribuable());
                }
                // Famille inactive ?
                ContribuableHistoriqueRCListeSearch currentSearchHisto = new ContribuableHistoriqueRCListeSearch();
                currentSearchHisto.setForIdContribuableInfo(currentFamille.getIdContribuable());
                currentSearchHisto = AmalServiceLocator.getContribuableService().searchHistoriqueRCListe(
                        currentSearchHisto);
                if (currentSearchHisto.getSize() == 1) {
                    idContribuableDossierHistorique = currentFamille.getIdContribuable();
                    JadeThread.logInfo("DOSSIER HISTORIQUE FAMILLE TROUVE", "DOSSIER HISTORIQUE FAMILLE TROUVE;"
                            + currentFamille.getIdContribuable() + ";" + currentCsvEntity.toString());
                } else if (currentSearchHisto.getSize() > 1) {
                    JadeThread.logWarn(null,
                            "ERROR, MULTIPLE RESULTATS HISTORIQUES " + currentFamille.getIdContribuable());
                }
                // Bizarre
                if (!((currentSearch.getSize() == 1) || (currentSearchHisto.getSize() == 1))) {
                    idContribuableDossier = null;
                    idContribuableDossierHistorique = null;
                    JadeThread.logInfo("DOSSIER BIZARRE FAMILLE TROUVE", "DOSSIER BIZARRE FAMILLE TROUVE;"
                            + currentFamille.getIdContribuable() + ";" + currentCsvEntity.toString());
                }
            } else if (currentContribuable != null) {
                idContribuableDossier = currentContribuable.getId();
                JadeThread.logInfo("DOSSIER TROUVE", "DOSSIER TROUVE ;" + currentContribuable.getId() + ";"
                        + currentCsvEntity.toString());
            } else if (currentContribuableHistorique != null) {
                idContribuableDossierHistorique = currentContribuableHistorique.getId();
                JadeThread.logInfo("DOSSIER HISTORIQUE TROUVE", "DOSSIER HISTORIQUE TROUVE;"
                        + currentContribuableHistorique.getId() + ";" + currentCsvEntity.toString());
            }
        }

        // ---------------------------------------------------------------------------------------------
        // Sauvegarde de l'id du dossier dans la table upload
        // Nom Prenom = 123234;A;Tartempion John
        // ---------------------------------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(idContribuableDossier) || !JadeStringUtil.isEmpty(idContribuableDossierHistorique)) {
            SimpleUploadFichierReprise fileUploaded = null;
            try {
                fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(currentProcessEntity.getIdRef());
                if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                    String nomPrenom = fileUploaded.getNomPrenom();
                    if (!JadeStringUtil.isEmpty(idContribuableDossier)) {
                        fileUploaded.setNomPrenom(idContribuableDossier + ";A;" + nomPrenom);
                        fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().update(fileUploaded);
                    } else if (!JadeStringUtil.isEmpty(idContribuableDossierHistorique)) {
                        fileUploaded.setNomPrenom(idContribuableDossierHistorique + ";H;" + nomPrenom);
                        fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().update(fileUploaded);
                    }
                }
            } catch (Exception e) {
                currentCsvEntity = null;
            }
            return;
        }
        // ---------------------------------------------------------------------------------------------
        // 5 - DOSSIER NON TROUVE, EN ERREUR SI CRITERES NSS/AVS OU NOM,PRENOM,DATE DE NAISSANCE INCOMPLET
        // ---------------------------------------------------------------------------------------------
        boolean bConjointIsPrincipal = false;
        if (!JadeStringUtil.isEmpty(searchedNoConjoint)) {
            if (searchedSexeConjoint.equals("M") && searchedSexeSourcier.equals("F")) {
                bConjointIsPrincipal = true;
            }
        }
        if (bConjointIsPrincipal) {
            String currentDateNaissance = "";
            try {
                JADate currentJADate = JADate.newDateFromAMJ(searchedDateOfBirthConjoint);
                currentDateNaissance = JACalendar.format(currentJADate);
            } catch (Exception ex) {
                currentDateNaissance = "";
            }
            if (JadeStringUtil.isEmpty(currentDateNaissance) || JadeStringUtil.isEmpty(searchedFamilyNameConjoint)
                    || JadeStringUtil.isEmpty(searchedGivenNameConjoint)) {
                JadeThread.logError("ERREUR CREATION DOSSIER", "CREATION DOSSIER IMPOSSIBLE CONJOINT INCOMPLET " + ";"
                        + currentCsvEntity.toString());
                return;
            }
        } else {
            String currentDateNaissance = "";
            try {
                JADate currentJADate = JADate.newDateFromAMJ(searchedDateOfBirth);
                currentDateNaissance = JACalendar.format(currentJADate);
            } catch (Exception ex) {
                currentDateNaissance = "";
            }
            if (JadeStringUtil.isEmpty(currentDateNaissance) || JadeStringUtil.isEmpty(searchedFamilyName)
                    || JadeStringUtil.isEmpty(searchedGivenName)) {
                JadeThread.logError("ERREUR CREATION DOSSIER", "CREATION DOSSIER IMPOSSIBLE SOURCIER INCOMPLET" + ";"
                        + currentCsvEntity.toString());
                return;
            }
        }

        // ---------------------------------------------------------------------------------------------
        // 6 - DOSSIER NON TROUVE, CREATION SI CRITERES COMPLETS
        // ---------------------------------------------------------------------------------------------
        // a) création tiers
        PersonneEtendueComplexModel tiersPrincipal = null;
        try {
            if (bConjointIsPrincipal) {
                tiersPrincipal = createTiersConjoint();
            } else {
                tiersPrincipal = createTiersSourcier();
            }
        } catch (Exception ex) {
            tiersPrincipal = null;
        }
        if (tiersPrincipal == null) {
            JadeThread.logError("ERREUR CREATION TIERS", "ERREUR CREATION TIERS " + ";" + currentCsvEntity.toString());
            return;
        }
        // b) création dossier
        Contribuable newDossier = new Contribuable();
        newDossier.setPersonneEtendueComplexModel(tiersPrincipal);
        newDossier.getContribuable().setIsContribuableActif(true);
        if (bConjointIsPrincipal) {
            newDossier.getFamille().setNoPersonne(searchedNoConjoint);
        } else {
            newDossier.getFamille().setNoPersonne(searchedNoSourcier);
        }
        try {
            newDossier = AmalServiceLocator.getContribuableService().create(newDossier);
        } catch (Exception ex) {
            JadeThread.logError("ERREUR CREATION DOSSIER",
                    "ERREUR CREATION DOSSIER " + ";" + currentCsvEntity.toString());
            return;
        }
        // c) enregistrement conjoint
        PersonneEtendueComplexModel tiersConjoint = null;
        try {
            if (bConjointIsPrincipal) {
                tiersConjoint = createTiersSourcier();
            } else {
                tiersConjoint = createTiersConjoint();
            }
        } catch (Exception ex) {
            tiersConjoint = null;
        }
        if (tiersConjoint != null) {
            FamilleContribuable newFamille = new FamilleContribuable();
            newFamille.setPersonneEtendue(tiersConjoint);
            newFamille.getSimpleFamille().setIdContribuable(newDossier.getContribuable().getIdContribuable());
            if ((tiersConjoint.getPersonne().getSexe() != null)
                    && tiersConjoint.getPersonne().getSexe().equals("516001")) {
                newFamille.getSimpleFamille().setPereMereEnfant(IAMCodeSysteme.CS_TYPE_PERE);
            } else {
                newFamille.getSimpleFamille().setPereMereEnfant(IAMCodeSysteme.CS_TYPE_MERE);
            }
            if (bConjointIsPrincipal) {
                newFamille.getSimpleFamille().setNoPersonne(searchedNoSourcier);
            } else {
                newFamille.getSimpleFamille().setNoPersonne(searchedNoConjoint);
            }
            try {
                AmalServiceLocator.getFamilleContribuableService().create(newFamille);
            } catch (Exception ex) {
                JadeThread.logWarn("CREATION DOSSIER",
                        "ERREUR CREATION CONJOINT FAMILLE" + ";" + currentCsvEntity.toString());
            }
        } else {
            if (!JadeStringUtil.isEmpty(currentCsvEntity.getNomConjoint())
                    && !JadeStringUtil.isEmpty(currentCsvEntity.getPrenomConjoint())
                    && !JadeStringUtil.isEmpty(currentCsvEntity.getDateNaissanceConjoint())) {
                // Warning
                JadeThread.logWarn("CREATION DOSSIER",
                        "ERREUR CREATION CONJOINT TIERS" + ";" + currentCsvEntity.toString());
            }
        }
        // d) enregistrement id dossier dans tables upload
        if (newDossier != null) {
            SimpleUploadFichierReprise fileUploaded = null;
            try {
                fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(currentProcessEntity.getIdRef());
                if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                    String nomPrenom = fileUploaded.getNomPrenom();
                    fileUploaded.setNomPrenom(newDossier.getContribuable().getIdContribuable() + ";A;" + nomPrenom);
                    fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().update(fileUploaded);
                }
            } catch (Exception e) {
                currentCsvEntity = null;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface#setCurrentEntity(ch.globaz
     * .jade.process.business.models.process.JadeProcessEntity)
     */
    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        currentProcessEntity = entity;
        String idUpload = entity.getIdRef();
        SimpleUploadFichierReprise fileUploaded = null;
        try {
            fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(idUpload);
            if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                currentCsvEntity = new AMProcessRepriseSourciersCsvEntity(fileUploaded.getXmlLignes());
            }
        } catch (Exception e) {
            currentCsvEntity = null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties#setProperties(java.util
     * .Map)
     */
    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        data = map;
    }

}
