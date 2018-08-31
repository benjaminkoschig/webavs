package ch.globaz.vulpecula.business.services.association;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import java.util.Map;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.common.Montant;

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

    boolean isDansLaFourchette(String idCotisationAssociationProfessionnelle, Montant masseSalariale)
            throws JadePersistenceException;
}
