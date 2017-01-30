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

}
