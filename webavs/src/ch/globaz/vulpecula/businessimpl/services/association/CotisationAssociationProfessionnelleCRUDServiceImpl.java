package ch.globaz.vulpecula.businessimpl.services.association;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSearchComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.association.CotisationAssociationProfessionnelleCRUDService;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.repositoriesjade.association.converter.CotisationAssociationProfessionnelleConverter;

public class CotisationAssociationProfessionnelleCRUDServiceImpl implements
        CotisationAssociationProfessionnelleCRUDService {

    @Override
    public CotisationAssociationProfessionnelleComplexModel create(
            CotisationAssociationProfessionnelleComplexModel complexModel) throws JadeApplicationException,
            JadePersistenceException {
        // Specifications
        // On recherche toutes les cotisations
        List<CotisationAssociationProfessionnelle> listAllCotisations = VulpeculaRepositoryLocator
                .getCotisationAssociationProfessionnelleRepository().findAll();

        CotisationAssociationProfessionnelle cotiSimpleModel = CotisationAssociationProfessionnelleConverter
                .getInstance().convertToDomain(complexModel.getCotisationAssociationProfessionnelleSimpleModel());

        Administration association = new Administration();
        association.setId(complexModel.getCotisationAssociationProfessionnelleSimpleModel()
                .getIdAssociationProfessionnelle());
        cotiSimpleModel.setAssociationProfessionnelle(association);

        try {
            cotiSimpleModel.validate(listAllCotisations);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }

        complexModel.getCotisationAssociationProfessionnelleSimpleModel().setLibelleUpper(
                JadeStringUtil.toUpperCase(complexModel.getCotisationAssociationProfessionnelleSimpleModel()
                        .getLibelle()));

        CotisationAssociationProfessionnelleSimpleModel entity = (CotisationAssociationProfessionnelleSimpleModel) JadePersistenceManager
                .add(complexModel.getCotisationAssociationProfessionnelleSimpleModel());
        complexModel.setCotisationAssociationProfessionnelleSimpleModel(entity);
        return complexModel;
    }

    @Override
    public CotisationAssociationProfessionnelleComplexModel delete(
            CotisationAssociationProfessionnelleComplexModel complexModel) throws JadeApplicationException,
            JadePersistenceException {
        CotisationAssociationProfessionnelleSimpleModel entity = (CotisationAssociationProfessionnelleSimpleModel) JadePersistenceManager
                .delete(complexModel.getCotisationAssociationProfessionnelleSimpleModel());
        complexModel.setCotisationAssociationProfessionnelleSimpleModel(entity);
        return complexModel;
    }

    @Override
    public CotisationAssociationProfessionnelleComplexModel read(String idEntity) throws JadeApplicationException,
            JadePersistenceException {
        CotisationAssociationProfessionnelleComplexModel model = new CotisationAssociationProfessionnelleComplexModel();
        model.setId(idEntity);
        return (CotisationAssociationProfessionnelleComplexModel) JadePersistenceManager.read(model);
    }

    @Override
    public CotisationAssociationProfessionnelleComplexModel update(
            CotisationAssociationProfessionnelleComplexModel complexModel) throws JadeApplicationException,
            JadePersistenceException {
        // Specifications
        // On recherche toutes les cotisations
        List<CotisationAssociationProfessionnelle> listAllCotisations = VulpeculaRepositoryLocator
                .getCotisationAssociationProfessionnelleRepository().findAll();

        CotisationAssociationProfessionnelle cotiSimpleModel = CotisationAssociationProfessionnelleConverter
                .getInstance().convertToDomain(complexModel.getCotisationAssociationProfessionnelleSimpleModel());

        Administration association = new Administration();
        association.setId(complexModel.getCotisationAssociationProfessionnelleSimpleModel()
                .getIdAssociationProfessionnelle());
        cotiSimpleModel.setAssociationProfessionnelle(association);

        try {
            cotiSimpleModel.validate(listAllCotisations);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }

        complexModel.getCotisationAssociationProfessionnelleSimpleModel().setLibelleUpper(
                JadeStringUtil.toUpperCase(complexModel.getCotisationAssociationProfessionnelleSimpleModel()
                        .getLibelle()));

        CotisationAssociationProfessionnelleSimpleModel entity = (CotisationAssociationProfessionnelleSimpleModel) JadePersistenceManager
                .update(complexModel.getCotisationAssociationProfessionnelleSimpleModel());
        complexModel.setCotisationAssociationProfessionnelleSimpleModel(entity);
        return complexModel;
    }

    @Override
    public int count(CotisationAssociationProfessionnelleSearchComplexModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public CotisationAssociationProfessionnelleSearchComplexModel search(
            CotisationAssociationProfessionnelleSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        searchModel.setForLibelleUpperLike(JadeStringUtil.toUpperCase(searchModel.getForLibelleLike()));
        searchModel.setForLibelleLike("");
        return (CotisationAssociationProfessionnelleSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

}
