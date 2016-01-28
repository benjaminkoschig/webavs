package globaz.ij.vb.prononces;

import globaz.ij.db.prononces.IJPrononce;

/**
 * 
 * @author rco Crée le 24.06.2013 Modifié le 30.09.2013
 * 
 */
public class IJCorrigerDepuisPrononceViewBean extends IJAbstractPrononceProxyViewBean {

    /**
	 * 
	 */
    private String dateCorrection = null;

    /**
	 * 
	 */
    public IJCorrigerDepuisPrononceViewBean() {
        super(new IJPrononce());
    }

    /**
     * 
     * @param prononce
     */
    public IJCorrigerDepuisPrononceViewBean(IJPrononce prononce) {
        super(prononce);
    }

    /**
     * 
     * @return
     */
    public String getDateCorrection() {
        return dateCorrection;
    }

    /**
     * 
     * @param dateCorrection
     */
    public void setDateCorrection(String dateCorrection) {
        this.dateCorrection = dateCorrection;
    }

}
