package ch.globaz.vulpecula.web.gson;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;

public class AssociationsGSON {
    public String idEmployeur;
    public AssociationGSON[] associations;

    private static class AssociationGSON {
        public String associationProfessionnelle;
        public String genre;
        public CotisationGSON[] cotisations;
    }

    private static class CotisationGSON {
        public String id;
        public String periodeDebut;
        public String periodeFin;
        public String reductionFacture;
        public String forfait;
        public String masseSalariale;
    }

    public List<AssociationCotisation> convertToDomain() {
        List<AssociationCotisation> cotisations = new ArrayList<AssociationCotisation>();
        for (AssociationGSON associationGSON : associations) {
            for (CotisationGSON cotisationGSON : associationGSON.cotisations) {
                AssociationCotisation associationCotisation = new AssociationCotisation();
                associationCotisation.setIdEmployeur(idEmployeur);
                associationCotisation.setGenre(GenreCotisationAssociationProfessionnelle
                        .fromValue(associationGSON.genre));
                associationCotisation.setPeriode(new Periode(cotisationGSON.periodeDebut, cotisationGSON.periodeFin));
                associationCotisation.setReductionFacture(new Taux(cotisationGSON.reductionFacture));
                associationCotisation.setForfait(new Montant(cotisationGSON.forfait));
                associationCotisation.setMasseSalariale(new Taux(cotisationGSON.masseSalariale));
                associationCotisation.setCotisationAssociationProfessionnelle(VulpeculaRepositoryLocator
                        .getCotisationAssociationProfessionnelleRepository().findById(cotisationGSON.id));

                cotisations.add(associationCotisation);
            }

        }
        return cotisations;
    }
}
