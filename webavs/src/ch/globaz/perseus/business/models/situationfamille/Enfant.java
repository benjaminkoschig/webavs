/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DDE
 * 
 */
public class Enfant extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamille membreFamille = null;
    private SimpleEnfant simpleEnfant = null;

    public Enfant() {
        super();
        simpleEnfant = new SimpleEnfant();
        membreFamille = new MembreFamille();
    }

    @Override
    public String getId() {
        return simpleEnfant.getId();
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    /**
     * @return the simpleEnfant
     */
    public SimpleEnfant getSimpleEnfant() {
        return simpleEnfant;
    }

    @Override
    public String getSpy() {
        return simpleEnfant.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleEnfant.setId(id);
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    /**
     * @param simpleEnfant
     *            the simpleEnfant to set
     */
    public void setSimpleEnfant(SimpleEnfant simpleEnfant) {
        this.simpleEnfant = simpleEnfant;
    }

    @Override
    public void setSpy(String spy) {
        simpleEnfant.setSpy(spy);
    }

}
