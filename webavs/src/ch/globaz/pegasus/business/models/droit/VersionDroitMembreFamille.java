package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.hera.business.models.famille.MembreFamille;

public class VersionDroitMembreFamille extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamille membreFamille = null;
    private SimpleDroitMembreFamille simpleDroitMembreFamille = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    public VersionDroitMembreFamille() {
        membreFamille = new MembreFamille();
        simpleDroitMembreFamille = new SimpleDroitMembreFamille();
        simpleVersionDroit = new SimpleVersionDroit();
    }

    @Override
    public String getId() {
        return simpleDroitMembreFamille.getId();
    }

    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    public SimpleDroitMembreFamille getSimpleDroitMembreFamille() {
        return simpleDroitMembreFamille;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        return simpleDroitMembreFamille.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDroitMembreFamille.setId(id);
    }

    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    public void setSimpleDroitMembreFamille(SimpleDroitMembreFamille simpleDroitMembreFamille) {
        this.simpleDroitMembreFamille = simpleDroitMembreFamille;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        setSpy(spy);
    }

}
