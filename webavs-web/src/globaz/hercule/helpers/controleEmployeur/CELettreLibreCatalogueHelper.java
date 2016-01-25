package globaz.hercule.helpers.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CELettreLibreCatalogueViewBean;
import globaz.hercule.itext.controleEmployeur.CELettreLibreCatalogue_Doc;

/**
 * @author SCO
 * @since 13 oct. 2010
 */
public class CELettreLibreCatalogueHelper extends FWHelper {

    /**
     * Constructeur de CELettreLibreCatalogueHelper
     */
    public CELettreLibreCatalogueHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CELettreLibreCatalogueViewBean vb = (CELettreLibreCatalogueViewBean) viewBean;

        try {

            CELettreLibreCatalogue_Doc process = new CELettreLibreCatalogue_Doc((BSession) session);
            process.setDateDebutControle(vb.getDateDebutControle());
            process.setDateFinControle(vb.getDateFinControle());
            process.setDateEffective(vb.getDateEffective());
            process.setVisaReviseur(vb.getVisaReviseur());
            process.setNumAffilie(vb.getNumAffilie());
            process.setIdTiers(vb.getIdTiers());
            process.setIdControle(vb.getSelectedId());

            process.setIdDocument(vb.getIdDocument());
            process.setIdDocumentDefaut(vb.getIdDocumentDefault());
            process.setEMailAddress(vb.getEmail());
            process.setDateEnvoi(vb.getDateEnvoi());

            BProcessLauncher.start(process);

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
