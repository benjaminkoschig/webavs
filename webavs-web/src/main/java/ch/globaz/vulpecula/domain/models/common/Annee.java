package ch.globaz.vulpecula.domain.models.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Une ann�e dans le syst�me calendaire, tel que {@code 2014}.
 * <p>
 * {@code Annee} est un objet immutable repr�sentant une ann�e calendaire.
 * </p>
 * <p>
 * Cette classe ne stocke, ni contient de notion de mois, jour, heure ou fuseau horaire.
 * </p>
 * 
 */
public final class Annee implements ValueObject, Comparable<Annee> {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 9999;
    private static final long serialVersionUID = 1L;

    /**
     * L'ann�e repr�sent�e.
     */
    private int annee;

    /**
     * Cr�e une instance de {@code Annee}.
     * 
     * @param annee l'ann�e � repr�senter, de {@code MIN_VALUE} � {@code MAX_VALUE}
     * @throws IllegalArgumentException si l'argument pass� est invalide
     */
    public Annee(int annee) {
        if (annee < MIN_VALUE || annee > MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        this.annee = annee;
    }

    /**
     * Cr�e une instance de {@code Annee}.
     * 
     * @param annee l'ann�e � repr�senter, de {@code MIN_VALUE} � {@code MAX_VALUE}
     * @throws IllegalArgumentException si l'argument pass� est invalide
     * @throws NumberFormatException si l'argument pass� n'est pas un nombre valide
     */
    public Annee(String annee) {
        this(Integer.valueOf(annee));
    }

    public Annee(Date date) {
        this(date.getYear());
    }

    @Override
    public int compareTo(Annee other) {
        return annee - other.annee;
    }

    /**
     * Cette ann�e se situe-t-elle apr�s l'ann�e sp�cifi�e.
     * 
     * @param other l'ann�e avec laquelle comparer, not null
     * @return true si l'ann�e se situe apr�s l'ann�e sp�cifi�e
     */
    public boolean isAfter(Annee other) {
        return annee > other.annee;
    }

    /**
     * Cette ann�e se situe-t-elle pendant ou apr�s l'ann�e sp�cifi�e.
     * 
     * @param other l'annn�e avec laquelle comparer
     * @return true si l'ann�e se situe apr�s ou pendant l'ann�e sp�cifi�e.
     */
    public boolean isAfterOrEquals(Annee other) {
        return annee >= other.annee;
    }

    /**
     * Cette ann�e se situe-t-elle pendant ou avant l'ann�e sp�cifi�e.
     * 
     * @param other l'annn�e avec laquelle comparer
     * @return true si l'ann�e se situe avant ou pendant l'ann�e sp�cifi�e.
     */
    public boolean isBeforeOrEquals(Annee other) {
        return annee <= other.annee;
    }

    /**
     * Cette ann�e se situe-t-elle avant l'ann�e sp�cifi�e.
     * 
     * @param other l'annn�e avec laquelle comparer
     * @return true si l'ann�e se situe avant l'ann�e sp�cifi�e.
     */
    public boolean isBefore(Annee other) {
        return annee < other.annee;
    }

    /**
     * Retourne la valeur de l'ann�e.
     * 
     * @return l'ann�e, de {@code MIN_VALUE} � {@code MAX_VALUE}
     */
    public int getValue() {
        return annee;
    }

    /**
     * Retourne le premier jour de l'ann�e. Soit le 01.01.XXXX.
     * 
     * @return Date repr�sentant le premier jour de l'ann�e
     */
    public Date getFirstDayOfYear() {
        return Date.getFirstDayOfYear(annee);
    }

    /**
     * Retourne le dernier jour de l'ann�e. Soit le 31.12.XXXX.
     * 
     * @return Date repr�sentant le dernier jour de l'ann�e
     */
    public Date getLastDayOfYear() {
        return Date.getLastDayOfYear(annee);
    }

    /**
     * Retourne le nombre d'ann�es entre deux ann�es.
     * 
     * @param anneeDebut Integer repr�sentant une ann�e
     * @param anneeFin Integer repr�sentant une ann�e
     * @return Liste des ann�es entre les deux ann�es (y compris)
     */
    public static List<Annee> between(int anneeDebut, int anneeFin) {
        List<Annee> annees = new ArrayList<Annee>();
        int nbAnnees = anneeFin - anneeDebut;
        for (int i = 0; i <= nbAnnees; i++) {
            annees.add(new Annee(anneeDebut + i));
        }
        return annees;
    }

    /**
     * Retourne le nombre d'ann�es entre deux ann�es.
     * 
     * @param anneeDebut Ann�e de d�but
     * @param anneeFin Ann�e de fin
     * @return Liste des ann�es entre les deux ann�es (y compris)
     */
    public static List<Annee> between(Annee anneeDebut, Annee anneeFin) {
        return between(anneeDebut.annee, anneeFin.annee);
    }

    /**
     * Retourne l'ann�e suivant celle en cours.
     * 
     * @return Ann�e suivante de l'actuelle (2010 -> 2011)
     */
    public Annee next() {
        return new Annee(annee + 1);
    }

    public Annee previous() {
        return previous(1);
    }

    public Annee previous(int nb) {
        return new Annee(annee - nb);
    }

    public boolean isContained(Annee anneeDebut, Annee anneeFin) {
        List<Annee> annees = Annee.between(anneeDebut, anneeFin);
        return annees.contains(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Annee) {
            Annee annee = (Annee) obj;
            return annee.getValue() == getValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getValue();
    }

    @Override
    public String toString() {
        return String.valueOf(annee);
    }

    public static Annee getCurrentYear() {
        return new Annee(Date.now().getAnnee());
    }
}
