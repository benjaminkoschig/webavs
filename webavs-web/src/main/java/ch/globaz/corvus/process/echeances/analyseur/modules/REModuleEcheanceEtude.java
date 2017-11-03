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
 * Module permettant de d�finir si une �ch�ance rentre dans la cat�gorie des �ch�ances d'�tude pour le mois courant <br/>
 * <br/>
 * Motifs de retour positif possibles :
 * <ul>
 * <li>{@link REMotifEcheance#EcheanceFinEtudes}</li>
 * <li>{@link REMotifEcheance#EcheanceFinEtudesDepassee}</li>
 * <li>{@link REMotifEcheance#EcheanceEtudesAucunePeriode}</li>
 * </ul>
 * Il est n�cessaire de passer les testes unitaires (REModuleEcheanceEtudeTest dans le projet __TestJUnit) si une
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

        // si le tiers n'a pas de date de naissance d�finie, ou une date de d�c�s, aucune �ch�ance � retenir
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

        // Tri des rentes du tiers par p�riode
        Collections.sort(rentesPourEnfant, new Comparator<IRERenteEcheances>() {
            @Override
            public int compare(IRERenteEcheances o1, IRERenteEcheances o2) {
                JadePeriodWrapper periodeRente1 = new JadePeriodWrapper(o1.getDateDebutDroit(), o1.getDateFinDroit());
                JadePeriodWrapper periodeRente2 = new JadePeriodWrapper(o2.getDateDebutDroit(), o2.getDateFinDroit());
                return periodeRente2.compareTo(periodeRente1);
            }
        });

        // si le tiers n'as pas entre 18 et 24 ans, ou s'il n'est pas b�n�ficiaire d'une compl�mentaire
        // pour enfant, aucune �ch�ance d'�tude � retenir

        if ((rentesPourEnfant.size() == 0) || (nbMoisVieDuTiers <= (18 * 12)) || (nbMoisVieDuTiers > (25 * 12))
                || !shouldBeTestedForStudies(nbMoisVieDuTiers, echeancesPourUnTiers)) {
            return REReponseModuleAnalyseEcheance.Faux;
        } else {

            IRERenteEcheances rentePourEnfantLaPlusRecente = rentesPourEnfant.get(0);

            // si pas de p�riode, motif aucune p�riode trouv�e
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

                // si la p�riode n'est pas une p�riode d'�tude, on l'ignore
                if (!ISFPeriode.CS_TYPE_PERIODE_ETUDE.equals(unePeriode.getCsTypePeriode())) {
                    continue;
                }

                if (JadeDateUtil.isDateMonthYearAfter(getMoisTraitement(),
                        JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))
                        && JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    // si date d'�ch�ance de la rente dans le mois de traitement, motif �ch�ance forc�e
                    if (JadeDateUtil.areDatesEquals("01." + getMoisTraitement(),
                            "01." + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                        return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                REMotifEcheance.EcheanceForcee, echeancesPourUnTiers.getIdTiers());
                    }

                    // si la p�riode chevauche le mois de traitement, aucune �ch�ance � retenir
                    return REReponseModuleAnalyseEcheance.Faux;
                }

                // si une p�riode commence dans le mois de traitement, et que la date de fin n'est pas dans le mois de
                // traitement, aucune �ch�ance � retenir
                if (getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))
                        && JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    return REReponseModuleAnalyseEcheance.Faux;
                }

                // si pas de date de d�but, et que la date de fin est plus �loign� que le mois de traitement,
                // aucune �ch�ance � retenir
                if (JadeStringUtil.isBlankOrZero(unePeriode.getDateDebut())
                        && JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    return REReponseModuleAnalyseEcheance.Faux;
                }

                // si la date de fin de la p�riode est dans le mois de traitement
                if (getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    if (iteratorDesPeriodes.hasNext()) {
                        // si d'autre p�riode, on va v�rifier qu'il n'y a pas de trou
                        periodeFinissantDansLeMoisDeTraitement = unePeriode;
                    } else {
                        // si date d'�ch�ance de la rente ult�rieur au mois de traitement, on ne sort rien
                        if (JadeDateUtil.isDateBefore("01." + getMoisTraitement(),
                                "01." + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                            return REReponseModuleAnalyseEcheance.Faux;
                        }

                        // si la rente couvrant la p�riode d'�tude se terminant dans le mois est d�j� diminu�e il n'y a
                        // rien � faire
                        if (getMoisTraitement().equals(rentePourEnfantLaPlusRecente.getDateFinDroit())) {
                            return REReponseModuleAnalyseEcheance.Faux;
                        }

                        // si pas d'autre p�riode, motif fin d'�tude
                        return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                REMotifEcheance.EcheanceFinEtudes, echeancesPourUnTiers.getIdTiers());
                    }
                }

                // si la p�riode d�bute avant ou apr�s le mois de traitement et que c'est la seul p�riode
                if ((echeancesPourUnTiers.getPeriodes().size() == 1)
                        && (JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut())) && JadeDateUtil
                                .isDateMonthYearBefore(getMoisTraitement(),
                                        JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut())))) {
                    return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                            REMotifEcheance.EcheanceEtudesAucunePeriode, echeancesPourUnTiers.getIdTiers());
                }

                // si la p�riode fini avant le mois de traitement
                if (JadeDateUtil.isDateMonthYearAfter(getMoisTraitement(),
                        JadeDateUtil.convertDateMonthYear(unePeriode.getDateFin()))) {
                    // s'il y a des p�riodes futur, on v�rifiera la pr�sence de trou � la prochaine it�ration
                    if (iteratorDesPeriodes.hasNext()) {
                        pasDePeriodeEnCoursAvecPeriodeFutur = true;
                        // on ignore le test sur les p�riodes future (car on ferait le teste sur la m�me p�riode)
                        continue;
                    } else {
                        if (JadeStringUtil.isBlank(rentePourEnfantLaPlusRecente.getDateEcheance())
                                || unePeriode.getPeriode().isDateDansLaPeriode(
                                        "01." + rentePourEnfantLaPlusRecente.getDateEcheance())
                                || JadeDateUtil.isDateAfter(unePeriode.getDateDebut(),
                                        rentePourEnfantLaPlusRecente.getDateEcheance())) {
                            // si pas de p�riode futur, et date d'�ch�ance d�j� d�pass�e (ou vide), motif : fin d'�tudes
                            // d�pass�es avec comme remarques la date de fin de la p�riode
                            return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                    REMotifEcheance.EcheanceFinEtudesDepassees, echeancesPourUnTiers.getIdTiers(), "("
                                            + unePeriode.getDateFin() + ")");
                        } else if (JadeDateUtil.isDateBefore(unePeriode.getDateFin(), "01."
                                + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                            if (JadeDateUtil.isDateMonthYearBefore(getMoisTraitement(),
                                    rentePourEnfantLaPlusRecente.getDateEcheance())) {
                                // si pas de p�riode futur, et date d'�ch�ance ult�rieur, on ne sort rien
                                return REReponseModuleAnalyseEcheance.Faux;
                            } else if (JadeDateUtil.areDatesEquals("01." + getMoisTraitement(), "01."
                                    + rentePourEnfantLaPlusRecente.getDateEcheance())) {
                                // si la date d'�ch�ance tombe dans le mois de traitement, sans p�riode ult�rieur (et
                                // sans p�riode couvrant le mois) : motif enqu�te interm�diaire
                                return REReponseModuleAnalyseEcheance.Vrai(
                                        rentePourEnfantLaPlusRecente,
                                        REMotifEcheance.EnqueteIntermediaire,
                                        echeancesPourUnTiers.getIdTiers(),
                                        "en " + rentePourEnfantLaPlusRecente.getDateEcheance() + " ("
                                                + unePeriode.getDateFin() + ")");
                            } else {
                                // cas des p�riodes pr�c�dant l'arriv�e � l'�ge de 18 ans
                                return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                        REMotifEcheance.EcheanceFinEtudesDepassees, echeancesPourUnTiers.getIdTiers());
                            }
                        }

                    }
                }

                // si une p�riode s'est fini avant le mois de traitement avec des p�riodes futurs, on v�rifie la
                // pr�sence de trou
                if (pasDePeriodeEnCoursAvecPeriodeFutur) {
                    String moisSuivante = JadeDateUtil.addMonths("01." + getMoisTraitement(), 1).substring(4);
                    // si la date de d�but de la p�riode n'est pas dans le mois courant, ou le mois suivant,
                    // il y a un trou, motif : fin d'�tudes d�pass�es
                    // avec comme remarques la date de fin de la p�riode
                    if (!getMoisTraitement().equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))
                            || moisSuivante.equals(JadeDateUtil.convertDateMonthYear(unePeriode.getDateDebut()))) {
                        return REReponseModuleAnalyseEcheance.Vrai(rentePourEnfantLaPlusRecente,
                                REMotifEcheance.EcheanceFinEtudesDepassees, echeancesPourUnTiers.getIdTiers(), "("
                                        + unePeriode.getDateFin() + ")");
                    }
                }
            } while (iteratorDesPeriodes.hasNext());

            // v�rification des trou si une p�riode se fini dans le mois de traitement
            if (periodeFinissantDansLeMoisDeTraitement != null) {
                for (IREPeriodeEcheances periodeSuivante : echeancesPourUnTiers.getPeriodes().tailSet(
                        periodeFinissantDansLeMoisDeTraitement)) {
                    if (periodeSuivante.equals(periodeFinissantDansLeMoisDeTraitement)) {
                        continue;
                    }
                    // si la date de d�but de la p�riode suivante n'est pas dans le mois courant, ou dans le mois
                    // suivant, il y a un trou, motif : �ch�ance fin d'�tude
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
        // si le tiers a 25 ans, on v�rifie s'il est list� dans les 25 ans
        if (nbMoisVieDuTiers == (25 * 12)) {

            REReponseModuleAnalyseEcheance reponse = module25ans.analyserEcheance(echeancesPourUnTiers);
            // si le cas ressort dans les listes des 25 ans, on ne le traite pas dans les �tudes
            if (reponse.isListerEcheance()) {
                shouldBeVerifiedInStudies = false;
            }
        }
        return shouldBeVerifiedInStudies;

    }

}
