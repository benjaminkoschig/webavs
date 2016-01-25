/**
 * 
 */
package ch.globaz.vulpecula.repositoriesjade.registre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.registre.ConventionRepository;
import ch.globaz.vulpecula.external.models.pyxis.CSTiers;
import ch.globaz.vulpecula.repositoriesjade.registre.converters.ConventionConverter;

/***
 * Implémentation Jade de {@link ConventionRepository}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public class ConventionRepositoryJade implements ConventionRepository {

    @Override
    public List<Convention> findAll() {
        List<Convention> conventions = new ArrayList<Convention>();

        AdministrationSearchComplexModel searchModel = new AdministrationSearchComplexModel();

        searchModel.setForGenreAdministration(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_CONVENTION);
        try {
            JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                AdministrationComplexModel administrationComplexModel = (AdministrationComplexModel) model;
                Convention convention = ConventionConverter.convertToDomain(administrationComplexModel);
                conventions.add(convention);
            }
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }

        return conventions;
    }

    @Override
    public Convention findById(final String id) {
        Convention convention = null;

        AdministrationSearchComplexModel searchModel = new AdministrationSearchComplexModel();
        searchModel.setForGenreAdministration(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_CONVENTION);
        searchModel.setForIdTiersAdministration(id);
        try {
            JadePersistenceManager.search(searchModel);
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                AdministrationComplexModel administrationComplexModel = (AdministrationComplexModel) model;
                convention = ConventionConverter.convertToDomain(administrationComplexModel);

            }
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }

        return convention;
    }

    @Override
    public Convention findByIdWithQualifications(final String idConvention) {
        Convention convention = findById(idConvention);
        if (convention == null) {
            return null;
        }

        convention.setQualifications(VulpeculaRepositoryLocator.getConventionQualificationRepository()
                .findByIdConvention(idConvention));

        return convention;
    }
}
