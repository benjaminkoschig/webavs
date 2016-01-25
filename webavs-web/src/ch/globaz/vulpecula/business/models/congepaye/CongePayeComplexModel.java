package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;

public class CongePayeComplexModel extends JadeComplexModel {

    private static final long serialVersionUID = -7238980013549500414L;
    private CongePayeSimpleModel congePayeSimpleModel;
    private PosteTravailComplexModel posteTravailComplexModel;
    private CompteurSimpleModel compteurSimpleModel;
    private PassageModel passageModel;
    private JournalSimpleModel journalSimpleModel;

    public CongePayeComplexModel() {
        super();
        congePayeSimpleModel = new CongePayeSimpleModel();
        posteTravailComplexModel = new PosteTravailComplexModel();
        compteurSimpleModel = new CompteurSimpleModel();
        passageModel = new PassageModel();
        journalSimpleModel = new JournalSimpleModel();
    }

    @Override
    public String getId() {
        return congePayeSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return congePayeSimpleModel.getSpy();
    }

    @Override
    public void setId(String arg0) {
        congePayeSimpleModel.setId(arg0);
    }

    @Override
    public void setSpy(String spy) {
        congePayeSimpleModel.setSpy(spy);
    }

    /**
     * @return the congePayeSimpleModel
     */
    public CongePayeSimpleModel getCongePayeSimpleModel() {
        return congePayeSimpleModel;
    }

    /**
     * @return the posteTravailComplexModel
     */
    public PosteTravailComplexModel getPosteTravailComplexModel() {
        return posteTravailComplexModel;
    }

    /**
     * @param posteTravailComplexModel the posteTravailComplexModel to set
     */
    public void setPosteTravailComplexModel(PosteTravailComplexModel posteTravailComplexModel) {
        this.posteTravailComplexModel = posteTravailComplexModel;
    }

    /**
     * @return the compteurSimpleModel
     */
    public CompteurSimpleModel getCompteurSimpleModel() {
        return compteurSimpleModel;
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
