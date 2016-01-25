/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.detailfamille;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.services.models.detailfamille.SimpleDetailFamilleService;
import ch.globaz.amal.businessimpl.checkers.detailfamille.SimpleDetailFamilleChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author CBU
 * 
 */
public class SimpleDetailFamilleServiceImpl implements SimpleDetailFamilleService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.detailfamille.SimpleDetailFamilleService#count(ch.globaz.amal.business
     * .models.detailfamille.SimpleDetailFamilleSearch)
     */
    @Override
    public int count(SimpleDetailFamilleSearch detailFamilleSearch) throws JadePersistenceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException {
        if (detailFamilleSearch == null) {
            throw new DetailFamilleException("Unable to search detailFamilleSearch, the search model passed is null!");
        }
        return JadePersistenceManager.count(detailFamilleSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleDetailFamilleService#create(ch.globaz.amal.business
     * .models.contribuable.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamille create(SimpleDetailFamille detailFamille) throws JadePersistenceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException {
        if (detailFamille == null) {
            throw new DetailFamilleException("Unable to create simpledetailfamille, the model passed is null!");
        }
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        // Elements non renseignés
        if (detailFamille.getAnnonceCaisseMaladie() == null) {
            detailFamille.setAnnonceCaisseMaladie(false);
        }
        if (detailFamille.getCodeActif() == null) {
            detailFamille.setCodeActif(true);
        }
        if (detailFamille.getCodeForcer() == null) {
            detailFamille.setCodeForcer(false);
        }
        if (detailFamille.getRefus() == null) {
            detailFamille.setRefus(false);
        }
        // Utilisateur
        detailFamille.setUser(currentSession.getUserName());
        // Appel du checker
        SimpleDetailFamilleChecker.checkForCreate(detailFamille);
        return (SimpleDetailFamille) JadePersistenceManager.add(detailFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleDetailFamilleService#delete(ch.globaz.amal.business
     * .models.contribuable.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamille delete(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, DocumentException, JadeApplicationServiceNotAvailableException {
        if (detailFamille == null) {
            throw new DetailFamilleException("Unable to delete simpledetailfamille, the model passed is null!");
        }
        if (detailFamille.getId() == null) {
            throw new DetailFamilleException("Unable to delete simpledetailfamille, the id passed is null!");
        }
        // retrieve info if needed
        if (detailFamille.getIdFamille() == null) {
            detailFamille = (SimpleDetailFamille) JadePersistenceManager.read(detailFamille);
        }
        SimpleDetailFamilleChecker.checkForDelete(detailFamille);
        return (SimpleDetailFamille) JadePersistenceManager.delete(detailFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.SimpleDetailFamilleService#read(java.lang.String)
     */
    @Override
    public SimpleDetailFamille read(String idDetailFamille) throws DetailFamilleException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDetailFamille)) {
            throw new DetailFamilleException("Unable to read simpledetailfamille, the id passed is null!");
        }
        SimpleDetailFamille detailFamille = new SimpleDetailFamille();
        detailFamille.setId(idDetailFamille);
        return (SimpleDetailFamille) JadePersistenceManager.read(detailFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleDetailFamilleService#search(ch.globaz.amal.business
     * .models.contribuable.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamilleSearch search(SimpleDetailFamilleSearch search) throws JadePersistenceException,
            DetailFamilleException {
        if (search == null) {
            throw new DetailFamilleException("Unable to search simpledetailfamille, the search model passed is null!");
        }
        return (SimpleDetailFamilleSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleDetailFamilleService#update(ch.globaz.amal.business
     * .models.contribuable.SimpleDetailFamille)
     */
    @Override
    public SimpleDetailFamille update(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (detailFamille == null) {
            throw new DetailFamilleException("Unable to update simpledetailfamille, the model passed is null!");
        }
        // Utilisateur
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        detailFamille.setUser(currentSession.getUserName());
        // Checker
        SimpleDetailFamilleChecker.checkForUpdate(detailFamille);
        // Si code annonce à false, date annonce à vide
        if (detailFamille.getAnnonceCaisseMaladie() == false) {
            detailFamille.setDateAnnonceCaisseMaladie("");
        }
        // Si code actif à false, annonce sedex
        if (!detailFamille.getCodeActif()) {
            // update sedex message en état initial
            SimpleAnnonceSedexSearch annonceSedexSearch = new SimpleAnnonceSedexSearch();
            annonceSedexSearch.setForIdContribuable(detailFamille.getIdContribuable());
            annonceSedexSearch.setForIdDetailFamille(detailFamille.getIdDetailFamille());
            ArrayList<String> statusInitial = new ArrayList<String>();
            statusInitial.add(IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
            statusInitial.add(IAMCodeSysteme.AMStatutAnnonceSedex.CREE.getValue());
            statusInitial.add(IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_CREE.getValue());
            statusInitial.add(IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_ENVOYE.getValue());
            annonceSedexSearch.setInStatus(statusInitial);
            try {
                annonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(annonceSedexSearch);
            } catch (AnnonceSedexException ase) {
                JadeThread.logInfo(null, "Erreur recherche annonce sedex Initiale ! ==>" + ase.toString());
            }

            if (annonceSedexSearch.getSize() > 0) {
                // On a trouvé des annonces status initiale et on essaie de désactiver le subside ==> on
                // efface
                for (JadeAbstractModel model : annonceSedexSearch.getSearchResults()) {
                    SimpleAnnonceSedex simpleAnnonceSedex = (SimpleAnnonceSedex) model;
                    try {
                        simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().delete(
                                simpleAnnonceSedex);
                    } catch (AnnonceSedexException e) {
                        JadeThread.logInfo(
                                null,
                                "Erreur lors de la suppression d'une annonce initiale provoquée par la désactivation du subside ! (Id annonce : "
                                        + simpleAnnonceSedex.getId() + ", IdSubside :"
                                        + detailFamille.getIdDetailFamille() + " --> " + e.toString());
                    }
                }
            }

            // Création d'une interruption
            try {
                AmalImplServiceLocator.getAnnoncesRPService().initAnnonceInterruption(
                        detailFamille.getIdContribuable(), detailFamille.getIdDetailFamille(),
                        detailFamille.getCodeActif());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return (SimpleDetailFamille) JadePersistenceManager.update(detailFamille);
    }

}
