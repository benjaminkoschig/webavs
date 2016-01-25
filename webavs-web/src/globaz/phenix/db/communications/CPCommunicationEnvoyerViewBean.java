package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CPCommunicationEnvoyerViewBean extends CPCommunicationFiscaleAffichageViewBean implements
        BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEdition;
    private String EMailAddress;
    private String filename;
    private String throughSedex;
    private Boolean donneesCommerciales = new Boolean(false);
    private Boolean donneesPrivees = new Boolean(false);

    /**
     * @return
     */
    public String getDateEdition() {
        return dateEdition;
    }

    public Boolean getDonneesCommerciales() {
        return donneesCommerciales;
    }

    public Boolean getDonneesPrivees() {
        return donneesPrivees;
    }

    /**
     * @return
     */
    public String getEMailAddress() {
        return EMailAddress;
    }

    public String getFilename() {
        return filename;
    }

    public String getThroughSedex() {
        return throughSedex;
    }

    /**
     * @param string
     */
    public void setDateEdition(String date) {
        dateEdition = date;
    }

    public void setDonneesCommerciales(Boolean donneesCommerciales) {
        this.donneesCommerciales = donneesCommerciales;
    }

    public void setDonneesPrivees(Boolean donneesPrivees) {
        this.donneesPrivees = donneesPrivees;
    }

    /**
     * @param string
     */
    public void setEMailAddress(String string) {
        EMailAddress = string;
    }

    public void setFilename(String file) {
        filename = file;
    }

    public void setThroughSedex(String throughSedex) {
        this.throughSedex = throughSedex;
    }

}
