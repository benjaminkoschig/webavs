package ch.globaz.perseus.businessimpl.services.statistiquesMensuelles;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.perseus.business.statsmensuelles.Administration;
import ch.globaz.perseus.business.statsmensuelles.StatistiquesMensuellesDataMonitoringInterface;
import ch.globaz.perseus.business.statsmensuelles.TypeStat;

/**
 * @author RCO
 *         Cette classe va remplir un container de données qui sera (dans une autre classe) mappé avec un fichier Excel
 */
public class StatistiquesMensuellesDataMonitoringContainer {

    private CommonExcelmlContainer container = new CommonExcelmlContainer();
    private StatistiquesMensuellesDonnees statMensuellesDM = null;
    // ------------------------- Les totaux -----------------------------------------
    private int demandesEnregistreesHorsRITotal = 0;
    private int demandesEnregistreesRITotal = 0;

    private int decisionsOctroiHorsRITotal = 0;
    private BigDecimal montantOctroiHorsRITotal = new BigDecimal(0);
    private int decisionsOctroiRITotal = 0;
    private BigDecimal montantOctroiRITotal = new BigDecimal(0);
    private int decisionsPartielHorsRITotal = 0;
    private int decisionsPartielRITotal = 0;
    private int decisionsRefusHorsRITotal = 0;
    private int decisionsRefusRITotal = 0;
    private int decisionsNonEntreeEnMatiereTotalHorsRI = 0;
    private int decisionsNonEntreeEnMatiereTotalRI = 0;
    private int decisionsSuppressionHorsRITotal = 0;
    private int decisionsSuppressionRITotal = 0;
    private int decisionsProjetTotal = 0;
    private int decisionsRenonciationTotal = 0;
    private int revisionsExtraordinairesTotal = 0;

    // -----------------------------------------------------------------------------

    public StatistiquesMensuellesDataMonitoringContainer() {

    }

