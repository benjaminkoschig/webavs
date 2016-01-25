package globaz.pavo.helpers.compte;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CICompteIndividuelCertificatAssuranceViewBean;
import globaz.pavo.process.CICompteIndividuelImpressionCertifProcess;

public class CICompteIndividuelCertificatAssuranceHelper extends FWHelper {

    private CICompteIndividuelCertificatAssuranceViewBean vb;

    public CICompteIndividuelCertificatAssuranceHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            vb = (globaz.pavo.db.compte.CICompteIndividuelCertificatAssuranceViewBean) viewBean;
            CICompteIndividuelImpressionCertifProcess process = new CICompteIndividuelImpressionCertifProcess();
            process.setSession((BSession) session);
            process.setMotif(vb.getMotif());
            process.setNomPrenom(vb.getNomPrenom());
            process.setSexe(vb.getSexe());
            process.setEMailAddress(vb.getEMailAddress());
            process.setDateNaiss(vb.getDateNaissance());
            process.setNoNnss(NSUtil.formatAVSUnknown(vb.getNssFormate()));
            process.setLangue(vb.getLangueImp());
            process.setNAffilie(vb.getLikeInIdAffiliation());
            process.setEmployeur(vb.getEmployeur());
            process.setTypeAffilie(vb.getForTypeAffilie());
            process.executeProcess();

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }

    }
}
