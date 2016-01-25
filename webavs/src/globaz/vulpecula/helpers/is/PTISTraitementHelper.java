package globaz.vulpecula.helpers.is;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.is.PTISTraitementViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.is.HistoriqueProcessusAf;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.process.is.TraitementISProcess;
import ch.globaz.vulpecula.process.is.TraitementNonISProcess;

public class PTISTraitementHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTISTraitementViewBean vb = (PTISTraitementViewBean) viewBean;

        if (JadeStringUtil.isEmpty(vb.getIdProcessusPaiementDirect())
                && JadeStringUtil.isEmpty(vb.getIdProcessusPaiementIndirect())) {
            throw new ViewException(SpecificationMessage.PROCESSUS_AF_MANQUANT);

        } else {

            if (!JadeStringUtil.isEmpty(vb.getIdProcessusPaiementDirect())) {
                HistoriqueProcessusAf historique = VulpeculaRepositoryLocator.getHistoriqueProcessusAfRepository()
                        .findByIdProcessus(vb.getIdProcessusPaiementDirect());
                if (historique == null) {
                    TraitementISProcess process = new TraitementISProcess();
                    process.setIdProcessusDirect(vb.getIdProcessusPaiementDirect());
                    process.setEMailAddress(vb.getEmail());
                    try {
                        BProcessLauncher.start(process);
                    } catch (Exception e) {
                        throw new ViewException(SpecificationMessage.IMPOT_SOURCE_ERREUR_TECHNIQUE);
                    }
                } else {
                    throw new ViewException(SpecificationMessage.PROCESSUS_DEJA_LANCE);
                }
            }

            if (!JadeStringUtil.isEmpty(vb.getIdProcessusPaiementIndirect())) {
                HistoriqueProcessusAf historique = VulpeculaRepositoryLocator.getHistoriqueProcessusAfRepository()
                        .findByIdProcessus(vb.getIdProcessusPaiementIndirect());
                if (historique == null) {
                    TraitementNonISProcess processMC = new TraitementNonISProcess();
                    processMC.setIdProcessusIndirect(vb.getIdProcessusPaiementIndirect());
                    processMC.setEMailAddress(vb.getEmail());
                    try {
                        BProcessLauncher.start(processMC);
                    } catch (Exception e) {
                        throw new ViewException(SpecificationMessage.IMPOT_SOURCE_ERREUR_TECHNIQUE);
                    }
                } else {
                    throw new ViewException(SpecificationMessage.PROCESSUS_DEJA_LANCE);
                }
            }
        }
    }
}
