package ch.globaz.pegasus.businessimpl.services.models.calcul;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.calcul.*;
import ch.globaz.pegasus.business.models.creancier.*;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.calcul.CalculDroitService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.calcul.donneeInterne.DonneeInterneHomeVersement;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPrecedante;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.*;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.utils.periode.GroupePeriodesResolver;
import ch.globaz.utils.periode.GroupePeriodesResolver.EachPeriode;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;

import static globaz.externe.IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;

/**
 * @author ECO
 */
public class CalculDroitServiceImpl extends PegasusAbstractServiceImpl implements CalculDroitService {
    private final String REQUERANT_HOME = "REQUERANT_HOME";
    private final String CONJOINT_HOME = "CONJOINT_HOME";
    private final String REQUERANT_DEP_PERS = "REQUERANT_DEP_PERS";
    private final String CONJOINT_DEP_PERS = "CONJOINT_DEP_PERS";
    private final String REQUERANT_MNT_SEJOUR = "REQUERANT_SEJ";
    private final String CONJOINT_MNT_SEJOUR = "CONJOINT_SEJ";

    @Override
    public Droit calculDroit(Droit droit) throws JadePersistenceException, JadeApplicationException, CalculException,
            SecurityException, NoSuchMethodException {
        return this.calculDroit(droit, true, null);
    }

