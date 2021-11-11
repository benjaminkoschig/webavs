package ch.globaz.pegasus.businessimpl.services.models.calcul;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
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
import ch.globaz.pegasus.business.models.creancier.*;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.calcul.CalculDroitService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.calcul.donneeInterne.DonneeInterneHomeVersement;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPlanCalculReforme;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static globaz.externe.IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;

/**
 * @author ECO
 */
public class CalculDroitServiceImpl extends PegasusAbstractServiceImpl implements CalculDroitService {
    private final String REQUERANT_HOME = "REQUERANT_HOME";
    private final String CONJOINT_HOME = "CONJOINT_HOME";
    private final String REQUERANT_DEP_PERS = "REQUERANT_DEP_PERS";
    private final String CONJOINT_DEP_PERS = "CONJOINT_DEP_PERS";
    //Variable pour savoir si il faut reporter les créances/retenus des versement en home pour les cas où les données en home ne sont pas touché et ignoré dans le calculteur.
    private boolean aDonneeSejourHomeEncoreValide = false;
    private Map<String, SimplePCAccordee> mapCsRoleToPCAWithoutDateFin = new LinkedHashMap<>();
    //LABEL
    private final String LOG_ERROR_MESSAGE_SEUIL_FORTUNE_DEPASSE = "pegasus.calcul.seuil.fortune.depasse";

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
            DonneesHorsDroitsProvider containerGlobal = DonneesHorsDroitsProvider.getInstance();
            containerGlobal.init();

            String dateSplitReforme = null;
            String version = droit.getSimpleVersionDroit().getNoVersion();
            // sur un calcul rétro : vérifie s'il n'y avait une période uniquement réforme
            boolean retroactifCalcul = retroactif;
            if (retroactif && !JadeStringUtil.isBlankOrZero(version) && Integer.parseInt(version) > 1) {
                List<PCAccordeePlanCalculReforme> list = PcaPlanCalculReforme.getListPcaFromNoVersion(droit.getId(), version);
                // Si la date de début est supérieure à la date de début de la dernière période, il faudra vérifier le statut réforme sur l'ancien droit
                if (list.isEmpty() || JadeDateUtil.isDateAfter(dateDebutPlageCalcul, JadeDateUtil.getFirstDateOfMonth(list.get(list.size() - 1).getDateDebut()))) {
                    retroactifCalcul = false;
                } else {
                    dateSplitReforme = PcaPlanCalculReforme.getSplitDateReformeFromVersion(list);
                }
            }
            // si la date de la plage est nulle, c'est qu'il n'y a pas de rente
            // et pas de calcul à faire
            if (dateDebutPlageCalcul != null) {

                // récupère données de persistence et le garde en cache mémoire
                Map<String, JadeAbstractSearchModel> cacheDonneesBD = PegasusImplServiceLocator.getPeriodesService()
                        .getDonneesCalculDroit(droit, dateDebutPlageCalcul, dateFinPlageCalcul);

                List<PeriodePCAccordee> listePCAccordes = calculeDroitPartagePeriodes(droit, dateDebutPlageCalcul,
                        dateFinPlageCalcul, cacheDonneesBD, containerGlobal, isDateFinForce, dateSplitReforme);

                List<PeriodePCAccordee> listePCAccordesReforme = calculeDroitPartagePeriodes(droit, dateDebutPlageCalcul,
                        dateFinPlageCalcul, cacheDonneesBD, containerGlobal, isDateFinForce, dateSplitReforme);
                // procède au calcul à proprement dit
                calculeDroitTraitement(droit, cacheDonneesBD, listePCAccordes, listePCAccordesReforme, dateDebutPlageCalcul,
                        dateFinPlageCalcul, containerGlobal, retroactifCalcul, dateSplitReforme);

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
            throw new CalculException("Service not available - Calculator - " + e.getMessage());
        }

