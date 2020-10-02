package ch.globaz.pegasus.businessimpl.services.models.calcul;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.calcul.*;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.SimpleCreancier;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.calcul.CalculDroitService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.calcul.donneeInterne.DonneeInterneHomeVersement;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif;
import ch.globaz.pegasus.businessimpl.utils.calcul.IPeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PersonnePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.utils.periode.GroupePeriodesResolver;
import ch.globaz.utils.periode.GroupePeriodesResolver.EachPeriode;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;

/**
 * @author ECO
 */
public class CalculDroitServiceImpl extends PegasusAbstractServiceImpl implements CalculDroitService {

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
        if(listePCAccordes == null || listePCAccordes.isEmpty()){
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
                    isCCTrouve = listeEquals;
                    // Si retenus
                    if (isCCTrouve) {
                        calculsComparatifsComparaison.add(cc);
                        cc.setComparer(true);
                    }
                }

                if (calculsComparatifsComparaison.size() == 1) {
                    // 1 seul cas avant ou aprés réforme pas de comparaison sur le montant
                    CalculComparatif ccTrouve = calculsComparatifsComparaison.get(0);
                    ccDefautByComparaison = ccTrouve.copyCC();
                    ccTrouve.setPlanRetenu(true);
                } else if(calculsComparatifsComparaison.size() > 1) {
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
                if(isRefusFortune(pcAccordesReforme)) {
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
            if(JadeDateUtil.isDateBefore(JadeDateUtil.getGlobazFormattedDate(dateDebut),dateReforme)
                    || isRefusFortune(pcAccordesReforme)) {
                // periode de cette pca avant la réforme à ne pas comparer ou refus seuil de fortune sur le calcul réforme
                pcAccordes.determineCCFavorable();
                continue;
            }

            for(CalculComparatif calculComparatifs : pcAccordes.getCCRetenu()) {
                if(calculComparatifs!= null) {
                    calculComparatifs.setPlanRetenu(false);
                }
            }
            for(CalculComparatif calculComparatifsReforme : pcAccordesReforme.getCCRetenu()) {
                if(calculComparatifsReforme!= null) {
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
                                         Map<String, JadeAbstractSearchModel> cacheDonneesBD) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException,
            DroitException {
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
        // sauve les resultats en BD
        for (PeriodePCAccordee pcAccordee : listePCAccordes) {
            pcAccordee.setCalculRetro(retroactif);
            pcas.addAll(PegasusImplServiceLocator.getCalculPersistanceService().sauvePCAccordee(droit, pcAccordee,
                    pcaReplaced));
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
            mappingHomes(homeVersementList, homeReplaced, allNewPca);
        }
        boolean isVersementHome = false;
        if (homeVersementList != null && homeVersementList.size() > 0) {
            isVersementHome = true;
        }


        for (SimplePCAccordee simplePCAccordee : allNewPca) {
            updateOldRetenuWithDateEnd(pcaReplaced, simplePCAccordee, isVersementHome);
        }
        createRetenusForVersementsHome(homeVersementList);

        createCreancierForVersementsHome(homeVersementList);

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

    private void createCreancierForVersementsHome(List<DonneeInterneHomeVersement> homeVersementList) throws JadeApplicationServiceNotAvailableException, PmtMensuelException, CreancierException, JadePersistenceException {
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
                int nbreJours = JadeDateUtil.getNbDaysBetween(dateDebut, dateFin);
                Float montantTotalCreancier = nbreJours * Float.parseFloat(calculDonneesHome.getMontantPrixChambre());
                Creancier creancier = new Creancier();
                SimpleCreancier simpleCreancier = new SimpleCreancier();
                simpleCreancier.setCsEtat(IPCDroits.CS_ETAT_CREANCE_A_PAYE);
                simpleCreancier.setCsTypeCreance(IPCCreancier.CS_TYPE_CREANCE_TIERS);
                simpleCreancier.setIdDemande(calculDonneesHome.getIdDemande());
                simpleCreancier.setIdDomaineApplicatif(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                simpleCreancier.setIdTiers(calculDonneesHome.getIdTiersHome());
                simpleCreancier.setIdTiersAdressePaiement(calculDonneesHome.getIdTiersHome());
                simpleCreancier.setIdTiersRegroupement(calculDonneesHome.getIdTiersRegroupement());
                simpleCreancier.setMontant(montantTotalCreancier.toString());
                creancier.setSimpleCreancier(simpleCreancier);
                creancier = PegasusServiceLocator.getCreancierService().create(creancier);
                CreanceAccordee creanceAccordee = new CreanceAccordee();
                SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();
                simpleCreanceAccordee.setIdCreancier(creancier.getId());
                simpleCreanceAccordee.setIdPCAccordee(calculDonneesHome.getIdPca());
                Float montantMensuel = Float.parseFloat(calculDonneesHome.getMontantMensuel());
                if(montantMensuel<montantTotalCreancier){
                    simpleCreanceAccordee.setMontant(montantMensuel.toString());
                }else{
                    simpleCreanceAccordee.setMontant(montantTotalCreancier.toString());
                }
                creanceAccordee.setSimpleCreanceAccordee(simpleCreanceAccordee);
                PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);
            }
        }
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
                //TODO: Ajouter le vrai montant après calcul
                retenue.setMontantRetenuMensuel("100.00");
                retenue.setMontantTotalARetenir("999'999.00");
                pcaRetenue.setSimpleRetenue(retenue);
                PegasusServiceLocator.getRetenueService().createWithOutCheck(pcaRetenue);
            }
        }
    }

