package ch.globaz.vulpecula.repositoriesjade.association.converter;

import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.business.models.comptabilite.RubriqueSimpleModel;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.osiris.db.comptes.CARubrique;

public class CotisationAssociationProfessionnelleConverter implements
        DomaineConverterJade<CotisationAssociationProfessionnelle, CotisationAssociationProfessionnelleComplexModel, CotisationAssociationProfessionnelleSimpleModel> {
    private static final CotisationAssociationProfessionnelleConverter INSTANCE = new CotisationAssociationProfessionnelleConverter();

    public static CotisationAssociationProfessionnelleConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public CotisationAssociationProfessionnelle convertToDomain(
            CotisationAssociationProfessionnelleComplexModel cotisationAssociationProfessionnelleComplexModel) {
        CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel = cotisationAssociationProfessionnelleComplexModel
                .getCotisationAssociationProfessionnelleSimpleModel();
        AdministrationComplexModel associationProfessionnelleComplexModel = cotisationAssociationProfessionnelleComplexModel
                .getAdministrationComplexModel();
        RubriqueSimpleModel rubriqueModel = cotisationAssociationProfessionnelleComplexModel.getRubriqueSimpleModel();

        CARubrique rubrique = new CARubrique();
        if (rubriqueModel != null && rubriqueModel.getId() != null && rubriqueModel.getId().length() != 0
                && !rubriqueModel.getId().equals("0")) {
            rubrique.setId(rubriqueModel.getIdRubrique());
            rubrique.setIdExterne(rubriqueModel.getIdExterne());
        }

        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = convertToDomain(
                cotisationAssociationProfessionnelleSimpleModel);
        cotisationAssociationProfessionnelle.setAssociationProfessionnelle(
                AdministrationConverter.convertToDomain(associationProfessionnelleComplexModel));
        cotisationAssociationProfessionnelle.setRubrique(rubrique);

        return cotisationAssociationProfessionnelle;
    }

    @Override
    public CotisationAssociationProfessionnelleSimpleModel convertToPersistence(
            CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle) {
        CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel = new CotisationAssociationProfessionnelleSimpleModel();
        cotisationAssociationProfessionnelleSimpleModel.setId(cotisationAssociationProfessionnelle.getId());
        cotisationAssociationProfessionnelleSimpleModel.setIdAssociationProfessionnelle(
                cotisationAssociationProfessionnelle.getIdAssociationProfessionnelle());
        cotisationAssociationProfessionnelleSimpleModel.setLibelle(cotisationAssociationProfessionnelle.getLibelle());
        cotisationAssociationProfessionnelleSimpleModel
                .setLibelleUpper(JadeStringUtil.toUpperCase(cotisationAssociationProfessionnelle.getLibelle()));
        cotisationAssociationProfessionnelleSimpleModel
                .setLibelleFR(cotisationAssociationProfessionnelle.getLibelleFR());
        cotisationAssociationProfessionnelleSimpleModel
                .setLibelleDE(cotisationAssociationProfessionnelle.getLibelleDE());
        cotisationAssociationProfessionnelleSimpleModel
                .setLibelleIT(cotisationAssociationProfessionnelle.getLibelleIT());
        cotisationAssociationProfessionnelleSimpleModel
                .setMasseSalarialeDefaut(cotisationAssociationProfessionnelle.getMasseSalarialeDefaut().getValue());
        cotisationAssociationProfessionnelleSimpleModel
                .setFacturerDefaut(cotisationAssociationProfessionnelle.getFacturerDefaut().getValue());
        cotisationAssociationProfessionnelleSimpleModel.setSpy(cotisationAssociationProfessionnelle.getSpy());
        cotisationAssociationProfessionnelleSimpleModel
                .setGenre(cotisationAssociationProfessionnelle.getGenre().getValue());
        cotisationAssociationProfessionnelleSimpleModel
                .setIdRubrique(cotisationAssociationProfessionnelle.getIdRubrique());
        cotisationAssociationProfessionnelleSimpleModel
                .setPrintOrder(cotisationAssociationProfessionnelle.getPrintOrder());
        return cotisationAssociationProfessionnelleSimpleModel;
    }

    @Override
    public CotisationAssociationProfessionnelle convertToDomain(
            CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel) {
        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = new CotisationAssociationProfessionnelle();
        cotisationAssociationProfessionnelle.setId(cotisationAssociationProfessionnelleSimpleModel.getId());
        cotisationAssociationProfessionnelle.setLibelle(cotisationAssociationProfessionnelleSimpleModel.getLibelle());
        cotisationAssociationProfessionnelle
                .setLibelleFR(cotisationAssociationProfessionnelleSimpleModel.getLibelleFR());
        cotisationAssociationProfessionnelle
                .setLibelleDE(cotisationAssociationProfessionnelleSimpleModel.getLibelleDE());
        cotisationAssociationProfessionnelle
                .setLibelleIT(cotisationAssociationProfessionnelleSimpleModel.getLibelleIT());
        cotisationAssociationProfessionnelle.setMasseSalarialeDefaut(
                new Taux(cotisationAssociationProfessionnelleSimpleModel.getMasseSalarialeDefaut()));
        cotisationAssociationProfessionnelle.setFacturerDefaut(CategorieFactureAssociationProfessionnelle
                .fromValue(cotisationAssociationProfessionnelleSimpleModel.getFacturerDefaut()));
        cotisationAssociationProfessionnelle.setSpy(cotisationAssociationProfessionnelleSimpleModel.getSpy());
        cotisationAssociationProfessionnelle.setGenre(GenreCotisationAssociationProfessionnelle
                .fromValue(cotisationAssociationProfessionnelleSimpleModel.getGenre()));
        cotisationAssociationProfessionnelle
                .setIdRubrique(cotisationAssociationProfessionnelleSimpleModel.getIdRubrique());
        cotisationAssociationProfessionnelle
                .setPrintOrder(cotisationAssociationProfessionnelleSimpleModel.getPrintOrder());
        return cotisationAssociationProfessionnelle;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new CotisationAssociationProfessionnelleSearchSimpleModel();
    }
}
