/**
 * 
 */
package ch.globaz.amal.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author cbu
 * 
 */
public class CaisseMaladieGroupeRCListeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiersGroupe = null;
    private String forNumGroupe = null;
    private String forTypeLien = null;

    public String getForIdTiersGroupe() {
        return forIdTiersGroupe;
    }

    public String getForNumGroupe() {
        return forNumGroupe;
    }

    public String getForTypeLien() {
        return forTypeLien;
    }

    public void setForIdTiersGroupe(String forIdTiersGroupe) {
        this.forIdTiersGroupe = forIdTiersGroupe;
    }

    public void setForNumGroupe(String forNumGroupe) {
        this.forNumGroupe = forNumGroupe;
    }

    public void setForTypeLien(String forTypeLien) {
        this.forTypeLien = forTypeLien;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return CaisseMaladieGroupeRCListe.class;
    }

}
