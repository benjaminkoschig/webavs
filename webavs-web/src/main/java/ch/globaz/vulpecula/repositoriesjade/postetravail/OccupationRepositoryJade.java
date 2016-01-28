/**
 * 
 */
package ch.globaz.vulpecula.repositoriesjade.postetravail;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.postetravail.TauxOccupationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.TauxOccupationSimpleModel;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.postetravail.OccupationRepository;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.OccupationConverter;

/**
 * Implémentation Jade de {@link OccupationRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 24 déc. 2013
 * 
 */
public class OccupationRepositoryJade implements OccupationRepository {

    @Override
    public List<Occupation> findOccupationsByIdPosteTravail(String idPosteTravail) {
        List<Occupation> listeOccupations = new ArrayList<Occupation>();
        try {
            TauxOccupationSearchSimpleModel searchModel = new TauxOccupationSearchSimpleModel();
            searchModel.setForIdPosteTravail(idPosteTravail);
            searchModel = (TauxOccupationSearchSimpleModel) JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
                TauxOccupationSimpleModel tauxOccupationSimpleModel = (TauxOccupationSimpleModel) abstractModel;
                Occupation occupation = OccupationConverter.convertToDomain(tauxOccupationSimpleModel);
                listeOccupations.add(occupation);
            }
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }
        return listeOccupations;
    }

    @Override
    public List<Occupation> findOccupationsByPosteTravail(PosteTravail posteTravail) {
        return findOccupationsByIdPosteTravail(posteTravail.getId());
    }

}
