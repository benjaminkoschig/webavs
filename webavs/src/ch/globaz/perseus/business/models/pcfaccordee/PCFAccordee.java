/**
 * 
 */
package ch.globaz.perseus.business.models.pcfaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.models.demande.Demande;

/**
 * @author DDE
 * 
 */
public class PCFAccordee extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private OutputCalcul calcul = null;
    private Demande demande = null;
    private SimplePCFAccordee simplePCFAccordee = null;

    /**
	 * 
	 */
    public PCFAccordee() {
        super();
        demande = new Demande();
        simplePCFAccordee = new SimplePCFAccordee();
        calcul = new OutputCalcul();
    }

    /**
     * @return the calcul
     */
    public OutputCalcul getCalcul() {
        return calcul;
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simplePCFAccordee.getId();
    }

    /**
     * @return the simplePCFAccordee
     */
    public SimplePCFAccordee getSimplePCFAccordee() {
        return simplePCFAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simplePCFAccordee.getSpy();
    }

    /**
     * @param calcul
     *            the calcul to set
     */
    public void setCalcul(OutputCalcul calcul) {
        this.calcul = calcul;
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simplePCFAccordee.setId(id);
    }

    /**
     * @param simplePCFAccordee
     *            the simplePCFAccordee to set
     */
    public void setSimplePCFAccordee(SimplePCFAccordee simplePCFAccordee) {
        this.simplePCFAccordee = simplePCFAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simplePCFAccordee.setSpy(spy);
    }
}
