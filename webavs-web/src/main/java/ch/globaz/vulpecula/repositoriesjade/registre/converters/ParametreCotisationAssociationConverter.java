package ch.globaz.vulpecula.repositoriesjade.registre.converters;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSimpleModel;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.models.registre.TypeParamCotisationAP;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.association.converter.CotisationAssociationProfessionnelleConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class ParametreCotisationAssociationConverter
        implements
        DomaineConverterJade<ParametreCotisationAssociation, ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSimpleModel> {

    private static final ParametreCotisationAssociationConverter INSTANCE = new ParametreCotisationAssociationConverter();

    public static ParametreCotisationAssociationConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public ParametreCotisationAssociationSimpleModel convertToPersistence(
            ParametreCotisationAssociation parametreCotisationAssociation) {
        ParametreCotisationAssociationSimpleModel cotisationCaisseMetierSimpleModel = new ParametreCotisationAssociationSimpleModel();
        cotisationCaisseMetierSimpleModel.setId(parametreCotisationAssociation.getId());
        cotisationCaisseMetierSimpleModel.setIdCotisationAssociationProfessionnelle(parametreCotisationAssociation
                .getIdCotisationAssociationProfessionnelle());
        cotisationCaisseMetierSimpleModel.setTypeParam(parametreCotisationAssociation.getTypeParam().getValue());
        cotisationCaisseMetierSimpleModel.setTaux(parametreCotisationAssociation.getTaux().getValue());
        cotisationCaisseMetierSimpleModel.setMontant(parametreCotisationAssociation.getMontant().getValue());
        cotisationCaisseMetierSimpleModel.setFourchetteDebut(parametreCotisationAssociation.getFourchetteDebut()
                .getValueNormalisee());
        cotisationCaisseMetierSimpleModel.setFourchetteFin(parametreCotisationAssociation.getFourchetteFin()
                .getValueNormalisee());
        cotisationCaisseMetierSimpleModel.setSpy(parametreCotisationAssociation.getSpy());
        return cotisationCaisseMetierSimpleModel;
    }

    @Override
    public ParametreCotisationAssociation convertToDomain(
            ParametreCotisationAssociationSimpleModel parametreCotisationAssociationSimpleModel) {
        ParametreCotisationAssociation cotisationCaisseMetier = new ParametreCotisationAssociation();
        cotisationCaisseMetier.setId(parametreCotisationAssociationSimpleModel.getId());
        if (!JadeStringUtil.isEmpty(parametreCotisationAssociationSimpleModel.getTypeParam())) {
            cotisationCaisseMetier.setTypeParam(TypeParamCotisationAP
                    .fromValue(parametreCotisationAssociationSimpleModel.getTypeParam()));
        }
        if (!JadeStringUtil.isEmpty(parametreCotisationAssociationSimpleModel.getTaux())) {
            cotisationCaisseMetier.setTaux(new Taux(parametreCotisationAssociationSimpleModel.getTaux()));
        }
        if (!JadeStringUtil.isEmpty(parametreCotisationAssociationSimpleModel.getMontant())) {
            cotisationCaisseMetier.setMontant(new Montant(parametreCotisationAssociationSimpleModel.getMontant()));
        }
        if (!JadeStringUtil.isEmpty(parametreCotisationAssociationSimpleModel.getFourchetteDebut())) {
            cotisationCaisseMetier.setFourchetteDebut(new Montant(parametreCotisationAssociationSimpleModel
                    .getFourchetteDebut()));
        }
        if (!JadeStringUtil.isEmpty(parametreCotisationAssociationSimpleModel.getFourchetteFin())) {
            cotisationCaisseMetier.setFourchetteFin(new Montant(parametreCotisationAssociationSimpleModel
                    .getFourchetteFin()));
        }
        cotisationCaisseMetier.setSpy(parametreCotisationAssociationSimpleModel.getSpy());
        return cotisationCaisseMetier;
    }

    @Override
    public ParametreCotisationAssociation convertToDomain(
            ParametreCotisationAssociationComplexModel parametreCotisationAssociationComplexModel) {
        CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel = parametreCotisationAssociationComplexModel
                .getCotisationAssociationProfessionnelleSimpleModel();
        AdministrationComplexModel administrationComplexModel = parametreCotisationAssociationComplexModel
                .getAdministrationComplexModel();
        ParametreCotisationAssociationSimpleModel parametreCotisationAssociationSimpleModel = parametreCotisationAssociationComplexModel
                .getParametreCotisationAssociationSimpleModel();

        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = CotisationAssociationProfessionnelleConverter
                .getInstance().convertToDomain(cotisationAssociationProfessionnelleSimpleModel);
        Administration associationProfessionnelle = AdministrationConverter.convertToDomain(administrationComplexModel);
        cotisationAssociationProfessionnelle.setAssociationProfessionnelle(associationProfessionnelle);

        ParametreCotisationAssociation parametreCotisationAssociation = convertToDomain(parametreCotisationAssociationSimpleModel);
        parametreCotisationAssociation.setCotisationAssociationProfessionnelle(cotisationAssociationProfessionnelle);
        return parametreCotisationAssociation;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new ParametreCotisationAssociationSearchSimpleModel();
    }
}
