package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;

public interface FortuneService extends JadeApplicationService {
    /**
     * Donne la fortune RI de la PC Accordee liee a la decision
     * 
     * @param idDecision
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    public BigDecimal calculeFortuneFromDecision(String idDecision) throws JadeApplicationServiceNotAvailableException,
            DecisionException;

    /**
     * Donne la fortune RI d'une PC Accordee
     * 
     * @param idPcAccordee
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     */
    public BigDecimal calculeFortuneFromPcAccordee(String idPcAccordee)
            throws JadeApplicationServiceNotAvailableException, PCAccordeeException;
}
