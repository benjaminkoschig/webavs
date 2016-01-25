package ch.globaz.perseus.businessimpl.services.statistiquesMensuelles;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesDemPcfDec;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesEnfantDemande;
import ch.globaz.perseus.business.statsmensuelles.Administration;
import ch.globaz.perseus.business.statsmensuelles.StatistiquesMensuellesComptageMontantInterface;
import ch.globaz.perseus.business.statsmensuelles.StatistiquesMensuellesDataMonitoringInterface;
import ch.globaz.perseus.business.statsmensuelles.TypeStat;

/**
 * @author RCO <br/>
 *         Utilisé pour stockés les données pour les statistiques mensuelles.
 * 
 */
public class StatistiquesMensuellesDonnees {

    /**
     * {@link TreeMap}<{@link String}, {@link Administration}> <br/>
     * Key: idAdministration {@link String}, value: objet {@link Administration}
     */
    private TreeMap<String, Administration> administration;

    /**
     * Key: idAdministration, Value: Map de (key:TypeStat, value: types de données à compter) <br/>
     * {@link Map}<{@link String}, {@link Map}<{@link TypeStat}, {@link StatistiquesMensuellesDataMonitoringInterface}>>
     */
    private Map<String, Map<TypeStat, StatistiquesMensuellesDataMonitoringInterface>> dataMonitoring = new HashMap<String, Map<TypeStat, StatistiquesMensuellesDataMonitoringInterface>>();

    /**
     * Key: idDossier, Value: List de StatistiquesMensuellesDemPcfDec <br/>
     * {@link Map}<{@link String}, {@link List}<{@link StatistiquesMensuellesDemPcfDec}>>
     */
    private Map<String, List<StatistiquesMensuellesDemPcfDec>> statsGroupesParDossier;

    /**
     * {@link String} moisDebut, Format MM.AAAA
     */
    private String moisDebut;

    /**
     * {@link String} moisFin, Format MM.AAAA
     */
    private String moisFin;

    /**
     * {@link Map}<{@link String}<{@link Map}<{@link TypeStat}, {@link List}<{@link String}>>> Map contenant en key les
     * id des administrations et dans les values : <br/>
     * un Map contenant en key le type de stat ({@link TypeStat}) et en value une liste d'idDemandes pour lesquelles on
     * va compter les enfants.
     */
    private Map<String, Map<TypeStat, List<StatistiquesMensuellesDemPcfDec>>> mapIdDemandePourEnfants = new HashMap<String, Map<TypeStat, List<StatistiquesMensuellesDemPcfDec>>>();

    /**
     * {@link Map}<{@link TypeStat}, {@link List}<{@link String}>>
     */
    private Map<TypeStat, List<String>> typeDecisionSelonIdDossier = new HashMap<TypeStat, List<String>>();

    /**
     * Map associant l'état initiale des RI avec les valeurs finales {@link Map}<{@link String}, {@link Boolean}>
     */
    private Map<String, Boolean> mapAutreRI;
    private Map<String, Boolean> mapDemandeRI;

    /**
     * @param administrationMap ({@link TreeMap}<{@link String}, {@link Administration}>
     * @param statsGroupesParDossier {@link Map}<{@link String}, {@link List}<{@link StatistiquesMensuellesDemPcfDec}>>
     * @param moisDebut {@link String} Format du mois MM.AAAA
     * @param moisFin {@link String} Format du mois MM.AAAA
     */
    public StatistiquesMensuellesDonnees(TreeMap<String, Administration> administrationMap,
            Map<String, List<StatistiquesMensuellesDemPcfDec>> statsGroupesParDossier, Map<String, Boolean> mapAutreRI,
            Map<String, Boolean> mapDemandeRI, String moisDebut, String moisFin) {
        this.statsGroupesParDossier = statsGroupesParDossier;
        administration = administrationMap;
        setMapDataMonitoring();
        this.mapAutreRI = mapAutreRI;
        this.mapDemandeRI = mapDemandeRI;
        this.moisDebut = moisDebut;
        this.moisFin = moisFin;
        traitementDonnee();
    }

