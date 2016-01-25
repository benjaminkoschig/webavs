package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

public class DecompteCTNumeroRequis extends AbstractSpecification<Decompte> {

    @Override
    public boolean isValid(Decompte d) {
        if (TypeDecompte.CONTROLE_EMPLOYEUR.equals(d.getType())) {
            if (d.getNumeroDecompte() == null) {
                addMessage(SpecificationMessage.DECOMPTE_CT_NUMERO_REQUIS);
            } else if (!d.getNumeroDecompte().getCode().equals(NumeroDecompte.CONTROLE_EMPLOYEUR)) {
                addMessage(SpecificationMessage.DECOMPTE_CT_MAUVAIS_CODE, NumeroDecompte.CONTROLE_EMPLOYEUR);
            }
        }
        return true;
    }

}
