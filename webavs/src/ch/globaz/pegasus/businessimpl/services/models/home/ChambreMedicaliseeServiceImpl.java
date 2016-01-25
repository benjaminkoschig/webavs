package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.home.ChambreMedicaliseeException;
import ch.globaz.pegasus.business.models.home.ChambreMedicaliseeSearch;
import ch.globaz.pegasus.business.services.models.home.ChambreMedicaliseeService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class ChambreMedicaliseeServiceImpl extends PegasusAbstractServiceImpl implements ChambreMedicaliseeService {

    @Override
    public int count(ChambreMedicaliseeSearch search) throws ChambreMedicaliseeException, JadePersistenceException {

        if (search == null) {
            throw new ChambreMedicaliseeException(
                    "Unable to count chambreMedicalisee, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public ChambreMedicaliseeSearch search(ChambreMedicaliseeSearch prixChambreSearch) throws JadePersistenceException,
            ChambreMedicaliseeException {
        // TODO Auto-generated method stub
        return null;
    }

}