    @Override
    public Droit calculDroit(Droit droit, boolean retroactif, List<String> dfForVersion) throws CalculException,
            JadePersistenceException, JadeApplicationException, SecurityException, NoSuchMethodException {

        try {
            if ((droit == null) || droit.isNew()) {
                throw new CalculBusinessException("Unable to calcul droit, the model is null or new!");
            }

            String[] datesPlageCalcul = calculDroitPlageCalcul(droit, retroactif);

            String dateDebutPlageCalcul = datesPlageCalcul[0];
            String dateFinPlageCalcul = datesPlageCalcul[1]; // date de fin par défaut utilisée dans les calculs dans le

            // Date de fin force
            boolean isDateFinForce = false;

            // cas périodes sans fin
            // Classe de gestion des variables métiers
            // VariableMetierProvider varMetProvider = new VariableMetierProvider();
            DonneesHorsDroitsProvider containerGlobal = DonneesHorsDroitsProvider.getInstance();
            containerGlobal.init();
            // si la date de la plage est nulle, c'est qu'il n'y a pas de rente
            // et pas de calcul à faire
            if (dateDebutPlageCalcul != null) {

                // récupère données de persistence et le garde en cache mémoire
                Map<String, JadeAbstractSearchModel> cacheDonneesBD = PegasusImplServiceLocator.getPeriodesService()
                        .getDonneesCalculDroit(droit, dateDebutPlageCalcul, dateFinPlageCalcul);

                List<PeriodePCAccordee> listePCAccordes = calculeDroitPartagePeriodes(droit, dateDebutPlageCalcul,
                        dateFinPlageCalcul, cacheDonneesBD, containerGlobal, isDateFinForce);

                List<PeriodePCAccordee> listePCAccordesReforme = calculeDroitPartagePeriodes(droit, dateDebutPlageCalcul,
                        dateFinPlageCalcul, cacheDonneesBD, containerGlobal, isDateFinForce);
                // procède au calcul à proprement dit
                calculeDroitTraitement(droit, cacheDonneesBD, listePCAccordes, listePCAccordesReforme, dateDebutPlageCalcul,
                        dateFinPlageCalcul, containerGlobal);

                // récupère anciennes pc accordées
                PegasusImplServiceLocator.getCalculPersistanceService().recupereAnciensPCAccordee(dateDebutPlageCalcul,
                        droit, cacheDonneesBD);
                calculFinalise(droit, retroactif, dateDebutPlageCalcul, containerGlobal, cacheDonneesBD, listePCAccordes);

                // Gestion de date de debut des périodes pour le cas du calcul mois suivant, et dans le sversions > 1
                int noVersion = Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion());

                // la liste des données modifiées ne doit pas être vide
                if (!retroactif && (noVersion > 1) && (dfForVersion.size() > 0)) {
                    PegasusServiceLocator.getCalculMoisSuivantService().updateDonneeFinancieres(dfForVersion,
                            droit.getSimpleVersionDroit().getNoVersion());
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("Service not available - " + e.getMessage());
        }

        return droit;

    }

    private void calculFinalise(Droit droit, boolean retroactif, String dateDebutPlageCalcul, DonneesHorsDroitsProvider containerGlobal, Map<String, JadeAbstractSearchModel> cacheDonneesBD, List<PeriodePCAccordee> listePCAccordes) throws JadePersistenceException, JadeApplicationException {
        // calcul des éventuels jours d'appoint
        PegasusImplServiceLocator.getCalculComparatifService().calculJoursAppoint(
                listePCAccordes,
                (CalculPcaReplaceSearch) cacheDonneesBD
                        .get(ConstantesCalcul.CONTAINER_DONNEES_PCACCORDEES_REPLACED));

        calculeDroitPersistence(droit, retroactif, listePCAccordes, cacheDonneesBD);

        if (retroactif) {
            CalculAllocationNoel calculAllocationNoel = new CalculAllocationNoel();

            calculAllocationNoel.calculAndSaveAllocationDeNoel(listePCAccordes, droit, containerGlobal,
                    dateDebutPlageCalcul);
        }
    }

    @Override
    public String[] calculDroitPlageCalcul(Droit droit, boolean retroactif) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, CalculException, JadePersistenceException, DemandeException {
        String dateProchainPaiement = "01." + PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        String datePlageCalcul;
        String dateFinPlageCalcul; // date de fin par défaut utilisée dans les calculs dans le cas périodes sans fin

        if (retroactif) {
            // cherche la plage de dates du droit à calculer
            String[] datesPlageCalcul = PegasusImplServiceLocator.getPeriodesService().recherchePlageCalcul(droit);
            datePlageCalcul = datesPlageCalcul[0];

            // Si la date de fin est vide, et que la date de fin de la demande est également -->prochain paiement
            if (JadeStringUtil.isEmpty(datesPlageCalcul[1])
                    && JadeStringUtil.isBlank(droit.getDemande().getSimpleDemande().getDateFin())) {
                dateFinPlageCalcul = JadeDateUtil.addDays(dateProchainPaiement, -1);

                if (JadeDateUtil.isDateAfter(datePlageCalcul, dateFinPlageCalcul)) {
                    // Si la date de début de la plage de calcul est plus grande que la date de fin, on lève une erreur
                    // demandant de faire un calcul a effet mois suivant
                    throw new CalculBusinessException("pegasus.calcul.periodes.plagedate.superposition",
                            datePlageCalcul, dateProchainPaiement);
                }
            }
            // Si la date de fin de la demande n'est pas vide, on plaffone la plage de calcul avec
            else if (!JadeStringUtil.isBlank(droit.getDemande().getSimpleDemande().getDateFin())) {
                dateFinPlageCalcul = "01." + droit.getDemande().getSimpleDemande().getDateFin();
            } else {
                dateFinPlageCalcul = datesPlageCalcul[1];
            }
        } else {
            // définit la date de prochain paiement comme limite de la plage de dates
            datePlageCalcul = dateProchainPaiement;
            // on met la durée de la plage de calcul à 1 mois.
            dateFinPlageCalcul = JadeDateUtil.addMonths(dateProchainPaiement, 1);
        }

        if ((dateFinPlageCalcul != null) && JadeDateUtil.isDateBefore(dateFinPlageCalcul, datePlageCalcul)) {
            throw new CalculBusinessException("pegasus.calcul.periodes.plagedate.integrity", dateFinPlageCalcul,
                    datePlageCalcul);
        }

        return new String[]{datePlageCalcul, dateFinPlageCalcul};

    }

    @Override
    public IPeriodePCAccordee calculDroitPourComparaison(Droit droit, Collection<String> listeIdPersonnes,
                                                         DonneesHorsDroitsProvider containerGlobal, boolean persistCalcul, boolean retroactif)
            throws CalculException, JadePersistenceException, JadeApplicationException {

        List<PeriodePCAccordee> list = this.calculDroitPourComparaison(droit, listeIdPersonnes, containerGlobal,
                persistCalcul, retroactif, null);
        return list.get(list.size() - 1);
    }

    private List<PeriodePCAccordee> calculDroitPourComparaison(Droit droit, Collection<String> listeIdPersonnes,
                                                               DonneesHorsDroitsProvider containerGlobal, boolean persistCalcul, boolean retroactif,
                                                               String dateDebutPeriodeForce) throws CalculException, JadePersistenceException, JadeApplicationException {

        List<PeriodePCAccordee> listePCAccordes = null;

        List<PeriodePCAccordee> listePCAccordesReforme = null;
        try {
            if ((droit == null) || droit.isNew()) {
                throw new CalculException("Unable to calcul droit, the model is null or new!");
            }

            if ((listeIdPersonnes == null) || (listeIdPersonnes.size() == 0)) {
                throw new CalculException("Unable to calcul droit, the listeIdPersonnes is null or empty!");
            }
            String dateDebutPlageCalcul = null;
            String dateFinPlageCalcul = null;

            if (JadeStringUtil.isEmpty(dateDebutPeriodeForce)) {
                String[] datesPlageCalcul = calculDroitPlageCalcul(droit, retroactif);
                dateDebutPlageCalcul = datesPlageCalcul[0];
                dateFinPlageCalcul = datesPlageCalcul[1]; // date de fin par défaut utilisée dans les calculs dans le
                // cas périodes sans fin
            } else {
                dateDebutPlageCalcul = "01." + dateDebutPeriodeForce;

                Calendar cal = JadeDateUtil.getGlobazCalendar(dateDebutPlageCalcul);
                int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                dateFinPlageCalcul = String.valueOf(lastDay) + "." + dateDebutPeriodeForce;
            }

            // si la date de la plage est nulle, c'est qu'il n'y a pas de rente
            // et pas de calcul à faire
            if (dateDebutPlageCalcul != null) {

                if (containerGlobal == null) {
                    throw new CalculException("donneesHorsDroitProvider is null!");
                }
                // récupère données de persistence et le garde en cache mémoire
                Map<String, JadeAbstractSearchModel> cacheDonneesBD = PegasusImplServiceLocator.getPeriodesService()
                        .getDonneesCalculDroit(droit, dateDebutPlageCalcul, dateFinPlageCalcul);

                listePCAccordes = calculeDroitPartagePeriodes(droit, dateDebutPlageCalcul, dateFinPlageCalcul,
                        cacheDonneesBD, containerGlobal, false);

                listePCAccordesReforme = calculeDroitPartagePeriodes(droit, dateDebutPlageCalcul, dateFinPlageCalcul,
                        cacheDonneesBD, containerGlobal, false);

                if ((listePCAccordes.size() > 2) || (listePCAccordes.size() < 1)) {
                    throw new CalculException("Expected 1 or 2 PC Accordee!");
                }

                // procède au calcul à proprement dit
                calculeDroitTraitement(droit, cacheDonneesBD, listePCAccordes, listePCAccordesReforme, dateDebutPlageCalcul,
                        dateFinPlageCalcul, containerGlobal);

                dertermineFavorableCombinaisonPersonne(listeIdPersonnes, listePCAccordes);

                // calcul des éventuels jours d'appoint
                PegasusImplServiceLocator.getCalculComparatifService().calculJoursAppoint(
                        listePCAccordes,
                        (CalculPcaReplaceSearch) cacheDonneesBD
                                .get(ConstantesCalcul.CONTAINER_DONNEES_PCACCORDEES_REPLACED));

                // récupère anciennes pc accordées
                PegasusImplServiceLocator.getCalculPersistanceService().recupereAnciensPCAccordee(dateDebutPlageCalcul,
                        droit, cacheDonneesBD);

                // this.calculeDroitPersistence(droit, retroactif, listePCAccordes, cacheDonneesBD);
                if (persistCalcul) {
                    persistCalculForComparaison(droit, retroactif, listePCAccordes, cacheDonneesBD);
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("Service not available - " + e.getMessage());
        }

        if (listePCAccordes == null) {
            throw new CalculException("listePCAccordes shouldn't be null!!");
        }

        return listePCAccordes;

    }

    private void dertermineFavorableCombinaisonPersonne(Collection<String> listeIdPersonnes, List<PeriodePCAccordee> listePCAccordes) throws CalculException {
        if (listePCAccordes == null || listePCAccordes.isEmpty()) {
            return;
        }
        for (PeriodePCAccordee periode : listePCAccordes) {
            // choisit si possible le CC dont la combinaison de personnes coincide avec celle donnée en
            // paramètre
            CalculComparatif[] anciensCCRetenus = periode.getCCRetenu();
            // Cas retenus par le calculateur
            CalculComparatif ccDefautByCalculator = anciensCCRetenus[0].copyCC();
            // Cas retenus par la comparaison
            CalculComparatif ccDefautByComparaison = null;

            List<CalculComparatif> calculsComparatifs = periode.getCalculsComparatifs();
            boolean isCCTrouve = false;
            // boolean isCCRetenusDouble = false;

            List<CalculComparatif> calculsComparatifsComparaison = new ArrayList<>();

            // Si il n'y a pas de calcul comparatif pour le conjoint, CAS COUPLE SEPARE PAR LA MALADIE
            if (periode.getCalculsComparatifsConjoint().size() == 0) {
                for (CalculComparatif cc : calculsComparatifs) {
                    // boolean isPlanRetenu = cc.getIsPlanretenu();
                    // on met le plan retenu a false
                    cc.setPlanRetenu(Boolean.FALSE);

                    boolean listeEquals = cc.equalsPersonnes(listeIdPersonnes);

                    // Si retenus
                    if (listeEquals) {
                        isCCTrouve = true;
                        calculsComparatifsComparaison.add(cc);
                        cc.setComparer(true);
                    }
                }

                if (calculsComparatifsComparaison.size() == 1) {
                    // 1 seul cas avant ou aprés réforme pas de comparaison sur le montant
                    CalculComparatif ccTrouve = calculsComparatifsComparaison.get(0);
                    ccDefautByComparaison = ccTrouve.copyCC();
                    ccTrouve.setPlanRetenu(true);
                } else if (calculsComparatifsComparaison.size() > 1) {
                    // comparaison avant et aprés réforme
                    periode.determineCCFavorable(calculsComparatifsComparaison);
                    ccDefautByComparaison = periode.getCCRetenu()[0].copyCC();
                }

                // Cas ou aucun plan n'est retenu, la liste des personnes ne concordent jamais
                // On va setter, par défaut le cas retenus par le calculateur
                if (!isCCTrouve) {
                    periode.setCalculComparatifByDefaut(ccDefautByCalculator);
                    JadeThread.logWarn(this.getClass().toString(),
                            "pegasus.calcul.adptation.calculcomparatifretenu.nontrouve.integrity");
                } else {
                    // Si les id plan calcul différents
                    if (!ccDefautByCalculator.getPersonnes().equals(ccDefautByComparaison.getPersonnes())) {
                        JadeThread.logWarn(this.getClass().toString(),
                                "pegasus.calcul.adptation.calculcomparatifretenu.nonfavorable.integrity");
                    }
                }
            }
        }
    }

    private void fusionneCalcul(List<PeriodePCAccordee> listePCAccordes, List<PeriodePCAccordee> listePCAccordesReforme, String dateReforme) throws CalculException {

        if (listePCAccordesReforme.isEmpty()) {
            // pas de fusion seul les calculs avant réforme compte
            return;
        } else if (listePCAccordes.isEmpty()) {
            // pas de fusion : on remplace la liste ancienne avec les données de la liste calcul réforme
            // flag les calculs en réforme
            for (PeriodePCAccordee pcAccordesReforme : listePCAccordesReforme) {
                getCalculReforme(pcAccordesReforme.getCalculsComparatifs());
                if (isRefusFortune(pcAccordesReforme)) {
                    throw new CalculException("pegasus.calcul.seuil.fortune.depasse", pcAccordesReforme.getCalculsComparatifs().get(0).getFortune());
                }
            }
            listePCAccordes.addAll(listePCAccordesReforme);
            return;
        }

        if (listePCAccordes.size() != listePCAccordesReforme.size()) {
            throw new CalculException("Les nombres de PCA avant réforme et après réforme ne sont pas identique !");
        }

        for (int i = 0; i < listePCAccordes.size(); i++) {
            PeriodePCAccordee pcAccordes = listePCAccordes.get(i);
            PeriodePCAccordee pcAccordesReforme = listePCAccordesReforme.get(i);
            Date dateDebut = pcAccordesReforme.getDateDebut();
            if (JadeDateUtil.isDateBefore(JadeDateUtil.getGlobazFormattedDate(dateDebut), dateReforme)
                    || isRefusFortune(pcAccordesReforme)) {
                // periode de cette pca avant la réforme à ne pas comparer ou refus seuil de fortune sur le calcul réforme
                pcAccordes.determineCCFavorable();
                continue;
            }

            for (CalculComparatif calculComparatifs : pcAccordes.getCCRetenu()) {
                if (calculComparatifs != null) {
                    calculComparatifs.setPlanRetenu(false);
                }
            }
            for (CalculComparatif calculComparatifsReforme : pcAccordesReforme.getCCRetenu()) {
                if (calculComparatifsReforme != null) {
                    calculComparatifsReforme.setPlanRetenu(false);
                }
            }
            pcAccordes.getCalculsComparatifs().addAll(getCalculReforme(pcAccordesReforme.getCalculsComparatifs()));
            pcAccordes.getCalculsComparatifsConjoint().addAll(getCalculReforme(pcAccordesReforme.getCalculsComparatifsConjoint()));
            fusionnePersonne(pcAccordes, pcAccordesReforme);
            pcAccordes.getPersonnes().putAll(pcAccordesReforme.getPersonnes());
            pcAccordes.determineCCFavorable();
        }
    }

    private boolean isRefusFortune(PeriodePCAccordee pcAccordesReforme) {
        return pcAccordesReforme.getCalculsComparatifs().get(0).isRefusFortune();
    }

    private void fusionnePersonne(PeriodePCAccordee pcAccordes, PeriodePCAccordee pcAccordesReforme) {
        for (CalculComparatif calcul : pcAccordes.getCalculsComparatifs()) {
            List<PersonnePCAccordee> list = new ArrayList<>();
            for (PersonnePCAccordee personne : calcul.getPersonnes()) {
                list.add(pcAccordesReforme.getPersonnes().get(personne.getIdPersonne()));
            }
            calcul.setPersonnes(list);
        }
    }

    private List<CalculComparatif> getCalculReforme(List<CalculComparatif> listCalcul) {
        for (CalculComparatif calcul : listCalcul) {
            calcul.setReformePc(true);
        }
        return listCalcul;
    }

    private List<PeriodePCAccordee> calculeDroitPartagePeriodes(Droit droit, String dateDebutPlageCalcul,
                                                                String dateFinPlageCalcul, Map<String, JadeAbstractSearchModel> cacheDonneesBD,
                                                                DonneesHorsDroitsProvider containerGlobal, boolean isDateFinForce) throws CalculException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        List<PeriodePCAccordee> listePCAccordes;

        listePCAccordes = PegasusImplServiceLocator.getPeriodesService().recherchePeriodesCalcul(droit,
                dateDebutPlageCalcul, dateFinPlageCalcul, cacheDonneesBD, containerGlobal, isDateFinForce);
        return listePCAccordes;
    }

    private void calculeDroitPersistence(Droit droit, boolean retroactif, List<PeriodePCAccordee> listePCAccordes,
                                         Map<String, JadeAbstractSearchModel> cacheDonneesBD) throws JadePersistenceException, JadeApplicationException{
        JadeBusinessMessage[] calculProcessWarns = null;

        // gestion warn pour le calcul
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            calculProcessWarns = JadeThread.logMessages();
            JadeThread.logClear();
        }

        CalculPcaReplaceSearch pcaReplaced = (CalculPcaReplaceSearch) cacheDonneesBD
                .get(ConstantesCalcul.CONTAINER_DONNEES_PCACCORDEES_REPLACED);

        CopiesGenerate copiesGenerate = new CopiesGenerate(PegasusImplServiceLocator.getCalculPersistanceService());

        List<CalculPcaReplace> copies = copiesGenerate.generateAndPersist(pcaReplaced, listePCAccordes.get(0)
                .getStrDateDebut(), droit.getSimpleVersionDroit());

        List<SimplePCAccordee> allNewPca = new ArrayList<SimplePCAccordee>();
        List<PCAccordeePlanCalcul> pcas = new ArrayList<PCAccordeePlanCalcul>();
        List<DonneeInterneHomeVersement> homeVersementList = new ArrayList<>();
        List<DonneeInterneHomeVersement> sejourVersementList = new ArrayList<>();
        Map<String, String> mapMontantTotalHome = new HashMap<>();
        // sauve les resultats en BD
        for (PeriodePCAccordee pcAccordee : listePCAccordes) {
            pcAccordee.setCalculRetro(retroactif);
            pcas.addAll(PegasusImplServiceLocator.getCalculPersistanceService().sauvePCAccordee(droit, pcAccordee,
                    pcaReplaced));
            for (CalculComparatif cc : pcAccordee.getCalculsComparatifs()) {
                if (cc.getIsPlanretenu()) {
                    checkSejourMoisPartiel(droit, pcAccordee, cc, sejourVersementList);
                    String montantTotalHome = cc.getMontantPrixHome();
                    Float montantDepensePersonnel = cc.getMontants().getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL);
                    mapMontantTotalHome.put(REQUERANT_MNT_SEJOUR, montantDepensePersonnel.toString());
                    for (PersonnePCAccordee personnePCAccordee : cc.getPersonnes()) {
                        if (personnePCAccordee.getIsHome()) {
                            if (!JadeStringUtil.isBlankOrZero(montantTotalHome)) {
                                mapMontantTotalHome.put(REQUERANT_HOME, montantTotalHome);
                                mapMontantTotalHome.put(REQUERANT_DEP_PERS, montantDepensePersonnel.toString());
                            }
                        }
                    }
                }
            }
            for (CalculComparatif cc : pcAccordee.getCalculsComparatifsConjoint()) {
                if (cc.getIsPlanretenu()) {
                    checkSejourMoisPartiel(droit, pcAccordee, cc, sejourVersementList);
                    String montantTotalHome = cc.getMontantPrixHome();
                    Float montantDepensePersonnel = cc.getMontants().getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL);
                    for (PersonnePCAccordee personnePCAccordee : cc.getPersonnes()) {
                        if (personnePCAccordee.getIsHome()) {
                            if (!JadeStringUtil.isBlankOrZero(montantTotalHome)) {
                                mapMontantTotalHome.put(CONJOINT_HOME, montantTotalHome);
                                mapMontantTotalHome.put(CONJOINT_DEP_PERS, montantDepensePersonnel.toString());
                            }
                        }
                    }
                }
            }
        }

