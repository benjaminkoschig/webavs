/**
 * 
 */
package ch.globaz.vulpecula.businessimpl.services.ebusiness;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.vulpecula.business.models.ebusiness.TravailleurEbuSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurEbuSearchSimpleModel;
import ch.globaz.vulpecula.business.services.ebusiness.NouveauTravailleurServiceCRUD;

/**
 * @author JPA
 * 
 */
public class NouveauTravailleurServiceCRUDImpl implements NouveauTravailleurServiceCRUD {

    @Override
    public int count(final TravailleurEbuSearchSimpleModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to count travailleur, the search model passed is null!");
        }
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public TravailleurEbuSimpleModel read(final String idEntity) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TravailleurEbuSearchSimpleModel search(final TravailleurEbuSearchSimpleModel searchModel)
            throws JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search travailleur, the search model passed is null!");
        }
        return (TravailleurEbuSearchSimpleModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public TravailleurEbuSimpleModel update(final TravailleurEbuSimpleModel travailleur)
            throws JadePersistenceException {
        if (travailleur == null) {
            throw new JadePersistenceException("Unable to update travailleur, the model passed is null!");
        }
        return travailleur;
    }

    @Override
    public TravailleurEbuSimpleModel create(TravailleurEbuSimpleModel entity) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TravailleurEbuSimpleModel delete(TravailleurEbuSimpleModel entity) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }
}
