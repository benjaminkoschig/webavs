/**
 * 
 */
package ch.globaz.amal.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author cbu
 * 
 */
public class CaisseMaladieSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forActifs = null;
    private String forGroupeNull = null;
    private String forIdTiersCaisse = null;
    private String forIdTiersGroupe = null;
    private String forNumCaisse = null;
    private String forNumGroupe = null;
    private String forTypeAdmin = null;
    private String forTypeLien = null;
    private String likeNomCaisse = null;

    public String getForActifs() {
        return forActifs;
    }

    public String getForGroupeNull() {
        return forGroupeNull;
    }

    public String getForIdTiersCaisse() {
        return forIdTiersCaisse;
    }

    public String getForIdTiersGroupe() {
        return forIdTiersGroupe;
    }

    public String getForNumCaisse() {
        return forNumCaisse;
    }

    public String getForNumGroupe() {
        return forNumGroupe;
    }

    public String getForTypeAdmin() {
        return forTypeAdmin;
    }

    public String getForTypeLien() {
        return forTypeLien;
    }

    public String getLikeNomCaisse() {
        return likeNomCaisse;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForActifs(String forActifs) {
        this.forActifs = forActifs;
    }

    public void setForGroupeNull(String forGroupeNull) {
        this.forGroupeNull = forGroupeNull;
    }

    public void setForIdTiersCaisse(String forIdTiersCaisse) {
        this.forIdTiersCaisse = forIdTiersCaisse;
    }

    public void setForIdTiersGroupe(String forIdTiersGroupe) {
        this.forIdTiersGroupe = forIdTiersGroupe;
    }

    public void setForNumCaisse(String forNumCaisse) {
        this.forNumCaisse = forNumCaisse;
    }

    public void setForNumGroupe(String forNumGroupe) {
        this.forNumGroupe = forNumGroupe;
    }

    public void setForTypeAdmin(String forTypeAdmin) {
        this.forTypeAdmin = forTypeAdmin;
    }

    public void setForTypeLien(String forTypeLien) {
        this.forTypeLien = forTypeLien;
    }

    public void setLikeNomCaisse(String likeNomCaisse) {
        this.likeNomCaisse = likeNomCaisse;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return CaisseMaladie.class;
    }

}
