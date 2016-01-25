package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePeriode extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateFin = "";
    private String idDetenteurBTE = "";
    private String idMembreFamille = "";
    private String idPeriode = "";
    private String pays = "";
    private String type = "";

    /**
     * getter pour l'attribut date debut de la période
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin de la période
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idPeriode;
    }

    /**
     * getter pour l'attribut idMembreFamille du detenteur d'une période BTE
     * 
     * @return la valeur courante de l'attribut id membre famille detenteur ou 0 si la période n'est pas de type BTE
     */
    public String getIdDetenteurBTE() {
        return idDetenteurBTE;
    }

    /**
     * getter pour l'attribut idMembreFamille de la periode
     * 
     * @return la valeur courante de l'attribut idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    /**
     * getter pour l'attribut pays
     * 
     * @return la valeur courante de l'attribut pays quand le type est de type Assurance Etrangère ou 0 pour les autres
     *         types de période
     */
    public String getPays() {
        return pays;
    }

    /**
     * getter pour l'attribut type de la période
     * 
     * @return la valeur courante de l'attribut type periode n'est pas Assurance Etrangère
     */
    public String getType() {
        return type;
    }

    /**
     * Setter pour l'attribut dateDebut
     * 
     * @param dateDebut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Setter pour l'attribut dateFin
     * 
     * @param dateFin
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idPeriode = id;
    }

    /**
     * Setter pour l'attribut idDetenteurBTE
     * 
     * @param idDetenteurBTE
     */
    public void setIdDetenteurBTE(String idDetenteurBTE) {
        this.idDetenteurBTE = idDetenteurBTE;
    }

    /**
     * Setter pour l'attribut idMembreFamille
     * 
     * @param idMembreFamille
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * Setter pour l'attribut idPeriode
     * 
     * @param idPeriode
     */
    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    /**
     * Setter pour l'attribut pays
     * 
     * @param pays
     */
    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * Setter pour l'attribut type
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

}
