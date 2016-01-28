package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSimpleModel;

public class TauxCongePayeSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String taux;
    private String idAssurance;
    private String idCongePaye;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    public String getIdCongePaye() {
        return idCongePaye;
    }

    public void setIdCongePaye(String idCongePaye) {
        this.idCongePaye = idCongePaye;
    }
}
