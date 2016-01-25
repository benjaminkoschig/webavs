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
import globaz.naos.db.taxeCo2.AFFigerTaxeCo2ViewBean;
import globaz.naos.process.taxeCo2.AFProcessFigerTaxeCo2;

/**
 * <H1>Description</H1>
 * <p>
 * Helper permettant de passer les donnees depuis le {@link globaz.naos.process.AFAnnonceSalairesViewBean le viewBean
 * léger} vers {@link globaz.naos.process.AFAnnonceSalaires le process}
 * </p>
 * 
 * @author vre
 */
public class AFFigerTaxeCo2Helper extends FWHelper {

    // Type de documents : 1 = Bouclement d'acompte
    // 2 = Prévision d'acompte
    // 3 = Decision d'acompte
    // 4 = Relevé à blanc

    public static final String typeDocument = "1";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalairesHelper.
     */
    public AFFigerTaxeCo2Helper() {
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
        if (viewBean instanceof AFFigerTaxeCo2ViewBean) {
            AFFigerTaxeCo2ViewBean asViewBean = (AFFigerTaxeCo2ViewBean) viewBean;
            asViewBean._validate();
            if (!FWViewBeanInterface.ERROR.equals(asViewBean.getMsgType())) {
                AFProcessFigerTaxeCo2 process = new AFProcessFigerTaxeCo2();
                process.setAnneeMasse(asViewBean.getAnneeMasse());
                process.setAnneeRedistribution(asViewBean.getAnneeRedistri());
                process.setEMailAddress(asViewBean.getEmail());
                process.setReinitialiser(asViewBean.getReinitialiser());
                process.setISession(session);
                try {
                    BProcessLauncher.start(process);
                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.toString());
                }
            }
        } else {
            super._start(viewBean, action, session);
        }
    }
}
