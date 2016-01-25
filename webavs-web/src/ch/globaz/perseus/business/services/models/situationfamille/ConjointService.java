package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.Conjoint;
import ch.globaz.perseus.business.models.situationfamille.ConjointSearchModel;

public interface ConjointService extends JadeApplicationService {

    public int count(ConjointSearchModel search) throws SituationFamilleException, JadePersistenceException;

    public Conjoint create(Conjoint conjoint) throws JadePersistenceException, SituationFamilleException;

    public Conjoint delete(Conjoint conjoint) throws JadePersistenceException, SituationFamilleException;

    public String getIdConjoint(String idTiers) throws JadePersistenceException, SituationFamilleException;

    public Conjoint read(String idConjoint) throws JadePersistenceException, SituationFamilleException;

    public ConjointSearchModel search(ConjointSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException;

    public Conjoint update(Conjoint conjoint) throws JadePersistenceException, SituationFamilleException;

}
