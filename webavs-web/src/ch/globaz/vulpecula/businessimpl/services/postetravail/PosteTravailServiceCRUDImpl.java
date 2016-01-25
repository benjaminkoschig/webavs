/**
 *
 */
package ch.globaz.vulpecula.businessimpl.services.postetravail;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSearchComplexModel;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailServiceCRUD;

/**
 * @author JPA
 * 
 */
public class PosteTravailServiceCRUDImpl implements PosteTravailServiceCRUD {

    @Override
    public int count(final PosteTravailSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to count poste travail, the search model passed is null!");
        }
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public PosteTravailComplexModel create(final PosteTravailComplexModel simplePosteTravail)
            throws JadePersistenceException {
        throw new NotImplementedException();
    }

    @Override
    public PosteTravailComplexModel delete(final PosteTravailComplexModel simplePosteTravail)
            throws JadeApplicationException, JadePersistenceException {
        throw new NotImplementedException();
    }

    @Override
    public PosteTravailComplexModel read(final String id) throws JadePersistenceException {
        if (id == null) {
            throw new JadePersistenceException("Unable to read poste travail, the id passed is null!");
        }
        PosteTravailComplexModel model = new PosteTravailComplexModel();
        model.setId(id);
        return (PosteTravailComplexModel) JadePersistenceManager.read(model);
    }

    @Override
    public PosteTravailSearchComplexModel search(final PosteTravailSearchComplexModel searchModel)
            throws JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search poste travail, the search model passed is null!");
        }
        return (PosteTravailSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public PosteTravailComplexModel update(final PosteTravailComplexModel simplePosteTravail)
            throws JadePersistenceException {
        throw new NotImplementedException();
    }

    @Override
    public PosteTravailSearchComplexModel searchPostesActifs(final PosteTravailSearchComplexModel searchModel)
            throws JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search poste travail, the search model passed is null!");
        }
        return (PosteTravailSearchComplexModel) JadePersistenceManager.search(searchModel);
    }
}
