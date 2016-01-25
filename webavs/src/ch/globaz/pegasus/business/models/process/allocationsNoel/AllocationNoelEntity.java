package ch.globaz.pegasus.business.models.process.allocationsNoel;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.jade.process.business.enumProcess.JadeProcessEntityStateEnum;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;

public class AllocationNoelEntity extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JadeProcessEntityStateEnum csEtat;
    private String csGenrePca;
    private String description = null;
    private Boolean isManual = false;
    private String montantConjoint;
    private String montantRequerant;
    private SimpleAllocationNoel simpleAllocationNoel = null;

    public AllocationNoelEntity() {
        simpleAllocationNoel = new SimpleAllocationNoel();
    }

    public JadeProcessEntityStateEnum getCsEtat() {
        return csEtat;
    }

    public String getCsGenrePca() {
        return csGenrePca;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getId() {
        return simpleAllocationNoel.getIdAllocationNoel();
    }

    public Boolean getIsManual() {
        return isManual;
    }

    public String getMontantConjoint() {
        return montantConjoint;
    }

    public String getMontantRequerant() {
        return montantRequerant;
    }

    public SimpleAllocationNoel getSimpleAllocationNoel() {
        return simpleAllocationNoel;
    }

    @Override
    public String getSpy() {
        return simpleAllocationNoel.getSpy();
    }

    public void setCsEtat(JadeProcessEntityStateEnum csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGenrePca(String csGenrePca) {
        this.csGenrePca = csGenrePca;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(String id) {
        simpleAllocationNoel.setIdAllocationNoel(id);
    }

    public void setIsManual(Boolean isManual) {
        this.isManual = isManual;
    }

    public void setMontantConjoint(String montantConjoint) {
        this.montantConjoint = montantConjoint;
    }

    public void setMontantRequerant(String montantRequerant) {
        this.montantRequerant = montantRequerant;
    }

    public void setSimpleAllocationNoel(SimpleAllocationNoel simpleAllocationNoel) {
        this.simpleAllocationNoel = simpleAllocationNoel;
    }

    @Override
    public void setSpy(String spy) {
        simpleAllocationNoel.setSpy(spy);
    }
}