        for (PCAccordeePlanCalcul pca : pcas) {
            SimplePCAccordee simplePCAccordee = pca.getSimplePCAccordee();
            simplePCAccordee.setMontantMensuel(pca.getSimplePlanDeCalcul().getMontantPCMensuelle());
            allNewPca.add(simplePCAccordee);
        }

        for (CalculPcaReplace pca : copies) {
            allNewPca.add(pca.getSimplePCAccordee());
        }

        CalculDonneesHomeSearch homeReplaced = (CalculDonneesHomeSearch) cacheDonneesBD
                .get(ConstantesCalcul.CONTAINER_DONNEES_HOMES);


        /**
         * Gestion des versement en home (futur,retenus)
         */
        if (homeReplaced != null) {
            mappingHomes(homeVersementList, homeReplaced, allNewPca, mapMontantTotalHome,droit);
        }

        for (SimplePCAccordee simplePCAccordee : allNewPca) {
            reporterLaRetenuSiExistant(pcaReplaced, simplePCAccordee, homeVersementList);
            updateOldRetenuWithDateEnd(pcaReplaced, simplePCAccordee);
            createCreancierHystorique(pcaReplaced);
        }
        filterCreancier(pcaReplaced, homeVersementList);
        createRetenusForVersementsHome(homeVersementList);

        Map<String, Creancier> mapCreancierDejaCreer = new HashMap<>();
        createCreancierForVersementsHome(homeVersementList, mapCreancierDejaCreer);
        createCreancierForVersementsSejour(sejourVersementList, mapCreancierDejaCreer);

