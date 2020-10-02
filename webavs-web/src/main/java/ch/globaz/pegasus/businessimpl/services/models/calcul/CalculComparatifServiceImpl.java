/**
 *
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.ConstantesCalcul;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.calcul.HomeCalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.calcul.*;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocalite;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.calcul.CalculComparatifService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.calcul.joursAppoint.CalculJourAppoint;
import ch.globaz.pegasus.businessimpl.services.models.calcul.joursAppoint.PrepareDonneForJourAppoint;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PersonnePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.*;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.math.BigDecimal;
import java.util.*;

/**
 * Classe de calcul des PC Accordées.
 *
 * @author ECO
 * @author SCE
 */
public class CalculComparatifServiceImpl extends PegasusAbstractServiceImpl implements CalculComparatifService {

    /**
     * Mise à jour des jours d'appoint. Dans le cas ou la propriété boolééen est activée, un calcul de sjours d'appoint
     * doit être effectué
     *
     * @param newPca
     *            la nouvelle pcAccordée
     * @return la nouvelle pc avec les jours d'appoints calculé
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static PCAccordee updateJoursAppoint(PCAccordee newPca) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        try {
            try {
                if (PCproperties.getBoolean(EPCProperties.GESTION_JOURS_APPOINTS)) {

                    PCAccordeeSearch searchForJA = new PCAccordeeSearch();
                    searchForJA.setForIdDemande(newPca.getSimpleDroit().getIdDemandePC());
                    searchForJA.setForIdDroit(newPca.getSimpleDroit().getIdDroit());
                    searchForJA.setForDateDebut(newPca.getSimplePCAccordee().getDateDebut());
                    searchForJA.setWhereKey("forJoursAppointUpdatePCA");
                    searchForJA.setOrderKey("byDateDebut");
                    searchForJA = PegasusServiceLocator.getPCAccordeeService().search(searchForJA);

                    if (searchForJA.getSize() > 0) {
                        BigDecimal oldMontant = new BigDecimal(((PCAccordee) searchForJA.getSearchResults()[0])
                                .getSimplePrestationsAccordees().getMontantPrestation());

                        SimpleJoursAppoint joursAppoint = newPca.getFirstJoursAppoint();

                        if (joursAppoint != null) {
                            CalculJourAppoint calculJourAppoint = new CalculJourAppoint();
                            calculJourAppoint.updateMontantJoursAppoint(joursAppoint, new BigDecimal(newPca
                                    .getSimplePrestationsAccordees().getMontantPrestation()), oldMontant);
                            PegasusImplServiceLocator.getSimpleJoursAppointService().update(joursAppoint);
                        }
                    }
                    return newPca;
                }
            } catch (PropertiesException e) {
                throw new PCAccordeeException("unable to obtain ther properties", e);
            }
        } catch (NumberFormatException e) {
            throw new PCAccordeeException("Number formate exception", e);
        }
        return newPca;
    }

    /**
     * Calcul des pcAccordées
     */
    @Override
    public void calculePCAccordes(Droit droit, List<PeriodePCAccordee> listePCAccordes, boolean determineCCFavorable) throws CalculException {

        String dateReforme = null;
        try {
            dateReforme = EPCProperties.DATE_REFORME_PC.getValue();
        } catch (PropertiesException e) {
            throw new CalculException("Unbale to obtain properties for reforme pc", e);
        }
        for (PeriodePCAccordee periodePCAccordee : listePCAccordes) {
            if (!periodePCAccordee.isCalculReforme() || !JadeDateUtil.isDateBefore(JadeDateUtil.getGlobazFormattedDate(periodePCAccordee.getDateDebut()), dateReforme)) {
                periodePCAccordee.finaliseCC(droit);

                if (determineCCFavorable) {
                    // determine maintenant le calcul le plus favorable, sinon pour comparaison ancien/nouveau(reforme) sera fait plus tard
                    periodePCAccordee.determineCCFavorable();
                }
            }
        }

    }

