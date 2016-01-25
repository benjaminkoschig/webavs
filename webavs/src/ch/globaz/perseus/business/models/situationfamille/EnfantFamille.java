/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DDE
 * 
 */
public class EnfantFamille extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Enfant enfant = null;
    private SimpleEnfantFamille simpleEnfantFamille = null;

    public EnfantFamille() {
        super();
        simpleEnfantFamille = new SimpleEnfantFamille();
        enfant = new Enfant();
    }

    /**
     * @return the enfant
     */
    public Enfant getEnfant() {
        return enfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleEnfantFamille.getId();
    }

    /**
     * Retourne une version lisible (avec un O ou rien) si l'enfant est respectivement à l'AI ou pas
     * 
     * @return
     */
    public String getIsAI() {
        return (enfant.getMembreFamille().getSimpleMembreFamille().getIsAI()) ? "O" : "";
    }

    /**
     * @return the simpleEnfantFamille
     */
    public SimpleEnfantFamille getSimpleEnfantFamille() {
        return simpleEnfantFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleEnfantFamille.getSpy();
    }

    /**
     * @param enfant
     *            the enfant to set
     */
    public void setEnfant(Enfant enfant) {
        this.enfant = enfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleEnfantFamille.setId(id);
    }

    /**
     * @param simpleEnfantFamille
     *            the simpleEnfantFamille to set
     */
    public void setSimpleEnfantFamille(SimpleEnfantFamille simpleEnfantFamille) {
        this.simpleEnfantFamille = simpleEnfantFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleEnfantFamille.setSpy(spy);
    }

}
