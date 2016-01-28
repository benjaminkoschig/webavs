/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.donneesfinancieres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.DonneeFinanciereType;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSearchModel;
import ch.globaz.perseus.business.services.models.donneesfinancieres.DonneeFinanciereService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class DonneeFinanciereServiceImpl extends PerseusAbstractServiceImpl implements DonneeFinanciereService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.DonneeFinanciereService#deleteForDemande(java.lang
     * .String)
     */
    @Override
    public int deleteForDemande(String idDemande) throws JadePersistenceException, DonneesFinancieresException {
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new DonneesFinancieresException(
                    "Unable to delete DonneFinanciere for demande, the id passed is null!");
        }

        try {
            // Suppressiond des données financières spécialisées
            // SimpleDonneeFinanciereSpecialisationSearchModel searchModel2 = new
            // SimpleDonneeFinanciereSpecialisationSearchModel();
            // searchModel2.setForIdDemande(idDemande);
            // PerseusImplServiceLocator.getSimpleDonneeFinanciereSpecialisationService().delete(searchModel2);

            SimpleDonneeFinanciereSearchModel searchModel = new SimpleDonneeFinanciereSearchModel();
            searchModel.setForIdDemande(idDemande);
            return PerseusImplServiceLocator.getSimpleDonneeFinanciereService().delete(searchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available : " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.donneesfinancieres.DonneeFinanciereService#
     * deleteForDemandeAndMembreFamille(java.lang.String, java.lang.String)
     */
    @Override
    public int deleteForDemandeAndMembreFamille(String idDemande, String idMembreFamille)
            throws JadePersistenceException, DonneesFinancieresException {
        if (JadeStringUtil.isEmpty(idDemande) || JadeStringUtil.isEmpty(idMembreFamille)) {
            throw new DonneesFinancieresException(
                    "Unable to delete DonneFinanciere for demande and membreFamille, an id passed is null!");
        }

        try {
            // Suppressiond des données financières spécialisées
            // SimpleDonneeFinanciereSpecialisationSearchModel searchModel2 = new
            // SimpleDonneeFinanciereSpecialisationSearchModel();
            // searchModel2.setForIdDemande(idDemande);
            // searchModel2.setForIdMembreFamille(idMembreFamille);
            // PerseusImplServiceLocator.getSimpleDonneeFinanciereSpecialisationService().delete(searchModel2);

            SimpleDonneeFinanciereSearchModel searchModel = new SimpleDonneeFinanciereSearchModel();
            // searchModel.setWhereKey("withMembreFamille");
            searchModel.setForIdDemande(idDemande);
            searchModel.setForIdMembreFamille(idMembreFamille);

            return PerseusImplServiceLocator.getSimpleDonneeFinanciereService().delete(searchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneesFinancieresException("Service not available : " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.RevenuService#search(ch.globaz.perseus.business
     * .models.donneesfinancieres.RevenuType, java.lang.String, java.lang.String)
     */
    @Override
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

}
