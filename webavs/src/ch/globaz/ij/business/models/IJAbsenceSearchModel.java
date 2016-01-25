package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class IJAbsenceSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAbsence;
    private String forIdBaseIndemnisation;
    private String forIdDossier;

    public final String getForIdAbsence() {
        return forIdAbsence;
    }

    public final String getForIdBaseIndemnisation() {
        return forIdBaseIndemnisation;
    }

    public final String getForIdDossier() {
        return forIdDossier;
    }

    public final void setForIdAbsence(String forIdAbsence) {
        this.forIdAbsence = forIdAbsence;
    }

    public final void setForIdBaseIndemnisation(String forIdBaseIndemnisation) {
        this.forIdBaseIndemnisation = forIdBaseIndemnisation;
    }

    public final void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJAbsence.class;
    }

}