    /**
     * Affecte pour chaque identifiant administration une map contenant : <br/>
     * <ul>
     * <li>En key : &nbsp;&nbsp;&nbsp; le type de statistique {@link TypeStat}</li>
     * <li>En value : &nbsp;l'interface {@link StatistiquesMensuellesDataMonitoringInterface}</li>
     * </ul>
     */
    private void setMapDataMonitoring() {
        for (String uneAdministration : administration.keySet()) {
            Map<TypeStat, StatistiquesMensuellesDataMonitoringInterface> mapTypesDeStats = initialiseNouveauMapStat();
            dataMonitoring.put(uneAdministration, mapTypesDeStats);
        }
    }

    /**
     * Initialise un nouveau {@link Map}<{@link TypeStat}, {@link StatistiquesMensuellesDataMonitoringInterface}> </br>
     * Chaque statistiques à compter a son pareil dans les types de stats
     * 
     * @return {@link Map}<{@link TypeStat}, {@link StatistiquesMensuellesDataMonitoringInterface}>
     */
    private Map<TypeStat, StatistiquesMensuellesDataMonitoringInterface> initialiseNouveauMapStat() {
        // Variables pour les divers comptages --------------------------------------------------------------------
        Map<TypeStat, StatistiquesMensuellesDataMonitoringInterface> maListeDeStats = new HashMap<TypeStat, StatistiquesMensuellesDataMonitoringInterface>();
        StatistiquesMensuellesDataMonitoringInterface smDemandesEnregistrees = new StatistiquesMensuellesDemandesEnregistrees();
        StatistiquesMensuellesDataMonitoringInterface smOctroi = new StatistiquesMensuellesOctroi();
        StatistiquesMensuellesDataMonitoringInterface smOctroiPartiel = new StatistiquesMensuellesOctroiPartiel();
        StatistiquesMensuellesDataMonitoringInterface smRefusSansCalcul = new StatistiquesMensuellesRefusSansCalcul();
        StatistiquesMensuellesDataMonitoringInterface smNonEntreeMatiere = new StatistiquesMensuellesNonEntreeEnMatiere();
        StatistiquesMensuellesDataMonitoringInterface smSuppression = new StatistiquesMensuellesSuppression();
        StatistiquesMensuellesDataMonitoringInterface smProjet = new StatistiquesMensuellesProjet();
        StatistiquesMensuellesDataMonitoringInterface smRenonciation = new StatistiquesMensuellesRenonciation();
        StatistiquesMensuellesDataMonitoringInterface smRevisionExtraordinaire = new StatistiquesMensuellesRevisionExtraordinaire();
        StatistiquesMensuellesDataMonitoringInterface smEnfantOctroiMoins6 = new StatistiquesMensuellesEnfantOctroiMoins6();
        StatistiquesMensuellesDataMonitoringInterface smEnfantOctroiPluss6 = new StatistiquesMensuellesEnfantOctroiPlus6();
        StatistiquesMensuellesDataMonitoringInterface smEnfantPartielMoins6 = new StatistiquesMensuellesEnfantsPartielMoins6();
        StatistiquesMensuellesDataMonitoringInterface smEnfantPartielPlus6 = new StatistiquesMensuellesEnfantsPartielPlus6();
        maListeDeStats.put(TypeStat.DEMANDE, smDemandesEnregistrees);
        maListeDeStats.put(TypeStat.OCTROI_COMPLET, smOctroi);
        maListeDeStats.put(TypeStat.OCTROI_PARTIEL, smOctroiPartiel);
        maListeDeStats.put(TypeStat.REFUS_SANS_CALCUL, smRefusSansCalcul);
        maListeDeStats.put(TypeStat.NON_ENTREE_MATIERE, smNonEntreeMatiere);
        maListeDeStats.put(TypeStat.SUPPRESSION, smSuppression);
        maListeDeStats.put(TypeStat.PROJET, smProjet);
        maListeDeStats.put(TypeStat.RENONCIATION, smRenonciation);
        maListeDeStats.put(TypeStat.REVISION_EXTRAORDINAIRE, smRevisionExtraordinaire);
        maListeDeStats.put(TypeStat.ENFANTS_OCTROI_MOINS_6, smEnfantOctroiMoins6);
        maListeDeStats.put(TypeStat.ENFANTS_OCTROI_PLUS_6, smEnfantOctroiPluss6);
        maListeDeStats.put(TypeStat.ENFANTS_PARTIEL_MOINS_6, smEnfantPartielMoins6);
        maListeDeStats.put(TypeStat.ENFANTS_PARTIEL_PLUS_6, smEnfantPartielPlus6);

        return maListeDeStats;
    }