        GeneratePcaToDelete generatePcaToDelete = new GeneratePcaToDelete(allNewPca, pcaReplaced);
        copiesGenerate.persist(generatePcaToDelete.generate());
        // si tout s'est bien passé, on met la version du droit en etat calculé
        droit.getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_CALCULE);
        PegasusServiceLocator.getDroitService().updateDroit(droit);

        if (copiesGenerate.getHasMoreCopieHasExpected()) {
            String ids = "";
            for (CalculPcaReplace copie : copies) {
                ids = ids + copie.getSimplePCAccordee().getIdPcaParent() + "-";
            }
            // On fait un warn pour ne pas bloquer l'utilisateur.
            JadeThread.logWarn(this.getClass().getName(), "pegasus.calcul.copie.tooMutch", new String[]{ids});
        }

        checkIfWarnForRFM(pcas, pcaReplaced);

        // Gestion warn calcul
        if (calculProcessWarns != null) {
            // iteration sur les warn stocké
            for (JadeBusinessMessage warnMess : calculProcessWarns) {
                JadeThread.logWarn("", warnMess.getMessageId());
            }
        }
    }

    private void checkSejourMoisPartiel(Droit droit, PeriodePCAccordee pcAccordee, CalculComparatif cc, List<DonneeInterneHomeVersement> sejourVersementList) throws JadeApplicationException, JadePersistenceException {
        if(!cc.getMontants().containsValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL)){
            return;
        }

        Float montantSejour = cc.getMontants().getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL);
        TupleDonneeRapport tupleMoisPartiel = cc.getMontants().getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL);
        if (tupleMoisPartiel == null) {
            return;
        }

        Float versementDirect = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_VERSEMENT_DIRECT);
        Float idHome = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME);

        if(montantSejour != 0.0f && TupleDonneeRapport.readBoolean(versementDirect)) {
            ListPCAccordee pca = PegasusImplServiceLocator.getPCAccordeeService().read(pcAccordee.getIdSimplePcAccordee());
            Home complexeHome = PegasusImplServiceLocator.getHomeService().read(Float.toString(idHome));
            SimpleHome home = complexeHome.getSimpleHome();
            AdresseTiersDetail homeAdressePaiementFormatee = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(home.getIdTiersHome(), Boolean.TRUE, TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), "");
            String idAdressePaiement = homeAdressePaiementFormatee.getFields().get(AdresseTiersDetail.ADRESSEP_ID_ADRESSE);

            DonneeInterneHomeVersement donnee = new DonneeInterneHomeVersement();
            donnee.setIdRenteAccordee(pca.getSimplePCAccordee().getIdPrestationAccordee());
            donnee.setIdPca(pca.getSimplePCAccordee().getIdPCAccordee());
            donnee.setIdAdressePaiement(idAdressePaiement);
            donnee.setIdTiersHome(home.getIdTiersHome());
            donnee.setMontantPCMensuel(pca.getSimplePlanDeCalcul().getMontantPCMensuelle());
            donnee.setIdDemande(droit.getDemande().getSimpleDemande().getIdDemande());
            donnee.setIdTiersHome(home.getIdTiersHome());
            donnee.setIdTiersAdressePaiement(home.getIdTiersHome());
            donnee.setIdTiersRegroupement(pca.getIdTiersBeneficiaire());
            donnee.setCsRoleBeneficiaire(pca.getSimplePCAccordee().getCsRoleBeneficiaire());
            donnee.setMontantHomes(Float.toString(montantSejour));
            donnee.setMontantDepenses(Float.toString(cc.getMontants().getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL)));
            donnee.setCsTypeVersement(DonneeInterneHomeVersement.TYPE_CREANCIER);
            donnee.setDateDebut(pcAccordee.getStrDateDebut());
            donnee.setDateFin(pcAccordee.getStrDateFin());

            sejourVersementList.add(donnee);
        }

    }

    private void reporterLaRetenuSiExistant(CalculPcaReplaceSearch anciennesPCAccordees, SimplePCAccordee simplePCAccordee, List<DonneeInterneHomeVersement> donneeInterneHomeVersements) throws JadeApplicationException, JadePersistenceException {
        CalculPcaReplace anciennePCACourante;
        DonneeInterneHomeVersement donneeInterneHomeVersementNew;
        String idPcaOld;
        Map<String, DonneeInterneHomeVersement> mapNewRetenues = new HashMap<>();
        Map<String, PcaRetenue> mapOldHomeRetenues = new HashMap<>();
        Map<String, JadeAbstractModel> mapAutreRetenues = new HashMap<>();
        //Préparation retrouver le bon retenu si il y'a déjà une retenue de l'ancienne droit pour le même home.
        for (DonneeInterneHomeVersement donneeInterneHomeVersement : donneeInterneHomeVersements) {
            if (donneeInterneHomeVersement.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_RETENUS)) {
                mapNewRetenues.put(donneeInterneHomeVersement.getIdTiersAdressePaiement()+""+donneeInterneHomeVersement.getCsRoleBeneficiaire(), donneeInterneHomeVersement);
            }
        }
        if (JadeStringUtil.isBlankOrZero(simplePCAccordee.getDateFin())) {
            for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
                CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
                if (JadeStringUtil.isBlankOrZero(ancienneDonnee.getSimplePCAccordee().getDateFin())
                        && IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(ancienneDonnee.getSimplePCAccordee().getCsEtatPC())) {
                    if (ancienneDonnee.getSimplePrestationsAccordees().getIsRetenues()) {
                        idPcaOld = ancienneDonnee.getSimplePCAccordee().getIdPCAccordee();
                        PcaRetenueSearch search = new PcaRetenueSearch();
                        search.setForIdPca(idPcaOld);
                        search = PegasusServiceLocator.getRetenueService().search(search);
                        for (JadeAbstractModel model : search.getSearchResults()) {
                            PcaRetenue retenueAncienne = (PcaRetenue) model;
                            //Si c'est un home, on le map dans la liste des home.
                            if (isHome(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt())) {
                                mapOldHomeRetenues.put(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt()+""+retenueAncienne.getCsRoleFamillePC(), retenueAncienne);
                            }else{
                                mapAutreRetenues.put(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt()+""+retenueAncienne.getCsRoleFamillePC(), model);
                            }
                        }
                    }
                }
            }
        }
        for(String oldKey : mapOldHomeRetenues.keySet()){
                if(mapNewRetenues.containsKey(oldKey)){
                    donneeInterneHomeVersementNew =mapNewRetenues.get(oldKey);
                    Float montantAVerser = getMontantHome(donneeInterneHomeVersementNew.getMontantHomes(), 1);
                    Float montantAVerserOld = Float.parseFloat(mapOldHomeRetenues.get(oldKey).getSimpleRetenue().getMontantRetenuMensuel());
                    if (montantAVerser.floatValue() == montantAVerserOld.floatValue()) {
                        PcaRetenue retenue;
                        try {
                            retenue = (PcaRetenue) JadePersistenceUtil.clone(mapOldHomeRetenues.get(oldKey));
                        } catch (JadeCloneModelException e) {
                            throw new PCAccordeeException("Unable to clone this PCA id: "
                                    + donneeInterneHomeVersementNew.getIdPca());
                        }
                        retenue.setIdPCAccordee(donneeInterneHomeVersementNew.getIdPca());
                        retenue.getSimpleRetenue().setDateDebutRetenue(
                                PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());
                        PegasusServiceLocator.getRetenueService().createWithOutCheck(retenue);
                        donneeInterneHomeVersements.remove(donneeInterneHomeVersementNew);
                    }
                }
        }
        for(Map.Entry<String, JadeAbstractModel> retenueAutre:mapAutreRetenues.entrySet()){
                PcaRetenue retenue;
                try {
                    retenue = (PcaRetenue) JadePersistenceUtil.clone(retenueAutre.getValue());
                } catch (JadeCloneModelException e) {
                    throw new PCAccordeeException("Unable to clone this PCA id: "
                            + simplePCAccordee.getIdPCAccordee());
                }
                retenue.setIdPCAccordee(simplePCAccordee.getIdPCAccordee());
                retenue.getSimpleRetenue().setDateDebutRetenue(
                        PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());
                PegasusServiceLocator.getRetenueService().createWithOutCheck(retenue);
        }


