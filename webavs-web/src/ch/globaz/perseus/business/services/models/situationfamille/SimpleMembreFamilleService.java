package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;

public interface SimpleMembreFamilleService extends JadeApplicationService {
    public SimpleMembreFamille create(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleMembreFamille createOrRead(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleMembreFamille delete(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleMembreFamille read(String idMembreFamille) throws JadePersistenceException, SituationFamilleException;

    public SimpleMembreFamille update(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException;

}
