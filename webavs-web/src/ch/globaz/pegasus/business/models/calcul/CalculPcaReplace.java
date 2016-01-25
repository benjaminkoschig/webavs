package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

public class CalculPcaReplace extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDroit = null;
    private String noVersion = null;
    private SimpleInformationsComptabilite simpleInformationsComptabilite = null;
    private SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimplePlanDeCalcul simplePlanDeCalcul = null;
    private SimplePrestationsAccordees simplePrestationsAccordees = null;
    private SimplePrestationsAccordees simplePrestationsAccordeesConjoint = null;

    public CalculPcaReplace() {
        super();
        simplePCAccordee = new SimplePCAccordee();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        simplePrestationsAccordeesConjoint = new SimplePrestationsAccordees();
        simpleInformationsComptabiliteConjoint = new SimpleInformationsComptabilite();
        simpleInformationsComptabilite = new SimpleInformationsComptabilite();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
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

    public String getIdDroit() {
        return idDroit;
    }

    public String getNoVersion() {
        return noVersion;
    }

    public SimpleInformationsComptabilite getSimpleInformationsComptabilite() {
        return simpleInformationsComptabilite;
    }

    public SimpleInformationsComptabilite getSimpleInformationsComptabiliteConjoint() {
        return simpleInformationsComptabiliteConjoint;
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

    public SimplePrestationsAccordees getSimplePrestationsAccordeesConjoint() {
        return simplePrestationsAccordeesConjoint;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simplePCAccordee.setIdPCAccordee(id);

    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void setSimpleInformationsComptabilite(SimpleInformationsComptabilite simpleInformationsComptabilite) {
        this.simpleInformationsComptabilite = simpleInformationsComptabilite;
    }

    public void setSimpleInformationsComptabiliteConjoint(
            SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint) {
        this.simpleInformationsComptabiliteConjoint = simpleInformationsComptabiliteConjoint;
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

    public void setSimplePrestationsAccordeesConjoint(SimplePrestationsAccordees simplePrestationsAccordeesConjoint) {
        this.simplePrestationsAccordeesConjoint = simplePrestationsAccordeesConjoint;
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
