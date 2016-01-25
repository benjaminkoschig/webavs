package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author jsi
 * 
 */
public class FactureRentePontSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forCsEtatFacture = null;
    private String forCsSousTypeSoin = null;
    private String forCsTypeSoin = null;
    private String forDateFacture = null;
    private String forIdDossier = null;
    private String forIdGestionnaire = null;
    private String forIdQdRentePont = null;
    private String forMontant = null;
    private String likeNom = null;

    private String likeNss = null;
    private String likePrenom = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCsEtatFacture() {
        return forCsEtatFacture;
    }

    public String getForCsSousTypeSoin() {
        return forCsSousTypeSoin;
    }

    public String getForCsTypeSoin() {
        return forCsTypeSoin;
    }

    public String getForDateFacture() {
        return forDateFacture;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForIdQdRentePont() {
        return forIdQdRentePont;
    }

    public String getForMontant() {
        return forMontant;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCsEtatFacture(String forCsEtatFacture) {
        this.forCsEtatFacture = forCsEtatFacture;
    }

    public void setForCsSousTypeSoin(String forCsSousTypeSoin) {
        this.forCsSousTypeSoin = forCsSousTypeSoin;
    }

    public void setForCsTypeSoin(String forCsTypeSoin) {
        this.forCsTypeSoin = forCsTypeSoin;
    }

    public void setForDateFacture(String forDateFacture) {
        this.forDateFacture = forDateFacture;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForIdQdRentePont(String forIdQdRentePont) {
        this.forIdQdRentePont = forIdQdRentePont;
    }

    public void setForMontant(String forMontant) {
        this.forMontant = forMontant;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    @Override
    public Class whichModelClass() {
        return FactureRentePont.class;
    }

}
