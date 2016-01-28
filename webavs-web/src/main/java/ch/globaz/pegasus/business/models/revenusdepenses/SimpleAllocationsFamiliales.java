package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleAllocationsFamiliales extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEcheance = null;
    private String idAllocationsFamiliales = null;
    private String idCaisseAf = null;
    private String idDonneeFinanciereHeader = null;
    private String montantMensuel = null;

    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idAllocationsFamiliales;
    }

    public String getIdAllocationsFamiliales() {
        return idAllocationsFamiliales;
    }

    public String getIdCaisseAf() {
        return idCaisseAf;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idAllocationsFamiliales = id;
    }

    public void setIdAllocationsFamiliales(String idAllocationsFamiliales) {
        this.idAllocationsFamiliales = idAllocationsFamiliales;
    }

    public void setIdCaisseAf(String idCaisseAf) {
        this.idCaisseAf = idCaisseAf;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

}
