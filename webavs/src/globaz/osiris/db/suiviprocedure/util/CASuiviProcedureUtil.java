package globaz.osiris.db.suiviprocedure.util;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;

public class CASuiviProcedureUtil {

    public static CACompteAnnexeViewBean getCompteAnnexe(BSession session, String idCompteAnnexe) {
        if (JadeStringUtil.isIntegerEmpty(idCompteAnnexe)) {
            return new CACompteAnnexeViewBean();
        }

        CACompteAnnexeViewBean compteAnnexe = new CACompteAnnexeViewBean();
        compteAnnexe.setSession(session);

        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);

        try {
            compteAnnexe.retrieve();

            if (compteAnnexe.isNew() || compteAnnexe.hasErrors()) {
                return new CACompteAnnexeViewBean();
            }

            return compteAnnexe;
        } catch (Exception e) {
            return new CACompteAnnexeViewBean();
        }
    }
}
