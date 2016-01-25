package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModelAJAX;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSearchComplexModelAJAX;
import ch.globaz.vulpecula.business.services.decompte.DecompteServiceCRUD;

/**
 * @author Arnaud Geiser (AGE) | Créé le 17 mars 2014
 * 
 */
public class DecompteServiceCRUDImpl implements DecompteServiceCRUD {

    @Override
    public int count(DecompteSearchComplexModelAJAX search) throws JadeApplicationException, JadePersistenceException {
        return JadePersistenceManager.count(search);
    }

    @Override
    public DecompteComplexModelAJAX create(DecompteComplexModelAJAX entity) throws JadeApplicationException,
            JadePersistenceException {
        throw new NotImplementedException();
    }

    @Override
    public DecompteComplexModelAJAX delete(DecompteComplexModelAJAX entity) throws JadeApplicationException,
            JadePersistenceException {
        throw new NotImplementedException();
    }

    @Override
    public DecompteComplexModelAJAX read(String idEntity) throws JadeApplicationException, JadePersistenceException {
        DecompteComplexModel model = new DecompteComplexModel();
        model.setId(idEntity);

        return (DecompteComplexModelAJAX) JadePersistenceManager.read(model);
    }

    @Override
    public DecompteSearchComplexModelAJAX search(DecompteSearchComplexModelAJAX search)
            throws JadeApplicationException, JadePersistenceException {
        return (DecompteSearchComplexModelAJAX) JadePersistenceManager.search(search);
    }

    @Override
    public DecompteComplexModelAJAX update(DecompteComplexModelAJAX entity) throws JadeApplicationException,
            JadePersistenceException {
        throw new NotImplementedException();
    }

}
