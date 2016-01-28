package ch.globaz.pegasus.businessimpl.services.models.pmtmensuel;

import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.services.models.pmtmensuel.PmtMensuelService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class PmtMensuelServiceImpl extends PegasusAbstractServiceImpl implements PmtMensuelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.parametre.PmtMensuelService#getDateDernierPmt()
     */
    @Override
    public String getDateDernierPmt() throws PmtMensuelException {

        // Récupère l'élément session du thread context - Nécessaire pour
        // l'interfaçage entre nouveau et ancien framework
        // BSession session = (BSession) JadeThread.getTemporaryObject("bsession");
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session == null) {
            throw new PmtMensuelException("Unable to create transaction, session not defined in thread context!");
        }

        // utilisation de la methode des rentes
        return REPmtMensuel.getDateDernierPmt(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.parametre.PmtMensuelService#getDateProchainPmt()
     */
    @Override
    public String getDateProchainPmt() throws PmtMensuelException {

        // Récupère l'élément session du thread context - Nécessaire pour
        // l'interfaçage entre nouveau et ancien framework
        BSession session = (BSession) JadeThread.getTemporaryObject("bsession");
        if (session == null) {
            throw new PmtMensuelException("Unable to create transaction, session not defined in thread context!");
        }

        // utilisation de la methode des rentes
        return REPmtMensuel.getDateProchainPmt(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.parametre.PmtMensuelService#isValidationDecisionAuthorise()
     */
    @Override
    public boolean isValidationDecisionAuthorise() throws PmtMensuelException {

        // Récupère l'élément session du thread context - Nécessaire pour
        // l'interfaçage entre nouveau et ancien framework
        BSession session = (BSession) JadeThread.getTemporaryObject("bsession");
        if (session == null) {
            throw new PmtMensuelException("Unable to create transaction, session not defined in thread context!");
        }

        // utilisation de la methode des rentes
        return REPmtMensuel.isValidationDecisionAuthorise(session);
    }

}
