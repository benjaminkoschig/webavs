package ch.globaz.vulpecula.domain.models.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Une année dans le système calendaire, tel que {@code 2014}.
 * <p>
 * {@code Annee} est un objet immutable représentant une année calendaire.
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
     * L'année représentée.
     */
    private int annee;

    /**
     * Crée une instance de {@code Annee}.
     * 
     * @param annee l'année à représenter, de {@code MIN_VALUE} à {@code MAX_VALUE}
     * @throws IllegalArgumentException si l'argument passé est invalide
     */
    public Annee(int annee) {
        if (annee < MIN_VALUE || annee > MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        this.annee = annee;
    }

    /**
     * Crée une instance de {@code Annee}.
     * 
     * @param annee l'année à représenter, de {@code MIN_VALUE} à {@code MAX_VALUE}
     * @throws IllegalArgumentException si l'argument passé est invalide
     * @throws NumberFormatException si l'argument passé n'est pas un nombre valide
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
     * Cette année se situe-t-elle après l'année spécifiée.
     * 
     * @param other l'année avec laquelle comparer, not null
     * @return true si l'année se situe après l'année spécifiée
     */
    public boolean isAfter(Annee other) {
        return annee > other.annee;
    }

    /**
     * Cette année se situe-t-elle pendant ou après l'année spécifiée.
     * 
     * @param other l'annnée avec laquelle comparer
     * @return true si l'année se situe après ou pendant l'année spécifiée.
     */
    public boolean isAfterOrEquals(Annee other) {
        return annee >= other.annee;
    }

    /**
     * Cette année se situe-t-elle pendant ou avant l'année spécifiée.
     * 
     * @param other l'annnée avec laquelle comparer
     * @return true si l'année se situe avant ou pendant l'année spécifiée.
     */
    public boolean isBeforeOrEquals(Annee other) {
        return annee <= other.annee;
    }

    /**
     * Cette année se situe-t-elle avant l'année spécifiée.
     * 
     * @param other l'annnée avec laquelle comparer
     * @return true si l'année se situe avant l'année spécifiée.
     */
    public boolean isBefore(Annee other) {
        return annee < other.annee;
    }

    /**
     * Retourne la valeur de l'année.
     * 
     * @return l'année, de {@code MIN_VALUE} à {@code MAX_VALUE}
     */
    public int getValue() {
        return annee;
    }

    /**
     * Retourne le premier jour de l'année. Soit le 01.01.XXXX.
     * 
     * @return Date représentant le premier jour de l'année
     */
    public Date getFirstDayOfYear() {
        return Date.getFirstDayOfYear(annee);
    }

    /**
     * Retourne le dernier jour de l'année. Soit le 31.12.XXXX.
     * 
     * @return Date représentant le dernier jour de l'année
     */
    public Date getLastDayOfYear() {
        return Date.getLastDayOfYear(annee);
    }

    /**
     * Retourne le nombre d'années entre deux années.
     * 
     * @param anneeDebut Integer représentant une année
     * @param anneeFin Integer représentant une année
     * @return Liste des années entre les deux années (y compris)
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
     * Retourne le nombre d'années entre deux années.
     * 
     * @param anneeDebut Année de début
     * @param anneeFin Année de fin
     * @return Liste des années entre les deux années (y compris)
     */
    public static List<Annee> between(Annee anneeDebut, Annee anneeFin) {
        return between(anneeDebut.annee, anneeFin.annee);
    }

    /**
     * Retourne l'année suivant celle en cours.
     * 
     * @return Année suivante de l'actuelle (2010 -> 2011)
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
