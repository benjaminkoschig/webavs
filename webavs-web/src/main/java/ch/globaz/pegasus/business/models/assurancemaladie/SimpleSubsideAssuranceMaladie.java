package ch.globaz.pegasus.business.models.assurancemaladie;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleSubsideAssuranceMaladie extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idSubsideAssuranceMaladie = null;
    private String idDonneeFinanciereHeader = null;
    private String montant = null;


    @Override
    public String getId() {
        return idSubsideAssuranceMaladie;
    }


    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }


    @Override
    public void setId(String id) {
        idSubsideAssuranceMaladie = id;
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


    public String getIdSubsideAssuranceMaladie() {
        return idSubsideAssuranceMaladie;
    }

    public void setIdSubsideAssuranceMaladie(String idSubsideAssuranceMaladie) {
        this.idSubsideAssuranceMaladie = idSubsideAssuranceMaladie;
    }
}
