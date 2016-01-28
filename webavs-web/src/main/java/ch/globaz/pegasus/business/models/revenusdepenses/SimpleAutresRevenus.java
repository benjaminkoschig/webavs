package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleAutresRevenus extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEcheance = null;
    private String idAutresRevenus = null;
    private String idDonneeFinanciereHeader = null;
    private String libelle = null;
    private String montant = null;

    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idAutresRevenus;
    }

    public String getIdAutresRevenus() {
        return idAutresRevenus;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idAutresRevenus = id;

    }

    public void setIdAutresRevenus(String idAutresRevenus) {
        this.idAutresRevenus = idAutresRevenus;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
