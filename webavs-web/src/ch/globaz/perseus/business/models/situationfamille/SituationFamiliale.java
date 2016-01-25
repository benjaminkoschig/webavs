/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DDE
 * 
 */
public class SituationFamiliale extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Conjoint conjoint = null;
    private Requerant requerant = null;
    private SimpleSituationFamiliale simpleSituationFamiliale = null;

    public SituationFamiliale() {
        super();
        simpleSituationFamiliale = new SimpleSituationFamiliale();
        conjoint = new Conjoint();
        requerant = new Requerant();
    }

    /**
     * @return the conjoint
     */
    public Conjoint getConjoint() {
        return conjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleSituationFamiliale.getId();
    }

    /**
     * @return the requerant
     */
    public Requerant getRequerant() {
        return requerant;
    }

    /**
     * @return the simpleSituationFamiliale
     */
    public SimpleSituationFamiliale getSimpleSituationFamiliale() {
        return simpleSituationFamiliale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleSituationFamiliale.getSpy();
    }

    /**
     * @param conjoint
     *            the conjoint to set
     */
    public void setConjoint(Conjoint conjoint) {
        this.conjoint = conjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleSituationFamiliale.setId(id);
    }

    /**
     * @param requerant
     *            the requerant to set
     */
    public void setRequerant(Requerant requerant) {
        this.requerant = requerant;
    }

    /**
     * @param simpleSituationFamiliale
     *            the simpleSituationFamiliale to set
     */
    public void setSimpleSituationFamiliale(SimpleSituationFamiliale simpleSituationFamiliale) {
        this.simpleSituationFamiliale = simpleSituationFamiliale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleSituationFamiliale.setSpy(spy);
    }

}
