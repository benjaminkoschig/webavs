package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ServiceMilitaireSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private String forIdPosteTravail;
    private String forIdPassage;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    public void setForIdPosteTravail(String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    @Override
    public Class<ServiceMilitaireSimpleModel> whichModelClass() {
        return ServiceMilitaireSimpleModel.class;
    }

}
