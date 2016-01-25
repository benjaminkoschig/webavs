package globaz.naos.helpers.attestation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.itext.attestation.AFAttestationChaSoc_Doc;
import globaz.naos.vb.attestation.AFAttestationChaSocProcessViewBean;

public class AFAttestationChaSocProcessHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFAttestationChaSocProcessViewBean attestationChaSocViewBean = (AFAttestationChaSocProcessViewBean) viewBean;
        AFAttestationChaSoc_Doc attestationChaSocDoc = new AFAttestationChaSoc_Doc();
        attestationChaSocDoc.setSession((BSession) session);
        attestationChaSocDoc.setAffiliationId(attestationChaSocViewBean.getAffiliation().getAffiliationId());
        attestationChaSocDoc.setDateAttestation(attestationChaSocViewBean.getDateAttestation());
        attestationChaSocDoc.setDateValidite(attestationChaSocViewBean.getDateValidite());
        attestationChaSocDoc.setNombreExemplaire(attestationChaSocViewBean.getNombreExemplaire());
        attestationChaSocDoc.setPaiementRegulier(attestationChaSocViewBean.getPaiementRegulier());
        attestationChaSocDoc.setTitre(attestationChaSocViewBean.getTitre());
        attestationChaSocDoc.setEMailAddress(attestationChaSocViewBean.getEmail());
        try {
            BProcessLauncher.start(attestationChaSocDoc);
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
