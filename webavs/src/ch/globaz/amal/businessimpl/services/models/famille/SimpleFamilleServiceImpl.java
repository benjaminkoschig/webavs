package ch.globaz.amal.businessimpl.services.models.famille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.services.models.famille.SimpleFamilleService;
import ch.globaz.amal.businessimpl.checkers.famille.SimpleFamilleChecker;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class SimpleFamilleServiceImpl implements SimpleFamilleService {

    @Override
    public int count(SimpleFamilleSearch familleSearch) throws JadePersistenceException, FamilleException {
        if (familleSearch == null) {
            throw new FamilleException("Unable to search familleSearch, the search model passed is null!");
        }
        return JadePersistenceManager.count(familleSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleFamilleService#create(ch.globaz.amal.business.models
     * .contribuable.SimpleFamille)
     */
    @Override
    public SimpleFamille create(SimpleFamille simpleFamille) throws JadePersistenceException, FamilleException {
        if (simpleFamille == null) {
            throw new FamilleException("Unable to create Famille, the model passed is null!");
        }
        SimpleFamilleChecker.checkForCreate(simpleFamille);
        return (SimpleFamille) JadePersistenceManager.add(simpleFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleFamilleService#delete(ch.globaz.amal.business.models
     * .contribuable.SimpleFamille)
     */
    @Override
    public SimpleFamille delete(SimpleFamille simpleFamille) throws FamilleException, JadePersistenceException {
        if (simpleFamille == null) {
            throw new FamilleException("Unable to delete simpleFamille, the model passed is null!");
        }
        SimpleFamilleChecker.checkForDelete(simpleFamille);
        return (SimpleFamille) JadePersistenceManager.delete(simpleFamille);
    }

    @Override
    public SimpleFamilleSearch getChefDeFamille(String idContribuable) throws FamilleException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idContribuable)) {
            throw new FamilleException("Unable to read famille, the id passed is null!");
        }

        SimpleFamilleSearch familleSearch = new SimpleFamilleSearch();
        familleSearch.setForIdContribuable(idContribuable);
        familleSearch.setIsContribuable(true);

        return (SimpleFamilleSearch) JadePersistenceManager.search(familleSearch);

    }

    @Override
    public Collection<SimpleFamille> getFamilleListEnfants(String idContribuable) throws FamilleException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idContribuable)) {
            throw new FamilleException("Unable to getFamilleMembers, the id passed is null!");
        }
        SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
        simpleFamilleSearch.setForIdContribuable(idContribuable);
        simpleFamilleSearch = (SimpleFamilleSearch) JadePersistenceManager.search(simpleFamilleSearch);

        Collection<SimpleFamille> collection = new ArrayList<SimpleFamille>();
        for (Iterator it = Arrays.asList(simpleFamilleSearch.getSearchResults()).iterator(); it.hasNext();) {
            SimpleFamille simpleFamille = (SimpleFamille) it.next();
            collection.add(simpleFamille);
        }
        return collection;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.SimpleFamilleService#read(java.lang.String)
     */
    @Override
    public SimpleFamille read(String idFamille) throws FamilleException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idFamille)) {
            throw new FamilleException("Unable to read simplefamille, the id passed is null!");
        }
        SimpleFamille famille = new SimpleFamille();
        famille.setId(idFamille);
        famille = (SimpleFamille) JadePersistenceManager.read(famille);
        return synchronizeWithTiers(famille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.famille.SimpleFamilleService#search(ch.globaz.amal.business.models.famille
     * .SimpleFamilleSearch)
     */
    @Override
    public SimpleFamilleSearch search(SimpleFamilleSearch simpleFamilleSearch) throws FamilleException,
            JadePersistenceException {
        if (simpleFamilleSearch == null) {
            throw new FamilleException("Unable to search simpleFamille, the model passed is null!");
        }

        return (SimpleFamilleSearch) JadePersistenceManager.search(simpleFamilleSearch);
    }

    private SimpleFamille synchronizeWithTiers(SimpleFamille baseFamille) {
        SimpleFamille updatedFamille = null;
        try {
            PersonneEtendueComplexModel famillePersonne = baseFamille.getPersonneEtendue();
            if (JadeStringUtil.isEmpty(famillePersonne.getTiers().getDesignation1())
                    || JadeStringUtil.isEmpty(famillePersonne.getTiers().getDesignation2())) {
                // Contrôle que nous pouvons synchroniser.
                // Si impossible, retourne la famille telle quelle
                return baseFamille;
            } else {
                // On écrase les valeurs de simplefamille avec les valeurs de tiers
                SimpleFamille baseFamilleToUpdate = baseFamille;
                boolean bNeedUpdate = false;
                if (!baseFamilleToUpdate.getDateNaissance().equals(famillePersonne.getPersonne().getDateNaissance())) {
                    if (!JadeStringUtil.isEmpty(famillePersonne.getPersonne().getDateNaissance())) {
                        baseFamilleToUpdate.setDateNaissance(famillePersonne.getPersonne().getDateNaissance());
                        bNeedUpdate = true;
                    }
                }
                String nomPrenomTiers = famillePersonne.getTiers().getDesignation1() + " "
                        + famillePersonne.getTiers().getDesignation2();
                if (!baseFamilleToUpdate.getNomPrenom().equals(nomPrenomTiers)) {
                    baseFamilleToUpdate.setNomPrenom(nomPrenomTiers);
                    bNeedUpdate = true;
                }
                String nomPrenomTiersUpper = famillePersonne.getTiers().getDesignationUpper1() + " "
                        + famillePersonne.getTiers().getDesignationUpper2();
                if (!baseFamilleToUpdate.getNomPrenomUpper().equals(nomPrenomTiersUpper)) {
                    baseFamilleToUpdate.setNomPrenomUpper(nomPrenomTiersUpper);
                    bNeedUpdate = true;
                }
                if (!baseFamilleToUpdate.getSexe().equals(famillePersonne.getPersonne().getSexe())) {
                    if (!JadeStringUtil.isEmpty(famillePersonne.getPersonne().getSexe())) {
                        baseFamilleToUpdate.setSexe(famillePersonne.getPersonne().getSexe());
                        bNeedUpdate = true;
                    }
                }
                if (bNeedUpdate) {
                    updatedFamille = update(baseFamilleToUpdate);
                } else {
                    updatedFamille = baseFamille;
                }
            }
        } catch (Exception currentException) {
            currentException.printStackTrace();
            return baseFamille;
        }
        return updatedFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleFamilleService#update(ch.globaz.amal.business.models
     * .contribuable.SimpleFamille)
     */
    @Override
    public SimpleFamille update(SimpleFamille famille) throws FamilleException, JadePersistenceException {
        if (famille == null) {
            throw new FamilleException("Unable to update famille, the model passed is null!");
        }
        if (famille.isNew()) {
            throw new FamilleException("Unable to update famille, the model passed is new!");
        }
        SimpleFamilleChecker.checkForUpdate(famille);
        return (SimpleFamille) JadePersistenceManager.update(famille);

        // TODO : CHECK SI CHANGEMENT DE FIN DE DROIT ET APPLICATION AU SUBSIDE EN RELATION AVEC ANNONCE à 0
    }
}
