package ch.globaz.vulpecula.business.models.notification;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class NotificationSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 8243037424285376171L;

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    @Override
    public Class<NotificationSimpleModel> whichModelClass() {
        return NotificationSimpleModel.class;
    }
}
