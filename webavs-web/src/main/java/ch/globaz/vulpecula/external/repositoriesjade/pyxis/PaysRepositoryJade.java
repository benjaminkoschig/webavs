/**
 * 
 */
package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.vulpecula.external.models.pyxis.Pays;
import ch.globaz.vulpecula.external.repositories.tiers.PaysRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.PaysConverter;

/**
 * Implémentation Jade de {@link PaysRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 23 déc. 2013
 * 
 */
public class PaysRepositoryJade implements PaysRepository {

    @Override
    public Pays findById(String id) {
        try {
            PaysSearchSimpleModel searchModel = new PaysSearchSimpleModel();
            searchModel.setForIdPays(id);
            searchModel = (PaysSearchSimpleModel) JadePersistenceManager.search(searchModel);
            if (searchModel.getSearchResults().length > 0) {
                return PaysConverter.convertToDomain((PaysSimpleModel) searchModel.getSearchResults()[0]);
            }
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }
        return null;
    }

}
