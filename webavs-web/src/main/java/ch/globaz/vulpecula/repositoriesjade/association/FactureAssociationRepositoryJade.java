package ch.globaz.vulpecula.repositoriesjade.association;

import java.util.List;
import ch.globaz.vulpecula.business.models.association.FactureAssociationComplexModel;
import ch.globaz.vulpecula.business.models.association.FactureAssociationSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import ch.globaz.vulpecula.domain.models.association.FacturesAssociations;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.association.EnteteFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.domain.repositories.association.FactureAssociationRepository;
import ch.globaz.vulpecula.domain.repositories.association.LigneFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.repositoriesjade.musca.converters.PassageConverter;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.AdministrationConverter;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.converter.AssociationCotisationConverter;
import ch.globaz.vulpecula.repositoriesjade.association.converter.CotisationAssociationProfessionnelleConverter;
import ch.globaz.vulpecula.repositoriesjade.association.converter.EnteteFactureAssociationProfessionnelleConverter;
import ch.globaz.vulpecula.repositoriesjade.association.converter.LigneFactureAssociationProfessionnelleConverter;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.EmployeurConverter;

public class FactureAssociationRepositoryJade implements FactureAssociationRepository {
    private LigneFactureAssociationProfessionnelleRepository ligneFactureRepository;
    private EnteteFactureAssociationProfessionnelleRepository enteteFactureRepository;

    public FactureAssociationRepositoryJade(LigneFactureAssociationProfessionnelleRepository ligneFactureRepository,
            EnteteFactureAssociationProfessionnelleRepository enteteFactureRepository) {
        this.ligneFactureRepository = ligneFactureRepository;
        this.enteteFactureRepository = enteteFactureRepository;
    }

    @Override
    public FactureAssociation create(FactureAssociation facture) {
        EnteteFactureAssociation enteteWithId = enteteFactureRepository.create(facture.getEnteteFacture());
        for (LigneFactureAssociation ligne : facture.getLignes()) {
            ligne.setEnteteFacture(enteteWithId);
            ligneFactureRepository.create(ligne);
        }
        return facture;
    }

    @Override
    public FactureAssociation findById(String id) {
        List<LigneFactureAssociation> lignes = ligneFactureRepository.findByIdEntete(id);
        return new FactureAssociation(lignes);
    }

    @Override
    public FacturesAssociations findByIdIn(List<String> ids) {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForIdIn(ids);
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }

    @Override
    public FacturesAssociations findValides() {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForEtat(EtatFactureAP.VALIDE.getValue());
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }

    @Override
    public FacturesAssociations findValidesByIdPassage(String idPassage) {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForIdPassage(idPassage);
        searchModel.setForEtat(EtatFactureAP.VALIDE.getValue());
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }

    @Override
    public FacturesAssociations findByIdPassageFacturation(String idPassage) {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForIdPassage(idPassage);
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }

    @Override
    public FactureAssociation update(FactureAssociation entity) {
        return null;
    }

    @Override
    public void delete(FactureAssociation entity) {
        // FIXME : Controler état de la facture ?
        for (LigneFactureAssociation ligneFactureAP : entity.getLignes()) {
            VulpeculaRepositoryLocator.getLigneFactureRepository().delete(ligneFactureAP);
        }
        VulpeculaRepositoryLocator.getEnteteFactureRepository().delete(entity.getEnteteFacture());
    }

    @Override
    public void deleteById(String idEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FacturesAssociations findByIdEmployeurAndByAnnee(String idEmployeur, Annee annee) {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForAnneeFacture(annee);
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }

    @Override
    public FacturesAssociations findByIdEmployeurAndByAnnee(String idEmployeur, Annee annee,
            GenreCotisationAssociationProfessionnelle genre, List<String> idsAssociation) {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForAnneeFacture(annee);
        searchModel.setForIdAssociationIn(idsAssociation);

        if (genre != null) {
            searchModel.setForGenre(genre.getValue());
        }
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }

    private Administration fetchAdministrationParent(FactureAssociationComplexModel model) {
        return AdministrationConverter.convertToDomain(model.getAdministrationParentComplexModel());
    }

    private Administration fetchAdministration(FactureAssociationComplexModel model) {
        return AdministrationConverter.convertToDomain(model.getAdministrationComplexModel());
    }

    @Override
    public FacturesAssociations findByIdEmployeur(String idEmployeur) {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }

    private FacturesAssociations fetch(List<FactureAssociationComplexModel> facms) {
        FacturesAssociations fas = new FacturesAssociations();
        for (FactureAssociationComplexModel facm : facms) {
            LigneFactureAssociation ligne = LigneFactureAssociationProfessionnelleConverter.getInstance()
                    .convertToDomain(facm.getLigneFactureAssociationProfessionnelleSimpleModel());
            AssociationCotisation associationCotisation = AssociationCotisationConverter.getInstance().convertToDomain(
                    facm.getAssociationCotisationSimpleModel());
            CotisationAssociationProfessionnelle cotisationAP = CotisationAssociationProfessionnelleConverter
                    .getInstance().convertToDomain(facm.getCotisationAssociationProfessionnelleSimpleModel());
            EnteteFactureAssociation entete = EnteteFactureAssociationProfessionnelleConverter.getInstance()
                    .convertToDomain(facm.getEnteteFactureAssociationProfessionnelleSimpleModel());
            Passage passage = PassageConverter.getInstance().convertToDomain(facm.getPassageModel());
            entete.setModele(VulpeculaServiceLocator.getParametrageAPService().findById(entete.getModele().getId()));
            entete.setAssociationProfessionnelleParent(fetchAdministrationParent(facm));
            entete.setPassageFacturation(passage);

            Employeur employeur = EmployeurConverter.getInstance().convertToDomain(facm.getEmployeurComplexModel());
            entete.setEmployeur(employeur);

            ligne.setAssociationCotisation(associationCotisation);
            ligne.setEnteteFacture(entete);
            associationCotisation.setCotisationAssociationProfessionnelle(cotisationAP);
            cotisationAP.setAssociationProfessionnelle(fetchAdministration(facm));
            fas.addLigne(ligne);
        }
        return fas;
    }

    @Override
    public void create(FacturesAssociations factures) {
        for (FactureAssociation facture : factures) {
            create(facture);
        }
    }

    @Override
    public FacturesAssociations findByIdPassageFacturationAndIdEmployeur(String idPassageFacturation, String idEmployeur) {
        FactureAssociationSearchComplexModel searchModel = new FactureAssociationSearchComplexModel();
        searchModel.setForIdPassage(idPassageFacturation);
        searchModel.setForIdEmployeur(idEmployeur);
        List<FactureAssociationComplexModel> facms = RepositoryJade.searchForAndFetch(searchModel);
        return fetch(facms);
    }
}
