/**
 * 
 */
package ch.globaz.amal.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author cbu
 * 
 */
public class CaisseMaladieGroupeRCListe extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idTiersGroupe = null;
    private String nomGroupe = null;
    private String numGroupe = null;
    private String typeLien = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idTiersGroupe;
    }

    public String getIdTiersGroupe() {
        return idTiersGroupe;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public String getNumGroupe() {
        return numGroupe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTypeLien() {
        return typeLien;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idTiersGroupe = id;
    }

    public void setIdTiersGroupe(String idTiersGroupe) {
        this.idTiersGroupe = idTiersGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public void setNumGroupe(String numGroupe) {
        this.numGroupe = numGroupe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

    public void setTypeLien(String typeLien) {
        this.typeLien = typeLien;
    }

}
