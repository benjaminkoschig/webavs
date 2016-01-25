/**
 * 
 */
package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.rentepont.QDRentePontException;
import ch.globaz.perseus.business.models.rentepont.QDRentePont;
import ch.globaz.perseus.business.models.rentepont.QDRentePontSearchModel;

/**
 * @author JSI
 * 
 */
public interface QDRentePontService extends JadeApplicationService {
    public int count(QDRentePontSearchModel search) throws QDRentePontException, JadePersistenceException;

    public QDRentePont create(QDRentePont qdRentePont) throws JadePersistenceException, QDRentePontException;

    public QDRentePont delete(QDRentePont qdRentePont) throws JadePersistenceException, QDRentePontException;

    public float getMontantMaximalRemboursable(QDRentePont qdRentePont);

    public QDRentePont read(String idQDRentePont) throws JadePersistenceException, QDRentePontException;

    public QDRentePontSearchModel search(QDRentePontSearchModel searchModel) throws JadePersistenceException,
            QDRentePontException;

    public QDRentePont update(QDRentePont qdRentePont) throws JadePersistenceException, QDRentePontException;

}
