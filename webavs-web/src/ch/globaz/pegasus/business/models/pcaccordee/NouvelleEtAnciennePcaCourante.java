package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;

public class NouvelleEtAnciennePcaCourante extends JadeComplexModel {

    private static final long serialVersionUID = 1L;

    private SimplePCAccordee nouvellePca = new SimplePCAccordee();
    private SimplePrestationsAccordees nouvellePrestationsAccordees = new SimplePrestationsAccordees();
    private SimplePrestationsAccordees nouvellePrestationsAccordeesConjoint = new SimplePrestationsAccordees();
    private SimplePlanDeCalcul nouveauPlanDeCalcul = new SimplePlanDeCalcul();
    private SimplePCAccordee anciennePca = new SimplePCAccordee();
    private SimplePlanDeCalcul ancienPlanDeCalcul = new SimplePlanDeCalcul();
    private String idDroit;

    public NouvelleEtAnciennePcaCourante() {
        super();
    }

    public SimplePlanDeCalcul getNouveauPlanDeCalcul() {
        return nouveauPlanDeCalcul;
    }

    public void setNouveauPlanDeCalcul(SimplePlanDeCalcul nouveauPlanDeCalcul) {
        this.nouveauPlanDeCalcul = nouveauPlanDeCalcul;
    }

    public SimplePCAccordee getAnciennePca() {
        return anciennePca;
    }

    public SimplePrestationsAccordees getNouvellePrestationsAccordees() {
        return nouvellePrestationsAccordees;
    }

    public void setNouvellePrestationsAccordees(SimplePrestationsAccordees nouvellePrestationsAccordees) {
        this.nouvellePrestationsAccordees = nouvellePrestationsAccordees;
    }

    public SimplePrestationsAccordees getNouvellePrestationsAccordeesConjoint() {
        return nouvellePrestationsAccordeesConjoint;
    }

    public void setNouvellePrestationsAccordeesConjoint(SimplePrestationsAccordees nouvellePrestationsAccordeesConjoint) {
        this.nouvellePrestationsAccordeesConjoint = nouvellePrestationsAccordeesConjoint;
    }

    public void setAnciennePca(SimplePCAccordee anciennePca) {
        this.anciennePca = anciennePca;
    }

    public SimplePlanDeCalcul getAncienPlanDeCalcul() {
        return ancienPlanDeCalcul;
    }

    public void setAncienPlanDeCalcul(SimplePlanDeCalcul ancienPlanDeCalcul) {
        this.ancienPlanDeCalcul = ancienPlanDeCalcul;
    }

    public SimplePCAccordee getNouvellePca() {
        return nouvellePca;
    }

    public void setNouvellePca(SimplePCAccordee nouvellePca) {
        this.nouvellePca = nouvellePca;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public void setSpy(String spy) {

    }

}
