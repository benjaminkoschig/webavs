package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class CommunicationOCCSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutRapport = null;
    private String forDateFinRapport = null;
    private String forIdCommunicationOCC = null;
    private String forIdLot = null;
    private List<String> forIdVersionDroitIn = null;

    public String getForDateDebutRapport() {
        return forDateDebutRapport;
    }

    public String getForDateFinRapport() {
        return forDateFinRapport;
    }

    public String getForIdCommunicationOCC() {
        return forIdCommunicationOCC;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public void setForDateDebutRapport(String forDateDebutRapport) {
        this.forDateDebutRapport = forDateDebutRapport;
    }

    public void setForDateFinRapport(String forDateFinRapport) {
        this.forDateFinRapport = forDateFinRapport;
    }

    public void setForIdCommunicationOCC(String forIdCommunicationOCC) {
        this.forIdCommunicationOCC = forIdCommunicationOCC;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<CommunicationOCC> whichModelClass() {
        return CommunicationOCC.class;
    }

    public List<String> getForIdVersionDroitIn() {
        return forIdVersionDroitIn;
    }

    public void setForIdVersionDroitIn(List<String> forIdVersionDroitIn) {
        this.forIdVersionDroitIn = forIdVersionDroitIn;
    }

}
