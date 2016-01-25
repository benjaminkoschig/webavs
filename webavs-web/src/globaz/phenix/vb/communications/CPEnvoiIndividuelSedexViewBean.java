package globaz.phenix.vb.communications;

import globaz.phenix.db.CPAbstractViewBean;

/**
 * Bean représentant l'écrans de réinjection
 * 
 * @author JPA
 * @revision JPA 29.09.2010
 */
public class CPEnvoiIndividuelSedexViewBean extends CPAbstractViewBean {
    private Boolean donneesCommerciales = new Boolean(false);
    private Boolean donneesPrivees = new Boolean(false);
    private String eMailAddress = "";
    private Boolean envoiImmediat = new Boolean(false);
    private Boolean lifd = new Boolean(false);

    /**
     * Constructeur de CEReinjectionViewBean
     */
    public CPEnvoiIndividuelSedexViewBean() {
        super();
    }

    public Boolean getDonneesCommerciales() {
        return donneesCommerciales;
    }

    public Boolean getDonneesPrivees() {
        return donneesPrivees;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public Boolean getEnvoiImmediat() {
        return envoiImmediat;
    }

    public Boolean getLifd() {
        return lifd;
    }

    public void setDonneesCommerciales(Boolean donneesCommerciales) {
        this.donneesCommerciales = donneesCommerciales;
    }

    public void setDonneesPrivees(Boolean donneesPrivees) {
        this.donneesPrivees = donneesPrivees;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setEnvoiImmediat(Boolean envoiImmediat) {
        this.envoiImmediat = envoiImmediat;
    }

    public void setLifd(Boolean lifd) {
        this.lifd = lifd;
    }
}
