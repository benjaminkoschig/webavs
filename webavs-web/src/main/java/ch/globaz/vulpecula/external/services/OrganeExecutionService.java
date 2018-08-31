package ch.globaz.vulpecula.external.services;

import ch.globaz.vulpecula.external.models.osiris.OrganeExecution;

public interface OrganeExecutionService {
    OrganeExecution findById(String id);
}
