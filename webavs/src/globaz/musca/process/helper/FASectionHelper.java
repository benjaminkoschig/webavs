package globaz.musca.process.helper;

import globaz.framework.util.FWCurrency;
import globaz.osiris.api.APISection;

/**
 * Classe : type_conteneur Description : Date de création: 10 sept. 04
 * 
 * @author scr
 */
public class FASectionHelper {

    public static int A_COMPENSER = 3;
    public static int COMPENSEE = 1;
    public static int PARTIELLEMENT_COMPENSEE = 2;

    private FWCurrency montantACompenser = null;
    private APISection section = null;
    private int status = FASectionHelper.A_COMPENSER;

    /**
     * Constructor for FASectionHelper.
     */
    public FASectionHelper(APISection section) {
        this.section = section;
        status = FASectionHelper.A_COMPENSER;
        montantACompenser = new FWCurrency(section.getSolde());
    }

    /**
     * Returns the montantACompenser.
     * 
     * @return FWCurrency
     */
    public FWCurrency getMontantACompenser() {
        return montantACompenser;
    }

    /**
     * Returns the section.
     * 
     * @return APISection
     */
    public APISection getSection() {
        return section;
    }

    /**
     * Returns the status.
     * 
     * @return int
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the montantACompenser.
     * 
     * @param montantACompenser
     *            The montantACompenser to set
     */
    public void setMontantACompenser(FWCurrency montantACompenser) {
        this.montantACompenser = montantACompenser;
    }

    /**
     * Sets the section.
     * 
     * @param section
     *            The section to set
     */
    public void setSection(APISection section) {
        this.section = section;
    }

    /**
     * Sets the status.
     * 
     * @param status
     *            The status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

}
