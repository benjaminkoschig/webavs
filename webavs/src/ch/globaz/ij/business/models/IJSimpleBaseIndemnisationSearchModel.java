package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class IJSimpleBaseIndemnisationSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDeDebut;
    private String forDateDeFin;
    private String forIdBaseIndemnisation;
    private String forIdParent;
    private String forJoursExternes;
    private String forJoursInternes;
    private String forJoursInterruption;
    private String forMotifInterruption;

    public IJSimpleBaseIndemnisationSearchModel() {
        forDateDeDebut = "";
        forDateDeFin = "";
        forIdBaseIndemnisation = "";
        forJoursExternes = "";
        forJoursInternes = "";
        forJoursInterruption = "";
        forMotifInterruption = "";
        forIdParent = "";
    }

    public String getForDateDeDebut() {
        return forDateDeDebut;
    }

    public String getForDateDeFin() {
        return forDateDeFin;
    }

    public String getForIdBaseIndemnisation() {
        return forIdBaseIndemnisation;
    }

    public String getForIdParent() {
        return forIdParent;
    }

    public String getForJoursExternes() {
        return forJoursExternes;
    }

    public String getForJoursInternes() {
        return forJoursInternes;
    }

    public String getForJoursInterruption() {
        return forJoursInterruption;
    }

    public String getForMotifInterruption() {
        return forMotifInterruption;
    }

    public void setForDateDeDebut(String forDateDeDebut) {
        this.forDateDeDebut = forDateDeDebut;
    }

    public void setForDateDeFin(String forDateDeFin) {
        this.forDateDeFin = forDateDeFin;
    }

    public void setForIdBaseIndemnisation(String forIdBaseIndemnisation) {
        this.forIdBaseIndemnisation = forIdBaseIndemnisation;
    }

    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
    }

    public void setForJoursExternes(String forJoursExternes) {
        this.forJoursExternes = forJoursExternes;
    }

    public void setForJoursInternes(String forJoursInternes) {
        this.forJoursInternes = forJoursInternes;
    }

    public void setForJoursInterruption(String forJoursInterruption) {
        this.forJoursInterruption = forJoursInterruption;
    }

    public void setForMotifInterruption(String forMotifInterruption) {
        this.forMotifInterruption = forMotifInterruption;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJSimpleBaseIndemnisation.class;
    }
}
