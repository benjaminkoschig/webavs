package globaz.vulpecula.helpers.listes;

import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.ListeSyndicat;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.process.syndicats.ListeSyndicatsProcess;
import ch.globaz.vulpecula.process.syndicats.ListeTravailleursPaiementSyndicatProcess;
import ch.globaz.vulpecula.process.syndicats.ListeTravailleursSalaireSyndicatProcess;
import ch.globaz.vulpecula.process.syndicats.ListeTravailleursSansSyndicatProcess;
import ch.globaz.vulpecula.process.syndicats.ListeTravailleursSyndicatProcess;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.vulpecula.vb.listes.PTListeSyndicatsViewBean;

public class PTListeSyndicatsHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (VulpeculaServiceLocator.getUsersService().hasRightForPrinting((BSession) session)) {
            try {
                PTListeSyndicatsViewBean vb = (PTListeSyndicatsViewBean) viewBean;
                BProcess process = getProcess(vb.getListe());
                if (process instanceof ListeSyndicatsProcess) {
                    ListeSyndicatsProcess listeSyndicatsProcess = (ListeSyndicatsProcess) process;
                    listeSyndicatsProcess.setEMailAddress(vb.getEmail());
                    listeSyndicatsProcess.setAnnee(new Annee(vb.getAnnee()));
                    listeSyndicatsProcess.setIdSyndicat(vb.getIdSyndicat());
                    listeSyndicatsProcess.setIdCaisseMetier(vb.getIdCaisseMetier());
                    listeSyndicatsProcess.setIdTravailleur(vb.getIdTravailleur());
                } else if (process instanceof ListeTravailleursSansSyndicatProcess) {
                    ListeTravailleursSansSyndicatProcess listeTravailleursSansSyndicatProcess = (ListeTravailleursSansSyndicatProcess) process;
                    listeTravailleursSansSyndicatProcess.setEMailAddress(vb.getEmail());
                    listeTravailleursSansSyndicatProcess.setAnnee(new Annee(vb.getAnnee()));
                    listeTravailleursSansSyndicatProcess.setIdCaisseMetier(vb.getIdCaisseMetier());
                    listeTravailleursSansSyndicatProcess.setIdTravailleur(vb.getIdTravailleur());
                }
                BProcessLauncher.start(process);
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
            }
        } else {
            throw new ViewException(SpecificationMessage.PAS_DROIT_TRAITEMENT);
        }

    }

    private BProcess getProcess(String genre) {
        ListeSyndicat genreListe = ListeSyndicat.fromValue(genre);
        switch (genreListe) {
            case TRAVAILLEURS_SALAIRE_SYNDICAT:
                return new ListeTravailleursSalaireSyndicatProcess();
            case TRAVAILLEURS_SYNDICAT:
                return new ListeTravailleursSyndicatProcess();
            // return new ListeTravailleursCaisseMetierProcess();
            case TRAVAILLEURS_PAIEMENT_SYNDICAT:
                return new ListeTravailleursPaiementSyndicatProcess();
            case TRAVAILLEURS_SANS_SYNDICAT:
                return new ListeTravailleursSansSyndicatProcess();
        }
        throw new IllegalArgumentException("Le type de liste n'est pas valide");
    }
}