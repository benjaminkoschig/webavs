package ch.globaz.eform.businessimpl.services;

import ch.globaz.eform.business.search.GFAdministrationSearch;
import ch.globaz.eform.business.services.GFAdministrationService;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class GFAdministrationServiceImpl implements GFAdministrationService {
    @Override
    public GFAdministrationSearch find(GFAdministrationSearch search) {
        AdministrationSearchComplexModel searchComplexModel = new AdministrationSearchComplexModel();

        if (!StringUtils.isBlank(search.getForGenreAdministration())) {
            searchComplexModel.setForGenreAdministration(search.getForGenreAdministration());
        }
        if (!StringUtils.isBlank(search.getForCodeAdministrationLike())) {
            searchComplexModel.setForCodeAdministrationLike(search.getForCodeAdministrationLike());
        }
        if (!StringUtils.isBlank(search.getForSedexIdLike())) {
            searchComplexModel.setForDesignation1Like(search.getForSedexIdLike());
        }

        try {
            searchComplexModel = TIBusinessServiceLocator.getAdministrationService().find(searchComplexModel);

            if (searchComplexModel.getSize() > 0) {
                AdministrationComplexModel[] models;
                if (Boolean.parseBoolean(search.getNotNull())) {
                    models = Arrays.stream(searchComplexModel.getSearchResults())
                            .map(o -> (AdministrationComplexModel) o)
                            .filter(model -> !StringUtils.isBlank(model.getAdmin().getSedexId()))
                            .toArray(AdministrationComplexModel[]::new);
                } else {
                    models = (AdministrationComplexModel[]) searchComplexModel.getSearchResults();
                }

                search.setSearchResults(models);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return search;
    }
}