    private void mappingHomes(List<DonneeInterneHomeVersement> homeVersementList, CalculDonneesHomeSearch listHomes, List<SimplePCAccordee> allNewPca) throws JadeApplicationServiceNotAvailableException, PmtMensuelException {


        for (JadeAbstractModel model : listHomes.getSearchResults()) {
            CalculDonneesHome home = (CalculDonneesHome) model;
            if (home.getIsVersementDirect()) {
                DonneeInterneHomeVersement donnee;
                for (SimplePCAccordee simplePCAccordee : allNewPca) {
                    if (simplePCAccordee.getCsRoleBeneficiaire().equals(home.getCsRoleFamille())
                            && simplePCAccordee.getDateDebut().equals(home.getDateDebutDFH())) {
                        Map<Periode,String> mapPeriode = createPeriodeVersementHome(home.getDateDebutDFH(),home.getDateFinDFH());
                        for(Map.Entry<Periode,String> entry  :mapPeriode.entrySet()){
                            String csTypeVersement = entry.getValue();
                            Periode periode = entry.getKey();
                            donnee = new DonneeInterneHomeVersement();
                            donnee.setIdRenteAccordee(simplePCAccordee.getIdPrestationAccordee());
                            donnee.setIdPca(simplePCAccordee.getIdPCAccordee());
                            donnee.setIdAdressePaiement(home.getIdAdressePaiement());
                            donnee.setIdTiersHome(home.getIdTiersHome());
                            donnee.setMontantPrixChambre(home.getPrixJournalier());
                            donnee.setIdDemande(home.getIdDemande());
                            donnee.setIdTiersHome(home.getIdTiersHome());
                            donnee.setIdTiersAdressePaiement(home.getIdTiersHome());
                            donnee.setIdTiersRegroupement(home.getIdTiersRegroupement());
                            donnee.setMontantMensuel(simplePCAccordee.getMontantMensuel());
                            if (JadeStringUtil.isBlankOrZero(simplePCAccordee.getIdPrestationAccordeeConjoint())) {
                                donnee.setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                            } else {
                                donnee.setCsRoleBeneficiaire(simplePCAccordee.getCsRoleBeneficiaire());
                            }
                            donnee.setCsTypeVersement(csTypeVersement);
                            donnee.setDateDebut(periode.getDateDebut());
                            donnee.setDateFin(periode.getDateFin());
                            homeVersementList.add(donnee);
                        }
                    }
                }
            }

        }


    }

    private Map<Periode, String> createPeriodeVersementHome(String dateDebutDFH, String dateFinDFH) throws JadeApplicationServiceNotAvailableException, PmtMensuelException {
        Map<Periode,String> map = new HashMap<>();
        Periode periode;

        /**
         *  CAS 1 : Entrée en home à partir du mois du prochain paiement
         */
        if (isDateAfterOrEquals(dateDebutDFH, PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt())) {
            periode = new Periode(dateDebutDFH,dateFinDFH);
            map.put(periode,DonneeInterneHomeVersement.TYPE_RETENUS);
        } else {
            /**
             *  CAS 2 : Entrée en home avant le mois du prochain paiement et termine après ce même mois.
             */
            if(JadeStringUtil.isBlankOrZero(dateFinDFH) || (!JadeStringUtil.isBlankOrZero(dateFinDFH) && isDateAfterOrEquals(dateFinDFH, PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt()))){
                periode = new Periode(dateDebutDFH,PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
                map.put(periode,DonneeInterneHomeVersement.TYPE_CREANCIER);
                periode = new Periode( PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt(),dateFinDFH);
                map.put(periode,DonneeInterneHomeVersement.TYPE_RETENUS);
            }else{
                /**
                 *  CAS 3 : Entrée en home avant le mois du prochain paiement et termine avant ce même mois.
                 */
                periode = new Periode(dateDebutDFH,dateFinDFH);
                map.put(periode,DonneeInterneHomeVersement.TYPE_CREANCIER);
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

    private void updateOldRetenuWithDateEnd(CalculPcaReplaceSearch anciennesPCAccordees, SimplePCAccordee simplePCAccordee, Boolean isVersementHome) throws JadeApplicationException, JadePersistenceException {
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
                    if (anciennePCACourante.getSimplePrestationsAccordees().getIsRetenues()) {
                        idPcaOld = ancienneDonnee.getSimplePCAccordee().getIdPCAccordee();
                        PcaRetenueSearch search = new PcaRetenueSearch();
                        search.setForIdPca(idPcaOld);
                        search = PegasusServiceLocator.getRetenueService().search(search);
                        for (JadeAbstractModel model : search.getSearchResults()) {
                            PcaRetenue retenue = (PcaRetenue) model;
                            /**
                             * CAS 1 : Si il y'a un versement home, alors on cloture tout les retenus de l'ancien PCA
                             */
                            if (isVersementHome && JadeStringUtil.isBlankOrZero(retenue.getSimpleRetenue().getDateFinRetenue())) {
                                retenue.getSimpleRetenue().setDateFinRetenue(dateProchainPaiement);
                                PegasusServiceLocator.getRetenueService().update(retenue);
                            }
                            /**
                             * CAS 2 : Si il y'a pas de versement home, alors on remet comme avant les retenus de l'ancien PCA si il a été ajouté lors d'une calcul.
                             */
                            if (!isVersementHome && retenue.getSimpleRetenue().getDateFinRetenue().equals(dateProchainPaiement)) {
                                retenue.getSimpleRetenue().setDateFinRetenue("");
                                PegasusServiceLocator.getRetenueService().update(retenue);
                            }

                        }
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
