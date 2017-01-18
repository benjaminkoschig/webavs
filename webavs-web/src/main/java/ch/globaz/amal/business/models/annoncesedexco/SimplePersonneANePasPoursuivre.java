package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePersonneANePasPoursuivre extends JadeSimpleModel {
    private static final long serialVersionUID = 463601327635170370L;

    private String idPersonneNPP = null;
    private String idFamille = null;
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

    public String getIdFamille() {
        return idFamille;
    }

    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
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

}
