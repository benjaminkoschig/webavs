/**
 * 
 */
package ch.globaz.amal.business.services.sedexCO;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;

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
     * @throws JadeSedexMessageNotHandledException
     * 
     */
    public void importMessages(SedexMessage message) throws JadeApplicationException, JadePersistenceException,
            JadeSedexMessageNotHandledException;

    public AdministrationSearchComplexModel find(AdministrationSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Création de la liste des annonces au format CSV
     * 
     * @param idMessageSedex
     * @return L'url du fichier crée
     * 
     */
    public String exportListAnnonces(String filters, String order) throws JadeApplicationException,
            JadePersistenceException;
}
