/*
 * Créé le 9 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.taxeCo2.AFListeRadieTaxeCo2ViewBean;
import globaz.naos.itext.taxeCo2.AFListeRadieTaxeCo2_Doc;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Helper permettant de passer les donnees depuis le {@link globaz.naos.process.AFAnnonceSalairesViewBean le viewBean
 * léger} vers {@link globaz.naos.process.AFAnnonceSalaires le process}
 * </p>
 * 
 * @author vre
 */
public class AFListeRadieTaxeCo2Helper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesHelper.
     */
    public AFListeRadieTaxeCo2Helper() {
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
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            AFListeRadieTaxeCo2ViewBean asViewBean = (AFListeRadieTaxeCo2ViewBean) viewBean;
            AFListeRadieTaxeCo2_Doc list = new AFListeRadieTaxeCo2_Doc();

            list.setAnneeMasse(asViewBean.getAnneeMasse());
            list.setAnneeRedistri(asViewBean.getAnneeRedistri());
            list.setEMailAddress(asViewBean.getEmail());
            list.setISession(session);
            // list.executeProcess();
            BProcessLauncher.start(list);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
