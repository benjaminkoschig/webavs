package globaz.aquila.vb.infos;

import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COEtapeInfoConfigViewBean extends COEtapeInfoConfig implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -6042262536724143926L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String libEtape;
    private String libSequence;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut lib etape.
     * 
     * @return la valeur courante de l'attribut lib etape
     */
    public String getLibEtape() {
        return libEtape;
    }

    /**
     * getter pour l'attribut lib sequence.
     * 
     * @return la valeur courante de l'attribut lib sequence
     */
    public String getLibSequence() {
        return libSequence;
    }

    /**
     * setter pour l'attribut lib etape.
     * 
     * @param libEtape
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibEtape(String libEtape) {
        this.libEtape = libEtape;
    }

    /**
     * setter pour l'attribut lib sequence.
     * 
     * @param libSequence
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibSequence(String libSequence) {
        this.libSequence = libSequence;
    }
}
