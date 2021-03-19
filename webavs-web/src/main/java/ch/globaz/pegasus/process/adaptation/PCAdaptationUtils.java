package ch.globaz.pegasus.process.adaptation;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.AssuranceMaladieException;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.droit.*;
import ch.globaz.pegasus.primeassurancemaladie.PrimeAssuranceMaladieFromCSV;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;
import java.util.Map.Entry;

import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenu;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenuSearch;
import ch.globaz.pegasus.business.models.process.adaptation.DonneeFinanciereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.process.adaptation.DonneeFinancierePartiel;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.ICalculComparatif;
import ch.globaz.pegasus.businessimpl.utils.calcul.IPeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.VariableMetier;
import ch.globaz.pegasus.businessimpl.utils.dessaisissement.DessaisissementUtils;
import ch.globaz.pegasus.businessimpl.utils.dessaisissement.DessaisissementUtils.Fortune;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCAdaptationUtils {

    private static String calculDessaisissementTotal(TupleDonneeRapport tuple,
                                                     DonneesHorsDroitsProvider containerGlobal, Date dateDebut) {

        // construit une liste des données déssaisies à amortir
        List<Fortune> fortunes = new ArrayList<Fortune>();
        // Récupération du tuple de la fortune
        TupleDonneeRapport tupleFortunes = tuple.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_FORTUNES_DESSAISIES);
        // récupère la date la plus ancienne
        Date dateDebutCalcul = null;
        // Somme du dessaisissement
        float sommeInitiale = 0f;
        float somme = 0f;

        // Si tuple fortune
        if (tupleFortunes != null) {
            // iteration sur les dessaisissements
            for (TupleDonneeRapport fortune : tupleFortunes.getEnfants().values()) {

                Date dateFortune = JadeDateUtil.getGlobazDate("01."
                        + fortune.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_DATE));
                float montant = fortune.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_MONTANT);
                fortunes.add(new Fortune(dateFortune, montant));
                if ((dateDebutCalcul == null) || dateFortune.before(dateDebutCalcul)) {
                    dateDebutCalcul = dateFortune;
                }
                sommeInitiale += montant;
            }
            TreeMap<Long, String> amortissements = null;

            // recherche des variables métiers CS_AMORTISSEMENT_FORTUNE
            for (VariableMetier var : containerGlobal.getListeVariablesMetiers()) {
                if (var.getCsTypeVariableMetier().equals(
                        IPCVariableMetier.CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE)) {
                    amortissements = var.getVariablesMetiers();
                }
            }

            if ((amortissements == null) || (amortissements.size() == 0)) {
                JadeThread.logError(PCAdaptationUtils.class.toString(), "pegasus.process.adaptaion.listeAmortissement");
            }
            somme = DessaisissementUtils.calculeAmortissement(dateDebut, dateDebutCalcul, fortunes, amortissements);

        }

        return Float.toString(somme);
    }

    private static void checkEtatDemande(Droit currentDroit) throws JadeNoBusinessLogSessionError {
        // La demande ne peut se trouvé dans l'état refusé ou supprimé
        if (IPCDemandes.CS_REFUSE.equals(currentDroit.getDemande().getSimpleDemande().getCsEtatDemande())) {
            JadeThread.logError("", "pegasus.process.adaptaion.etatDemandeRefus");
        } else if (IPCDemandes.CS_SUPPRIME.equals(currentDroit.getDemande().getSimpleDemande().getCsEtatDemande())) {
            JadeThread.logError("", "pegasus.process.adaptaion.etatDemandeSupprime");
        } else if (!IPCDemandes.CS_OCTROYE.equals(currentDroit.getDemande().getSimpleDemande().getCsEtatDemande())) {
            String[] params = new String[1];
            params[0] = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                    currentDroit.getDemande().getSimpleDemande().getCsEtatDemande());
            JadeThread.logError("", "pegasus.process.adaptaion.etatDemandeChange", params);
        }

    }

    public static String createHtmlForButtonList(JadeProcessStep step) {
        String action = "";

        action = " <a data-g-download='docType:xls,parametres:"
                + step.getIdExecutionProcess()
                + ","
                + "serviceClassName:ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService,"
                + "serviceMethodName:createListeControleProcessAdaptation,docName:listeDeControle,asynchroneMode:true, defaultMail:"
                + BSessionUtil.getSessionFromThreadContext().getUserEMail()
                + "'>"
                + BSessionUtil.getSessionFromThreadContext()
                .getLabel("JSP_PROCESS_TRAITEMENT_ANNUEL_LISTE_DE_CONTROLE") + "</a>";

        return action;
    }

    /**
     * Construit une Collection des membres de familles retenu dans le calcul courant.
     *
     * @param search
     *            PCAIdMembreFamilleRetenuSearch
     * @return
     */
    public static void createListMembreFamille(PCAIdMembreFamilleRetenuSearch search, Collection<String> collectionMb,
                                               List<String> listIdDroitMembreFamille) {

        for (JadeAbstractModel model : search.getSearchResults()) {
            PCAIdMembreFamilleRetenu pca = (PCAIdMembreFamilleRetenu) model;
            collectionMb.add(pca.getSimpleDroitMembreFamille().getIdMembreFamilleSF());
            listIdDroitMembreFamille.add(pca.getSimpleDroitMembreFamille().getIdDroitMembreFamille());
        }
    }

    /**
     * Retourne les personnes comprise dans le cas retenu courant (sans date de fin) d'une version avec le montant de la
     * pc mensuelle et l'id de celle-ci.
     *
     * @param idVersionDroit
     *            l'id de la version pour laquelle le ou les calculk retenu doivent etre retrouves.
     * @return
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static PCAIdMembreFamilleRetenuSearch findIdMembreFamilleWithPlanPlanCalculRetenu(String idVersionDroit)
            throws PCAccordeeException, JadePersistenceException, JadeApplicationServiceNotAvailableException {

        PCAIdMembreFamilleRetenuSearch search = new PCAIdMembreFamilleRetenuSearch();
        search.setForIdVersionDroit(idVersionDroit);
        search.setIsPlanRetenu(true);
        PegasusImplServiceLocator.getPCAccordeeService().searchPCAIdMembreFamilleRetenuSearch(search);
        return search;
    }

    public static DroitSearch findTheCurrentDroit(String idDemande) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdDemandePc(idDemande);

        // On doit retourner max 2 droit le validé et celui qui est en fase de correction.
        // Les autres droit doivent se trouver dans l'état historisé.
        droitSearch.setForCsEtatDroitIn((Arrays.asList(IPCDroits.CS_VALIDE, IPCDroits.CS_AU_CALCUL,
                IPCDroits.CS_CALCULE, IPCDroits.CS_ENREGISTRE)));
        droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        return droitSearch;
    }

    public static DroitSearch findDroitValideFromIdDroit(String idDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdDroit(idDroit);

        // On doit retourner max 2 droit le validé et celui qui est en fase de correction.
        // Les autres droit doivent se trouver dans l'état historisé.
        droitSearch.setForCsEtatDroitIn((Arrays.asList(IPCDroits.CS_VALIDE)));
        droitSearch = PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        return droitSearch;
    }

    /**
     * Permet de retrouver le droit courant(validé) et le futur droit( a calculer) Retourne une liste avec 2 éléments le
     * premier c'est le droit courant et les deuxième c'est le droit à calculer. Si un droit n'est pas validé es que ce
     * n'est pas un droit du à l'adaptation ceci vas supprimer ce droit !!!
     *
     * @param dataToSave
     *
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws DossierException
     * @throws DemandeException
     */
    public static Droit[] findUpdatableDroits(String idDemande, String date, String csMotif,
                                              Map<PCProcessAdapationEnum, String> dataToSave) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException, DossierException {
        DroitSearch droitSearch = null;
        Droit currentDroit = null;
        Droit droitACalculer = null;
        Droit[] list = new Droit[2];
        boolean hasDroitValide = false;
        droitSearch = PCAdaptationUtils.findTheCurrentDroit(idDemande);

        if (droitSearch.getSearchResults().length == 0) {
            JadeThread.logError(PCAdaptationUtils.class.toString(), "pegasus.process.adaptaion.auncunDroitTrouve");
        } else if (droitSearch.getSearchResults().length == 1) {
            currentDroit = (Droit) droitSearch.getSearchResults()[0];
            if (!IPCDroits.CS_VALIDE.equals(currentDroit.getSimpleVersionDroit().getCsEtatDroit())) {
                JadeThread.logError(PCAdaptationUtils.class.toString(), "pegasus.process.adaptaion.etatDroitNonValide");
            }
            // On recherche bien la pca en cour de la version du droit validé
        } else if (droitSearch.getSearchResults().length == 2) {
            for (JadeAbstractModel model : droitSearch.getSearchResults()) {
                Droit droit = (Droit) model;

                if (IPCDroits.CS_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
                    if (hasDroitValide) {
                        JadeThread.logError(PCAdaptationUtils.class.toString(),
                                "pegasus.process.adaptaion.deuxVersionValide");
                    }
                    currentDroit = droit;
                    hasDroitValide = true;
                } else if (IPCDroits.CS_CALCULE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                        || IPCDroits.CS_AU_CALCUL.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                        || IPCDroits.CS_ENREGISTRE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
                    try {
                        if (!IPCDroits.CS_MOTIF_DROIT_ADAPTATION.equals(droit.getSimpleVersionDroit().getCsMotif())) {
                            PegasusServiceLocator.getDroitService().supprimerVersionDroit(droit);
                            dataToSave.put(PCProcessAdapationEnum.HAS_DELETE_VERSION_DROIT, "true");

                        } else {
                            droitACalculer = droit;
                            // met le droit en état Au Calcul et vide tous les résultats existant
                            if (IPCDroits.CS_CALCULE.equals(droitACalculer.getSimpleVersionDroit().getCsEtatDroit())) {
                                try {
                                    // Attention la fonction reinitaliserDroit fait un commit !!!!!
                                    PegasusImplServiceLocator.getCalculDroitService().reinitialiseDroit(droitACalculer);
                                } catch (Exception e) {
                                    JadeProcessCommonUtils.addError(e);
                                }
                            }
                        }
                    } catch (DonneeFinanciereException e) {
                        JadeProcessCommonUtils.addError(e);
                    }

                } else {
                    JadeThread.logError(PCAdaptationUtils.class.toString(),
                            "pegasus.process.adaptaion.etatDroitIncoherant");
                }
            }

        } else {
            JadeThread.logError(PCAdaptationUtils.class.toString(), "pegasus.process.adaptaion.droitNombre");
        }

        PCAdaptationUtils.checkEtatDemande(currentDroit);

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            if (droitACalculer == null) {
                droitACalculer = PegasusServiceLocator.getDroitService().corrigerCreateVersionDroit(currentDroit, date,
                        csMotif);
            }
        }
        list[0] = currentDroit;
        list[1] = droitACalculer;
        return list;
    }

    /**
     * Permet de retrouver le droit courant(validé) et le futur droit( a calculer) Retourne une liste avec 2 éléments le
     * premier c'est le droit courant et les deuxième c'est le droit à calculer. Si un droit est calculé, il ne sera pas supprimé et non modifiable
     * Il ressortira en erreur dans le processus d'adaptation!!!
     *
     * @param idDemande            ?
     * @param date
     * @param csMotif
     * @param montantNouvellePrime
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws DossierException
     * @throws DemandeException
     * @return
     */
    public static Droit[] findUpdatableDroitsForPrimeLAMal(String idDemande, String date, String csMotif,
                                                           Map<String, PrimeAssuranceMaladieFromCSV> listePrimeFromCsv) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException, DossierException {
        DroitSearch droitSearch = null;
        Droit currentDroit = null;
        Droit droitACalculer = null;
        Droit[] list = new Droit[2];
        boolean hasDroitValide = false;
        droitSearch = PCAdaptationUtils.findTheCurrentDroit(idDemande);

        if (droitSearch.getSearchResults().length == 0) {
            JadeThread.logError(PCAdaptationUtils.class.toString(), "pegasus.process.adaptaion.auncunDroitTrouve");
        } else if (droitSearch.getSearchResults().length == 1) {
            currentDroit = (Droit) droitSearch.getSearchResults()[0];
            if (!IPCDroits.CS_VALIDE.equals(currentDroit.getSimpleVersionDroit().getCsEtatDroit())) {
                JadeThread.logError(PCAdaptationUtils.class.toString(), "pegasus.process.adaptaion.etatDroitNonValide");
            }
            // On recherche bien la pca en cour de la version du droit validé
        } else if (droitSearch.getSearchResults().length == 2) {
            for (JadeAbstractModel model : droitSearch.getSearchResults()) {
                Droit droit = (Droit) model;

                if (IPCDroits.CS_VALIDE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
                    if (hasDroitValide) {
                        JadeThread.logError(PCAdaptationUtils.class.toString(),
                                "pegasus.process.adaptaion.deuxVersionValide");
                    }
                    currentDroit = droit;
                    hasDroitValide = true;
                } else if (IPCDroits.CS_CALCULE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                        || IPCDroits.CS_AU_CALCUL.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
                        || IPCDroits.CS_ENREGISTRE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {

                    // Pour le processus Prime Lamal, les droits en cours ne doivent pas être supprimés, mais une erreur doit être remontées
                    if (isExistingPrimeAssuranceMaladieWithSameMontant(droit, listePrimeFromCsv)) {
                        JadeThread.logError(PCAdaptationUtils.class.toString(),
                                "pegasus.process.adaptation.droitEnCoursAvecPrimeEgalLamal");
                    } else {
                        JadeThread.logError(PCAdaptationUtils.class.toString(),
                                "pegasus.process.adaptation.droitEnCoursSansPrimeEgalLamal");
                    }
                } else {
                    JadeThread.logError(PCAdaptationUtils.class.toString(),
                            "pegasus.process.adaptaion.etatDroitIncoherant");
                }
            }

        } else {
            JadeThread.logError(PCAdaptationUtils.class.toString(), "pegasus.process.adaptaion.droitNombre");
        }

        PCAdaptationUtils.checkEtatDemande(currentDroit);

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            if (droitACalculer == null) {
                droitACalculer = PegasusServiceLocator.getDroitService().corrigerCreateVersionDroit(currentDroit, date,
                        csMotif);
            }
        }
        list[0] = currentDroit;
        list[1] = droitACalculer;
        return list;
    }

    /**
     * Recherche si une prime existe avec le meme montant
     *
     * @param droit             ?
     * @param listePrimeFromCsv
     * @return
     */
    private static boolean isExistingPrimeAssuranceMaladieWithSameMontant(Droit droit, Map<String, PrimeAssuranceMaladieFromCSV> listePrimeFromCsv) {
        PrimeAssuranceMaladieSearch searchAssuranceMaladie = null;
        try {
            DroitMembreFamille requerant = findDroitFamilleFromIdDroit(droit.getId());
            if (requerant != null) {
                searchAssuranceMaladie = searchPrimeAssuranceMaladie(requerant.getSimpleDroitMembreFamille().getId() ,droit.getSimpleVersionDroit().getNoVersion());

                String montantPrimeAssurance = null;
                if (listePrimeFromCsv.containsKey(requerant.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel())) {
                    montantPrimeAssurance = listePrimeFromCsv.get(requerant.getMembreFamille().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()).getMontant();
                }

                if (searchAssuranceMaladie.getSearchResults().length > 0 && montantPrimeAssurance != null && !montantPrimeAssurance.isEmpty()) {
                    for(Object object : searchAssuranceMaladie.getSearchResults()) {
                        PrimeAssuranceMaladie primeAssuranceMaladieExistante = (PrimeAssuranceMaladie) object;
                        // Si l'on trouve une prime assurance avec le même montant
                        // Alors pas d'ecriture de prime
                        if (primeAssuranceMaladieExistante.getSimpleDonneeFinanciereHeader().getDateFin().isEmpty()
                                && new Float(primeAssuranceMaladieExistante.getSimplePrimeAssuranceMaladie().getMontant()).equals(new Float(montantPrimeAssurance))){
                            return true;
                        }
                    }
                }
            }
        } catch (AssuranceMaladieException | JadeApplicationServiceNotAvailableException | JadePersistenceException | DroitException e) {
            JadeThread.logError(PCAdaptationUtils.class.toString(),"Erreur à la récupération du droit en cours.");
        }
        return false;
    }

    /**
     * Recherche droit famille avec l'idDroit
     *
     * @param idDroit
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws JadePersistenceException
     */
    private static DroitMembreFamille findDroitFamilleFromIdDroit(String idDroit) throws JadeApplicationServiceNotAvailableException, DroitException, JadePersistenceException {
        DroitMembreFamille requerant = null;
        MembreFamilleEtenduSearch membreSearch = new MembreFamilleEtenduSearch();
        membreSearch.setForIdDroit(idDroit);
        membreSearch.setOrderKey("orderByRole");
        membreSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(membreSearch);
        for (Iterator it = Arrays.asList(membreSearch.getSearchResults()).iterator(); it.hasNext(); ) {
            MembreFamilleEtendu membre = (MembreFamilleEtendu) it.next();
            List<MembreFamilleEtendu> listeMembres = new ArrayList<>();
            listeMembres.add(membre);

            if (Objects.isNull(requerant) && membre.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC()
                    .equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
                requerant = membre.getDroitMembreFamille();
            }
        }

        return requerant;
    }

    /**
     * Recherche les primes d'assurance maladie d'un requerant
     *
     * @param idDroitMembreFamille
     * @param versionDroit
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AssuranceMaladieException
     * @throws DroitException
     */
    private static PrimeAssuranceMaladieSearch searchPrimeAssuranceMaladie(String idDroitMembreFamille, String versionDroit) throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AssuranceMaladieException, DroitException {
        PrimeAssuranceMaladieSearch searchAssuranceMaladie = new PrimeAssuranceMaladieSearch();
        searchAssuranceMaladie.setIdDroitMembreFamille(idDroitMembreFamille);
        if (Objects.nonNull(versionDroit)){
            searchAssuranceMaladie.setForNumeroVersion(versionDroit);
            searchAssuranceMaladie.setWhereKey("forVersionedPrimeAssuranceMaladie");
        }
        searchAssuranceMaladie = PegasusServiceLocator.getAssuranceMaladieService().searchPrimeAssuranceMaladie(searchAssuranceMaladie);

        return searchAssuranceMaladie;
    }

    /**
     * Enregistre les nouveaux et anciens montant de la pc mensuelle
     *
     * @param membreFamilleWithPlanPlanCalculRetenu
     * @param periodePCAccordee
     * @param collectionIdMembreFamille
     *            Correspond au personne comprise dans le calcule.
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DroitException
     * @throws AdaptationException
     * @throws RenteAdapationDemandeException
     */
    public static Map<PCProcessAdapationEnum, String> saveData(
            PCAIdMembreFamilleRetenuSearch membreFamilleWithPlanPlanCalculRetenu, IPeriodePCAccordee periodePCAccordee,
            Droit currentDroit, String dateAdaptation, Collection<String> collectionIdMembreFamille,
            DonneesHorsDroitsProvider containerGlobal) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, RenteAdapationDemandeException,
            AdaptationException {
        PCAIdMembreFamilleRetenu pcaOldRequerant = null;
        PCAIdMembreFamilleRetenu pcaOldConjoint = null;
        ICalculComparatif requerant = null;
        ICalculComparatif conjoint = null;
        Map<PCProcessAdapationEnum, String> map = new HashMap<PCProcessAdapationEnum, String>();

        if (membreFamilleWithPlanPlanCalculRetenu.getSearchResults().length > 0) {
            List<PCAIdMembreFamilleRetenu> list = PersistenceUtil.typeSearch(membreFamilleWithPlanPlanCalculRetenu,
                    membreFamilleWithPlanPlanCalculRetenu.whichModelClass());

            Map<String, List<PCAIdMembreFamilleRetenu>> mapPCA = JadeListUtil.groupBy(list,
                    new Key<PCAIdMembreFamilleRetenu>() {
                        @Override
                        public String exec(PCAIdMembreFamilleRetenu e) {
                            return e.getIdTiersBeneficiaire();
                        }
                    });

            if (mapPCA.size() > 0) {
                String idTiersRequerant = currentDroit.getDemande().getDossier().getDemandePrestation()
                        .getPersonneEtendue().getTiers().getIdTiers();

                // pour le conjoint si couple separé par la maladie
                for (Entry<String, List<PCAIdMembreFamilleRetenu>> entry : mapPCA.entrySet()) {
                    if (idTiersRequerant.equals(entry.getKey())) {
                        pcaOldRequerant = entry.getValue().get(0);
                    } else {
                        pcaOldConjoint = entry.getValue().get(0);
                    }
                }

                if (pcaOldRequerant != null) {
                    // les donnees de l'ancienne PC
                    map.put(PCProcessAdapationEnum.OLD_PC, pcaOldRequerant.getMontantPCMensuelle());
                    map.put(PCProcessAdapationEnum.OLD_ETAT_PC, pcaOldRequerant.getEtatPC());
                } else {
                    map.put(PCProcessAdapationEnum.OLD_PC, "0");
                    throw new AdaptationException("Aucune ancienne PCA trouvée pour le requérant");
                }

                requerant = periodePCAccordee.getCalculComparatifRetenus()[0];

                // les donnes de la nouvelle PC
                map.put(PCProcessAdapationEnum.NEW_PC, requerant.getMontantPCMensuel());
                map.put(PCProcessAdapationEnum.NEW_NB_ENFANT, requerant.getNbEnfants());
                map.put(PCProcessAdapationEnum.NEW_DONATION, requerant.getMontantDonation());
                map.put(PCProcessAdapationEnum.NEW_PRIX_HOME, requerant.getMontantPrixHome());
                map.put(PCProcessAdapationEnum.NEW_RENTE_AVS_AI, requerant.getMontantRentesAvsAi());
                map.put(PCProcessAdapationEnum.ID_PLAN_CALCUL_RETENU, requerant.getIdPlanCalcul());

                // les ancienne données financière
                DonneeFinanciereSearch search = new DonneeFinanciereSearch();
                search.setForIdMembreFamilleSFIn(collectionIdMembreFamille);
                search.setForIdDroit(currentDroit.getSimpleDroit().getIdDroit());
                search.setForDateValable(JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
                        + dateAdaptation, -1)));
                // On fait plus car on traite le droit validé et non le droit à calculé
                search.setForNoVersion(String.valueOf(Integer.valueOf(currentDroit.getSimpleVersionDroit()
                        .getNoVersion()) + 1));

                DonneeFinancierePartiel dfp = PegasusServiceLocator.getAdaptationService()
                        .findDonneeFinanciereAncienne(search);
                // Conversion date
                Date dateDebut = JadeDateUtil.getGlobazDate(JadeDateUtil.addMonths("01." + dateAdaptation, -1));

                map.put(PCProcessAdapationEnum.OLD_NB_ENFANT, dfp.getNbEnfant());
                map.put(PCProcessAdapationEnum.OLD_RENTE_AVS_AI, dfp.getMontantRentes());

                String montantOldDonation = PCAdaptationUtils.calculDessaisissementTotal(requerant.getMontants(),
                        containerGlobal, dateDebut);

                map.put(PCProcessAdapationEnum.OLD_DONATION, montantOldDonation);
                map.put(PCProcessAdapationEnum.OLD_PRIX_HOME, dfp.getPrixHome());

                map.put(PCProcessAdapationEnum.NEW_ETAT_PC, requerant.getEtatPC());

                // pour le conjoint si couple separé par la maladie
                if ((pcaOldConjoint != null)
                        || ((periodePCAccordee.getCalculComparatifRetenus().length == 2) && (periodePCAccordee
                        .getCalculComparatifRetenus()[1] != null))) {

                    // la description du conjoint
                    DroitMembreFamilleSearch dmfSearch = new DroitMembreFamilleSearch();
                    dmfSearch.setForIdDroit(currentDroit.getSimpleDroit().getIdDroit());
                    List<String> csRole = new ArrayList<String>();
                    csRole.add(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
                    dmfSearch.setForCsRoletMembreFamilleIn(csRole);
                    dmfSearch = PegasusServiceLocator.getDroitService().searchDroitMembreFamille(dmfSearch);

                    map.put(PCProcessAdapationEnum.OLD_PRIX_HOME_CONJOINT, dfp.getPrixHomeConjoint());
                    map.put(PCProcessAdapationEnum.OLD_RENTE_AVS_AI_CONJOINT, dfp.getMontantRentes());
                    map.put(PCProcessAdapationEnum.OLD_DONATION_CONJOINT, montantOldDonation);

                    if (dmfSearch.getSize() == 1) {
                        PersonneEtendueComplexModel p = ((DroitMembreFamille) dmfSearch.getSearchResults()[0])
                                .getMembreFamille().getPersonneEtendue();
                        String description = p.getPersonneEtendue().getNumAvsActuel() + " "
                                + p.getTiers().getDesignation1() + " " + p.getTiers().getDesignation2() + "CONJOINT";
                        map.put(PCProcessAdapationEnum.DESCRIPTION_CONJOINT, description);
                    } else {
                        map.put(PCProcessAdapationEnum.DESCRIPTION_CONJOINT, "Nombre de conjoint incohérant!");
                    }

                    if (pcaOldConjoint != null) {
                        map.put(PCProcessAdapationEnum.OLD_PC_CONJOINT, pcaOldConjoint.getMontantPCMensuelle());
                        map.put(PCProcessAdapationEnum.OLD_ETAT_PC_CONJOINT, pcaOldConjoint.getEtatPC());
                    } else {
                        map.put(PCProcessAdapationEnum.OLD_PC_CONJOINT, "0");
                    }

                    conjoint = periodePCAccordee.getCalculComparatifRetenus()[1];

                    if (conjoint != null) {

                        map.put(PCProcessAdapationEnum.ID_PLAN_CALCUL_RETENU_CONJOINT, conjoint.getIdPlanCalcul());

                        // les donnes de la nouvelle pc du conjoint
                        map.put(PCProcessAdapationEnum.NEW_PC_CONJOINT, conjoint.getMontantPCMensuel());
                        map.put(PCProcessAdapationEnum.NEW_DONATION_CONJOINT, conjoint.getMontantDonation());
                        map.put(PCProcessAdapationEnum.NEW_PRIX_HOME_CONJOINT, conjoint.getMontantPrixHome());
                        map.put(PCProcessAdapationEnum.NEW_RENTE_AVS_AI_CONJOINT, conjoint.getMontantRentesAvsAi());
                        map.put(PCProcessAdapationEnum.NEW_ETAT_PC_CONJOINT, conjoint.getEtatPC());
                    }
                }
            }
        }
        return map;
    }

}