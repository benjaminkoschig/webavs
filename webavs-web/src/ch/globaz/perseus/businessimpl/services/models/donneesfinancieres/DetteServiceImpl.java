/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation;
import ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class DetteServiceImpl extends PerseusAbstractServiceImpl implements DetteService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService#count(ch.globaz.perseus.business.
     * models.donneesfinancieres.DetteSearchModel)
     */
    @Override
    public int count(DetteSearchModel search) throws DonneesFinancieresException, JadePersistenceException {
        if (search == null) {
            throw new DonneesFinancieresException("Unable to count dettes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService#create(ch.globaz.perseus.business
     * .models.donneesfinancieres.Dette)
     */
    @Override
    public Dette create(Dette dette) throws JadePersistenceException, DonneesFinancieresException {
        if (dette == null) {
            throw new DonneesFinancieresException("Unable to create dette, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = dette.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere.setIdDemande(dette.getDemande().getId());
            simpleDonneeFinanciere.setIdMembreFamille(dette.getMembreFamille().getId());

            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().create(
                    simpleDonneeFinanciere);

            dette.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (dette.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = dette
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation.setIdDonneeFinanciere(simpleDonneeFinanciere.getId());

                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().create(simpleDonneeFinanciereSpecialisation);

                dette.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return dette;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService#delete(ch.globaz.perseus.business
     * .models.donneesfinancieres.Dette)
     */
    @Override
    public Dette delete(Dette dette) throws JadePersistenceException, DonneesFinancieresException {
        if (dette == null) {
            throw new DonneesFinancieresException("Unable to update dette, the given model is null!");
        }
        try {
            SimpleDonneeFinanciere simpleDonneeFinanciere = dette.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().delete(
                    simpleDonneeFinanciere);
            dette.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (dette.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = dette
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().delete(simpleDonneeFinanciereSpecialisation);
                dette.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return dette;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService#read(java.lang.String)
     */
    @Override
    public Dette read(String idDette) throws JadePersistenceException, DonneesFinancieresException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService#search(ch.globaz.perseus.business
     * .models.donneesfinancieres.DetteSearchModel)
     */
    @Override
    public DetteSearchModel search(DetteSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException {
        if (searchModel == null) {
            throw new DonneesFinancieresException("Unable to search dettes, the search model passed is null!");
        }
        return (DetteSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DetteService#update(ch.globaz.perseus.business
     * .models.donneesfinancieres.Dette)
     */
    @Override
    public Dette update(Dette dette) throws JadePersistenceException, DonneesFinancieresException {
        if (dette == null) {
            throw new DonneesFinancieresException("Unable to update dette, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = dette.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().update(
                    simpleDonneeFinanciere);
            dette.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (dette.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = dette
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().update(simpleDonneeFinanciereSpecialisation);
                dette.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return dette;
    }

}