    /**
     * Ajoute une statistique à notre liste de statistiques et la compte.
     * 
     * @param comptageInterface {@link StatistiquesMensuellesComptageMontantInterface}
     * @param typeStat {@link TypeStat}
     */
    private void ajouteStat(StatistiquesMensuellesComptageMontantInterface comptageInterface, TypeStat typeStat) {
        if (!isTypeDecisionDejaComptePourIdDossier(comptageInterface, typeStat)) {
            setRIOriginale(comptageInterface, typeStat);
            dataMonitoring.get(comptageInterface.getDemande().getIdAgenceCommunale()).get(typeStat)
                    .compter(comptageInterface);
        }
    }

    private void ajouteStatTypeOctroi(StatistiquesMensuellesComptageMontantInterface comptageInterface,
            TypeStat typeStat) {
        setRIOriginale(comptageInterface, typeStat);
        dataMonitoring.get(comptageInterface.getDemande().getIdAgenceCommunale()).get(typeStat)
                .compter(comptageInterface);
    }

    /*
     * Associe la valeur initiale de RI à notre statistique. Les utilisateurs peuvent oublier de recocher la case à
     * cocher
     * pour indiquer si oui ou non les clients viennent du RI. Dans ce cas, on corrige leurs oubli.
     */
    private void setRIOriginale(StatistiquesMensuellesComptageMontantInterface comptageInterface, TypeStat typeStat) {
        switch (typeStat) {
            case ENFANTS_OCTROI_MOINS_6:
            case ENFANTS_OCTROI_PLUS_6:
            case ENFANTS_PARTIEL_MOINS_6:
            case ENFANTS_PARTIEL_PLUS_6:
            case NON_ENTREE_MATIERE:
            case OCTROI_COMPLET:
            case OCTROI_PARTIEL:
            case SUPPRESSION:
            case REFUS_SANS_CALCUL:
                if (mapAutreRI.containsKey(comptageInterface.getDemande().getIdDossier())) {
                    comptageInterface.getDemande().setFromRI(
                            mapAutreRI.get(comptageInterface.getDemande().getIdDossier()));
                }
                break;
            case DEMANDE:
                if (mapDemandeRI.containsKey(comptageInterface.getDemande().getIdDossier())) {
                    comptageInterface.getDemande().setFromRI(
                            mapDemandeRI.get(comptageInterface.getDemande().getIdDossier()));
                }
            default:
                break;
        }
    }

    private boolean isTypeDecisionDejaComptePourIdDossier(
            StatistiquesMensuellesComptageMontantInterface comptageInterface, TypeStat typeStat) {

        if (typeStat == TypeStat.REVISION_EXTRAORDINAIRE) {
            return false;
        }

        if (typeDecisionSelonIdDossier.containsKey(typeStat)) {
            if (typeDecisionSelonIdDossier.get(typeStat).contains(comptageInterface.getDemande().getIdDossier())) {
                return true;
            } else {
                typeDecisionSelonIdDossier.get(typeStat).add(comptageInterface.getDemande().getIdDossier());
                return false;
            }
        } else {
            List<String> listIdDossier = new ArrayList<String>();
            listIdDossier.add(comptageInterface.getDemande().getIdDossier());
            typeDecisionSelonIdDossier.put(typeStat, listIdDossier);

            return false;
        }
    }

