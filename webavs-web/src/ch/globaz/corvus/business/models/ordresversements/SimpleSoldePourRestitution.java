package ch.globaz.corvus.business.models.ordresversements;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleSoldePourRestitution extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeRestitution;
    private String idFactureARestituer;
    private String idOrdreVersement;
    private String idPrestation;
    private String idRetenue;
    private String idSoldePourRestitution;
    private String montant;
    private String montantMensuelARetenir;

    public SimpleSoldePourRestitution() {
        super();

        csTypeRestitution = "";
        idFactureARestituer = "";
        idOrdreVersement = "";
        idPrestation = "";
        idRetenue = "";
        idSoldePourRestitution = "";
        montant = "";
        montantMensuelARetenir = "";
    }

    public String getCsTypeRestitution() {
        return csTypeRestitution;
    }

    @Override
    public String getId() {
        return idSoldePourRestitution;
    }

    public String getIdFactureARestituer() {
        return idFactureARestituer;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRetenue() {
        return idRetenue;
    }

    public String getIdSoldePourRestitution() {
        return idSoldePourRestitution;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantMensuelARetenir() {
        return montantMensuelARetenir;
    }

    public void setCsTypeRestitution(String csTypeRestitution) {
        this.csTypeRestitution = csTypeRestitution;
    }

    @Override
    public void setId(String id) {
        idSoldePourRestitution = id;
    }

    public void setIdFactureARestituer(String idFactureARestituer) {
        this.idFactureARestituer = idFactureARestituer;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRetenue(String idRetenue) {
        this.idRetenue = idRetenue;
    }

    public void setIdSoldePourRestitution(String idSoldePourRestitution) {
        this.idSoldePourRestitution = idSoldePourRestitution;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantMensuelARetenir(String montantMensuelARetenir) {
        this.montantMensuelARetenir = montantMensuelARetenir;
    }
}
