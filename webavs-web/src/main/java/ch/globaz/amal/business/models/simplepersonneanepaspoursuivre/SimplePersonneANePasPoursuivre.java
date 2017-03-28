package ch.globaz.amal.business.models.simplepersonneanepaspoursuivre;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePersonneANePasPoursuivre extends JadeSimpleModel {
    private static final long serialVersionUID = 463601327635170370L;

    private String idPersonneNPP = null;
    private String nss = null;
    private String idTiersCM = null;
    private String idFamille = null;
    private String annee = null;
    private Boolean flagEnvoi = null;
    private Boolean flagReponse = null;
    private String montantCreance = null;
    private String nomPrenom = null;
    private String npaLocalite = null;
    private String idAnnonceSedex = null;
    private String idDetailFamille = null;

    @Override
    public String getId() {
        return getIdPersonneNPP();
    }

    @Override
    public void setId(String id) {
        idPersonneNPP = id;
    }

    public String getIdPersonneNPP() {
        return idPersonneNPP;
    }

    public void setIdPersonneNPP(String idPersonneNPP) {
        this.idPersonneNPP = idPersonneNPP;
    }

    public Boolean getFlagEnvoi() {
        return flagEnvoi;
    }

    public void setFlagEnvoi(Boolean flagEnvoi) {
        this.flagEnvoi = flagEnvoi;
    }

    public Boolean getFlagReponse() {
        return flagReponse;
    }

    public void setFlagReponse(Boolean flagReponse) {
        this.flagReponse = flagReponse;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getIdTiersCM() {
        return idTiersCM;
    }

    public void setIdTiersCM(String idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

    public String getIdFamille() {
        return idFamille;
    }

    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

    public String getMontantCreance() {
        return montantCreance;
    }

    public void setMontantCreance(String montantCreance) {
        this.montantCreance = montantCreance;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getNpaLocalite() {
        return npaLocalite;
    }

    public void setNpaLocalite(String npaLocalite) {
        this.npaLocalite = npaLocalite;
    }

    public String getIdAnnonceSedex() {
        return idAnnonceSedex;
    }

    public void setIdAnnonceSedex(String idAnnonceSedex) {
        this.idAnnonceSedex = idAnnonceSedex;
    }

    public String getIdDetailFamille() {
        return idDetailFamille;
    }

    public void setIdDetailFamille(String idDetailFamille) {
        this.idDetailFamille = idDetailFamille;
    }

}