    @Override
    public void calculJoursAppoint(List<PeriodePCAccordee> listePCAccordes,
                                   CalculPcaReplaceSearch calculPcaReplaceSearch) throws CalculException {
        PrepareDonneForJourAppoint donneForJourAppoint = new PrepareDonneForJourAppoint();

        donneForJourAppoint.addJourAppointInPeriodeIfNeeded(listePCAccordes, calculPcaReplaceSearch);
    }

    /**
     * Consolidations des variables métiers. Via le provider
     *
     * @param listePCAccordes
     * @param cacheDonneesBD
     * @param containerGlobal
     */
    private void consolidationVariablesMetier(List<PeriodePCAccordee> listePCAccordes,
                                              Map<String, JadeAbstractSearchModel> cacheDonneesBD, DonneesHorsDroitsProvider containerGlobal) {

        // VariableMetierProvider varMetProvider = containerGlobal.;
        // date du premier taux OFAS, si pas de valeurs
        final long datePremierTauxOFAS = Collections.min(containerGlobal.getTauxOFAS().getVariablesMetiers().keySet());

        for (PeriodePCAccordee periodePCA : listePCAccordes) {
            // Ajout des variables metiers aux périodePCA
            for (VariableMetier varMetier : containerGlobal.getListeVariablesMetiers()) {
                periodePCA.getControlleurVariablesMetier().put(varMetier.getCsTypeVariableMetier(),
                        new ControlleurVariablesMetier(varMetier, periodePCA.getDateDebut().getTime()));

            }
            // gestion taux PFAS, si pas de taux OFAS, on ajoute la valeur par defaut
            if (!periodePCA.getControlleurVariablesMetier().containsKey(IPCVariableMetier.CS_2091_DPC)) {
                periodePCA.getControlleurVariablesMetier().put(IPCVariableMetier.CS_2091_DPC,
                        new ControlleurVariablesMetier(containerGlobal.getTauxOFAS(), datePremierTauxOFAS));
            }

        }

    }

    @Override
    public void consolideCacheDonneesPersonnes(List<PeriodePCAccordee> listePCAccordes,
                                               Map<String, JadeAbstractSearchModel> cacheDonneesBD, Map<String, CalculMembreFamille> listePersonnes,
                                               String dateFinPlage, DonneesHorsDroitsProvider containerGlobal, boolean isReforme, boolean isFratrie) throws CalculException {
        for (PeriodePCAccordee periodePCAccordee : listePCAccordes) {
            periodePCAccordee.setCalculReforme(isReforme);
            periodePCAccordee.setFratrie(isFratrie);
        }

        consolideDonneesFinancieres(listePCAccordes, cacheDonneesBD, listePersonnes, dateFinPlage);

        // tri des variables metier

        consolidationVariablesMetier(listePCAccordes, cacheDonneesBD, containerGlobal);

        consolideMonnaiesEtrangeres(listePCAccordes, cacheDonneesBD, containerGlobal);
        // tri des primes moyennes d'assurance maladie
        ForfaitPrimeAssuranceMaladieLocaliteSearch assMalLocaliteSearchModel = (ForfaitPrimeAssuranceMaladieLocaliteSearch) cacheDonneesBD
                .get(ConstantesCalcul.CONTAINTER_PRIME_MOYENNE_ASSURANCE_MALADIE);

        for (PeriodePCAccordee periodePCAccordee : listePCAccordes) {

            // cherche les primes d'ass. mal. concernées
            String anneePeriode = periodePCAccordee.getStrDateDebut();
            for (JadeAbstractModel absModel : assMalLocaliteSearchModel.getSearchResults()) {
                ForfaitPrimeAssuranceMaladieLocalite forfait = (ForfaitPrimeAssuranceMaladieLocalite) absModel;
                String dateForfait = forfait.getSimpleForfaitPrimesAssuranceMaladie().getDateDebut();
                if (anneePeriode.substring(6).equals(dateForfait.substring(6))) {
                    periodePCAccordee.addForfaitPrimeMaladie(forfait.getSimpleForfaitPrimesAssuranceMaladie());
                }
            }

            // parcourt les périodes pour executer les stratégies
            // cherche par la même occasion les entrées en home (requiert que les pca soient triés par date).
            periodePCAccordee.consolideDonnees();
        }

        // les données consolidées sont maintenant prêtes à être calculées

    }

