package ch.globaz.pegasus.business.models.process.allocationsNoel;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCAccordeePopulation extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean hasRepresantLegal = false;
    private PersonneEtendueComplexModel personneEtendue = null;
    private SimpleDroit simpleDroit = null;
    private SimpleInformationsComptabilite simpleInformationsComptabilite = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimplePlanDeCalcul simplePlanDeCalcul = null;
    private SimplePrestationsAccordees simplePrestationsAccordees = null;

    public PCAccordeePopulation() {
        super();
        simplePCAccordee = new SimplePCAccordee();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        personneEtendue = new PersonneEtendueComplexModel();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        simpleDroit = new SimpleDroit();
        simpleInformationsComptabilite = new SimpleInformationsComptabilite();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simplePCAccordee.getIdPCAccordee();
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    public SimpleInformationsComptabilite getSimpleInformationsComptabilite() {
        return simpleInformationsComptabilite;
    }

    /**
     * @return the simplePCAccordee
     */
    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    /**
     * @return the simplePrestationsAccordees
     */
    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    public boolean isHasRepresantLegal() {
        return hasRepresantLegal;
    }

    public void setHasRepresantLegal(boolean hasRepresantLegal) {
        this.hasRepresantLegal = hasRepresantLegal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simplePCAccordee.setIdPCAccordee(id);

    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    public void setSimpleInformationsComptabilite(SimpleInformationsComptabilite simpleInformationsComptabilite) {
        this.simpleInformationsComptabilite = simpleInformationsComptabilite;
    }

    /**
     * @param simplePCAccordee
     *            the simplePCAccordee to set
     */
    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    /**
     * @param simplePrestationsAccordees
     *            the simplePrestationsAccordees to set
     */
    public void setSimplePrestationsAccordees(SimplePrestationsAccordees simplePrestationsAccordees) {
        this.simplePrestationsAccordees = simplePrestationsAccordees;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);
    }
}