    /**
     * @return {@link TreeMap}<{@link String}, {@link Administration}> <br/>
     *         <ul>
     *         <li>En key : &nbsp;&nbsp;&nbsp; idAdministration {@link String}</li>
     *         <li>En value : &nbsp;l'interface objet {@link Administration}</li>
     *         </ul>
     */
    public TreeMap<String, Administration> getAdministration() {
        return administration;
    }

    /**
     * Retourne les statistiques par {@link Administration}
     * 
     * @param idAdministration {@link String}
     * @return {@link Map}<{@link TypeStat}, {@link StatistiquesMensuellesDataMonitoringInterface}>
     */
    public Map<TypeStat, StatistiquesMensuellesDataMonitoringInterface> getStatsParAdministration(
            String idAdministration) {
        return dataMonitoring.get(idAdministration);
    }

    /**
     * Distribue dans le map la statistique à compter.
     */
    private void traitementDonnee() {

        // traitement des données
        for (List<StatistiquesMensuellesDemPcfDec> statsPourUnDossier : statsGroupesParDossier.values()) {

            // Comptage des demandes
            for (int indexDemande = 0; indexDemande < statsPourUnDossier.size(); indexDemande++) {
                if ((!CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(
                        statsPourUnDossier.get(indexDemande).getDemande().getTypeDemande()))
                        && (!CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(
                                statsPourUnDossier.get(indexDemande).getDemande().getTypeDemande()))
                        && dateDansLeCreneau(statsPourUnDossier.get(indexDemande).getDemande().getDateDemandeSaisie())) {
                    ajouteStat(statsPourUnDossier.get(indexDemande), TypeStat.DEMANDE);
                    break;
                }
            }

            for (int indexDecisionValide = 0; indexDecisionValide < statsPourUnDossier.size(); indexDecisionValide++) {
                if (CSEtatDecision.VALIDE.getCodeSystem().equals(
                        statsPourUnDossier.get(indexDecisionValide).getCsEtatDecision())
                        && dateDansLeCreneau(statsPourUnDossier.get(indexDecisionValide).getDateValidation())) {

                    ajouteStatistiqueSelonTypeStat(statsPourUnDossier, indexDecisionValide);
                }
            }
        }

        comptageDesEnfants(TypeStat.OCTROI_COMPLET);
        comptageDesEnfants(TypeStat.OCTROI_PARTIEL);
    }

    /**
     * Compte une statistique selon son type
     * 
     * @param statsPourUnDossier {@link List}<{@link StatistiquesMensuellesDemPcfDec}>
     * @param indexDecisionValide {@link int} index de la décision à prendre
     */
    private void ajouteStatistiqueSelonTypeStat(List<StatistiquesMensuellesDemPcfDec> statsPourUnDossier,
            int indexDecisionValide) {
        switch (CSTypeDecision.getEnumFromCodeSystem(statsPourUnDossier.get(indexDecisionValide).getCsTypeDecision()
                .toString())) {
            case NON_ENTREE_MATIERE:
                ajouteStat(statsPourUnDossier.get(indexDecisionValide), TypeStat.NON_ENTREE_MATIERE);
                break;

            case SUPPRESSION:
                ajouteStat(statsPourUnDossier.get(indexDecisionValide), TypeStat.SUPPRESSION);
                break;

            case PROJET:
                ajouteStat(statsPourUnDossier.get(indexDecisionValide), TypeStat.PROJET);
                break;

            case RENONCIATION:
                ajouteStat(statsPourUnDossier.get(indexDecisionValide), TypeStat.RENONCIATION);
                break;
            default:
                boolean isRevExtra;
                isRevExtra = isRevisionExtraordinaire(statsPourUnDossier, indexDecisionValide);

                ajouteStatistiqueAvecRevisionExtraordinaire(statsPourUnDossier, indexDecisionValide, isRevExtra);
        }
    }

