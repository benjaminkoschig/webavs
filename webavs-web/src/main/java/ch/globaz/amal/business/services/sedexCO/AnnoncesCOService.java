/**
 * 
 */
package ch.globaz.amal.business.services.sedexCO;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * @author cbu
 * 
 */
public interface AnnoncesCOService extends JadeApplicationService {

    /**
     * Importation des annonces CO<br>
     * <br>
     * Appelé par le gestionnaire SEDEX (JadeSedexService.xml)
     * 
     * @param idMessageSedex
     * 
     */
    public void importMessages(SedexMessage message) throws JadeApplicationException, JadePersistenceException;
}
