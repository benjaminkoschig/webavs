package ch.globaz.pegasus.businessimpl.services.models.blocage;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.PcaBloqueConjoint;
import ch.globaz.pegasus.business.models.blocage.PcaBloqueConjointSearch;
import ch.globaz.pegasus.business.models.blocage.PcaBloqueSearch;
import ch.globaz.pegasus.business.services.models.blocage.PcaBloqueService;

public class PcaBloqueServiceImpl implements PcaBloqueService {

    @Override
    public PcaBloque readPcaBloque(String idPca) throws BlocageException, JadePersistenceException {
        PcaBloqueSearch search = new PcaBloqueSearch();
        search.setForIdPca(idPca);
        search = searchPcaBloque(search);
        PcaBloque pcaBloque = null;
        if (search.getSize() == 1) {
            pcaBloque = (PcaBloque) search.getSearchResults()[0];
        } else if (search.getSize() > 1) {
            throw new BlocageException("Too many pcabloque was founded with this id pca:" + idPca);
        } else {
            throw new BlocageException("Any one pcabloque was founded with this id pca:" + idPca);
        }
        return pcaBloque;
    }

    @Override
    public PcaBloqueSearch searchPcaBloque(PcaBloqueSearch search) throws BlocageException, JadePersistenceException {
        if (search == null) {
            throw new BlocageException("Unable to searchPcaBloque search, the model passed is null!");
        }

        PcaBloqueConjointSearch searchConjoint = new PcaBloqueConjointSearch();
        searchConjoint.setForIdPca(search.getForIdPca());
        searchConjoint.setForIdDemande(search.getForIdPca());
        searchConjoint = (PcaBloqueConjointSearch) JadePersistenceManager.search(searchConjoint);
        Map<String, PcaBloqueConjoint> mapPcaConjoints = new HashMap<String, PcaBloqueConjoint>();
        for (JadeAbstractModel model : searchConjoint.getSearchResults()) {
            PcaBloqueConjoint pca = (PcaBloqueConjoint) model;
            mapPcaConjoints.put(pca.getIdPca(), pca);
        }
        search = (PcaBloqueSearch) JadePersistenceManager.search(search);
        for (JadeAbstractModel model : search.getSearchResults()) {
            PcaBloque pca = (PcaBloque) model;
            if (mapPcaConjoints.containsKey(pca.getIdPca())) {
                PcaBloqueConjoint pcaConjoint = mapPcaConjoints.get(pca.getIdPca());

                // pca.setMontantBloque(new BigDecimal(pca.getMontantBloque()).add(
                // new BigDecimal(pcaConjoint.getMontantBloque())).toString());

                String montantDe = JadeStringUtil.isBlankOrZero(pca.getMontantDebloque()) ? "0" : pcaConjoint
                        .getMontantDebloque();

                String montantDeConjoint = JadeStringUtil.isBlankOrZero(pcaConjoint.getMontantDebloque()) ? "0"
                        : pcaConjoint.getMontantDebloque();

                pca.setMontantDebloque(new BigDecimal(montantDe).add(new BigDecimal(montantDeConjoint)).toString());

            }
        }

        return search;

    }
}
