package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleRequerant;

public interface SimpleRequerantService extends JadeApplicationService {
    public SimpleRequerant create(SimpleRequerant requerant) throws JadePersistenceException, SituationFamilleException;

    public SimpleRequerant createOrRead(SimpleRequerant requerant) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleRequerant delete(SimpleRequerant requerant) throws JadePersistenceException, SituationFamilleException;

    public SimpleRequerant read(String idRequerant) throws JadePersistenceException, SituationFamilleException;

    public SimpleRequerant update(SimpleRequerant requerant) throws JadePersistenceException, SituationFamilleException;

}