        return droit;

    }

    private void calculFinalise(Droit droit, boolean retroactif, String dateDebutPlageCalcul, DonneesHorsDroitsProvider containerGlobal, Map<String, JadeAbstractSearchModel> cacheDonneesBD, List<PeriodePCAccordee> listePCAccordes) throws JadePersistenceException, JadeApplicationException {

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
                        cacheDonneesBD, containerGlobal, false, null);

                listePCAccordesReforme = calculeDroitPartagePeriodes(droit, dateDebutPlageCalcul, dateFinPlageCalcul,
                        cacheDonneesBD, containerGlobal, false, null);

                if ((listePCAccordes.size() > 2) || (listePCAccordes.size() < 1)) {
                    throw new CalculException("Expected 1 or 2 PC Accordee!");
                }

                // procède au calcul à proprement dit
                calculeDroitTraitement(droit, cacheDonneesBD, listePCAccordes, listePCAccordesReforme, dateDebutPlageCalcul,
                        dateFinPlageCalcul, containerGlobal, retroactif, null);

                dertermineFavorableCombinaisonPersonne(listeIdPersonnes, listePCAccordes);

                // calcul des éventuels jours d'appoint
                PegasusImplServiceLocator.getCalculComparatifService().calculJoursAppoint(
                        listePCAccordes,
                        (CalculPcaReplaceSearch) cacheDonneesBD
                                .get(ConstantesCalcul.CONTAINER_DONNEES_PCACCORDEES_REPLACED));

                // récupère anciennes pc accordées
                PegasusImplServiceLocator.getCalculPersistanceService().recupereAnciensPCAccordee(dateDebutPlageCalcul,
                        droit, cacheDonneesBD);

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

            List<CalculComparatif> calculsComparatifsComparaison = new ArrayList<>();

            // Si il n'y a pas de calcul comparatif pour le conjoint, CAS COUPLE SEPARE PAR LA MALADIE
            if (periode.getCalculsComparatifsConjoint().size() == 0) {
                for (CalculComparatif cc : calculsComparatifs) {
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
            // pas de fusion seul les calculs avant réforme comptent
            return;
        } else if (listePCAccordes.isEmpty()) {
            // pas de fusion : on remplace la liste ancienne avec les données de la liste calcul réforme
            // flag les calculs en réforme
            for (PeriodePCAccordee pcAccordesReforme : listePCAccordesReforme) {
                getCalculReforme(pcAccordesReforme.getCalculsComparatifs());
                if (!pcAccordesReforme.getCalculsComparatifs().isEmpty() && isRefusFortune(pcAccordesReforme)) {
                    throw new CalculException(LOG_ERROR_MESSAGE_SEUIL_FORTUNE_DEPASSE, pcAccordesReforme.getCalculsComparatifs().get(0).getFortune());
                }
            }
            listePCAccordes.addAll(listePCAccordesReforme);
            return;
        }

        if (listePCAccordes.size() != listePCAccordesReforme.size()) {
            throw new CalculException("Les nombres de PCA avant réforme et après réforme ne sont pas identique !");
        }

        boolean isReforme = false;

        for (int i = 0; i < listePCAccordes.size(); i++) {
            PeriodePCAccordee pcAccordes = listePCAccordes.get(i);
            PeriodePCAccordee pcAccordesReforme = listePCAccordesReforme.get(i);
            Date dateDebut = pcAccordesReforme.getDateDebut();
            if (isReforme && isRefusFortune(pcAccordesReforme)) {
                throw new CalculException(LOG_ERROR_MESSAGE_SEUIL_FORTUNE_DEPASSE, pcAccordesReforme.getCalculsComparatifs().get(0).getFortune());
            } else if ((isReforme || pcAccordes.isNePasCalculer()) && !isRefusFortune(pcAccordesReforme)) {
                // la période précédente a déterminée qu'un calcul réforme était plus favorable : tous les suivants sont uniquement réforme
                // ou sur un calcul rétro : une ancienne version de droit était déjà un calcul uniquement réforme
                keepReformeOnly(pcAccordes, pcAccordesReforme);
                isReforme = true;
            } else if (JadeDateUtil.isDateBefore(JadeDateUtil.getGlobazFormattedDate(dateDebut), dateReforme)
                    || isRefusFortune(pcAccordesReforme)) {
                // Contrôle sur calcul comparatif lorsque qu'il y a depassement de fortune
                if (pcAccordes.getCalculsComparatifs().size() == 0) {
                    throw new CalculException(LOG_ERROR_MESSAGE_SEUIL_FORTUNE_DEPASSE, pcAccordesReforme.getCalculsComparatifs().get(0).getFortune());
                }
                // periode de cette pca avant la réforme à ne pas comparer ou refus seuil de fortune sur le calcul réforme
                pcAccordes.determineCCFavorable();
            } else {
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
                isReforme = pcAccordes.determineCCFavorable();
            }
        }

    }

    /**
     * remplace les calculs d'une pca par ceux de la pca réforme
     *
     * @param pcAccordes
     * @param pcAccordesReforme
     * @throws CalculException
     */
    private void keepReformeOnly(PeriodePCAccordee pcAccordes, PeriodePCAccordee pcAccordesReforme) throws CalculException {
        pcAccordes.getCalculsComparatifs().clear();
        pcAccordes.getCalculsComparatifsConjoint().clear();
        pcAccordes.getCalculsComparatifs().addAll(getCalculReforme(pcAccordesReforme.getCalculsComparatifs()));
        pcAccordes.getCalculsComparatifsConjoint().addAll(getCalculReforme(pcAccordesReforme.getCalculsComparatifsConjoint()));
        pcAccordes.getPersonnes().putAll(pcAccordesReforme.getPersonnes());
        pcAccordes.setTypeSeparationCC(pcAccordesReforme.getTypeSeparationCC());
        pcAccordes.determineCCFavorable();
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
                                                                DonneesHorsDroitsProvider containerGlobal, boolean isDateFinForce, String dateSplitReforme) throws CalculException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        List<PeriodePCAccordee> listePCAccordes;

        listePCAccordes = PegasusImplServiceLocator.getPeriodesService().recherchePeriodesCalcul(droit,
                dateDebutPlageCalcul, dateFinPlageCalcul, cacheDonneesBD, containerGlobal, isDateFinForce, dateSplitReforme);
        return listePCAccordes;
    }

    private void calculeDroitPersistence(Droit droit, boolean retroactif, List<PeriodePCAccordee> listePCAccordes,
                                         Map<String, JadeAbstractSearchModel> cacheDonneesBD) throws JadePersistenceException, JadeApplicationException {
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

        List<SimplePCAccordee> allNewPca = new ArrayList<>();
        List<PCAccordeePlanCalcul> pcas = new ArrayList<>();
        List<DonneeInterneHomeVersement> homeVersementList = new LinkedList<>();
        List<DonneeInterneHomeVersement> sejourVersementList = new LinkedList<>();
        Map<String, String> mapMontantTotalHome = new LinkedHashMap<>();
        // sauve les resultats en BD

        CalculDonneesHomeSearch homeReplaced = (CalculDonneesHomeSearch) cacheDonneesBD
                .get(ConstantesCalcul.CONTAINER_DONNEES_HOMES_FOR_VERSEMENT_DIRECT);
/**
 * Regrouper les montants et infos par assurés en cas de séparation de maladie car on  a pas l'info pour le suite des opérations.
 *
 */

        for (PeriodePCAccordee pcAccordee : listePCAccordes) {
            pcAccordee.setCalculRetro(retroactif);
            pcas.addAll(PegasusImplServiceLocator.getCalculPersistanceService().sauvePCAccordee(droit, pcAccordee,
                    pcaReplaced));
            for (CalculComparatif cc : pcAccordee.getCalculsComparatifs()) {
                if (cc.getIsPlanretenu()) {
                    checkSejourMoisPartiel(droit, pcAccordee, cc, sejourVersementList);
                    String montantTotalHome = cc.getMontantPrixHome();
                    Float montantDepensePersonnel = cc.getMontants().getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL);
                    String fKey = pcAccordee.getIdSimplePcAccordee() + "-";
                    for (PersonnePCAccordee personnePCAccordee : cc.getPersonnes()) {
                        if (personnePCAccordee.getIsHome() && !JadeStringUtil.isBlankOrZero(montantTotalHome)) {
                            mapMontantTotalHome.put(fKey + REQUERANT_HOME, montantTotalHome);
                            mapMontantTotalHome.put(fKey + REQUERANT_DEP_PERS, montantDepensePersonnel.toString());
                            aDonneeSejourHomeEncoreValide = true;
                        }
                    }
                }
            }
            for (CalculComparatif cc : pcAccordee.getCalculsComparatifsConjoint()) {
                if (cc.getIsPlanretenu()) {
                    checkSejourMoisPartiel(droit, pcAccordee, cc, sejourVersementList);
                    String montantTotalHome = cc.getMontantPrixHome();
                    Float montantDepensePersonnel = cc.getMontants().getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL);
                    String fKey = pcAccordee.getIdSimplePcAccordeeConjoint() + "-";
                    for (PersonnePCAccordee personnePCAccordee : cc.getPersonnes()) {
                        if (personnePCAccordee.getIsHome() && !JadeStringUtil.isBlankOrZero(montantTotalHome)) {
                            mapMontantTotalHome.put(fKey + CONJOINT_HOME, montantTotalHome);
                            mapMontantTotalHome.put(fKey + CONJOINT_DEP_PERS, montantDepensePersonnel.toString());
                            aDonneeSejourHomeEncoreValide = true;
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
        /**
         * Gestion des versement en home (futur,retenus)
         * 1 ) Filtrer les homes
         * 2 ) Mapper les donneés des homes pour la création des créances et retenues.
         */
        if (homeReplaced != null) {
            homeReplaced = filterHome(homeReplaced, droit.getSimpleVersionDroit().getIdVersionDroit());
            if (homeReplaced != null) {
                mappingHomes(homeVersementList, homeReplaced, allNewPca, mapMontantTotalHome, droit);
            }
        }

        createCreancierHystoriqueOld(pcaReplaced);
        /**
         * Mapper requérant conjoint pour les PCA sans date de fin (facilite le report des retenus anciennes et valides)
         */
        mapPCASansDateFin(allNewPca);
        /**
         * Revert les anciennes retenus pour les cas quand on recalcul car on a mis fin aux anciennes retenus
         */
        revertRetenuesOldPca(droit);
        for (SimplePCAccordee simplePCAccordee : allNewPca) {
            reporterLaRetenuSiExistant(pcaReplaced, simplePCAccordee, homeVersementList);
            clotrerAncienneRetenus(pcaReplaced, simplePCAccordee, copies);
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
        for (SimplePCAccordee simplePCAccordee : allNewPca) {
            createCreancierHystoriqueNew(simplePCAccordee);
        }

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

    private void mapPCASansDateFin(List<SimplePCAccordee> allNewPca) {
        for (SimplePCAccordee pca : allNewPca) {
            if (JadeStringUtil.isBlankOrZero(pca.getDateFin())) {
                mapCsRoleToPCAWithoutDateFin.put(pca.getCsRoleBeneficiaire(), pca);
            }
        }
    }

    private void revertRetenuesOldPca(Droit droit) throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PcaRetenueSearch pcaRetenueSearch = new PcaRetenueSearch();
        pcaRetenueSearch.setForIdDroit(droit.getId());
        int noVersion = Integer.parseInt(droit.getSimpleVersionDroit().getNoVersion());
        if (noVersion > 1) {
            noVersion--;
        }
        pcaRetenueSearch.setForNoVersion(String.valueOf(noVersion));
        pcaRetenueSearch = PegasusServiceLocator.getRetenueService().search(pcaRetenueSearch);
        String dateProchainPaiement = null;
        try {
            dateProchainPaiement = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            for (JadeAbstractModel absDonnee : pcaRetenueSearch.getSearchResults()) {
                PcaRetenue retenueAncienne = (PcaRetenue) absDonnee;
                if (retenueAncienne.getSimpleRetenue().getDateFinRetenue().equals(dateProchainPaiement)) {
                    retenueAncienne.getSimpleRetenue().setDateFinRetenue("");
                    PegasusServiceLocator.getRetenueService().update(retenueAncienne);
                    SimplePrestationsAccordees simplePrestationsAccordees = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().read(retenueAncienne.getSimpleRetenue().getIdRenteAccordee());
                    simplePrestationsAccordees.setIsRetenues(Boolean.TRUE);
                    PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(simplePrestationsAccordees);
                }
            }
        } catch (PmtMensuelException e) {
            throw new DroitException("Unable to delete PCAccordee", e);
        } catch (JadeApplicationException e) {
            throw new DroitException("Unable to delete PCAccordee", e);
        }
    }

    /**
     * Filtrer les homes si on continue ou pas de versement au home (coche/décoche ou est home ou pas)
     *
     * @param homeReplaced
     * @param idVersionDroitNewDroit
     * @return
     */

    private CalculDonneesHomeSearch filterHome(CalculDonneesHomeSearch homeReplaced, String idVersionDroitNewDroit) {
        JadeAbstractModel[] array;
        Map<String, List<JadeAbstractModel>> mapNewDroitHome = new LinkedHashMap<>();
        Map<String, List<JadeAbstractModel>> mapOldDroitHome = new LinkedHashMap<>();
        List<JadeAbstractModel> list = new LinkedList<>();
        List<JadeAbstractModel> listTemp = new LinkedList<>();

        /**
         * Trier par Requérant et conjoint dans les list d'anciens et nouvelles données
         */
        for (JadeAbstractModel model : homeReplaced.getSearchResults()) {
            CalculDonneesHome home = (CalculDonneesHome) model;
            if (home.getIdVersionDroit().equals(idVersionDroitNewDroit)) {
                if (mapNewDroitHome.containsKey(home.getIdTiersHome() + home.getCsRoleFamille())) {
                    listTemp = mapNewDroitHome.get(home.getIdTiersHome() + home.getCsRoleFamille());
                } else {
                    listTemp = new LinkedList<>();
                }
                listTemp.add(model);
                mapNewDroitHome.put(home.getIdTiersHome() + home.getCsRoleFamille(), listTemp);
            } else {
                if (mapOldDroitHome.containsKey(home.getIdTiersHome() + home.getCsRoleFamille())) {
                    listTemp = mapOldDroitHome.get(home.getIdTiersHome() + home.getCsRoleFamille());
                } else {
                    listTemp = new LinkedList<>();
                }
                listTemp.add(model);
                mapOldDroitHome.put(home.getIdTiersHome() + home.getCsRoleFamille(), listTemp);
            }
        }
        /**
         * On vérifie si il y'a un changement de donnée du même home entre l'ancien droit et le nouveau droit
         * + Vérifier aussi il existe de séjour mois partiel valide qu'il faut ajouter dans la listge
         */

        for (Map.Entry<String, List<JadeAbstractModel>> entry : mapOldDroitHome.entrySet()) {
            if (mapNewDroitHome.containsKey(entry.getKey())) {
                List<JadeAbstractModel> listHomeNew = (List<JadeAbstractModel>)  mapNewDroitHome.get(entry.getKey());
                for (JadeAbstractModel model : listHomeNew) {
                    CalculDonneesHome homeNew = (CalculDonneesHome) model;
                    if (homeNew.getIsVersementDirect()) {
                        list.add(homeNew);
                    }
                }
                mapNewDroitHome.remove(entry.getKey());
            } else if (aDonneeSejourHomeEncoreValide) {
                list.addAll(entry.getValue());
            }
        }

        /**
         * Ajouter dans la liste des homes pour le système du paiement scindé uniquement si il y'a la coche.
         */

        for (List<JadeAbstractModel> listHomeRawData : mapNewDroitHome.values()) {
            for (JadeAbstractModel homeRawData : listHomeRawData) {
                CalculDonneesHome homeNew = (CalculDonneesHome) homeRawData;
                if (homeNew.getIsVersementDirect()) {
                    list.add(homeRawData);
                }
            }
        }
        if (!list.isEmpty()) {
            list = list.stream().filter(home -> ((CalculDonneesHome) home).getIsVersementDirect()).collect(Collectors.toList());
            array = new JadeAbstractModel[list.size()];
            list.toArray(array);
            homeReplaced.setSearchResults(array);
            return homeReplaced;
        } else {
            return null;
        }
    }

    private void checkSejourMoisPartiel(Droit droit, PeriodePCAccordee pcAccordee, CalculComparatif cc, List<DonneeInterneHomeVersement> sejourVersementList) throws JadeApplicationException, JadePersistenceException {
        if (!cc.getMontants().containsValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL)) {
            return;
        }

        TupleDonneeRapport tupleMoisPartiel = cc.getMontants().getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL);
        if (tupleMoisPartiel == null) {
            return;
        }

        Float montantSejourRequerant = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_MONTANT_REQUERANT);
        Float idHomeRequerant = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME_REQUERANT);
        Float versementDirectRequerant = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_VERSEMENT_DIRECT_REQUERANT);

        Float montantSejourConjoint = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_MONTANT_CONJOINT);
        Float idHomeConjoint = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_HOME_CONJOINT);
        Float versementDirectConjoint = tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_VERSEMENT_DIRECT_CONJOINT);
        if (isMontantNotZeroAndVersement(montantSejourRequerant, versementDirectRequerant)
                && isMontantNotZeroAndVersement(montantSejourConjoint, versementDirectConjoint)
                && Float.compare(idHomeRequerant, idHomeConjoint) == 0) {
            // Deux séjours mois partiels pour requérant et conjoint et même id home
            addSejourMontant(droit, pcAccordee, cc, sejourVersementList, montantSejourRequerant + montantSejourConjoint, tupleMoisPartiel
                    , idHomeRequerant, TupleDonneeRapport.readBoolean(versementDirectRequerant), TupleDonneeRapport.readBoolean(versementDirectConjoint));
        } else {
            if (isMontantNotZeroAndVersement(montantSejourRequerant, versementDirectRequerant)) {
                addSejourMontant(droit, pcAccordee, cc, sejourVersementList, montantSejourRequerant, tupleMoisPartiel
                        , idHomeRequerant, TupleDonneeRapport.readBoolean(versementDirectRequerant), TupleDonneeRapport.readBoolean(versementDirectConjoint));
            }
            if (isMontantNotZeroAndVersement(montantSejourConjoint, versementDirectConjoint)) {
                addSejourMontant(droit, pcAccordee, cc, sejourVersementList, montantSejourConjoint, tupleMoisPartiel
                        , idHomeConjoint, TupleDonneeRapport.readBoolean(versementDirectRequerant), TupleDonneeRapport.readBoolean(versementDirectConjoint));
            }
        }
    }

    private boolean isMontantNotZeroAndVersement(Float value, Float versementDirectRequerant) {
        return Float.compare(0.0f, value) != 0 && TupleDonneeRapport.readBoolean(versementDirectRequerant);
    }

    private void addSejourMontant(Droit droit, PeriodePCAccordee pcAccordee, CalculComparatif cc, List<DonneeInterneHomeVersement> sejourVersementList, Float montantSejour, TupleDonneeRapport tupleMoisPartiel, Float idHome, boolean versementRequerant, boolean versementConjoint) throws JadePersistenceException, JadeApplicationException {

        ListPCAccordee pca;

        if(versementRequerant && !versementConjoint){
            pca = PegasusImplServiceLocator.getPCAccordeeService().read(pcAccordee.getIdSimplePcAccordee());
        }else{
            if (!JadeStringUtil.isBlankOrZero(pcAccordee.getIdSimplePcAccordeeConjoint()) ) {
                pca = PegasusImplServiceLocator.getPCAccordeeService().read(pcAccordee.getIdSimplePcAccordeeConjoint());
            } else {
                pca = PegasusImplServiceLocator.getPCAccordeeService().read(pcAccordee.getIdSimplePcAccordee());
            }
        }

        Home complexeHome = PegasusImplServiceLocator.getHomeService().read(Float.toString(idHome));
        if (complexeHome.getSimpleHome().isNew()) {
            return;
        }
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
        Periode periode = new Periode(pcAccordee.getStrDateDebut(), pcAccordee.getStrDateFin());
        String montantDejaVerser = searchMontantDejaVerser(periode, droit.getSimpleDroit().getIdDroit(), droit.getSimpleVersionDroit().getNoVersion(), pca.getSimplePCAccordee().getCsRoleBeneficiaire());
        if (JadeStringUtil.isBlankOrZero(montantDejaVerser)) {
            donnee.setMontantDejaVerser("0.0");
        } else {
            donnee.setMontantDejaVerser(montantDejaVerser);
        }

        donnee.setCsTypeVersement(DonneeInterneHomeVersement.TYPE_CREANCIER);
        donnee.setDateDebut(pcAccordee.getStrDateDebut());
        donnee.setDateFin(pcAccordee.getStrDateFin());
        donnee.setDateDebutPCA(pca.getSimplePCAccordee().getDateDebut());
        donnee.setDateDebutPCA(pca.getSimplePCAccordee().getDateFin());

        sejourVersementList.add(donnee);

    }

    /**
     * Reporter les anciennes retenus au nouveau droit
     * 1 ) Si la retenue est encore valide (sans date de fin)
     * 2) Si la retenue a été généré automatiquement pour un versement en home et que il est encore valable => on reporte sinon on supprime car soit il y'a plus de versement direct soit plus de personnes en homes
     *
     * @param anciennesPCAccordees
     * @param simplePCAccordee
     * @param donneeInterneHomeVersements
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

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
                mapNewRetenues.put(donneeInterneHomeVersement.getIdTiersAdressePaiement() + "" + donneeInterneHomeVersement.getCsRoleBeneficiaire(), donneeInterneHomeVersement);
            }
        }
        if (JadeStringUtil.isBlankOrZero(simplePCAccordee.getDateFin())) {
            for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
                CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
                if (IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(ancienneDonnee.getSimplePCAccordee().getCsEtatPC())) {
                    idPcaOld = ancienneDonnee.getSimplePCAccordee().getIdPCAccordee();
                    PcaRetenueSearch search = new PcaRetenueSearch();
                    search.setForIdPca(idPcaOld);
                    search = PegasusServiceLocator.getRetenueService().search(search);
                    for (JadeAbstractModel model : search.getSearchResults()) {
                        PcaRetenue retenueAncienne = (PcaRetenue) model;
                        //Si c'est un home, on le map dans la liste des home.
                        if (isHome(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt())) {
                            mapOldHomeRetenues.put(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt() + "" + retenueAncienne.getCsRoleFamillePC(), retenueAncienne);
                        } else {
                            if (canReportRetenue(simplePCAccordee, retenueAncienne)) {
                                mapAutreRetenues.put(retenueAncienne.getSimpleRetenue().getIdTiersAdressePmt() + "" + retenueAncienne.getCsRoleFamillePC(), model);
                            }
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, PcaRetenue> pcaRetenueEntry : mapOldHomeRetenues.entrySet()) {
            if (mapNewRetenues.containsKey(pcaRetenueEntry.getKey())) {
                donneeInterneHomeVersementNew = mapNewRetenues.get(pcaRetenueEntry.getKey());
                Float montantAVerser = getMontantHome(donneeInterneHomeVersementNew.getMontantHomes(), 1);
                Float montantAVerserOld = Float.parseFloat(mapOldHomeRetenues.get(pcaRetenueEntry.getKey()).getSimpleRetenue().getMontantRetenuMensuel());
                if (montantAVerser.floatValue() == montantAVerserOld.floatValue() && donneeInterneHomeVersementNew.isVersementDirect()) {
                    PcaRetenue ancienneRetenue = mapOldHomeRetenues.get(pcaRetenueEntry.getKey());
                    PcaRetenue retenue;
                    try {
                        retenue = (PcaRetenue) JadePersistenceUtil.clone(ancienneRetenue);
                    } catch (JadeCloneModelException e) {
                        throw new PCAccordeeException("Unable to clone this PCA id: "
                                + donneeInterneHomeVersementNew.getIdPca());
                    }
                    retenue.setIdPCAccordee(donneeInterneHomeVersementNew.getIdPca());
                    String dateProchainPaiement =
                            PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
                    retenue.getSimpleRetenue().setDateDebutRetenue(dateProchainPaiement);
                    PegasusServiceLocator.getRetenueService().createWithOutCheck(retenue);
                    donneeInterneHomeVersements.remove(donneeInterneHomeVersementNew);
                }

            }
        }
        /**
         * Reporter les retenus hors ceux qui sont générés par le paiement scindé
         */
        for (Map.Entry<String, JadeAbstractModel> retenueAutre : mapAutreRetenues.entrySet()) {
            PcaRetenue retenue;
            try {
                retenue = (PcaRetenue) JadePersistenceUtil.clone(retenueAutre.getValue());
            } catch (JadeCloneModelException e) {
                throw new PCAccordeeException("Unable to clone this PCA id: "
                        + simplePCAccordee.getIdPCAccordee());
            }
            retenue.setIdPCAccordee(simplePCAccordee.getIdPCAccordee());
            String dateProchainePmt = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            if (isDateBeforeOrEquals(retenue.getSimpleRetenue().getDateDebutRetenue(), dateProchainePmt)) {
                retenue.getSimpleRetenue().setDateDebutRetenue(
                        PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());
            }
            PegasusServiceLocator.getRetenueService().createWithOutCheck(retenue);
        }

    }

    private boolean canReportRetenue(SimplePCAccordee simplePCAccordee, PcaRetenue retenueAncienne) throws JadeApplicationServiceNotAvailableException, PmtMensuelException {
        String dateFinRetenueAncienne = retenueAncienne.getSimpleRetenue().getDateFinRetenue();
        String dateProchainPaiementMensuel = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        if ((JadeStringUtil.isBlankOrZero(dateFinRetenueAncienne)
                || isDateAfterOrEquals(dateFinRetenueAncienne, dateProchainPaiementMensuel))
                && (retenueAncienne.getCsRoleFamillePC().equals(simplePCAccordee.getCsRoleBeneficiaire())
                ||
                //Cas 2DomRente où la retenue à reporter est celui du conjoint et qu'il faut reporter sur le requérant.
                (retenueAncienne.getCsRoleFamillePC().equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)
                        && mapCsRoleToPCAWithoutDateFin.size() == 1
                        && mapCsRoleToPCAWithoutDateFin.containsKey(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)))) {
            return true;
        }
        return false;
    }

    /**
     * Vérifier si l'idTiers de l'adresse de paiement vient d'un home
     *
     * @param idTiersAdressePmt
     * @return
     */
    private boolean isHome(String idTiersAdressePmt) throws JadeApplicationServiceNotAvailableException, HomeException, JadePersistenceException {
        SimpleHomeSearch search = new SimpleHomeSearch();
        search.setForIdTiersHome(idTiersAdressePmt);
        if (PegasusImplServiceLocator.getSimpleHomeService().count(search) > 0) {
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
        Map<String, CreanceAccordee> mapCreanceTrouve = new HashMap<>();
        Map<String, CreanceAccordee> mapOldCreance = new HashMap<>();
        Map<String, DonneeInterneHomeVersement> mapNewCreance = new HashMap<>();
        for (DonneeInterneHomeVersement donneeInterneHomeVersement : homeVersementList) {
            if (donneeInterneHomeVersement.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_CREANCIER)) {
                mapNewCreance.put(donneeInterneHomeVersement.getIdTiersAdressePaiement(), donneeInterneHomeVersement);
            }
        }
        for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
            CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
            CreanceAccordeeSearch creancierSearch = new CreanceAccordeeSearch();
            creancierSearch.setForIdVersionDroit(ancienneDonnee.getSimplePCAccordee().getIdVersionDroit());
            creancierSearch = PegasusServiceLocator.getCreanceAccordeeService().search(creancierSearch);
            for (JadeAbstractModel model : creancierSearch.getSearchResults()) {
                CreanceAccordee creanceAccordee = (CreanceAccordee) model;
                mapOldCreance.put(creanceAccordee.getSimpleCreancier().getIdTiersAdressePaiement(), creanceAccordee);
            }
        }
        DonneeInterneHomeVersement donneeInterneHomeVersement;
        CreanceAccordee creanceAccordee;
        for (String oldKey : mapOldCreance.keySet()) {
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
                // Supprimer l'ancienne créancier si les montants ont été changé avec la nouvelle droit sinon on retire de la liste des créances à créer
                if (!creanceAccordee.getSimpleCreancier().getMontant().equals(montantAVerser.toString())) {
                    PegasusImplServiceLocator.getSimpleCreancierService().deleteWithoutControl(creanceAccordee.getSimpleCreancier());
                    PegasusImplServiceLocator.getCreanceAccordeeService().delete(creanceAccordee);
                    mapCreanceTrouve.put(creanceAccordee.getSimpleCreancier().getIdTiersAdressePaiement(), creanceAccordee);
                } else {
                    homeVersementList.remove(donneeInterneHomeVersement);
                }
            } else {
                creanceAccordee = mapOldCreance.get(oldKey);
                if (Boolean.TRUE.equals(creanceAccordee.getSimpleCreancier().getIsHome())) {
                    PegasusImplServiceLocator.getSimpleCreancierService().deleteWithoutControl(creanceAccordee.getSimpleCreancier());
                    PegasusImplServiceLocator.getCreanceAccordeeService().delete(creanceAccordee);
                }
            }

        }


    }

    /**
     * Création du créancier et les créances accordées (répartitions du montant selon les PCAs) pour les versements directs en home
     * On verse au home = montant PC mensuel - montant PC mensuel déjà versé (cas de calcul rétro) - les dépenses personnels
     * Si c'est négatif, alors on mets à 0
     *
     * @param homeVersementList
     * @param mapCreancierDejaCreer
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PmtMensuelException
     * @throws CreancierException
     * @throws JadePersistenceException
     */
    private void createCreancierForVersementsHome(List<DonneeInterneHomeVersement> homeVersementList, Map<String, Creancier> mapCreancierDejaCreer) throws JadeApplicationServiceNotAvailableException, PmtMensuelException, CreancierException, JadePersistenceException {
        String dateDebut = "";
        String dateFin = "";
        for (DonneeInterneHomeVersement calculDonneesHome : homeVersementList) {
            if (calculDonneesHome.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_CREANCIER) && calculDonneesHome.isVersementDirect()) {
                if (JadeStringUtil.isBlankOrZero(calculDonneesHome.getDateFin())) {
                    dateFin = JadeDateUtil.getLastDateOfMonth(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
                } else {
                    dateFin = JadeDateUtil.getLastDateOfMonth(calculDonneesHome.getDateFin());
                }
                dateDebut = JadeDateUtil.getFirstDateOfMonth(calculDonneesHome.getDateDebut());
                int nbreMois = JadeDateUtil.getNbMonthsBetween(dateDebut, dateFin);
                Float montantTotalHome = getMontantHome(calculDonneesHome.getMontantHomes(), nbreMois);
                CreanceAccordee creanceAccordee = new CreanceAccordee();
                SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();
                simpleCreanceAccordee.setIdPCAccordee(calculDonneesHome.getIdPca());
                BigDecimal montantHome = BigDecimal.valueOf(getMontantHome(calculDonneesHome.getMontantHomes(), 1));
                Float montantPCMensuel = Float.parseFloat(calculDonneesHome.getMontantPCMensuel());
                Float montantAverser;
                String datefin;
                if (calculDonneesHome.getDateFinPCA() == null) {
                    datefin = calculDonneesHome.getDateFin();
                } else {
                    datefin = calculDonneesHome.getDateFinPCA();
                }
                nbreMois = JadeDateUtil.getNbMonthsBetween(JadeDateUtil.getFirstDateOfMonth(calculDonneesHome.getDateDebutPCA()), JadeDateUtil.getLastDateOfMonth(datefin));
                if (montantHome.floatValue() + Float.parseFloat(calculDonneesHome.getMontantDepenses()) / 12.0 > (montantPCMensuel - Float.parseFloat(calculDonneesHome.getMontantDejaVerser()))) {
                    float montantRestantApresDeduction = montantPCMensuel - Float.parseFloat(calculDonneesHome.getMontantDejaVerser());
                    if (montantRestantApresDeduction > 0.f) {
                        montantAverser = montantRestantApresDeduction - (Float.parseFloat(calculDonneesHome.getMontantDepenses()) / 12);
                    } else {
                        montantAverser = 0.f;
                    }
                    montantAverser = montantAverser * nbreMois;
                    montantAverser = BigDecimal.valueOf(montantAverser).setScale(0, RoundingMode.UP).floatValue();
                } else {
                    montantAverser = montantHome.setScale(0, RoundingMode.UP).floatValue();
                    if (montantAverser.floatValue() + Float.parseFloat(calculDonneesHome.getMontantDepenses()) / 12.0 > montantPCMensuel) {
                        montantAverser = montantAverser - 1;
                    }
                    montantAverser = montantAverser * nbreMois;
                }
                simpleCreanceAccordee.setMontant(montantAverser.toString());
                Creancier creancier = getCreancier(mapCreancierDejaCreer, calculDonneesHome, montantAverser, true);
                simpleCreanceAccordee.setIdCreancier(creancier.getId());
                creanceAccordee.setSimpleCreanceAccordee(simpleCreanceAccordee);
                PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);
            }
        }
    }

    private void createCreancierForVersementsSejour(List<DonneeInterneHomeVersement> sejourVersementList, Map<String, Creancier> mapCreancierDejaCreer) throws JadeApplicationServiceNotAvailableException, PmtMensuelException, CreancierException, JadePersistenceException {
        Map<String,DonneeInterneHomeVersement> mapFiltered = new LinkedHashMap<>();
        for (DonneeInterneHomeVersement calculDonneesHome : sejourVersementList) {
            if(!mapFiltered.containsKey(calculDonneesHome.getIdRenteAccordee())){
                mapFiltered.put(calculDonneesHome.getIdRenteAccordee(),calculDonneesHome);
            }

        }
        for (DonneeInterneHomeVersement calculDonneesHome : mapFiltered.values()) {
            BigDecimal montantAVerser = new BigDecimal(getMontantHome(calculDonneesHome.getMontantHomes(), 1));
            BigDecimal montantAVerserArrondi = montantAVerser.setScale(0, RoundingMode.UP);
            Creancier creancier = getCreancier(mapCreancierDejaCreer, calculDonneesHome, montantAVerserArrondi.floatValue(), true);
            CreanceAccordee creanceAccordee = new CreanceAccordee();
            SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();
            simpleCreanceAccordee.setIdCreancier(creancier.getId());
            simpleCreanceAccordee.setIdPCAccordee(calculDonneesHome.getIdPca());
            Float montantPCMensuel = Float.parseFloat(calculDonneesHome.getMontantPCMensuel());
            float montantRestantApresDeduction;
            if (montantAVerserArrondi.floatValue() > (montantPCMensuel - Float.parseFloat(calculDonneesHome.getMontantDejaVerser()))) {
                montantRestantApresDeduction = montantPCMensuel - Float.parseFloat(calculDonneesHome.getMontantDejaVerser());
                if (montantRestantApresDeduction < 0.f) {
                    montantRestantApresDeduction = 0;
                }
            } else {
                montantRestantApresDeduction = montantAVerserArrondi.floatValue();
                if (montantRestantApresDeduction < 0.f) {
                    montantRestantApresDeduction = 0;
                }
            }
            simpleCreanceAccordee.setMontant(BigDecimal.valueOf(montantRestantApresDeduction).toString());
            creanceAccordee.setSimpleCreanceAccordee(simpleCreanceAccordee);
            PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);
        }
    }

    /**
     * Empêcher de créer plusieurs même créanciers pour les cas des couples qui sont les 2 en homes et le même
     */
    private Creancier getCreancier(Map<String, Creancier> mapCreancierDejaCreer, DonneeInterneHomeVersement calculDonneesHome, Float montantAVerser, boolean addMontant) throws CreancierException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        Creancier creancier;
        String key = calculDonneesHome.getIdTiersHome();
        if (!mapCreancierDejaCreer.containsKey(key)) {
            creancier = new Creancier();
            SimpleCreancier simpleCreancier = new SimpleCreancier();
            simpleCreancier.setCsEtat(IPCDroits.CS_ETAT_CREANCE_A_PAYE);
            simpleCreancier.setCsTypeCreance(IPCCreancier.CS_TYPE_CREANCE_TIERS);
            simpleCreancier.setIdDemande(calculDonneesHome.getIdDemande());
            simpleCreancier.setIdDomaineApplicatif(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            simpleCreancier.setIdTiers(calculDonneesHome.getIdTiersHome());
            simpleCreancier.setIdTiersAdressePaiement(calculDonneesHome.getIdTiersHome());
            simpleCreancier.setIdTiersRegroupement(calculDonneesHome.getIdTiersRegroupement());
            Float montantAverserArrondi = new BigDecimal(montantAVerser).setScale(0, RoundingMode.UP).floatValue();
            simpleCreancier.setMontant(montantAverserArrondi.toString());
            simpleCreancier.setIsCalcule(true);
            simpleCreancier.setIsHome(true);
            creancier.setSimpleCreancier(simpleCreancier);
            creancier.setCsRole(calculDonneesHome.getCsRoleBeneficiaire());
            creancier = PegasusServiceLocator.getCreancierService().create(creancier);
            mapCreancierDejaCreer.put(key, creancier);
        } else {
            creancier = mapCreancierDejaCreer.get(key);
            /**
             * CAS : Conjoint va dans le même home que le requérant => Ajouter le paiement du conoint dans le montant total à verser au home
             */
            if (!creancier.isCreatedForOther() && !calculDonneesHome.getCsRoleBeneficiaire().equals(creancier.getCsRole())) {
                addMontant = true;
                creancier.setCreatedForOther(true);

            }
            if (addMontant) {
                Float montant = Float.parseFloat(creancier.getSimpleCreancier().getMontant()) + montantAVerser;
                creancier.getSimpleCreancier().setMontant(montant.toString());
                PegasusServiceLocator.getCreancierService().update(creancier);
                mapCreancierDejaCreer.put(key, creancier);
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
            if (calculDonneesHome.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_RETENUS) && calculDonneesHome.isVersementDirect()) {
                pcaRetenue.setIdPCAccordee(calculDonneesHome.getIdPca());
                pcaRetenue.setCsRoleFamillePC(calculDonneesHome.getCsRoleBeneficiaire());
                SimpleRetenuePayement retenue = new SimpleRetenuePayement();
                retenue.setCsTypeRetenue(IPCRetenues.CS_ADRESSE_PAIEMENT);
                retenue.setDateDebutRetenue(calculDonneesHome.getDateDebut());
                retenue.setDateFinRetenue(calculDonneesHome.getDateFin());
                retenue.setIdTiersAdressePmt(calculDonneesHome.getIdTiersHome());
                retenue.setIdRenteAccordee(calculDonneesHome.getIdRenteAccordee());
                retenue.setIdDomaineApplicatif(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                Float montantPCMensuel = Float.parseFloat(calculDonneesHome.getMontantPCMensuel());
                Float montantAverser;
                BigDecimal montantHome = new BigDecimal(getMontantHome(calculDonneesHome.getMontantHomes(), 1));
                if (montantHome.floatValue() + Float.parseFloat(calculDonneesHome.getMontantDepenses()) / 12.0 > montantPCMensuel) {
                    montantAverser = montantPCMensuel - (Float.parseFloat(calculDonneesHome.getMontantDepenses()) / 12);
                    montantAverser = new BigDecimal(montantAverser).setScale(0, RoundingMode.UP).floatValue();
                } else {
                    montantAverser = montantHome.setScale(0, RoundingMode.UP).floatValue();
                    if (montantAverser.floatValue() + Float.parseFloat(calculDonneesHome.getMontantDepenses()) / 12.0 > montantPCMensuel) {
                        montantAverser = montantAverser - 1;
                    }
                }

                retenue.setMontantRetenuMensuel(montantAverser.toString());
                retenue.setMontantTotalARetenir("999'999.00");
                pcaRetenue.setSimpleRetenue(retenue);
                PegasusServiceLocator.getRetenueService().createWithOutCheck(pcaRetenue);
            }
        }
    }

    private void mappingHomes(List<DonneeInterneHomeVersement> homeVersementList
            , CalculDonneesHomeSearch listHomes, List<SimplePCAccordee> allNewPca, Map<String, String> mapMontantTotalHome, Droit droit) throws JadeApplicationException, JadePersistenceException {

        DonneeInterneHomeVersement donnee;
        Map<String, List<SimplePCAccordee>> mapToIDPCA = new HashMap<>();
        List<SimplePCAccordee> listPCA;
        boolean isCoupleInHome = false;
        /**
         *  Contrôle : Si le couple est en home => Si il y'a déjà des montants versés => 1/2 du montant à déduire sur chaque répartition des créances
         */
        Map<String, List<SimplePCAccordee>> mapForCalcul = new LinkedHashMap<>();
        for (SimplePCAccordee pca : allNewPca) {
            if (!JadeStringUtil.isBlankOrZero(pca.getMontantMensuel())) {
                String dateFin = "";
                if (JadeStringUtil.isBlankOrZero(pca.getDateFin())) {
                    dateFin = "0";
                } else {
                    dateFin = pca.getDateFin();
                }
                String key = pca.getDateDebut() + dateFin;
                if (mapForCalcul.containsKey(key)) {
                    listPCA = mapForCalcul.get(key);
                    listPCA.add(pca);
                    mapForCalcul.put(key, listPCA);
                } else {
                    listPCA = new ArrayList<>();
                    listPCA.add(pca);
                    mapForCalcul.put(key, listPCA);
                }
            }
        }
        List<SimplePCAccordee> listUpdated = new LinkedList<>();
        for (Map.Entry<String, List<SimplePCAccordee>> entrySet : mapForCalcul.entrySet()) {
            for (SimplePCAccordee simplePCAccordee : entrySet.getValue()) {
                listUpdated.add(simplePCAccordee);
            }
        }

        /**
         * Mapper les PCA par Bénéficiaire
         */
        for (SimplePCAccordee pca : listUpdated) {
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


        /**
         * Regroup les homes pour éviter des doublons de retenus lié la prise en compte du prix du chambre.
         */
        Map<String, CalculDonneesHome> mapHomeFilter = new HashMap<>();
        for (JadeAbstractModel model : listHomes.getSearchResults()) {
            CalculDonneesHome home = (CalculDonneesHome) model;
            String key = home.getIdHome() + home.getCsRoleFamille() + home.getDateDebutDFH() + home.getDateFinDFH();
            if (!mapHomeFilter.containsKey(key)) {
                mapHomeFilter.put(key, home);
            }
        }
        /**
         * Remplir les données pour la création des retenus et/ou créancier avec une dernière contrôle
         */
        for (CalculDonneesHome model : mapHomeFilter.values()) {
            CalculDonneesHome home = model;
            if (mapToIDPCA.containsKey(home.getIdTiersRegroupement())) {
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
                        String fKey = simplePCAccordeeBenef.getIdPCAccordee() + "-";
                        //Cas séperation où le conjoint est devenu requérant.
                        if (donnee.getCsRoleBeneficiaire().equals(RoleMembreFamille.REQUERANT.getValue())) {
                            donnee.setMontantHomes(mapMontantTotalHome.get(fKey + REQUERANT_HOME));
                            donnee.setMontantDepenses(mapMontantTotalHome.get(fKey + REQUERANT_DEP_PERS));
                        } else {
                            donnee.setMontantHomes(mapMontantTotalHome.get(fKey + CONJOINT_HOME));
                            donnee.setMontantDepenses(mapMontantTotalHome.get(fKey + CONJOINT_DEP_PERS));
                        }
                        String montantDejaVerser = searchMontantDejaVerser(periode, droit.getSimpleDroit().getIdDroit(), droit.getSimpleVersionDroit().getNoVersion(), simplePCAccordeeBenef.getCsRoleBeneficiaire());
                        if (JadeStringUtil.isBlankOrZero(montantDejaVerser)) {
                            donnee.setMontantDejaVerser("0.0");
                        } else {
                            donnee.setMontantDejaVerser(montantDejaVerser);
                        }
                        donnee.setCsTypeVersement(csTypeVersement);
                        donnee.setDateDebut(periode.getDateDebut());
                        donnee.setDateFin(periode.getDateFin());
                        donnee.setDateDebutPCA(simplePCAccordeeBenef.getDateDebut());
                        donnee.setDateFinPCA(simplePCAccordeeBenef.getDateFin());
                        donnee.setVersementDirect(home.getIsVersementDirect());
                        if (canAddForVersementDirect(donnee, simplePCAccordeeBenef, periode)) {
                            homeVersementList.add(donnee);
                        }
                    }
                }
            }

        }
    }

    private boolean canAddForVersementDirect(DonneeInterneHomeVersement donnee, SimplePCAccordee simplePCAccordeeBenef, Periode periode) throws CalculException {

        /**
         * CAS 1 : C'est pour les paiments futurs : retenus => Vérifier que c'est mappé avec la PCA qui a pas de de date de fin
         */
        if (donnee.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_RETENUS) && JadeStringUtil.isBlankOrZero(simplePCAccordeeBenef.getDateFin())) {
            return true;
        }
        /**
         * CAS 2 : C'est pour les paiements rétros : créances
         *  1) Vérifier que c'est mappé avec la bonne PCA
         *  2) Vérier qu'il a n'a pas de date de fin
         */
        if (donnee.getCsTypeVersement().equals(DonneeInterneHomeVersement.TYPE_CREANCIER)
                && IsSamePeriode(simplePCAccordeeBenef.getDateDebut(), simplePCAccordeeBenef.getDateFin(), periode)
                && !JadeStringUtil.isBlankOrZero(simplePCAccordeeBenef.getDateFin())
        ) {
            return true;
        }

        return false;
    }

    private String searchMontantDejaVerser(Periode periodes, String idDroit, String noVersionDroitCourant, String csRoleBeneficiaire) throws CalculException {
        try {
            String dateMin;
            String dateMax;
            if (JadeDateUtil.isGlobazDateMonthYear(periodes.getDateDebut())) {
                dateMin = periodes.getDateDebut();
            } else {
                dateMin = periodes.getDateDebut().substring(3);
            }
            if (JadeStringUtil.isBlankOrZero(periodes.getDateFin())) {
                if (JadeDateUtil.isGlobazDateMonthYear(periodes.getDateDebut())) {
                    dateMax = "12." + dateMin.substring(3);
                } else {
                    dateMax = "12." + dateMin.substring(6);
                }
            } else {
                if (JadeDateUtil.isGlobazDateMonthYear(periodes.getDateFin())) {
                    dateMax = periodes.getDateFin();
                } else {
                    dateMax = periodes.getDateFin().substring(3);
                }
            }
            List<PcaForDecompte> pcaDejaVerser = PcaPrecedante.findPcaToReplaced(dateMin, dateMax, idDroit,
                    noVersionDroitCourant);
            if (pcaDejaVerser.size() == 1) {
                if (csRoleBeneficiaire.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)
                        && pcaDejaVerser.get(0).getSimplePrestationsAccordeesConjoint() != null) {
                    return pcaDejaVerser.get(0).getSimplePrestationsAccordeesConjoint().getMontantPrestation();
                } else {
                    return pcaDejaVerser.get(0).getSimplePrestationsAccordees().getMontantPrestation();
                }
            } else if (pcaDejaVerser.size() == 2) {
                for (PcaForDecompte pcaOld : pcaDejaVerser) {
                    if (pcaOld.getSimplePCAccordee().getCsRoleBeneficiaire().equals(csRoleBeneficiaire)) {
                        return pcaOld.getMontantPCMensuelle();
                    }
                }
            } else {
                return "0.0";
            }
        } catch (Exception e) {
            throw new CalculException("Service not available - " + e.getMessage());
        }
        return "0.0";
    }

    private boolean IsSamePeriode(String dateDebut, String dateFin, Periode periode) throws CalculException {
        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            if (isDateAfterOrEquals("01." + periode.getDateDebut(), "01." + dateDebut)) {
                return true;
            }
        } else {
            SimpleDateFormat reader = new SimpleDateFormat("MM.yyyy");
            Date dateDebutPeriode = null;
            try {
                dateDebutPeriode = reader.parse(periode.getDateDebut());
                Date dateFinPeriode = reader.parse(periode.getDateFin());
                Date dateDebutPCA = reader.parse(dateDebut);
                Date dateFinPCA = reader.parse(dateFin);
                if (dateFinPCA.before(dateDebutPeriode) || dateDebutPCA.after(dateFinPeriode)) {
                    return false;
                } else {
                    return true;
                }
            } catch (ParseException e) {
                throw new CalculException(e.getMessage());
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

    private boolean isDateBeforeOrEquals(String dateDebutDFH, String dateProchainPmt) {
        if (JadeDateUtil.isDateBefore(dateProchainPmt, dateDebutDFH) || dateDebutDFH.equals(dateProchainPmt)) {
            return true;
        }
        return false;

    }


    /**
     * Fermer tout les retenues des pcas de l'ancien droit pour éviter un blocage de ceux qui ont des retenues sans dates de fins
     *
     * @param isVersementHome
     * @param anciennesPCAccordees
     * @param simplePCAccordee
     * @param copies
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

    private void clotrerAncienneRetenus(CalculPcaReplaceSearch anciennesPCAccordees, SimplePCAccordee simplePCAccordee, List<CalculPcaReplace> copies) throws JadeApplicationException, JadePersistenceException {
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
                                if (isDateAfterOrEquals(retenue.getSimpleRetenue().getDateDebutRetenue(), dateProchainPaiement)) {
                                    retenue.getSimpleRetenue().setDateDebutRetenue(dateProchainPaiement);
                                }
                                retenue.getSimpleRetenue().setDateFinRetenue(dateProchainPaiement);
                                PegasusServiceLocator.getRetenueService().updateWithoutCheck(retenue);
                                SimplePrestationsAccordees simplePrestationsAccordees = anciennePCACourante.getSimplePrestationsAccordees();
                                simplePrestationsAccordees.setIsRetenues(Boolean.FALSE);
                                PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(simplePrestationsAccordees);
                                simplePrestationsAccordees = anciennePCACourante.getSimplePrestationsAccordeesConjoint();
                                if (simplePrestationsAccordees != null) {
                                    simplePrestationsAccordees.setIsRetenues(Boolean.FALSE);
                                    PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(simplePrestationsAccordees);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (CalculPcaReplace calculPcaReplace : copies) {
            SimplePrestationsAccordees simplePrestationsAccordees = calculPcaReplace.getSimplePrestationsAccordees();
            simplePrestationsAccordees.setIsRetenues(Boolean.FALSE);
            PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(simplePrestationsAccordees);
            simplePrestationsAccordees = calculPcaReplace.getSimplePrestationsAccordeesConjoint();
            if (simplePrestationsAccordees != null) {
                simplePrestationsAccordees.setIsRetenues(Boolean.FALSE);
                PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(simplePrestationsAccordees);
            }
        }
    }

    /**
     * Création de l'historique des  créanciers/créances si il n'existe pas
     *
     * @param anciennesPCAccordees
     */
    private void createCreancierHystoriqueOld(CalculPcaReplaceSearch anciennesPCAccordees) throws JadeApplicationServiceNotAvailableException, CreancierException, JadePersistenceException {
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

    private void createCreancierHystoriqueNew(SimplePCAccordee pcAccordee) throws JadeApplicationServiceNotAvailableException, CreancierException, JadePersistenceException {

        String idPCAAccordee = pcAccordee.getIdPCAccordee();
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
                                        DonneesHorsDroitsProvider containerGlobal, boolean retroactif, String dateSplitReforme) throws CalculException,
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
            dertermineCalculs(droit, listePCAccordes, listePCAccordesReforme, dateDebutPlageCalcul, dateFinPlageCalcul, dateReforme, retroactif, dateSplitReforme);
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
                                   String dateDebutPlageCalcul, String dateFinPlageCalcul, String dateReforme, boolean retroactif, String dateSplitReforme) throws PropertiesException, JadePersistenceException {
        String dateDemande = JadeDateUtil.getFirstDateOfMonth(droit.getDemande().getSimpleDemande().getDateDebut());
        if (JadeStringUtil.isEmpty(dateDemande) || JadeDateUtil.isDateBefore(dateDebutPlageCalcul, dateDemande)) {
            dateDemande = dateDebutPlageCalcul;
        }
        Boolean reforme = EPCProperties.REFORME_PC.getBooleanValue();
        String noVersionPrecedante = versionPrecedante(droit);
        if (JadeDateUtil.isDateBefore(dateFinPlageCalcul, dateReforme) || !reforme) {
            // calcul sur ancien calculateur uniquement
            listePCAccordesReforme.clear();
        } else if (!droit.getDemande().getSimpleDemande().getForcerCalculTransitoire() &&
                (dateDemande.equals(dateReforme) || JadeDateUtil.isDateAfter(dateDemande, dateReforme))) {
            // calcul uniquement calcul réforme
            listePCAccordes.clear();
        } else if (!droit.getDemande().getSimpleDemande().getForcerCalculTransitoire() &&
                !noVersionPrecedante.isEmpty()) {
            if (!retroactif) {
                // Si derniere periode de la version droit - 1 réforme : calcul uniquement calcul réforme
                uniquementReformeDroitPrecedant(droit, listePCAccordes, noVersionPrecedante);
            } else if (dateSplitReforme != null) {
                periodesCompareSelonDroitPrecedant(droit, listePCAccordes, dateSplitReforme);
            }
        }
        // sinon on garde les 2 listes pour le calcul comparatif
    }

    /**
     * Returne le numéro de version de droit précédent
     *
     * @param droit
     * @return
     */
    private String versionPrecedante(Droit droit) {
        String version = droit.getSimpleVersionDroit().getNoVersion();
        if (!JadeStringUtil.isBlankOrZero(version) && Integer.parseInt(version) > 1) {
            return Integer.toString(Integer.parseInt(version) - 1);
        }
        return "";
    }

    /**
     * Si la pca de la version de droit précédente est réforme alors toutes les pca de cette version sont réforme
     *
     * @param droit
     * @param listePCAccordes
     * @param noVersionPrecedante
     * @throws JadePersistenceException
     */
    private void uniquementReformeDroitPrecedant(Droit droit, List<PeriodePCAccordee> listePCAccordes, String noVersionPrecedante) throws JadePersistenceException {
        List<PCAccordeePlanCalculReforme> listPcaPrecedentes = PcaPlanCalculReforme.findPcaCourranteWithDateDebutDesc(droit.getId(), noVersionPrecedante);
        PCAccordeePlanCalculReforme pca = listPcaPrecedentes.get(0);
        if (pca.getReformePc() != null && pca.getReformePc()) {
            listePCAccordes.clear();
        }
    }

    /**
     * Pour un calcul rétro : uniquement réforme à partir de la date passée en param
     *
     * @param droit
     * @param listPca
     * @param dateSplitReforme
     * @throws JadePersistenceException
     */
    private void periodesCompareSelonDroitPrecedant(Droit droit, List<PeriodePCAccordee> listPca, String dateSplitReforme) throws JadePersistenceException {
        String dateCompare = JadeDateUtil.getFirstDateOfMonth(dateSplitReforme);
        for (PeriodePCAccordee pca : listPca) {
            if (!JadeDateUtil.isDateBefore(pca.getStrDateDebut(), dateCompare)) {
                pca.setNePasCalculer(true);
            }
        }
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
