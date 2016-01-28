package globaz.aquila.helpers.process;

import globaz.aquila.process.COProcessDumpConfigEtapes;
import globaz.aquila.vb.process.COProcessDumpConfigEtapesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COProcessDumpConfigEtapesHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        // valider le viewBean
        COProcessDumpConfigEtapesViewBean processViewBean = (COProcessDumpConfigEtapesViewBean) viewBean;

        if (JadeStringUtil.isBlank(processViewBean.getEmail())) {
            processViewBean.setMessage(((BSession) session).getLabel("AQUILA_EMAIL_REQUIS"));
            processViewBean.setMsgType(FWViewBeanInterface.ERROR);

            return;
        }

        // on instancie et lance le process
        COProcessDumpConfigEtapes process = new COProcessDumpConfigEtapes();
        COProcessDumpConfigEtapesViewBean dumpViewBean = (COProcessDumpConfigEtapesViewBean) viewBean;

        process.setISession(session);
        process.setEMailAddress(dumpViewBean.getEmail());
        process.setBaseDuplique(dumpViewBean.getBaseDuplique());
        process.setCsSequenceBase(dumpViewBean.getCsSequenceBase());
        process.setIncludeDelete(dumpViewBean.getIncludeDelete());
        process.setRecomputeIndexes(dumpViewBean.getRecomputeIndexes());
        process.setSchema(processViewBean.getSchema());

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
