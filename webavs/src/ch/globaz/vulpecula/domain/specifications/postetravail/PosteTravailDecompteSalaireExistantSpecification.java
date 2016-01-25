package ch.globaz.vulpecula.domain.specifications.postetravail;

import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailDecompteSalaireExistantSpecification extends AbstractSpecification<PosteTravail> {
    public List<DecompteSalaire> decompteSalaires = null;

    public PosteTravailDecompteSalaireExistantSpecification(final List<DecompteSalaire> decompteSalaires) {
        this.decompteSalaires = decompteSalaires;
    }

    @Override
    public boolean isValid(final PosteTravail poste) {
        if (decompteSalaires.size() > 0) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_DECOMPTE_SALAIRE_EXISTANT);
        }
        return true;
    }
}
