/*
 * Cr�� le 12 d�c. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.masse;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.masse.AFConfirmationSalairesViewBean;
import globaz.naos.process.AFImpressionAcompteProcess;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFConfirmationSalairesHelper extends FWHelper {

    // Type de documents : 1 = Bouclement d'acompte
    // 2 = Pr�vision d'acompte
    // 3 = Decision d'acompte
    // 4 = Relev� � blanc

    public static final String typeDocument = "3";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFConfirmationSalairesViewBean csViewBean = (AFConfirmationSalairesViewBean) viewBean;
        AFImpressionAcompteProcess process = new AFImpressionAcompteProcess();

        try {

            process.setAffiliationId(csViewBean.getAffiliationId());
            process.setDateEnvoi(csViewBean.getDateEnvoi());
            process.setPlanAffiliationId(csViewBean.getPlanAffiliationId());
            process.setDateDebut(csViewBean.getDateEnvoi());
            process.setEMailAddress(csViewBean.getEmail());
            process.setFromIdExterneRole(csViewBean.getFromIdExterneRole());
            process.setTillIdExterneRole(csViewBean.getTillIdExterneRole());
            process.setTypeDocument(AFConfirmationSalairesHelper.typeDocument);
            process.setISession(session);

            BProcessLauncher.start(process);
        } catch (Exception e1) {
            csViewBean.setMessage(e1.getMessage());
            csViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
