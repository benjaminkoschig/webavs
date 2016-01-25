package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

public class DecompteCTSPInteretsRequis extends AbstractSpecification<Decompte> {

    @Override
    public boolean isValid(Decompte decompte) {
        if (TypeDecompte.CONTROLE_EMPLOYEUR.equals(decompte.getType())
                || TypeDecompte.SPECIAL.equals(decompte.getType())) {
            if (decompte.getInteretsMoratoires() == null) {
                addMessage(SpecificationMessage.DECOMPTE_CT_ST_INTERETS_REQUIS);
            }
        }
        return true;
    }

}
