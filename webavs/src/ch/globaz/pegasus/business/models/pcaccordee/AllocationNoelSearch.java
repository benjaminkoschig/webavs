package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AllocationNoelSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String forIdPCAccordee = null;

    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    @Override
    public Class whichModelClass() {
        // TODO Auto-generated method stub
        return AllocationNoel.class;
    }

}
