package ch.globaz.vulpecula.domain.specifications.congepaye;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class CPPeriodeInActivitePoste extends AbstractSpecification<CongePaye> {

    @Override
    public boolean isValid(CongePaye congePaye) {
        PosteTravail posteTravail = congePaye.getPosteTravail();
        Annee anneeDebut = congePaye.getAnneeDebut();
        Annee anneeFin = congePaye.getAnneeFin();

        if (!posteTravail.isActifIn(anneeDebut) || !posteTravail.isActifIn(anneeFin)) {
            addMessage(SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, anneeDebut.getValue() + " - "
                    + anneeFin.getValue(), congePaye.getPeriodeActivitePoste().toString());
            return false;
        }
        return true;
    }
}