    /**
     * 
     * @param statsPourUnDossier {@link List}<{@link StatistiquesMensuellesDemPcfDec}>
     * @param indexDecisionValide {@link int} index de la décision à prendre
     * @param isRevExtra {@link boolean} indique si oui ou non on a une révision extraordinaire
     */
    private void ajouteStatistiqueAvecRevisionExtraordinaire(List<StatistiquesMensuellesDemPcfDec> statsPourUnDossier,
            int indexDecisionValide, boolean isRevExtra) {

        switch (CSTypeDecision.getEnumFromCodeSystem(statsPourUnDossier.get(indexDecisionValide).getCsTypeDecision()
                .toString())) {
            case OCTROI_COMPLET:
                if (!isRevExtra
                        || isDecisionPrecedenteTypeFournit(statsPourUnDossier, indexDecisionValide,
                                CSTypeDecision.OCTROI_PARTIEL.getCodeSystem())) {
                    if (!isTypeDecisionDejaComptePourIdDossier(statsPourUnDossier.get(indexDecisionValide),
                            TypeStat.OCTROI_COMPLET)) {
                        ajouteStatTypeOctroi(statsPourUnDossier.get(indexDecisionValide), TypeStat.OCTROI_COMPLET);
                        ajouteEnfant(statsPourUnDossier.get(indexDecisionValide), TypeStat.OCTROI_COMPLET);
                    }
                }
                break;
            case OCTROI_PARTIEL:
                if (!isRevExtra
                        || isDecisionPrecedenteTypeFournit(statsPourUnDossier, indexDecisionValide,
                                CSTypeDecision.OCTROI_COMPLET.getCodeSystem())) {
                    ajouteStat(statsPourUnDossier.get(indexDecisionValide), TypeStat.OCTROI_PARTIEL);
                    ajouteEnfant(statsPourUnDossier.get(indexDecisionValide), TypeStat.OCTROI_PARTIEL);
                }
                break;
            case REFUS_SANS_CALCUL:
                ajouteStat(statsPourUnDossier.get(indexDecisionValide), TypeStat.REFUS_SANS_CALCUL);
                break;
            default:
                break;
        }
    }

    /**
     * On remplit notre objet avec les id des demandes pour lesquelles on doit compter les enfants.
     * 
     * @param statsPourUnDossier
     * @param indexDecisionValide
     * @param typeStat
     */
    private void ajouteEnfant(StatistiquesMensuellesDemPcfDec statsPourUnDossier, TypeStat typeStat) {

        String key = statsPourUnDossier.getDemande().getIdAgenceCommunale();

        if (mapIdDemandePourEnfants.containsKey(key)) {
            if (mapIdDemandePourEnfants.get(key).containsKey(typeStat)) {
                mapIdDemandePourEnfants.get(key).get(typeStat).add(statsPourUnDossier);
            } else {
                List<StatistiquesMensuellesDemPcfDec> listIdDemande = new ArrayList<StatistiquesMensuellesDemPcfDec>();
                listIdDemande.add(statsPourUnDossier);
                mapIdDemandePourEnfants.get(key).put(typeStat, listIdDemande);
            }
        } else {
            List<StatistiquesMensuellesDemPcfDec> listStat = new ArrayList<StatistiquesMensuellesDemPcfDec>();
            listStat.add(statsPourUnDossier);

            Map<TypeStat, List<StatistiquesMensuellesDemPcfDec>> mapTypeStatEtIdDemande = new HashMap<TypeStat, List<StatistiquesMensuellesDemPcfDec>>();
            mapTypeStatEtIdDemande.put(typeStat, listStat);

            mapIdDemandePourEnfants.put(key, mapTypeStatEtIdDemande);
        }
    }

