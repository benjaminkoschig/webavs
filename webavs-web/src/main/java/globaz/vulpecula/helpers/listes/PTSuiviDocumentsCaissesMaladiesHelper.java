package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.vb.listes.PTSuiviDocumentsCaissesMaladiesViewBean;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.process.caissemaladie.SuiviCaisseMaladieAbstractProcess;
import ch.globaz.vulpecula.process.caissemaladie.SuiviCaisseMaladieFicheAnnonceProcess;
import ch.globaz.vulpecula.process.caissemaladie.SuiviCaisseMaladieStandardProcess;

public class PTSuiviDocumentsCaissesMaladiesHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTSuiviDocumentsCaissesMaladiesViewBean vb = (PTSuiviDocumentsCaissesMaladiesViewBean) viewBean;
        getProcess(vb.getTypeListe(), vb.isSimulation(), vb.getEmail()).start();
    }

    private SuiviCaisseMaladieAbstractProcess getProcess(int typeListe, boolean simulation, String email) {
        SuiviCaisseMaladieAbstractProcess process;
        if (SuiviCaisseMaladie.TYPE_01_05 == typeListe) {
            process = new SuiviCaisseMaladieStandardProcess();
            process.setDoc_Name(DocumentConstants.SUIVI_CAISSE_STANDARD_DOC_NAME);
            process.setStandard_Name(DocumentConstants.SUIVI_CAISSE_STANDARD_NAME);
        } else if (SuiviCaisseMaladie.TYPE_06_FA == typeListe) {
            process = new SuiviCaisseMaladieFicheAnnonceProcess();
            process.setDoc_Name(DocumentConstants.SUIVI_CAISSE_FA_DOC_NAME);
            process.setStandard_Name(DocumentConstants.SUIVI_CAISSE_FA_NAME);
        } else {
            throw new IllegalArgumentException("Le type " + typeListe + " n'est pas valide");
        }
        process.setSimulation(simulation);
        if (!JadeStringUtil.isEmpty(email)) {
            process.setEMailAddress(email);
        }
        return process;
    }
}
