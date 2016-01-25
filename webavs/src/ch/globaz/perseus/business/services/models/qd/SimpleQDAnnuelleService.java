package ch.globaz.perseus.business.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.SimpleQDAnnuelle;

/**
 * Publication des services disponibles pour un simple mod�le de QD principale.
 * 
 * @author JSI
 * 
 */
public interface SimpleQDAnnuelleService extends JadeApplicationService {
    /**
     * Cr�e une nouvelle entr�e dans la base de donn�es pour une QD principale.
     * 
     * @param qd
     * @return la QD principale cr��e
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQDAnnuelle create(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException;

    /**
     * Efface une QD principale dans la base de donn�es.
     * 
     * @param qd
     * @return la QD principale effac�e
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
     * Met � jour une QD principale dans la base de donn�es.
     * 
     * @param qd
     * @return la QD principale mise � jour
     * @throws JadePersistenceException
     * @throws QDException
     */
    public SimpleQDAnnuelle update(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException;

}
