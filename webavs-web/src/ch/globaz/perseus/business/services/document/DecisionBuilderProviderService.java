/**
 * 
 */
package ch.globaz.perseus.business.services.document;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.services.decision.DecisionBuilder;

/**
 * @author MBO
 * 
 */
public interface DecisionBuilderProviderService extends JadeApplicationService {

    public DecisionBuilder getBuilderFor(String idDecision) throws Exception;
}
