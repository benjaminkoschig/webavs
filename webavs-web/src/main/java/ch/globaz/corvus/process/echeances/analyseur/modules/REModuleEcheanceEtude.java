package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Module permettant de définir si une échéance rentre dans la catégorie des échéances d'étude pour le mois courant <br/>
 * <br/>
 * Motifs de retour positif possibles :
 * <ul>
 * <li>{@link REMotifEcheance#EcheanceFinEtudes}</li>
 * <li>{@link REMotifEcheance#EcheanceFinEtudesDepassee}</li>
 * <li>{@link REMotifEcheance#EcheanceEtudesAucunePeriode}</li>
 * </ul>
 * Il est nécessaire de passer les testes unitaires (REModuleEcheanceEtudeTest dans le projet __TestJUnit) si une
 * modification est fait sur ce module.
 * 
 * @author PBA
 */
public class REModuleEcheanceEtude extends REModuleAnalyseEcheance {

    private REModuleEcheance25Ans module25ans;

    public REModuleEcheanceEtude(BISession session, String moisTraitement) {
        super(session, moisTraitement);

        module25ans = new REModuleEcheance25Ans(getSession(), moisTraitement, false);
    }

    @Override
    protected REReponseModuleAnalyseEcheance analyserEcheance(IREEcheances echeancesPourUnTiers) {

        // si le tiers n'a pas de date de naissance définie, ou une date de décès, aucune échéance à retenir
        if (JadeStringUtil.isBlankOrZero(echeancesPourUnTiers.getDateNaissanceTiers())
                || !JadeStringUtil.isBlankOrZero(echeancesPourUnTiers.getDateDecesTiers())) {
            return REReponseModuleAnalyseEcheance.Faux;
        }

        int nbMoisVieDuTiers = REModuleAnalyseEcheanceUtils.getNbMoisVie(echeancesPourUnTiers.getDateNaissanceTiers(),
                getMoisTraitement());

        List<IRERenteEcheances> rentesPourEnfant = new ArrayList<IRERenteEcheances>();
        for (IRERenteEcheances uneRenteDuTiers : echeancesPourUnTiers.getRentesDuTiers()) {
            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRenteDuTiers
                    .getCodePrestation()));

            if (codePrestation.isRenteComplementairePourEnfant()) {
                rentesPourEnfant.add(uneRenteDuTiers);
            }
        }

        // Tri des rentes du tiers par période
        Collections.sort(rentesPourEnfant, new Comparator<IRERenteEcheances>() {
            @Override
            public int compare(IRERenteEcheances o1, IRERenteEcheances o2) {
                JadePeriodWrapper periodeRente1 = new JadePeriodWrapper(o1.getDateDebutDroit(), o1.getDateFinDroit());
                JadePeriodWrapper periodeRente2 = new JadePeriodWrapper(o2.getDateDebutDroit(), o2.getDateFinDroit());
                return periodeRente2.compareTo(periodeRente1);
            }
        });

        // si le tiers n'as pas entre 18 et 24 ans, ou s'il n'est pas bénéficiaire d'une complémentaire
        // pour enfant, aucune échéance d'étude à retenir

        if ((rentesPourEnfant.size() == 0) || (nbMoisVieDuTiers <= (18 * 12)) || (nbMoisVieDuTiers > (25 * 12))
                || !shouldBeTestedForStudies(nbMoisVieDuTiers, echeancesPourUnTiers)) {
            return REReponseModuleAnalyseEcheance.Faux;
        } else {

            IRERenteEcheances rentePourEnfantLaPlusRecente = rentesPourEnfant.get(0);

            // si pas de période, motif aucune période trouvée
            if (echeancesPourUnTiers.getPeriodes().size() == 0) {
                return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                        REMotifEcheance.EcheanceEtudesAucunePeriode, echeancesPourUnTiers.getIdTiers());
            }

            Iterator<IREPeriodeEcheances> iteratorDesPeriodes = echeancesPourUnTiers.getPeriodes().iterator();
            IREPeriodeEcheances periodeFinissantDansLeMoisDeTraitement = null;
            boolean pasDePeriodeEnCoursAvecPeriodeFutur = false;
            do {
                IREPeriodeEcheances unePeriode = iteratorDesPeriodes.next();
                pasDePeriodeEnCoursAvecPeriodeFutur = false;

                // si la période n'est pas une période d'étude, on l'ignore
                if (!ISFPeriode.CS_TYPE_PERIODE_ETUDE.equals(unePeriode.getCsTypePeriode())) {
                    continue;
                }

                if (JadeDateUtil.isDateMonthYearAfter(getMoisTraitement(),
                        JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))
                        && JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    // si date d'échéance de la rente dans le mois de traitement, motif échéance forcée
                    if (JadeDateUtil.areDatesEquals("01." + getMoisTraitement(),
                            "01." + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                        return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                REMotifEcheance.EcheanceForcee, echeancesPourUnTiers.getIdTiers());
                    }

                    // si la période chevauche le mois de traitement, aucune échéance à retenir
                    return REReponseModuleAnalyseEcheance.Faux;
                }

                // si une période commence dans le mois de traitement, et que la date de fin n'est pas dans le mois de
                // traitement, aucune échéance à retenir
                if (getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))
                        && JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    return REReponseModuleAnalyseEcheance.Faux;
                }

                // si pas de date de début, et que la date de fin est plus éloigné que le mois de traitement,
                // aucune échéance à retenir
                if (JadeStringUtil.isBlankOrZero(unePeriode.getDateDebut())
                        && JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    return REReponseModuleAnalyseEcheance.Faux;
                }

                // si la date de fin de la période est dans le mois de traitement
                if (getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    if (iteratorDesPeriodes.hasNext()) {
                        // si d'autre période, on va vérifier qu'il n'y a pas de trou
                        periodeFinissantDansLeMoisDeTraitement = unePeriode;
                    } else {
                        // si date d'échéance de la rente ultérieur au mois de traitement, on ne sort rien
                        if (JadeDateUtil.isDateBefore("01." + getMoisTraitement(),
                                "01." + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                            return REReponseModuleAnalyseEcheance.Faux;
                        }

                        // si la rente couvrant la période d'étude se terminant dans le mois est déjà diminuée il n'y a
                        // rien à faire
                        if (getMoisTraitement().equals(rentePourEnfantLaPlusRecente.getDateFinDroit())) {
                            return REReponseModuleAnalyseEcheance.Faux;
                        }

                        // si pas d'autre période, motif fin d'étude
                        return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                REMotifEcheance.EcheanceFinEtudes, echeancesPourUnTiers.getIdTiers());
                    }
                }

                // si la période débute avant ou après le mois de traitement et que c'est la seul période
                if ((echeancesPourUnTiers.getPeriodes().size() == 1)
                        && (JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut())) && JadeDateUtil
                                .isDateMonthYearBefore(getMoisTraitement(),
                                        JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut())))) {
                    return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                            REMotifEcheance.EcheanceEtudesAucunePeriode, echeancesPourUnTiers.getIdTiers());
                }

                // si la période fini avant le mois de traitement
                if (JadeDateUtil.isDateMonthYearAfter(getMoisTraitement(),
                        JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    // s'il y a des périodes futur, on vérifiera la présence de trou à la prochaine itération
                    if (iteratorDesPeriodes.hasNext()) {
                        pasDePeriodeEnCoursAvecPeriodeFutur = true;
                        // on ignore le test sur les périodes future (car on ferait le teste sur la même période)
                        continue;
                    } else {
                        if (JadeStringUtil.isBlank(rentePourEnfantLaPlusRecente.getDateEcheance())
                                || unePeriode.getPeriode().isDateDansLaPeriode(
                                        "01." + rentePourEnfantLaPlusRecente.getDateEcheance())
                                || JadeDateUtil.isDateAfter(unePeriode.getDateDebut(),
                                        rentePourEnfantLaPlusRecente.getDateEcheance())) {
                            // si pas de période futur, et date d'échéance déjà dépassée (ou vide), motif : fin d'études
                            // dépassées avec comme remarques la date de fin de la période
                            return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                    REMotifEcheance.EcheanceFinEtudesDepassees, echeancesPourUnTiers.getIdTiers(), "("
                                            + unePeriode.getDateFin() + ")");
                        } else if (JadeDateUtil.isDateBefore(unePeriode.getDateFin(), "01."
                                + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                            if (JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                    rentePourEnfantLaPlusRecente.getDateEcheance())) {
                                // si pas de période futur, et date d'échéance ultérieur, on ne sort rien
                                return REReponseModuleAnalyseEcheance.Faux;
                            } else if (JadeDateUtil.areDatesEquals("01." + getMoisTraitement(), "01."
                                    + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                                // si la date d'échéance tombe dans le mois de traitement, sans période ultérieur (et
                                // sans période couvrant le mois) : motif enquête intermédiaire
                                return REReponseModuleAnalyseEcheance.Vrai(
                                        rentePourEnfantLaPlusRecente,
                                        REMotifEcheance.EnqueteIntermediaire,
                                        echeancesPourUnTiers.getIdTiers(),
                                        "en " + rentePourEnfantLaPlusRecente.getDateEcheance() + " ("
                                                + unePeriode.getDateFin() + ")");
                            } else {
                                // cas des périodes précédant l'arrivée à l'âge de 18 ans
                                return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                        REMotifEcheance.EcheanceFinEtudesDepassees, echeancesPourUnTiers.getIdTiers());
                            }
                        }

                    }
                }

                // si une période s'est fini avant le mois de traitement avec des périodes futurs, on vérifie la
                // présence de trou
                if (pasDePeriodeEnCoursAvecPeriodeFutur) {
                    String moisSuivante = JadeDateUtil.addMonths("01." + getMoisTraitement(), 1).substring(4);
                    // si la date de début de la période n'est pas dans le mois courant, ou le mois suivant,
                    // il y a un trou, motif : fin d'études dépassées
                    // avec comme remarques la date de fin de la période
                    if (!getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))
                            || moisSuivante.equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))) {
                        return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                REMotifEcheance.EcheanceFinEtudesDepassees, echeancesPourUnTiers.getIdTiers(), "("
                                        + unePeriode.getDateFin() + ")");
                    }
                }
            } while (iteratorDesPeriodes.hasNext());

            // vérification des trou si une période se fini dans le mois de traitement
            if (periodeFinissantDansLeMoisDeTraitement != null) {
                for (IREPeriodeEcheances periodeSuivante : echeancesPourUnTiers.getPeriodes().tailSet(
                        periodeFinissantDansLeMoisDeTraitement)) {
                    if (periodeSuivante.equals(periodeFinissantDansLeMoisDeTraitement)) {
                        continue;
                    }
                    // si la date de début de la période suivante n'est pas dans le mois courant, ou dans le mois
                    // suivant, il y a un trou, motif : échéance fin d'étude
                    String moisSuivante = JadeDateUtil.addMonths("01." + getMoisTraitement(), 1).substring(3);
                    if (!getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(periodeSuivante.getDateDebut()))
                            && !moisSuivante.equals(JadeDateUtil.convertDateMonthYear(periodeSuivante.getDateDebut()))) {
                        return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                REMotifEcheance.EcheanceFinEtudes, echeancesPourUnTiers.getIdTiers());
                    }
                }
            }
        }
        return REReponseModuleAnalyseEcheance.Faux;
    }

    private boolean shouldBeTestedForStudies(int nbMoisVieDuTiers, IREEcheances echeancesPourUnTiers) {
        boolean shouldBeVerifiedInStudies = true;
        // si le tiers a 25 ans, on vérifie s'il est listé dans les 25 ans
        if (nbMoisVieDuTiers == (25 * 12)) {

            REReponseModuleAnalyseEcheance reponse = module25ans.analyserEcheance(echeancesPourUnTiers);
            // si le cas ressort dans les listes des 25 ans, on ne le traite pas dans les études
            if (reponse.isListerEcheance()) {
                shouldBeVerifiedInStudies = false;
            }
        }
        return shouldBeVerifiedInStudies;

    }

}
