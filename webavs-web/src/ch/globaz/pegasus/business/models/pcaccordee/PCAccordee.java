package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCAccordee extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private SimpleJoursAppoint simpleJoursAppoint = null;
    private List<SimpleJoursAppoint> listeJoursAppoint = null;
    private PersonneEtendueComplexModel personneEtendue = null;
    private PersonneEtendueComplexModel personneEtendueConjoint = null;

    private ArrayList<SimplePlanDeCalcul> planCalculs = null;

    private SimpleDroit simpleDroit = null;
    private SimpleInformationsComptabilite simpleInformationsComptabilite = null;
    private SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimplePrestationsAccordees simplePrestationsAccordees = null;
    private SimplePrestationsAccordees simplePrestationsAccordeesConjoint = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    public PCAccordee() {
        super();
        simplePCAccordee = new SimplePCAccordee();
        simpleVersionDroit = new SimpleVersionDroit();
        simpleDroit = new SimpleDroit();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        simplePrestationsAccordeesConjoint = new SimplePrestationsAccordees();
        simpleInformationsComptabiliteConjoint = new SimpleInformationsComptabilite();
        simpleInformationsComptabilite = new SimpleInformationsComptabilite();
        personneEtendue = new PersonneEtendueComplexModel();
        personneEtendueConjoint = new PersonneEtendueComplexModel();
        // this.simpleJoursAppoint = new SimpleJoursAppoint();
        listeJoursAppoint = new ArrayList<SimpleJoursAppoint>();
    }

    public SimpleJoursAppoint getFirstJoursAppoint() {
        if (listeJoursAppoint.size() > 0) {
            return listeJoursAppoint.get(0);
        } else {
            return null;
        }
    }

    public void addJoursAppoint(SimpleJoursAppoint jAppoint) {
        listeJoursAppoint.add(jAppoint);
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

    public List<SimpleJoursAppoint> getListeJoursAppoint() {
        return listeJoursAppoint;
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    public PersonneEtendueComplexModel getPersonneEtendueConjoint() {
        return personneEtendueConjoint;
    }

    /**
     * @return the planCalculs
     */
    public ArrayList<SimplePlanDeCalcul> getPlanCalculs() {
        return planCalculs;
    }

    public SimplePlanDeCalcul getPlanRetenu() throws PCAccordeeException {
        if (planCalculs == null) {
            throw new PCAccordeeException("No plan calcul found!");
        }
        for (SimplePlanDeCalcul plan : planCalculs) {
            if (plan.getIsPlanRetenu()) {
                return plan;
            }
        }
        return null;

    }

    /**
     * @return the simpleDroit
     */
    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    // public SimpleJoursAppoint getSimpleJoursAppoint() {
    // return this.simpleJoursAppoint;
    // }

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

    /**
     * @return the simplePrestationsAccordees
     */
    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordeesConjoint() {
        return simplePrestationsAccordeesConjoint;
    }

    /**
     * @return the simpleVersionDroit
     */
    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
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

    public void loadPlanCalculs() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (getSimplePCAccordee() == null) {
            throw new PCAccordeeException("Unable to loadPlanCalculs , the pca model passed is null!");
        }
        if (JadeStringUtil.isBlankOrZero(getSimplePCAccordee().getId())) {
            throw new PCAccordeeException("Unable to loadPlanCalculs, the id pca passed is null!");
        }

        SimplePlanDeCalculSearch planSearch = new SimplePlanDeCalculSearch();
        planSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        planSearch.setForIdPCAccordee(getSimplePCAccordee().getId());
        ArrayList<SimplePlanDeCalcul> listePlanCalculs = new ArrayList<SimplePlanDeCalcul>();

        for (SimplePlanDeCalcul plan : PegasusServiceLocator.getPCAccordeeService().searchPlanCalcul(planSearch)) {
            listePlanCalculs.add(plan);
        }
        setPlanCalculs(listePlanCalculs);
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

    public void setPersonneEtendueConjoint(PersonneEtendueComplexModel personneEtendueConjoint) {
        this.personneEtendueConjoint = personneEtendueConjoint;
    }

    // public void setPlanRetenu(SimplePlanDeCalcul planRetenu) {
    // if (this.planCalculs == null) {
    // this.planCalculs.add(planRetenu);
    // }
    // }

    /**
     * @param planCalculs
     *            the planCalculs to set
     */
    public void setPlanCalculs(ArrayList<SimplePlanDeCalcul> planCalculs) {
        this.planCalculs = planCalculs;
    }

    /**
     * @param simpleDroit
     *            the simpleDroit to set
     */
    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    public void setSimpleInformationsComptabilite(SimpleInformationsComptabilite simpleInformationsComptabilite) {
        this.simpleInformationsComptabilite = simpleInformationsComptabilite;
    }

    // public void setSimpleJoursAppoint(SimpleJoursAppoint simpleJoursAppoint) {
    // this.simpleJoursAppoint = simpleJoursAppoint;
    // }

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

    /**
     * @param simpleVersionDroit
     *            the simpleVersionDroit to set
     */
    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
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
