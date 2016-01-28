package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.facture.FactureException;
import ch.globaz.perseus.business.models.qd.SimpleFacture;

/**
 * Publication des services disponibles pour un simple modèle de facture.
 * 
 * @author JSI
 * 
 */
public interface SimpleFactureService extends JadeApplicationService {
    /**
     * Création d'une nouvelle SimpleFacture en base de données
     * 
     * @param facture
     * @return la simple facture créée
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public SimpleFacture create(SimpleFacture facture) throws JadePersistenceException, FactureException,
            PerseusException, JadeApplicationServiceNotAvailableException;

    /**
     * Supprime une SimpleFacture en base de données
     * 
     * @param facture
     * @return la simple facture effacée
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public SimpleFacture delete(SimpleFacture facture) throws JadePersistenceException, FactureException;

    /**
     * TODO Javadoc
     * 
     * @param idFacture
     * @return
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public SimpleFacture read(String idFacture) throws JadePersistenceException, FactureException;

    /**
     * Met à jour une simple facture en base de données.
     * 
     * @param facture
     * @return la facture mise à jour
     * @throws JadePersistenceException
     * @throws FactureException
     */
    public SimpleFacture update(SimpleFacture facture) throws JadePersistenceException, FactureException;
}
