package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIEcritureEclaterViewBean extends CIEcriture implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateClotureEclatement;
    private String ecritureIdAEclater;
    private String moisDebut1;
    private String moisDebut2;
    private String moisFin1;
    private String moisFin2;
    private String montant1;
    private String montant2;

    /**
     * Constructor for CIEcritureEclaterViewBean.
     */
    public CIEcritureEclaterViewBean() {
        super();
    }

    /**
     * Returns the dateClotureEclatement.
     * 
     * @return String
     */
    public String getDateClotureEclatement() {
        return dateClotureEclatement;
    }

    /**
     * Returns the ecritureIdAEclater.
     * 
     * @return String
     */
    public String getEcritureIdAEclater() {
        return ecritureIdAEclater;
    }

    /**
     * Returns the moisDebut1.
     * 
     * @return String
     */
    public String getMoisDebut1() {
        return moisDebut1;
    }

    /**
     * Returns the moisDebut2.
     * 
     * @return String
     */
    public String getMoisDebut2() {
        return moisDebut2;
    }

    /**
     * Returns the moisFin1.
     * 
     * @return String
     */
    public String getMoisFin1() {
        return moisFin1;
    }

    /**
     * Returns the moisFin2.
     * 
     * @return String
     */
    public String getMoisFin2() {
        return moisFin2;
    }

    /**
     * Returns the montant1.
     * 
     * @return String
     */
    public String getMontant1() {
        return montant1;
    }

    /**
     * Returns the montant2.
     * 
     * @return String
     */
    public String getMontant2() {
        return montant2;
    }

    /**
     * Sets the dateClotureEclatement.
     * 
     * @param dateClotureEclatement
     *            The dateClotureEclatement to set
     */
    public void setDateClotureEclatement(String dateClotureEclatement) {
        this.dateClotureEclatement = dateClotureEclatement;
    }

    /**
     * Sets the ecritureIdAEclater.
     * 
     * @param ecritureIdAEclater
     *            The ecritureIdAEclater to set
     */
    public void setEcritureIdAEclater(String ecritureIdAEclater) {
        this.ecritureIdAEclater = ecritureIdAEclater;
    }

    /**
     * Sets the moisDebut1.
     * 
     * @param moisDebut1
     *            The moisDebut1 to set
     */
    public void setMoisDebut1(String moisDebut1) {
        this.moisDebut1 = moisDebut1;
    }

    /**
     * Sets the moisDebut2.
     * 
     * @param moisDebut2
     *            The moisDebut2 to set
     */
    public void setMoisDebut2(String moisDebut2) {
        this.moisDebut2 = moisDebut2;
    }

    /**
     * Sets the moisFin1.
     * 
     * @param moisFin1
     *            The moisFin1 to set
     */
    public void setMoisFin1(String moisFin1) {
        this.moisFin1 = moisFin1;
    }

    /**
     * Sets the moisFin2.
     * 
     * @param moisFin2
     *            The moisFin2 to set
     */
    public void setMoisFin2(String moisFin2) {
        this.moisFin2 = moisFin2;
    }

    /**
     * Sets the montant1.
     * 
     * @param montant1
     *            The montant1 to set
     */
    public void setMontant1(String montant1) {
        this.montant1 = montant1;
    }

    /**
     * Sets the montant2.
     * 
     * @param montant2
     *            The montant2 to set
     */
    public void setMontant2(String montant2) {
        this.montant2 = montant2;
    }

}
