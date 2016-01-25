/*
 * Cr�� le 9 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.releve;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.releve.AFGestionReleveViewBean;
import globaz.naos.process.AFGestionReleveProcess;

/**
 * Helper permettant de d�marrer le process de suivi des relev�s
 * 
 * @author sda
 */
public class AFGestionReleveHelper extends FWHelper {

    public AFGestionReleveHelper() {
        super();
    }

    /**
     * Start le process de suivi des relev�s
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        // On met les valeurs entr�es � l'�cran dans le viewBean
        AFGestionReleveViewBean gestionReleveViewBean = (AFGestionReleveViewBean) viewBean;
        AFGestionReleveProcess process = new AFGestionReleveProcess();
        // On passe toutes les donn�es entr�es � l'�cran dans le process
        process.setISession(session);
        process.setPeriode(gestionReleveViewBean.getPeriode());
        process.setGenererRappel(gestionReleveViewBean.getGenererRappel());
        process.setGenererSommation(gestionReleveViewBean.getGenererSommation());
        process.setGenererTaxation(gestionReleveViewBean.getGenererTaxation());
        process.setEMailAddress(gestionReleveViewBean.getEmail());

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
