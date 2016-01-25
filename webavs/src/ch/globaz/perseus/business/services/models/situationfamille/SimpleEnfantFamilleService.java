package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamilleSearchModel;

/**
 * @author DDE
 * 
 */
public interface SimpleEnfantFamilleService extends JadeApplicationService {

    public int count(SimpleEnfantFamilleSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleEnfantFamille create(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException;

    /**
     * Ajout d'enfant dans la situation familiale pour les rentes-ponts (sans les check) (Bricolage)
     * 
     * @param simpleEnfantfamille
     * @return
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public SimpleEnfantFamille createForRP(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleEnfantFamille delete(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException;

    public int delete(SimpleEnfantFamilleSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleEnfantFamille read(String idSimpleEnfantFamille) throws JadePersistenceException,
            SituationFamilleException;

    public SimpleEnfantFamilleSearchModel search(SimpleEnfantFamilleSearchModel searchModel)
            throws JadePersistenceException, SituationFamilleException;

    public SimpleEnfantFamille update(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException;

}
