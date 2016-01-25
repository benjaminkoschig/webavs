package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;

/**
 * @since WebBMS 0.01.04
 */
public class CompteurComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -9167044342813368037L;

    private CompteurSimpleModel compteurSimpleModel;
    private PosteTravailComplexModel posteTravailComplexModel;

    public CompteurComplexModel() {
        compteurSimpleModel = new CompteurSimpleModel();
        posteTravailComplexModel = new PosteTravailComplexModel();
    }

    @Override
    public String getId() {
        return compteurSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return compteurSimpleModel.getSpy();
    }

    @Override
    public void setId(String arg0) {
        compteurSimpleModel.setId(arg0);
    }

    @Override
    public void setSpy(String arg0) {
        compteurSimpleModel.setSpy(arg0);
    }

    /**
     * @return the compteurSimpleModel
     */
    public CompteurSimpleModel getCompteurSimpleModel() {
        return compteurSimpleModel;
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
}
