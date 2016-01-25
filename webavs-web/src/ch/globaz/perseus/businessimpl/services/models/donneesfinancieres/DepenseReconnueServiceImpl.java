/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation;
import ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class DepenseReconnueServiceImpl extends PerseusAbstractServiceImpl implements DepenseReconnueService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService#count(ch.globaz.perseus.
     * business. models.donneesfinancieres.DepenseReconnueSearchModel)
     */
    @Override
    public int count(DepenseReconnueSearchModel search) throws DonneesFinancieresException, JadePersistenceException {
        if (search == null) {
            throw new DonneesFinancieresException("Unable to count depenseReconnues, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService#create(ch.globaz.perseus
     * .business .models.donneesfinancieres.DepenseReconnue)
     */
    @Override
    public DepenseReconnue create(DepenseReconnue depenseReconnue) throws JadePersistenceException,
            DonneesFinancieresException {
        if (depenseReconnue == null) {
            throw new DonneesFinancieresException("Unable to create depenseReconnue, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = depenseReconnue.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere.setIdDemande(depenseReconnue.getDemande().getId());
            simpleDonneeFinanciere.setIdMembreFamille(depenseReconnue.getMembreFamille().getId());

            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().create(
                    simpleDonneeFinanciere);

            depenseReconnue.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (depenseReconnue.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = depenseReconnue
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation.setIdDonneeFinanciere(simpleDonneeFinanciere.getId());

                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().create(simpleDonneeFinanciereSpecialisation);

                depenseReconnue.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return depenseReconnue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService#delete(ch.globaz.perseus
     * .business .models.donneesfinancieres.DepenseReconnue)
     */
    @Override
    public DepenseReconnue delete(DepenseReconnue depenseReconnue) throws JadePersistenceException,
            DonneesFinancieresException {
        if (depenseReconnue == null) {
            throw new DonneesFinancieresException("Unable to update depenseReconnue, the given model is null!");
        }
        try {
            SimpleDonneeFinanciere simpleDonneeFinanciere = depenseReconnue.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().delete(
                    simpleDonneeFinanciere);
            depenseReconnue.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (depenseReconnue.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = depenseReconnue
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().delete(simpleDonneeFinanciereSpecialisation);
                depenseReconnue.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return depenseReconnue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService#read(java.lang.String)
     */
    @Override
    public DepenseReconnue read(String idDepenseReconnue) throws JadePersistenceException, DonneesFinancieresException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService#search(ch.globaz.perseus
     * .business .models.donneesfinancieres.DepenseReconnueSearchModel)
     */
    @Override
    public DepenseReconnueSearchModel search(DepenseReconnueSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException {
        if (searchModel == null) {
            throw new DonneesFinancieresException("Unable to search depenseReconnues, the search model passed is null!");
        }
        return (DepenseReconnueSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.DepenseReconnueService#update(ch.globaz.perseus
     * .business .models.donneesfinancieres.DepenseReconnue)
     */
    @Override
    public DepenseReconnue update(DepenseReconnue depenseReconnue) throws JadePersistenceException,
            DonneesFinancieresException {
        if (depenseReconnue == null) {
            throw new DonneesFinancieresException("Unable to update depenseReconnue, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = depenseReconnue.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().update(
                    simpleDonneeFinanciere);
            depenseReconnue.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (depenseReconnue.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = depenseReconnue
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().update(simpleDonneeFinanciereSpecialisation);
                depenseReconnue.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return depenseReconnue;
    }

}
