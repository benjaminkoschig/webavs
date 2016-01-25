package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AnnonceRafamDelegueComplexSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forRecordNumber = null;

    public String getForRecordNumber() {
        return forRecordNumber;
    }

    public void setForRecordNumber(String forRecordNumber) {
        this.forRecordNumber = forRecordNumber;
    }

    @Override
    public Class<AnnonceRafamDelegueComplexModel> whichModelClass() {
        return AnnonceRafamDelegueComplexModel.class;
    }

}
