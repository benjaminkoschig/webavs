package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

/**
 * @author Jonas Paratte (JPA) | Créé le 27.03.2014
 * 
 */
public class DecompteMotifProlongationRequisSpecification extends AbstractSpecification<Decompte> {

    private Decompte ancienDecompte = null;

    public DecompteMotifProlongationRequisSpecification(Decompte ancienDecompte) {
        this.ancienDecompte = ancienDecompte;
    }

    @Override
    public boolean isValid(Decompte decompte) {
        if (ancienDecompte.getDateRappel() != null && !ancienDecompte.getDateRappel().equals(decompte.getDateRappel())) {
            // Dans ce cas, le motif doit être renseigné
            if (decompte.getMotifProlongation() == null || decompte.getMotifProlongation().getValue().length() <= 0
                    || decompte.getMotifProlongation().getValue().equals("0")) {
                addMessage(SpecificationMessage.DECOMPTE_MOTIF_PROLONGATION_REQUIS);
            }
        }
        return true;
    }
}
