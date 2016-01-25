package ch.globaz.vulpecula.external.models.pyxis;

/**
 * Repr�sentation m�tier d'une PersonneSimple (Personne) selon le module Pyxis
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
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

    // FIXME: IdLocaliteDependance non pr�sent sur le mod�le de donn�es
    public String getIdLocaliteDependance() {
        return idLocaliteDependance;
    }

    // FIXME: IdLocaliteDependance non pr�sent sur le mod�le de donn�es
    public void setIdLocaliteDependance(String idLocaliteDependance) {
        this.idLocaliteDependance = idLocaliteDependance;
    }

    /**
     * Retourne la date de naissance
     * 
     * @return String repr�sentant la date de naissance au format Suisse
     *         dd.MM.yyyy
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Mise � jour de la date de naissance
     * 
     * @param dateNaissance
     *            String repr�sentant la nouvelle date de naissance au format
     *            Suisse dd.MM.yyyy
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * Retourne la date de d�c�s
     * 
     * @return String repr�sentant la date de d�c�s au format Suisse dd.MM.yyyy
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * Mise � jour de la date de naisssance
     * 
     * @param dateDeces
     *            String repr�sentant la nouvelle date de naissance au format
     *            Suisse dd.MM.yyyy
     */
    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    /**
     * Retourne l'�tat civil de la personne sous forme de code syst�me
     * 
     * @return Code syst�me repr�sentant l'�tat civil de la personne
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    /**
     * Mise � jour de l'�tat civil de la personne
     * 
     * @param etatCivil
     *            Code syst�me de type String repr�sentant le nouvel �tat civil
     *            de la personne
     */
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    /**
     * Retourne le sexe de la personne sous forme de code syst�me
     * 
     * @return Code syst�me repr�sentant le sexe de la personne
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * Mise � jour du sexe de la personne sous forme de code syst�me
     * 
     * @param sexe
     *            COde syst�me de type String repr�sentant le nouveau sexe de la
     *            personne
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * Retourne la cl� �trang�re pointant sur le canton
     * 
     * @return String repr�sentant la cl� �trang�re pointant sur le canton
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Mise � jour de la cl� �trang�re pointant sur le canton
     * 
     * @param canton
     *            String repr�sentant la cl� �trang�re pointant sur le nouveau
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
