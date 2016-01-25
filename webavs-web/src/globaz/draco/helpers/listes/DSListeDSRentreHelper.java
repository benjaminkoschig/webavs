/*
 * Créé le 9 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.helpers.listes;

import globaz.draco.db.listes.DSListeDSRentreViewBean;
import globaz.draco.print.list.DSListeDSRentre_Doc;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;

/**
 * <H1>Description</H1>
 * <p>
 * Helper permettant de passer les donnees depuis le {@link globaz.naos.process.AFAnnonceSalairesViewBean le viewBean
 * léger} vers {@link globaz.naos.process.AFAnnonceSalaires le process}
 * </p>
 * 
 * @author vre
 */
public class DSListeDSRentreHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesHelper.
     */
    public DSListeDSRentreHelper() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * copie les donnees du viewBean vers le process.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            DSListeDSRentreViewBean asViewBean = (DSListeDSRentreViewBean) viewBean;
            DSListeDSRentre_Doc list = new DSListeDSRentre_Doc();

            list.setDateReference(asViewBean.getDateReference());
            list.setEMailAddress(asViewBean.getEMail());
            list.setISession(session);
            // list.executeProcess();
            BProcessLauncher.start(list);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
