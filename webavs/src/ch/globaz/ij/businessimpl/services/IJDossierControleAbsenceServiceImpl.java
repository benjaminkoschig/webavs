package ch.globaz.ij.businessimpl.services;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsences;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsencesSearchModel;
import ch.globaz.ij.business.services.IJDossierControleAbsenceService;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;

public class IJDossierControleAbsenceServiceImpl implements IJDossierControleAbsenceService {

    @Override
    public int count(IJSimpleDossierControleAbsencesSearchModel search) throws ServiceBusinessException,
            ServiceTechnicalException {
        if (search == null) {
            throw new ServiceTechnicalException(
                    "IJDossierControleAbsenceServiceImpl : unable to count values because searchModel is null");
        }
        try {
            return JadePersistenceManager.count(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException("IJDossierControleAbsenceServiceImpl : unable to count values");
        }
    }

    @Override
    public IJSimpleDossierControleAbsences create(IJSimpleDossierControleAbsences entity)
            throws ServiceBusinessException, ServiceTechnicalException {
        validateEntity(entity);
        IJSimpleDossierControleAbsences returnEntity = null;
        try {
            returnEntity = (IJSimpleDossierControleAbsences) JadePersistenceManager.add(entity);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnEntity;
    }

    @Override
    public IJSimpleDossierControleAbsences delete(IJSimpleDossierControleAbsences entity)
            throws ServiceBusinessException, ServiceTechnicalException {
        validateEntity(entity);
        IJSimpleDossierControleAbsences returnedEntity = null;
        try {
            returnedEntity = (IJSimpleDossierControleAbsences) JadePersistenceManager.delete(entity);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnedEntity;
    }

    @Override
    public IJSimpleDossierControleAbsences historiser(String idDossier) throws ServiceTechnicalException,
            ServiceBusinessException {
        IJSimpleDossierControleAbsences dossier = read(idDossier);

        if (dossier == null) {
            throw new ServiceTechnicalException("Can not retreive entity IJSimpleDossierControleAbsences with id = "
                    + idDossier);
        }
        dossier.setIsHistorise(true);
        update(dossier);
        return dossier;
    }

    @Override
    public IJSimpleDossierControleAbsences read(String idEntity) throws ServiceBusinessException,
            ServiceTechnicalException {
        IJSimpleDossierControleAbsences entity = new IJSimpleDossierControleAbsences();
        entity.setIdDossierControleAbsences(idEntity);
        IJSimpleDossierControleAbsences returnedEntity = null;
        try {
            returnedEntity = (IJSimpleDossierControleAbsences) JadePersistenceManager.read(entity);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnedEntity;
    }

    @Override
    public IJSimpleDossierControleAbsencesSearchModel search(IJSimpleDossierControleAbsencesSearchModel search)
            throws ServiceBusinessException, ServiceTechnicalException {
        IJSimpleDossierControleAbsencesSearchModel returnedSearchModel = null;
        try {
            returnedSearchModel = (IJSimpleDossierControleAbsencesSearchModel) JadePersistenceManager.search(search);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnedSearchModel;
    }

    @Override
    public IJSimpleDossierControleAbsences update(IJSimpleDossierControleAbsences entity)
            throws ServiceBusinessException, ServiceTechnicalException {
        IJSimpleDossierControleAbsences returnedEntity = null;
        IJSimpleDossierControleAbsences returnedEntity2 = null;
        // traitement pas optimisé mais pas d'id tiers dans l'entité et,
        // en théorie, l'id tiers ne devrait pas être modifié
        try {
            returnedEntity = read(entity.getIdDossierControleAbsences());
            if (returnedEntity == null) {
                throw new ServiceTechnicalException("No entity with id : " + entity.getIdDossierControleAbsences()
                        + " found for update");
            }
            returnedEntity.setDateDebutFPI(entity.getDateDebutFPI());
            returnedEntity.setDateDebutIJAI(entity.getDateDebutIJAI());

            returnedEntity2 = (IJSimpleDossierControleAbsences) JadePersistenceManager.update(returnedEntity);
        } catch (JadePersistenceException e) {
            throw new ServiceTechnicalException(e.getMessage(), e);
        }
        return returnedEntity2;
    }

    private void validateEntity(IJSimpleDossierControleAbsences entity) throws ServiceBusinessException,
            ServiceTechnicalException {

        // On ne peut pas valider l'id dossier car l'entitée n'est pas encore créée
        // if (!JadeStringUtil.isBlankOrZero(entity.getIdDossierControleAbsences())) {
        // throw new ServiceTechnicalException("No id found for : IdDossierControleAbsences");
        // }

        if (JadeStringUtil.isBlankOrZero(entity.getIdTiers())) {
            throw new ServiceTechnicalException("No id found for : IdTiers");
        }

        // TODO remettre ce test !!!
        // if ((!JadeDateUtil.isGlobazDate(entity.getDateDebutFPI()))
        // && (!JadeDateUtil.isGlobazDate(entity.getDateDebutIJAI()))) {
        // throw new ServiceBusinessException(this.translate("ERROR_AT_LEAST_ONE_OF_FPI_OR_IJAI_DATE_MUST_BE_SET"));
        // }
    }

}
