package ch.globaz.pegasus.business.models.process.allocationsNoel;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Collection;

public class PCAccordeePopulationSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = null;
    private String forCsEtatDemande = null;
    private String forCsEtatPlanCalcul = null;
    private String forCsEtatVersionDroit = null;
    private String forCsSexe = null;
    private String forDateFin = null;
    private String forDateNaissance = null;
    private String forIdPCAccordee = null;
    private String forIdTiers = null;
    private Collection<String> forInIdsPca = new ArrayList<String>();
    private String isPCAccordeeSupprime = null;
    private String forDateDebut = null;

    /**
     * 
     * @return th forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsEtatPlanCalcul() {
        return forCsEtatPlanCalcul;
    }

    public String getForCsEtatVersionDroit() {
        return forCsEtatVersionDroit;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * 
     * @return the forIdPCAccordee
     */
    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    /**
     * 
     * @return th forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    public Collection<String> getForInIdsPca() {
        return forInIdsPca;
    }

    public String getIsPCAccordeeSupprime() {
        return isPCAccordeeSupprime;
    }

    /**
     * 
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsEtatPlanCalcul(String forCsEtatPlanCalcul) {
        this.forCsEtatPlanCalcul = forCsEtatPlanCalcul;
    }

    public void setForCsEtatVersionDroit(String forCsEtatVersionDroit) {
        this.forCsEtatVersionDroit = forCsEtatVersionDroit;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdPCAccordee
     *            the forIdPCAccordee to set
     */
    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    /**
     * 
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForInIdsPca(Collection<String> forInIdsPca) {
        this.forInIdsPca = forInIdsPca;
    }

    public void setIsPCAccordeeSupprime(String isPCAccordeeSupprime) {
        this.isPCAccordeeSupprime = isPCAccordeeSupprime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<PCAccordeePopulation> whichModelClass() {
        return PCAccordeePopulation.class;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

}
