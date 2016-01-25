package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeSimpleModel;

public class TauxServiceMilitaireSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -488335678015857081L;

    private String id;
    private String idServiceMilitaire;
    private String idAssurance;
    private String taux;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdServiceMilitaire() {
        return idServiceMilitaire;
    }

    public void setIdServiceMilitaire(String idServiceMilitaire) {
        this.idServiceMilitaire = idServiceMilitaire;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }
}
