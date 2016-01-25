/**
 * 
 */
package ch.globaz.vulpecula.businessimpl.services.registre;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSimpleModel;
import ch.globaz.vulpecula.business.services.registre.ParametreCotisationAssociationServiceCRUD;

/**
 * @author JPA
 * 
 */
public class ParametreCotisationAssociationServiceCRUDImpl implements ParametreCotisationAssociationServiceCRUD {
    /**
     * service description
     */
    private static final String SERVICE_DESCRIPTION = "CotisationsCM";

    @Override
    public int count(final ParametreCotisationAssociationSearchComplexModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to count cotisationsCM, the search model passed is null!");
        }
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public ParametreCotisationAssociationComplexModel create(final ParametreCotisationAssociationComplexModel entity)
            throws JadeApplicationException, JadePersistenceException {
        if (entity == null) {
            throw new JadePersistenceException("Unable to create  " + ParametreCotisationAssociationServiceCRUDImpl.SERVICE_DESCRIPTION
                    + ", the entity passed is null!");
        }
        ParametreCotisationAssociationSimpleModel simpleModel = (ParametreCotisationAssociationSimpleModel) JadePersistenceManager
                .add(entity.getParametreCotisationAssociationSimpleModel());
        entity.setParametreCotisationAssociationSimpleModel(simpleModel);
        return entity;
    }

    @Override
    public ParametreCotisationAssociationComplexModel delete(final ParametreCotisationAssociationComplexModel entity)
            throws JadeApplicationException, JadePersistenceException {
        if (entity == null) {
            throw new JadePersistenceException("Unable to delete  " + ParametreCotisationAssociationServiceCRUDImpl.SERVICE_DESCRIPTION
                    + ", the entity passed is null!");
        }
        ParametreCotisationAssociationSimpleModel simpleModel = (ParametreCotisationAssociationSimpleModel) JadePersistenceManager
                .delete(entity.getParametreCotisationAssociationSimpleModel());
        entity.setParametreCotisationAssociationSimpleModel(simpleModel);
        return entity;
    }

    @Override
    public ParametreCotisationAssociationComplexModel read(final String idEntity) throws JadeApplicationException,
            JadePersistenceException {
        if (idEntity == null) {
            throw new JadePersistenceException("Unable to read  " + ParametreCotisationAssociationServiceCRUDImpl.SERVICE_DESCRIPTION
                    + ", the id passed is null!");
        }

        ParametreCotisationAssociationComplexModel model = new ParametreCotisationAssociationComplexModel();
        model.setId(idEntity);

        return (ParametreCotisationAssociationComplexModel) JadePersistenceManager.read(model);
    }

    @Override
    public ParametreCotisationAssociationSearchComplexModel search(
            final ParametreCotisationAssociationSearchComplexModel searchModel) throws JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search cotisationsCM, the search model passed is null!");
        }
        return (ParametreCotisationAssociationSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public ParametreCotisationAssociationComplexModel update(
            final ParametreCotisationAssociationComplexModel cotisationCM) throws JadePersistenceException {
        if (cotisationCM == null) {
            throw new JadePersistenceException("Unable to update cotisationCM, the model passed is null!");
        }
        ParametreCotisationAssociationSimpleModel simpleModel = (ParametreCotisationAssociationSimpleModel) JadePersistenceManager
                .update(cotisationCM.getParametreCotisationAssociationSimpleModel());
        cotisationCM.setParametreCotisationAssociationSimpleModel(simpleModel);
        return cotisationCM;
    }
}
