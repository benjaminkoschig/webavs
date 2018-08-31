package ch.globaz.vulpecula.domain.repositories.association;

import java.util.List;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import ch.globaz.vulpecula.domain.models.association.FacturesAssociations;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface FactureAssociationRepository extends Repository<FactureAssociation> {
    FacturesAssociations findByIdEmployeurAndByAnnee(String idEmployeur, Annee annee);

    FacturesAssociations findByIdEmployeurAndByAnnee(String idEmployeur, Annee annee,
            GenreCotisationAssociationProfessionnelle genre, List<String> associationsATraiter);

    FacturesAssociations findByIdEmployeur(String idEmployeur);

    FacturesAssociations findByIdIn(List<String> ids);

    void create(FacturesAssociations factures);

    FacturesAssociations findValides();

    FacturesAssociations findValidesByIdPassage(String idPassage);

    FacturesAssociations findByIdPassageFacturation(String idPassageFacturation);

    FacturesAssociations findByIdPassageFacturationAndIdEmployeur(String idPassageFacturation, String idEmployeur);
}
