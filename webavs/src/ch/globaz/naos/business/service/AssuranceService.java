package ch.globaz.naos.business.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.naos.business.model.AssuranceSimpleModel;

public interface AssuranceService extends JadeApplicationService {
    public String getAssuranceLibelle(String idAssurance, String langue) throws JadePersistenceException,
            JadeApplicationException;

    public AssuranceSimpleModel read(String idAssurance) throws JadePersistenceException, JadeApplicationException;
}
