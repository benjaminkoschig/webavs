package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleConversionRente extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String age = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idConversionRente = null;
    private String renteFemme = null;
    private String renteHomme = null;
    private String typeDeValeur = null;

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idConversionRente;
    }

    /**
     * @return the idConversionRente
     */
    public String getIdConversionRente() {
        return idConversionRente;
    }

    /**
     * @return the renteFemme
     */
    public String getRenteFemme() {
        return renteFemme;
    }

    /**
     * @return the renteHomme
     */
    public String getRenteHomme() {
        return renteHomme;
    }

    public String getTypeDeValeur() {
        return typeDeValeur;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idConversionRente = id;

    }

    /**
     * @param idConversionRente
     *            the idConversionRente to set
     */
    public void setIdConversionRente(String idConversionRente) {
        this.idConversionRente = idConversionRente;
    }

    /**
     * @param renteFemme
     *            the renteFemme to set
     */
    public void setRenteFemme(String renteFemme) {
        this.renteFemme = renteFemme;
    }

    /**
     * @param renteHomme
     *            the renteHomme to set
     */
    public void setRenteHomme(String renteHomme) {
        this.renteHomme = renteHomme;
    }

    public void setTypeDeValeur(String typeDeValeur) {
        this.typeDeValeur = typeDeValeur;
    }

}
