package ch.globaz.vulpecula.domain.models.postetravail;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;

/**
 * ValueObject repr�sentant l'occupation d'un poste de travail
 * 
 */
public class Occupation {
    /** Identifiant de l'occupation */
    private String id;

    /** Date � laquelle le taux entre en fonction */
    private Date dateValidite;

    /** Taux auquel le poste de travail est occup� */
    private Taux taux;

    /** Spy pour la gestion de la concurrence */
    private String spy;

    /**
     * Retourne l'identifiant de l'occupation
     * 
     * @return String repr�sentant l'identifiant
     */
    public String getId() {
        return id;
    }

    /**
     * Mise � jour de l'identifiant de l'occupation
     * 
     * @param id
     *            String repr�sentant l'identifiant
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retourne la date de validit� � partir de laquelle l'occupation entre en
     * fonction
     * 
     * @return {@link Date} de validit�
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
     * Mise � jour de la date de validit� � laquelle l'occupation entre en
     * fonction
     * 
     * @param dateValidite
     *            {@link Date} de validit�
     */
    public void setDateValidite(Date dateValidite) {
        this.dateValidite = dateValidite;
    }

    /**
     * Mise � jour du taux de l'occupation
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
     * @return double repr�sentant un taux
     */
    public String getTauxAsValue() {
        return taux.getValue();
    }

    /**
     * Retourne la date de valid�t sous forme de valeur
     * 
     * @return String repr�sentant la date au format dd.mm.YYYY
     */
    public String getDateValiditeAsValue() {
        return dateValidite.getSwissValue();
    }

    /**
     * Retourne le spy
     * 
     * @return String repr�sentant le spy
     */
    public String getSpy() {
        return spy;
    }

    /**
     * Mise � jour du spy
     * 
     * @param spy
     *            Nouveau spy
     */
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * Cr�ation d'une nouvelle occupation de 100% � la date actuelle.
     * 
     * @param date Date � laquelle cr�er la nouvelle occupation
     * @return Une occupation � 100.
     */
    public static Occupation valueOf100At(Date date) {
        Occupation occupation = new Occupation();
        occupation.setDateValidite(date);
        occupation.setTaux(new Taux(100));
        return occupation;
    }
}
