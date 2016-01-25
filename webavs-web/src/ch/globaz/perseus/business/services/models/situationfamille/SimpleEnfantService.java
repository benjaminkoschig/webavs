package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfant;

public interface SimpleEnfantService extends JadeApplicationService {
    public SimpleEnfant create(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException;

    public SimpleEnfant createOrRead(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException;

    public SimpleEnfant delete(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException;

    public SimpleEnfant read(String idEnfant) throws JadePersistenceException, SituationFamilleException;

    public SimpleEnfant update(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException;

}
