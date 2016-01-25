package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.SimpleQD;
import ch.globaz.perseus.business.models.qd.SimpleQDSearch;

/**
 * Publication des services disponibles pour un simple modèle de sous QD.
 * 
 * @author JSI
 * 
 */
public interface SimpleQDService extends JadeApplicationService {

    /**
     * Crée une nouvelle entrée dans la base de données pour une sous QD.
     * 
     * @param qd
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQD create(SimpleQD qd) throws JadePersistenceException, QDException;

    /**
     * Efface une sous QD dans la base de données.
     * 
     * @param qd
     * @return la sous QD effacée
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQD delete(SimpleQD qd) throws JadePersistenceException, QDException;

    /**
     * TODO Javadoc
     * 
     * @param qd
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQD read(String idQD) throws JadePersistenceException, QDException;

    public SimpleQDSearch search(SimpleQDSearch search) throws QDException, JadePersistenceException;

    /**
     * Met à jour une sous QD dans la base de données.
     * 
     * @param qd
     * @return la sous QD mise à jour
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQD update(SimpleQD qd) throws JadePersistenceException, QDException;

}
