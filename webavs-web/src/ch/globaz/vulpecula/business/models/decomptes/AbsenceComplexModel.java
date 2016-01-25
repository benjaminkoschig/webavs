package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class AbsenceComplexModel extends JadeComplexModel {
    private AbsenceSimpleModel absenceSimpleModel;

    public AbsenceComplexModel() {
        absenceSimpleModel = new AbsenceSimpleModel();
    }

    public AbsenceSimpleModel getAbsenceSimpleModel() {
        return absenceSimpleModel;
    }

    public void setAbsenceSimpleModel(AbsenceSimpleModel absenceSimpleModel) {
        this.absenceSimpleModel = absenceSimpleModel;
    }

    @Override
    public String getId() {
        return absenceSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return absenceSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        absenceSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        absenceSimpleModel.setSpy(spy);
    }
}
