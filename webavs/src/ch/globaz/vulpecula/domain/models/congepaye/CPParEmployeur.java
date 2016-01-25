package ch.globaz.vulpecula.domain.models.congepaye;

import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.prestations.PrestationParEmployeur;

public class CPParEmployeur implements PrestationParEmployeur {
    private final Employeur employeur;
    private final CongesPayes congesPayes;

    public CPParEmployeur(Employeur employeur, CongesPayes congesPayes) {
        this.employeur = employeur;
        this.congesPayes = congesPayes;
    }

    @Override
    public Employeur getEmployeur() {
        return employeur;
    }

    public CongesPayes getCongesPayes() {
        return congesPayes;
    }
}
