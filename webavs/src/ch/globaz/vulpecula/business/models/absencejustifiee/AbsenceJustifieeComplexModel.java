package ch.globaz.vulpecula.business.models.absencejustifiee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;

public class AbsenceJustifieeComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 4884240490749531865L;

    private AbsenceJustifieeSimpleModel absenceJustifieeSimpleModel;
    private PosteTravailComplexModel posteTravailComplexModel;
    private PassageModel passageModel;
    private JournalSimpleModel journalSimpleModel;

    public AbsenceJustifieeComplexModel() {
        absenceJustifieeSimpleModel = new AbsenceJustifieeSimpleModel();
        posteTravailComplexModel = new PosteTravailComplexModel();
        passageModel = new PassageModel();
        journalSimpleModel = new JournalSimpleModel();
    }

    @Override
    public String getId() {
        return absenceJustifieeSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return absenceJustifieeSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        absenceJustifieeSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        absenceJustifieeSimpleModel.setSpy(spy);
    }

    public AbsenceJustifieeSimpleModel getAbsenceJustifieeSimpleModel() {
        return absenceJustifieeSimpleModel;
    }

    public void setAbsenceJustifieeSimpleModel(AbsenceJustifieeSimpleModel absenceJustifieeSimpleModel) {
        this.absenceJustifieeSimpleModel = absenceJustifieeSimpleModel;
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
