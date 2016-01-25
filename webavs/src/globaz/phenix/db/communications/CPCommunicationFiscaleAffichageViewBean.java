/*
 * Created on 24 nov. 05
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.log.JadeLogger;

/**
 * @author mar
 */
public class CPCommunicationFiscaleAffichageViewBean extends CPCommunicationFiscaleAffichage implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CPCommunicationFiscaleAffichageViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getNomPrenom() {// CPCommunicationFiscaleAffichage entity) {
        String nomPrenom = "";
        try {
            String nom = getNom();
            String prenom = getPrenom();
            nomPrenom = nom + " " + prenom;
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return nomPrenom;
    }

}
