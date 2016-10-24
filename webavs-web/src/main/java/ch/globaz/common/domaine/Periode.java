package ch.globaz.common.domaine;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <p>
 * Wrapper (invariable) de deux date (au format MM.AAAA ou JJ.MM.AAAA) permettant de faire des comparaisons entre
 * périodes et d'avoir un tri "naturel" dans des conteneur standard Java implémentant ce fonctionnement tel que
 * {@link SortedSet} ou avec les utilitaires Java tel que {@link Collections#sort(java.util.List)}
 * </p>
 * <p>
 * Si le tri implémenté dans cette classe ne vous convient pas, il est possible de faire votre propre {@link Comparator
 * Comparator&lt;PRPeriodeUtils&gt;} et de le passer en paramètre au {@link SortedSet} à la construction (ou dans
 * l'utilitaire {@link Collections#sort(java.util.List, Comparator)}). Votre comparateur écrasera la comparaison faites
 * dans cette classe.
 * </p>
 */
public class Periode implements Comparable<Periode>, Serializable {
    public static Periode EMPTY = new Periode("0", "0");

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public boolean isEmpty() {
        return this == EMPTY;
    }

    /**
     * Contient les différentes valeurs de retour possible pour la comparaison de deux période entre elles
     * 
     * @see {@link Periode#comparerChevauchement(Periode)}
     */
    public static enum ComparaisonDePeriode {
        /**
         * Est retourné par la méthode {@link PRPeriodeUtils#comparerChevauchement(PRPeriodeUtils)} si les périodes ont
         * en commun un laps de temps
         */
        LES_PERIODES_SE_CHEVAUCHENT,
        /**
         * Est retourné par la méthode {@link PRPeriodeUtils#comparerChevauchement(PRPeriodeUtils)} si les périodes se
         * suivent sans discontinuité
         */
        LES_PERIODES_SE_SUIVENT,
        /**
         * Est retourné par la méthode {@link PRPeriodeUtils#comparerChevauchement(PRPeriodeUtils)} si les périodes
         * n'ont pas de laps de temps en commun
         */
        LES_PERIODES_SONT_INDEPENDANTES
    }

    public static enum StrategieConversionDateMoisAnnee {
        /**
         * Les dates au format MM.AAAA seront transformées comme suit :
         * <ul>
         * <li>Date de début : 01.2000 -> 31.01.2000</li>
         * <li>Date de fin : 01.2000 -> 31.01.2000</li>
         * </ul>
         */
        DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS,
        /**
         * Les dates au format MM.AAAA seront transformées comme suit :
         * <ul>
         * <li>Date de début : 01.2000 -> 31.01.2000</li>
         * <li>Date de fin : 01.2000 -> 01.01.2000</li>
         * </ul>
         */
        DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS,
        /**
         * Les dates au format MM.AAAA seront transformées comme suit :
         * <ul>
         * <li>Date de début : 01.2000 -> 01.01.2000</li>
         * <li>Date de fin : 01.2000 -> 01.01.2000</li>
         * </ul>
         */
        DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS,
        /**
         * Les dates au format MM.AAAA seront transformées comme suit :
         * <ul>
         * <li>Date de début : 01.2000 -> 01.01.2000</li>
         * <li>Date de fin : 01.2000 -> 31.01.2000</li>
         * </ul>
         * <strong>!!! Sera utilisée comme stratégie par défaut</strong>
         */
        DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS
    }

    /**
     * Type de période, JJ.MM.AAAA ou MM.AAAA
     */
    public static enum TypePeriode {
        JOUR_MOIS_ANNEE,
        MOIS_ANNEE
    }

    public static final String DATE_MAX = "31.12.2999";
    public static final String DATE_MIN = "01.01.1970";
    public static final String MOIS_MAX = "12.2999";
    public static final String MOIS_MIN = "01.1970";

    private static SimpleDateFormat ddsMMsyyyy = new SimpleDateFormat("dd.MM.yyyy");

    private static final StrategieConversionDateMoisAnnee STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT = StrategieConversionDateMoisAnnee.DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS;

    /**
     * <p>
     * Permet de convertir une date de début de période (si au format MM.AAAA) grâce à la stratégie de conversion passée
     * en paramètre.<br/>
     * Si la date est déjà une date complète (JJ.MM.AAAA) elle sera retournée telle quelle.
     * </p>
     * <p>
     * Si la stratégie n'est pas définie (=<code>null</code>), la stratégie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera appliquée par
     * défaut
     * </p>
     * <p>
     * Si la date passée en paramètre n'est pas valide, une chaîne vide sera retournée.
     * </p>
     * 
     * @param dateDebut
     *            la date de début de période à convertir
     * @param strategie
     *            la stratégie à appliquer si la date passée en paramètre est une date MM.AAAA
     * @return une date au format JJ.MM.AAAA
     */
    static String convertDateDebut(final String dateDebut, final StrategieConversionDateMoisAnnee strategie) {
        if (JadeDateUtil.isGlobazDate(dateDebut)) {
            return dateDebut;
        }
        if (JadeDateUtil.isGlobazDateMonthYear(dateDebut)) {
            if (strategie == null) {
                return "01." + dateDebut;
            }
            switch (strategie) {
                case DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS:
                case DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS:
                    return JadeDateUtil.getLastDateOfMonth(dateDebut);
                case DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS:
                case DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS:
                    return "01." + dateDebut;
            }
        }
        return Periode.DATE_MIN;
    }

    /**
     * <p>
     * Permet de convertir une date de fin de période (si au format MM.AAAA) grâce à la stratégie de conversion passée
     * en paramètre.<br/>
     * Si la date est déjà une date complète (JJ.MM.AAAA) elle sera retournée telle quelle.
     * </p>
     * <p>
     * Si la stratégie n'est pas définie (=<code>null</code>), la stratégie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera appliquée par
     * défaut
     * </p>
     * <p>
     * Si la date passée en paramètre n'est pas valide, une chaîne vide sera retournée.
     * </p>
     * 
     * @param dateFin
     *            la date de fin de période à convertir
     * @param strategie
     *            la stratégie à appliquer si la date passée en paramètre est une date MM.AAAA
     * @return une date au format JJ.MM.AAAA
     */
    static String convertDateFin(final String dateFin, final StrategieConversionDateMoisAnnee strategie) {
        if (JadeDateUtil.isGlobazDate(dateFin)) {
            return dateFin;
        }
        if (JadeDateUtil.isGlobazDateMonthYear(dateFin)) {
            if (strategie == null) {
                return JadeDateUtil.getLastDateOfMonth(dateFin);
            }
            switch (strategie) {
                case DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS:
                case DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS:
                    return JadeDateUtil.getLastDateOfMonth(dateFin);
                case DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS:
                case DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS:
                    return "01." + dateFin;
            }
        }
        return Periode.DATE_MAX;
    }

    /**
     * <p>
     * Retourne une période représentant le laps de temps commun au deux périodes passées en paramètre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>.
     * </p>
     * <p>
     * Appliquera la stratégie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} s'il y a des
     * périodes de type MM.AAAA
     * </p>
     * 
     * @param unePeriode
     * @param uneAutrePeriode
     * @return une période représentant le laps de temps commun aux deux périodes, ou <code>null</code> si aucun laps de
     *         temps commun
     */
    public static Periode intersection(final Periode unePeriode, final Periode uneAutrePeriode) {
        return Periode.intersection(unePeriode, uneAutrePeriode,
                Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT);
    }

    /**
     * <p>
     * Retourne une période représentant le laps de temps commun au deux périodes passées en paramètre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>
     * </p>
     * <p>
     * Appliquera la stratégie passée en paramètre s'il y a des périodes de type MM.AAAA<br/>
     * Retournera <code>null</code> si la stratégie n'est pas définie.
     * </p>
     * 
     * @param unePeriode
     * @param uneAutrePeriode
     * @param strategiePourLesDatesMoisAnnee
     * @return une période représentant le laps de temps commun aux deux périodes, ou <code>null</code> si aucun laps de
     *         temps commun
     */
    public static Periode intersection(final Periode unePeriode, final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategiePourLesDatesMoisAnnee) {
        return unePeriode.intersection(uneAutrePeriode, strategiePourLesDatesMoisAnnee);
    }

    private final String dateDebut;
    private final String dateFin;
    private TypePeriode type;

    /**
     * <p>
     * Construit une période et défini automatique le {@link TypePeriode}.
     * </p>
     * <p>
     * Les deux dates doivent être du même format (à savoir soit MM.AAAA ou JJ.MM.AAAA).
     * </p>
     * 
     * @param dateDebut
     *            une date au format MM.AAAA ou JJ.MM.AAAA (les deux dates doivent avoir le même format)
     * @param dateFin
     *            une date au format MM.AAAA. ou JJ.MM.AAAA (les deux dates doivent avoir le même format)
     */
    public Periode(final String dateDebut, final String dateFin) {
        Checkers.checkNotNull(dateDebut, "periode.dateDebut");

        if (!JadeStringUtil.isBlankOrZero(dateDebut)) {
            if (JadeDateUtil.isGlobazDate(dateDebut)) {
                type = TypePeriode.JOUR_MOIS_ANNEE;
            } else {
                type = TypePeriode.MOIS_ANNEE;
            }
        } else if (!JadeStringUtil.isBlankOrZero(dateFin)) {
            if (JadeDateUtil.isGlobazDate(dateFin)) {
                type = TypePeriode.JOUR_MOIS_ANNEE;
            } else {
                type = TypePeriode.MOIS_ANNEE;
            }
        } else {
            type = TypePeriode.JOUR_MOIS_ANNEE;
        }
        this.dateDebut = dateDebut;
        if (dateFin == null) {
            this.dateFin = "";
        } else {
            this.dateFin = dateFin;
        }
    }

    /**
     * <p>
     * Évalue deux périodes afin de savoir s'il y a chevauchement, continuité ou aucun lien entres ces périodes.
     * </p>
     * <p>
     * Retours possible :
     * <ul>
     * <li>
     * {@link ComparaisonDePeriode#LES_PERIODES_SE_CHEVAUCHENT}</li>
     * <li>
     * {@link ComparaisonDePeriode#LES_PERIODES_SE_SUIVENT}</li>
     * <li>
     * {@link ComparaisonDePeriode#LES_PERIODES_SONT_INDEPENDANTES}</li>
     * </ul>
     * </p>
     * <p>
     * La comparaison se fait au jour près.<br/>
     * Pour qu'une période en suive une autre, il faut que la date de début de la deuxième soit le suivant de la date de
     * fin de la première :
     * <ul>
     * <li><strong>[01.01.2000 - 15.01.2000] [15.01.2000 - 31.01.2000]</strong> : les périodes se chevauchent (un jour
     * commun)</li>
     * <li><strong>[01.01.2000 - 15.01.2000] [16.01.2000 - 31.01.2000]</strong> : les périodes se suivent</li>
     * <li><strong>[01.01.2000 - 15.01.2000] [17.01.2000 - 31.01.2000]</strong> : les périodes sont indépendantes (trou
     * d'un jour)</li>
     * </ul>
     * </p>
     * <p>
     * Si comparaison hybride, utilise par défaut la stratégie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période à évaluer
     * @return une valeur de l'énuméré {@link ComparaisonDePeriode}
     */
    public ComparaisonDePeriode comparerChevauchement(final Periode uneAutrePeriode) {
        return this.comparerChevauchement(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT);

    }

    /**
     * Permet de comparer le chevauchement (au jour près) entre les périodes en définissant une stratégie pour la
     * conversion des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la période à évaluer
     * @param strategiePourLesDatesMoisAnnee
     *            la stratégie à appliquer pour la conversion des périodes MM.AAAA
     * @return une valeur de l'énuméré {@link ComparaisonDePeriode}
     * @see #comparerChevauchementMois(Periode)
     */
    public ComparaisonDePeriode comparerChevauchement(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategiePourLesDatesMoisAnnee) {
        return this.comparerChevauchement(uneAutrePeriode, strategiePourLesDatesMoisAnnee, false);
    }

    /**
     * Permet de comparer le chevauchement entre les périodes (au jour près ou au mois près) en définissant une
     * stratégie pour la conversion des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la période à évaluer
     * @param strategiePourLesDatesMoisAnnee
     *            la stratégie à appliquer pour la conversion des périodes MM.AAAA
     * @param lesPeriodesSeSuiventSiMoisSuivant
     *            défini si l'écart minimal d'un chevauchement est un jour (<code>false</code>) ou un mois (
     *            <code>true</code>)
     * @return une valeur de l'énuméré {@link ComparaisonDePeriode}
     */
    private ComparaisonDePeriode comparerChevauchement(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategiePourLesDatesMoisAnnee,
            final boolean lesPeriodesSeSuiventSiMoisSuivant) {

        String dateDebut = Periode.convertDateDebut(getDateDebut(), strategiePourLesDatesMoisAnnee);
        String dateFin = Periode.convertDateFin(getDateFin(), strategiePourLesDatesMoisAnnee);
        String dateDebutAutrePeriode = Periode.convertDateDebut(uneAutrePeriode.getDateDebut(),
                strategiePourLesDatesMoisAnnee);
        String dateFinAutrePeriode = Periode.convertDateFin(uneAutrePeriode.getDateFin(),
                strategiePourLesDatesMoisAnnee);

        switch (this.compareTo(uneAutrePeriode)) {
            case 0:
                return ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
            case -1:
                // cas où la date de début de cette période est plus petite (plus ancienne) que celle de l'autre période
                // ou que les dates de début sont égales et que la date de fin est plus petite (plus ancienne)
                // que celle de l'autre période
                if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                    // si les dates de début sont les mêmes, il y a obligatoirement chevauchement
                    return ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
                } else {
                    if (JadeDateUtil.isDateAfter(dateFin, dateDebutAutrePeriode)
                            || JadeDateUtil.areDatesEquals(dateFin, dateDebutAutrePeriode)) {
                        // cas suivant :
                        // *****[.....this.....]***************
                        // ***************[....??????**********
                        return ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
                    } else {

                        if (lesPeriodesSeSuiventSiMoisSuivant) {
                            switch (JadeDateUtil.getNbMonthsBetween(JadeDateUtil.getFirstDateOfMonth(dateFin),
                                    JadeDateUtil.getFirstDateOfMonth(dateDebutAutrePeriode))) {
                                case 0:
                                    // cas suivant :
                                    // *****[.....this.....]***************
                                    // ********************[....??????*****
                                    return ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
                                case 1:
                                    // cas suivant :
                                    // *****[.....this.....]***************
                                    // *********************[....??????****
                                    return ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT;
                            }
                        } else if (JadeDateUtil.getNbDayBetween(dateFin, dateDebutAutrePeriode) <= 1) {
                            // cas suivant :
                            // *****[.....this.....]***************
                            // *********************[....??????****
                            return ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT;
                        }
                        // cas suivant :
                        // *****[.....this.....]***************
                        // ***********************[....??????**
                        return ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES;
                    }
                }
            case 1:
                // cas où la date de début de cette période est plus grande (plus récente) que celle de l'autre période
                // ou que les dates de début sont égales et que la date de fin est plus grande (plus récente)
                // que celle de l'autre période
                if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                    return ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
                } else {
                    if (JadeDateUtil.isDateBefore(dateDebut, dateFinAutrePeriode)
                            || JadeDateUtil.areDatesEquals(dateDebut, dateFinAutrePeriode)) {
                        // cas suivant :
                        // ***************[.....this.....]*****
                        // **********??????....]***************
                        return ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
                    } else {
                        if (lesPeriodesSeSuiventSiMoisSuivant) {
                            switch (JadeDateUtil.getNbMonthsBetween(
                                    JadeDateUtil.getFirstDateOfMonth(dateFinAutrePeriode),
                                    JadeDateUtil.getFirstDateOfMonth(dateDebut))) {
                                case 0:
                                    // cas suivant :
                                    // **************[.....this.....]******
                                    // ****??????....]*********************
                                    return ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
                                case 1:
                                    // cas suivant :
                                    // ***************[.....this.....]*****
                                    // ****??????....]*********************
                                    return ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT;
                            }
                        } else if (JadeDateUtil.getNbDayBetween(dateFinAutrePeriode, dateDebut) <= 1) {
                            // cas suivant :
                            // ***************[.....this.....]*****
                            // ****??????....]*********************
                            return ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT;
                        }
                    }
                    // cas suivant :
                    // ***************[.....this.....]*****
                    // **??????....]***********************
                    return ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES;
                }
        }
        return ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES;

    }

    /**
     * Permet de comparer le chevauchement entre les périodes (au mois près) avec la stratégie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} pour la conversion
     * des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la période à évaluer
     * @return une valeur de l'énuméré {@link ComparaisonDePeriode}
     */
    public ComparaisonDePeriode comparerChevauchementMois(final Periode uneAutrePeriode) {
        return this.comparerChevauchement(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT,
                true);
    }

    /**
     * Permet de comparer le chevauchement entre les périodes (au mois près) en définissant une stratégie pour la
     * conversion des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la période à évaluer
     * @param strategiePourLesDatesMoisAnnee
     *            la stratégie à appliquer pour la conversion des périodes MM.AAAA
     * @return une valeur de l'énuméré {@link ComparaisonDePeriode}
     */
    public ComparaisonDePeriode comparerChevauchementMois(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategie) {
        return this.comparerChevauchement(uneAutrePeriode, strategie, true);
    }

    /**
     * <p>
     * Permet de trier une liste de périodes en utilisant {@link Collections#sort(java.util.List)} ou de maintenir un
     * ordre de tri automatique en utilisant un {@link SortedSet} de {@link Periode}.
     * </p>
     * <p>
     * Les listes seront triées avec la période ayant la date de début la plus petite (la plus ancienne) comme première
     * période. Si deux périodes partagent la même date de début, celle ayant la date de fin la plus grande (plus
     * récente) sera en haut de la liste.
     * </p>
     * <p>
     * Utilise la stratégie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS}
     * par défaut.<br/>
     * Pour l'utilisation d'autres stratégies, utilisez {@link #compareTo(Periode, StrategieConversionDateMoisAnnee)}
     * </p>
     * <p>
     * Exemple :
     * <table>
     * <thead>
     * <tr>
     * <th width="20%" style="text-align:left;">index</th>
     * <th>------------------Temps-------------------></th>
     * </tr>
     * </thead><tbody>
     * <tr>
     * <td>0</td>
     * <td><strong> [&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;01.01.2000 -
     * 31.12.2002&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]</strong></td>
     * </tr>
     * <tr>
     * <td>1</td>
     * <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;&nbsp;&nbsp;&nbsp;01.01.2001 -
     * 31.12.2002&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]</strong></td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;&nbsp;&nbsp;&nbsp;01.01.2001 -
     * 31.12.2001&nbsp;&nbsp;&nbsp;&nbsp;]</strong></td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     */
    @Override
    public int compareTo(final Periode uneAutrePeriode) {
        return this.compareTo(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT);
    }

    /**
     * <p>
     * Permet de trier une liste de périodes en utilisant {@link Collections#sort(java.util.List)} ou de maintenir un
     * ordre de tri automatique en utilisant un {@link SortedSet} de {@link Periode}.
     * </p>
     * <p>
     * Les dates de type MM.AAAA seront traitées selon la stratégie passée en paramètre
     * </p>
     * 
     * @param uneAutrePeriode
     * @param strategiePourLesDatesMoisAnnee
     * @return
     * @see #compareTo(Periode)
     */
    public int compareTo(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategiePourLesDatesMoisAnnee) {

        String dateDebut = Periode.convertDateDebut(getDateDebut(), strategiePourLesDatesMoisAnnee);
        String dateFin = Periode.convertDateFin(getDateFin(), strategiePourLesDatesMoisAnnee);
        String dateDebutAutrePeriode = Periode.convertDateDebut(uneAutrePeriode.getDateDebut(),
                strategiePourLesDatesMoisAnnee);
        String dateFinAutrePeriode = Periode.convertDateFin(uneAutrePeriode.getDateFin(),
                strategiePourLesDatesMoisAnnee);

        if ((!JadeStringUtil.isBlank(dateDebut) || !JadeStringUtil.isBlank(dateDebutAutrePeriode))
                && !JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
            // si une des période n'a pas de début
            if (JadeStringUtil.isBlankOrZero(dateDebut)) {
                return 1;
            } else if (JadeStringUtil.isBlankOrZero(dateDebutAutrePeriode)) {
                return -1;
            }

            // sinon comparaison des dates de début
            if (JadeDateUtil.isDateAfter(dateDebut, dateDebutAutrePeriode)) {
                return 1;
            } else if (JadeDateUtil.isDateBefore(dateDebut, dateDebutAutrePeriode)) {
                return -1;
            }
        } else if (JadeDateUtil.isDateAfter(dateFin, dateFinAutrePeriode)) {
            return 1;
        } else if (JadeDateUtil.isDateBefore(dateFin, dateFinAutrePeriode)) {
            return -1;
        }
        return 0;
    }

    /**
     * <p>
     * Retourne une ou deux périodes représentant les laps de temps non-commun de cette période avec celle passée en
     * paramètre.
     * </p>
     * <p>
     * La liste retournée sera triée dans l'ordre naturel des périodes implémenté dans {@link #compareTo(Periode)}. La
     * stratégie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * utilisée pour convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la différence sur Wikipdia.org</a> dans la théorie des ensembles.
     * </p>
     * <p>
     * Note : Pour un autre tri, veuillez implémenter votre propre {@link Comparator} et utiliser la méthode
     * {@link #difference(Periode, Comparator)}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @return un {@link SortedSet} contenant la ou les périodes représentant la différence symétrique de cette période
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> difference(final Periode uneAutrePeriode) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, null);
    }

    /**
     * <p>
     * Retourne une ou deux périodes représentant les laps de temps non-commun de cette période avec celle passée en
     * paramètre.
     * </p>
     * <p>
     * La liste retournée sera triée dans l'ordre défini par le {@link Comparator} passé en paramètre.<br/>
     * Si le comparateur est <code>null</code>, l'ordre naturel (implémenté dans {@link #compareTo(Periode)}) sera
     * utilisé pour trier la liste.
     * </p>
     * <p>
     * La stratégie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * utilisé.
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la différence sur Wikipdia.org</a> dans la théorie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @param comparateur
     *            le {@link Comparator} qui servira à trier la liste retournée
     * @return un {@link SortedSet} contenant la ou les périodes représentant la différence symétrique de cette période
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> difference(final Periode uneAutrePeriode, final Comparator<Periode> comparateur) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, comparateur);
    }

    /**
     * <p>
     * Retourne une ou deux périodes représentant les laps de temps non-commun de cette période avec celle passée en
     * paramètre.
     * </p>
     * <p>
     * La liste retournée sera triée dans l'ordre naturel des périodes implémenté dans {@link #compareTo(Periode)}.<br/>
     * Pour un autre tri, veuillez implémenter votre propre {@link Comparator} et utiliser la méthode
     * {@link #difference(Periode, StrategieConversionDateMoisAnnee, Comparator)}
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la différence sur Wikipdia.org</a> dans la théorie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @param strategie
     *            la stratégie de conversion des périodes au format MM.AAAA à utiliser
     * @return un {@link SortedSet} contenant la ou les périodes représentant la différence symétrique de cette période
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> difference(final Periode uneAutrePeriode, final StrategieConversionDateMoisAnnee strategie) {
        return this.difference(uneAutrePeriode, strategie, null);
    }

    /**
     * <p>
     * Retourne une ou deux périodes représentant les laps de temps non-commun de cette période avec celle passée en
     * paramètre.
     * </p>
     * <p>
     * La liste retournée sera triée dans l'ordre défini par le {@link Comparator} passé en paramètre.<br/>
     * Si le comparateur est <code>null</code>, l'ordre naturel (implémenté dans {@link #compareTo(Periode)}) sera
     * utilisé pour trier la liste.
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la différence sur Wikipdia.org</a> dans la théorie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @param strategie
     *            la stratégie de conversion des périodes au format MM.AAAA à utiliser
     * @param comparateur
     *            le {@link Comparator} qui servira à trier la liste retournée
     * @return un {@link SortedSet} contenant la ou les périodes représentant la différence symétrique de cette période
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> difference(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategie, final Comparator<Periode> comparateur) {
        return this.difference(uneAutrePeriode, strategie, comparateur, false);
    }

    private SortedSet<Periode> difference(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategie, final Comparator<Periode> comparateur,
            final boolean differenceSymetrique) {

        SortedSet<Periode> resultat;

        if (comparateur != null) {
            resultat = new TreeSet<Periode>(comparateur);
        } else {
            resultat = new TreeSet<Periode>();
        }

        String dateDebut = Periode.convertDateDebut(getDateDebut(), strategie);
        String dateFin = Periode.convertDateFin(getDateFin(), strategie);
        String dateDebutAutrePeriode = Periode.convertDateDebut(uneAutrePeriode.getDateDebut(), strategie);
        String dateFinAutrePeriode = Periode.convertDateFin(uneAutrePeriode.getDateFin(), strategie);

        switch (this.comparerChevauchement(uneAutrePeriode, strategie)) {
            case LES_PERIODES_SE_CHEVAUCHENT:
            case LES_PERIODES_SE_SUIVENT:
                switch (this.compareTo(uneAutrePeriode, strategie)) {
                    case 1:
                        // cas où la date de début de cette période est plus grande (plus récente) que celle de l'autre
                        // période
                        // ou que les dates de début sont égales et que la date de fin est plus grande (plus récente)
                        // que celle de l'autre période
                        if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                            // cas suivant :
                            // **[..........this..........]****
                            // **[..uneAutrePeriode..]*********
                            resultat.add(new Periode(dateFinAutrePeriode, dateFin));
                        } else if (JadeDateUtil.isDateBefore(dateFin, dateFinAutrePeriode)) {
                            // cas suivant :
                            // ********[....this....]**********
                            // *****[..uneAutrePeriode..]******
                            if (differenceSymetrique) {
                                resultat.add(new Periode(dateDebutAutrePeriode, dateDebut));
                                resultat.add(new Periode(dateFin, dateFinAutrePeriode));
                            }
                        } else {
                            // cas suivant :
                            // *********[.......this........]**
                            // **[..uneAutrePeriode..]*********
                            if (differenceSymetrique) {
                                resultat.add(new Periode(dateDebutAutrePeriode, dateDebut));
                            }
                            resultat.add(new Periode(dateFinAutrePeriode, dateFin));
                        }
                        break;
                    case -1:
                        // cas où la date de début de cette période est plus petite (plus ancienne) que celle de l'autre
                        // période
                        // ou que les dates de début sont égales et que la date de fin est plus petite (plus ancienne)
                        // que celle de l'autre période
                        if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                            // cas suivant :
                            // **[.....this.....]**************
                            // **[..uneAutrePeriode..]*********
                            if (differenceSymetrique) {
                                resultat.add(new Periode(dateFin, dateFinAutrePeriode));
                            }
                        } else if (JadeDateUtil.isDateAfter(dateFin, dateFinAutrePeriode)) {
                            // cas suivant :
                            // **[..........this...........]***
                            // *****[..uneAutrePeriode..]******
                            resultat.add(new Periode(dateDebut, dateDebutAutrePeriode));
                            resultat.add(new Periode(dateFinAutrePeriode, dateFin));
                        } else {
                            // cas suivant :
                            // **[.......this........]*********
                            // **********[..uneAutrePeriode..]*
                            resultat.add(new Periode(dateDebut, dateDebutAutrePeriode));
                            if (differenceSymetrique) {
                                resultat.add(new Periode(dateFin, dateFinAutrePeriode));
                            }
                        }
                }
                break;
            case LES_PERIODES_SONT_INDEPENDANTES:
                resultat.add(new Periode(getDateDebut(), getDateFin()));
                if (differenceSymetrique) {
                    resultat.add(new Periode(uneAutrePeriode.getDateDebut(), uneAutrePeriode.getDateFin()));
                }
                break;
        }

        return resultat;
    }

    /**
     * <p>
     * Retourne une ou deux périodes représentant les laps de temps non-commun aux deux périodes (nuance importante avec
     * {@link #difference(Periode)}, voir le lien wikipedia plus bas).
     * </p>
     * <p>
     * La liste retournée sera triée dans l'ordre naturel des périodes implémenté dans {@link #compareTo(Periode)}. La
     * stratégie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * utilisée pour convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la différence symétrique sur wikipdia.org</a> dans la théorie des ensembles.
     * </p>
     * <p>
     * Note : Pour un autre tri, veuillez implémenter votre propre {@link Comparator} et utiliser la méthode
     * {@link #differenceSymetrique(Periode, Comparator)}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#916; uneAutrePeriode</strong>
     * @return un {@link SortedSet} contenant la ou les périodes représentant la différence symétrique de cette période
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> differenceSymetrique(final Periode uneAutrePeriode) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, null, true);
    }

    /**
     * <p>
     * Retourne une ou deux périodes représentant les laps de temps non-commun aux deux périodes (nuance importante avec
     * {@link #difference(Periode, Comparator)}, voir le lien wikipedia plus bas).
     * </p>
     * <p>
     * La liste retournée sera triée dans l'ordre défini dans le comparateur passé en paramètre. La stratégie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera utilisée pour
     * convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la différence symétrique sur wikipdia.org</a> dans la théorie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#916; uneAutrePeriode</strong>
     * @return un {@link SortedSet} contenant la ou les périodes représentant la différence symétrique de cette période
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> differenceSymetrique(final Periode uneAutrePeriode, final Comparator<Periode> comparator) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, comparator,
                true);
    }

    /**
     * <p>
     * Retourne une ou deux périodes représentant les laps de temps non-commun aux deux périodes (nuance importante avec
     * {@link #difference(Periode, Comparator)}, voir le lien wikipedia plus bas).
     * </p>
     * <p>
     * La liste retournée sera triée dans l'ordre défini dans le comparateur passé en paramètre. La stratégie passée en
     * paramètre sera utilisée pour convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la différence symétrique sur wikipdia.org</a> dans la théorie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#916; uneAutrePeriode</strong>
     * @param strategie
     *            la stratégie de conversion des périodes au format MM.AAAA à utiliser
     * @return un {@link SortedSet} contenant la ou les périodes représentant la différence symétrique de cette période
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> differenceSymetrique(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategie) {
        return this.difference(uneAutrePeriode, strategie, null, true);
    }

    /**
     * Égales si <code>obj</code> est un {@link Periode} et si
     * <code>this.{@link #compareTo(Periode) compareTo(obj)} == 0</code>
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Periode)) {
            return false;
        }
        return this.compareTo((Periode) obj) == 0;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public TypePeriode getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * <p>
     * Retourne une période représentant le laps de temps commun avec la période passée en paramètre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>.
     * </p>
     * <p>
     * Appliquera la stratégie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} s'il y a des
     * périodes de type MM.AAAA
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#8745; uneAutrePeriode</strong>
     * @return une période représentant le laps de temps commun avec l'autre périodes, ou <code>null</code> si aucun
     *         laps de temps commun
     */
    public Periode intersection(final Periode uneAutrePeriode) {
        return Periode.intersection(this, uneAutrePeriode);
    }

    /**
     * <p>
     * Retourne une période représentant le laps de temps commun avec la période passée en paramètre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>
     * </p>
     * <p>
     * Appliquera la stratégie passée en paramètre s'il y a des périodes de type MM.AAAA<br/>
     * Retournera <code>null</code> si la stratégie n'est pas définie.
     * </p>
     * <p>
     * Voir : <a
     * href="http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Intersection_de_deux_ensembles"
     * >L'intersection sur Wikipedia.org</a>
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#8745; uneAutrePeriode</strong>
     * @param strategiePourLesDatesMoisAnnee
     *            la stratégie de conversion des périodes au format MM.AAAA à utiliser
     * @return une période représentant le laps de temps commun avec l'autre périodes, ou <code>null</code> si aucun
     *         laps de temps commun
     */
    public Periode intersection(final Periode uneAutrePeriode, final StrategieConversionDateMoisAnnee strategie) {

        if (strategie == null) {
            return null;
        }

        String dateDebut = Periode.convertDateDebut(getDateDebut(), strategie);
        String dateFin = Periode.convertDateFin(getDateFin(), strategie);
        String dateDebutAutrePeriode = Periode.convertDateDebut(uneAutrePeriode.getDateDebut(), strategie);
        String dateFinAutrePeriode = Periode.convertDateFin(uneAutrePeriode.getDateFin(), strategie);

        ComparaisonDePeriode typeChevauchement = this.comparerChevauchement(uneAutrePeriode, strategie);
        int compareTo = this.compareTo(uneAutrePeriode, strategie);

        if (ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT.equals(typeChevauchement)) {
            String dateDebutPeriodeCommune;
            String dateFinPeriodeCommune;

            switch (compareTo) {
                case 0:
                    // cas suivant :
                    // ******[........this.......]******
                    // ******[..uneAutrePeriode..]*****
                    dateDebutPeriodeCommune = dateDebut;
                    dateFinPeriodeCommune = dateFin;
                case 1:
                    // cas où la date de début de cette période est plus grande (plus récente) que celle de l'autre
                    // période
                    // ou que les dates de début sont égales et que la date de fin est plus grande (plus récente)
                    // que celle de l'autre période

                    // cas suivant :
                    // ****************[..this..]******
                    // ***[..uneAutrePeriode..?????****
                    // ou
                    // ***[..........this.........]****
                    // ***[..uneAutrePeriode..]********
                    dateDebutPeriodeCommune = dateDebut;

                    if (JadeDateUtil.areDatesEquals(dateFin, dateFinAutrePeriode)) {
                        // cas suivant :
                        // *****************[..this..]*****
                        // ******[..uneAutrePeriode..]*****
                        dateFinPeriodeCommune = dateFin;
                    } else if (JadeDateUtil.isDateAfter(dateFin, dateFinAutrePeriode)) {
                        // cas suivant :
                        // *****************[..this..]*****
                        // **[..uneAutrePeriode..]*********
                        // ou
                        // ***[..........this.........]****
                        // ***[..uneAutrePeriode..]********
                        dateFinPeriodeCommune = dateFinAutrePeriode;
                    } else {
                        // cas suivant :
                        // ***********[..this..]***********
                        // ******[..uneAutrePeriode..]*****
                        dateFinPeriodeCommune = dateFin;
                    }
                    break;
                case -1:
                    // cas où la date de début de cette période est plus petite (plus ancienne) que celle de l'autre
                    // période
                    // ou que les dates de début sont égales et que la date de fin est plus petite (plus ancienne)
                    // que celle de l'autre période

                    // cas suivant :
                    // **[...........this..........]***
                    // *****[..uneAutrePeriode..??????*
                    // ou
                    // ******[..this..]****************
                    // ******[..uneAutrePeriode..]*****
                    dateDebutPeriodeCommune = dateDebutAutrePeriode;

                    if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                        // cas suivant :
                        // ******[..this..]****************
                        // ******[..uneAutrePeriode..]*****
                        dateFinPeriodeCommune = dateFin;
                    } else if (JadeDateUtil.isDateBefore(dateFin, dateFinAutrePeriode)) {
                        // cas suivant :
                        // **[..this..]******************
                        // ******[..uneAutrePeriode..]***
                        dateFinPeriodeCommune = dateFin;
                    } else {
                        // cas suivant :
                        // **[..........this...........]***
                        // *****[..uneAutrePeriode..]******
                        // ou
                        // **[........this..........]******
                        // *****[..uneAutrePeriode..]******
                        dateFinPeriodeCommune = dateFinAutrePeriode;
                    }
                    break;
                default:
                    dateDebutPeriodeCommune = "";
                    dateFinPeriodeCommune = "";
            }

            return new Periode(dateDebutPeriodeCommune, dateFinPeriodeCommune);
        }
        return null;
    }

    /**
     * <p>
     * Retourne une période représentant le laps de temps commun avec la période passée en paramètre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code><br/>
     * La période retournée sera exprimée en mois seulement (dates au format MM.AAAA)
     * </p>
     * <p>
     * Voir : <a
     * href="http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Intersection_de_deux_ensembles"
     * >L'intersection sur Wikipedia.org</a>
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#8745; uneAutrePeriode</strong>
     * @return une période représentant le laps de temps commun avec l'autre périodes, ou <code>null</code> si aucun
     *         laps de temps commun
     */
    public Periode intersectionMois(final Periode uneAutrePeriode) {
        Periode intersection = this.intersection(uneAutrePeriode);

        if (intersection != null) {
            String dateDebutEnMois, dateFinEnMois;

            if (JadeDateUtil.isGlobazDate(intersection.getDateDebut())) {
                dateDebutEnMois = JadeDateUtil.convertDateMonthYear(intersection.getDateDebut());
            } else if (JadeDateUtil.isGlobazDateMonthYear(intersection.getDateDebut())) {
                dateDebutEnMois = intersection.getDateDebut();
            } else {
                dateDebutEnMois = null;
            }
            if (JadeDateUtil.isGlobazDate(intersection.getDateFin())) {
                dateFinEnMois = JadeDateUtil.convertDateMonthYear(intersection.getDateFin());
            } else if (JadeDateUtil.isGlobazDateMonthYear(intersection.getDateFin())) {
                dateFinEnMois = intersection.getDateFin();
            } else {
                dateFinEnMois = null;
            }

            return new Periode(dateDebutEnMois, dateFinEnMois);
        }

        return null;
    }

    /**
     * Défini si une date, au format JJ.MM.AAAA, se trouve dans la période défini dans ce conteneur.
     * 
     * @param date
     *            une date au format JJ.MM.AAAA
     * @return <code>true</code> si la date passée en paramètre est dans la période.<br/>
     *         <code>false</code> si la date n'est pas valide ou si elle n'est pas dans la période.
     */
    public boolean isDateDansLaPeriode(final String date) {
        if (!JadeDateUtil.isGlobazDate(date)) {
            return false;
        }

        String dateDebutPeriode = Periode.convertDateDebut(dateDebut,
                Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT);
        String dateFinPeriode = Periode
                .convertDateFin(dateFin, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT);

        if (JadeDateUtil.areDatesEquals(date, dateDebutPeriode) || JadeDateUtil.areDatesEquals(date, dateFinPeriode)) {
            return true;
        }

        if (JadeDateUtil.isDateAfter(date, dateDebutPeriode) && JadeDateUtil.isDateBefore(date, dateFinPeriode)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(Periode.class.getName());
        toStringBuilder.append("(");
        toStringBuilder.append("[").append(getDateDebut()).append(" - ").append(getDateFin()).append("]");
        toStringBuilder.append(" type:").append(getType().toString());
        toStringBuilder.append(")");
        return toStringBuilder.toString();
    }

    /**
     * <p>
     * Retourne la fusion des deux périodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * <p>
     * La stratégie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * appliquée pour la conversion des dates MM.AAAA<br/>
     * Si après conversion, les périodes sont indépendantes, mais dans le même mois (ou à un mois de différence),
     * retournera <code>null</code>.<br/>
     * Pour un fonctionnement d'union mensuel, utilisez {@link #unionMois(Periode)}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @return une période représentant le laps de temps commun couvrant les deux périodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les périodes
     */
    public Periode union(final Periode uneAutrePeriode) {
        return this.union(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, false);
    }

    /**
     * <p>
     * Retourne la fusion des deux périodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @param strategie
     *            la stratégie de conversion des périodes au format MM.AAAA à utiliser
     * @return une période représentant le laps de temps commun couvrant les deux périodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les périodes
     */
    public Periode union(final Periode uneAutrePeriode, final StrategieConversionDateMoisAnnee strategie) {
        return this.union(uneAutrePeriode, strategie, false);
    }

    /**
     * <p>
     * Retourne la fusion des deux périodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @param strategie
     *            la stratégie de conversion des périodes au format MM.AAAA à utiliser
     * @param lesPeriodesSeSuiventSiMoisSuivant
     *            défini si l'écart minimal d'un chevauchement est un jour (<code>false</code>) ou un mois (
     *            <code>true</code>)
     * @return une période représentant le laps de temps commun couvrant les deux périodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les périodes
     */
    private Periode union(final Periode uneAutrePeriode, final StrategieConversionDateMoisAnnee strategie,
            final boolean lesPeriodesSeSuiventSiMoisSuivant) {

        String dateDebut = Periode.convertDateDebut(getDateDebut(), strategie);
        String dateFin = Periode.convertDateFin(getDateFin(), strategie);
        String dateDebutAutrePeriode = Periode.convertDateDebut(uneAutrePeriode.getDateDebut(), strategie);
        String dateFinAutrePeriode = Periode.convertDateFin(uneAutrePeriode.getDateFin(), strategie);

        switch (lesPeriodesSeSuiventSiMoisSuivant ? this.comparerChevauchementMois(uneAutrePeriode, strategie) : this
                .comparerChevauchement(uneAutrePeriode, strategie)) {
            case LES_PERIODES_SE_CHEVAUCHENT:
            case LES_PERIODES_SE_SUIVENT:
                switch (this.compareTo(uneAutrePeriode, strategie)) {
                    case 0:
                        // cas suivant :
                        // ******[........this.......]******
                        // ******[..uneAutrePeriode..]*****
                        return new Periode(dateDebut, dateFin);
                    case 1:
                        // cas où la date de début de cette période est plus grande (plus récente) que celle de l'autre
                        // période
                        // ou que les dates de début sont égales et que la date de fin est plus grande (plus récente)
                        // que celle de l'autre période
                        if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                            // cas suivant :
                            // ******[....this....]************
                            // ******[..uneAutrePeriode..]*****
                            return new Periode(dateDebutAutrePeriode, dateFinAutrePeriode);
                        } else if (JadeDateUtil.isDateAfter(dateFin, dateFinAutrePeriode)) {
                            // cas suivant :
                            // ***************[....this....]***
                            // ***[..uneAutrePeriode..]********
                            return new Periode(dateDebutAutrePeriode, dateFin);
                        } else {
                            // cas suivant :
                            // **********[....this....]********
                            // ******[..uneAutrePeriode..]*****
                            return new Periode(dateDebutAutrePeriode, dateFinAutrePeriode);
                        }
                    case -1:
                        // cas où la date de début de cette période est plus petite (plus ancienne) que celle de l'autre
                        // période
                        // ou que les dates de début sont égales et que la date de fin est plus petite (plus ancienne)
                        // que celle de l'autre période
                        if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                            // cas suivant :
                            // ***[..........this.........]****
                            // ***[..uneAutrePeriode..]********
                            return new Periode(dateDebut, dateFin);
                        } else if (JadeDateUtil.isDateAfter(dateFinAutrePeriode, dateFin)) {
                            // cas suivant :
                            // ***[....this....]***************
                            // *********[..uneAutrePeriode..]**
                            return new Periode(dateDebut, dateFinAutrePeriode);
                        } else {
                            // cas suivant :
                            // **[...........this...........]**
                            // *****[..uneAutrePeriode..]******
                            return new Periode(dateDebut, dateFin);
                        }
                }
            default:
                return null;
        }
    }

    /**
     * <p>
     * Retourne la fusion des deux périodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * <p>
     * La stratégie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * appliquée pour la conversion des dates MM.AAAA<br/>
     * Un écart jusqu'au mois suivant ou précédant sera considéré comme étant continue.
     * </p>
     * <p>
     * La période retournée sera exprimée en mois (dates au format MM.AAAA)
     * </p>
     * 
     * @param uneAutrePeriode
     *            la période qui sera utilisée pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @return une période représentant le laps de temps commun couvrant les deux périodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les périodes
     */
    public Periode unionMois(final Periode uneAutrePeriode) {
        Periode union = this.union(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, true);

        if (union != null) {
            String dateDebutUnion, dateFinUnion;

            if (JadeDateUtil.isGlobazDate(union.getDateDebut())) {
                dateDebutUnion = JadeDateUtil.convertDateMonthYear(union.getDateDebut());
            } else if (JadeDateUtil.isGlobazDateMonthYear(union.getDateDebut())) {
                dateDebutUnion = union.getDateDebut();
            } else {
                dateDebutUnion = "";
            }
            if (JadeDateUtil.isGlobazDate(union.getDateFin())) {
                if (Periode.DATE_MAX.equals(union.getDateFin())) {
                    dateFinUnion = "";
                } else {
                    dateFinUnion = JadeDateUtil.convertDateMonthYear(union.getDateFin());
                }
            } else if (JadeDateUtil.isGlobazDateMonthYear(union.getDateFin())) {
                dateFinUnion = union.getDateFin();
            } else {
                dateFinUnion = "";
            }

            return new Periode(dateDebutUnion, dateFinUnion);
        }

        return null;
    }

    /**
     * @param annee
     * @param noSemaine
     * @return
     */
    public static Periode resolvePeriodeByWeek(String annee, String noSemaine) {
        int anneInt = 0;
        int noSemaineInt = 0;
        if (annee != null) {
            anneInt = Integer.valueOf(annee);
        }
        if (noSemaine != null) {
            noSemaineInt = Integer.valueOf(noSemaine);
        }
        return resolvePeriodeByWeek(anneInt, noSemaineInt);
    }

    public static Periode resolvePeriodeByYear(String annee) {
        return new Periode("01." + annee, "12." + annee);
    }

    public static Periode resolvePeriodeByYear(Integer annee) {
        return new Periode("01." + annee, "12." + annee);
    }

    /**
     * Calcule une période en fonction de l'année passé en paramètre est le numéro de la semaine.
     * La période commence le lundi est ce termine le dimanche.
     * Si la valeur de l'année est egale à 0 on prend l'année courante.
     * Si la valeur de la semaien est égale à 0 on prend la semaine courante.
     * Une erreur est lancé si le numéro de semaine est supérieur à 52
     * Une erreur est aussi lancé si il y des nombres négatifs
     * 
     * @param annee
     * @param noSemaine
     * @return
     */
    public static Periode resolvePeriodeByWeek(int annee, int noSemaine) {
        Checkers.checkCantBeNegative(annee, "annee");
        Checkers.checkCantBeNegative(noSemaine, "noSemaine");
        if (noSemaine > 52) {
            throw new IllegalArgumentException("[noSemaine] can't be a superior as 52, value:" + noSemaine);
        }

        Calendar current = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        if (annee == 0) {
            cal.set(Calendar.YEAR, current.get(Calendar.YEAR));
            annee = cal.get(Calendar.YEAR);
        } else {
            cal.set(Calendar.YEAR, annee);
        }

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (noSemaine == 0) {
            cal.set(Calendar.WEEK_OF_YEAR, current.get(Calendar.WEEK_OF_YEAR));
            noSemaine = cal.get(Calendar.WEEK_OF_YEAR);
        } else {
            cal.set(Calendar.WEEK_OF_YEAR, noSemaine);
        }

        String dateDebutRapport = ddsMMsyyyy.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 6);
        String dateFinRapport = ddsMMsyyyy.format(cal.getTime());

        return new Periode(dateDebutRapport, dateFinRapport);
    }

    public boolean isDateFinEmpty() {
        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            return true;
        }
        return false;
    }

    public Integer countNbDaysIn() {
        return PRDateUtils.getNbDayBetween2(dateDebut, dateFin);
    }

    public Integer countNbDays() {
        return PRDateUtils.getNbDayBetween2(dateDebut, dateFin) + 1;
    }
}
