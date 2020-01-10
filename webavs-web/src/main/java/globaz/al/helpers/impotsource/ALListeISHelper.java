package globaz.al.helpers.impotsource;

import ch.globaz.al.impotsource.domain.TypeListeAF;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.al.impotsource.process.ListeISRetenuesProcess;
import ch.globaz.al.impotsource.process.ListeISParCAFProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.al.vb.impotsource.ALListeISViewBean;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

public class ALListeISHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            ALListeISViewBean vb = (ALListeISViewBean) viewBean;
            TypeListeAF typeListe = TypeListeAF.fromValue(vb.getTypeListe());
            Annee annee = new Annee(vb.getAnnee());
            String canton = vb.getCanton();
            String idTiersAdministration = vb.getCaisseAF();
            String email = vb.getEmail();

            switch (typeListe) {
                case LR_RETENUES_PAR_CAF_FISC:
                    startListeISRetenuesParCAF(annee, canton, email);
                    break;
                case LR_RETENUES_PAR_CAF:
                    startListeISRetenuesProcess(annee, canton, idTiersAdministration, email);
                    break;
                case TOUS:
                    startListeISRetenuesProcess(annee, canton, idTiersAdministration, email);
                    startListeISRetenuesParCAF(annee, canton, email);
                    break;
            }
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    private void startListeISRetenuesProcess(Annee annee, String canton, String idTiersAdministration, String email)
            throws Exception {
        ListeISRetenuesProcess process = new ListeISRetenuesProcess();
        process.setAnnee(annee);
        process.setCanton(canton);
        process.setCaisseAF(idTiersAdministration);
        process.setEMailAddress(email);
        BProcessLauncher.start(process);
    }

    private void startListeISRetenuesParCAF(Annee annee, String canton, String email) throws Exception {
        ListeISParCAFProcess process = new ListeISParCAFProcess();
        process.setEMailAddress(email);
        process.setAnnee(annee);
        process.setCanton(canton);
        BProcessLauncher.start(process);
    }
}
