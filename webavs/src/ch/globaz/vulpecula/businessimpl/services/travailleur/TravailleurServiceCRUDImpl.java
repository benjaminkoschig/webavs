/**
 * 
 */
package ch.globaz.vulpecula.businessimpl.services.travailleur;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.vulpecula.business.models.travailleur.PersonneEtendueMetierSearchComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSearchComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSimpleModel;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurServiceCRUD;

/**
 * @author JPA
 * 
 */
public class TravailleurServiceCRUDImpl implements TravailleurServiceCRUD {

    @Override
    public int count(final TravailleurSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to count travailleur, the search model passed is null!");
        }
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public TravailleurComplexModel create(final TravailleurComplexModel entity) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TravailleurComplexModel delete(final TravailleurComplexModel entity) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TravailleurComplexModel read(final String idEntity) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TravailleurSearchComplexModel search(final TravailleurSearchComplexModel searchModel)
            throws JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search travailleur, the search model passed is null!");
        }
        return (TravailleurSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public PersonneEtendueMetierSearchComplexModel searchForNewTravailleur(
            final PersonneEtendueMetierSearchComplexModel searchModel) throws JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search travailleur, the search model passed is null!");
        }
        if (searchModel.getForDesignation1Like() != null) {
            searchModel.setForDesignation1Like(JadeStringUtil.convertSpecialChars(searchModel.getForDesignation1Like()
                    .toUpperCase()));
        }
        if (searchModel.getForDesignation2Like() != null) {
            searchModel.setForDesignation2Like(JadeStringUtil.convertSpecialChars(searchModel.getForDesignation2Like()
                    .toUpperCase()));
        }
        return (PersonneEtendueMetierSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public TravailleurComplexModel update(final TravailleurComplexModel travailleur) throws JadePersistenceException {
        if (travailleur == null) {
            throw new JadePersistenceException("Unable to update travailleur, the model passed is null!");
        }
        travailleur.setTravailleurSimpleModel((TravailleurSimpleModel) JadePersistenceManager.update(travailleur
                .getTravailleurSimpleModel()));
        return travailleur;
    }
}
