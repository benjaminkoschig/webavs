package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;

public class PlanDeCalculWitMembreFamille extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamille droitMembreFamille = null;
    private SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul = null;
    private SimplePlanDeCalcul simplePlanDeCalcul = null;
    private SimpleDonneesPersonnelles simpleDonneesPersonnelles = null;
    private SimplePCAccordee simplePCAccordee = null;
    private String idVersionDroit;

    public PlanDeCalculWitMembreFamille() {
        super();
        simplePersonneDansPlanCalcul = new SimplePersonneDansPlanCalcul();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        droitMembreFamille = new DroitMembreFamille();
        simpleDonneesPersonnelles = new SimpleDonneesPersonnelles();
        setSimplePCAccordee(new SimplePCAccordee());

    }

    public DroitMembreFamille getDroitMembreFamille() {
        return droitMembreFamille;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    @Override
    public String getId() {
        return simplePlanDeCalcul.getIdPlanDeCalcul();
    }

    public SimplePersonneDansPlanCalcul getSimplePersonneDansPlanCalcul() {
        return simplePersonneDansPlanCalcul;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    @Override
    public String getSpy() {
        return simplePlanDeCalcul.getSpy();
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setDroitMembreFamille(DroitMembreFamille droitMembreFamille) {
        this.droitMembreFamille = droitMembreFamille;
    }

    @Override
    public void setId(String id) {
        simplePlanDeCalcul.setIdPlanDeCalcul(id);
    }

    public void setSimplePersonneDansPlanCalcul(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul) {
        this.simplePersonneDansPlanCalcul = simplePersonneDansPlanCalcul;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    @Override
    public void setSpy(String spy) {
        simplePlanDeCalcul.setSpy(spy);
    }

    public SimpleDonneesPersonnelles getSimpleDonneesPersonnelles() {
        return simpleDonneesPersonnelles;
    }

    public void setSimpleDonneesPersonnelles(SimpleDonneesPersonnelles simpleDonneesPersonnelles) {
        this.simpleDonneesPersonnelles = simpleDonneesPersonnelles;
    }

}
