package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleFactureSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateFacture;
    private String forEtat;
    private String forIdQD;
    private String forLibelle;
    private String forMontant;
    private String forMontantRembourse;
    private String forMotif;

    public String getForDateFacture() {
        return forDateFacture;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdQD() {
        return forIdQD;
    }

    public String getForLibelle() {
        return forLibelle;
    }

    public String getForMontant() {
        return forMontant;
    }

    public String getForMontantRembourse() {
        return forMontantRembourse;
    }

    public String getForMotif() {
        return forMotif;
    }

    public void setForDateFacture(String dateFacture) {
        forDateFacture = dateFacture;
    }

    public void setForEtat(String etat) {
        forEtat = etat;
    }

    public void setForIdQD(String idQD) {
        forIdQD = idQD;
    }

    public void setForLibelle(String libelle) {
        forLibelle = libelle;
    }

    public void setForMontant(String montant) {
        forMontant = montant;
    }

    public void setForMontantRembourse(String montantRembourse) {
        forMontantRembourse = montantRembourse;
    }

    public void setForMotif(String motif) {
        forMotif = motif;
    }

    @Override
    public Class whichModelClass() {
        return SimpleFacture.class;
    }
}
