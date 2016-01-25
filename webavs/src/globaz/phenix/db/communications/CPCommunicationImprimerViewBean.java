package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliationViewBean;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CPCommunicationImprimerViewBean extends CPCommunicationFiscaleAffichageViewBean implements
        BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEdition;
    private String EMailAddress;

    private Boolean impressionListe = new Boolean(false);

    /**
     * @return
     */
    public String getDateEdition() {
        return dateEdition;
    }

    public String getDebutActivite() {
        String debutActivite = "";

        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
                AFAffiliationViewBean affi = new AFAffiliationViewBean();
                affi.setSession(getSession());
                affi.setId(getIdAffiliation());
                affi.retrieve();
                if (affi != null && !affi.isNew()) {
                    debutActivite = affi.getDateDebut();
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return debutActivite;

    }

    /**
     * @return
     */
    public String getEMailAddress() {
        return EMailAddress;
    }

    public Boolean getImpressionListe() {
        return impressionListe;
    }

    /**
     * @param string
     */
    public void setDateEdition(String date) {
        dateEdition = date;
    }

    /**
     * @param string
     */
    public void setEMailAddress(String string) {
        EMailAddress = string;
    }

    // public String getCanton() {
    // try{
    // TITiersViewBean tiers = new TITiersViewBean ();
    // tiers.setSession(getSession());
    // tiers.setIdTiers(getIdTiers());
    // tiers.retrieve();
    // if (tiers!=null && !tiers.isNew()) {
    // return tiers.getCanton();
    // }
    // }catch(Exception e){
    // JadeLogger.error(this, e);
    // }
    // return "";
    // }

    public void setImpressionListe(Boolean impressionListe) {
        this.impressionListe = impressionListe;
    }
}
