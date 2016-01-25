package ch.globaz.vulpecula.external.repositoriesjade.naos;

import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.naos.business.model.AffiliationTiersComplexModel;
import ch.globaz.pyxis.business.model.TiersSearchSimpleModel;
import ch.globaz.vulpecula.external.models.affiliation.Affilie;
import ch.globaz.vulpecula.external.repositories.affiliation.AffilieRepository;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.AffilieConverter;

/***
 * Implémentation Jade de {@link AffilieRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public class AffilieRepositoryJade implements AffilieRepository {

    @Override
    public Affilie findById(String id) {
        try {
            TiersSearchSimpleModel searchModel = new TiersSearchSimpleModel();
            searchModel.setForIdTiers(id);
            searchModel = (TiersSearchSimpleModel) JadePersistenceManager.search(searchModel);
            if (searchModel.getSearchResults().length > 0) {
                return AffilieConverter
                        .convertToDomain((AffiliationTiersComplexModel) searchModel.getSearchResults()[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
