/**
 * 
 */
package ch.globaz.perseus.business.services.models.retenue;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;

/**
 * @author dde
 * 
 */
public interface SimpleRetenueService extends JadeApplicationService {

    public SimpleRetenue create(SimpleRetenue retenue) throws RetenueException, JadePersistenceException;

    public SimpleRetenue delete(SimpleRetenue retenue) throws RetenueException, JadePersistenceException;

    public int delete(SimpleRetenueSearchModel retenueSearchModel) throws RetenueException, JadePersistenceException;

    public SimpleRetenue read(String idRetenue) throws RetenueException, JadePersistenceException;

    public SimpleRetenueSearchModel search(SimpleRetenueSearchModel searchModel) throws RetenueException,
            JadePersistenceException;

    public SimpleRetenue update(SimpleRetenue retenue) throws RetenueException, JadePersistenceException;

}
