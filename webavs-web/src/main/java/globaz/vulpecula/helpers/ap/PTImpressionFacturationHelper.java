package globaz.vulpecula.helpers.ap;

import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.ap.DocumentFactureAPPrinter;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.vulpecula.vb.ap.PTImpressionFacturationViewBean;

public class PTImpressionFacturationHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        PTImpressionFacturationViewBean vb = (PTImpressionFacturationViewBean) viewBean;

        List<FactureAssociation> listFactureAssociation = VulpeculaRepositoryLocator.getFactureAssociationRepository()
                .findByIdPassageFacturation(vb.getIdPassage()).getFactures();
        


        try {
            if (!isValidPassage(vb.getIdPassage())) {
                throw new Exception(
                        BSessionUtil.getSessionFromThreadContext().getLabel("FACTURATION_AP_ERROR_IMPRESSION_PASSAGE"));
            }
            
            if(listFactureAssociation.isEmpty()) {
                throw new Exception(
                        BSessionUtil.getSessionFromThreadContext().getLabel("FACTURATION_AP_NO_FACT"));
            }
            
            DocumentFactureAPPrinter printer = new DocumentFactureAPPrinter();
            printer.setEMailAddress(vb.getEmail());
            printer.setIds(DocumentPrinter.getIds(listFactureAssociation));
            printer.start();
            

            

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        }

    }

    /**
     * Cette methode controle que le passage de facturation soit de type association professionnelle
     *
     * @throws Exception
     *
     */
    private Boolean isValidPassage(String passage) throws Exception {
        FAPassageModuleManager modPassManager = new FAPassageModuleManager();
        BSession session = BSessionUtil.getSessionFromThreadContext();
        modPassManager.setSession(session);
        modPassManager.setForIdPassage(passage);
        modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_ASSOCIATIONS_PROF);
        modPassManager.find(0);
        if (modPassManager.getSize() == 0) {
            JadeLogger.info(modPassManager,
                    this.getClass().toString() + "\n" + session.getLabel("FACTURATION_AP_ERROR_IMPRESSION_PASSAGE"));
            return false;
        }
        return true;
    }
    
}
