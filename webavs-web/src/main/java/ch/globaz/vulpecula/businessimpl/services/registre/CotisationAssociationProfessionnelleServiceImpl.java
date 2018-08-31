package ch.globaz.vulpecula.businessimpl.services.registre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModel;
import ch.globaz.pyxis.business.model.CompositionTiersSimpleModelSearch;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.registre.CotisationAssociationProfessionnelleService;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.repositories.association.CotisationAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;

public class CotisationAssociationProfessionnelleServiceImpl implements CotisationAssociationProfessionnelleService {
    private CotisationAssociationProfessionnelleRepository repository;
    private final static String TYPE_LIEN_ASSOCIATION_PROF = "507013";
    private final static String TYPE_LIEN_ASSOCIATION_CPP = "507014";

    public CotisationAssociationProfessionnelleServiceImpl(CotisationAssociationProfessionnelleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Map<AssociationGenre, Collection<CotisationAssociationProfessionnelle>> findAllCotisationsByAssociationGenre() {
        List<CotisationAssociationProfessionnelle> cotisations = repository.findAll();
        return CotisationAssociationProfessionnelle.groupByAssociationGenre(cotisations);
    }

    @Override
    public List<CotisationAssociationProfessionnelle> findCotisationsByAssociationGenre(String idAssociation,
            GenreCotisationAssociationProfessionnelle genre) {
        return repository.findByAssociationGenre(idAssociation, genre);
    }

    @Override
    public void validate(ParametreCotisationAssociation cotisationCM) throws UnsatisfiedSpecificationException {
        // On recherche toutes les cotisations identiques, avant de regarder la fourchette
        List<ParametreCotisationAssociation> cotisationList = VulpeculaRepositoryLocator
                .getParametreCotisationAssociationRepository().findCotisationsForFourchette(cotisationCM);
        cotisationCM.validate(cotisationList);
    }

    @Override
    public CotisationAssociationProfessionnelle findCotisationsById(String idCotisationAP) {
        return repository.findById(idCotisationAP);
    }

    @Override
    public List<String> findAssociationsParente(String idAssociation) {
        CompositionTiersSimpleModelSearch search = new CompositionTiersSimpleModelSearch();
        search.setForIdTiersEnfant(idAssociation);
        search.setForTypeLien(TYPE_LIEN_ASSOCIATION_PROF);
        List<CompositionTiersSimpleModel> listeComposition = RepositoryJade.searchForAndFetch(search);

        List<String> idTiersParent = new ArrayList<String>();

        for (CompositionTiersSimpleModel compo : listeComposition) {
            idTiersParent.add(compo.getIdTiersParent());
        }

        return idTiersParent;
    }

    @Override
    public List<String> findAssociationsEnfant(String idAssociation) {
        CompositionTiersSimpleModelSearch search = new CompositionTiersSimpleModelSearch();
        search.setForIdTiersParent(idAssociation);
        search.setForTypeLien(TYPE_LIEN_ASSOCIATION_PROF);
        List<CompositionTiersSimpleModel> listeComposition = RepositoryJade.searchForAndFetch(search);

        List<String> idTiersEnfant = new ArrayList<String>();

        for (CompositionTiersSimpleModel compo : listeComposition) {
            idTiersEnfant.add(compo.getIdTiersEnfant());
        }

        return idTiersEnfant;
    }

    @Override
    public List<String> findAssociationsCPP(String idAssociation) {
        CompositionTiersSimpleModelSearch search = new CompositionTiersSimpleModelSearch();
        search.setForIdTiersEnfant(idAssociation);
        search.setForTypeLien(TYPE_LIEN_ASSOCIATION_CPP);
        List<CompositionTiersSimpleModel> listeComposition = RepositoryJade.searchForAndFetch(search);

        CompositionTiersSimpleModelSearch search2 = new CompositionTiersSimpleModelSearch();
        search2.setForIdTiersParent(idAssociation);
        search2.setForTypeLien(TYPE_LIEN_ASSOCIATION_CPP);
        List<CompositionTiersSimpleModel> listeComposition2 = RepositoryJade.searchForAndFetch(search2);

        List<String> idTiersParent = new ArrayList<String>();

        for (CompositionTiersSimpleModel compo : listeComposition) {
            idTiersParent.add(compo.getIdTiersParent());
        }

        for (CompositionTiersSimpleModel compo : listeComposition2) {
            idTiersParent.add(compo.getIdTiersEnfant());
        }

        return idTiersParent;
    }
}