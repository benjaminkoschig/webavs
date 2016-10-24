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
 * p�riodes et d'avoir un tri "naturel" dans des conteneur standard Java impl�mentant ce fonctionnement tel que
 * {@link SortedSet} ou avec les utilitaires Java tel que {@link Collections#sort(java.util.List)}
 * </p>
 * <p>
 * Si le tri impl�ment� dans cette classe ne vous convient pas, il est possible de faire votre propre {@link Comparator
 * Comparator&lt;PRPeriodeUtils&gt;} et de le passer en param�tre au {@link SortedSet} � la construction (ou dans
 * l'utilitaire {@link Collections#sort(java.util.List, Comparator)}). Votre comparateur �crasera la comparaison faites
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
     * Contient les diff�rentes valeurs de retour possible pour la comparaison de deux p�riode entre elles
     * 
     * @see {@link Periode#comparerChevauchement(Periode)}
     */
    public static enum ComparaisonDePeriode {
        /**
         * Est retourn� par la m�thode {@link PRPeriodeUtils#comparerChevauchement(PRPeriodeUtils)} si les p�riodes ont
         * en commun un laps de temps
         */
        LES_PERIODES_SE_CHEVAUCHENT,
        /**
         * Est retourn� par la m�thode {@link PRPeriodeUtils#comparerChevauchement(PRPeriodeUtils)} si les p�riodes se
         * suivent sans discontinuit�
         */
        LES_PERIODES_SE_SUIVENT,
        /**
         * Est retourn� par la m�thode {@link PRPeriodeUtils#comparerChevauchement(PRPeriodeUtils)} si les p�riodes
         * n'ont pas de laps de temps en commun
         */
        LES_PERIODES_SONT_INDEPENDANTES
    }

    public static enum StrategieConversionDateMoisAnnee {
        /**
         * Les dates au format MM.AAAA seront transform�es comme suit :
         * <ul>
         * <li>Date de d�but : 01.2000 -> 31.01.2000</li>
         * <li>Date de fin : 01.2000 -> 31.01.2000</li>
         * </ul>
         */
        DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS,
        /**
         * Les dates au format MM.AAAA seront transform�es comme suit :
         * <ul>
         * <li>Date de d�but : 01.2000 -> 31.01.2000</li>
         * <li>Date de fin : 01.2000 -> 01.01.2000</li>
         * </ul>
         */
        DATE_DEBUT_DERNIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS,
        /**
         * Les dates au format MM.AAAA seront transform�es comme suit :
         * <ul>
         * <li>Date de d�but : 01.2000 -> 01.01.2000</li>
         * <li>Date de fin : 01.2000 -> 01.01.2000</li>
         * </ul>
         */
        DATE_DEBUT_PERMIER_DU_MOIS_DATE_FIN_PREMIER_DU_MOIS,
        /**
         * Les dates au format MM.AAAA seront transform�es comme suit :
         * <ul>
         * <li>Date de d�but : 01.2000 -> 01.01.2000</li>
         * <li>Date de fin : 01.2000 -> 31.01.2000</li>
         * </ul>
         * <strong>!!! Sera utilis�e comme strat�gie par d�faut</strong>
         */
        DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS
    }

    /**
     * Type de p�riode, JJ.MM.AAAA ou MM.AAAA
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
     * Permet de convertir une date de d�but de p�riode (si au format MM.AAAA) gr�ce � la strat�gie de conversion pass�e
     * en param�tre.<br/>
     * Si la date est d�j� une date compl�te (JJ.MM.AAAA) elle sera retourn�e telle quelle.
     * </p>
     * <p>
     * Si la strat�gie n'est pas d�finie (=<code>null</code>), la strat�gie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera appliqu�e par
     * d�faut
     * </p>
     * <p>
     * Si la date pass�e en param�tre n'est pas valide, une cha�ne vide sera retourn�e.
     * </p>
     * 
     * @param dateDebut
     *            la date de d�but de p�riode � convertir
     * @param strategie
     *            la strat�gie � appliquer si la date pass�e en param�tre est une date MM.AAAA
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
     * Permet de convertir une date de fin de p�riode (si au format MM.AAAA) gr�ce � la strat�gie de conversion pass�e
     * en param�tre.<br/>
     * Si la date est d�j� une date compl�te (JJ.MM.AAAA) elle sera retourn�e telle quelle.
     * </p>
     * <p>
     * Si la strat�gie n'est pas d�finie (=<code>null</code>), la strat�gie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera appliqu�e par
     * d�faut
     * </p>
     * <p>
     * Si la date pass�e en param�tre n'est pas valide, une cha�ne vide sera retourn�e.
     * </p>
     * 
     * @param dateFin
     *            la date de fin de p�riode � convertir
     * @param strategie
     *            la strat�gie � appliquer si la date pass�e en param�tre est une date MM.AAAA
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
     * Retourne une p�riode repr�sentant le laps de temps commun au deux p�riodes pass�es en param�tre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>.
     * </p>
     * <p>
     * Appliquera la strat�gie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} s'il y a des
     * p�riodes de type MM.AAAA
     * </p>
     * 
     * @param unePeriode
     * @param uneAutrePeriode
     * @return une p�riode repr�sentant le laps de temps commun aux deux p�riodes, ou <code>null</code> si aucun laps de
     *         temps commun
     */
    public static Periode intersection(final Periode unePeriode, final Periode uneAutrePeriode) {
        return Periode.intersection(unePeriode, uneAutrePeriode,
                Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT);
    }

    /**
     * <p>
     * Retourne une p�riode repr�sentant le laps de temps commun au deux p�riodes pass�es en param�tre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>
     * </p>
     * <p>
     * Appliquera la strat�gie pass�e en param�tre s'il y a des p�riodes de type MM.AAAA<br/>
     * Retournera <code>null</code> si la strat�gie n'est pas d�finie.
     * </p>
     * 
     * @param unePeriode
     * @param uneAutrePeriode
     * @param strategiePourLesDatesMoisAnnee
     * @return une p�riode repr�sentant le laps de temps commun aux deux p�riodes, ou <code>null</code> si aucun laps de
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
     * Construit une p�riode et d�fini automatique le {@link TypePeriode}.
     * </p>
     * <p>
     * Les deux dates doivent �tre du m�me format (� savoir soit MM.AAAA ou JJ.MM.AAAA).
     * </p>
     * 
     * @param dateDebut
     *            une date au format MM.AAAA ou JJ.MM.AAAA (les deux dates doivent avoir le m�me format)
     * @param dateFin
     *            une date au format MM.AAAA. ou JJ.MM.AAAA (les deux dates doivent avoir le m�me format)
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
     * �value deux p�riodes afin de savoir s'il y a chevauchement, continuit� ou aucun lien entres ces p�riodes.
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
     * La comparaison se fait au jour pr�s.<br/>
     * Pour qu'une p�riode en suive une autre, il faut que la date de d�but de la deuxi�me soit le suivant de la date de
     * fin de la premi�re :
     * <ul>
     * <li><strong>[01.01.2000 - 15.01.2000] [15.01.2000 - 31.01.2000]</strong> : les p�riodes se chevauchent (un jour
     * commun)</li>
     * <li><strong>[01.01.2000 - 15.01.2000] [16.01.2000 - 31.01.2000]</strong> : les p�riodes se suivent</li>
     * <li><strong>[01.01.2000 - 15.01.2000] [17.01.2000 - 31.01.2000]</strong> : les p�riodes sont ind�pendantes (trou
     * d'un jour)</li>
     * </ul>
     * </p>
     * <p>
     * Si comparaison hybride, utilise par d�faut la strat�gie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode � �valuer
     * @return une valeur de l'�num�r� {@link ComparaisonDePeriode}
     */
    public ComparaisonDePeriode comparerChevauchement(final Periode uneAutrePeriode) {
        return this.comparerChevauchement(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT);

    }

    /**
     * Permet de comparer le chevauchement (au jour pr�s) entre les p�riodes en d�finissant une strat�gie pour la
     * conversion des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la p�riode � �valuer
     * @param strategiePourLesDatesMoisAnnee
     *            la strat�gie � appliquer pour la conversion des p�riodes MM.AAAA
     * @return une valeur de l'�num�r� {@link ComparaisonDePeriode}
     * @see #comparerChevauchementMois(Periode)
     */
    public ComparaisonDePeriode comparerChevauchement(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategiePourLesDatesMoisAnnee) {
        return this.comparerChevauchement(uneAutrePeriode, strategiePourLesDatesMoisAnnee, false);
    }

    /**
     * Permet de comparer le chevauchement entre les p�riodes (au jour pr�s ou au mois pr�s) en d�finissant une
     * strat�gie pour la conversion des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la p�riode � �valuer
     * @param strategiePourLesDatesMoisAnnee
     *            la strat�gie � appliquer pour la conversion des p�riodes MM.AAAA
     * @param lesPeriodesSeSuiventSiMoisSuivant
     *            d�fini si l'�cart minimal d'un chevauchement est un jour (<code>false</code>) ou un mois (
     *            <code>true</code>)
     * @return une valeur de l'�num�r� {@link ComparaisonDePeriode}
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
                // cas o� la date de d�but de cette p�riode est plus petite (plus ancienne) que celle de l'autre p�riode
                // ou que les dates de d�but sont �gales et que la date de fin est plus petite (plus ancienne)
                // que celle de l'autre p�riode
                if (JadeDateUtil.areDatesEquals(dateDebut, dateDebutAutrePeriode)) {
                    // si les dates de d�but sont les m�mes, il y a obligatoirement chevauchement
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
                // cas o� la date de d�but de cette p�riode est plus grande (plus r�cente) que celle de l'autre p�riode
                // ou que les dates de d�but sont �gales et que la date de fin est plus grande (plus r�cente)
                // que celle de l'autre p�riode
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
     * Permet de comparer le chevauchement entre les p�riodes (au mois pr�s) avec la strat�gie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} pour la conversion
     * des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la p�riode � �valuer
     * @return une valeur de l'�num�r� {@link ComparaisonDePeriode}
     */
    public ComparaisonDePeriode comparerChevauchementMois(final Periode uneAutrePeriode) {
        return this.comparerChevauchement(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT,
                true);
    }

    /**
     * Permet de comparer le chevauchement entre les p�riodes (au mois pr�s) en d�finissant une strat�gie pour la
     * conversion des dates MM.AAAA
     * 
     * @param uneAutrePeriode
     *            la p�riode � �valuer
     * @param strategiePourLesDatesMoisAnnee
     *            la strat�gie � appliquer pour la conversion des p�riodes MM.AAAA
     * @return une valeur de l'�num�r� {@link ComparaisonDePeriode}
     */
    public ComparaisonDePeriode comparerChevauchementMois(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategie) {
        return this.comparerChevauchement(uneAutrePeriode, strategie, true);
    }

    /**
     * <p>
     * Permet de trier une liste de p�riodes en utilisant {@link Collections#sort(java.util.List)} ou de maintenir un
     * ordre de tri automatique en utilisant un {@link SortedSet} de {@link Periode}.
     * </p>
     * <p>
     * Les listes seront tri�es avec la p�riode ayant la date de d�but la plus petite (la plus ancienne) comme premi�re
     * p�riode. Si deux p�riodes partagent la m�me date de d�but, celle ayant la date de fin la plus grande (plus
     * r�cente) sera en haut de la liste.
     * </p>
     * <p>
     * Utilise la strat�gie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS}
     * par d�faut.<br/>
     * Pour l'utilisation d'autres strat�gies, utilisez {@link #compareTo(Periode, StrategieConversionDateMoisAnnee)}
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
     * Permet de trier une liste de p�riodes en utilisant {@link Collections#sort(java.util.List)} ou de maintenir un
     * ordre de tri automatique en utilisant un {@link SortedSet} de {@link Periode}.
     * </p>
     * <p>
     * Les dates de type MM.AAAA seront trait�es selon la strat�gie pass�e en param�tre
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
            // si une des p�riode n'a pas de d�but
            if (JadeStringUtil.isBlankOrZero(dateDebut)) {
                return 1;
            } else if (JadeStringUtil.isBlankOrZero(dateDebutAutrePeriode)) {
                return -1;
            }

            // sinon comparaison des dates de d�but
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
     * Retourne une ou deux p�riodes repr�sentant les laps de temps non-commun de cette p�riode avec celle pass�e en
     * param�tre.
     * </p>
     * <p>
     * La liste retourn�e sera tri�e dans l'ordre naturel des p�riodes impl�ment� dans {@link #compareTo(Periode)}. La
     * strat�gie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * utilis�e pour convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la diff�rence sur Wikipdia.org</a> dans la th�orie des ensembles.
     * </p>
     * <p>
     * Note : Pour un autre tri, veuillez impl�menter votre propre {@link Comparator} et utiliser la m�thode
     * {@link #difference(Periode, Comparator)}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @return un {@link SortedSet} contenant la ou les p�riodes repr�sentant la diff�rence sym�trique de cette p�riode
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> difference(final Periode uneAutrePeriode) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, null);
    }

    /**
     * <p>
     * Retourne une ou deux p�riodes repr�sentant les laps de temps non-commun de cette p�riode avec celle pass�e en
     * param�tre.
     * </p>
     * <p>
     * La liste retourn�e sera tri�e dans l'ordre d�fini par le {@link Comparator} pass� en param�tre.<br/>
     * Si le comparateur est <code>null</code>, l'ordre naturel (impl�ment� dans {@link #compareTo(Periode)}) sera
     * utilis� pour trier la liste.
     * </p>
     * <p>
     * La strat�gie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * utilis�.
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la diff�rence sur Wikipdia.org</a> dans la th�orie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @param comparateur
     *            le {@link Comparator} qui servira � trier la liste retourn�e
     * @return un {@link SortedSet} contenant la ou les p�riodes repr�sentant la diff�rence sym�trique de cette p�riode
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> difference(final Periode uneAutrePeriode, final Comparator<Periode> comparateur) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, comparateur);
    }

    /**
     * <p>
     * Retourne une ou deux p�riodes repr�sentant les laps de temps non-commun de cette p�riode avec celle pass�e en
     * param�tre.
     * </p>
     * <p>
     * La liste retourn�e sera tri�e dans l'ordre naturel des p�riodes impl�ment� dans {@link #compareTo(Periode)}.<br/>
     * Pour un autre tri, veuillez impl�menter votre propre {@link Comparator} et utiliser la m�thode
     * {@link #difference(Periode, StrategieConversionDateMoisAnnee, Comparator)}
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la diff�rence sur Wikipdia.org</a> dans la th�orie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @param strategie
     *            la strat�gie de conversion des p�riodes au format MM.AAAA � utiliser
     * @return un {@link SortedSet} contenant la ou les p�riodes repr�sentant la diff�rence sym�trique de cette p�riode
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> difference(final Periode uneAutrePeriode, final StrategieConversionDateMoisAnnee strategie) {
        return this.difference(uneAutrePeriode, strategie, null);
    }

    /**
     * <p>
     * Retourne une ou deux p�riodes repr�sentant les laps de temps non-commun de cette p�riode avec celle pass�e en
     * param�tre.
     * </p>
     * <p>
     * La liste retourn�e sera tri�e dans l'ordre d�fini par le {@link Comparator} pass� en param�tre.<br/>
     * Si le comparateur est <code>null</code>, l'ordre naturel (impl�ment� dans {@link #compareTo(Periode)}) sera
     * utilis� pour trier la liste.
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la diff�rence sur Wikipdia.org</a> dans la th�orie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this \ uneAutrePeriode</strong>
     * @param strategie
     *            la strat�gie de conversion des p�riodes au format MM.AAAA � utiliser
     * @param comparateur
     *            le {@link Comparator} qui servira � trier la liste retourn�e
     * @return un {@link SortedSet} contenant la ou les p�riodes repr�sentant la diff�rence sym�trique de cette p�riode
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
                        // cas o� la date de d�but de cette p�riode est plus grande (plus r�cente) que celle de l'autre
                        // p�riode
                        // ou que les dates de d�but sont �gales et que la date de fin est plus grande (plus r�cente)
                        // que celle de l'autre p�riode
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
                        // cas o� la date de d�but de cette p�riode est plus petite (plus ancienne) que celle de l'autre
                        // p�riode
                        // ou que les dates de d�but sont �gales et que la date de fin est plus petite (plus ancienne)
                        // que celle de l'autre p�riode
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
     * Retourne une ou deux p�riodes repr�sentant les laps de temps non-commun aux deux p�riodes (nuance importante avec
     * {@link #difference(Periode)}, voir le lien wikipedia plus bas).
     * </p>
     * <p>
     * La liste retourn�e sera tri�e dans l'ordre naturel des p�riodes impl�ment� dans {@link #compareTo(Periode)}. La
     * strat�gie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * utilis�e pour convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la diff�rence sym�trique sur wikipdia.org</a> dans la th�orie des ensembles.
     * </p>
     * <p>
     * Note : Pour un autre tri, veuillez impl�menter votre propre {@link Comparator} et utiliser la m�thode
     * {@link #differenceSymetrique(Periode, Comparator)}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#916; uneAutrePeriode</strong>
     * @return un {@link SortedSet} contenant la ou les p�riodes repr�sentant la diff�rence sym�trique de cette p�riode
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> differenceSymetrique(final Periode uneAutrePeriode) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, null, true);
    }

    /**
     * <p>
     * Retourne une ou deux p�riodes repr�sentant les laps de temps non-commun aux deux p�riodes (nuance importante avec
     * {@link #difference(Periode, Comparator)}, voir le lien wikipedia plus bas).
     * </p>
     * <p>
     * La liste retourn�e sera tri�e dans l'ordre d�fini dans le comparateur pass� en param�tre. La strat�gie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera utilis�e pour
     * convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la diff�rence sym�trique sur wikipdia.org</a> dans la th�orie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#916; uneAutrePeriode</strong>
     * @return un {@link SortedSet} contenant la ou les p�riodes repr�sentant la diff�rence sym�trique de cette p�riode
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> differenceSymetrique(final Periode uneAutrePeriode, final Comparator<Periode> comparator) {
        return this.difference(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, comparator,
                true);
    }

    /**
     * <p>
     * Retourne une ou deux p�riodes repr�sentant les laps de temps non-commun aux deux p�riodes (nuance importante avec
     * {@link #difference(Periode, Comparator)}, voir le lien wikipedia plus bas).
     * </p>
     * <p>
     * La liste retourn�e sera tri�e dans l'ordre d�fini dans le comparateur pass� en param�tre. La strat�gie pass�e en
     * param�tre sera utilis�e pour convertir les dates au format MM.AAAA
     * </p>
     * <p>
     * Voir : <a href=
     * "http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Diff.C3.A9rence_et_diff.C3.A9rence_sym.C3.A9trique"
     * >la diff�rence sym�trique sur wikipdia.org</a> dans la th�orie des ensembles.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#916; uneAutrePeriode</strong>
     * @param strategie
     *            la strat�gie de conversion des p�riodes au format MM.AAAA � utiliser
     * @return un {@link SortedSet} contenant la ou les p�riodes repr�sentant la diff�rence sym�trique de cette p�riode
     *         avec uneAutrePeriode
     */
    public SortedSet<Periode> differenceSymetrique(final Periode uneAutrePeriode,
            final StrategieConversionDateMoisAnnee strategie) {
        return this.difference(uneAutrePeriode, strategie, null, true);
    }

    /**
     * �gales si <code>obj</code> est un {@link Periode} et si
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
     * Retourne une p�riode repr�sentant le laps de temps commun avec la p�riode pass�e en param�tre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>.
     * </p>
     * <p>
     * Appliquera la strat�gie
     * {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} s'il y a des
     * p�riodes de type MM.AAAA
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#8745; uneAutrePeriode</strong>
     * @return une p�riode repr�sentant le laps de temps commun avec l'autre p�riodes, ou <code>null</code> si aucun
     *         laps de temps commun
     */
    public Periode intersection(final Periode uneAutrePeriode) {
        return Periode.intersection(this, uneAutrePeriode);
    }

    /**
     * <p>
     * Retourne une p�riode repr�sentant le laps de temps commun avec la p�riode pass�e en param�tre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code>
     * </p>
     * <p>
     * Appliquera la strat�gie pass�e en param�tre s'il y a des p�riodes de type MM.AAAA<br/>
     * Retournera <code>null</code> si la strat�gie n'est pas d�finie.
     * </p>
     * <p>
     * Voir : <a
     * href="http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Intersection_de_deux_ensembles"
     * >L'intersection sur Wikipedia.org</a>
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#8745; uneAutrePeriode</strong>
     * @param strategiePourLesDatesMoisAnnee
     *            la strat�gie de conversion des p�riodes au format MM.AAAA � utiliser
     * @return une p�riode repr�sentant le laps de temps commun avec l'autre p�riodes, ou <code>null</code> si aucun
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
                    // cas o� la date de d�but de cette p�riode est plus grande (plus r�cente) que celle de l'autre
                    // p�riode
                    // ou que les dates de d�but sont �gales et que la date de fin est plus grande (plus r�cente)
                    // que celle de l'autre p�riode

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
                    // cas o� la date de d�but de cette p�riode est plus petite (plus ancienne) que celle de l'autre
                    // p�riode
                    // ou que les dates de d�but sont �gales et que la date de fin est plus petite (plus ancienne)
                    // que celle de l'autre p�riode

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
     * Retourne une p�riode repr�sentant le laps de temps commun avec la p�riode pass�e en param�tre.<br/>
     * Si aucun laps de temps commun, retourne <code>null</code><br/>
     * La p�riode retourn�e sera exprim�e en mois seulement (dates au format MM.AAAA)
     * </p>
     * <p>
     * Voir : <a
     * href="http://fr.wikipedia.org/wiki/Alg%C3%A8bre_des_parties_d%27un_ensemble#Intersection_de_deux_ensembles"
     * >L'intersection sur Wikipedia.org</a>
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#8745; uneAutrePeriode</strong>
     * @return une p�riode repr�sentant le laps de temps commun avec l'autre p�riodes, ou <code>null</code> si aucun
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
     * D�fini si une date, au format JJ.MM.AAAA, se trouve dans la p�riode d�fini dans ce conteneur.
     * 
     * @param date
     *            une date au format JJ.MM.AAAA
     * @return <code>true</code> si la date pass�e en param�tre est dans la p�riode.<br/>
     *         <code>false</code> si la date n'est pas valide ou si elle n'est pas dans la p�riode.
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
     * Retourne la fusion des deux p�riodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * <p>
     * La strat�gie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * appliqu�e pour la conversion des dates MM.AAAA<br/>
     * Si apr�s conversion, les p�riodes sont ind�pendantes, mais dans le m�me mois (ou � un mois de diff�rence),
     * retournera <code>null</code>.<br/>
     * Pour un fonctionnement d'union mensuel, utilisez {@link #unionMois(Periode)}
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @return une p�riode repr�sentant le laps de temps commun couvrant les deux p�riodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les p�riodes
     */
    public Periode union(final Periode uneAutrePeriode) {
        return this.union(uneAutrePeriode, Periode.STRATEGIE_CONVERSION_DATE_MOIS_ANNEE_PAR_DEFAUT, false);
    }

    /**
     * <p>
     * Retourne la fusion des deux p�riodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @param strategie
     *            la strat�gie de conversion des p�riodes au format MM.AAAA � utiliser
     * @return une p�riode repr�sentant le laps de temps commun couvrant les deux p�riodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les p�riodes
     */
    public Periode union(final Periode uneAutrePeriode, final StrategieConversionDateMoisAnnee strategie) {
        return this.union(uneAutrePeriode, strategie, false);
    }

    /**
     * <p>
     * Retourne la fusion des deux p�riodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @param strategie
     *            la strat�gie de conversion des p�riodes au format MM.AAAA � utiliser
     * @param lesPeriodesSeSuiventSiMoisSuivant
     *            d�fini si l'�cart minimal d'un chevauchement est un jour (<code>false</code>) ou un mois (
     *            <code>true</code>)
     * @return une p�riode repr�sentant le laps de temps commun couvrant les deux p�riodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les p�riodes
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
                        // cas o� la date de d�but de cette p�riode est plus grande (plus r�cente) que celle de l'autre
                        // p�riode
                        // ou que les dates de d�but sont �gales et que la date de fin est plus grande (plus r�cente)
                        // que celle de l'autre p�riode
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
                        // cas o� la date de d�but de cette p�riode est plus petite (plus ancienne) que celle de l'autre
                        // p�riode
                        // ou que les dates de d�but sont �gales et que la date de fin est plus petite (plus ancienne)
                        // que celle de l'autre p�riode
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
     * Retourne la fusion des deux p�riodes ou <code>null</code> si aucun laps de temps commun.
     * </p>
     * <p>
     * La strat�gie {@link StrategieConversionDateMoisAnnee#DATE_DEBUT_PREMIER_DU_MOIS_DATE_FIN_DERNIER_DU_MOIS} sera
     * appliqu�e pour la conversion des dates MM.AAAA<br/>
     * Un �cart jusqu'au mois suivant ou pr�c�dant sera consid�r� comme �tant continue.
     * </p>
     * <p>
     * La p�riode retourn�e sera exprim�e en mois (dates au format MM.AAAA)
     * </p>
     * 
     * @param uneAutrePeriode
     *            la p�riode qui sera utilis�e pour appliquer <strong>this &#8746; uneAutrePeriode</strong>
     * @return une p�riode repr�sentant le laps de temps commun couvrant les deux p�riodes, ou <code>null</code> si
     *         aucun laps de temps commun entre les p�riodes
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
     * Calcule une p�riode en fonction de l'ann�e pass� en param�tre est le num�ro de la semaine.
     * La p�riode commence le lundi est ce termine le dimanche.
     * Si la valeur de l'ann�e est egale � 0 on prend l'ann�e courante.
     * Si la valeur de la semaien est �gale � 0 on prend la semaine courante.
     * Une erreur est lanc� si le num�ro de semaine est sup�rieur � 52
     * Une erreur est aussi lanc� si il y des nombres n�gatifs
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
