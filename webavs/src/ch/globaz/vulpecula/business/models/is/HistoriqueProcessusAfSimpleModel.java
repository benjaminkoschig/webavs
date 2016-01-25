package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeSimpleModel;

public class HistoriqueProcessusAfSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -227868754034462821L;

    private String id;
    private String idProcessus;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdProcessus() {
        return idProcessus;
    }

    public void setIdProcessus(String idProcessus) {
        this.idProcessus = idProcessus;
    }
}
