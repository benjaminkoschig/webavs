package globaz.pegasus.utils;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.services.AmalInterApplicationServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeIdMembresRetenus;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeIdMembresRetenusSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class AmalUtilsForDecisionsPC {

    /* Variable contenat le code html du message warning incoherenr */
    private static String amalIncoherenceDisplayMessage = null;

    /* Variable contenant le code html du message d'erreur technique à l'appel du service amal */
    private static String amalTechnicalErrorDisplayMessage = null;

    /* amal incoherent */
    private static Boolean isAmalIncoherent = false;

    /* amal en erreur */
    private static Boolean isAmalOnError = false;

    /* incoonu amal */
    private static Boolean isInconnuAmal = false;

    /* map retournée par le service amal <periode, liste de membres famille > */
    static Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal = null;

    /* map des incohérences amal <period, liset de tiers> */
    private static Map<String, ArrayList<String>> mapIncoherences = null;
    static Map<String, List<PCAccordeeIdMembresRetenus>> tiersForPeriod = null;

    /* liste des tiers incconu <idTiers,Description> */
    private static Map<String, String> tiersInconnuAmal = null;

    public static void checkAndGenerateWarningCoherenceWithAmal(String idVersionDroit, BSession session)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DecisionException, PCAccordeeException {

        if (session == null) {
            throw new DecisionException("Unable to check the amal coherence, the session passed is null!");
        }
        if (idVersionDroit == null) {
            throw new DecisionException("Unable to check the amal coherence, the id of version droit passed is null!");
        }

        // Recherche des pca avec les infos des membres familles
        PCAccordeeIdMembresRetenusSearch pcaMembreRetenusSearch = new PCAccordeeIdMembresRetenusSearch();
        pcaMembreRetenusSearch.setForIdVersionDroit(idVersionDroit);
        pcaMembreRetenusSearch = PegasusServiceLocator.getPCAccordeeService().search(pcaMembreRetenusSearch);

        // Map group by
        List<PCAccordeeIdMembresRetenus> list = PersistenceUtil.typeSearch(pcaMembreRetenusSearch,
                pcaMembreRetenusSearch.whichModelClass());
        // group-by periode
        Map<String, List<PCAccordeeIdMembresRetenus>> periodForTiers = JadeListUtil.groupBy(list,
                new Key<PCAccordeeIdMembresRetenus>() {
                    @Override
                    public String exec(PCAccordeeIdMembresRetenus e) {
                        return e.getSimplePCAccordee().getDateDebut();
                    }
                });
        // group by tiers
        AmalUtilsForDecisionsPC.tiersForPeriod = JadeListUtil.groupBy(list, new Key<PCAccordeeIdMembresRetenus>() {
            @Override
            public String exec(PCAccordeeIdMembresRetenus e) {
                return e.getMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers();
            }
        });

        // map pour amal
        Map<String, List<String>> mapAmal = new HashMap<String, List<String>>();

        // iteration sur les périodes pour construction map amal
        for (String dateDebut : periodForTiers.keySet()) {
            List<String> idTiers = new ArrayList<String>();
            // (Iteration sur les idTiers
            for (PCAccordeeIdMembresRetenus membreRetenus : periodForTiers.get(dateDebut)) {

                idTiers.add(membreRetenus.getMembreFamille().getMembreFamille().getPersonneEtendue().getTiers()
                        .getIdTiers());
            }
            mapAmal.put(dateDebut, idTiers);
        }

        String returnMsg = "";

        // Gestion exception amal
        try {
            // Récupération de la liste amal
            AmalUtilsForDecisionsPC.listeAmal = AmalInterApplicationServiceLocator.getPCCustomerService()
                    .getAmalSubsidesByPeriodes(mapAmal);

            // Map des incohérences pour génération warning
            AmalUtilsForDecisionsPC.mapIncoherences = new HashMap<String, ArrayList<String>>();
            // Map inconnu
            AmalUtilsForDecisionsPC.tiersInconnuAmal = new HashMap<String, String>();

            // Iteration sur les périodes retournées
            for (String periode : AmalUtilsForDecisionsPC.listeAmal.keySet()) {
                Map<String, List<SimpleDetailFamille>> tiers = AmalUtilsForDecisionsPC.listeAmal.get(periode);
                // si null, inconnu dans amal

                AmalUtilsForDecisionsPC.mapIncoherences.put(periode, new ArrayList<String>());

                // Iteration sur les tiers retournées
                for (String tier : tiers.keySet()) {

                    // Si null inconnu amal
                    if (tiers.get(tier) == null) {
                        if (!AmalUtilsForDecisionsPC.tiersInconnuAmal.containsKey(tier)) {
                            PersonneEtendueComplexModel tiersInconnu = (AmalUtilsForDecisionsPC.tiersForPeriod
                                    .get(tier).get(0)).getMembreFamille().getMembreFamille().getPersonneEtendue();
                            AmalUtilsForDecisionsPC.tiersInconnuAmal.put(tier,
                                    AmalUtilsForDecisionsPC.getTierDescription(tiersInconnu));
                            AmalUtilsForDecisionsPC.isInconnuAmal = true;
                        }
                    }
                    // Si liste de subsid vide, pas de subside pour la période
                    else if (tiers.get(tier).size() == 0) {
                        // PersonneEtendueComplexModel tiersIncoherent = (tiersForPeriod.get(tier).get(0))
                        // .getMembreFamille().getMembreFamille().getPersonneEtendue();
                        // String tiersAsString = AmalUtilsForDecisionsPC.getTierDescription(tiersIncoherent);
                        AmalUtilsForDecisionsPC.mapIncoherences.get(periode).add(tier);
                    }

                }

            }

            // Comparateur pour tri des périodes
            Comparator<String> periodesComparator = new PeriodAmalComparator();
            Map<String, ArrayList<String>> periodesOrdrees = new TreeMap<String, ArrayList<String>>(periodesComparator);
            periodesOrdrees.putAll(AmalUtilsForDecisionsPC.mapIncoherences);

            // return mapIncoherences;
            AmalUtilsForDecisionsPC.amalIncoherenceDisplayMessage = AmalUtilsForDecisionsPC.generateHtmlAsWarning(
                    periodesOrdrees, AmalUtilsForDecisionsPC.tiersInconnuAmal, session, false);
        } catch (Exception ex) {
            // JadeThread.logError("Error during amal validation", "pegasus.decision.amal.error");
            AmalUtilsForDecisionsPC.isAmalOnError = true;
            JadeLogger.error(null, "Problem during checking coherrence with subside amal");
            AmalUtilsForDecisionsPC.amalTechnicalErrorDisplayMessage = AmalUtilsForDecisionsPC.generateHtmlAsWarning(
                    null, null, session, true);
        }
    }

    private static String generateHtmlAsWarning(Map<String, ArrayList<String>> mapIncoherences,
            Map<String, String> tiersInconu, BSession session, boolean isAmalOnError) {

        // Recup des labels
        String lbl_amalIncoherenceTitre = session.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_INCOHERENCE_AMAL_TITRE");
        String lbl_amalIncoherenceInconnu = session.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_INCOHERENCE_AMAL_INCONNU");
        String lbl_amalIncoherencePasDeSubside = session
                .getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_INCOHERENCE_AMAL_PERIODE_NO_SUBSIDE");
        String lbl_amalIncoherencePeriodePeriodeOuverte = session
                .getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_INCOHERENCE_AMAL_DES");
        String lbl_amalIncoherenceError = session
                .getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_INCOHERENCE_AMAL_PERIODE_ERROR");
        String lbl_amalIncoherenceLink = session.getLabel("JSP_PC_PREP_DECISION_CALCUL_DE_INCOHERENCE_AMAL_LINK_AMAL");

        // Construction de la chaine
        StringBuilder message = new StringBuilder("");

        if (!isAmalOnError) {
            message.append("<div id='infoAmalIncoherence' data-g-boxmessage='type:WARN'>");
            message.append("<span>" + lbl_amalIncoherenceTitre + "</span><br>");

            // Iteration sur les tiers inconnus, si pas null
            if (tiersInconu != null) {
                for (String tier : tiersInconu.values()) {
                    message.append("<span class='spanInfoAmalIncoherence'>" + tier + " - " + lbl_amalIncoherenceInconnu
                            + "</span>");
                }
            }

            if (mapIncoherences != null) {
                // Iteration sur les périodes incohérentes
                for (String periode : mapIncoherences.keySet()) {

                    message.append("<span class='spanInfoAmalIncoherence'>" + lbl_amalIncoherencePeriodePeriodeOuverte
                            + " " + periode + "</span>");
                    message.append("<ul class='ulInfoAmalIncoherence'>");
                    // iteration sur les tiers posant problèmes
                    for (String tier : mapIncoherences.get(periode)) {
                        PersonneEtendueComplexModel tiersIncoherent = (AmalUtilsForDecisionsPC.tiersForPeriod.get(tier)
                                .get(0)).getMembreFamille().getMembreFamille().getPersonneEtendue();
                        String tiersAsString = AmalUtilsForDecisionsPC.getTierDescription(tiersIncoherent);

                        message.append("<li>" + tiersAsString + " - <span class='descIncoherence'>"
                                + lbl_amalIncoherencePasDeSubside + "</span></li>");
                        AmalUtilsForDecisionsPC.isAmalIncoherent = true;
                    }
                    message.append("</ul>");
                }
                message.append("<a href='amal'>" + lbl_amalIncoherenceLink + "</a>");
                message.append("</div>");
                // Si pas d'incoherrence on vide la chaine
                // if (!AmalUtilsForDecisionsPC.isAmalIncoherent) {
                // message = new StringBuilder("");
                // }
            }

        } else {
            message.append("<div id='infoAmalIncoherence' data-g-boxmessage='type:ERROR'>");
            message.append("<span>" + lbl_amalIncoherenceError + "</span><br>");
            message.append("<a href='amal' target='_blank'>" + lbl_amalIncoherenceLink + "</a>");
            message.append("</div>");
            AmalUtilsForDecisionsPC.isAmalIncoherent = true;
            // message.append("<span class='spanInfoAmalIncoherence'>" + lbl_amalIncoherenceError + "</span></div>");
        }

        return message.toString();

    }

    public static String getAmalIncoherenceDisplayMessage() {
        return AmalUtilsForDecisionsPC.amalIncoherenceDisplayMessage;
    }

    public static String getAmalTechnicalErrorDisplayMessage() {
        return AmalUtilsForDecisionsPC.amalTechnicalErrorDisplayMessage;
    }

    public static String getDateDecisionAmalForTiersByPeriod(String period, String idTiers) {

        // si période présente, normalement ok, mais préventif
        if (AmalUtilsForDecisionsPC.listeAmal.containsKey(period)) {
            // recherche le tiers (simpleDetailFamille)
            if (AmalUtilsForDecisionsPC.listeAmal.get(period).containsKey(idTiers)) {
                ArrayList<SimpleDetailFamille> detailListe = (ArrayList<SimpleDetailFamille>) AmalUtilsForDecisionsPC.listeAmal
                        .get(period).get(idTiers);

                if ((detailListe != null)) {
                    return (detailListe.get(0).getDateEnvoi());
                } else {
                    return null;
                }

            }
        }
        return null;
    }

    /**
     * Recherche se pour une période données le tiers passé en paramètre est incohérent pour amal
     * 
     * @param period
     * @param idTiers
     * @return
     */
    public static Boolean getIfTiersIsIncoherentWithAmalForPeriod(String period, String idTiers) {

        // Si la période est contenue dans la map d'incohérence
        if (AmalUtilsForDecisionsPC.mapIncoherences.containsKey(period)) {
            // Si le tiers recherhcé y est présent
            if (AmalUtilsForDecisionsPC.mapIncoherences.get(period).contains(idTiers)) {
                return false;
            }
        }
        return true;
    }

    public static Boolean getIfTypeDemandeByTiersForPeriodIsTypeP(String period, String idTiers)
            throws DecisionException {

        // si la liste contient la période
        if (AmalUtilsForDecisionsPC.listeAmal.containsKey(period)) {
            Map<String, List<SimpleDetailFamille>> tiers = AmalUtilsForDecisionsPC.listeAmal.get(period);
            // Si le tiers est différent de null
            if (tiers.get(idTiers) != null) {
                // Code "P"
                ArrayList<SimpleDetailFamille> detailListe = (ArrayList<SimpleDetailFamille>) AmalUtilsForDecisionsPC.listeAmal
                        .get(period).get(idTiers);
                SimpleDetailFamille detail = detailListe.get(0);
                if (detail.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        // Pas de clé pour la période
        else {
            throw new DecisionException("Periode not found for deteminate the type of demande Amal");
        }
        return null;
    }

    public static Boolean getIsAmalIncoherent() {
        return AmalUtilsForDecisionsPC.isAmalIncoherent;
    }

    public static Boolean getIsAmalOnError() {
        return AmalUtilsForDecisionsPC.isAmalOnError;
    }

    public static Boolean getIsInconnuAmal() {
        return AmalUtilsForDecisionsPC.isInconnuAmal;
    }

    private static String getTierDescription(PersonneEtendueComplexModel personne) {
        String tiersAsString = personne.getPersonneEtendue().getNumAvsActuel() + " / "
                + personne.getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2();
        return tiersAsString;

    }

    public static Boolean hasSubsideByTiersForPeriod(String period, String idTiers) {
        if (AmalUtilsForDecisionsPC.mapIncoherences.containsKey(period)) {
            if (AmalUtilsForDecisionsPC.mapIncoherences.get(period).contains(idTiers)) {
                return false;
            } else {
                return true;
            }
        } else {
            // la période n'est pas présete il y a un susbside
            return true;
        }
    }

    public static void setListeAmal(Map<String, Map<String, List<SimpleDetailFamille>>> liste) {
        AmalUtilsForDecisionsPC.listeAmal = liste;
    }

}
