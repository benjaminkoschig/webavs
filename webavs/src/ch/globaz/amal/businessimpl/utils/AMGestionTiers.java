package ch.globaz.amal.businessimpl.utils;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMGestionTiers {

    public final static String CS_DOMAINE_AMAL = "42002700";
    public final static String CS_DOMAINE_DEFAUT = "519004";
    public final static String CS_ROLE_AMAL = "42003300";

    public final static String CS_TYPE_CAISSE_MALADIE = "509008";
    public final static String CS_TYPE_COURRIER = "508001";

    public final static String CS_TYPE_DOMICILE = "508008";

    public final static String DATE_VALIDITE_ADRESSE_STD = "01.11.2010";

    /**
     * @param contribuable
     * @throws ContribuableException
     */
    private void checkAdresse(Contribuable contribuable) throws ContribuableException {
        try {
            String dateToday = "";
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            dateToday = sdf.format(cal.getTime());

            AdresseTiersDetail currentAdresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    contribuable.getPersonneEtendue().getTiers().getIdTiers(), true, dateToday,
                    AMGestionTiers.CS_DOMAINE_AMAL, AMGestionTiers.CS_TYPE_COURRIER, null);

            if ((currentAdresse == null) || JadeStringUtil.isEmpty(currentAdresse.getAdresseFormate())) {
                Boolean isMarried = "515002".equals(contribuable.getPersonneEtendue().getPersonne().getEtatCivil());

                if (isMarried) {
                    // Set l'etat civil à "Marié"
                    contribuable.getAdresseComplexModel().getAdresse().setTitreAdresse("19150010");
                }

                AdresseComplexModel adresseComplexModel = TIBusinessServiceLocator.getAdresseService().addAdresse(
                        contribuable.getAdresseComplexModel(), AMGestionTiers.CS_DOMAINE_AMAL,
                        AMGestionTiers.CS_TYPE_COURRIER, false);
            }
        } catch (Exception e) {
            throw new ContribuableException(e.getMessage());
        }
    }

    /**
     * Méthode utilitaire qui permet la création d'un tiers dans pyxis si les conditions suivantes ne sont pas remplies
     * :
     * <ul>
     * <li>Un tiers existe avec ce NSS</li>
     * <li>Un tiers existe avec ce N° de contribuable</li>
     * <li>Un tiers existe avec nom, prénom et date de naissance</li>
     * </ul>
     * 
     * Si le tiers existe, il est retourné, sinon il est créé.
     * 
     * @param personneEtendue
     * @return le Tiers (crée ou existant)
     * @throws ContribuableException
     * @throws JadePersistenceException
     */
    public PersonneEtendueComplexModel checkTiersExist(PersonneEtendueComplexModel personneEtendue)
            throws ContribuableException, JadePersistenceException {

        if (personneEtendue == null) {
            throw new ContribuableException("Unable to create contribuable, the given model is null!");
        }

        PersonneEtendueSearchComplexModel personneEtendueSearch = this.findTiers(personneEtendue);

        if (personneEtendueSearch == null) {
            personneEtendue.getTiers().setPersonnePhysique(true);
            try {
                return TIBusinessServiceLocator.getPersonneEtendueService().create(personneEtendue);
            } catch (Exception e) {
                throw new ContribuableException("Error while creating Tiers " + e.getMessage());
            }
        }

        return (PersonneEtendueComplexModel) personneEtendueSearch.getSearchResults()[0];
    }

    public void createTiersContribuable(Contribuable contribuable) throws JadePersistenceException,
            ContribuableException {
        PersonneEtendueComplexModel p = checkTiersExist(contribuable.getPersonneEtendue());

        contribuable.setPersonneEtendueComplexModel(p);

        if (!JadeStringUtil.isEmpty(contribuable.getAdresseComplexModel().getLocalite().getNumPostal())) {
            contribuable.getAdresseComplexModel().getTiers()
                    .setId(contribuable.getPersonneEtendue().getTiers().getIdTiers());
            checkAdresse(contribuable);
        }
    }

    public void createTiersContribuable(FamilleContribuable familleContribuable) throws JadePersistenceException,
            ContribuableException {
        familleContribuable.setPersonneEtendue(checkTiersExist(familleContribuable.getPersonneEtendue()));

        if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getNoAVS())) {
            familleContribuable.getSimpleFamille().setNoAVS(
                    JadeStringUtil.removeChar(familleContribuable.getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel(), '.'));
        }

        if (JadeStringUtil.isBlankOrZero(familleContribuable.getSimpleFamille().getDateNaissance())) {
            familleContribuable.getSimpleFamille().setDateNaissance(
                    familleContribuable.getPersonneEtendue().getPersonne().getDateNaissance());
        }

        if (JadeStringUtil.isEmpty(familleContribuable.getSimpleFamille().getNomPrenom())) {
            familleContribuable.getSimpleFamille().setNomPrenom(
                    familleContribuable.getPersonneEtendue().getTiers().getDesignation1() + " "
                            + familleContribuable.getPersonneEtendue().getTiers().getDesignation2());

            familleContribuable.getSimpleFamille().setNomPrenomUpper(
                    familleContribuable.getPersonneEtendue().getTiers().getDesignationUpper1() + " "
                            + familleContribuable.getPersonneEtendue().getTiers().getDesignationUpper2());
        }
    }

    private boolean existeAdresse(SimpleContribuableInfos simpleContribuableInfos) {
        if (!JadeStringUtil.isBlank(simpleContribuableInfos.getAdresse1())
                || !JadeStringUtil.isBlank(simpleContribuableInfos.getAdresse2())
                || !JadeStringUtil.isBlank(simpleContribuableInfos.getAdresse3())
                || !JadeStringUtil.isBlank(simpleContribuableInfos.getRue())
                || !JadeStringUtil.isBlank(simpleContribuableInfos.getNumero())
                || !JadeStringUtil.isBlank(simpleContribuableInfos.getCommune())
                || !JadeStringUtil.isBlank(simpleContribuableInfos.getCasepostale())) {
            return true;
        }
        return false;
    }

    /**
     * @param personneEtendue
     * @return PersonneEtendueSearchComplex trouvé - NULL si le tiers n'existe pas
     * @throws JadePersistenceException
     * @throws ContribuableException
     */
    public PersonneEtendueSearchComplexModel findTiers(PersonneEtendueComplexModel personneEtendue)
            throws JadePersistenceException, ContribuableException {

        // Check des entrées
        if (personneEtendue == null) {
            throw new ContribuableException("Unable to find tiers, the model is null !");
        }

        // Préparation de la reprise
        String forNoAvsActuel = "";
        String forNoContribuable = "";
        String forDesignation1 = "";
        String forDesignation2 = "";
        String forDateNaissance = "";
        Boolean tiersExist = false;
        PersonneEtendueSearchComplexModel personneEtendueSearch = new PersonneEtendueSearchComplexModel();

        // Recherche
        try {

            // 1er critère : Numéro AVS (NSS)
            if (!JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumAvsActuel())) {
                forNoAvsActuel = personneEtendue.getPersonneEtendue().getNumAvsActuel();
                personneEtendueSearch.setForNumeroAvsActuel(forNoAvsActuel);
                personneEtendueSearch.setFor_isInactif("2");
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);

                if (personneEtendueSearch.getSearchResults().length > 0) {
                    tiersExist = true;
                    // Si multiple résultat, affinage sur nom et prénom
                    if (personneEtendueSearch.getSearchResults().length > 1) {
                        JadeLogger.info(this,
                                "RESULTS SIZE AVANT AFFINAGE : " + personneEtendueSearch.getSearchResults().length);
                        personneEtendueSearch = findTiersByNameAndDateNaissance(personneEtendue, personneEtendueSearch);
                        JadeLogger.info(this,
                                "RESULTS SIZE APRES AFFINAGE : " + personneEtendueSearch.getSearchResults().length);
                    }
                }
            }

            // 2ème critère : No de contribuable et date de naissance (conjoints ont potentiellement les numéros de
            // contribuables identiques)
            if (!tiersExist
                    && !JadeStringUtil.isBlankOrZero(personneEtendue.getPersonneEtendue().getNumContribuableActuel())) {

                if (!JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {
                    forDateNaissance = personneEtendue.getPersonne().getDateNaissance();
                }

                personneEtendueSearch = new PersonneEtendueSearchComplexModel();

                forNoContribuable = personneEtendue.getPersonneEtendue().getNumContribuableActuel();
                personneEtendueSearch.setForNoContribuable(forNoContribuable);
                personneEtendueSearch.setForDateNaissance(forDateNaissance);
                personneEtendueSearch.setFor_isInactif("2");
                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);
                if (personneEtendueSearch.getSearchResults().length > 0) {
                    tiersExist = true;
                    // Si multiple résultat, affinage sur nom et prénom
                    if (personneEtendueSearch.getSearchResults().length > 1) {
                        JadeLogger.info(this,
                                "RESULTS SIZE AVANT AFFINAGE : " + personneEtendueSearch.getSearchResults().length);
                        personneEtendueSearch = findTiersByNameAndDateNaissance(personneEtendue, personneEtendueSearch);
                        JadeLogger.info(this,
                                "RESULTS SIZE APRES AFFINAGE : " + personneEtendueSearch.getSearchResults().length);
                    }
                }
            }

            // 3ème critère : nom, prénom, date de naissance
            if (!tiersExist
                    && (!JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getDesignation1())
                            && !JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getDesignation2()) && !JadeStringUtil
                                .isBlankOrZero(personneEtendue.getPersonne().getDateNaissance()))) {

                personneEtendueSearch = new PersonneEtendueSearchComplexModel();

                if (!JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getDesignation1())) {
                    forDesignation1 = JadeStringUtil.convertSpecialChars(personneEtendue.getTiers().getDesignation1())
                            .toUpperCase();
                }

                if (!JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getDesignation2())) {
                    forDesignation2 = JadeStringUtil.convertSpecialChars(personneEtendue.getTiers().getDesignation2())
                            .toUpperCase();
                }

                if (!JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {
                    forDateNaissance = personneEtendue.getPersonne().getDateNaissance();
                }

                personneEtendueSearch.setForDesignation1(forDesignation1);
                personneEtendueSearch.setForDesignation2(forDesignation2);
                personneEtendueSearch.setForDateNaissance(forDateNaissance);
                personneEtendueSearch.setFor_isInactif("2");

                personneEtendueSearch = TIBusinessServiceLocator.getPersonneEtendueService()
                        .find(personneEtendueSearch);

                if (personneEtendueSearch.getSearchResults().length > 0) {
                    tiersExist = true;
                }
            }
        } catch (JadeApplicationException e) {
            throw new ContribuableException("Error while searching Tiers " + e.getMessage() + e.toString());
        } catch (Exception ex) {
            throw new ContribuableException("Error while searching Tiers " + ex.getMessage() + ex.toString());
        }

        if (tiersExist) {
            return personneEtendueSearch;
        }

        return null;

    }

    public PersonneEtendueSearchComplexModel findTiers(PersonneEtendueSearchComplexModel p)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        if (p == null) {
            throw new ContribuableException("Unable to find tiers, the model is null !");
        }

        return TIBusinessServiceLocator.getPersonneEtendueService().find(p);

    }

    private PersonneEtendueSearchComplexModel findTiersByNameAndDateNaissance(
            PersonneEtendueComplexModel personneEtendue, PersonneEtendueSearchComplexModel searchedResults)
            throws JadePersistenceException, ContribuableException {

        PersonneEtendueSearchComplexModel returnSearched = new PersonneEtendueSearchComplexModel();

        ArrayList<JadeAbstractModel> resultsArray = new ArrayList<JadeAbstractModel>();
        // à affiner, nous avons déjà soit le no contribuable, soit le nss, affinage sur nom, prénom, date de naissance
        if (!JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getDesignation1())
                && !JadeStringUtil.isBlankOrZero(personneEtendue.getTiers().getDesignation2())
                && !JadeStringUtil.isBlankOrZero(personneEtendue.getPersonne().getDateNaissance())) {

            String designation1Upper = JadeStringUtil.convertSpecialChars(personneEtendue.getTiers().getDesignation1())
                    .toUpperCase();
            String designation2Upper = JadeStringUtil.convertSpecialChars(personneEtendue.getTiers().getDesignation2())
                    .toUpperCase();
            String dateNaissance = personneEtendue.getPersonne().getDateNaissance();

            for (int iResult = 0; iResult < searchedResults.getSize(); iResult++) {
                PersonneEtendueComplexModel personneResults = (PersonneEtendueComplexModel) searchedResults
                        .getSearchResults()[iResult];
                // Check nom, prénom, date de naissance et ajout dans l'array de retour au cas ou
                if (personneResults.getTiers().getDesignationUpper1().equals(designation1Upper)
                        && personneResults.getTiers().getDesignationUpper2().equals(designation2Upper)
                        && personneResults.getPersonne().getDateNaissance().equals(dateNaissance)) {

                    resultsArray.add(personneResults);
                }
            }
        }
        // Retour
        if (resultsArray.size() == 0) {
            return searchedResults;
        } else {
            // Attribution du tableau de résultat à returnSearched
            JadeAbstractModel[] newResults = new JadeAbstractModel[resultsArray.size()];
            for (int iResult = 0; iResult < resultsArray.size(); iResult++) {
                newResults[iResult] = resultsArray.get(iResult);
            }
            returnSearched.setSearchResults(newResults);
            return returnSearched;
        }
    }

}
