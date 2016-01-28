package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;

/**
 * @author DDE
 * 
 */
public interface RentePontService extends JadeApplicationService {

    /**
     * Permet de calculer le rétro actif dû pour une demande à sa validation
     * 
     * @param rentePont
     * @return
     * @throws JadePersistenceException
     * @throws RentePontException
     */
    public Float calculerRetro(RentePont rentePont) throws JadePersistenceException, RentePontException;

    public int count(RentePontSearchModel search) throws RentePontException, JadePersistenceException;

    public RentePont create(RentePont rentePont) throws JadePersistenceException, RentePontException;

    public RentePont delete(RentePont rentePont) throws JadePersistenceException, RentePontException;

    public List<Enfant> getListEnfants(RentePont rentePont) throws JadePersistenceException, RentePontException;

    public boolean hasDossierRentePontValidee(String idDossier) throws JadePersistenceException, RentePontException;

    public RentePont read(String idRentePont) throws JadePersistenceException, RentePontException;

    public RentePontSearchModel search(RentePontSearchModel searchModel) throws JadePersistenceException,
            RentePontException;

    public RentePont update(RentePont rentePont) throws JadePersistenceException, RentePontException;

    public RentePont validate(RentePont rentePont) throws JadePersistenceException, RentePontException;

}
