package globaz.ij.helpers.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteListViewBean;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJIJCalculeeJointIndemniteHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redefinition de cette methode pour charger les infos a afficher dans l'ecran rc au moment de la recherche.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJIJCalculeeJointIndemniteListViewBean icViewBean = (IJIJCalculeeJointIndemniteListViewBean) viewBean;
        IJPrononce prononce = IJPrononce.loadPrononce((BSession) session, null, icViewBean.getForIdPrononce(),
                icViewBean.getCsTypeIJ());
        PRTiersWrapper tiers = prononce.loadDemande(null).loadTiers();

        icViewBean.setNomPrenomAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        icViewBean.setNoAVSAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        icViewBean.setDatePrononce(prononce.getDatePrononce());

        // BZ 7366
        icViewBean.setIdTiers(tiers.getIdTiers());
    }
}
