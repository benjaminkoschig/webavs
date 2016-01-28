package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleContratEntretienViager extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csLibelle = null;
    private String idContratEntretienViager = null;
    private String idDonneeFinanciereHeader = null;
    private String montantContrat = null;

    public String getCsLibelle() {
        return csLibelle;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idContratEntretienViager;
    }

    public String getIdContratEntretienViager() {
        return idContratEntretienViager;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getMontantContrat() {
        return montantContrat;
    }

    public void setCsLibelle(String csLibelle) {
        this.csLibelle = csLibelle;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idContratEntretienViager = id;
    }

    public void setIdContratEntretienViager(String idContratEntretienViager) {
        this.idContratEntretienViager = idContratEntretienViager;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setMontantContrat(String montantContrat) {
        this.montantContrat = montantContrat;
    }

}
