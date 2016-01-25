package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleLibelleContratEntretienViager extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csLibelleContratEntretienViager = null;
    private String idContratEntretienViager = null;
    private String idLibelleContratEntretienViager = null;

    public String getCsLibelleContratEntretienViager() {
        return csLibelleContratEntretienViager;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idLibelleContratEntretienViager;
    }

    public String getIdContratEntretienViager() {
        return idContratEntretienViager;
    }

    public String getIdLibelleContratEntretienViager() {
        return idLibelleContratEntretienViager;
    }

    public void setCsLibelleContratEntretienViager(String csLibelleContratEntretienViager) {
        this.csLibelleContratEntretienViager = csLibelleContratEntretienViager;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idLibelleContratEntretienViager = id;
    }

    public void setIdContratEntretienViager(String idContratEntretienViager) {
        this.idContratEntretienViager = idContratEntretienViager;
    }

    public void setIdLibelleContratEntretienViager(String idLibelleContratEntretienViager) {
        this.idLibelleContratEntretienViager = idLibelleContratEntretienViager;
    }

}
