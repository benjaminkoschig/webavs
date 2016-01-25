package globaz.vulpecula.vb.registre;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.repositoriesjade.registre.converters.ParametreCotisationAssociationConverter;

public class PTParametresCotisationsAssociationsAjaxViewBean
        extends
        JadeAbstractAjaxCrudFindViewBean<ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSearchComplexModel> {
    private static final long serialVersionUID = 9175295613505932350L;

    private ParametreCotisationAssociationComplexModel currentEntity = new ParametreCotisationAssociationComplexModel();
    private transient ParametreCotisationAssociationSearchComplexModel searchModel = null;

    public PTParametresCotisationsAssociationsAjaxViewBean() {
        initList();
        currentEntity = new ParametreCotisationAssociationComplexModel();
    }

    @Override
    public void add() throws Exception {
        try {
            validate();
            super.add();
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }
    }

    private ParametreCotisationAssociation convertToDomain(ParametreCotisationAssociationComplexModel complexModel) {
        ParametreCotisationAssociationSimpleModel parametreCotisationAssociationSimpleModel = complexModel
                .getParametreCotisationAssociationSimpleModel();
        ParametreCotisationAssociation parametreCotisationAssociation = ParametreCotisationAssociationConverter
                .getInstance().convertToDomain(parametreCotisationAssociationSimpleModel);
        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = new CotisationAssociationProfessionnelle();
        cotisationAssociationProfessionnelle.setId(parametreCotisationAssociationSimpleModel
                .getIdCotisationAssociationProfessionnelle());
        parametreCotisationAssociation.setCotisationAssociationProfessionnelle(cotisationAssociationProfessionnelle);
        return parametreCotisationAssociation;
    }

    private void validate() throws UnsatisfiedSpecificationException {
        ParametreCotisationAssociation parametreCotisationAssociation = convertToDomain(currentEntity);
        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = parametreCotisationAssociation
                .getCotisationAssociationProfessionnelle();
        if (cotisationAssociationProfessionnelle.mustBeFetched()) {
            parametreCotisationAssociation.setCotisationAssociationProfessionnelle(VulpeculaRepositoryLocator
                    .getCotisationAssociationProfessionnelleRepository().findById(
                            cotisationAssociationProfessionnelle.getId()));
        }
        VulpeculaServiceLocator.getCotisationAssociationProfessionnelleService().validate(
                parametreCotisationAssociation);
    }

    @Override
    public void delete() throws Exception {
        super.delete();
    }

    @Override
    public void update() throws Exception {
        try {
            validate();
            super.update();
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }
    }

    @Override
    public void initList() {
        searchModel = new ParametreCotisationAssociationSearchComplexModel();
    }

    @Override
    public ParametreCotisationAssociationComplexModel getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public ParametreCotisationAssociationSearchComplexModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSearchComplexModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getParametreCotisationServiceCRUD();
    }

    @Override
    public void setCurrentEntity(ParametreCotisationAssociationComplexModel entite) {
        currentEntity = entite;
    }

    @Override
    public void setSearchModel(ParametreCotisationAssociationSearchComplexModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }
}
