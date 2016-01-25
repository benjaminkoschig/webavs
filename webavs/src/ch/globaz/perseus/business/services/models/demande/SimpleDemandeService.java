/**
 * 
 */
package ch.globaz.perseus.business.services.models.demande;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.demande.SimpleDemandeSearchModel;

/**
 * @author DDE
 * 
 */
public interface SimpleDemandeService extends JadeApplicationService {
    public int count(SimpleDemandeSearchModel searchModel) throws JadePersistenceException, DemandeException;

    public SimpleDemande create(SimpleDemande demande) throws JadePersistenceException, DemandeException;

    public SimpleDemande delete(SimpleDemande demande) throws JadePersistenceException, DemandeException;

    public SimpleDemande read(String idDemande) throws JadePersistenceException, DemandeException;

    public SimpleDemandeSearchModel search(SimpleDemandeSearchModel searchModel) throws JadePersistenceException,
            DemandeException;

    public SimpleDemande update(SimpleDemande demande) throws JadePersistenceException, DemandeException;
}
