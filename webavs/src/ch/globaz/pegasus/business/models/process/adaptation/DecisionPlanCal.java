package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeComplexModel;

public class DecisionPlanCal extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatPC = null;
    private String idDemande = null;
    private String nss = null;

    public String getEtatPC() {
        return etatPC;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getNss() {
        return nss;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setEtatPC(String etatPC) {
        this.etatPC = etatPC;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
