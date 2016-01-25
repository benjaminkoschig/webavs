package globaz.pavo.helpers.compte;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CICompteIndividuelAttestationViewBean;
import globaz.pavo.process.CICompteIndividuelImpressionAttestProcess;

public class CICompteIndividuelAttestationHelper extends FWHelper {

    private CICompteIndividuelAttestationViewBean vb;

    public CICompteIndividuelAttestationHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            vb = (globaz.pavo.db.compte.CICompteIndividuelAttestationViewBean) viewBean;
            CICompteIndividuelImpressionAttestProcess process = new CICompteIndividuelImpressionAttestProcess();
            process.setSession((BSession) session);
            process.setNomPrenom(vb.getNomPrenom());
            process.setEMailAddress(vb.getEMailAddress());
            process.setAnneeCot(vb.getDateCot());
            process.setDateNaiss(vb.getDateNaissance());
            process.setLangue(vb.getLangueImp());
            process.setNAffilie(vb.getLikeInIdAffiliation());
            process.setSexe(vb.getSexe());
            process.setNoNnss(NSUtil.formatAVSUnknown(vb.getNssFormate()));
            process.executeProcess();
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }

    }
}
