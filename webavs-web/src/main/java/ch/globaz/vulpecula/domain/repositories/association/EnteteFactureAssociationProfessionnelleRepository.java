package ch.globaz.vulpecula.domain.repositories.association;

import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface EnteteFactureAssociationProfessionnelleRepository extends Repository<EnteteFactureAssociation> {
    List<EnteteFactureAssociation> findByIdEmployeurWithDependencies(String idEmployeur);

    Collection<EnteteFactureAssociation> findForEtatWithDependencies(EtatFactureAP etat);

    EnteteFactureAssociation findByIdAssociationAndAnneeAndIdEmployeur(String idAssociationParente, String annee,
            String idEmployeur);

    List<EnteteFactureAssociation> findByIdAssociationAndAnneeAndIdEmployeurForEnfant(String idAssociationEnfant,
            String annee, String idEmployeur);

    List<EnteteFactureAssociation> findByIdPassageFacturation(String idPassage);
}
