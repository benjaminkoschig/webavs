package globaz.draco.helpers.declaration;

import globaz.draco.db.declaration.DSLettreReclamationNssViewBean;
import globaz.draco.process.DSLettreReclamationNssProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;

/**
 * 
 * @author SCO
 * @since 15 juil. 2011
 */
public class DSLettreReclamationNssHelper extends FWHelper {

    /**
     * Constructeur de DSLettreReclamationNssHelper
     */
    public DSLettreReclamationNssHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        DSLettreReclamationNssViewBean vb = (DSLettreReclamationNssViewBean) viewBean;

        try {

            DSLettreReclamationNssProcess process = new DSLettreReclamationNssProcess();
            process.setSession((BSession) session);

            process.setAnnee(vb.getAnnee());
            process.setFromAffilie(vb.getFromAffilie());
            process.setToAffilie(vb.getToAffilie());
            process.setDelaiRappel(vb.getDelaiRappel());
            process.setDateDocument(vb.getDateDocument());
            process.setGenreEdition(vb.getGenreEdition());
            process.setObservation(vb.getObservation());
            process.setTypeDeclaration(vb.getTypeDeclaration());
            process.setTypeDocument(vb.getTypeDocument());
            process.setEMailAddress(vb.getEmail());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
