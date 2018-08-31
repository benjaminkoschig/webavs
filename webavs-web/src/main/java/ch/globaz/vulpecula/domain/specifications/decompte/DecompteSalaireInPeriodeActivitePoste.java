package ch.globaz.vulpecula.domain.specifications.decompte;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class DecompteSalaireInPeriodeActivitePoste extends AbstractSpecification<DecompteSalaire> {

    @Override
    public boolean isValid(DecompteSalaire decompteSalaire) {
        Date dateDebut = decompteSalaire.getPeriodeActivitePoste().getDateDebut().getFirstDayOfMonth();
        Date dateFin = null;
        if (decompteSalaire.getPeriodeActivitePoste().getDateFin() != null) {
            dateFin = decompteSalaire.getPeriodeActivitePoste().getDateFin().getLastDayOfMonth();
        }

        Periode periodeActivitePoste = new Periode(dateDebut, dateFin);

        // 1st case: Poste de travail contiens la periode de decompte
        // 2nd case: Le poste de travail est contenue dans la periode
        // 3rd case: La date de fin du poste est contenue dans la periode
        // 4th case: La date de debut du poste est contenue dans la periode
        if (periodeActivitePoste.contains(decompteSalaire.getPeriode())
                || decompteSalaire.getPeriode().contains(periodeActivitePoste)
                || decompteSalaire.getPeriode().contains(dateDebut)
                || (dateFin != null && decompteSalaire.getPeriode().contains(dateFin))) {
            return true;
        } else {
            addMessage(SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, decompteSalaire.getPeriode()
                    .toString(), decompteSalaire.getPeriodeActivitePoste().toString());
            return false;
        }
    }
}
