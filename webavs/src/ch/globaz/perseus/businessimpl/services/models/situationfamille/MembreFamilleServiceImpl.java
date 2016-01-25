package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.services.models.situationfamille.MembreFamilleService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author vyj
 */
public class MembreFamilleServiceImpl extends PerseusAbstractServiceImpl implements MembreFamilleService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.MembreFamilleService#read(java.lang.String)
     */
    @Override
    public MembreFamille read(String idMembreFamille) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idMembreFamille)) {
            throw new SituationFamilleException("Unable to read a membreFamille, the id passed is null!");
        }
        MembreFamille membreFamille = new MembreFamille();
        membreFamille.setId(idMembreFamille);
        return (MembreFamille) JadePersistenceManager.read(membreFamille);
    }

}
