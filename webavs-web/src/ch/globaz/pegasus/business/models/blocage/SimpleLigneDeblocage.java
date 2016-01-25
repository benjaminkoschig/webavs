package ch.globaz.pegasus.business.models.blocage;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleLigneDeblocage extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtat;
    private String csTypeDeblocage;
    private String idApplicationAdressePaiement;
    private String idDeblocage;
    private String idPca;
    private String idPrestation;
    private String idRoleDetteEnCompta;
    private String idSectionDetteEnCompta;
    private String idTiersAdressePaiement;
    private String idTiersCreancier;
    private String montant;
    private String refPaiement;

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsTypeDeblocage() {
        return csTypeDeblocage;
    }

    @Override
    public String getId() {
        return idDeblocage;
    }

    public String getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    public String getIdDeblocage() {
        return idDeblocage;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRoleDetteEnCompta() {
        return idRoleDetteEnCompta;
    }

    public String getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTiersCreancier() {
        return idTiersCreancier;
    }

    public String getMontant() {
        return montant;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsTypeDeblocage(String csTypeDeblocage) {
        this.csTypeDeblocage = csTypeDeblocage;
    }

    @Override
    public void setId(String id) {
        idDeblocage = id;
    }

    public void setIdApplicationAdressePaiement(String idApplicationAdressePaiement) {
        this.idApplicationAdressePaiement = idApplicationAdressePaiement;
    }

    public void setIdDeblocage(String idDeblocage) {
        this.idDeblocage = idDeblocage;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRoleDetteEnCompta(String idRoleDetteEnCompta) {
        this.idRoleDetteEnCompta = idRoleDetteEnCompta;
    }

    public void setIdSectionDetteEnCompta(String idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTiersCreancier(String idTiersCreancier) {
        this.idTiersCreancier = idTiersCreancier;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

}
