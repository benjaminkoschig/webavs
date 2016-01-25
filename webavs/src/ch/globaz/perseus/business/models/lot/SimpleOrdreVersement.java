/**
 * 
 */
package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */
public class SimpleOrdreVersement extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csRole = null;
    private String csTypeVersement = null;
    private String idDomaineApplication = null;
    private String idExterne = null;
    private String idOrdreVersement = null;
    private String idPrestation = null;
    private String idRole = null;
    private String idTiers = null;
    private String idTiersAdressePaiement = null;
    private String montantVersement = null;
    private String numFacture = null;

    public String getCsRole() {
        return csRole;
    }

    public String getCsTypeVersement() {
        return csTypeVersement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idOrdreVersement;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getMontantVersement() {
        return montantVersement;
    }

    public String getNumFacture() {
        return numFacture;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setCsTypeVersement(String csTypeVersement) {
        this.csTypeVersement = csTypeVersement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idOrdreVersement = id;

    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setMontantVersement(String montant) {
        montantVersement = montant;
    }

    public void setNumFacture(String numFacture) {
        this.numFacture = numFacture;
    }
}
