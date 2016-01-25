package globaz.corvus.helpers.decisions;

import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.process.REGenererLettreConfirmationAjournementProcess;
import globaz.corvus.vb.decisions.REPreparerDecisionAvecAjournementViewBean;
import globaz.corvus.vb.decisions.REPreparerDecisionSpecifiqueViewBean;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import ch.globaz.corvus.domaine.DemandeRente;

public class REPreparerDecisionAvecAjournementHelper extends REPreparerDecisionSpecifiqueHelper {

    @Override
    protected void actionSpecifiqueSupplementaire(final REPreparerDecisionSpecifiqueViewBean viewBean,
            final DemandeRente demande) {

        REPreparerDecisionAvecAjournementViewBean avecAjournementViewBean = (REPreparerDecisionAvecAjournementViewBean) viewBean;

        if (avecAjournementViewBean.isEditionDuDocument()) {
            try {
                REGenererLettreConfirmationAjournementProcess genererLettreRevocationAjournementProcess = new REGenererLettreConfirmationAjournementProcess();
                genererLettreRevocationAjournementProcess.setSession(BSessionUtil.getSessionFromThreadContext());
                genererLettreRevocationAjournementProcess.setAdresseEmail(viewBean.getAdresseEmailGestionnaire());
                genererLettreRevocationAjournementProcess.setDateDuDocument(avecAjournementViewBean
                        .getDateSurLeDocument());
                genererLettreRevocationAjournementProcess.setMiseEnGed(avecAjournementViewBean.isMiseEnGed());
                genererLettreRevocationAjournementProcess.setDemandeRente(demande);

                BProcessLauncher.start(genererLettreRevocationAjournementProcess, false);
            } catch (Exception ex) {
                throw new RETechnicalException(ex);
            }
        }
    }
}
