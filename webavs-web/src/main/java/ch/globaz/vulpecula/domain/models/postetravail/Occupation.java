package ch.globaz.vulpecula.domain.models.postetravail;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;

/**
 * ValueObject représentant l'occupation d'un poste de travail
 * 
 */
public class Occupation {
    /** Identifiant de l'occupation */
    private String id;

    /** Date à laquelle le taux entre en fonction */
    private Date dateValidite;

    /** Taux auquel le poste de travail est occupé */
    private Taux taux;

    /** Spy pour la gestion de la concurrence */
    private String spy;

    /**
     * Retourne l'identifiant de l'occupation
     * 
     * @return String représentant l'identifiant
     */
    public String getId() {
        return id;
    }

    /**
     * Mise à jour de l'identifiant de l'occupation
     * 
     * @param id
     *            String représentant l'identifiant
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retourne la date de validité à partir de laquelle l'occupation entre en
     * fonction
     * 
     * @return {@link Date} de validité
     */
    public Date getDateValidite() {
        return dateValidite;
    }

    /**
     * Retourne le taux de l'occupation
     * 
     * @return {@link Taux} d'occupation
     */
    public Taux getTaux() {
        return taux;
    }

    /**
     * Mise à jour de la date de validité à laquelle l'occupation entre en
     * fonction
     * 
     * @param dateValidite
     *            {@link Date} de validité
     */
    public void setDateValidite(Date dateValidite) {
        this.dateValidite = dateValidite;
    }

    /**
     * Mise à jour du taux de l'occupation
     * 
     * @param taux
     *            Taux de l'occupation
     */
    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    /**
     * Retourne le taux sous forme de valeur
     * 
     * @return double représentant un taux
     */
    public String getTauxAsValue() {
        return taux.getValue();
    }

    /**
     * Retourne la date de validét sous forme de valeur
     * 
     * @return String représentant la date au format dd.mm.YYYY
     */
    public String getDateValiditeAsValue() {
        return dateValidite.getSwissValue();
    }

    /**
     * Retourne le spy
     * 
     * @return String représentant le spy
     */
    public String getSpy() {
        return spy;
    }

    /**
     * Mise à jour du spy
     * 
     * @param spy
     *            Nouveau spy
     */
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * Création d'une nouvelle occupation de 100% à la date actuelle.
     * 
     * @param date Date à laquelle créer la nouvelle occupation
     * @return Une occupation à 100.
     */
    public static Occupation valueOf100At(Date date) {
        Occupation occupation = new Occupation();
        occupation.setDateValidite(date);
        occupation.setTaux(new Taux(100));
        return occupation;
    }
}