    private void consolideDonneesFinancieres(List<PeriodePCAccordee> listePCAccordes,
                                             Map<String, JadeAbstractSearchModel> cacheDonneesBD, Map<String, CalculMembreFamille> listePersonnes,
                                             String dateFinPlage) {
        CalculDonneesCCSearch searchModel = (CalculDonneesCCSearch) cacheDonneesBD
                .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT_CC);

        // Map de relation periode --> ListType de chambre pour une période
        Map<PeriodePCAccordee, List<PersonneTypeChambre>> mapIdTypeChambre = new HashMap<PeriodePCAccordee, List<PersonneTypeChambre>>();

        Integer sizeFamille = cacheDonneesBD
                .get(ConstantesCalcul.CONTAINER_DONNEES_PERSONNE).getSize();

        // parcourt liste de données de persistance
        for (globaz.jade.persistence.model.JadeAbstractModel absDonnee : searchModel.getSearchResults()) {
            final CalculDonneesCC donnee = (CalculDonneesCC) absDonnee;

            // récupère infos de tri de la donnée
            String strDateDebut = "01." + donnee.getDateDebutDonneeFinanciere();
            String strDateFin = donnee.getDateFinDonneeFinanciere();
            strDateFin = PegasusDateUtil.setDateMaxDayOfMonth(strDateFin);

            // si la date de fin est indéfini(vide), considérer que la donnée
            // financière est "effective"(valide) sans limite de fin.
            // Pratiquement, vu que le calcul d'un droit se fait jusqu'à
            // aujourd'hui, la limite de fin est la date de prochain paiement, ou le mois après si le calcul n'est pas
            // retroactif.
            if (JadeStringUtil.isEmpty(strDateFin)) {
                strDateFin = JadeDateUtil.addDays(dateFinPlage, 1);
            }
            String idPersonne = donnee.getIdMembreFamilleSF();
            String idDroitPersonne = donnee.getIdDroitMembreFamille();
            String csRoleFamille = donnee.getCsRoleFamille();

            // cherche les périodes de calcul dont la donnée financière
            // influence le calcul pour y ajouter la donnée financière. Par la
            // même occasion, détermine
            for (PeriodePCAccordee periodePCAccordee : listePCAccordes) {
                String periodeDateDebut = periodePCAccordee.getStrDateDebut();
                if (!JadeDateUtil.isDateBefore(periodeDateDebut, strDateDebut)
                        && !JadeDateUtil.isDateAfter(periodeDateDebut, strDateFin)) {
                    // cherche ou crée la personne titulaire de la donnée
                    // financière
                    PersonnePCAccordee personne;
                    if (periodePCAccordee.getPersonnes().containsKey(idPersonne)) {
                        personne = periodePCAccordee.getPersonnes().get(idPersonne);
                    } else {
                        personne = new PersonnePCAccordee(idPersonne, csRoleFamille);
                        periodePCAccordee.getPersonnes().put(idPersonne, personne);
                        personne.setIdDroitPersonne(idDroitPersonne);
                    }
                    personne.getDonneesBD().add(donnee);
                    personne.setDateNaissance(listePersonnes.get(idPersonne).getDateNaissance());

                    // Permet de connaître le type de chambre concerné par la période
                    if (IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {

                        if (mapIdTypeChambre.containsKey(periodePCAccordee)) {
                            mapIdTypeChambre.get(periodePCAccordee).add(
                                    new PersonneTypeChambre(idPersonne, donnee.getTaxeJournaliereIdTypeChambre()));
                        } else {
                            List<PersonneTypeChambre> personnesTypeChambre = new ArrayList<PersonneTypeChambre>(2);
                            personnesTypeChambre.add(new PersonneTypeChambre(idPersonne, donnee
                                    .getTaxeJournaliereIdTypeChambre()));
                            mapIdTypeChambre.put(periodePCAccordee, personnesTypeChambre);
                        }
                    }
                }
            }
            donnee.setNbTotalFamille(sizeFamille.toString());

        }

        // Ajout du conjoint dans la liste des personnes le cas échéant --> si pas de données financières
        CalculMembreFamilleSearch mbrFamSearch = (CalculMembreFamilleSearch) cacheDonneesBD
                .get(ConstantesCalcul.CONTAINER_DONNEES_PERSONNE);
        // Si on a un conjoint dans la liste
        for (JadeAbstractModel membre : mbrFamSearch.getSearchResults()) {
            CalculMembreFamille membreFamille = (CalculMembreFamille) membre;
            if (membreFamille.getSimpleDroitMembreFamille().getCsRoleFamillePC()
                    .equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
                // /iteration sur la liste des periodePCA
                for (PeriodePCAccordee pca : listePCAccordes) {
                    // Si pas de conjoints on l'ajoute
                    if (pca.getPersonneByCsRole(IPCDroits.CS_ROLE_FAMILLE_CONJOINT) == null) {
                        PersonnePCAccordee personne = new PersonnePCAccordee(membreFamille
                                .getSimpleDroitMembreFamille().getIdMembreFamilleSF(), membreFamille
                                .getSimpleDroitMembreFamille().getCsRoleFamillePC());
                        pca.getPersonnes().put(membreFamille.getSimpleDroitMembreFamille().getIdMembreFamilleSF(),
                                personne);
                        personne.setIdDroitPersonne(membreFamille.getSimpleDroitMembreFamille()
                                .getIdDroitMembreFamille());
                        personne.setDateNaissance(membreFamille.getDateNaissance());
                    }
                }

            }
        }
        // parcours les données des homes
        if (cacheDonneesBD.containsKey(ConstantesCalcul.CONTAINER_DONNEES_HOMES)) {
            CalculDonneesHomeSearch homeSearchModel = (CalculDonneesHomeSearch) cacheDonneesBD
                    .get(ConstantesCalcul.CONTAINER_DONNEES_HOMES);
            for (JadeAbstractModel absDonnee : homeSearchModel.getSearchResults()) {
                CalculDonneesHome donnee = (CalculDonneesHome) absDonnee;
                String dateDebutHome = "01." + donnee.getDateDebutPeriode();
                String dateFinHome = PegasusDateUtil.setDateMaxDayOfMonth(donnee.getDateFinPeriode());
                String dateDebutChambre = "01." + donnee.getDateDebutPrixChambre();
                String dateFinChambre = PegasusDateUtil.setDateMaxDayOfMonth(donnee.getDateFinPrixChambre());

                for (PeriodePCAccordee periodePCAccordee : listePCAccordes) {
                    List<PersonneTypeChambre> personneChambres = mapIdTypeChambre.get(periodePCAccordee);

                    // on s'assure que la map des types de chambre contient une chambre pour la période
                    if (mapIdTypeChambre.containsKey(periodePCAccordee)) {

                        // on itere sur les 2 resultats possibles
                        for (PersonneTypeChambre personneDansChambre : personneChambres) {
                            // Si on a bien une chambre du type voulu on l'ajoute
                            if (personneDansChambre.getIdTypeChambre().equals(donnee.getIdTypeChambre())) {
                                String periodeDateDebut = periodePCAccordee.getStrDateDebut();
                                String periodeDateFin = periodePCAccordee.getStrDateFin();
                                if (isIntersecting(dateDebutHome, dateFinHome, periodeDateDebut, periodeDateFin)
                                        && isIntersecting(dateDebutChambre, dateFinChambre, periodeDateDebut,
                                        periodeDateFin)) {
                                    periodePCAccordee.getDonneesHomes().add(donnee);

                                    for (JadeAbstractModel membre : mbrFamSearch.getSearchResults()) {
                                        CalculMembreFamille membreFamille = (CalculMembreFamille) membre;
                                        RoleMembreFamille roleMembreFamille = RoleMembreFamille.fromValue(membreFamille
                                                .getSimpleDroitMembreFamille().getCsRoleFamillePC());
                                        if (membreFamille.getId().equals(donnee.getIdMembreFamille())) {
                                            if (roleMembreFamille.isConjoint() || roleMembreFamille.isRequerant()) {
                                                periodePCAccordee.getPersonneByCsRole(roleMembreFamille.getValue())
                                                        .setHome(donnee);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void consolideMonnaiesEtrangeres(List<PeriodePCAccordee> listePCAccordes,
                                             Map<String, JadeAbstractSearchModel> cacheDonneesBD, DonneesHorsDroitsProvider containerGlobalCalcul)
            throws CalculException {

        for (PeriodePCAccordee periodePCA : listePCAccordes) {
            // Ajout des monnaieEtrangeres aux périodePCA
            for (ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.MonnaieEtrangere monnaieTrangere : containerGlobalCalcul
                    .getListeMonnaiesEtrangeres()) {
                periodePCA.getControlleurMonnaiesEtrangere().put(monnaieTrangere.getCsTypeMonnaieEtrangere(),
                        new ControlleurMonnaieEtrangere(monnaieTrangere, periodePCA.getDateDebut().getTime()));

            }
        }

    }

    /**
     * Test la cohérence entre la liste des types de chambres et le modèle de recheche des homes. rendu nécessaire par
     * le fait qu'un home peut avoir été saisi sans prix pour la période.
     *
     * @param homeSearch
     * @param listTypeChambre
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws HomeException
     * @throws TaxeJournaliereHomeException
     */
    private ArrayList<String> dealHomeCoherenceError(CalculDonneesHomeSearch homeSearch,
                                                     List<CalculDonneesCC> listTypeChambre) throws TaxeJournaliereHomeException, HomeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ArrayList<String> errorList = new ArrayList<String>();
        // Si recherche vide, on traite les erreurs
        if (homeSearch.getSearchResults().length == 0) {
            for (CalculDonneesCC donne : listTypeChambre) {
                errorList.add(getInfoHomeForErrorHandling(donne.getIdDonneeFinanciereHeader()));
            }
        } else {
            // parcours des homes du modele de recherche
            for (CalculDonneesCC donne : listTypeChambre) {
                boolean hasFindHome = false;
                for (JadeAbstractModel data : homeSearch.getSearchResults()) {
                    CalculDonneesHome home = ((CalculDonneesHome) data);
                    if ((home != null) && (home.getIdTypeChambre() != null)
                            && home.getIdTypeChambre().equals(donne.getTaxeJournaliereIdTypeChambre())) {
                        hasFindHome = true;
                    }
                }

                if (!hasFindHome) {
                    errorList.add(getInfoHomeForErrorHandling(donne.getIdDonneeFinanciereHeader()));
                }
            }
        }

        return errorList;
    }

    private String getInfoHomeForErrorHandling(String idDonneFianciere) throws TaxeJournaliereHomeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, HomeException {
        String errorString = null;
        SimpleTaxeJournaliereHomeSearch txJhom = new SimpleTaxeJournaliereHomeSearch();
        txJhom.setForIdDonneeFinanciere(idDonneFianciere);
        txJhom = PegasusImplServiceLocator.getSimpleTaxeJournaliereHomeService().search(txJhom);

        if (txJhom.getSize() > 0) {
            SimpleTaxeJournaliereHome taxeJourHome = (SimpleTaxeJournaliereHome) txJhom.getSearchResults()[0];

            Home home = new Home();
            home = PegasusServiceLocator.getHomeService().read(taxeJourHome.getIdHome());

            if (home != null) {

                SimplePeriodeServiceEtatSearch serviceEtatSearch = new SimplePeriodeServiceEtatSearch();

                serviceEtatSearch.setWhereKey(SimplePeriodeServiceEtatSearch.CHECL_FOR_ANTERIEURS_PERIODES);
                serviceEtatSearch.setForIdHome(home.getSimpleHome().getId());
                if (!JadeStringUtil.isBlankOrZero(taxeJourHome.getDateEntreeHome())) {
                    serviceEtatSearch.setForDateDebutBefore(taxeJourHome.getDateEntreeHome().substring(3));
                } else {
                    SimpleDonneeFinanciereHeader donneeFinanciereHeader;
                    try {
                        donneeFinanciereHeader = PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                                .read(taxeJourHome.getIdDonneeFinanciereHeader());
                        serviceEtatSearch.setForDateDebutBefore(donneeFinanciereHeader.getDateDebut());

                    } catch (DonneeFinanciereException e) {
                        throw new HomeException("Unable to read the donneFinanciereHeader with this id:"
                                + taxeJourHome.getIdDonneeFinanciereHeader());
                    }

                }
                int nb = 0;
                try {
                    nb = PegasusImplServiceLocator.getSimplePeriodeServiceEtatService().count(serviceEtatSearch);
                } catch (PeriodeServiceEtatException e1) {
                    throw new HomeException(
                            "Unable to obtain informations on the adresse for the home with this idTiers :"
                                    + home.getSimpleHome().getId(), e1);
                }
                if (nb > 0) {
                    errorString = BSessionUtil.getSessionFromThreadContext().getLabel(
                            "CALCUL_PRIX_CHAMBRES_HOME_INCONNU");
                } else {
                    errorString = BSessionUtil.getSessionFromThreadContext().getLabel("AUCUN_SERVICE_ETAT");

                }

                if (home.getAdresse().getTiers().getDesignation1() != null) {
                    errorString = errorString.replace("{home}", home.getAdresse().getTiers().getDesignation1());
                } else if (home.getSimpleHome().getNomBatiment() != null) {
                    errorString = errorString.replace("{home}", home.getSimpleHome().getNomBatiment());
                } else {
                    errorString = errorString.replace("{home}", "(no home check adresse domicile)");

                }
                errorString = errorString.replace("{periode}", taxeJourHome.getDateEntreeHome());

            }
        } else {
            throw new HomeException("Any one taxeJournaliere found wiht this idDonneFinanciereHeader:"
                    + idDonneFianciere);
        }
        return errorString;

    }

    /**
     * Check if two ranges of date are intersecting.
     *
     * @param date1Start
     *            Start date of the first range (doesn't need to be the oldest range). Throws a NullPointerException if
     *            null or invalid.
     * @param date1End
     *            End date of the first range. If null or invalid, considered as endless. It must be after start date1.
     * @param date2Start
     *            Start date of the second range. Throws a NullPointerException if null or invalid.
     * @param date2End
     *            End date of the second range. If null or invalid, considered as endless. It must be after start date2.
     * @return true if the two ranges are intersecting.
     */
    private boolean isIntersecting(String date1Start, String date1End, String date2Start, String date2End) {

        Calendar cal1Start = JadeDateUtil.getGlobazCalendar(date1Start);
        Calendar cal1End = null;
        Calendar cal2Start = JadeDateUtil.getGlobazCalendar(date2Start);

        // sort periodes by start dates
        if (cal2Start.before(cal1Start)) {
            Calendar tmp = cal1Start;
            // cal1Start = cal2Start; // opération inutilisée
            cal2Start = tmp;

            cal1End = JadeDateUtil.getGlobazCalendar(date2End);
        } else {
            cal1End = JadeDateUtil.getGlobazCalendar(date1End);
        }

        if (cal1End == null) {
            return true;
        }

        // since we ensured now that cal1 starts before or at the same moment as cal2, we only need to check that cal2
        // doesn't start after the end of cal1 to determine if they intersect.
        return !cal2Start.after(cal1End);
    }

    @Override
    public void loadDonneesCalculComparatif(Droit droit, Map<String, JadeAbstractSearchModel> cacheDonnees,
                                            List<PeriodePCAccordee> periodes, Map<String, CalculMembreFamille> listePersonnes, String debutPlage, String dateFinPlage)
            throws CalculException, JadePersistenceException, TaxeJournaliereHomeException, HomeException {
        // Liste des membres de famille
        Set<String> listeFamille = new HashSet<String>();

        CalculDonneesDroitSearch searchModel = (CalculDonneesDroitSearch) cacheDonnees
                .get(ConstantesCalcul.CONTAINER_DONNEES_DROIT);

        if (searchModel.getSize() == 0) {
            throw new CalculException("The number of data in the initial plage shouldn't be null!");
        }

        CalculMembreFamilleSearch mbrFamSearch = (CalculMembreFamilleSearch) cacheDonnees
                .get(ConstantesCalcul.CONTAINER_DONNEES_PERSONNE);
        // On itere sur les donnéée
        for (JadeAbstractModel absDonnee : mbrFamSearch.getSearchResults()) {
            final CalculMembreFamille donnee = (CalculMembreFamille) absDonnee;
            listeFamille.add(donnee.getSimpleDroitMembreFamille().getIdMembreFamilleSF());
            listePersonnes.put(donnee.getSimpleDroitMembreFamille().getIdMembreFamilleSF(), donnee);
        }

        try {
            // récupère données
            CalculDonneesCCSearch searchDonneesCalculModel = new CalculDonneesCCSearch();
            searchDonneesCalculModel.setForIdDroit(droit.getSimpleDroit().getId());
            searchDonneesCalculModel.setForIdMembreFamilleSFIn(new ArrayList<String>(listeFamille));
            searchDonneesCalculModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchDonneesCalculModel.setForDateFin(JadeDateUtil.convertDateMonthYear(debutPlage));
            searchDonneesCalculModel = PegasusImplServiceLocator.getCalculDonneesEnfantsService().search(
                    searchDonneesCalculModel);
            cacheDonnees.put(ConstantesCalcul.CONTAINER_DONNEES_DROIT_CC, searchDonneesCalculModel);

            // récupère données homes
            List<String> listIdTypeChambre = new ArrayList<String>();
            List<CalculDonneesCC> listTypeChambre = new ArrayList<CalculDonneesCC>();
            List<String> listIdLocalite = new ArrayList<>();

            for (JadeAbstractModel absDonnee : searchDonneesCalculModel.getSearchResults()) {
                CalculDonneesCC donnee = (CalculDonneesCC) absDonnee;
                if (IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {
                    listIdTypeChambre.add(donnee.getTaxeJournaliereIdTypeChambre());
                    listTypeChambre.add(donnee);
                }
            }

            if (listIdTypeChambre.size() > 0) {

                CalculDonneesHomeSearch homeSearch = new CalculDonneesHomeSearch();
                homeSearch.setInIdTypeChambre(listIdTypeChambre);
                homeSearch.setForIdDroit(droit.getSimpleDroit().getIdDroit());
                // on filtre les periodes de prix echues
                // homeSearch.setForDateDebut(JadeDateUtil.convertDateMonthYear(debutPlage));
                homeSearch.setForDateFin(JadeDateUtil.convertDateMonthYear(debutPlage));

                homeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                homeSearch = PegasusImplServiceLocator.getCalculDonneesHomeService().search(homeSearch);

                // Coherence
                ArrayList<String> homeError = dealHomeCoherenceError(homeSearch, listTypeChambre);

                if (homeError.size() != 0) {
                    throw new HomeCalculBusinessException(homeError);
                }

                cacheDonnees.put(ConstantesCalcul.CONTAINER_DONNEES_HOMES, homeSearch);
            }

            // Si rtype de chambre dans la liste et pas de dop
            // 1 si taille serach model != homeSearch --> exception

            for (JadeAbstractModel absDonnee : searchDonneesCalculModel.getSearchResults()) {
                CalculDonneesCC donnee = (CalculDonneesCC) absDonnee;
                if (IPCTaxeJournaliere.CS_TYPE_DONNEE_FINANCIERE.equals(donnee.getCsTypeDonneeFinanciere())) {

                    listTypeChambre.add(donnee);
                }
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("Service not available - " + e.getMessage());
        }
    }
}
