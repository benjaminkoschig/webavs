package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSimpleModel;

public class CotisationAssociationProfessionnelleSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idAssociationProfessionnelle;
    private String libelle;
    private String libelleUpper;
    private String libelleFR;
    private String libelleDE;
    private String libelleIT;
    private String masseSalarialeDefaut;
    private String facturerDefaut;
    private String idRubrique;
    private String printOrder;

    private String genre;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getLibelleFR() {
        return libelleFR;
    }

    public void setLibelleFR(String libelleFR) {
        this.libelleFR = libelleFR;
    }

    public String getLibelleDE() {
        return libelleDE;
    }

    public void setLibelleDE(String libelleDE) {
        this.libelleDE = libelleDE;
    }

    public String getLibelleIT() {
        return libelleIT;
    }

    public void setLibelleIT(String libelleIT) {
        this.libelleIT = libelleIT;
    }

    public String getMasseSalarialeDefaut() {
        return masseSalarialeDefaut;
    }

    public void setMasseSalarialeDefaut(String masseSalarialeDefaut) {
        this.masseSalarialeDefaut = masseSalarialeDefaut;
    }

    public String getFacturerDefaut() {
        return facturerDefaut;
    }

    public void setFacturerDefaut(String facturerDefaut) {
        this.facturerDefaut = facturerDefaut;
    }

    public String getIdAssociationProfessionnelle() {
        return idAssociationProfessionnelle;
    }

    public void setIdAssociationProfessionnelle(String idAssociationProfessionnelle) {
        this.idAssociationProfessionnelle = idAssociationProfessionnelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLibelleUpper() {
        return libelleUpper;
    }

    public void setLibelleUpper(String libelleUpper) {
        this.libelleUpper = libelleUpper;
    }

    /**
     * @return the idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @param idRubrique the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @return the printOrder
     */
    public String getPrintOrder() {
        return printOrder;
    }

    /**
     * @param printOrder the printOrder to set
     */
    public void setPrintOrder(String printOrder) {
        this.printOrder = printOrder;
    }
}
