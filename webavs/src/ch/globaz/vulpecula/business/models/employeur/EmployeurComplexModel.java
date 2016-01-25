/**
 *
 */
package ch.globaz.vulpecula.business.models.employeur;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AffiliationTiersComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

/**
 * Modèle de base de données représentation la relation entre un employeur et sa convention.
 * 
 * @author sel
 * 
 */
public class EmployeurComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 8846314027277333941L;

    private AffiliationTiersComplexModel affiliationTiersComplexModel = null;
    private AdministrationComplexModel administrationComplexModel = null;
    private EmployeurSimpleModel employeurSimpleModel = null;

    public EmployeurComplexModel() {
        affiliationTiersComplexModel = new AffiliationTiersComplexModel();
        administrationComplexModel = new AdministrationComplexModel();
        employeurSimpleModel = new EmployeurSimpleModel();
    }

    @Override
    public String getId() {
        return employeurSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return employeurSimpleModel.getSpy();
    }

    @Override
    public void setId(final String id) {
        employeurSimpleModel.setId(id);
    }

    @Override
    public void setSpy(final String spy) {
        employeurSimpleModel.setSpy(spy);
    }

    public AffiliationTiersComplexModel getAffiliationTiersComplexModel() {
        return affiliationTiersComplexModel;
    }

    public void setAffiliationTiersComplexModel(final AffiliationTiersComplexModel affiliationTiersComplexModel) {
        this.affiliationTiersComplexModel = affiliationTiersComplexModel;
    }

    public AdministrationComplexModel getAdministrationComplexModel() {
        return administrationComplexModel;
    }

    public void setAdministrationComplexModel(final AdministrationComplexModel administrationComplexModel) {
        this.administrationComplexModel = administrationComplexModel;
    }

    public EmployeurSimpleModel getEmployeurSimpleModel() {
        return employeurSimpleModel;
    }

    public void setEmployeurSimpleModel(final EmployeurSimpleModel employeurSimpleModel) {
        this.employeurSimpleModel = employeurSimpleModel;
    }
}
