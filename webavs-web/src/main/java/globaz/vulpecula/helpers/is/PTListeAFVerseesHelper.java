package globaz.vulpecula.helpers.is;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.vulpecula.business.exception.SaisiePeriodeException;
import globaz.vulpecula.vb.is.PTListeAFVerseesViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.process.is.ListeAFVerseesProcess;

/**
 * 
 * @author jwe
 * 
 */
public class PTListeAFVerseesHelper extends FWHelper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTListeAFVerseesViewBean vb = (PTListeAFVerseesViewBean) viewBean;
            ListeAFVerseesProcess listeAFVerseesProcess = new ListeAFVerseesProcess();
            listeAFVerseesProcess.setEMailAddress(vb.getEmail());
            checkPeriodValidty(vb.getDateDebut(), vb.getDateFin(), (BSession) session);
            listeAFVerseesProcess.setDateDebut(vb.getDateDebut());
            listeAFVerseesProcess.setDateFin(vb.getDateFin());
            BProcessLauncher.start(listeAFVerseesProcess);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    private void checkPeriodValidty(String dateFrom, String dateTo, BSession session) throws SaisiePeriodeException {

        if (JadeStringUtil.isBlankOrZero(dateFrom)) {

            throw new SaisiePeriodeException(session.getLabel("LISTE_ANNONCE_ERREUR_PERIODE_SAISIE_SANS_DD"));
        }
        Date dateDebut = new Date(dateFrom);
        Date dateFin = new Date(dateTo);

        // Vérifier si la période donnée est une periode valable
        if (!JadeStringUtil.isBlankOrZero(dateTo)
                && !JadeDateUtil.isDateBefore(dateDebut.getFirstDayOfMonth().getSwissValue(), dateFin
                        .getLastDayOfMonth().getSwissValue())) {

            throw new SaisiePeriodeException(session.getLabel("LISTE_ANNONCE_ERREUR_PERIODE_SAISIE"));
        }
    }

}
