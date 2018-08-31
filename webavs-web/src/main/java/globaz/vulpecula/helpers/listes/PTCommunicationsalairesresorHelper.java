package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.vulpecula.vb.listes.PTCommunicationsalairesresorViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.communicationsalaires.CSTypeListeSalaire;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.process.communicationsalaires.AbstractCommunicationSalairesProcess;
import ch.globaz.vulpecula.process.communicationsalaires.CommunicationSalairesResorProcess;
import ch.globaz.vulpecula.process.communicationsalaires.CommunicationSalairesRetavalProcess;
import com.sun.star.lang.IllegalArgumentException;

public class PTCommunicationsalairesresorHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (VulpeculaServiceLocator.getUsersService().hasRightForPrinting((BSession) session)) {
            try {
                PTCommunicationsalairesresorViewBean vb = (PTCommunicationsalairesresorViewBean) viewBean;
                AbstractCommunicationSalairesProcess process;
                if (CSTypeListeSalaire.RESOR.getCodeSystem().equals(vb.getTypeListe())) {
                    CommunicationSalairesResorProcess processResor = new CommunicationSalairesResorProcess();
                    processResor.setIdConvention(vb.getIdConvention());
                    processResor.setTypeDecompte(vb.getCodeTypeDecompte());
                    processResor.setMiseAJour(vb.isMiseAJour());
                    process = processResor;
                } else {
                    process = new CommunicationSalairesRetavalProcess();
                }
                try {
                    if (vb.getAnnee() != null && vb.getAnnee().length() == 4) {
                        process.setAnnee(new Annee(vb.getAnnee()));
                    } else {
                        throw new IllegalArgumentException("L'année n'est pas saisie");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("L'année n'est pas saisie correctement");
                }
                process.setEMailAddress(vb.getEmail());
                process.setSendCompletionMail(true);
                process.setSendMailOnError(true);
                BProcessLauncher.start(process);
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
            }
        } else {
            throw new ViewException(SpecificationMessage.PAS_DROIT_TRAITEMENT);
        }

    }
}
