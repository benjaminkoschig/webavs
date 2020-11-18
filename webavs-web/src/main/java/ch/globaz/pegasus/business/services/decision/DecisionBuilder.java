package ch.globaz.pegasus.business.services.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.businessimpl.services.models.decision.DACPublishHandler;

public interface DecisionBuilder {

    public void build(DACPublishHandler handler) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception;

    public void buildDecisionsForFtp(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public void buildForFtpValidation(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;
    public void buildDecisionForAdaptation(DACPublishHandler handler)throws Exception;
    public void buildDecisionForGedOnly(DACPublishHandler handler)throws Exception;
}
