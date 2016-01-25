package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class DecompteSalairePeriodeMemeAnnee extends AbstractSpecification<DecompteSalaire> {

    @Override
    public boolean isValid(DecompteSalaire decompteSalaire) {
        if (!decompteSalaire.getPeriode().isSameYear()) {
            addMessage(SpecificationMessage.DECOMPTE_SALAIRE_PAS_MEME_ANNEE);
        }
        return true;
    }

}
