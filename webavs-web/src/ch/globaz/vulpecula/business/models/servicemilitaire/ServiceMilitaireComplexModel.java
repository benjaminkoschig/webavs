package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;

public class ServiceMilitaireComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 1L;

    private ServiceMilitaireSimpleModel serviceMilitaireSimpleModel;
    private PosteTravailComplexModel posteTravailComplexModel;
    private PassageModel passageModel;
    private JournalSimpleModel journalSimpleModel;

    public ServiceMilitaireComplexModel() {
        serviceMilitaireSimpleModel = new ServiceMilitaireSimpleModel();
        posteTravailComplexModel = new PosteTravailComplexModel();
        passageModel = new PassageModel();
        journalSimpleModel = new JournalSimpleModel();
    }

    @Override
    public String getId() {
        return serviceMilitaireSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return serviceMilitaireSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        serviceMilitaireSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        serviceMilitaireSimpleModel.setSpy(spy);
    }

    public ServiceMilitaireSimpleModel getServiceMilitaireSimpleModel() {
        return serviceMilitaireSimpleModel;
    }

    public void setServiceMilitaireSimpleModel(ServiceMilitaireSimpleModel serviceMilitaireSimpleModel) {
        this.serviceMilitaireSimpleModel = serviceMilitaireSimpleModel;
    }

    public PosteTravailComplexModel getPosteTravailComplexModel() {
        return posteTravailComplexModel;
    }

    public void setPosteTravailComplexModel(PosteTravailComplexModel posteTravailComplexModel) {
        this.posteTravailComplexModel = posteTravailComplexModel;
    }

    public PassageModel getPassageModel() {
        return passageModel;
    }

    public void setPassageModel(PassageModel passageModel) {
        this.passageModel = passageModel;
    }

    public JournalSimpleModel getJournalSimpleModel() {
        return journalSimpleModel;
    }

    public void setJournalSimpleModel(JournalSimpleModel journalSimpleModel) {
        this.journalSimpleModel = journalSimpleModel;
    }
}
