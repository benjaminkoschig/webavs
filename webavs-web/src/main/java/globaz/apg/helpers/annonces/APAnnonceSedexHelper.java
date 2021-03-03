/*
 * Créé le 6 déc. 05
 */
package globaz.apg.helpers.annonces;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.annonces.APAnnonceAPGManager;
import globaz.apg.vb.annonces.APAnnonceSedexViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author dvh
 */
public class APAnnonceSedexHelper extends APAnnonceAPGHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    public FWViewBeanInterface reenvoyer(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        String envelopeMessageId = ((APAnnonceSedexViewBean) viewBean).getChampsAnnonce().getEnvelopeMessageId();
        if (!JadeStringUtil.isEmpty(envelopeMessageId)) {
            APAnnonceAPGManager annonceManager = new APAnnonceAPGManager();
            annonceManager.setSession(session);
            annonceManager.setForEnvelopeMessageId(envelopeMessageId);
            annonceManager.find();
            Iterator<APAnnonceAPG> it = annonceManager.iterator();
            while (it.hasNext()) {
                APAnnonceAPG annonce = it.next();
                annonce.setEtat(IAPAnnonce.CS_VALIDE);
                annonce.update();
            }
        } else {
            APAnnonceAPG annonce = new APAnnonceAPG();
            annonce.setSession(session);
            annonce.setIdAnnonce(((APAnnonceSedexViewBean) viewBean).getChampsAnnonce().getMessageId());
            annonce.retrieve();
            annonce.setEtat(IAPAnnonce.CS_VALIDE);
            annonce.update();
        }

        return viewBean;
    }

}
