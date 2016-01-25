/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.donneesfinancieres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciereType;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation;
import ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class RevenuServiceImpl extends PerseusAbstractServiceImpl implements RevenuService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#count(ch.globaz.perseus.business.
     * models.donneesfinancieres.RevenuSearchModel)
     */
    @Override
    public int count(RevenuSearchModel search) throws DonneesFinancieresException, JadePersistenceException {
        if (search == null) {
            throw new DonneesFinancieresException("Unable to count revenus, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#create(ch.globaz.perseus.business
     * .models.donneesfinancieres.Revenu)
     */
    @Override
    public Revenu create(Revenu revenu) throws JadePersistenceException, DonneesFinancieresException {
        if (revenu == null) {
            throw new DonneesFinancieresException("Unable to create revenu, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = revenu.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere.setIdDemande(revenu.getDemande().getId());
            simpleDonneeFinanciere.setIdMembreFamille(revenu.getMembreFamille().getId());

            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().create(
                    simpleDonneeFinanciere);

            revenu.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (revenu.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = revenu
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation.setIdDonneeFinanciere(simpleDonneeFinanciere.getId());

                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().create(simpleDonneeFinanciereSpecialisation);

                revenu.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return revenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#delete(ch.globaz.perseus.business
     * .models.donneesfinancieres.Revenu)
     */
    @Override
    public Revenu delete(Revenu revenu) throws JadePersistenceException, DonneesFinancieresException {
        if (revenu == null) {
            throw new DonneesFinancieresException("Unable to update revenu, the given model is null!");
        }
        try {
            SimpleDonneeFinanciere simpleDonneeFinanciere = revenu.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().delete(
                    simpleDonneeFinanciere);
            revenu.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (revenu.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = revenu
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().delete(simpleDonneeFinanciereSpecialisation);
                revenu.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return revenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#read(java.lang.String)
     */
    @Override
    public Revenu read(String idRevenu) throws JadePersistenceException, DonneesFinancieresException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#search(ch.globaz.perseus.business
     * .models.donneesfinancieres.RevenuType, java.lang.String, java.lang.String)
     */
    public DonneeFinanciere search(DonneeFinanciereType type, String idMembreFamille, String idDemande)
            throws JadePersistenceException, DonneesFinancieresException {
        if (type == null) {
            throw new DonneesFinancieresException("Unable to search revenus, the type passed is null!");
        }
        if (JadeStringUtil.isEmpty(idMembreFamille)) {
            throw new DonneesFinancieresException("Unable to search revenus, the idMembreFamille passed is empty!");
        }
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new DonneesFinancieresException("Unable to search revenus, the idDemande passed is empty!");
        }
        RevenuSearchModel searchModel = new RevenuSearchModel();
        searchModel.setForIdDemande(idDemande);
        searchModel.setForIdMembreFamille(idMembreFamille);
        List<String> listType = new ArrayList<String>();
        listType.add(type.toString());
        searchModel.setInType(listType);

        if (searchModel.getSearchResults().length > 0) {
            return (DonneeFinanciere) searchModel.getSearchResults()[0];
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#search(ch.globaz.perseus.business
     * .models.donneesfinancieres.RevenuSearchModel)
     */
    @Override
    public RevenuSearchModel search(RevenuSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException {
        if (searchModel == null) {
            throw new DonneesFinancieresException("Unable to search revenus, the search model passed is null!");
        }
        return (RevenuSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#update(ch.globaz.perseus.business
     * .models.donneesfinancieres.Revenu)
     */
    @Override
    public Revenu update(Revenu revenu) throws JadePersistenceException, DonneesFinancieresException {
        if (revenu == null) {
            throw new DonneesFinancieresException("Unable to update revenu, the given model is null!");
        }
        try {

            SimpleDonneeFinanciere simpleDonneeFinanciere = revenu.getSimpleDonneeFinanciere();
            simpleDonneeFinanciere = PerseusImplServiceLocator.getSimpleDonneeFinanciereService().update(
                    simpleDonneeFinanciere);
            revenu.setSimpleDonneeFinanciere(simpleDonneeFinanciere);

            // Si le type de données financières utilise des données spécifiques le persister en mémoire
            if (revenu.getTypeAsEnum().useSpecialisation()) {
                SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = revenu
                        .getSimpleDonneeFinanciereSpecialisation();
                simpleDonneeFinanciereSpecialisation = PerseusImplServiceLocator
                        .getSimpleDonneeFinanciereSpecialisationService().update(simpleDonneeFinanciereSpecialisation);
                revenu.setSimpleDonneeFinanciereSpecialisation(simpleDonneeFinanciereSpecialisation);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available - " + e.getMessage());
        }

        return revenu;
    }

}
