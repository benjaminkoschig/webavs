package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;

/**
 * Insérez la description du type ici. Date de création : (27.11.2002 10:37:05)
 * 
 * @author: Administrator
 */
public class DSDeclarationHelper extends FWHelper {
    /**
     * Commentaire relatif au constructeur TITiersHelper.
     */
    public DSDeclarationHelper() {
        super();
    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {
        if (action.getActionPart().equals("customFind")) {
            viewBean.setMsgType(FWViewBeanInterface.OK);
            viewBean.setMessage("");
            DSDeclarationViewBean vBean = (DSDeclarationViewBean) viewBean;
            // Sous contrôle d'exceptions
            try {
                // Récupérer la déclaration
                DSDeclarationViewBean decl = new DSDeclarationViewBean();
                decl.setISession(session);
                decl.setIdDeclaration(vBean.getIdDeclaration());
                decl.retrieve();
                // Contrôler si la déclaration peut être lue
                if (decl.hasErrors() || decl.isNew()) {
                    viewBean.setMessage("Impossible de trouver la déclaration");
                }
                // Contrôler l'état de la déclaration
                if (!decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_OUVERT)) {
                    viewBean.setMessage("La déclaration doit être ouverte pour être validée");
                }
                // Mise à jour de l'état de la déclaration
                decl.setEtat(DSDeclarationViewBean.CS_AFACTURER);
                decl.setEnValidation(true);
                decl.update();
                if (decl.hasErrors()) {
                    viewBean.setMessage("La mise à jour de la déclaration a échouée");
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}