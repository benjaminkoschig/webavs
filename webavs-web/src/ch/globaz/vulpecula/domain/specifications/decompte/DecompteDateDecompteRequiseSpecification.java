package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

/**
 * @author Jonas Paratte (JPA) | Créé le 27.03.2014
 * 
 */
public class DecompteDateDecompteRequiseSpecification extends AbstractSpecification<Decompte> {

    @Override
    public boolean isValid(Decompte decompte) {
        if (decompte.getDateEtablissement() == null) {
            addMessage(SpecificationMessage.DECOMPTE_DATE_DECOMPTE_REQUISE);
        } else {
            try {
                new Date(decompte.getDateEtablissement().getValue());
            } catch (Exception e) {
                addMessage(SpecificationMessage.DECOMPTE_DATE_DECOMPTE_REQUISE);
            }
        }
        return true;
    }

}
