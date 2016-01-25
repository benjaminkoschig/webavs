package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.rentepont.FactureRentePontException;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.FactureRentePontSearchModel;

/**
 * @author jsi
 * 
 */
public interface FactureRentePontService extends JadeApplicationService {
    public int count(FactureRentePontSearchModel search) throws FactureRentePontException, JadePersistenceException;

    public FactureRentePont create(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;

    public FactureRentePont delete(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;

    public FactureRentePont read(String idFactureRentePont) throws JadePersistenceException, FactureRentePontException;

    public FactureRentePont restituer(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;

    public FactureRentePontSearchModel search(FactureRentePontSearchModel searchModel) throws JadePersistenceException,
            FactureRentePontException;

    public FactureRentePont update(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;

    public FactureRentePont validate(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;

}
