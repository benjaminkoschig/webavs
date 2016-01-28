package globaz.perseus.helpers.attestationsfiscalesRP;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.perseus.process.attestationsfiscalesRP.PFAttestationsFiscalesRPExportProcess;
import globaz.perseus.vb.attestationsfiscalesRP.PFAttestationsFiscalesRPViewBean;
import globaz.prestation.tools.PRDateFormater;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.horizon.jaspe.util.JACalendar;
import ch.horizon.jaspe.util.JACalendarGregorian;

public class PFAttestationsFiscalesRPHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        if (viewBean instanceof PFAttestationsFiscalesRPViewBean) {
            ((PFAttestationsFiscalesRPViewBean) viewBean).init();
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        PFAttestationsFiscalesRPViewBean vb = (PFAttestationsFiscalesRPViewBean) viewBean;

        if (!JadeStringUtil.isBlank(vb.getCaisse())) {

        }
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            // Test si l'année d'attestation n'est pas plus grande que l'année du dernier pmt mensuel
            PFAttestationsFiscalesRPViewBean vb = (PFAttestationsFiscalesRPViewBean) viewBean;
            String AnneeDernierPaiement = PRDateFormater.convertDate_MMxAAAA_to_AAAA(PerseusServiceLocator
                    .getPmtMensuelRentePontService().getDateDernierPmt());
            JACalendarGregorian cal = new JACalendarGregorian();
            if (JACalendar.COMPARE_SECONDUPPER == cal.compare(AnneeDernierPaiement, vb.getAnneeAttestation())) {
                String[] param = new String[1];
                param[0] = AnneeDernierPaiement;
                JadeThread.logError(this.getClass().getName(),
                        "perseus.attestationsfiscales.generer.anneeattestationplusgrandequedernierpmtmensuel", param);
            }

            if (!JadeThread.logHasMessages()) {

                PFAttestationsFiscalesRPExportProcess process = new PFAttestationsFiscalesRPExportProcess();
                process.setSession((BSession) session);
                process.setMailAd(vb.geteMailAdresse(session.getUserEMail()));
                process.setAnneeAttestations(vb.getAnneeAttestation());
                process.setDateDocument(vb.getDateDocument());
                process.setCaisse(vb.getCaisse());
                if (!JadeStringUtil.isNull(vb.getIdDossier())) {
                    process.setIdDossier(vb.getIdDossier());
                }
                process.setIsSendToGed(vb.getIsSendToGed());
                process.setNomCaisse(vb.getNomCaisse());
                process.setAdresseCaisse(vb.getRueCaisse());
                process.setCodePostaleCaisse(vb.getCodePostaleCaisse());
                process.setLocaliteCaisse(vb.getLocaliteCaisse());
                process.setPrenomPersonneContactCaisse(vb.getPrenomPersonneContact());
                process.setNomPersonnecontactCaisse(vb.getNomPersonneContact());
                process.setTelPersonneContactCaisse(vb.getTelephoneContactCaisse());
                BProcessLauncher.startJob(process);

            }
        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMessage("Unable to start........");
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
