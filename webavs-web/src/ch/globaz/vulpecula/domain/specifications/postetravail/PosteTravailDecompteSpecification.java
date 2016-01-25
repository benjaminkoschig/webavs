package ch.globaz.vulpecula.domain.specifications.postetravail;

import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailDecompteSpecification extends AbstractSpecification<PosteTravail> {
    public List<DecompteSalaire> decomptes = null;

    public PosteTravailDecompteSpecification(final List<DecompteSalaire> decomptes) {
        this.decomptes = decomptes;
    }

    @Override
    public boolean isValid(final PosteTravail poste) {

        // On récupère la date la plus grande et la plus petite afin de contrôler que le poste soit dans cette
        // fourchette.
        Date dateDebutPlusPetite = null;
        Date dateFinPlusGrande = null;

        for (DecompteSalaire decompteSalaire : decomptes) {
            if (!decompteSalaire.getDecompte().getEtat().getValue().equals(EtatDecompte.ANNULE.getValue())) {
                if (decompteSalaire.getPeriode().getDateDebut() != null) {
                    if (dateDebutPlusPetite != null) {
                        if (decompteSalaire.getPeriode().getDateDebut().before(dateDebutPlusPetite)) {
                            dateDebutPlusPetite = decompteSalaire.getPeriode().getDateDebut();
                        }
                    } else {
                        dateDebutPlusPetite = decompteSalaire.getPeriode().getDateDebut();
                    }
                    if (dateFinPlusGrande != null) {
                        if (decompteSalaire.getPeriode().getDateFin().after(dateFinPlusGrande)) {
                            dateFinPlusGrande = decompteSalaire.getPeriode().getDateFin();
                        }
                    } else {
                        dateFinPlusGrande = decompteSalaire.getPeriode().getDateFin();
                    }
                }
            }
        }

        if (dateDebutPlusPetite != null) {
            if (dateDebutPlusPetite.getYear() < poste.getDebutActivite().getYear()) {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_DECOMPTE_EXISTANT_HORS_NOUVELLE_PERIODE);
            }
        }
        if (dateFinPlusGrande != null && poste.getFinActivite() != null) {
            if (poste.getFinActivite().getYear() < dateFinPlusGrande.getYear()) {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_DECOMPTE_EXISTANT_HORS_NOUVELLE_PERIODE);
            }
        }
        return true;
    }
}
