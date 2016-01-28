package globaz.perseus.helpers.rentepont;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.context.JadeThread;
import globaz.perseus.vb.rentepont.PFFactureRentePontViewBean;
import ch.globaz.perseus.business.models.qd.SimpleFacture;

public class PFFactureRentePontHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);

        if (viewBean instanceof PFFactureRentePontViewBean) {
            ((PFFactureRentePontViewBean) viewBean).init();
        }
    }

    @Override
    public void _delete(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof PFFactureRentePontViewBean) {
            if (((PFFactureRentePontViewBean) viewBean).getModificationFacture()) {
                if ((((PFFactureRentePontViewBean) viewBean).getAdressePaiementAssure() == "")
                        || (((PFFactureRentePontViewBean) viewBean).getAdresseCourrierAssure() == "")) {
                    JadeThread.logError(SimpleFacture.class.getName(), "perseus.facture.adressepaiement.mandatory");
                } else {
                    ((PFFactureRentePontViewBean) viewBean).addFactureModifie();
                    ((PFFactureRentePontViewBean) viewBean).deleteFactureAModifier();
                }
            } else {
                ((PFFactureRentePontViewBean) viewBean).delete();
            }
        }
    }

}
