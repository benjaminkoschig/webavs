package ch.globaz.vulpecula.repositoriesjade.association.converter;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.association.EnteteFactureAssociationProfessionnelleSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.EnteteFactureAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.models.association.ModeleEntete;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class EnteteFactureAssociationProfessionnelleConverter
        implements
        DomaineConverterJade<EnteteFactureAssociation, JadeComplexModel, EnteteFactureAssociationProfessionnelleSimpleModel> {

    private static final EnteteFactureAssociationProfessionnelleConverter INSTANCE = new EnteteFactureAssociationProfessionnelleConverter();

    public static EnteteFactureAssociationProfessionnelleConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public EnteteFactureAssociation convertToDomain(JadeComplexModel associationCotisationComplexModel) {
        return null;
    }

    @Override
    public EnteteFactureAssociationProfessionnelleSimpleModel convertToPersistence(EnteteFactureAssociation entete) {
        EnteteFactureAssociationProfessionnelleSimpleModel enteteSimpleModel = new EnteteFactureAssociationProfessionnelleSimpleModel();
        enteteSimpleModel.setId(entete.getId());
        enteteSimpleModel.setSpy(entete.getSpy());
        if (entete.getAnneeFacture() != null) {
            enteteSimpleModel.setAnneeFacture(entete.getAnneeFacture().toString());
        }

        if (entete.getEtat() != null) {
            enteteSimpleModel.setEtat(entete.getEtat().getValue());
        }

        if (entete.getEmployeur() != null) {
            enteteSimpleModel.setIdEmployeur(entete.getEmployeur().getId());
        }

        if (entete.getMasseSalariale() != null) {
            enteteSimpleModel.setMasseSalariale(entete.getMasseSalariale().getValue());
        }
        if (entete.getMontantTotal() != null) {
            enteteSimpleModel.setMontantTotal(entete.getMontantTotal().getValue());
        }
        if (entete.getReductionFacture() != null) {
            enteteSimpleModel.setReductionFacture(entete.getReductionFacture().getValue());
        }

        if (entete.getModele() != null) {
            enteteSimpleModel.setIdModeleEntete(entete.getIdModeleEntete());
        }

        if (entete.getAssociationProfessionnelleParent() != null) {
            enteteSimpleModel.setIdAssociation(entete.getIdAssociationProfessionnelleParent());
        }

        enteteSimpleModel.setNumeroSection(entete.getNumeroSection());

        enteteSimpleModel.setIdPassageFacturation(entete.getIdPassageFacturation());
        return enteteSimpleModel;
    }

    @Override
    public EnteteFactureAssociation convertToDomain(
            EnteteFactureAssociationProfessionnelleSimpleModel enteteFactureSimpleModel) {

        EnteteFactureAssociation enteteFacture = new EnteteFactureAssociation();
        enteteFacture.setId(enteteFactureSimpleModel.getId());
        enteteFacture.setSpy(enteteFactureSimpleModel.getSpy());

        enteteFacture.setNumeroSection(enteteFactureSimpleModel.getNumeroSection());

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getAnneeFacture())) {
            enteteFacture.setAnneeFacture(new Annee(enteteFactureSimpleModel.getAnneeFacture()));
        }

        if (!JadeStringUtil.isBlankOrZero(enteteFactureSimpleModel.getEtat())) {
            enteteFacture.setEtat(EtatFactureAP.fromValue(enteteFactureSimpleModel.getEtat()));
        }

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getMasseSalariale())) {
            enteteFacture.setMasseSalariale(new Montant(enteteFactureSimpleModel.getMasseSalariale()));
        }

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getMontantTotal())) {
            enteteFacture.setMontantTotal(new Montant(enteteFactureSimpleModel.getMontantTotal()));
        }

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getReductionFacture())) {
            enteteFacture.setReductionFacture(new Taux(enteteFactureSimpleModel.getReductionFacture()));
        }

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getIdModeleEntete())) {
            enteteFacture.setModele(new ModeleEntete(enteteFactureSimpleModel.getIdModeleEntete()));
        }

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getIdAssociation())) {
            Administration assoc = new Administration();
            assoc.setId(enteteFactureSimpleModel.getIdAssociation());
            enteteFacture.setAssociationProfessionnelleParent(assoc);
        }

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getIdPassageFacturation())) {
            Passage passage = new Passage();
            passage.setId(enteteFactureSimpleModel.getIdPassageFacturation());
            enteteFacture.setPassageFacturation(passage);
        }

        if (!JadeStringUtil.isEmpty(enteteFactureSimpleModel.getIdEmployeur())) {
            Employeur employeur = new Employeur();
            employeur.setId(enteteFactureSimpleModel.getIdEmployeur());
            enteteFacture.setEmployeur(employeur);
        }

        return enteteFacture;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new EnteteFactureAssociationProfessionnelleSearchSimpleModel();
    }
}
