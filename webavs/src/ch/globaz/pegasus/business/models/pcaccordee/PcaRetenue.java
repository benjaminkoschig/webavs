package ch.globaz.pegasus.business.models.pcaccordee;

import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.pegasus.businessimpl.utils.PegasusSelectField;

public class PcaRetenue extends PegasusSelectField {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String csRoleFamillePC;
    public String idPCAccordee;
    public SimpleRetenuePayement simpleRetenue;

    public PcaRetenue() {
        simpleRetenue = new SimpleRetenuePayement();
    }

    public String getCsRoleFamillePC() {
        return csRoleFamillePC;
    }

    @Override
    public String getId() {
        return simpleRetenue.getId();
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public SimpleRetenuePayement getSimpleRetenue() {
        return simpleRetenue;
    }

    public void setCsRoleFamillePC(String csRoleFamillePC) {
        this.csRoleFamillePC = csRoleFamillePC;
    }

    @Override
    public void setId(String id) {
        simpleRetenue.setId(id);
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setSimpleRetenue(SimpleRetenuePayement simpleRetenue) {
        this.simpleRetenue = simpleRetenue;
    }
}
