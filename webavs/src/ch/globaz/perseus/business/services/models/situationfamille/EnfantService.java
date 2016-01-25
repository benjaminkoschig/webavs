package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.EnfantSearchModel;

public interface EnfantService extends JadeApplicationService {

    public int count(EnfantSearchModel search) throws SituationFamilleException, JadePersistenceException;

    public Enfant create(Enfant enfant) throws JadePersistenceException, SituationFamilleException;

    public Enfant delete(Enfant enfant) throws JadePersistenceException, SituationFamilleException;

    public Enfant read(String idEnfant) throws JadePersistenceException, SituationFamilleException;

    public EnfantSearchModel search(EnfantSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException;

    public Enfant update(Enfant enfant) throws JadePersistenceException, SituationFamilleException;

}
