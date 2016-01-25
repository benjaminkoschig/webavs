package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class VersionDroitPCAPlanDeCacule extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneEtendueComplexModel personneEtendue = null;
    private PersonneEtendueComplexModel personneEtendueConjoint = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimplePlanDeCalcul simplePlanDeCalcul = null;
    private SimplePrestationsAccordees simplePrestationsAccordees = null;
    private SimplePrestationsAccordees simplePrestationsAccordeesConjoint = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    public VersionDroitPCAPlanDeCacule() {
        personneEtendue = new PersonneEtendueComplexModel();
        personneEtendueConjoint = new PersonneEtendueComplexModel();
        simplePCAccordee = new SimplePCAccordee();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        simpleVersionDroit = new SimpleVersionDroit();
        simplePrestationsAccordeesConjoint = new SimplePrestationsAccordees();
    }

    @Override
    public String getId() {
        return getSimplePCAccordee().getId();
    }

    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public PersonneEtendueComplexModel getPersonneEtendueConjoint() {
        return personneEtendueConjoint;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordeesConjoint() {
        return simplePrestationsAccordeesConjoint;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        return getSimplePCAccordee().getSpy();
    }

    @Override
    public void setId(String id) {
        simplePCAccordee.setId(id);
    }

    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    public void setPersonneEtendueConjoint(PersonneEtendueComplexModel personneEtendueConjoint) {
        this.personneEtendueConjoint = personneEtendueConjoint;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    public void setSimplePrestationsAccordees(SimplePrestationsAccordees simplePrestationsAccordees) {
        this.simplePrestationsAccordees = simplePrestationsAccordees;
    }

    public void setSimplePrestationsAccordeesConjoint(SimplePrestationsAccordees simplePrestationsAccordeesConjoint) {
        this.simplePrestationsAccordeesConjoint = simplePrestationsAccordeesConjoint;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);
    }

}
