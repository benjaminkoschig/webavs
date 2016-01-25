/**
 * 
 */
package ch.globaz.perseus.business.models.demande;

import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordee;

/**
 * @author MBO
 * 
 */
public class DemandeEtendue extends Demande {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimplePCFAccordee simplePcfAccordee = null;

    public DemandeEtendue() {
        super();
        simplePcfAccordee = new SimplePCFAccordee();
    }

    public SimplePCFAccordee getSimplePcfAccordee() {
        return simplePcfAccordee;
    }

    public void setSimplePcfAccordee(SimplePCFAccordee simplePcfAccordee) {
        this.simplePcfAccordee = simplePcfAccordee;
    }

}
