package ch.globaz.vulpecula.repositoriesjade.association.converter;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class CotisationAssociationProfessionnelleConverter
        implements
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

        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = convertToDomain(cotisationAssociationProfessionnelleSimpleModel);
        cotisationAssociationProfessionnelle.setAssociationProfessionnelle(AdministrationConverter
                .convertToDomain(associationProfessionnelleComplexModel));
        return cotisationAssociationProfessionnelle;
    }

    @Override
    public CotisationAssociationProfessionnelleSimpleModel convertToPersistence(
            CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle) {
        CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel = new CotisationAssociationProfessionnelleSimpleModel();
        cotisationAssociationProfessionnelleSimpleModel.setId(cotisationAssociationProfessionnelle.getId());
        cotisationAssociationProfessionnelleSimpleModel.setLibelle(cotisationAssociationProfessionnelle.getLibelle());
        cotisationAssociationProfessionnelleSimpleModel.setMontantBase(cotisationAssociationProfessionnelle
                .getMontantBase().getValue());
        cotisationAssociationProfessionnelleSimpleModel.setMontantMinimum(cotisationAssociationProfessionnelle
                .getMontantMinimum().getValue());
        cotisationAssociationProfessionnelleSimpleModel.setMontantMaximum(cotisationAssociationProfessionnelle
                .getMontantMaximum().getValue());
        cotisationAssociationProfessionnelleSimpleModel.setSpy(cotisationAssociationProfessionnelle.getSpy());
        cotisationAssociationProfessionnelleSimpleModel.setGenre(cotisationAssociationProfessionnelle.getGenre()
                .getValue());
        return cotisationAssociationProfessionnelleSimpleModel;
    }

    @Override
    public CotisationAssociationProfessionnelle convertToDomain(
            CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel) {
        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = new CotisationAssociationProfessionnelle();
        cotisationAssociationProfessionnelle.setId(cotisationAssociationProfessionnelleSimpleModel.getId());
        cotisationAssociationProfessionnelle.setLibelle(cotisationAssociationProfessionnelleSimpleModel.getLibelle());
        cotisationAssociationProfessionnelle.setMontantBase(new Montant(cotisationAssociationProfessionnelleSimpleModel
                .getMontantBase()));
        cotisationAssociationProfessionnelle.setMontantMinimum(new Montant(
                cotisationAssociationProfessionnelleSimpleModel.getMontantMinimum()));
        cotisationAssociationProfessionnelle.setMontantMaximum(new Montant(
                cotisationAssociationProfessionnelleSimpleModel.getMontantMaximum()));
        cotisationAssociationProfessionnelle.setSpy(cotisationAssociationProfessionnelleSimpleModel.getSpy());
        cotisationAssociationProfessionnelle.setGenre(GenreCotisationAssociationProfessionnelle
                .fromValue(cotisationAssociationProfessionnelleSimpleModel.getGenre()));
        return cotisationAssociationProfessionnelle;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new CotisationAssociationProfessionnelleSearchSimpleModel();
    }
}
