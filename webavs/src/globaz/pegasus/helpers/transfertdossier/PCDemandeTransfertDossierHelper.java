package globaz.pegasus.helpers.transfertdossier;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.annonce.transfertdossier.PCDemandeTransfertDossierProcess;
import globaz.pegasus.vb.transfertdossier.PCDemandeTransfertDossierViewBean;
import java.util.Arrays;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.transfertdossier.TransfertDossierBuilderType;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.TransfertDossierAbstractBuilder;

public class PCDemandeTransfertDossierHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof PCDemandeTransfertDossierViewBean) {
            PCDemandeTransfertDossierViewBean vb = (PCDemandeTransfertDossierViewBean) viewBean;

            try {
                // verification de tout élément pouvant interrompre le process
                PegasusServiceLocator.getTransfertDossierPCProviderService().checkProcessArguments(
                        vb.getIdDernierDomicileLegal(), vb.getIdNouvelleCaisse(), vb.getCopies());

                if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                        && !FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                    PCDemandeTransfertDossierProcess process = new PCDemandeTransfertDossierProcess();
                    process.setSession((BSession) session);
                    process.setTypeBuilder(TransfertDossierBuilderType.DEMANDE_INITIALE_SANS_PC);
                    process.setIdDemandePc(vb.getIdDemandePc());

                    Map<String, String> params = process.getParametres();
                    params.put(TransfertDossierAbstractBuilder.ID_DEMANDE_PC, vb.getIdDemandePc());
                    params.put(TransfertDossierAbstractBuilder.ID_DERNIER_DOMICILE_LEGAL,
                            vb.getIdDernierDomicileLegal());
                    params.put(TransfertDossierAbstractBuilder.ID_GESTIONNAIRE, vb.getIdGestionnaire());
                    params.put(TransfertDossierAbstractBuilder.MAIL_GEST, vb.getMailAddress());
                    params.put(TransfertDossierAbstractBuilder.ID_NOUVELLE_CAISSE, vb.getIdNouvelleCaisse());
                    params.put(TransfertDossierAbstractBuilder.DATE_TRANSFERT, vb.getDateTransfert());
                    params.put(TransfertDossierAbstractBuilder.DATE_SUR_DOCUMENT, vb.getDateSurDocument());

                    process.setCopies(Arrays.asList(vb.getCopies()));
                    process.setAnnexes(Arrays.asList(vb.getAnnexes()));

                    BProcessLauncher.startJob(process);

                }
            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }
}
