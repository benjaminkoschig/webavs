package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalite;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalites;
import ch.globaz.vulpecula.external.models.DetailGroupeLocaliteComplexModel;
import ch.globaz.vulpecula.external.models.DetailGroupeLocaliteSearchComplexModel;
import ch.globaz.vulpecula.external.models.DetailGroupeLocaliteSimpleModel;
import ch.globaz.vulpecula.external.repositories.tiers.DetailGroupeLocaliteRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.DetailGroupeLocaliteConverter;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class DetailGroupeLocaliteRepositoryJade extends
        RepositoryJade<DetailGroupeLocalite, DetailGroupeLocaliteComplexModel, DetailGroupeLocaliteSimpleModel>
        implements DetailGroupeLocaliteRepository {

    @Override
    public DomaineConverterJade<DetailGroupeLocalite, DetailGroupeLocaliteComplexModel, DetailGroupeLocaliteSimpleModel> getConverter() {
        return new DetailGroupeLocaliteConverter();
    }

    /**
     * @return un objet DetailGroupeLocalites auquel on donne en param�tre les r�sultats de la requ�te sous forme de
     *         Collection.
     *         Cet objet va se charger de s�parer R�gion et District par le biais des m�thodes getRegion et getDistrict
     */
    @Override
    public DetailGroupeLocalites findByIdLocalite(String id) {
        DetailGroupeLocaliteSearchComplexModel searchComplexModel = new DetailGroupeLocaliteSearchComplexModel();
        searchComplexModel.setForIdLocalite(id);
        return new DetailGroupeLocalites(searchAndFetch(searchComplexModel));
    }
}