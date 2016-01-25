package ch.globaz.vulpecula.businessimpl.services.rubrique;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.vulpecula.business.models.comptabilite.RubriqueSearchSimpleModel;
import ch.globaz.vulpecula.business.services.rubrique.RubriqueService;

public class RubriqueServiceImpl implements RubriqueService {
    /**
     * Opération de recherche sur les rubriques
     */
    @Override
    public RubriqueSearchSimpleModel find(RubriqueSearchSimpleModel searchModel) throws JadePersistenceException,
            JadeApplicationException {
        return (RubriqueSearchSimpleModel) JadePersistenceManager.search(searchModel);
    }
}