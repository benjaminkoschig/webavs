/*
 * Créé le 12 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
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
    // 2 = Prévision d'acompte
    // 3 = Decision d'acompte
    // 4 = Relevé à blanc

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
