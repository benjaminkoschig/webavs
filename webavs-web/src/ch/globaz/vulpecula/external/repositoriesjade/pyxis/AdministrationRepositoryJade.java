/**
 * 
 */
package ch.globaz.vulpecula.external.repositoriesjade.pyxis;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.external.models.AdministrationComplexModel;
import ch.globaz.vulpecula.external.models.AdministrationSearchComplexModel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.models.pyxis.CSTiers;
import ch.globaz.vulpecula.external.repositories.tiers.AdministrationRepository;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;

/**
 * Implémentation Jade de {@link AdministrationRepository}
 * 
 * @author sel
 * 
 */
public class AdministrationRepositoryJade implements AdministrationRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(AdministrationRepositoryJade.class);

    private PropertiesService propertiesService = VulpeculaServiceLocator.getPropertiesService();

    @Override
    public List<Administration> findAllAssociationsProfessionnelles() {
        return findAll(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_ASSOCIATIONPROFESSIONNELLE);
    }

    @Override
    public Administration findById(String id) {
        Administration administration = null;
        AdministrationSearchComplexModel adminComplexModel = new AdministrationSearchComplexModel();
        adminComplexModel.setForIdTiersAdministration(id);
        try {
            JadePersistenceManager.search(adminComplexModel);
            if (adminComplexModel.getSize() > 0) {
                AdministrationComplexModel administrationComplexModel = (AdministrationComplexModel) adminComplexModel
                        .getSearchResults()[0];
                administration = AdministrationConverter.convertToDomain(administrationComplexModel);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return administration;
    }

    @Override
    public List<Administration> findAllCaissesMaladies() {
        return findAll(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_ASSUREURMALADIE);
    }

    @Override
    public List<Administration> findAllSyndicats() {
        return findAll(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_SYNDICATS);
    }

    @Override
    public List<Administration> findAllCaissesMetiers() {
        List<String> caissesMetiers = propertiesService.getCaissesMetiers();
        return findAll(caissesMetiers);
    }

    @Override
    public List<Administration> findAllConventions() {
        return findAll(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_CONVENTION);
    }

    @Override
    public List<Administration> findAllCaissesAF() {
        List<String> caissesAF = propertiesService.getCaissesAF();
        return findAll(caissesAF);
    }

    private List<Administration> findAll(List<String> ids) {
        List<Administration> administrations = new ArrayList<Administration>();
        AdministrationSearchComplexModel adminComplexModel = new AdministrationSearchComplexModel();
        adminComplexModel.setForIdTiersAdministrationIn(ids);
        adminComplexModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            JadePersistenceManager.search(adminComplexModel);
            for (int i = 0; i < adminComplexModel.getSize(); i++) {
                AdministrationComplexModel administrationComplexModel = (AdministrationComplexModel) adminComplexModel
                        .getSearchResults()[i];
                Administration administration = AdministrationConverter.convertToDomain(administrationComplexModel);
                administrations.add(administration);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return administrations;
    }

    private List<Administration> findAll(String genreAdministration) {
        List<Administration> administrations = new ArrayList<Administration>();
        AdministrationSearchComplexModel adminComplexModel = new AdministrationSearchComplexModel();
        adminComplexModel.setForGenreAdministration(genreAdministration);
        adminComplexModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            JadePersistenceManager.search(adminComplexModel);
            for (int i = 0; i < adminComplexModel.getSize(); i++) {
                AdministrationComplexModel administrationComplexModel = (AdministrationComplexModel) adminComplexModel
                        .getSearchResults()[i];
                Administration administration = AdministrationConverter.convertToDomain(administrationComplexModel);
                administrations.add(administration);
            }
        } catch (JadePersistenceException ex) {
            LOGGER.error(ex.getMessage());
        }
        return administrations;
    }
}
