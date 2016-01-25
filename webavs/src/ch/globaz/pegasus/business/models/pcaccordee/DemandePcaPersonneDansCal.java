package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

public class DemandePcaPersonneDansCal extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamille membreFamille = null;
    private SimpleDemande simpleDemande = null;
    private SimpleDroit simpleDroit = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul = null;
    private SimplePlanDeCalcul simplePlanDeCalcul = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    public DemandePcaPersonneDansCal() {
        super();
        simpleVersionDroit = new SimpleVersionDroit();
        simpleDroit = new SimpleDroit();
        simplePCAccordee = new SimplePCAccordee();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        simpleDemande = new SimpleDemande();
        membreFamille = new DroitMembreFamille();
        simplePersonneDansPlanCalcul = new SimplePersonneDansPlanCalcul();
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public DroitMembreFamille getMembreFamille() {
        return membreFamille;
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePersonneDansPlanCalcul getSimplePersonneDansPlanCalcul() {
        return simplePersonneDansPlanCalcul;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setMembreFamille(DroitMembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePersonneDansPlanCalcul(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul) {
        this.simplePersonneDansPlanCalcul = simplePersonneDansPlanCalcul;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