    /**
     * Lance la requête et compte les enfants qui seront ajoutés dans la liste.
     * <b> Cette méthode part du principe que chaque enfant possède une date de naissance renseigné.</b>
     */
    private void comptageDesEnfants(TypeStat typeStat) {
        // Récupération de la liste contenant tous les enfants par idDemande
        Map<String, List<StatistiquesMensuellesEnfantDemande>> statsIdDemandeListEnfants = null;

        try {
            statsIdDemandeListEnfants = StatistiquesMensuellesDBNewPersistence.getListPlusJeuneEnfant(
                    mapIdDemandePourEnfants, typeStat);
        } catch (JadeApplicationServiceNotAvailableException e) {
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }

        String dateDuJour = JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now()));
        if ((!statsIdDemandeListEnfants.isEmpty()) && (statsIdDemandeListEnfants != null)) {
            for (String key : statsIdDemandeListEnfants.keySet()) {
                String dateNaissance = statsIdDemandeListEnfants.get(key).get(0).getDateNaissance();
                int ageEnfant = -1;
                if (!JadeStringUtil.isEmpty(dateNaissance)) {
                    ageEnfant = JadeDateUtil.getNbYearsBetween(dateNaissance, dateDuJour);
                }

                if (ageEnfant >= 0) {
                    StatistiquesMensuellesEnfantDemande sm = statsIdDemandeListEnfants.get(key).get(0);
                    if (sm != null) {
                        if (TypeStat.OCTROI_COMPLET.equals(typeStat)) {
                            if (ageEnfant < 6) {
                                ajouteStat(sm, TypeStat.ENFANTS_OCTROI_MOINS_6);
                            } else {
                                ajouteStat(sm, TypeStat.ENFANTS_OCTROI_PLUS_6);
                            }
                        } else if (TypeStat.OCTROI_PARTIEL.equals(typeStat)) {
                            if (ageEnfant < 6) {
                                ajouteStat(sm, TypeStat.ENFANTS_PARTIEL_MOINS_6);
                            } else {
                                ajouteStat(sm, TypeStat.ENFANTS_PARTIEL_PLUS_6);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * @param listDeStatsPourUnDossier liste contenant les statistiques de type            {@StatistiquesMensuellesDemPcfDec
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * }
     * @param indexDecisionValide {@int} index inquant quelle est la statistique à prendre.
     * @return {@boolean} indiquant <b>true</b> si on a affaire à une révision de demande de type
     *         extraordinaire ou pas <b>false</b>.
     */
    private boolean isRevisionExtraordinaire(List<StatistiquesMensuellesDemPcfDec> listDeStatsPourUnDossier,
            int indexDecisionValide) {
        boolean isRevExtra;
        isRevExtra = CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(
                listDeStatsPourUnDossier.get(indexDecisionValide).getDemande().getTypeDemande());
        if (isRevExtra) {
            ajouteStat(listDeStatsPourUnDossier.get(indexDecisionValide), TypeStat.REVISION_EXTRAORDINAIRE);
        }
        return isRevExtra;
    }

    /**
     * @param statsPourUnDossier {@link List}<{@link StatistiquesMensuellesDemPcfDec}>
     * @param indexDecisionValide {@link int}
     * @param codeSystem {@link String} Correspond au code system de décision voir {@link CSTypeDecision}
     * @return <b>true</b> si la décision précédente correspond au type de décision fournit en paramètre. <b>false</b>
     *         dans tout les autres cas. {@link Boolean}
     */
    private boolean isDecisionPrecedenteTypeFournit(List<StatistiquesMensuellesDemPcfDec> statsPourUnDossier,
            int indexDecisionValide, String codeSystem) {

        Decision decision = StatistiquesMensuellesDBNewPersistence.getDecisionPrecedente(statsPourUnDossier.get(
                indexDecisionValide).getDemande());
        if (decision == null) {
            return false;
        }
        if (codeSystem.equals(decision.getSimpleDecision().getCsTypeDecision())) {
            return true;
        }
        return false;
    }

    /**
     * @param date de type {@link String} au format DD.MM.AAAA
     * @return <b>true</b> si la date se trouve dans le créneau ou <b> false </b> si la date ne se trouve pas dans le
     *         créneau.
     */
    private boolean dateDansLeCreneau(String date) {
        try {
            date = date.substring(3);
            return date.equals(moisDebut)
                    || date.equals(moisFin)
                    || (JadeDateUtil.isDateMonthYearBefore(date, moisFin) && JadeDateUtil.isDateMonthYearAfter(date,
                            moisDebut));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(date);
        }
        return false;
    }
}