package globaz.vulpecula.helpers.comptabilite;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.comptabilite.PTListeRecapParRubriqueViewBean;
import ch.globaz.vulpecula.process.comptabilite.ListeRecapParRubriqueProcess;

public class PTListeRecapParRubriqueHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        PTListeRecapParRubriqueViewBean vb = (PTListeRecapParRubriqueViewBean) viewBean;
        ListeRecapParRubriqueProcess process = new ListeRecapParRubriqueProcess();

        process.setEMailAddress(vb.getEmail());
        process.setForIdRole(vb.getForIdRole());
        process.setForIdGenre(vb.getForIdGenreCompte());
        process.setForIdCategorie(vb.getForIdCategorie());
        process.setFromDateValeur(vb.getFromDateDebut());
        process.setToDateValeur(vb.getToDateFin());
        process.setFromIdExterne(vb.getFromIdExterneRole());
        process.setToIdExterne(vb.getToIdExterneRole());
        process.setFromIdExternes(vb.getFromIdExternes());

        try {
            BProcessLauncher.start(process);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
