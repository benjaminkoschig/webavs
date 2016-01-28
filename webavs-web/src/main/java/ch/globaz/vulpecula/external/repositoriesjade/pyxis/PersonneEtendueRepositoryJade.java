package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.vulpecula.external.models.pyxis.PersonneEtendue;
import ch.globaz.vulpecula.external.repositories.tiers.PersonneEtendueRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.PersonneEtendueConverter;

/***
 * Impl�mentation Jade de {@link PersonneEtendueRepository}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 20 d�c. 2013
 * 
 */
public class PersonneEtendueRepositoryJade implements PersonneEtendueRepository {

    @Override
    public PersonneEtendue findById(String id) {
        try {
            PersonneEtendueSearchComplexModel searchModel = new PersonneEtendueSearchComplexModel();
            searchModel.setForIdTiers(id);
            searchModel = (PersonneEtendueSearchComplexModel) JadePersistenceManager.search(searchModel);
            if (searchModel.getSearchResults().length > 0) {
                return PersonneEtendueConverter.convertToDomain((PersonneEtendueComplexModel) searchModel
                        .getSearchResults()[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