//        if (JadeStringUtil.isBlankOrZero(simplePCAccordee.getDateFin())) {
//            for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
//                CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
//                if (JadeStringUtil.isBlankOrZero(ancienneDonnee.getSimplePCAccordee().getDateFin())
//                        && IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(ancienneDonnee.getSimplePCAccordee().getCsEtatPC())) {
//                    if (ancienneDonnee.getSimplePrestationsAccordees().getIsRetenues()) {
//                        idPcaOld = ancienneDonnee.getSimplePCAccordee().getIdPCAccordee();
//                        PcaRetenueSearch search = new PcaRetenueSearch();
//                        search.setForIdPca(idPcaOld);
//                        search = PegasusServiceLocator.getRetenueService().search(search);
//                        for (JadeAbstractModel model : search.getSearchResults()) {
//                            PcaRetenue retenueAncienne = (PcaRetenue) model;
//                            //Si même home mais montant identique : on repend la même retenu et on enlève la nouvelle retenue de la liste à créer
//                            if (mapNewRetenues.containsKey(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt())) {
//                                donneeInterneHomeVersementNew = mapNewRetenues.get(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt());
//                                Float montantAVerser = getMontantHome(donneeInterneHomeVersementNew.getMontantHomes(), 1);
//                                Float montantAVerserOld = Float.parseFloat(retenueAncienne.getSimpleRetenue().getMontantRetenuMensuel());
//                                if (montantAVerser.floatValue() == montantAVerserOld.floatValue()) {
//                                    PcaRetenue retenue;
//                                    try {
//                                        retenue = (PcaRetenue) JadePersistenceUtil.clone(model);
//                                    } catch (JadeCloneModelException e) {
//                                        throw new PCAccordeeException("Unable to clone this PCA id: "
//                                                + simplePCAccordee.getIdPCAccordee());
//                                    }
//                                    retenue.setIdPCAccordee(simplePCAccordee.getIdPCAccordee());
//                                    retenue.getSimpleRetenue().setDateDebutRetenue(
//                                            PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());
//                                    PegasusServiceLocator.getRetenueService().createWithOutCheck(retenue);
//                                    donneeInterneHomeVersements.remove(donneeInterneHomeVersementNew);
//                                }
//                            //Si l'ancienne retenu ne fait pas parti des nouvelles retenues à crééer => ça veut que dire que le home du bénéficiaire a été supprimé.
//                            } else if (!isHome(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt())){
//                                PegasusServiceLocator.getRetenueService().delete(retenueAncienne);
//                                //Pour le reste qui n'est pas un home, on reprends les retenus actifs.
//                            }else{
//                                SimpleHomeSearch simpleHomeSearch = new SimpleHomeSearch();
//                                simpleHomeSearch.setForIdTiersHome(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt());
//                                if (retenueAncienne.getCsRoleFamillePC().equals(simplePCAccordee.getCsRoleBeneficiaire()) && PegasusImplServiceLocator.getSimpleHomeService().count(simpleHomeSearch)==0) {
//                                    PcaRetenue retenue;
//                                    try {
//                                        retenue = (PcaRetenue) JadePersistenceUtil.clone(model);
//                                    } catch (JadeCloneModelException e) {
//                                        throw new PCAccordeeException("Unable to clone this PCA id: "
//                                                + simplePCAccordee.getIdPCAccordee());
//                                    }
//                                    retenue.setIdPCAccordee(simplePCAccordee.getIdPCAccordee());
//                                    retenue.getSimpleRetenue().setDateDebutRetenue(
//                                            PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());
//                                    PegasusServiceLocator.getRetenueService().createWithOutCheck(retenue);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * Vérifier si l'idTiers de l'adresse de paiement vient d'un home
     * @param idTiersAdressePmt
     * @return
     */
    private boolean isHome(String idTiersAdressePmt) throws JadeApplicationServiceNotAvailableException, HomeException, JadePersistenceException {
        SimpleHomeSearch search = new SimpleHomeSearch();
        search.setForIdTiersHome(idTiersAdressePmt);
        if(PegasusImplServiceLocator.getSimpleHomeService().count(search)>0){
            return true;
        }
        return false;
    }


    /**
     * Il va supprimer les créancier et retenues existants liés à l'ancien PCA pour éviter un doublon selon des règles définies.
     */
    private void filterCreancier(CalculPcaReplaceSearch anciennesPCAccordees, List<DonneeInterneHomeVersement> homeVersementList) throws JadeApplicationException, JadePersistenceException {
        String dateDebut = "";
        String dateFin = "";
        Map<String,CreanceAccordee> mapCreanceTrouve = new HashMap<>();
        Map<String,CreanceAccordee> mapOldCreance = new HashMap<>();
        Map<String,DonneeInterneHomeVersement> mapNewCreance = new HashMap<>();
        for (DonneeInterneHomeVersement donneeInterneHomeVersement : homeVersementList) {
            if (donneeInterneHomeVersement.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_CREANCIER)) {
                mapNewCreance.put(donneeInterneHomeVersement.getIdTiersAdressePaiement(),donneeInterneHomeVersement);
            }
        }
        for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
            CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
            CreanceAccordeeSearch creancierSearch = new CreanceAccordeeSearch();
            creancierSearch.setForIdVersionDroit(ancienneDonnee.getSimplePCAccordee().getIdVersionDroit());
            creancierSearch = PegasusServiceLocator.getCreanceAccordeeService().search(creancierSearch);
            for (JadeAbstractModel model : creancierSearch.getSearchResults()) {
                CreanceAccordee creanceAccordee = (CreanceAccordee) model;
                mapOldCreance.put(creanceAccordee.getSimpleCreancier().getIdTiersAdressePaiement(),creanceAccordee);
            }
        }
        DonneeInterneHomeVersement donneeInterneHomeVersement;
        CreanceAccordee creanceAccordee;
        for(String oldKey : mapOldCreance.keySet()) {
            if (mapNewCreance.containsKey(oldKey)) {
                donneeInterneHomeVersement = mapNewCreance.get(oldKey);
                creanceAccordee = mapOldCreance.get(oldKey);
                if (JadeStringUtil.isBlankOrZero(donneeInterneHomeVersement.getDateFin())) {
                    dateFin = JadeDateUtil.getLastDateOfMonth(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
                } else {
                    dateFin = JadeDateUtil.getLastDateOfMonth(donneeInterneHomeVersement.getDateFin());
                }
                dateDebut = JadeDateUtil.getFirstDateOfMonth(donneeInterneHomeVersement.getDateDebut());
                int nbreMois = JadeDateUtil.getNbMonthsBetween(dateDebut, dateFin);
                Float montantAVerser = getMontantHome(donneeInterneHomeVersement.getMontantHomes(), nbreMois);
                // (A)
                if (!creanceAccordee.getSimpleCreancier().getMontant().equals(montantAVerser.toString())) {
                    PegasusImplServiceLocator.getSimpleCreancierService().deleteWithoutControl(creanceAccordee.getSimpleCreancier());
                    PegasusImplServiceLocator.getCreanceAccordeeService().delete(creanceAccordee);
                    mapCreanceTrouve.put(creanceAccordee.getSimpleCreancier().getIdTiersAdressePaiement(),creanceAccordee);
                } else {
                    homeVersementList.remove(donneeInterneHomeVersement);
                }
            }else{
                creanceAccordee = mapOldCreance.get(oldKey);
                if(Boolean.TRUE.equals(creanceAccordee.getSimpleCreancier().getIsHome())){
                    PegasusImplServiceLocator.getSimpleCreancierService().deleteWithoutControl(creanceAccordee.getSimpleCreancier());
                    PegasusImplServiceLocator.getCreanceAccordeeService().delete(creanceAccordee);
                }

            }

        }


    }

    private void createCreancierForVersementsHome(List<DonneeInterneHomeVersement> homeVersementList, Map<String, Creancier> mapCreancierDejaCreer) throws JadeApplicationServiceNotAvailableException, PmtMensuelException, CreancierException, JadePersistenceException {
        String dateDebut = "";
        String dateFin = "";
        for (DonneeInterneHomeVersement calculDonneesHome : homeVersementList) {
            if (calculDonneesHome.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_CREANCIER)) {
                if (JadeStringUtil.isBlankOrZero(calculDonneesHome.getDateFin())) {
                    dateFin = JadeDateUtil.getLastDateOfMonth(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
                } else {
                    dateFin = JadeDateUtil.getLastDateOfMonth(calculDonneesHome.getDateFin());
                }
                dateDebut = JadeDateUtil.getFirstDateOfMonth(calculDonneesHome.getDateDebut());
                int nbreMois = JadeDateUtil.getNbMonthsBetween(dateDebut, dateFin);
                Float montantAVerser = getMontantHome(calculDonneesHome.getMontantHomes(), nbreMois);
                Creancier creancier = getCreancier(mapCreancierDejaCreer, calculDonneesHome, montantAVerser, false);
                CreanceAccordee creanceAccordee = new CreanceAccordee();
                SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();
                simpleCreanceAccordee.setIdCreancier(creancier.getId());
                simpleCreanceAccordee.setIdPCAccordee(calculDonneesHome.getIdPca());
                Float montantPCMensuelDeduit = Float.parseFloat(calculDonneesHome.getMontantPCMensuel()) - (Float.parseFloat(calculDonneesHome.getMontantDepenses()) / 12) - (Float.parseFloat(calculDonneesHome.getMontantDejaVerser()));
                montantPCMensuelDeduit = montantPCMensuelDeduit * nbreMois;
                if (montantPCMensuelDeduit < 0.0) {
                    montantPCMensuelDeduit = Float.parseFloat("0");
                }
                if (montantAVerser > montantPCMensuelDeduit) {
                    simpleCreanceAccordee.setMontant(montantPCMensuelDeduit.toString());
                } else {
                    simpleCreanceAccordee.setMontant(montantAVerser.toString());
                }
                creanceAccordee.setSimpleCreanceAccordee(simpleCreanceAccordee);
                PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);
            }
        }
    }

    private void createCreancierForVersementsSejour(List<DonneeInterneHomeVersement> homeVersementList, Map<String, Creancier> mapCreancierDejaCreer) throws JadeApplicationServiceNotAvailableException, PmtMensuelException, CreancierException, JadePersistenceException {
        for (DonneeInterneHomeVersement calculDonneesHome : homeVersementList) {
            String dateFin = JadeDateUtil.getLastDateOfMonth(calculDonneesHome.getDateFin());
            String dateDebut = JadeDateUtil.getFirstDateOfMonth(calculDonneesHome.getDateDebut());
            Float montantAVerser = getMontantHome(calculDonneesHome.getMontantHomes(), 1);
            Creancier creancier = getCreancier(mapCreancierDejaCreer, calculDonneesHome, montantAVerser, true);
            CreanceAccordee creanceAccordee = new CreanceAccordee();
            SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();
            simpleCreanceAccordee.setIdCreancier(creancier.getId());
            simpleCreanceAccordee.setIdPCAccordee(calculDonneesHome.getIdPca());
            Float montantPCMensuel = Float.parseFloat(calculDonneesHome.getMontantPCMensuel());
            if (montantAVerser > montantPCMensuel) {
                simpleCreanceAccordee.setMontant(montantPCMensuel.toString());
            } else {
                simpleCreanceAccordee.setMontant(montantAVerser.toString());
            }
            creanceAccordee.setSimpleCreanceAccordee(simpleCreanceAccordee);
            PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);
        }
    }

    /**
     * Empêcher de créer plusieurs même créanciers s'il y a plusieurs créances sur le même.
     */
    private Creancier getCreancier(Map<String, Creancier> mapCreancierDejaCreer, DonneeInterneHomeVersement calculDonneesHome, Float montantAVerser, boolean addMontant) throws CreancierException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        Creancier creancier;
        if (!mapCreancierDejaCreer.containsKey(calculDonneesHome.getIdTiersHome())) {
             creancier = new Creancier();
            SimpleCreancier simpleCreancier = new SimpleCreancier();
            simpleCreancier.setCsEtat(IPCDroits.CS_ETAT_CREANCE_A_PAYE);
            simpleCreancier.setCsTypeCreance(IPCCreancier.CS_TYPE_CREANCE_TIERS);
            simpleCreancier.setIdDemande(calculDonneesHome.getIdDemande());
            simpleCreancier.setIdDomaineApplicatif(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            simpleCreancier.setIdTiers(calculDonneesHome.getIdTiersHome());
            simpleCreancier.setIdTiersAdressePaiement(calculDonneesHome.getIdTiersHome());
            simpleCreancier.setIdTiersRegroupement(calculDonneesHome.getIdTiersRegroupement());
            simpleCreancier.setMontant(montantAVerser.toString());
            simpleCreancier.setIsCalcule(true);
            simpleCreancier.setIsHome(true);
            creancier.setSimpleCreancier(simpleCreancier);
            creancier = PegasusServiceLocator.getCreancierService().create(creancier);
            mapCreancierDejaCreer.put(calculDonneesHome.getIdTiersHome(), creancier);
        } else {
            creancier = mapCreancierDejaCreer.get(calculDonneesHome.getIdTiersHome());
            if(addMontant) {
                Float montant = Float.parseFloat(creancier.getSimpleCreancier().getMontant()) + montantAVerser;
                creancier.getSimpleCreancier().setMontant(montant.toString());
                PegasusServiceLocator.getCreancierService().update(creancier);
            }
        }
        return creancier;
    }

    /**
     * Calcul pour sortir le montant exacte du créancier ou retennues
     *
     * @param montantHome
     * @param nbreMois
     * @return
     */
    private Float getMontantHome(String montantHome, int nbreMois) {
        return Float.parseFloat(montantHome) / 12 * nbreMois;
    }

    private void createRetenusForVersementsHome(List<DonneeInterneHomeVersement> homeVersementList) throws JadeApplicationException, JadePersistenceException {
        for (DonneeInterneHomeVersement calculDonneesHome : homeVersementList) {
            PcaRetenue pcaRetenue = new PcaRetenue();
            if (calculDonneesHome.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_RETENUS)) {
                pcaRetenue.setIdPCAccordee(calculDonneesHome.getIdPca());
                pcaRetenue.setCsRoleFamillePC(calculDonneesHome.getCsRoleBeneficiaire());
                SimpleRetenuePayement retenue = new SimpleRetenuePayement();
                retenue.setCsTypeRetenue(IPCRetenues.CS_ADRESSE_PAIEMENT);
                retenue.setDateDebutRetenue(calculDonneesHome.getDateDebut());
                retenue.setDateFinRetenue(calculDonneesHome.getDateFin());
                retenue.setIdTiersAdressePmt(calculDonneesHome.getIdTiersHome());
                retenue.setIdRenteAccordee(calculDonneesHome.getIdRenteAccordee());
                retenue.setIdDomaineApplicatif(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                Float montantAVerser = getMontantHome(calculDonneesHome.getMontantHomes(), 1);
                retenue.setMontantRetenuMensuel(montantAVerser.toString());
                retenue.setMontantTotalARetenir("999'999.00");
                pcaRetenue.setSimpleRetenue(retenue);
                PegasusServiceLocator.getRetenueService().createWithOutCheck(pcaRetenue);
            }
        }
    }

    private void mappingHomes(List<DonneeInterneHomeVersement> homeVersementList, CalculDonneesHomeSearch listHomes, List<SimplePCAccordee> allNewPca, Map<String, String> mapMontantTotalHome, Droit droit) throws JadeApplicationServiceNotAvailableException, PmtMensuelException, CalculException {

        DonneeInterneHomeVersement donnee;
        Map<String, List<SimplePCAccordee>> mapToIDPCA = new HashMap<>();
        List<SimplePCAccordee> listPCA;
        for (SimplePCAccordee pca : allNewPca) {
            if (!JadeStringUtil.isBlankOrZero(pca.getMontantMensuel())) {
                if (mapToIDPCA.containsKey(pca.getIdTiersBeneficiaire())) {
                    listPCA = mapToIDPCA.get(pca.getIdTiersBeneficiaire());
                    listPCA.add(pca);
                    mapToIDPCA.put(pca.getIdTiersBeneficiaire(), listPCA);
                } else {
                    listPCA = new ArrayList<>();
                    listPCA.add(pca);
                    mapToIDPCA.put(pca.getIdTiersBeneficiaire(), listPCA);
                }

            }
        }

        for (JadeAbstractModel model : listHomes.getSearchResults()) {
            CalculDonneesHome home = (CalculDonneesHome) model;
            if (Boolean.TRUE.equals(home.getIsVersementDirect()) && mapToIDPCA.containsKey(home.getIdTiersRegroupement())) {
                Map<Periode, String> mapPeriode = createPeriodeVersementHome(home.getDateDebutDFH(), home.getDateFinDFH());
                listPCA = mapToIDPCA.get(home.getIdTiersRegroupement());
                for (Map.Entry<Periode, String> entry : mapPeriode.entrySet()) {
                    String csTypeVersement = entry.getValue();
                    Periode periode = entry.getKey();
                    for (SimplePCAccordee simplePCAccordeeBenef : listPCA) {
                        donnee = new DonneeInterneHomeVersement();
                        donnee.setIdRenteAccordee(simplePCAccordeeBenef.getIdPrestationAccordee());
                        donnee.setIdPca(simplePCAccordeeBenef.getIdPCAccordee());
                        donnee.setIdAdressePaiement(home.getIdAdressePaiement());
                        donnee.setIdTiersHome(home.getIdTiersHome());
                        donnee.setMontantPCMensuel(simplePCAccordeeBenef.getMontantMensuel());
                        donnee.setIdDemande(home.getIdDemande());
                        donnee.setIdTiersHome(home.getIdTiersHome());
                        donnee.setIdTiersAdressePaiement(home.getIdTiersHome());
                        donnee.setIdTiersRegroupement(home.getIdTiersRegroupement());
                        donnee.setCsRoleBeneficiaire(simplePCAccordeeBenef.getCsRoleBeneficiaire());
                        //Cas séperation où le conjoint est devenu requérant.
                        if (donnee.getCsRoleBeneficiaire().equals(RoleMembreFamille.REQUERANT.getValue())) {
                            donnee.setMontantHomes(mapMontantTotalHome.get(REQUERANT_HOME));
                            donnee.setMontantDepenses(mapMontantTotalHome.get(REQUERANT_DEP_PERS));
                        } else {
                            donnee.setMontantHomes(mapMontantTotalHome.get(CONJOINT_HOME));
                            donnee.setMontantDepenses(mapMontantTotalHome.get(CONJOINT_DEP_PERS));
                        }
                        String montantDejaVerser = searchMontantDejaVerser(periode,droit.getSimpleDroit().getIdDroit(),droit.getSimpleVersionDroit().getNoVersion());
                        donnee.setMontantDejaVerser(montantDejaVerser);
                        donnee.setCsTypeVersement(csTypeVersement);
                        donnee.setDateDebut(periode.getDateDebut());
                        donnee.setDateFin(periode.getDateFin());
                        if (donnee.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_RETENUS) && JadeStringUtil.isBlankOrZero(simplePCAccordeeBenef.getDateFin())
                                || (donnee.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_CREANCIER) && IsSamePeriode(simplePCAccordeeBenef.getDateDebut(),simplePCAccordeeBenef.getDateFin(),periode))) {
                            homeVersementList.add(donnee);
                        }
                    }
                }
            }
        }
    }

    private String searchMontantDejaVerser(Periode periodes, String idDroit, String noVersionDroitCourant) throws CalculException {
        try {
            String dateMin = periodes.getDateDebut();
            String dateMax = periodes.getDateFin();
            if(JadeStringUtil.isBlankOrZero(dateMax)){
                dateMax = "12." + dateMin.substring(3);
            }
            List<PcaForDecompte> pcaDejaVerser = PcaPrecedante.findPcaToReplaced(dateMin, dateMax, idDroit,
                    noVersionDroitCourant);
            if(pcaDejaVerser.size() ==1){
                return pcaDejaVerser.get(0).getMontantPCMensuelle();
            }else{
                return "0.0";
            }
        } catch (Exception e) {
            throw new CalculException("Service not available - " + e.getMessage());
        }


    }

    private boolean IsSamePeriode(String dateDebut, String dateFin, Periode periode) {
        if(JadeStringUtil.isBlankOrZero(dateFin)){
            if(isDateAfterOrEquals(periode.getDateDebut(),dateDebut)){
                return true;
            }
        }else{
            if(dateDebut.equals(periode.getDateDebut()) && dateFin.equals(periode.getDateFin())){
                return true;
            }
        }
        return false;
    }


    private Map<Periode, String> createPeriodeVersementHome(String dateDebutDFH, String dateFinDFH) throws JadeApplicationServiceNotAvailableException, PmtMensuelException {
        Map<Periode, String> map = new HashMap<>();
        Periode periode;

        /**
         *  CAS 1 : Entrée en home à partir du mois du prochain paiement
         */
        if (isDateAfterOrEquals(dateDebutDFH, PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt())) {
            periode = new Periode(dateDebutDFH, dateFinDFH);
            map.put(periode, DonneeInterneHomeVersement.TYPE_RETENUS);
        } else {
            /**
             *  CAS 2 : Entrée en home avant le mois du prochain paiement et termine après ce même mois.
             */
            if (JadeStringUtil.isBlankOrZero(dateFinDFH) || (!JadeStringUtil.isBlankOrZero(dateFinDFH) && isDateAfterOrEquals(dateFinDFH, PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt()))) {
                periode = new Periode(dateDebutDFH, PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
                map.put(periode, DonneeInterneHomeVersement.TYPE_CREANCIER);
                periode = new Periode(PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt(), dateFinDFH);
                map.put(periode, DonneeInterneHomeVersement.TYPE_RETENUS);
            } else {
                /**
                 *  CAS 3 : Entrée en home avant le mois du prochain paiement et termine avant ce même mois.
                 */
                periode = new Periode(dateDebutDFH, dateFinDFH);
                map.put(periode, DonneeInterneHomeVersement.TYPE_CREANCIER);
            }

        }

        return map;

    }

    private boolean isDateAfterOrEquals(String dateDebutDFH, String dateProchainPmt) {
        if (JadeDateUtil.isDateAfter(dateProchainPmt, dateDebutDFH) || dateDebutDFH.equals(dateProchainPmt)) {
            return true;
        }
        return false;

    }

    /**
     * Fermer tout les retenues des pcas de l'ancien droit pour éviter un blocage de ceux qui ont des retenues sans dates de fins
     *
     * @param anciennesPCAccordees
     * @param simplePCAccordee
     * @param isVersementHome
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

    private void updateOldRetenuWithDateEnd(CalculPcaReplaceSearch anciennesPCAccordees, SimplePCAccordee simplePCAccordee) throws JadeApplicationException, JadePersistenceException {
        CalculPcaReplace anciennePCACourante;
        String idPcaOld;
        String dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        if (JadeStringUtil.isBlankOrZero(simplePCAccordee.getDateFin())) {
            for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
                CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
                // On recherche la pca courante (Qui n'a pas de date fin)
                if (JadeStringUtil.isBlankOrZero(ancienneDonnee.getSimplePCAccordee().getDateFin())
                        && IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(ancienneDonnee.getSimplePCAccordee().getCsEtatPC())) {
                    anciennePCACourante = ancienneDonnee;
                    if (Boolean.TRUE.equals(anciennePCACourante.getSimplePrestationsAccordees().getIsRetenues())) {
                        idPcaOld = ancienneDonnee.getSimplePCAccordee().getIdPCAccordee();
                        PcaRetenueSearch search = new PcaRetenueSearch();
                        search.setForIdPca(idPcaOld);
                        search = PegasusServiceLocator.getRetenueService().search(search);
                        for (JadeAbstractModel model : search.getSearchResults()) {
                            PcaRetenue retenue = (PcaRetenue) model;
                            if (JadeStringUtil.isBlankOrZero(retenue.getSimpleRetenue().getDateFinRetenue())) {
                                retenue.getSimpleRetenue().setDateFinRetenue(dateProchainPaiement);
                                PegasusServiceLocator.getRetenueService().updateWithoutCheck(retenue);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Création de l'historique des  créanciers/créances si il n'existe pas
     *
     * @param anciennesPCAccordees
     */
    private void createCreancierHystorique(CalculPcaReplaceSearch anciennesPCAccordees) throws JadeApplicationServiceNotAvailableException, CreancierException, JadePersistenceException {
        for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
            CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
            String idPCAAccordee = ancienneDonnee.getSimplePCAccordee().getIdPCAccordee();
            CreanceAccordeeSearch creancierSearch = new CreanceAccordeeSearch();
            creancierSearch.setForIdPCAccordee(idPCAAccordee);
            creancierSearch = PegasusServiceLocator.getCreanceAccordeeService().search(creancierSearch);

            SimpleCreancierHystoriqueSearch simpleCreancierHystoriqueSearch = new SimpleCreancierHystoriqueSearch();
            simpleCreancierHystoriqueSearch.setForIdPcAccordee(idPCAAccordee);
            //Copie pour pouvoir revenir en arrière
            if (PegasusImplServiceLocator.getSimpleCreancierHystoriqueService().count(simpleCreancierHystoriqueSearch) == 0) {
                for (JadeAbstractModel abstractModel : creancierSearch.getSearchResults()) {
                    CreanceAccordee creanceAccordee = (CreanceAccordee) abstractModel;
                    if (creanceAccordee.getSimpleCreancier().getIsHome()) {
                        PegasusImplServiceLocator.getSimpleCreancierHystoriqueService().create(creanceAccordee.getSimpleCreancier(), creanceAccordee.getSimpleCreanceAccordee());
                    }
                }
            }
        }
    }

    /**
     * Log un warning pour les prestations accordées dans les RFM
     */
    private void checkIfWarnForRFM(List<PCAccordeePlanCalcul> pcaNew, CalculPcaReplaceSearch anciennePca) {
        if (anciennePca.getSize() > 0) {
            CalculPcaReplace oldPca = (CalculPcaReplace) anciennePca.getSearchResults()[0];
            // log un warn si passage d'octroi -> refus
            if (!IPCValeursPlanCalcul.STATUS_REFUS.equals(oldPca.getSimplePlanDeCalcul().getEtatPC())) {
                for (PCAccordeePlanCalcul newPca : pcaNew) {
                    if (IPCValeursPlanCalcul.STATUS_REFUS.equals(newPca.getSimplePlanDeCalcul().getEtatPC())) {
                        JadeThread.logWarn("", "pegasus.pc.warning.prestations.rfm");
                        return;
                    }
                }
            }
            // log un warn si passage de domicile -> home
            if (IPCPCAccordee.CS_GENRE_PC_DOMICILE.equals(oldPca.getSimplePCAccordee().getCsGenrePC())) {
                for (PCAccordeePlanCalcul newPca : pcaNew) {
                    if (IPCPCAccordee.CS_GENRE_PC_HOME.equals(newPca.getSimplePCAccordee().getCsGenrePC())) {
                        JadeThread.logWarn("", "pegasus.pc.warning.prestations.rfm");
                        return;
                    }
                }
            }
        }
    }

    private void calculeDroitTraitement(Droit droit, Map<String, JadeAbstractSearchModel> cacheDonneesBD,
                                        List<PeriodePCAccordee> listePCAccordes, List<PeriodePCAccordee> listePCAccordesReforme, String dateDebutPlageCalcul, String dateFinPlageCalcul,
                                        DonneesHorsDroitsProvider containerGlobal) throws CalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, TaxeJournaliereHomeException,
            HomeException {
        Map<String, CalculMembreFamille> listePersonnes = new HashMap<String, CalculMembreFamille>();
        // charge les données financières et autres
        PegasusImplServiceLocator.getCalculComparatifService().loadDonneesCalculComparatif(droit, cacheDonneesBD,
                listePCAccordes, listePersonnes, dateDebutPlageCalcul, dateFinPlageCalcul);

        // trie et consolide les données brutes du cache de données de
        // persistence
        boolean isFratrie = droit.getDemande().getSimpleDemande().getIsFratrie();
        String dateReforme;

        try {
            dateReforme = EPCProperties.DATE_REFORME_PC.getValue();
            dertermineCalculs(droit, listePCAccordes, listePCAccordesReforme, dateFinPlageCalcul, dateReforme);
        } catch (PropertiesException e) {
            throw new CalculException("Unbale to obtain properties for reforme pc", e);
        }

        boolean calculsComparatifsImmediats = listePCAccordes.isEmpty() || listePCAccordesReforme.isEmpty();

        if (!listePCAccordes.isEmpty()) {
            // trie et consolide les données brutes du cache de données de persistence
            PegasusImplServiceLocator.getCalculComparatifService().consolideCacheDonneesPersonnes(listePCAccordes,
                    cacheDonneesBD, listePersonnes, dateFinPlageCalcul, containerGlobal, false, isFratrie);
            // procède aux calcul comparatifs
            PegasusImplServiceLocator.getCalculComparatifService().calculePCAccordes(droit, listePCAccordes, calculsComparatifsImmediats);
        }

        if (!listePCAccordesReforme.isEmpty()) {
            // S170908_002 : ajout calcul réforme PC
            // trie et consolide les données brutes du cache de données de persistence
            PegasusImplServiceLocator.getCalculComparatifService().consolideCacheDonneesPersonnes(listePCAccordesReforme,
                    cacheDonneesBD, listePersonnes, dateFinPlageCalcul, containerGlobal, true, isFratrie);

            // procède aux calcul comparatifs
            PegasusImplServiceLocator.getCalculComparatifService().calculePCAccordes(droit, listePCAccordesReforme, calculsComparatifsImmediats);
        }
        fusionneCalcul(listePCAccordes, listePCAccordesReforme, dateReforme);
    }

    /**
     * Détermine quels sont les calculs à effectuer :
     * - uniquement avant réforme
     * - uniquement après réforme
     * - calcul comparatif entre avant et après
     *
     * @param droit
     * @param listePCAccordes
     * @param listePCAccordesReforme
     * @param dateFinPlageCalcul
     * @param dateReforme
     * @throws PropertiesException
     */
    private void dertermineCalculs(Droit droit, List<PeriodePCAccordee> listePCAccordes, List<PeriodePCAccordee> listePCAccordesReforme,
                                   String dateFinPlageCalcul, String dateReforme) throws PropertiesException {
        String dateDemande = droit.getDemande().getSimpleDemande().getDateDepot();
        Boolean reforme = EPCProperties.REFORME_PC.getBooleanValue();
        if (JadeDateUtil.isDateBefore(dateFinPlageCalcul, dateReforme) || !reforme) {
            // calcul sur ancien calculateur uniquement
            listePCAccordesReforme.clear();
        } else if (!droit.getDemande().getSimpleDemande().getForcerCalculTransitoire() &&
                (dateDemande.equals(dateReforme) || JadeDateUtil.isDateAfter(dateDemande, dateReforme))) {
            // calcul uniquement calcul réforme
            listePCAccordes.clear();
        }
        // sinon on garde les 2 listes pour le calcul comparatif
    }

    @Override
    public IPeriodePCAccordee calculWithoutPersist(Droit droit, Collection<String> listeIdPersonnes,
                                                   DonneesHorsDroitsProvider containerGlobal, String dateDebutPeriodeForce) throws CalculException,
            JadePersistenceException, JadeApplicationException {

        List<PeriodePCAccordee> list = this.calculDroitPourComparaison(droit, listeIdPersonnes, containerGlobal, false,
                false, dateDebutPeriodeForce);

        return list.get(list.size() - 1);
    }

    private void checkDateFinCalcul(String dateFinCalcul) throws CalculBusinessException, PmtMensuelException,
            JadeApplicationServiceNotAvailableException {
        if (JadeDateUtil.isDateAfter("01." + dateFinCalcul, "01."
                + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {
            throw new CalculBusinessException("pegasus.calcul.periodes.datefin.integrity.after.dateprochainpaiement");
        }
    }

    @Override
    public List<CalculPcaReplace> genrateIfNeededCopie(String idVersionDroit) throws JadePersistenceException,
            JadeApplicationException {
        List<CalculPcaReplace> pcaCopie = genrateIfNeededCopieWithOutPersist(idVersionDroit);
        for (CalculPcaReplace pca : pcaCopie) {
            PegasusImplServiceLocator.getCalculPersistanceService().sauvePCAccordeeToCopie(pca);
        }
        return pcaCopie;
    }

    @Override
    public List<CalculPcaReplace> genrateIfNeededCopieWithOutPersist(String idVersionDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Checkers.checkNotNull(idVersionDroit, "idVersionDroit");
        if (JadeStringUtil.isBlankOrZero(idVersionDroit)) {
            throw new RuntimeException("The idVersionDroit is empty");
        }

        SimplePCAccordeeSearch simplePCAccordeeSearch = new SimplePCAccordeeSearch();
        simplePCAccordeeSearch.setForIdVersionDroit(idVersionDroit);
        List<SimplePCAccordee> pcas = PersistenceUtil.search(simplePCAccordeeSearch);

        // récupère anciennes pc accordées
        Droit droit = PegasusServiceLocator.getDroitService().readDroitFromVersion(idVersionDroit);
        CalculPcaReplaceSearch pcaReplaced = searchPcaRepleaced(pcas, droit);

        CopiesGenerate copiesGenerate = new CopiesGenerate(PegasusImplServiceLocator.getCalculPersistanceService());

        List<CalculPcaReplace> pcaCopie = new ArrayList<CalculPcaReplace>();

        List<String> dates = new ArrayList<String>();

        for (SimplePCAccordee pca : pcas) {
            dates.add(pca.getDateDebut());
        }

        for (SimplePCAccordee pca : pcas) {
            List<CalculPcaReplace> pcaTemp = copiesGenerate.generate(pcaReplaced, "01." + pca.getDateDebut(),
                    droit.getSimpleVersionDroit());
            if (!pcaTemp.isEmpty()) {
                // On s'assure de que la copie n'a pas déja été créer
                for (CalculPcaReplace pca1 : pcaTemp) {
                    if (!dates.contains(pca1.getSimplePCAccordee().getDateDebut())) {
                        pcaCopie.add(pca1);
                    }
                }
                dates.add(pcaTemp.get(0).getSimplePCAccordee().getDateDebut());
            }
        }

        return pcaCopie;
    }

    @Override
    public List<CalculPcaReplace> genrateIfNeededDeleteWithOutPersist(String idVersionDroit)
            throws JadePersistenceException, JadeApplicationException {

        Checkers.checkNotNull(idVersionDroit, "idVersionDroit");
        if (JadeStringUtil.isBlankOrZero(idVersionDroit)) {
            throw new RuntimeException("The idVersionDroit is empty");
        }

        SimplePCAccordeeSearch simplePCAccordeeSearch = new SimplePCAccordeeSearch();
        simplePCAccordeeSearch.setForIdVersionDroit(idVersionDroit);
        List<SimplePCAccordee> pcas = PersistenceUtil.search(simplePCAccordeeSearch);

        Droit droit = PegasusServiceLocator.getDroitService().readDroitFromVersion(idVersionDroit);

        // récupère anciennes pc accordées
        CalculPcaReplaceSearch pcaReplaced = searchPcaRepleaced(pcas, droit);

        GeneratePcaToDelete generatePcaToDelete = new GeneratePcaToDelete(pcas, pcaReplaced);
        List<CalculPcaReplace> pcaDelete = generatePcaToDelete.generate();

        List<String> keys = new ArrayList<String>();

        for (SimplePCAccordee pca : pcas) {
            keys.add(pca.getIdEntity() + "_" + pca.getIsSupprime());
            List<CalculPcaReplace> pcasDeleted = generatePcaToDelete.generate();
            // On s'assure de que la copie n'a pas déja été créer
            for (CalculPcaReplace pcaDeleted : pcasDeleted) {
                if (!keys.contains(pcaDeleted.getSimplePCAccordee().getIdEntity() + "_"
                        + pcaDeleted.getSimplePCAccordee().getIsSupprime())) {
                    PegasusImplServiceLocator.getCalculPersistanceService().sauvePCAccordeeToCopie(pcaDeleted);
                    pcaDelete.add(pcaDeleted);
                }
            }
        }

        return pcaDelete;
    }

    public List<CalculPcaReplace> genrateIfNeededDelete(String idVersionDroit) throws JadePersistenceException,
            JadeApplicationException {
        List<CalculPcaReplace> pcasDeleted = genrateIfNeededDeleteWithOutPersist(idVersionDroit);
        for (CalculPcaReplace pca1 : pcasDeleted) {
            PegasusImplServiceLocator.getCalculPersistanceService().sauvePCAccordeeToCopie(pca1);
        }
        return pcasDeleted;
    }

    private void persistCalculForComparaison(Droit droit, boolean retroactif, List<PeriodePCAccordee> listePCAccordes,
                                             Map<String, JadeAbstractSearchModel> cacheDonneesBD) throws PCAccordeeException, DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        calculeDroitPersistence(droit, retroactif, listePCAccordes, cacheDonneesBD);
    }

    @Override
    public void reinitialiseDroit(Droit droit) throws Exception {
        if (!IPCDroits.CS_AU_CALCUL.equals(droit.getSimpleVersionDroit().getCsEtatDroit())) {
            PegasusImplServiceLocator.getCalculPersistanceService().clearPCAccordee(droit);
            droit.getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_AU_CALCUL);
            PegasusServiceLocator.getDroitService().updateDroit(droit);
            JadeThread.commitSession();
        }
    }

    @Override
    public CalculMoisSuivantSearch searchDonneesFinancieresForVersionDroit(Droit droit) throws CalculException,
            JadePersistenceException {

        if (droit == null) {
            throw new CalculException("Unable to search for CalculMoisSuivantSerach, the droit model passed is null!!");
        }

        CalculMoisSuivantSearch searchMoisSuivantDF = new CalculMoisSuivantSearch();
        searchMoisSuivantDF.setForIdDroit(droit.getSimpleDroit().getId());
        searchMoisSuivantDF.setForIdVersionDroit(droit.getSimpleVersionDroit().getIdVersionDroit());
        searchMoisSuivantDF.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return (CalculMoisSuivantSearch) JadePersistenceManager.search(searchMoisSuivantDF);
    }

    private CalculPcaReplaceSearch searchPcaRepleaced(List<SimplePCAccordee> pcas, Droit droit)
            throws JadePersistenceException {
        GroupePeriodes groupePeriodes = GroupePeriodesResolver.genearateListPeriode(pcas,
                new EachPeriode<SimplePCAccordee>() {
                    @Override
                    public String[] dateDebutFin(SimplePCAccordee t) {
                        return new String[]{t.getDateDebut(), t.getDateFin()};
                    }
                });

        CalculPcaReplaceSearch search = new CalculPcaReplaceSearch();
        search.setForIdDroit(droit.getSimpleDroit().getIdDroit());
        search.setForNoVersion(droit.getSimpleVersionDroit().getNoVersion());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setOrderKey(CalculPcaReplaceSearch.ORDER_BY_DATE_DEBUT);
        search.setForDateFin(groupePeriodes.getDateDebutMin());

        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey(CalculPcaReplaceSearch.WITH_NO_VERSION_DROIT_LESS);
        search = (CalculPcaReplaceSearch) JadePersistenceManager.search(search);

        return search;
    }
}
