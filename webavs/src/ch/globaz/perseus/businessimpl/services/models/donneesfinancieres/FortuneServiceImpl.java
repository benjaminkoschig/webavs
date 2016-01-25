/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation;
import ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class FortuneServiceImpl extends PerseusAbstractServiceImpl implements FortuneService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService#count(ch.globaz.perseus.business.
     * models.donneesfinancieres.FortuneSearchModel)
     */
    @Override
    public int count(FortuneSearchModel search) throws DonneesFinancieresException, JadePersistenceException {
        if (search == null) {
            throw new DonneesFinancieresException("Unable to count fortunes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService#create(ch.globaz.perseus.business
     * .models.donneesfinancieres.Fortune)
     */
    @Override
    public Fortune create(Fortune fortune) throws JadePersistenceException, DonneesFinancieresException {
        if (fortune == null) {
            throw new DonneesFinancieresException("Unable to create fortune, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = fortune.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere.setIdDemande(fortune.getDemande().getId());
            simpleDonneeFinanciere.setIdMembreFamille(fortune.getMembreFamille().getId());

            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().create(
                    simpleDonneeFinanciere);

            fortune.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (fortune.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = fortune
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation.setIdDonneeFinanciere(simpleDonneeFinanciere.getId());

                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().create(simpleDonneeFinanciereSpecialisation);

                fortune.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return fortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService#delete(ch.globaz.perseus.business
     * .models.donneesfinancieres.Fortune)
     */
    @Override
    public Fortune delete(Fortune fortune) throws JadePersistenceException, DonneesFinancieresException {
        if (fortune == null) {
            throw new DonneesFinancieresException("Unable to update fortune, the given model is null!");
        }
        try {
            SimpleDonneeFinanciere simpleDonneeFinanciere = fortune.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().delete(
                    simpleDonneeFinanciere);
            fortune.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (fortune.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = fortune
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().delete(simpleDonneeFinanciereSpecialisation);
                fortune.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return fortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService#read(java.lang.String)
     */
    @Override
    public Fortune read(String idFortune) throws JadePersistenceException, DonneesFinancieresException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService#search(ch.globaz.perseus.business
     * .models.donneesfinancieres.FortuneSearchModel)
     */
    @Override
    public FortuneSearchModel search(FortuneSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException {
        if (searchModel == null) {
            throw new DonneesFinancieresException("Unable to search fortunes, the search model passed is null!");
        }
        return (FortuneSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.FortuneService#update(ch.globaz.perseus.business
     * .models.donneesfinancieres.Fortune)
     */
    @Override
    public Fortune update(Fortune fortune) throws JadePersistenceException, DonneesFinancieresException {
        if (fortune == null) {
            throw new DonneesFinancieresException("Unable to update fortune, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = fortune.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().update(
                    simpleDonneeFinanciere);
            fortune.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (fortune.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = fortune
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().update(simpleDonneeFinanciereSpecialisation);
                fortune.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return fortune;
    }

}
