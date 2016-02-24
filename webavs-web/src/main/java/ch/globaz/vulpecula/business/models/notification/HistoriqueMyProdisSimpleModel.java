package ch.globaz.vulpecula.business.models.notification;

import globaz.jade.persistence.model.JadeSimpleModel;

public class HistoriqueMyProdisSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -4595306990918046143L;

    private String idTiers;
    private String infoType;

    @Override
    public String getId() {
        return idTiers;
    }

    @Override
    public void setId(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
