package ch.globaz.perseus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;

public interface PrestationService extends JadeApplicationService {

    public int count(PrestationSearchModel search) throws LotException, JadePersistenceException;

    public Prestation create(Prestation prestation) throws JadePersistenceException, LotException;

    public Prestation delete(Prestation prestation) throws JadePersistenceException, LotException;

    public Prestation read(String idPrestation) throws JadePersistenceException, LotException;

    public JadeAbstractSearchModel search(JadeAbstractSearchModel searchModel) throws JadePersistenceException,
            LotException;

    public PrestationSearchModel search(PrestationSearchModel searchModel) throws JadePersistenceException,
            LotException;

    public Prestation update(Prestation prestation) throws JadePersistenceException, LotException;

    public List<Prestation> getPrestationsAsList(Lot lot, boolean isAgenceGestionnaire, List<String> listeDAgence)
            throws JadePersistenceException;
}
