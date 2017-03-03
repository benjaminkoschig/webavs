/**
 * 
 */
package globaz.naos.helpers.annoncesRee;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.annoncesRee.AFAnnoncesReeViewBean;
import ch.globaz.naos.ree.REEProcess;
import ch.globaz.naos.ree.domain.pojo.ProcessProperties;

/**
 * @author est
 * 
 */
public class AFAnnoncesReeHelper extends FWHelper {

    /**
     * Constructeur de AFAnnoncesReeHelper
     */
    public AFAnnoncesReeHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFAnnoncesReeViewBean vb = (AFAnnoncesReeViewBean) viewBean;

        try {
            REEProcess process = new REEProcess();
            process.setSession((BSession) session);

            if ("annonce_registre".equals(vb.getTypeAnnonce())) {
                process.setModeExecution("53");
            } else {
                process.setModeExecution("53-54");
            }

            ProcessProperties properties = new ProcessProperties();
            properties.setName(vb.getNomReference());
            properties.setDepartment(vb.getDepartementReference());
            properties.setPhone(vb.getTelephoneReference());
            properties.setOther(vb.getInfoReferences());
            properties.setEmail(vb.getEmailReference());
            properties.setTailleLot(Integer.parseInt(vb.getNbAnnonces()));
            properties.setValidation(vb.getIsValidationUnitaire());
            properties.setRecipientId(vb.getDestinataire());

            process.setProperties(properties);

            launchReeProcess(viewBean, process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    private void launchReeProcess(FWViewBeanInterface viewBean, REEProcess process) {
        try {
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}