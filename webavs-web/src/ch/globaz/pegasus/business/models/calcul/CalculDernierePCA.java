package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;

public class CalculDernierePCA extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String idPCAccordee = null;

    public String getDateDebut() {
        return dateDebut;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idPCAccordee;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idPCAccordee = id;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
