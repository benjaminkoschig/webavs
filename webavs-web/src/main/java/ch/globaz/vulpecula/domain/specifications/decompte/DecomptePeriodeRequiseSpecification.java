package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

/**
 * @author Jonas Paratte (JPA) | Créé le 27.03.2014
 * 
 */
public class DecomptePeriodeRequiseSpecification extends AbstractSpecification<Decompte> {

    @Override
    public boolean isValid(Decompte decompte) {
        if (decompte.getPeriode() == null) {
            addMessage(SpecificationMessage.DECOMPTE_PERIODE_REQUISE);
        }
        return true;
    }

}
