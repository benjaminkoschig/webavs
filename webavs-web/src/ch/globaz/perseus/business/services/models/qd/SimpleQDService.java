package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.SimpleQD;
import ch.globaz.perseus.business.models.qd.SimpleQDSearch;

/**
 * Publication des services disponibles pour un simple mod�le de sous QD.
 * 
 * @author JSI
 * 
 */
public interface SimpleQDService extends JadeApplicationService {

    /**
     * Cr�e une nouvelle entr�e dans la base de donn�es pour une sous QD.
     * 
     * @param qd
     * @return
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQD create(SimpleQD qd) throws JadePersistenceException, QDException;

    /**
     * Efface une sous QD dans la base de donn�es.
     * 
     * @param qd
     * @return la sous QD effac�e
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
     * Met � jour une sous QD dans la base de donn�es.
     * 
     * @param qd
     * @return la sous QD mise � jour
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQD update(SimpleQD qd) throws JadePersistenceException, QDException;

}
