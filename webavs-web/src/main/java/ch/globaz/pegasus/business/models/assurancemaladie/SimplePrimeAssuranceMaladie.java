package ch.globaz.pegasus.business.models.assurancemaladie;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimplePrimeAssuranceMaladie extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPrimeAssuranceMaladie = null;
    private String idDonneeFinanciereHeader = null;
    private String montant = null;


    @Override
    public String getId() {
        return idPrimeAssuranceMaladie;
    }


    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }


    @Override
    public void setId(String id) {
        idPrimeAssuranceMaladie = id;
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


    public String getIdPrimeAssuranceMaladie() {
        return idPrimeAssuranceMaladie;
    }

    public void setIdPrimeAssuranceMaladie(String idPrimeAssuranceMaladie) {
        this.idPrimeAssuranceMaladie = idPrimeAssuranceMaladie;
    }
}
