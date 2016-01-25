package globaz.ij.helpers.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestation;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.vb.prestations.IJCotisationViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJCotisationHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _retrieve(FWViewBeanInterface vb, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        ((BIPersistentObject) vb).retrieve();
        IJCotisationViewBean viewBean = (IJCotisationViewBean) vb;
        boolean isEditable = false;

        String idRepartitionPaiement = viewBean.getIdRepartitionPaiement();

        if (!JadeStringUtil.isBlankOrZero(idRepartitionPaiement)) {

            IJRepartitionPaiements repartitionPaiements = new IJRepartitionPaiements();
            repartitionPaiements.setSession(viewBean.getSession());
            repartitionPaiements.setIdRepartitionPaiement(idRepartitionPaiement);
            repartitionPaiements.retrieve();
            if (!repartitionPaiements.isNew()) {

                String idPrestation = repartitionPaiements.getIdPrestation();

                if (!JadeStringUtil.isBlankOrZero(idRepartitionPaiement)) {
                    IJPrestation prestation = new IJPrestation();
                    prestation.setSession(viewBean.getSession());
                    prestation.setIdPrestation(idPrestation);
                    prestation.retrieve();
                    if (!prestation.isNew()) {
                        isEditable = !IIJPrestation.CS_ANNULE.equals(prestation.getCsEtat())
                                && !IIJPrestation.CS_DEFINITIF.equals(prestation.getCsEtat());
                    }
                }
            }
        }
        viewBean.setIsEditable(isEditable);
    }

    /**
     * charge les infos sur le prononce et la repartition de paiement pour cette cotisation.
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
        IJCotisationViewBean cotisation = (IJCotisationViewBean) viewBean;

        // charger les infos sur la repartition de paiement
        IJRepartitionJointPrestation repartition = new IJRepartitionJointPrestation();

        repartition.setIdRepartitionPaiement(cotisation.getIdRepartitionPaiement());
        repartition.setISession(session);
        repartition.retrieve();

        cotisation.setMontantBrutRepartition(repartition.getMontantBrut());
        cotisation.setNomBeneficiaireRepartition(repartition.getNom());
        cotisation.setIdPrestation(repartition.getIdPrestation());

        // charger les infos sur la base d'indemnisation
        IJBaseIndemnisation baseIndemnisation = new IJBaseIndemnisation();

        baseIndemnisation.setIdBaseIndemisation(repartition.getIdBaseIndemnisation());
        baseIndemnisation.setISession(session);
        baseIndemnisation.retrieve();

        if (!baseIndemnisation.isNew()) {
            cotisation.setDateDebutBaseIndemnisation(baseIndemnisation.getDateDebutPeriode());
            cotisation.setDateFinBaseIndemnisation(baseIndemnisation.getDateFinPeriode());
            cotisation.setIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemisation());

            // charger les infos sur le prononce
            IJPrononce prononce = IJPrononce.loadPrononce((BSession) session, null, baseIndemnisation.getIdPrononce(),
                    baseIndemnisation.getCsTypeIJ());
            PRTiersWrapper tiers = prononce.loadDemande(null).loadTiers();

            cotisation.setNoAVSAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            cotisation.setNomPrenomAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            cotisation.setDatePrononce(prononce.getDatePrononce());
        } else {
            cotisation.setMessage(((BSession) session).getLabel("BI_NON_TROUVEE"));
            cotisation.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
