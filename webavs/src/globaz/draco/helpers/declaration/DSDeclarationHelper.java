package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (27.11.2002 10:37:05)
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
            // Sous contr�le d'exceptions
            try {
                // R�cup�rer la d�claration
                DSDeclarationViewBean decl = new DSDeclarationViewBean();
                decl.setISession(session);
                decl.setIdDeclaration(vBean.getIdDeclaration());
                decl.retrieve();
                // Contr�ler si la d�claration peut �tre lue
                if (decl.hasErrors() || decl.isNew()) {
                    viewBean.setMessage("Impossible de trouver la d�claration");
                }
                // Contr�ler l'�tat de la d�claration
                if (!decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_OUVERT)) {
                    viewBean.setMessage("La d�claration doit �tre ouverte pour �tre valid�e");
                }
                // Mise � jour de l'�tat de la d�claration
                decl.setEtat(DSDeclarationViewBean.CS_AFACTURER);
                decl.setEnValidation(true);
                decl.update();
                if (decl.hasErrors()) {
                    viewBean.setMessage("La mise � jour de la d�claration a �chou�e");
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