package ch.globaz.vulpecula.external.models.pyxis;

/**
 * Représentation métier d'une PersonneSimple (Personne) selon le module Pyxis
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public class PersonneSimple extends Tiers {
    private String idLocaliteDependance;
    private String dateNaissance;
    private String dateDeces;
    private String etatCivil;
    private String sexe;
    private String canton;
    private String district;

    public PersonneSimple() {
    }

    public PersonneSimple(Tiers tiers) {
        super(tiers);
    }

    public PersonneSimple(PersonneSimple personneSimple) {
        super(personneSimple);
        idLocaliteDependance = personneSimple.getIdLocaliteDependance();
        dateNaissance = personneSimple.getDateNaissance();
        dateDeces = personneSimple.getDateDeces();
        etatCivil = personneSimple.getEtatCivil();
        sexe = personneSimple.getSexe();
        canton = personneSimple.getCanton();
        district = personneSimple.getDistrict();
    }

    // FIXME: IdLocaliteDependance non présent sur le modèle de données
    public String getIdLocaliteDependance() {
        return idLocaliteDependance;
    }

    // FIXME: IdLocaliteDependance non présent sur le modèle de données
    public void setIdLocaliteDependance(String idLocaliteDependance) {
        this.idLocaliteDependance = idLocaliteDependance;
    }

    /**
     * Retourne la date de naissance
     * 
     * @return String représentant la date de naissance au format Suisse
     *         dd.MM.yyyy
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Mise à jour de la date de naissance
     * 
     * @param dateNaissance
     *            String représentant la nouvelle date de naissance au format
     *            Suisse dd.MM.yyyy
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * Retourne la date de décès
     * 
     * @return String représentant la date de décès au format Suisse dd.MM.yyyy
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * Mise à jour de la date de naisssance
     * 
     * @param dateDeces
     *            String représentant la nouvelle date de naissance au format
     *            Suisse dd.MM.yyyy
     */
    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    /**
     * Retourne l'état civil de la personne sous forme de code système
     * 
     * @return Code système représentant l'état civil de la personne
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    /**
     * Mise à jour de l'état civil de la personne
     * 
     * @param etatCivil
     *            Code système de type String représentant le nouvel état civil
     *            de la personne
     */
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    /**
     * Retourne le sexe de la personne sous forme de code système
     * 
     * @return Code système représentant le sexe de la personne
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * Mise à jour du sexe de la personne sous forme de code système
     * 
     * @param sexe
     *            COde système de type String représentant le nouveau sexe de la
     *            personne
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * Retourne la clé étrangère pointant sur le canton
     * 
     * @return String représentant la clé étrangère pointant sur le canton
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Mise à jour de la clé étrangère pointant sur le canton
     * 
     * @param canton
     *            String représentant la clé étrangère pointant sur le nouveau
     *            canton
     */
    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * Retourne le district
     * 
     * @return le district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Set le district
     * 
     * @param district
     */
    public void setDistrict(String district) {
        this.district = district;
    }
}
