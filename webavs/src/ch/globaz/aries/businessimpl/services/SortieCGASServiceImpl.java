package ch.globaz.aries.businessimpl.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliation;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliationSearchModel;
import ch.globaz.aries.business.models.SimpleSortieCGAS;
import ch.globaz.aries.business.services.SortieCGASService;

public class SortieCGASServiceImpl implements SortieCGASService {

    @Override
    public SimpleSortieCGAS create(SimpleSortieCGAS sortieCgas) throws JadePersistenceException {
        return (SimpleSortieCGAS) JadePersistenceManager.add(sortieCgas);
    }

    @Override
    public SimpleSortieCGAS delete(SimpleSortieCGAS sortieCgas) throws JadePersistenceException {
        return (SimpleSortieCGAS) JadePersistenceManager.delete(sortieCgas);
    }

    @Override
    public SimpleSortieCGAS read(String idSortieCgas) throws JadePersistenceException {
        SimpleSortieCGAS sortieCgas = new SimpleSortieCGAS();
        sortieCgas.setIdSortie(idSortieCgas);
        return (SimpleSortieCGAS) JadePersistenceManager.read(sortieCgas);
    }

    @Override
    public ComplexSortieCGASDecisionCGASAffiliation readComplexSortie(String idSortieCgas)
            throws JadePersistenceException {
        ComplexSortieCGASDecisionCGASAffiliation complexSortieCgas = new ComplexSortieCGASDecisionCGASAffiliation();
        complexSortieCgas.setId(idSortieCgas);
        return (ComplexSortieCGASDecisionCGASAffiliation) JadePersistenceManager.read(complexSortieCgas);
    }

    @Override
    public ComplexSortieCGASDecisionCGASAffiliationSearchModel search(
            ComplexSortieCGASDecisionCGASAffiliationSearchModel complexSearchModel) throws JadePersistenceException {
        return (ComplexSortieCGASDecisionCGASAffiliationSearchModel) JadePersistenceManager.search(complexSearchModel);
    }

    @Override
    public SimpleSortieCGAS update(SimpleSortieCGAS sortieCgas) throws JadePersistenceException {
        return (SimpleSortieCGAS) JadePersistenceManager.update(sortieCgas);
    }

}
