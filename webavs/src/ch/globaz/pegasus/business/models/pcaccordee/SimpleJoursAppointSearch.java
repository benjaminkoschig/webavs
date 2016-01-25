package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleJoursAppointSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPCAccordee = null;
    private List<String> inIdsPca = null;

    /**
     * @return the forIdPCAccordee
     */
    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public List<String> getInIdsPca() {
        return inIdsPca;
    }

    /**
     * @param forIdPCAccordee
     *            the forIdPCAccordee to set
     */
    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setInIdsPca(List<String> inIdsPca) {
        this.inIdsPca = inIdsPca;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleJoursAppoint> whichModelClass() {
        return SimpleJoursAppoint.class;
    }

}
