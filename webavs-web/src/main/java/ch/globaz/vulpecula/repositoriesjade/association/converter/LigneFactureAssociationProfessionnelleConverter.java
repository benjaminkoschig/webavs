package ch.globaz.vulpecula.repositoriesjade.association.converter;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.association.LigneFactureAssociationProfessionnelleSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.LigneFactureAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.TypeParamCotisationAP;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class LigneFactureAssociationProfessionnelleConverter
        implements
        DomaineConverterJade<LigneFactureAssociation, JadeComplexModel, LigneFactureAssociationProfessionnelleSimpleModel> {

    private static final LigneFactureAssociationProfessionnelleConverter INSTANCE = new LigneFactureAssociationProfessionnelleConverter();

    public static LigneFactureAssociationProfessionnelleConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public LigneFactureAssociation convertToDomain(JadeComplexModel associationCotisationComplexModel) {
        return null;
    }

    @Override
    public LigneFactureAssociationProfessionnelleSimpleModel convertToPersistence(LigneFactureAssociation ligne) {
        LigneFactureAssociationProfessionnelleSimpleModel ligneSimpleModel = new LigneFactureAssociationProfessionnelleSimpleModel();
        ligneSimpleModel.setId(ligne.getId());
        if (ligne.getEnteteFacture() != null) {
            ligneSimpleModel.setIdEnteteFacture(ligne.getEnteteFacture().getId());
        }
        if (ligne.getPeriodeDebut() != null) {
            ligneSimpleModel.setPeriodeDebut(ligne.getPeriodeDebut().getSwissValue());
        }
        if (ligne.getPeriodeFin() != null) {
            ligneSimpleModel.setPeriodeFin(ligne.getPeriodeFin().getSwissValue());
        }
        if (ligne.getAssociationCotisation() != null) {
            ligneSimpleModel.setIdAssociationCotisation(ligne.getAssociationCotisation().getId());
        }
        if (ligne.getFourchetteDebut() != null) {
            ligneSimpleModel.setFourchetteDebut(ligne.getFourchetteDebut().getValue());
        }
        if (ligne.getFourchetteFin() != null) {
            ligneSimpleModel.setFourchetteFin(ligne.getFourchetteFin().getValue());
        }
        if (ligne.getMontantCotisation() != null) {
            ligneSimpleModel.setMontantCotisation(ligne.getMontantCotisation().getValue());
        }
        if (ligne.getTypeParametre() != null) {
            ligneSimpleModel.setTypeParametre(ligne.getTypeParametre().getValue());
        }
        if (ligne.getTauxCotisation() != null) {
            ligneSimpleModel.setTauxCotisation(ligne.getTauxCotisation().getValue());
        }
        if (ligne.getMassePourCotisation() != null) {
            ligneSimpleModel.setMassePourCotisation(ligne.getMassePourCotisation().getValue());
        }

        if (ligne.getFacteur() != null) {
            ligneSimpleModel.setFacteurCotisation(ligne.getFacteur().toString());
        }

        ligneSimpleModel.setSpy(ligne.getSpy());
        return ligneSimpleModel;
    }

    @Override
    public LigneFactureAssociation convertToDomain(
            LigneFactureAssociationProfessionnelleSimpleModel ligneFactureSimpleModel) {

        LigneFactureAssociation ligneFacture = new LigneFactureAssociation();
        ligneFacture.setId(ligneFactureSimpleModel.getId());
        ligneFacture.setSpy(ligneFactureSimpleModel.getSpy());
        if (!JadeStringUtil.isEmpty(ligneFactureSimpleModel.getIdEnteteFacture())) {
            EnteteFactureAssociation entete = VulpeculaRepositoryLocator.getEnteteFactureRepository().findById(
                    ligneFactureSimpleModel.getIdEnteteFacture());
            ligneFacture.setEnteteFacture(entete);
        }

        if (!JadeStringUtil.isBlankOrZero(ligneFactureSimpleModel.getPeriodeDebut())) {
            ligneFacture.setPeriodeDebut(new Date(ligneFactureSimpleModel.getPeriodeDebut()));
        }

        if (!JadeStringUtil.isBlankOrZero(ligneFactureSimpleModel.getPeriodeFin())) {
            ligneFacture.setPeriodeFin(new Date(ligneFactureSimpleModel.getPeriodeFin()));
        }

        if (!JadeStringUtil.isBlankOrZero(ligneFactureSimpleModel.getIdAssociationCotisation())) {
            AssociationCotisation associationCotisation = VulpeculaRepositoryLocator
                    .getAssociationCotisationRepository().findByIdWithDependencies(
                            ligneFactureSimpleModel.getIdAssociationCotisation());
            ligneFacture.setAssociationCotisation(associationCotisation);
        }

        if (!JadeStringUtil.isEmpty(ligneFactureSimpleModel.getFourchetteDebut())) {
            ligneFacture.setFourchetteDebut(new Montant(ligneFactureSimpleModel.getFourchetteDebut()));
        }

        if (!JadeStringUtil.isEmpty(ligneFactureSimpleModel.getFourchetteFin())) {
            ligneFacture.setFourchetteFin(new Montant(ligneFactureSimpleModel.getFourchetteFin()));
        }

        if (!JadeStringUtil.isEmpty(ligneFactureSimpleModel.getMontantCotisation())) {
            ligneFacture.setMontantCotisation(new Montant(ligneFactureSimpleModel.getMontantCotisation()));
        }

        if (!JadeStringUtil.isBlankOrZero(ligneFactureSimpleModel.getTypeParametre())) {
            ligneFacture.setTypeParametre(TypeParamCotisationAP.fromValue(ligneFactureSimpleModel.getTypeParametre()));
        }

        if (!JadeStringUtil.isEmpty(ligneFactureSimpleModel.getTauxCotisation())) {
            ligneFacture.setTauxCotisation(new Taux(ligneFactureSimpleModel.getTauxCotisation()));
        }

        if (!JadeStringUtil.isEmpty(ligneFactureSimpleModel.getMassePourCotisation())) {
            ligneFacture.setMassePourCotisation(new Montant(ligneFactureSimpleModel.getMassePourCotisation()));
        }

        if (!JadeStringUtil.isBlankOrZero(ligneFactureSimpleModel.getFacteurCotisation())) {
            ligneFacture.setFacteur(Double.valueOf(ligneFactureSimpleModel.getFacteurCotisation()));
        }

        return ligneFacture;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new LigneFactureAssociationProfessionnelleSearchSimpleModel();
    }
}
