package ch.globaz.vulpecula.domain.models.absencejustifiee;

import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.prestations.PrestationParEmployeur;

public class AJParEmployeur implements PrestationParEmployeur {
    private static final long serialVersionUID = 5900356780645438485L;

    private final Employeur employeur;
    private final AbsencesJustifiees absencesJustifiees;

    public AJParEmployeur(Employeur employeur, AbsencesJustifiees absencesJustifiees) {
        this.employeur = employeur;
        this.absencesJustifiees = absencesJustifiees;
    }

    @Override
    public Employeur getEmployeur() {
        return employeur;
    }

    public AbsencesJustifiees getAbsencesJustifiees() {
        return absencesJustifiees;
    }
}
