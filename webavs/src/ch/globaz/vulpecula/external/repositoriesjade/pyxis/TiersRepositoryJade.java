package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pyxis.business.model.TiersSearchSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.Tiers;
import ch.globaz.vulpecula.external.repositories.tiers.TiersRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.TiersConverter;

/***
 * Implémentation Jade de {@link TiersRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public class TiersRepositoryJade implements TiersRepository {

    @Override
    public Tiers findById(final String id) {
        try {
            TiersSearchSimpleModel searchModel = new TiersSearchSimpleModel();
            searchModel.setForIdTiers(id);
            searchModel = (TiersSearchSimpleModel) JadePersistenceManager.search(searchModel);
            if (searchModel.getSearchResults().length > 0) {
                TiersSimpleModel tiersSimpleModel = (TiersSimpleModel) searchModel.getSearchResults()[0];
                return TiersConverter.convertToDomain(tiersSimpleModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
