package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;

public interface MembreFamilleService extends JadeApplicationService {

    public MembreFamille read(String idMembreFamille) throws JadePersistenceException, SituationFamilleException;

}
