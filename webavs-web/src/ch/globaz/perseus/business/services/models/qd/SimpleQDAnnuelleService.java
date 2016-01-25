package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.SimpleQDAnnuelle;

/**
 * Publication des services disponibles pour un simple modèle de QD principale.
 * 
 * @author JSI
 * 
 */
public interface SimpleQDAnnuelleService extends JadeApplicationService {
    /**
     * Crée une nouvelle entrée dans la base de données pour une QD principale.
     * 
     * @param qd
     * @return la QD principale créée
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQDAnnuelle create(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException;

    /**
     * Efface une QD principale dans la base de données.
     * 
     * @param qd
     * @return la QD principale effacée
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQDAnnuelle delete(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException;

    /**
     * TODO Javadoc
     * 
     * @param idQD
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQDAnnuelle read(String idQD) throws JadePersistenceException, QDException;

    /**
     * Met à jour une QD principale dans la base de données.
     * 
     * @param qd
     * @return la QD principale mise à jour
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQDAnnuelle update(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException;

}
