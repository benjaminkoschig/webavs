package ch.globaz.vulpecula.repositoriesjade.association;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.models.association.EnteteFactureAssociationProfessionnelleSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.EnteteFactureAssociationProfessionnelleSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.repositories.association.EnteteFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.domain.repositories.association.LigneFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.external.repositories.tiers.AdministrationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.converter.EnteteFactureAssociationProfessionnelleConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class EnteteFactureAssociationProfessionnelleRepositoryJade extends
        RepositoryJade<EnteteFactureAssociation, JadeComplexModel, EnteteFactureAssociationProfessionnelleSimpleModel>
        implements EnteteFactureAssociationProfessionnelleRepository {

    private LigneFactureAssociationProfessionnelleRepository ligneFactureAssociationProfessionnelleRepository;
    private AdministrationRepository administrationRepository;

    public EnteteFactureAssociationProfessionnelleRepositoryJade(
            LigneFactureAssociationProfessionnelleRepository ligneFactureAssociationProfessionnelleRepository,
            AdministrationRepository administrationRepository) {
        this.ligneFactureAssociationProfessionnelleRepository = ligneFactureAssociationProfessionnelleRepository;
        this.administrationRepository = administrationRepository;
    }

    @Override
    public DomaineConverterJade<EnteteFactureAssociation, JadeComplexModel, EnteteFactureAssociationProfessionnelleSimpleModel> getConverter() {
        return EnteteFactureAssociationProfessionnelleConverter.getInstance();
    }

    @Override
    public List<EnteteFactureAssociation> findByIdEmployeurWithDependencies(String idEmployeur) {
        EnteteFactureAssociationProfessionnelleSearchSimpleModel searchModel = new EnteteFactureAssociationProfessionnelleSearchSimpleModel();
        searchModel.setForIdEmployeur(idEmployeur);
        List<EnteteFactureAssociation> entetes = searchAndFetch(searchModel);
        for (EnteteFactureAssociation enteteFactureAssociation : entetes) {
            List<LigneFactureAssociation> lignes = ligneFactureAssociationProfessionnelleRepository
                    .findByIdEntete(enteteFactureAssociation.getId());
            enteteFactureAssociation.setAssociationProfessionnelleParent(administrationRepository
                    .findById(enteteFactureAssociation.getIdAssociationProfessionnelleParent()));
            enteteFactureAssociation.setModele(VulpeculaServiceLocator.getParametrageAPService().findById(
                    enteteFactureAssociation.getIdModeleEntete()));
            enteteFactureAssociation.setLignesFacture(lignes);
        }
        return entetes;
    }

    @Override
    public Collection<EnteteFactureAssociation> findForEtatWithDependencies(EtatFactureAP etat) {
        EnteteFactureAssociationProfessionnelleSearchSimpleModel searchModel = new EnteteFactureAssociationProfessionnelleSearchSimpleModel();
        searchModel.setForEtat(etat);

        List<EnteteFactureAssociation> entetes = searchAndFetch(searchModel);

        for (EnteteFactureAssociation enteteFactureAssociation : entetes) {
            List<LigneFactureAssociation> lignes = VulpeculaRepositoryLocator.getLigneFactureRepository()
                    .findByIdEntete(enteteFactureAssociation.getId());
            enteteFactureAssociation.setLignesFacture(lignes);
        }

        return entetes;
    }

    @Override
    public EnteteFactureAssociation findByIdAssociationAndAnneeAndIdEmployeur(String idAssociationParente,
            String annee, String idEmployeur) {
        EnteteFactureAssociationProfessionnelleSearchSimpleModel searchModel = new EnteteFactureAssociationProfessionnelleSearchSimpleModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdAssociation(idAssociationParente);
        searchModel.setForAnnee(annee);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<EnteteFactureAssociation> findByIdAssociationAndAnneeAndIdEmployeurForEnfant(
            String idAssociationEnfant, String annee, String idEmployeur) {
        EnteteFactureAssociationProfessionnelleSearchSimpleModel searchModel = new EnteteFactureAssociationProfessionnelleSearchSimpleModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdAssociation(idAssociationEnfant);
        searchModel.setForAnnee(annee);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<EnteteFactureAssociation> findByIdPassageFacturation(String idPassage) {
        EnteteFactureAssociationProfessionnelleSearchSimpleModel searchModel = new EnteteFactureAssociationProfessionnelleSearchSimpleModel();
        searchModel.setForIdPassageFacturation(idPassage);
        return searchAndFetch(searchModel);
    }

}
