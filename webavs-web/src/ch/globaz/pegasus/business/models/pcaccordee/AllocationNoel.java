package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class AllocationNoel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PersonneEtendueComplexModel personneEtendue = null;

    private SimpleAllocationNoel simpleAllocationNoel = null;

    private SimplePCAccordee simplePCAccordee = null;

    private SimplePrestationsAccordees simplePrestationsAccordees = null;

    private SimplePrestationsAccordees simplePrestationsAccordeesConjoint = null;

    public AllocationNoel() {
        simpleAllocationNoel = new SimpleAllocationNoel();

        simplePCAccordee = new SimplePCAccordee();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        simplePrestationsAccordeesConjoint = new SimplePrestationsAccordees();
        personneEtendue = new PersonneEtendueComplexModel();
    }

    @Override
    public String getId() {
        return simpleAllocationNoel.getIdAllocationNoel();
    }

    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public SimpleAllocationNoel getSimpleAllocationNoel() {
        return simpleAllocationNoel;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordeesConjoint() {
        return simplePrestationsAccordeesConjoint;
    }

    @Override
    public String getSpy() {
        return simpleAllocationNoel.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleAllocationNoel.setIdAllocationNoel(id);

    }

    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    public void setSimpleAllocationNoel(SimpleAllocationNoel simpleAllocationNoel) {
        this.simpleAllocationNoel = simpleAllocationNoel;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePrestationsAccordees(SimplePrestationsAccordees simplePrestationsAccordees) {
        this.simplePrestationsAccordees = simplePrestationsAccordees;
    }

    public void setSimplePrestationsAccordeesConjoint(SimplePrestationsAccordees simplePrestationsAccordeesConjoint) {
        this.simplePrestationsAccordeesConjoint = simplePrestationsAccordeesConjoint;
    }

    @Override
    public void setSpy(String spy) {
        simpleAllocationNoel.setSpy(spy);

    }

}
