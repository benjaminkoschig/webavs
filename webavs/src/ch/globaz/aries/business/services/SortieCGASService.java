package ch.globaz.aries.business.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliation;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliationSearchModel;
import ch.globaz.aries.business.models.SimpleSortieCGAS;

public interface SortieCGASService extends JadeApplicationService {
    public SimpleSortieCGAS create(SimpleSortieCGAS sortieCgas) throws JadePersistenceException;

    public SimpleSortieCGAS delete(SimpleSortieCGAS sortieCgas) throws JadePersistenceException;

    public SimpleSortieCGAS read(String idSortieCgas) throws JadePersistenceException;

    public ComplexSortieCGASDecisionCGASAffiliation readComplexSortie(String idSortieCgas)
            throws JadePersistenceException;

    public ComplexSortieCGASDecisionCGASAffiliationSearchModel search(
            ComplexSortieCGASDecisionCGASAffiliationSearchModel complexSearchModel) throws JadePersistenceException;

    public SimpleSortieCGAS update(SimpleSortieCGAS sortieCgas) throws JadePersistenceException;
}
