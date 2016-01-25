/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.hera.business.exceptions.models.PeriodeException;
import ch.globaz.pegasus.business.constantes.ConstantesCalcul;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.calcul.AutreRenteCalculSearch;
import ch.globaz.pegasus.business.models.calcul.CalculDernierePCA;
import ch.globaz.pegasus.business.models.calcul.CalculDernierePCASearch;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesDroitSearch;
import ch.globaz.pegasus.business.models.calcul.CalculMembreFamilleSearch;
import ch.globaz.pegasus.business.models.calcul.CalculPlageInitial;
import ch.globaz.pegasus.business.models.calcul.CalculPlageInitialSearch;
import ch.globaz.pegasus.business.models.calcul.CalculPlagesExistantes;
import ch.globaz.pegasus.business.models.calcul.CalculPlagesExistantesSearch;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.ConditionPartageFactory;
import ch.globaz.pegasus.businessimpl.utils.calcul.IConditionPartagePeriode;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

/**
 * @author ECO
 */
public class PeriodesServiceImpl extends PegasusAbstractServiceImpl implements
        ch.globaz.pegasus.business.services.models.calcul.PeriodesService {

    /**
     * Comparateur pour le treeset des dates de partage. Les dates peuvent être en mois/année et seront considérés comme
     * le 1er du mois. Null non supporté.
     * 
     * @author ECO
     */
    private final class DatesPartageComparator implements Comparator<String> {
        @Override
        public int compare(String arg0, String arg1) {
            // conversion en dd.MM.aa(aa) si arg en MM.aa(aa)
            if ((arg0.length() == 5) || (arg0.length() == 7)) {
                arg0 = "01." + arg0;
            }
            if ((arg1.length() == 5) || (arg1.length() == 7)) {
                arg1 = "01." + arg1;
            }

            return Long.valueOf(JadeDateUtil.getGlobazDate(arg0).getTime()).compareTo(
                    Long.valueOf(JadeDateUtil.getGlobazDate(arg1).getTime()));
        }
    }

    /**
     * Calcule la date de début de la plage de calcul d'un droit pour une rente donnée, dans le cas d'un premier calcul
     * du droit.
     * 
     * @param rente
     *            La rente servant de base pour calculer la période de la plage de calcul.
     * @param dateDepotPC
     *            Date de dépôt de la demande PC.
     * @return La date calculée de la plage de calcul.
     */
    private String calculeDatePlageInitial(CalculPlageInitial rente, String dateDepotPC) {
        String dateDepotRente = getDateDepotRente(rente);
        String dateDebutRente = "01." + rente.getDateDebut();
        String dateDecisionRente = getDateDecisionRente(rente);

        // Si pas de date de décision, on retourne la date depotpc
        if (JadeStringUtil.isBlankOrZero(dateDecisionRente)) {
            return dateDepotPC;
        }
        // Différence entre dateAnnonce (depot PC) et date de decision de la rente
        int diffMoisPCRente = JadeDateUtil.getNbMonthsBetween(dateDecisionRente, dateDepotPC);
        if (diffMoisPCRente > 6) {
            return dateDepotPC;
        } else {
            if ((dateDepotRente != null) && JadeDateUtil.isDateBefore(dateDebutRente, dateDepotRente)) {
                return dateDepotRente;
            } else {
                return dateDebutRente;
            }
        }
    }

    private String[] cherchePlageChangementVersion(Droit droit) throws DonneeFinanciereException,
            JadePersistenceException, CalculException {

        // charge données
        DonneeFinanciereHeaderSearch search = new DonneeFinanciereHeaderSearch();
        search.setForIdVersionDroit(droit.getSimpleVersionDroit().getIdVersionDroit());
        // TODO pour tests période pcaccordee, on recupère tous les header qui ne sont pas des copies de version
        // précédentes

        /**
         * désactivation du filtre isCopie afin de gérer les fermetures de période de donnée financière
         */
        // search.setForIsCopieFromPreviousVersion(Boolean.FALSE);

        try {
            search = PegasusImplServiceLocator.getDonneeFinanciereHeaderService().search(search);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("Service not available!", e);
        }
        String dateDebutPlage = null;
        String dateFinPlage = null;
        long timeDebutPlage = 0;
        Date timeFinPlage = new Date(0); // date non nulle et ancienne
        for (JadeAbstractModel absDFH : search.getSearchResults()) {
            DonneeFinanciereHeader dfh = (DonneeFinanciereHeader) absDFH;

            /**
             * gestion manuelle des données financières avec flag isCopie afin de gérer les fermetures de période de
             * donnée financière. Lorsque le cas se présente la période suivant sa date de fin doit être recalculée.
             */
            // vérification du flag copie ancienne version
            if (dfh.getSimpleDonneeFinanciereHeader().getIsCopieFromPreviousVersion()) {

                // récupère la date de fin de la df. Si la date est fermée (non nul), la mettre au début du mois
                // suivant. La date de fin doit donc être le 1er du mois dans ce cas précis, afin d'être unne date de
                // début
                Date timeFinDF = PegasusDateUtil.getPegasusMonthYearDate(dfh.getSimpleDonneeFinanciereHeader()
                        .getDateFin(), true);
                if (timeFinDF != null) {
                    // ajoute un mois à la date de fin
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(timeFinDF);
                    cal.add(Calendar.MONTH, 1);
                    if ((timeDebutPlage == 0) || (cal.getTimeInMillis() < timeDebutPlage)) {
                        timeDebutPlage = cal.getTimeInMillis();
                        dateDebutPlage = JadeDateUtil.getGlobazFormattedDate(cal.getTime()).substring(3);
                    }
                }

            } else {

                long timeDF = PegasusDateUtil.getPegasusMonthYearDate(
                        dfh.getSimpleDonneeFinanciereHeader().getDateDebut(), true).getTime();
                Date timeFinDF = PegasusDateUtil.getPegasusMonthYearDate(dfh.getSimpleDonneeFinanciereHeader()
                        .getDateFin(), false);

                // premiere iteration on set les dates de debut
                if ((dateDebutPlage == null) || (timeDF < timeDebutPlage)) {
                    timeDebutPlage = timeDF;
                    dateDebutPlage = dfh.getSimpleDonneeFinanciereHeader().getDateDebut();
                }

                //
                if (timeFinPlage != null) {
                    if ((dateFinPlage == null) || (timeFinDF == null) || (timeFinDF.getTime() > timeFinPlage.getTime())) {
                        timeFinPlage = timeFinDF;
                        dateFinPlage = dfh.getSimpleDonneeFinanciereHeader().getDateFin();
                    }
                }
            }
        }

        if ((dateFinPlage == null) || (timeFinPlage != null)) {
            timeFinPlage = null;
        }
        if ((dateFinPlage != null) && JadeDateUtil.isGlobazDateMonthYear(dateFinPlage)) {
            dateFinPlage = "01." + dateFinPlage;
        }

        if (dateDebutPlage == null) {
            throw new CalculBusinessException("pegasus.calcul.periodes.recalcul.changement.mandatory");
        }
        // si la date de debut de la plage est antérieur à la date de début de validité de la demande
        // cette date de validite a ete définis lors de la validation de la version 1 du droit
        String dateDebutValiditeDemande = droit.getDemande().getSimpleDemande().getDateDebut();
        // Ne doit jamais arrivé..., mais...
        if (dateDebutValiditeDemande == null) {
            throw new CalculException(
                    "The dateDebut demande which should be setted in the validation of the initial version of the droit is null!");
        }
        // Plafonnement de la date de debut avec la date de debut de la demande, la date de but de la donnee financiere
        // ne doit pas etre avant la date de dbut de la demande
        if (JadeDateUtil.isDateAfter("01." + dateDebutValiditeDemande, "01." + dateDebutPlage)) {
            dateDebutPlage = dateDebutValiditeDemande;
        }

        // Plaffonnement avec la date de fin de la demande, si pas vide
        String dateFinDemande = droit.getDemande().getSimpleDemande().getDateFin();
        if (!JadeStringUtil.isBlank(dateFinDemande)) {
            dateFinPlage = "01." + dateFinDemande;
        }

        return new String[] { "01." + dateDebutPlage, dateFinPlage };
    }

    /**
     * Recherche la date de début de la plage de calcul en cas d'un premier calcul du droit.
     * 
     * @param droit
     *            Le droit dont la plage de calcul doit être déterminé.
     * @return La date de début de plage, ou null si pas de rente.
     * @throws JadePersistenceException
     *             en cas de problème dans la persistence.
     * @throws CalculException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    private String[] cherchePlageInitial(Droit droit) throws JadePersistenceException, CalculException,
            DemandeException, JadeApplicationServiceNotAvailableException {
        String dateDebutRentes = null;
        final String dateDepotDemandePC = droit.getDemande().getSimpleDemande().getDateDepot();

        // check si demande avant
        DemandeSearch demandeSearch = new DemandeSearch();
        demandeSearch.setForIdDossier(droit.getDemande().getDossier().getDossier().getIdDossier());
        // on filtre la demande elle-meme
        demandeSearch.setOrderKey(DemandeSearch.ORDER_DATE_DEBUT_DESC);
        demandeSearch.setForIdDemandeNotEquals(droit.getDemande().getId());
        demandeSearch = PegasusServiceLocator.getDemandeService().search(demandeSearch);

        // date de demande
        String dateFinDemandePrecedente = null;

        // si plus d'un résultat, alors on a une demande précédente, forcpément close
        if (demandeSearch.getSearchResults().length > 0) {
            // iteration sur les demandes
            for (JadeAbstractModel demandePrecedente : demandeSearch.getSearchResults()) {
                String dateFin = (((Demande) demandePrecedente).getSimpleDemande().getDateFin());
                // on prend la premiere demande non close, ordreé par date de debut
                if (!JadeStringUtil.isBlankOrZero(dateFin) && (dateFinDemandePrecedente == null)) {
                    // La date de fin de la demande est antérieur
                    dateFinDemandePrecedente = ((Demande) demandePrecedente).getSimpleDemande().getDateFin();
                }
            }
        }
        // charge données
        CalculPlageInitialSearch search = new CalculPlageInitialSearch();
        search.setForIdVersionDroit(droit.getSimpleVersionDroit().getId());
        search = (CalculPlageInitialSearch) JadePersistenceManager.search(search);
        // recherche de la date de la rente la plus ancienne, pour avoir une
        // plage de calcul qui couvre le plus de données
        for (JadeAbstractModel abstractRente : search.getSearchResults()) {
            CalculPlageInitial rente = (CalculPlageInitial) abstractRente;

            // calcule la date de début de plage pour cette rente
            String dateRente = calculeDatePlageInitial(rente, dateDepotDemandePC);
            if (dateDebutRentes != null) {
                dateDebutRentes = JadeDateUtil.getOldestDate(dateDebutRentes, dateRente);
            } else {
                dateDebutRentes = dateRente;
            }
        }
        if (dateDebutRentes == null) {
            throw new CalculBusinessException("pegasus.calcul.periodes.rente.mandatory");
        }

        if (JadeDateUtil.isDateBefore(dateDebutRentes, "01." + dateFinDemandePrecedente)) {
            dateDebutRentes = JadeDateUtil.addMonths("01." + dateFinDemandePrecedente, 1);
        }

        return new String[] { dateDebutRentes, null };
    }

    @Override
    public String getDateDebutSansRetroactif(Droit droit, String dateDebutOriginal) throws CalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Récupère la dernière pca de la version précédente du droit
        CalculDernierePCASearch search = new CalculDernierePCASearch();
        search.setForNoVersionDroit(String.valueOf(Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion()) - 1));
        search.setForIdDroit(droit.getSimpleDroit().getIdDroit());
        search.setDefinedSearchSize(1);
        search.setOrderKey("byDateDebut");
        search = PegasusImplServiceLocator.getCalculDonneesDroitService().calculDernierePCASearch(search);

        if (search.getSize() == 1) {
            return "01." + ((CalculDernierePCA) search.getSearchResults()[0]).getDateDebut();
        }

        return dateDebutOriginal;
    }

    /**
     * Retourne la date de décsision de la rente. Ce provider retourne le champ date spécifique au type de donnée
     * financière.
     * 
     * @param rente
     *            Objet model de la rente
     * @return la date de décision, ou null
     */
    private String getDateDecisionRente(CalculPlageInitial rente) {
        if (IPCDroits.CS_RENTE_AVS_AI.equals(rente.getCsTypeDonneeFinanciere())) {
            return rente.getDateDecisionAvsAi();
        } else if (IPCDroits.CS_IJAI.equals(rente.getCsTypeDonneeFinanciere())) {
            return rente.getDateDecisionIjAi();
        } else if (IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE.equals(rente.getCsTypeDonneeFinanciere())) {
            return rente.getDateDecisionApiAvsAi();
        }
        return null;
    }

    /**
     * Retourne la date de dépot de la rente. Ce provider retourne le champ date spécifique au type de donnée
     * financière.
     * 
     * @param rente
     *            Objet model de la rente
     * @return la date de dépot, ou null
     */
    private String getDateDepotRente(CalculPlageInitial rente) {
        if (IPCDroits.CS_RENTE_AVS_AI.equals(rente.getCsTypeDonneeFinanciere())) {
            return rente.getDateDepotAvsAi();
        } else if (IPCDroits.CS_IJAI.equals(rente.getCsTypeDonneeFinanciere())) {
            return rente.getDateDepotIjAi();
        } else if (IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE.equals(rente.getCsTypeDonneeFinanciere())) {
            return rente.getDateDepotAvsAi();
        }
        return null;
    }

    private Set<String> getDatesPartagePeriodes(Droit droit,
            Map<String, JadeAbstractSearchModel> cacheDonneesPersistence, String dateDebut,
            DonneesHorsDroitsProvider containerGlobal) throws CalculException {

        // crée liste de dates
        Set<String> listeDates = new TreeSet<String>(new DatesPartageComparator());

        ConditionPartageFactory conditionFactory = ConditionPartageFactory.getInstance();
        // parcours des conditions
        for (IConditionPartagePeriode condition : conditionFactory.getListeConditions()) {
            Collection<String> conditionDates = condition.calculateDates(dateDebut, cacheDonneesPersistence,
                    containerGlobal);
            // FIX: si un null est dans le lot, il faut l'enlever. La collection
            // doit supporter le null.
            conditionDates.remove(null);
            listeDates.addAll(conditionDates);
        }

        return listeDates;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.calcul.PeriodesService#
     * getDonneesCalculDroit(ch.globaz.pegasus.business.models.droit.Droit, java.lang.String)
     */
    @Override
    public Map<String, JadeAbstractSearchModel> getDonneesCalculDroit(Droit droit, String debutPlage,
            String dateFinPlage) throws CalculException, JadePersistenceException, DroitException, PeriodeException,
            MonnaieEtrangereException, AutreRenteException, ForfaitsPrimesAssuranceMaladieException {
        Map<String, JadeAbstractSearchModel> result = new HashMap<String, JadeAbstractSearchModel>();

        try {
            CalculMembreFamilleSearch calculMembreFamilleSearch = new CalculMembreFamilleSearch();
            calculMembreFamilleSearch.setForIdDroit(droit.getId());
            calculMembreFamilleSearch = PegasusImplServiceLocator.getCalculMembreFamilleService().search(
                    calculMembreFamilleSearch);
            result.put(ConstantesCalcul.CONTAINER_DONNEES_PERSONNE, calculMembreFamilleSearch);

            // récupère données financières liées au droit
            CalculDonneesDroitSearch searchDonneesDroit = new CalculDonneesDroitSearch();
            searchDonneesDroit.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchDonneesDroit.setForIdDroit(droit.getId());
            // searchDonneesDroit.setForDateDebut(debutPlage);
            searchDonneesDroit.setForDateFin(debutPlage);
            searchDonneesDroit = PegasusImplServiceLocator.getCalculDonneesDroitService().search(searchDonneesDroit);

            result.put(ConstantesCalcul.CONTAINER_DONNEES_DROIT, searchDonneesDroit);

            // récupère primes moyennes assurance maladie
            DonneesPersonnellesSearch donneesPersonnellesSearch = new DonneesPersonnellesSearch();
            donneesPersonnellesSearch.setForIdDroit(droit.getSimpleDroit().getId());
            donneesPersonnellesSearch.setDefinedSearchSize(1);
            donneesPersonnellesSearch.setWhereKey(DonneesPersonnellesSearch.FOR_DROIT);
            donneesPersonnellesSearch = PegasusServiceLocator.getDroitService().searchDonneesPersonnelles(
                    donneesPersonnellesSearch);
            if (donneesPersonnellesSearch.getSize() > 0) {
                JadeAbstractModel[] l = donneesPersonnellesSearch.getSearchResults();
                String idDerniereLocalite = ((DonneesPersonnelles) l[0]).getLocalite().getIdLocalite();

                ForfaitPrimeAssuranceMaladieLocaliteSearch assuranceMaladieLocaliteSearch = new ForfaitPrimeAssuranceMaladieLocaliteSearch();
                assuranceMaladieLocaliteSearch.setForIdLocalite(idDerniereLocalite);

                String forDateDebut = debutPlage;
                String forDateFin = JadeDateUtil.addDays(dateFinPlage, 1);
                assuranceMaladieLocaliteSearch.setForDateDebut(forDateDebut);
                assuranceMaladieLocaliteSearch.setForDateFin(forDateFin);
                assuranceMaladieLocaliteSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                assuranceMaladieLocaliteSearch = PegasusServiceLocator.getParametreServicesLocator()
                        .getForfaitPrimeAssuranceMaladieLocaliteService().search(assuranceMaladieLocaliteSearch);

                // si aucun forfait n'est trouvé pour la localité du requerant, inutile de continuer le calcul
                if (assuranceMaladieLocaliteSearch.getSize() == 0) {
                    DonneesPersonnelles dp;
                    try {
                        DroitMembreFamilleSearch searchModel = new DroitMembreFamilleSearch();
                        searchModel.setForIdDroit(droit.getSimpleDroit().getIdDroit());
                        searchModel.setForCsRoletMembreFamilleIn(new ArrayList<String>() {
                            /**
                             * 
                             */
                            private static final long serialVersionUID = 1L;

                            {
                                this.add(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                            }
                        });
                        searchModel = PegasusImplServiceLocator.getDroitMembreFamilleService().search(searchModel);
                        if (searchModel.getSize() == 0) {
                            throw new CalculException("No requerant found!");
                        }

                        DroitMembreFamille dmf = (DroitMembreFamille) searchModel.getSearchResults()[0];
                        dp = PegasusImplServiceLocator.getDonneesPersonnellesService().read(
                                dmf.getSimpleDroitMembreFamille().getIdDonneesPersonnelles());
                    } catch (Exception e) {
                        throw new CalculException("Failed to gather information for a calculBusinessException!", e);
                    }
                    String nomLocalite = dp.getLocalite().getNumPostal();
                    throw new CalculBusinessException("pegasus.calcul.primeAssurance.forfait.mandatory", nomLocalite,
                            forDateDebut, forDateFin);
                }

                result.put(ConstantesCalcul.CONTAINTER_PRIME_MOYENNE_ASSURANCE_MALADIE, assuranceMaladieLocaliteSearch);
            }

            AutreRenteCalculSearch autreRenteCalculSearch = new AutreRenteCalculSearch();
            autreRenteCalculSearch.setWhereKey("calcul");
            autreRenteCalculSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            autreRenteCalculSearch.setForIdDroit(droit.getId());
            autreRenteCalculSearch = PegasusImplServiceLocator.getAutreRenteCalculService().search(
                    autreRenteCalculSearch);
            result.put(ConstantesCalcul.CONTAINER_AUTRES_RENTES, autreRenteCalculSearch);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("Service not available - " + e.getMessage());
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.calcul.PeriodesService#
     * recherchePeriodesCalcul(ch.globaz.pegasus.business.models.droit.Droit, java.lang.String)
     */
    @Override
    public List<PeriodePCAccordee> recherchePeriodesCalcul(Droit droit, String debutPlage, String finPlage,
            Map<String, JadeAbstractSearchModel> cacheDonneesPersistence, DonneesHorsDroitsProvider containerGlobal,
            boolean isDateFinForce) throws CalculException, JadePersistenceException {

        if (containerGlobal == null) {
            throw new CalculException("donneesHorsDroitProvider is null!");
        }
        // Recherche des dates de partages
        Set<String> listeDatesPartage = getDatesPartagePeriodes(droit, cacheDonneesPersistence, debutPlage,
                containerGlobal);

        // pour correction problème si date de fin --> fin d'année
        String dateFinPlage = JadeDateUtil.addDays(finPlage, 1);

        // supression des dates sortant de la plage
        for (Iterator<String> itDates = listeDatesPartage.iterator(); itDates.hasNext();) {
            String d = itDates.next();

            if (JadeDateUtil.isDateBefore(d, debutPlage) || JadeDateUtil.isDateAfter(d, dateFinPlage)) {
                itDates.remove();
            }
        }

        // Création des périodes
        List<PeriodePCAccordee> periodes = new ArrayList<PeriodePCAccordee>(listeDatesPartage.size() + 1);
        // depuis le début de la plage...
        PeriodePCAccordee periode = new PeriodePCAccordee();
        periode.setStrDateDebut(debutPlage);
        for (String datePartage : listeDatesPartage) {
            // ...pour chaque date de partage...
            // convertir en dd.MM.aaaa si nécessaire
            if ((datePartage.length() == 5) || (datePartage.length() == 7)) {
                datePartage = "01." + datePartage;
            }
            // verifie que debut!=fin
            if (!datePartage.equals(periode.getStrDateDebut())) {
                // créer période
                periode.setStrDateFin(JadeDateUtil.addDays(datePartage, -1));
                periodes.add(periode);
                periode = new PeriodePCAccordee();
                periode.setStrDateDebut(datePartage);
            }
        }
        // ...jusqu'à la date de fin de la plage, i.e. maintenant
        // Si la date de fin de la demande est setter, on plafonne la date de fin de la pca
        if (!JadeStringUtil.isBlank(droit.getDemande().getSimpleDemande().getDateFin())) {
            periode.setDateFinDemandeToClosePca(droit.getDemande().getSimpleDemande().getDateFin());
            periode.setStrDateFin(droit.getDemande().getSimpleDemande().getDateFin());
            periode.setDateFin(JadeDateUtil.getGlobazDate("01." + droit.getDemande().getSimpleDemande().getDateFin()));
        }
        // Si on est dans le cas du forcage de la date fin
        else if (isDateFinForce) {
            periode.setDateFinDemandeToClosePca(finPlage);
            periode.setStrDateFin(finPlage);
            periode.setDateFin(JadeDateUtil.getGlobazDate(finPlage));
        } else {
            periode.setDateFin(null);
        }
        periodes.add(periode);

        return periodes;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.calcul.PeriodesService#
     * recherchePlageCalcul(ch.globaz.pegasus.business.models.droit.Droit)
     */
    @Override
    public String[] recherchePlageCalcul(Droit droit) throws JadePersistenceException, CalculException,
            DemandeException, JadeApplicationServiceNotAvailableException {
        String[] result = null;
        // Si on a une demande purement rétroactive, on ne considèe pas la plage comme initiale car on a déja une date
        // de fin
        if ((Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion()) == 1)
                && !droit.getDemande().getSimpleDemande().getIsPurRetro()) {
            result = cherchePlageInitial(droit);
        } else {
            try {
                result = cherchePlageChangementVersion(droit);
            } catch (DonneeFinanciereException e) {
                throw new CalculException("Couldn't determine date plage calcul", e);
            }
        }

        // la date de debut ne doit pas etre null
        if (result[0] != null) {
            // arrondit la date au premier jour du mois
            result[0] = "01" + result[0].substring(2);
        } else {
            throw new CalculException("Date plage calcul shouldn't be null!");
        }

        // verification que la plage de calcul ne soit pas déjà occupée, et réduire la plage si besoin
        String idRequerant = droit.getDemande().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers();

        CalculPlagesExistantesSearch search = new CalculPlagesExistantesSearch();
        search.setForIdRequerant(idRequerant);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = (CalculPlagesExistantesSearch) JadePersistenceManager.search(search);

        for (JadeAbstractModel absDonnee : search.getSearchResults()) {
            CalculPlagesExistantes donnee = (CalculPlagesExistantes) absDonnee;

            if (!donnee.getIdVersionDroit().equals(droit.getSimpleVersionDroit().getIdVersionDroit())) {

                if (JadeStringUtil.isEmpty(donnee.getDateFin())) {
                    throw new CalculException("The dateFin from donnee (CalculPagesExistantes) is empty");
                } else {
                    if (JadeDateUtil.isDateBefore(result[0], donnee.getDateFin())) {
                        result[0] = donnee.getDateFin();
                    }
                }
            }

        }

        return result;
    }

}
