package globaz.vulpecula.helpers.congepaye;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.vulpecula.vb.congepaye.PTBouclementViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.process.congepaye.BouclementProcess;

public class PTBouclementHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (VulpeculaServiceLocator.getUsersService().hasRightForPrinting((BSession) session)) {
            PTBouclementViewBean vb = (PTBouclementViewBean) viewBean;
            BouclementProcess bouclementProcess = new BouclementProcess();
            try {
                bouclementProcess.setAnnee(new Annee(vb.getAnnee()));
                bouclementProcess.setEMailAddress(vb.getEmail());
                bouclementProcess.setIdConvention(vb.getIdConvention());
                bouclementProcess.setMiseAJour(vb.isMiseAJour());
                BProcessLauncher.start(bouclementProcess);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        } else {
            throw new ViewException(SpecificationMessage.PAS_DROIT_TRAITEMENT);
        }
    }
}
