package ch.globaz.auriga.businessimpl.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.auriga.business.constantes.AUSortieEtat;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliation;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliationSearchModel;
import ch.globaz.auriga.business.models.SimpleSortieCAP;
import ch.globaz.auriga.business.services.SortieCAPService;

public class SortieCAPServiceImpl implements SortieCAPService {
    @Override
    public SimpleSortieCAP changeEtatSortie(String idSortie, AUSortieEtat nouvelEtat) throws JadePersistenceException {
        SimpleSortieCAP sortie = read(idSortie);
        sortie.setEtat(nouvelEtat.getCodeSystem());
        sortie = (SimpleSortieCAP) JadePersistenceManager.update(sortie);
        return sortie;
    }

    @Override
    public SimpleSortieCAP create(SimpleSortieCAP sortieCap) throws JadePersistenceException {
        return (SimpleSortieCAP) JadePersistenceManager.add(sortieCap);
    }

    @Override
    public SimpleSortieCAP delete(SimpleSortieCAP sortieCap) throws JadePersistenceException {
        return (SimpleSortieCAP) JadePersistenceManager.delete(sortieCap);
    }

    @Override
    public SimpleSortieCAP read(String idSortieCap) throws JadePersistenceException {
        SimpleSortieCAP sortieCap = new SimpleSortieCAP();
        sortieCap.setIdSortie(idSortieCap);
        return (SimpleSortieCAP) JadePersistenceManager.read(sortieCap);
    }

    @Override
    public ComplexSortieCAPDecisionCAPAffiliation readComplexSortie(String idSortieCap) throws JadePersistenceException {
        ComplexSortieCAPDecisionCAPAffiliation complexSortieCap = new ComplexSortieCAPDecisionCAPAffiliation();
        complexSortieCap.setId(idSortieCap);
        return (ComplexSortieCAPDecisionCAPAffiliation) JadePersistenceManager.read(complexSortieCap);
    }

    @Override
    public ComplexSortieCAPDecisionCAPAffiliationSearchModel search(
            ComplexSortieCAPDecisionCAPAffiliationSearchModel complexSearchModel) throws JadePersistenceException {
        return (ComplexSortieCAPDecisionCAPAffiliationSearchModel) JadePersistenceManager.search(complexSearchModel);
    }

    @Override
    public SimpleSortieCAP update(SimpleSortieCAP sortieCap) throws JadePersistenceException {
        return (SimpleSortieCAP) JadePersistenceManager.update(sortieCap);
    }

}
