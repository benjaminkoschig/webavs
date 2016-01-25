/**
 * 
 */
package ch.globaz.perseus.business.services.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;

/**
 * @author MBO
 * 
 */
public interface DecisionBuilder {
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception;

}
