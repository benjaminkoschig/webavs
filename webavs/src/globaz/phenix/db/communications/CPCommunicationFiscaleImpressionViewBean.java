package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CPCommunicationFiscaleImpressionViewBean extends CPCommunicationFiscaleAffichageViewBean implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEdition;
    private String EMailAddress;

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

    @Override
    public String getNomPrenom() {
        String nomPrenom = "";
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(getIdTiers());
                tiers.retrieve();
                if (tiers != null && !tiers.isNew()) {
                    nomPrenom = tiers.getPrenomNom();
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return nomPrenom;
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
}
