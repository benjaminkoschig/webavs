package ch.globaz.vulpecula.repositoriesjade.association;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationComplexModel;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationSearchComplexModel;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.association.AssociationCotisationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.association.converter.AssociationCotisationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class AssociationCotisationRepositoryJade extends
RepositoryJade<AssociationCotisation, AssociationCotisationComplexModel, AssociationCotisationSimpleModel>
implements AssociationCotisationRepository {

    @Override
    public DomaineConverterJade<AssociationCotisation, AssociationCotisationComplexModel, AssociationCotisationSimpleModel> getConverter() {
        return AssociationCotisationConverter.getInstance();
    }

    @Override
    public List<AssociationCotisation> create(List<AssociationCotisation> associationsCotisations) {
        List<AssociationCotisation> acs = new ArrayList<AssociationCotisation>();
        for (AssociationCotisation associationCotisation : associationsCotisations) {
            acs.add(create(associationCotisation));
        }
        return acs;
    }

    @Override
    public List<AssociationCotisation> findByIdEmployeur(String idEmployeur) {
        AssociationCotisationSearchComplexModel searchModel = new AssociationCotisationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<AssociationCotisation> findByIdEmployeurForFacturation(String idEmployeur) {
        AssociationCotisationSearchComplexModel searchModel = new AssociationCotisationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<String> findEmployeursForYear(String idEmployeur, String idAssociation, Annee annee) {
        AssociationCotisationSearchComplexModel searchModel = new AssociationCotisationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdAssociationProfessionnelle(idAssociation);
        searchModel.setForDateDebut(annee.getFirstDayOfYear());
        searchModel.setForDateFin(annee.getLastDayOfYear());
        List<AssociationCotisation> liste = searchAndFetch(searchModel);
        List<String> listIdEmployeurs = new ArrayList<String>();
        for (AssociationCotisation associationCotisation : liste) {
            if (!listIdEmployeurs.contains(associationCotisation.getIdEmployeur())) {
                listIdEmployeurs.add(associationCotisation.getIdEmployeur());
            }
        }
        return listIdEmployeurs;
    }

    @Override
    public List<String> findEmployeursForYear(String idEmployeur, List<String> idsAssociation, Annee annee) {
        AssociationCotisationSearchComplexModel searchModel = new AssociationCotisationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdAssociationProfessionnelleIn(idsAssociation);
        searchModel.setForDateDebut(annee.getFirstDayOfYear());
        searchModel.setForDateFin(annee.getLastDayOfYear());
        searchModel.setWhereKey(AssociationCotisationSearchComplexModel.WHERE_FACTURATION_AP);
        List<AssociationCotisation> liste = searchAndFetch(searchModel);
        List<String> listIdEmployeurs = new ArrayList<String>();
        for (AssociationCotisation associationCotisation : liste) {
            if (!listIdEmployeurs.contains(associationCotisation.getIdEmployeur())) {
                listIdEmployeurs.add(associationCotisation.getIdEmployeur());
            }
        }
        return listIdEmployeurs;
    }

    @Override
    public AssociationCotisation findByIdWithDependencies(String idAssociationCotisation) {
        AssociationCotisation association = findById(idAssociationCotisation);

        CotisationAssociationProfessionnelle cotisation = VulpeculaRepositoryLocator
                .getCotisationAssociationProfessionnelleRepository()
                .findById(association.getIdCotisationAssociationProfessionnelle());
        association.setCotisationAssociationProfessionnelle(cotisation);

        return association;
    }

    @Override
    public List<AssociationCotisation> findByIdEmployeurForYearWithDependencies(String idEmployeur, Annee annee,
            List<String> idsAssociations, GenreCotisationAssociationProfessionnelle genre) {
        AssociationCotisationSearchComplexModel searchModel = new AssociationCotisationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdAssociationProfessionnelleIn(idsAssociations);
        searchModel.setOrderKey(AssociationCotisationSearchComplexModel.ORDERBY_PRINTORDER);
        List<AssociationCotisation> liste = new ArrayList<AssociationCotisation>();

        for (AssociationCotisation ac : searchAndFetch(searchModel)) {
            if (genre != null && !genre.equals(ac.getGenre())) {
                continue;
            }
            if (ac.getPeriode().isActifIn(annee)) {
                liste.add(ac);
            }
        }
        for (AssociationCotisation associationCotisation : liste) {
            if (genre != null && !genre.equals(associationCotisation.getGenre())) {
                continue;
            }
            CotisationAssociationProfessionnelle cotisation = VulpeculaRepositoryLocator
                    .getCotisationAssociationProfessionnelleRepository()
                    .findById(associationCotisation.getIdCotisationAssociationProfessionnelle());
            associationCotisation.setCotisationAssociationProfessionnelle(cotisation);
        }
        return liste;
    }

    @Override
    public List<String> findEmployeursForYear(String idEmployeur, List<String> idsAssociation, Annee annee,
            GenreCotisationAssociationProfessionnelle genre) {
        AssociationCotisationSearchComplexModel searchModel = new AssociationCotisationSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForIdAssociationProfessionnelleIn(idsAssociation);
        searchModel.setForDateDebut(annee.getFirstDayOfYear());
        searchModel.setForDateFin(annee.getLastDayOfYear());
        searchModel.setWhereKey(AssociationCotisationSearchComplexModel.WHERE_FACTURATION_AP);
        List<AssociationCotisation> liste = searchAndFetch(searchModel);
        List<String> listIdEmployeurs = new ArrayList<String>();
        for (AssociationCotisation associationCotisation : liste) {
            if (!listIdEmployeurs.contains(associationCotisation.getIdEmployeur()) && genre.equals(associationCotisation.getGenre())) {
                listIdEmployeurs.add(associationCotisation.getIdEmployeur());
            }
        }
        return listIdEmployeurs;
    }
}