    /**
     * Remplit le container contenant les données des statistiques à afficher
     * 
     * @param statMensuellesDM {@link StatistiquesMensuellesDonnees}
     * @param moisDebut {@link String}
     * @param moisFin {@link String}
     * @return Conteneur de données {@link CommonExcelmlContainer}
     */
    public CommonExcelmlContainer getPerseusContainer(StatistiquesMensuellesDonnees statMensuellesDM, String moisDebut,
            String moisFin) {

        this.statMensuellesDM = statMensuellesDM;
        TreeMap<String, Administration> administrations = statMensuellesDM.getAdministration();
        for (Administration uneAdministration : administrations.values()) {
            Map<TypeStat, StatistiquesMensuellesDataMonitoringInterface> statsParAdministration = statMensuellesDM
                    .getStatsParAdministration(uneAdministration.getIdAdministration());

            // Localités
            container.put("ong1colA", uneAdministration.getNomAdminstration());

            // Demandes enregistrées
            int demandeHRI = ((StatistiquesMensuellesDemandesEnregistrees) statsParAdministration.get(TypeStat.DEMANDE))
                    .getHorsRI();
            container.put("ong1colB", String.valueOf(demandeHRI));
            demandesEnregistreesHorsRITotal += demandeHRI;

            int demandeRI = ((StatistiquesMensuellesDemandesEnregistrees) statsParAdministration.get(TypeStat.DEMANDE))
                    .getRI();
            container.put("ong1colC", String.valueOf(demandeRI));
            demandesEnregistreesRITotal += demandeRI;

            container.put("ong1colD",
                    String.valueOf(((StatistiquesMensuellesDemandesEnregistrees) statsParAdministration
                            .get(TypeStat.DEMANDE)).getTotal()));

            // Décision d'octroi
            int octroiHRI = ((StatistiquesMensuellesOctroi) statsParAdministration.get(TypeStat.OCTROI_COMPLET))
                    .getHorsRI();
            container.put("ong1colE", String.valueOf(octroiHRI));
            decisionsOctroiHorsRITotal += octroiHRI;

            BigDecimal octroiMontantHRI = ((StatistiquesMensuellesOctroi) statsParAdministration
                    .get(TypeStat.OCTROI_COMPLET)).getMontantHorsRI();
            montantOctroiHorsRITotal = montantOctroiHorsRITotal.add(octroiMontantHRI);
            container.put("ong1colF", String.valueOf(octroiMontantHRI));

            int octroiRI = ((StatistiquesMensuellesOctroi) statsParAdministration.get(TypeStat.OCTROI_COMPLET)).getRI();
            container.put("ong1colG", String.valueOf(octroiRI));
            decisionsOctroiRITotal += octroiRI;

            BigDecimal octroiMontantRI = ((StatistiquesMensuellesOctroi) statsParAdministration
                    .get(TypeStat.OCTROI_COMPLET)).getMontantRI();
            container.put("ong1colH", String.valueOf(octroiMontantRI));
            montantOctroiRITotal = montantOctroiRITotal.add(octroiMontantRI);

            container.put("ong1colI", String.valueOf(((StatistiquesMensuellesOctroi) statsParAdministration
                    .get(TypeStat.OCTROI_COMPLET)).getTotal()));
            container.put("ong1colJ", String.valueOf(((StatistiquesMensuellesOctroi) statsParAdministration
                    .get(TypeStat.OCTROI_COMPLET)).getMontantTotal()));

            // Décisions d'octroi partiel
            int octroiPartielHRI = ((StatistiquesMensuellesOctroiPartiel) statsParAdministration
                    .get(TypeStat.OCTROI_PARTIEL)).getHorsRI();
            container.put("ong1colK", String.valueOf(octroiPartielHRI));
            decisionsPartielHorsRITotal += octroiPartielHRI;

            int octroiPartielRI = ((StatistiquesMensuellesOctroiPartiel) statsParAdministration
                    .get(TypeStat.OCTROI_PARTIEL)).getRI();
            container.put("ong1colL", String.valueOf(octroiPartielRI));
            decisionsPartielRITotal += octroiPartielRI;

            container.put("ong1colM", String.valueOf(((StatistiquesMensuellesOctroiPartiel) statsParAdministration
                    .get(TypeStat.OCTROI_PARTIEL)).getTotal()));

            // Décisions de refus
            int refusHRI = ((StatistiquesMensuellesRefusSansCalcul) statsParAdministration
                    .get(TypeStat.REFUS_SANS_CALCUL)).getHorsRI();
            container.put("ong1colN", String.valueOf(refusHRI));
            decisionsRefusHorsRITotal += refusHRI;

            int refusRI = ((StatistiquesMensuellesRefusSansCalcul) statsParAdministration
                    .get(TypeStat.REFUS_SANS_CALCUL)).getRI();
            container.put("ong1colO", String.valueOf(refusRI));
            decisionsRefusRITotal += refusRI;

            container.put("ong1colP", String.valueOf(((StatistiquesMensuellesRefusSansCalcul) statsParAdministration
                    .get(TypeStat.REFUS_SANS_CALCUL)).getTotal()));

            // Décisions de non entrée en matière
            int nonEntreMatHRI = ((StatistiquesMensuellesNonEntreeEnMatiere) statsParAdministration
                    .get(TypeStat.NON_ENTREE_MATIERE)).getHorsRI();
            container.put("DecisionNonEntreeMatHRI", String.valueOf(nonEntreMatHRI));
            decisionsNonEntreeEnMatiereTotalHorsRI += nonEntreMatHRI;

            int nonEntreMatRI = ((StatistiquesMensuellesNonEntreeEnMatiere) statsParAdministration
                    .get(TypeStat.NON_ENTREE_MATIERE)).getRI();
            container.put("DecisionNonEntreeMatRI", String.valueOf(nonEntreMatRI));
            decisionsNonEntreeEnMatiereTotalRI += nonEntreMatRI;

            container.put("DecisionNonEntreeMatTotal", String
                    .valueOf(((StatistiquesMensuellesNonEntreeEnMatiere) statsParAdministration
                            .get(TypeStat.NON_ENTREE_MATIERE)).getTotal()));

            // Décisions de suppression volontaire
            int renSuppressionHRI = ((StatistiquesMensuellesSuppression) statsParAdministration
                    .get(TypeStat.SUPPRESSION)).getHorsRI();
            container.put("DecisionRenVolontaireHRI", String.valueOf(renSuppressionHRI));
            decisionsSuppressionHorsRITotal += renSuppressionHRI;

            int renSuppressionRI = ((StatistiquesMensuellesSuppression) statsParAdministration
                    .get(TypeStat.SUPPRESSION)).getRI();
            container.put("DecisionRenVolontaireRI", String.valueOf(renSuppressionRI));
            decisionsSuppressionRITotal += renSuppressionRI;

            container.put("DecisionRenVolontaireTotal", String
                    .valueOf(((StatistiquesMensuellesSuppression) statsParAdministration.get(TypeStat.SUPPRESSION))
                            .getTotal()));

            // Projets de décisions
            int projet = ((StatistiquesMensuellesProjet) statsParAdministration.get(TypeStat.PROJET)).getTotal();
            container.put("ProjetDecision", String.valueOf(projet));
            decisionsProjetTotal += projet;

            // Renonciations au projet de décision
            int renPrj = ((StatistiquesMensuellesRenonciation) statsParAdministration.get(TypeStat.RENONCIATION))
                    .getTotal();
            container.put("RenonciationProjetDecision", String.valueOf(renPrj));
            decisionsRenonciationTotal += renPrj;

            // Révisions extraordinaire
            int revExtra = ((StatistiquesMensuellesRevisionExtraordinaire) statsParAdministration
                    .get(TypeStat.REVISION_EXTRAORDINAIRE)).getTotal();
            container.put("RevisionExtraordinaire", String.valueOf(revExtra));
            revisionsExtraordinairesTotal += revExtra;
        }

        BSession session = BSessionUtil.getSessionFromThreadContext();

        // Dates période
        container.put("ong1Periodes",
                session.getLabel("PF_STATS_MENSUELLES_PERIODE") + moisDebut + " " + session.getLabel("PF_STATS_A")
                        + moisFin);
        container.put("ong2Periodes",
                session.getLabel("PF_STATS_MENSUELLES_PERIODE") + moisDebut + " " + session.getLabel("PF_STATS_A")
                        + moisFin);
        container.put("ong3Periodes",
                session.getLabel("PF_STATS_MENSUELLES_PERIODE") + moisDebut + " " + session.getLabel("PF_STATS_A")
                        + moisFin);
        container.put("ong4Periodes",
                session.getLabel("PF_STATS_MENSUELLES_PERIODE") + moisDebut + " " + session.getLabel("PF_STATS_A")
                        + moisFin);

        // Demandes enregistrées
        container.put("ong1colBtotal", String.valueOf(demandesEnregistreesHorsRITotal));
        container.put("ong1colCtotal", String.valueOf(demandesEnregistreesRITotal));
        container.put("ong1colDtotal", String.valueOf(demandesEnregistreesHorsRITotal + demandesEnregistreesRITotal));

        // Décisions d'octroi
        container.put("ong1colEtotal", String.valueOf(decisionsOctroiHorsRITotal));
        container.put("ong1colFtotal", String.valueOf(montantOctroiHorsRITotal));

        container.put("ong1colGtotal", String.valueOf(decisionsOctroiRITotal));
        container.put("ong1colHtotal", String.valueOf(montantOctroiRITotal));

        container.put("ong1colItotal", String.valueOf(decisionsOctroiHorsRITotal + decisionsOctroiRITotal));
        container.put("ong1colJtotal", String.valueOf(montantOctroiHorsRITotal.add(montantOctroiRITotal)));

        // Décisions d'octroi partiel
        container.put("ong1colKtotal", String.valueOf(decisionsPartielHorsRITotal));
        container.put("ong1colLtotal", String.valueOf(decisionsPartielRITotal));
        container.put("ong1colMtotal", String.valueOf(decisionsPartielHorsRITotal + decisionsPartielRITotal));

        // Décisions de refus
        container.put("ong1colNtotal", String.valueOf(decisionsRefusHorsRITotal));
        container.put("ong1colOtotal", String.valueOf(decisionsRefusRITotal));
        container.put("ong1colPtotal", String.valueOf(decisionsRefusHorsRITotal + decisionsRefusRITotal));

        // Décisions de non entrée en matière
        container.put("DecisionNonEntreeMatHRITotal", String.valueOf(decisionsNonEntreeEnMatiereTotalHorsRI));
        container.put("DecisionNonEntreeMatRITotal", String.valueOf(decisionsNonEntreeEnMatiereTotalRI));
        container.put("DecisionNonEntreeMatGrosTotal",
                String.valueOf(decisionsNonEntreeEnMatiereTotalHorsRI + decisionsNonEntreeEnMatiereTotalRI));

        // Décisions de suppression volontaire
        container.put("DecisionRenVolontaireHRITotal", String.valueOf(decisionsSuppressionHorsRITotal));
        container.put("DecisionRenVolontaireRITotal", String.valueOf(decisionsSuppressionRITotal));
        container.put("DecisionRenVolontaireGrosTotal",
                String.valueOf(decisionsSuppressionHorsRITotal + decisionsSuppressionRITotal));

        // Projets de décision
        container.put("ProjetDecisionTotal", String.valueOf(decisionsProjetTotal));

        // Renonciations au projet de décision
        container.put("RenonciationProjetDecisionTotal", String.valueOf(decisionsRenonciationTotal));

        // Révisions extraordinaire
        container.put("RevisionExtraordinaireTotal", String.valueOf(revisionsExtraordinairesTotal));

        remplireOngletTypeMenageSelonEnfants();

        return container;

    }

