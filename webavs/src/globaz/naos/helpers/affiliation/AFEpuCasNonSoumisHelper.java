/*
 * Cr�� le 22 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFEpuCasNonSoumisViewBean;
import globaz.naos.process.AFEpuCasNonSoumisProcess;

/**
 * @author BJO
 * 
 */
public class AFEpuCasNonSoumisHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe AFAffiliationNAIndSansCIHelper.
     */
    public AFEpuCasNonSoumisHelper() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFEpuCasNonSoumisViewBean anViewBean = (AFEpuCasNonSoumisViewBean) viewBean;

        try {
            AFEpuCasNonSoumisProcess process = new AFEpuCasNonSoumisProcess((BSession) session);

            process.setEMailAddress(anViewBean.getEmail());
            process.setDate(anViewBean.getFromDate());
            process.setSimulation(anViewBean.getSimulation());

            BProcessLauncher.start(process);
        } catch (Exception e) {
            anViewBean.setMessage(e.getMessage());
            anViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
