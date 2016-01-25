package ch.globaz.vulpecula.business.models.travailleur;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author JPA
 * 
 */
public class TravailleurComplexModel extends JadeComplexModel {

    private TravailleurSimpleModel travailleurSimpleModel = null;
    private PersonneEtendueComplexModel personneEtendueComplexModel = null;

    public TravailleurComplexModel() {
        super();
        travailleurSimpleModel = new TravailleurSimpleModel();
        personneEtendueComplexModel = new PersonneEtendueComplexModel();
    }

    public TravailleurSimpleModel getTravailleurSimpleModel() {
        return travailleurSimpleModel;
    }

    public void setTravailleurSimpleModel(TravailleurSimpleModel travailleurSimpleModel) {
        this.travailleurSimpleModel = travailleurSimpleModel;
    }

    public PersonneEtendueComplexModel getPersonneEtendueComplexModel() {
        return personneEtendueComplexModel;
    }

    public void setPersonneEtendueComplexModel(PersonneEtendueComplexModel personneEtendueComplexModel) {
        this.personneEtendueComplexModel = personneEtendueComplexModel;
    }

    @Override
    public String getId() {
        return travailleurSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return travailleurSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        travailleurSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        travailleurSimpleModel.setSpy(spy);
    }
}
