package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.AnnonceLaprams;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsMediatorDonneeFinanciereSearch;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsSearch;
import ch.globaz.pegasus.business.models.annonce.RechercheHomeSashSearch;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLapramsSearch;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.AnnonceLapramsService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AnnonceLapramsServiceImpl implements AnnonceLapramsService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.AnnonceLapramsService#count(ch.globaz.pegasus
     * .business.models.annonce.AnnonceLapramsSearch)
     */
    @Override
    public int count(AnnonceLapramsSearch search) throws AnnonceException, JadePersistenceException {
        if (search == null) {
            throw new AnnonceException("Unable to count prestation, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public void deleteByIdsDecision(List<String> idsDecision) throws AnnonceException, JadePersistenceException,
            PrestationException, JadeApplicationServiceNotAvailableException {
        if (idsDecision == null) {
            throw new AnnonceException("Unable to search annonceLaprams, the search model passed is null!");
        }

        if (idsDecision.size() > 0) {
            AnnonceLapramsSearch search = new AnnonceLapramsSearch();
            search.setInIdsDecision(idsDecision);
            search = search(search);
            List<String> listIdAnnonceDfh = new ArrayList<String>();
            for (JadeAbstractModel model : search.getSearchResults()) {
                AnnonceLaprams annonceLaprams = (AnnonceLaprams) model;
                listIdAnnonceDfh.add(annonceLaprams.getSimpleAnnonceLaprams().getIdAnnonceLAPRAMS());

            }
            // On vérifie que l'on a bien des annonces car dans certain cas il n'y aura pas d'aonnce.
            if (listIdAnnonceDfh.size() > 0) {
                PegasusImplServiceLocator.getSimpleAnnonceLapramsDoFinHService().deleteByIdAnnonce(listIdAnnonceDfh);
                SimpleAnnonceLapramsSearch simpleAnnonceSearch = new SimpleAnnonceLapramsSearch();
                simpleAnnonceSearch.setInIdsDecision(idsDecision);
                JadePersistenceManager.delete(simpleAnnonceSearch);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.AnnonceLapramsService#read(java.lang.String)
     */
    @Override
    public AnnonceLaprams read(String idAnnonceLaprams) throws JadePersistenceException, AnnonceException {
        if (JadeStringUtil.isEmpty(idAnnonceLaprams)) {
            throw new AnnonceException("Unable to read annonce LAPRAMS, the id passed is null!");
        }
        AnnonceLaprams annonceLaprams = new AnnonceLaprams();
        annonceLaprams.setId(idAnnonceLaprams);
        return (AnnonceLaprams) JadePersistenceManager.read(annonceLaprams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.AnnonceLapramsService#search(ch.globaz.pegasus
     * .business.models.annonce.AnnonceLapramsSearch)
     */
    @Override
    public AnnonceLapramsSearch search(AnnonceLapramsSearch search) throws JadePersistenceException,
            PrestationException {
        if (search == null) {
            throw new PrestationException("Unable to search annonce LAPRAMS, the search model passed is null!");
        }
        return (AnnonceLapramsSearch) JadePersistenceManager.search(search);
    }

    @Override
    public AnnonceLapramsMediatorDonneeFinanciereSearch searchDonneesFinancieresDetail(
            AnnonceLapramsMediatorDonneeFinanciereSearch search) throws AnnonceException, JadePersistenceException {
        if (search == null) {
            throw new AnnonceException("Unable to search données annonce LAPRAMS, the search model passed is null!");
        }
        return (AnnonceLapramsMediatorDonneeFinanciereSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.AnnonceLapramsService#searchHomesSASH_SPAS(
     * ch.globaz.pegasus.business.models.annonce.RechercheHomeSashSearch)
     */
    @Override
    public RechercheHomeSashSearch searchHomesSASH_SPAS(RechercheHomeSashSearch searchModel)
            throws JadePersistenceException, AnnonceException {
        if (searchModel == null) {
            throw new AnnonceException("Unable to search annonceLaprams, the search model passed is null!");
        }
        return (RechercheHomeSashSearch) JadePersistenceManager.search(searchModel);
    }

}