    private void remplireOngletTypeMenageSelonEnfants() {
        // ################ DEUXIEME ONGLET DU CLASSEUR EXCEL ####################
        int nbEnfantMoinsSixOctroiHorsRI = 0;
        BigDecimal enfantMontantMoinsSixOctroiHorsRI = new BigDecimal(0);
        BigDecimal enfantMontantMoinsSixOctroiRI = new BigDecimal(0);
        BigDecimal enfantMontantPlusSixOctroiRI = new BigDecimal(0);
        BigDecimal enfantMontantPlusSixOctroiHorsRI = new BigDecimal(0);

        int nbEnfantMoinsSixOctroiRI = 0;
        int nbEnfantPlusSixOctroiHorsRI = 0;
        int nbEnfantPlusSixOctroiRI = 0;

        int nbEnfantPartielHorsRiMoins6 = 0;
        int nbEnfantPartielRiMoins6 = 0;
        int nbEnfantPartielHorsRiPlus6 = 0;
        int nbEnfantPartielRiPlus6 = 0;

        StatistiquesMensuellesEnfantOctroiMoins6 smOctroiMoins6;
        StatistiquesMensuellesEnfantOctroiPlus6 smOctroiPlus6;
        StatistiquesMensuellesEnfantsPartielMoins6 smPartielMoins6;
        StatistiquesMensuellesEnfantsPartielPlus6 smPartielPlus6;

        for (String idAdministration : statMensuellesDM.getAdministration().keySet()) {
            smOctroiMoins6 = (StatistiquesMensuellesEnfantOctroiMoins6) statMensuellesDM.getStatsParAdministration(
                    idAdministration).get(TypeStat.ENFANTS_OCTROI_MOINS_6);
            smOctroiPlus6 = (StatistiquesMensuellesEnfantOctroiPlus6) statMensuellesDM.getStatsParAdministration(
                    idAdministration).get(TypeStat.ENFANTS_OCTROI_PLUS_6);
            smPartielMoins6 = (StatistiquesMensuellesEnfantsPartielMoins6) statMensuellesDM.getStatsParAdministration(
                    idAdministration).get(TypeStat.ENFANTS_PARTIEL_MOINS_6);
            smPartielPlus6 = (StatistiquesMensuellesEnfantsPartielPlus6) statMensuellesDM.getStatsParAdministration(
                    idAdministration).get(TypeStat.ENFANTS_PARTIEL_PLUS_6);

            enfantMontantMoinsSixOctroiRI = enfantMontantMoinsSixOctroiRI.add(smOctroiMoins6.getMontantRI());
            enfantMontantPlusSixOctroiRI = enfantMontantPlusSixOctroiRI.add(smOctroiPlus6.getMontantRI());
            enfantMontantMoinsSixOctroiHorsRI = enfantMontantMoinsSixOctroiHorsRI
                    .add(smOctroiMoins6.getMontantHorsRI());
            enfantMontantPlusSixOctroiHorsRI = enfantMontantPlusSixOctroiHorsRI.add(smOctroiPlus6.getMontantHorsRI());

            nbEnfantMoinsSixOctroiHorsRI += smOctroiMoins6.getHorsRI();
            nbEnfantMoinsSixOctroiRI += smOctroiMoins6.getRI();
            nbEnfantPlusSixOctroiHorsRI += smOctroiPlus6.getHorsRI();
            nbEnfantPlusSixOctroiRI += smOctroiPlus6.getRI();

            nbEnfantPartielHorsRiMoins6 += smPartielMoins6.getHorsRI();
            nbEnfantPartielRiMoins6 += smPartielMoins6.getRI();
            nbEnfantPartielHorsRiPlus6 += smPartielPlus6.getHorsRI();
            nbEnfantPartielRiPlus6 += smPartielPlus6.getRI();
        }

        // Décisions d'octroi
        container.put("ong2lig1colB", String.valueOf(nbEnfantMoinsSixOctroiHorsRI));
        container.put("ong2lig1colC", String.valueOf(enfantMontantMoinsSixOctroiHorsRI));
        container.put("ong2lig1colD", String.valueOf(nbEnfantMoinsSixOctroiRI));
        container.put("ong2lig1colE", String.valueOf(enfantMontantMoinsSixOctroiRI));
        container.put("ong2lig1colF", String.valueOf(nbEnfantMoinsSixOctroiHorsRI + nbEnfantMoinsSixOctroiRI));
        container.put("ong2lig1colG",
                String.valueOf(enfantMontantMoinsSixOctroiHorsRI.add(enfantMontantMoinsSixOctroiRI)));

        container.put("ong2lig2colB", String.valueOf(nbEnfantPlusSixOctroiHorsRI));
        container.put("ong2lig2colC", String.valueOf(enfantMontantPlusSixOctroiHorsRI));
        container.put("ong2lig2colD", String.valueOf(nbEnfantPlusSixOctroiRI));
        container.put("ong2lig2colE", String.valueOf(enfantMontantPlusSixOctroiRI));
        container.put("ong2lig2colF", String.valueOf(nbEnfantPlusSixOctroiHorsRI + nbEnfantPlusSixOctroiRI));
        container.put("ong2lig2colG",
                String.valueOf(enfantMontantPlusSixOctroiHorsRI.add(enfantMontantPlusSixOctroiRI)));

        container.put("ong2lig3colB", String.valueOf(nbEnfantMoinsSixOctroiHorsRI + nbEnfantPlusSixOctroiHorsRI));
        container.put("ong2lig3colC",
                String.valueOf(enfantMontantMoinsSixOctroiHorsRI.add(enfantMontantPlusSixOctroiHorsRI)));
        container.put("ong2lig3colD", String.valueOf(nbEnfantMoinsSixOctroiRI + nbEnfantPlusSixOctroiRI));
        container.put("ong2lig3colE", String.valueOf(enfantMontantMoinsSixOctroiRI.add(enfantMontantPlusSixOctroiRI)));
        container.put(
                "ong2lig3colF",
                String.valueOf(nbEnfantMoinsSixOctroiHorsRI + nbEnfantPlusSixOctroiHorsRI + nbEnfantMoinsSixOctroiRI
                        + nbEnfantPlusSixOctroiRI));
        container.put(
                "ong2lig3colG",
                String.valueOf(enfantMontantMoinsSixOctroiHorsRI.add(enfantMontantPlusSixOctroiHorsRI)
                        .add(enfantMontantMoinsSixOctroiRI).add(enfantMontantPlusSixOctroiRI)));

        // Décision d'octroi partiel
        container.put("ong2lig1colH", String.valueOf(nbEnfantPartielHorsRiMoins6));
        container.put("ong2lig1colI", String.valueOf(nbEnfantPartielRiMoins6));
        container.put("ong2lig1colJ", String.valueOf(nbEnfantPartielHorsRiMoins6 + nbEnfantPartielRiMoins6));

        container.put("ong2lig2colH", String.valueOf(nbEnfantPartielHorsRiPlus6));
        container.put("ong2lig2colI", String.valueOf(nbEnfantPartielRiPlus6));
        container.put("ong2lig2colJ", String.valueOf(nbEnfantPartielHorsRiPlus6 + nbEnfantPartielRiPlus6));

        container.put("ong2lig3colH", String.valueOf(nbEnfantPartielHorsRiMoins6 + nbEnfantPartielHorsRiPlus6));
        container.put("ong2lig3colI", String.valueOf(nbEnfantPartielRiMoins6 + nbEnfantPartielRiPlus6));
        container.put(
                "ong2lig3colJ",
                String.valueOf(nbEnfantPartielHorsRiMoins6 + nbEnfantPartielHorsRiPlus6 + nbEnfantPartielRiMoins6
                        + nbEnfantPartielRiPlus6));

    }
}
