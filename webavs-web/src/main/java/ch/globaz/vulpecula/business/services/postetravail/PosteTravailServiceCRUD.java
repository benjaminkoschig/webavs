package ch.globaz.vulpecula.business.services.postetravail;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSearchComplexModel;

/**
 * @author JPA
 * 
 */
public interface PosteTravailServiceCRUD extends
        JadeCrudService<PosteTravailComplexModel, PosteTravailSearchComplexModel> {

    @Override
    int count(PosteTravailSearchComplexModel searchModel) throws JadeApplicationException, JadePersistenceException;

    @Override
    PosteTravailSearchComplexModel search(PosteTravailSearchComplexModel searchModel) throws JadePersistenceException;

    PosteTravailSearchComplexModel searchPostesActifs(PosteTravailSearchComplexModel searchModel)
            throws JadePersistenceException;
}
