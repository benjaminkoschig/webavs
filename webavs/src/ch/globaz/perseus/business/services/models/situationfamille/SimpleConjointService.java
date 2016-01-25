package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleConjoint;

public interface SimpleConjointService extends JadeApplicationService {
    public SimpleConjoint create(SimpleConjoint conjoint) throws JadePersistenceException, SituationFamilleException;

    public SimpleConjoint createOrRead(SimpleConjoint conjoint) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleConjoint delete(SimpleConjoint conjoint) throws JadePersistenceException, SituationFamilleException;

    public SimpleConjoint read(String idConjoint) throws JadePersistenceException, SituationFamilleException;

    public SimpleConjoint update(SimpleConjoint conjoint) throws JadePersistenceException, SituationFamilleException;

}
