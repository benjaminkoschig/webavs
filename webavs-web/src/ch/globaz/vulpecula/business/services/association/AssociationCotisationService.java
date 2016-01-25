package ch.globaz.vulpecula.business.services.association;

import java.util.List;
import java.util.Map;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;

public interface AssociationCotisationService {
    /**
     * Recherche des cotisations groupés par association professionnelle (administration) d'un employeur.
     * 
     * @param idEmployeur String représentant l'id d'un employeur.
     * @return Cotisations groupés par association professionnelle
     */
    Map<AssociationGenre, List<AssociationCotisation>> getCotisationByAssociation(String idEmployeur);

    void create(String idEmployeur, List<AssociationCotisation> associationsCotisations)
            throws UnsatisfiedSpecificationException;
}
