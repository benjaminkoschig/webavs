package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class DecompteSalaireInPeriodeActivitePoste extends AbstractSpecification<DecompteSalaire> {

    @Override
    public boolean isValid(DecompteSalaire decompteSalaire) {
        Periode periodeActivitePoste = decompteSalaire.getPeriodeActivitePoste();
        if (!periodeActivitePoste.contains(decompteSalaire.getPeriode())) {
            addMessage(SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, decompteSalaire.getPeriode()
                    .toString(), decompteSalaire.getPeriodeActivitePoste().toString());
            return false;
        }
        return true;
    }
}
