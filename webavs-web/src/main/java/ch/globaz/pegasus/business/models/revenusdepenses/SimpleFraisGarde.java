package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleFraisGarde extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEcheance = null;
    private String idFraisGarde = null;
    private String idDonneeFinanciereHeader = null;
    private String montant = null;
    private String libelle = null;

    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getId() {
        return idFraisGarde;
    }


    public String getIdFraisGarde() {
        return idFraisGarde;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }


    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    @Override
    public void setId(String id) {
        idFraisGarde = id;
    }

    public void setIdFraisGarde(String idCotisationsPsal) {
        this.idFraisGarde = idCotisationsPsal;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }
    public String getMontant() {
        return montant;
    }
    public void setMontant(String montant) {
        this.montant = montant;
    }
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
