package ch.globaz.vulpecula.business.services.travailleur;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.travailleur.PersonneEtendueMetierSearchComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSearchComplexModel;

/**
 * @author JPA
 * 
 */
public interface TravailleurServiceCRUD extends JadeCrudService<TravailleurComplexModel, TravailleurSearchComplexModel> {

    @Override
    int count(TravailleurSearchComplexModel searchModel) throws JadeApplicationException, JadePersistenceException;

    @Override
    TravailleurSearchComplexModel search(TravailleurSearchComplexModel searchModel) throws JadePersistenceException;

    /**
     * Réservé pour la création d'un nouveau travailleur
     * personne physique
     * sexe obligatoire
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     */
    PersonneEtendueMetierSearchComplexModel searchForNewTravailleur(PersonneEtendueMetierSearchComplexModel searchModel)
            throws JadePersistenceException;
}
