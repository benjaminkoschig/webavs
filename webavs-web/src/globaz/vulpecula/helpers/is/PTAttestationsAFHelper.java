package globaz.vulpecula.helpers.is;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.is.PTAttestationsAFViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.documents.af.DocumentAttestationAFPourFiscPrinter;
import ch.globaz.vulpecula.documents.af.DocumentAttestationAFPrinter;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.is.TypeListAttestationAF;

public class PTAttestationsAFHelper extends FWHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTAttestationsAFViewBean vb = (PTAttestationsAFViewBean) viewBean;
            BProcess process = selectProcess(vb);
            BProcessLauncher.start(process);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    private BProcess selectProcess(PTAttestationsAFViewBean viewBean) {
        TypeListAttestationAF typeListe = TypeListAttestationAF.fromValue(viewBean.getListeAttestationAF());
        switch (typeListe) {
            case ALLOCATAIRE:
                String idAllocataire = viewBean.getIdAllocataire();
                String dateDebut = viewBean.getDateDebut();
                String dateFin = viewBean.getDateFin();
                DocumentAttestationAFPrinter processAAF = new DocumentAttestationAFPrinter(idAllocataire, dateDebut,
                        dateFin);
                processAAF.setEMailAddress(viewBean.getEmail());
                return processAAF;
            case FISC:
                Annee annee = new Annee(viewBean.getAnnee());
                DocumentAttestationAFPourFiscPrinter process = new DocumentAttestationAFPourFiscPrinter(annee);
                process.setAnnee(new Annee(viewBean.getAnnee()));
                process.setEMailAddress(viewBean.getEmail());
                return process;
            default:
                return null;
        }
    }
}
