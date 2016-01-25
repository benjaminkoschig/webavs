/**
 * 
 */
package ch.globaz.vulpecula.repositoriesjade.is;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.models.is.HistoriqueProcessusAfSearchSimpleModel;
import ch.globaz.vulpecula.business.models.is.HistoriqueProcessusAfSimpleModel;
import ch.globaz.vulpecula.domain.models.is.HistoriqueProcessusAf;
import ch.globaz.vulpecula.domain.repositories.is.HistoriqueProcessusAfRepository;
import ch.globaz.vulpecula.external.repositories.tiers.PaysRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.is.converters.HistoriqueProcessusAfConverter;

/**
 * Implémentation Jade de {@link PaysRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public class HistoriqueProcessusAfRepositoryJade extends
        RepositoryJade<HistoriqueProcessusAf, JadeComplexModel, HistoriqueProcessusAfSimpleModel> implements
        HistoriqueProcessusAfRepository {

    @Override
    public HistoriqueProcessusAf findByIdProcessus(String idProcessus) {
        HistoriqueProcessusAfSearchSimpleModel searchModel = new HistoriqueProcessusAfSearchSimpleModel();
        searchModel.setForIdProcessus(idProcessus);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public DomaineConverterJade<HistoriqueProcessusAf, JadeComplexModel, HistoriqueProcessusAfSimpleModel> getConverter() {
        return HistoriqueProcessusAfConverter.getInstance();
    }

    @Override
    public Collection<String> findIdProcessusByType(String forBusinessProcessus) {
        Collection<String> ids = new ArrayList<String>();
        HistoriqueProcessusAfSearchSimpleModel searchModel = new HistoriqueProcessusAfSearchSimpleModel();
        List<HistoriqueProcessusAfSimpleModel> historiques = RepositoryJade.searchForAndFetch(searchModel);
        for (HistoriqueProcessusAfSimpleModel historique : historiques) {
            ids.add(historique.getIdProcessus());
        }
        return ids;
    }

}
