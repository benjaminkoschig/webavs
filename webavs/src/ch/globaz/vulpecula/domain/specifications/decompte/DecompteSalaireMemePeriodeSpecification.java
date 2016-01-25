package ch.globaz.vulpecula.domain.specifications.decompte;

import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class DecompteSalaireMemePeriodeSpecification extends AbstractSpecification<DecompteSalaire> {
    private List<DecompteSalaire> salairesMemePeriode;

    public DecompteSalaireMemePeriodeSpecification(List<DecompteSalaire> salairesMemePeriode) {
        this.salairesMemePeriode = salairesMemePeriode;
    }

    @Override
    public boolean isValid(DecompteSalaire t) {
        if (!salairesMemePeriode.isEmpty()) {
            addMessage(SpecificationMessage.DECOMPTE_SALAIRE_DEJA_EXISTANT_TRAVAILLEUR_MEME_PERIODE);
        }
        return true;
    }

}
