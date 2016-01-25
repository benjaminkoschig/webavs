package globaz.corvus.helpers.process;

import globaz.corvus.vb.process.REGenererAttestationFiscaleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import ch.globaz.corvus.process.attestationsfiscales.REGenererAttestationsFiscalesProcess;

public class REGenererAttestationFiscaleHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // rien, empêche un appel à retrieve sur le viewBean
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {

            REGenererAttestationFiscaleViewBean attestationsFiscalesViewBean = (REGenererAttestationFiscaleViewBean) viewBean;

            if (JadeStringUtil.isBlankOrZero(attestationsFiscalesViewBean.getAnneeAttestations())) {
                throw new Exception(((BSession) session).getLabel("ERREUR_DATE_ATT_FISC_OBLIGATOIRE"));
            }
            if (JadeStringUtil.isBlankOrZero(attestationsFiscalesViewBean.getDateImpressionAttMMAAA())) {
                throw new Exception(((BSession) session).getLabel("ERREUR_DATE_OBLIGATOIRE_MULTIPLE_ATT_FISC"));
            }
            if (JadeStringUtil.isBlank(attestationsFiscalesViewBean.getEMailAddress())) {
                throw new Exception(((BSession) session).getLabel("ERREUR_ADRESSE_EMAIL_REQUISE"));
            }

            REGenererAttestationsFiscalesProcess process = new REGenererAttestationsFiscalesProcess();
            process.setSession(attestationsFiscalesViewBean.getSession());
            process.setEMailAddress(attestationsFiscalesViewBean.getEMailAddress());
            process.setAnnee(attestationsFiscalesViewBean.getAnneeAttestations());
            process.setDateImpression(attestationsFiscalesViewBean.getDateImpressionAttMMAAA());
            process.setNssDe(attestationsFiscalesViewBean.getNssDe());
            process.setNssA(attestationsFiscalesViewBean.getNssA());
            process.setIsSendToGed(attestationsFiscalesViewBean.getIsSendToGed());

            BProcessLauncher.start(process, false);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
    }
}