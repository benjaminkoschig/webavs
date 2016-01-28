package ch.globaz.auriga.business.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.auriga.business.constantes.AUSortieEtat;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliation;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliationSearchModel;
import ch.globaz.auriga.business.models.SimpleSortieCAP;

public interface SortieCAPService extends JadeApplicationService {
    public SimpleSortieCAP changeEtatSortie(String idSortie, AUSortieEtat nouvelEtat) throws JadePersistenceException;

    public SimpleSortieCAP create(SimpleSortieCAP sortieCap) throws JadePersistenceException;

    public SimpleSortieCAP delete(SimpleSortieCAP sortieCap) throws JadePersistenceException;

    public SimpleSortieCAP read(String idSortieCap) throws JadePersistenceException;

    public ComplexSortieCAPDecisionCAPAffiliation readComplexSortie(String idSortieCap) throws JadePersistenceException;

    public ComplexSortieCAPDecisionCAPAffiliationSearchModel search(
            ComplexSortieCAPDecisionCAPAffiliationSearchModel complexSearchModel) throws JadePersistenceException;

    public SimpleSortieCAP update(SimpleSortieCAP sortieCap) throws JadePersistenceException;
}
