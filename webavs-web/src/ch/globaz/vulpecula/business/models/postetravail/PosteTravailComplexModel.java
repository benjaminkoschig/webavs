package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;

/**
 * Représentation base de données d'un poste de travail et ses relations
 * 
 */
public class PosteTravailComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 5085154096806742778L;

    private PosteTravailSimpleModel posteTravailSimpleModel;
    private EmployeurComplexModel employeurComplexModel;
    private TravailleurComplexModel travailleurComplexModel;

    public PosteTravailComplexModel() {
        super();
        posteTravailSimpleModel = new PosteTravailSimpleModel();
        employeurComplexModel = new EmployeurComplexModel();
        travailleurComplexModel = new TravailleurComplexModel();
    }

    public PosteTravailSimpleModel getPosteTravailSimpleModel() {
        return posteTravailSimpleModel;
    }

    public void setPosteTravailSimpleModel(PosteTravailSimpleModel posteTravailSimpleModel) {
        this.posteTravailSimpleModel = posteTravailSimpleModel;
    }

    public EmployeurComplexModel getEmployeurComplexModel() {
        return employeurComplexModel;
    }

    public void setEmployeurComplexModel(EmployeurComplexModel employeurComplexModel) {
        this.employeurComplexModel = employeurComplexModel;
    }

    public TravailleurComplexModel getTravailleurComplexModel() {
        return travailleurComplexModel;
    }

    public void setTravailleurComplexModel(TravailleurComplexModel travailleurComplexModel) {
        this.travailleurComplexModel = travailleurComplexModel;
    }

    @Override
    public String getId() {
        return travailleurComplexModel.getId();
    }

    @Override
    public String getSpy() {
        return travailleurComplexModel.getSpy();
    }

    @Override
    public void setId(String id) {
        travailleurComplexModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        travailleurComplexModel.setSpy(spy);
    }
}
