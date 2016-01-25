package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CICompteIndividuelExtournerViewBean extends CICompteIndividuel implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String anneeDebut = "";

    private String anneeFin = "";

    /**
     * Constructor for CICompteIndividuelExtournerViewBean.
     */
    private CICompteIndividuel ci = null;

    private String compteIndividuelIdDestination = "";
    private String compteIndividuelIdSource = "";

    public CICompteIndividuelExtournerViewBean() throws java.lang.Exception {
    }

    /**
     * Returns the anneeDebut.
     * 
     * @return String
     */
    public String getAnneeDebut() {
        return anneeDebut;
    }

    /**
     * Returns the anneeFin.
     * 
     * @return String
     */
    public String getAnneeFin() {
        return anneeFin;
    }

    public CICompteIndividuel getCi() {
        ci = new CICompteIndividuel();
        ci.setSession(getSession());
        ci.setCompteIndividuelId(getCompteIndividuelId());
        try {
            ci.retrieve();
        } catch (Exception e) {
            ci = null;
        }
        return ci;
    }

    /**
     * Returns the compteIndividuelIdDestination.
     * 
     * @return String
     */
    public String getCompteIndividuelIdDestination() {
        return compteIndividuelIdDestination;
    }

    /**
     * Returns the compteIndividuelIdSource.
     * 
     * @return String
     */
    public String getCompteIndividuelIdSource() {
        return compteIndividuelIdSource;
    }

    /**
     * Sets the anneeDebut.
     * 
     * @param anneeDebut
     *            The anneeDebut to set
     */
    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    /**
     * Sets the anneeFin.
     * 
     * @param anneeFin
     *            The anneeFin to set
     */
    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    /**
     * Sets the compteIndividuelIdDestination.
     * 
     * @param compteIndividuelIdDestination
     *            The compteIndividuelIdDestination to set
     */
    public void setCompteIndividuelIdDestination(String compteIndividuelIdDestination) {
        this.compteIndividuelIdDestination = compteIndividuelIdDestination;
    }

    /**
     * Sets the compteIndividuelIdSource.
     * 
     * @param compteIndividuelIdSource
     *            The compteIndividuelIdSource to set
     */
    public void setCompteIndividuelIdSource(String compteIndividuelIdSource) {
        this.compteIndividuelIdSource = compteIndividuelIdSource;
    }

}
