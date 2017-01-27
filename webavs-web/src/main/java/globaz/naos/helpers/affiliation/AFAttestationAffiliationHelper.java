/*
 * Créé le 28 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAttestationAffiliationViewBean;
import globaz.naos.itext.affiliation.AFAttestationAffiliation_Doc;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFAttestationAffiliationHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAttestationAffiliationHelper.
     */
    public AFAttestationAffiliationHelper() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        AFAttestationAffiliationViewBean afViewBean = (AFAttestationAffiliationViewBean) viewBean;
        AFAffiliation affiliation = new AFAffiliation();

        affiliation.setAffiliationId(afViewBean.getAffiliationId());
        affiliation.setISession(session);
        affiliation.retrieve();

        afViewBean.setAffiliation(affiliation);
        afViewBean.setActivite(affiliation.getActivite());
        afViewBean.setEmail(session.getUserEMail());
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFAttestationAffiliationViewBean afViewBean = (AFAttestationAffiliationViewBean) viewBean;
        AFAttestationAffiliation_Doc doc = new AFAttestationAffiliation_Doc();

        doc.setISession(session);
        doc.setAffiliationId(afViewBean.getAffiliationId());
        doc.setEMailAddress(afViewBean.getEmail());
        doc.setActivite(afViewBean.getActivite());
        // doc.setBrancheEconomique(afViewBean.getBrancheEconomique());

        try {
            BProcessLauncher.start(doc);
        } catch (Exception e) {
            afViewBean.setMessage(e.getMessage());
            afViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
