package ch.globaz.vulpecula.business.models.notification;

import globaz.jade.persistence.model.JadeSimpleModel;

public class NotificationSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -423366217881899335L;

    private String id;
    private String infoType;
    private String idCible;
    private String extra;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getInfoType() {
        return infoType;
    }

    public String getIdCible() {
        return idCible;
    }

    public void setIdCible(String idCible) {
        this.idCible = idCible;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
