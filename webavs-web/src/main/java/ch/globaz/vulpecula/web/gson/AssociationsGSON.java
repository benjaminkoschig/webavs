package ch.globaz.vulpecula.web.gson;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationEmployeur;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;

public class AssociationsGSON {
    public String idEmployeur;
    public AssociationGSON[] associations;

    private static class AssociationGSON {
        public String associationProfessionnelle;
        public String genre;
        public CotisationGSON[] cotisations;
        public String masseAssociation;
    }

    private static class CotisationGSON {
        public String id;
        public String periodeDebut;
        public String periodeFin;
        public String forfait;
        public String facturer;
        public String idAssociationCotisation;
        public String pspy;
    }

    public List<AssociationCotisation> convertToDomain() {
        List<AssociationCotisation> cotisations = new ArrayList<AssociationCotisation>();
        for (AssociationGSON associationGSON : associations) {
            AssociationEmployeur associationEmployeur = new AssociationEmployeur();
            associationEmployeur.setIdEmployeur(idEmployeur);
            associationEmployeur.setIdAssociation(associationGSON.associationProfessionnelle);
            associationEmployeur.setMasseAssociation(new Montant(associationGSON.masseAssociation));
            for (CotisationGSON cotisationGSON : associationGSON.cotisations) {
                AssociationCotisation associationCotisation = new AssociationCotisation();
                associationCotisation.setIdEmployeur(idEmployeur);
                associationCotisation.setGenre(GenreCotisationAssociationProfessionnelle
                        .fromValue(associationGSON.genre));
                associationCotisation.setPeriode(new Periode(cotisationGSON.periodeDebut, cotisationGSON.periodeFin));
                associationCotisation.setForfait(new Montant(cotisationGSON.forfait));
                // associationCotisation.setFacturer(CategorieFactureAssociationProfessionnelle
                // .fromValue(cotisationGSON.facturer));
                associationCotisation.setCotisationAssociationProfessionnelle(VulpeculaRepositoryLocator
                        .getCotisationAssociationProfessionnelleRepository().findById(cotisationGSON.id));
                associationCotisation.setId(cotisationGSON.idAssociationCotisation);
                associationCotisation.setSpy(cotisationGSON.pspy);
                associationCotisation.setAssociationEmployeur(associationEmployeur);
                cotisations.add(associationCotisation);
            }

        }
        return cotisations;
    }
}
