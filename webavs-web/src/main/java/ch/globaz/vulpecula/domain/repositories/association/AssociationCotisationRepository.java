package ch.globaz.vulpecula.domain.repositories.association;

import java.util.List;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface AssociationCotisationRepository extends Repository<AssociationCotisation> {
    List<AssociationCotisation> create(List<AssociationCotisation> associationsCotisations);

    List<AssociationCotisation> findByIdEmployeur(String idEmployeur);

    List<AssociationCotisation> findByIdEmployeurForFacturation(String idEmployeur);

    List<String> findEmployeursForYear(String idEmployeur, String idAssociation, Annee annee);

    List<String> findEmployeursForYear(String idEmployeur, List<String> idsAssociation, Annee annee);

    List<String> findEmployeursForYear(String idEmployeur, List<String> idsAssociation, Annee annee, GenreCotisationAssociationProfessionnelle genre);

    AssociationCotisation findByIdWithDependencies(String idCotisation);

    List<AssociationCotisation> findByIdEmployeurForYearWithDependencies(String idEmployeur, Annee annee,
            List<String> idsAssociations, GenreCotisationAssociationProfessionnelle genre);
}
