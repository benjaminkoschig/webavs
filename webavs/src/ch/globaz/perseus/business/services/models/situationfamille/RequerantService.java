package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.Requerant;
import ch.globaz.perseus.business.models.situationfamille.RequerantSearchModel;

public interface RequerantService extends JadeApplicationService {

    public int count(RequerantSearchModel search) throws SituationFamilleException, JadePersistenceException;

    public Requerant create(Requerant requerant) throws JadePersistenceException, SituationFamilleException;

    public Requerant delete(Requerant requerant) throws JadePersistenceException, SituationFamilleException;

    public Requerant read(String idRequerant) throws JadePersistenceException, SituationFamilleException;

    public RequerantSearchModel search(RequerantSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException;

    public Requerant update(Requerant requerant) throws JadePersistenceException, SituationFamilleException;

}
