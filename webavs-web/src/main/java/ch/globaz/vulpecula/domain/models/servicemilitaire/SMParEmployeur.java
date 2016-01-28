package ch.globaz.vulpecula.domain.models.servicemilitaire;

import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.prestations.PrestationParEmployeur;

public class SMParEmployeur implements PrestationParEmployeur {
    private final Employeur employeur;
    private final ServicesMilitaires servicesMilitaires;

    public SMParEmployeur(Employeur employeur, ServicesMilitaires servicesMilitaires) {
        this.employeur = employeur;
        this.servicesMilitaires = servicesMilitaires;
    }

    @Override
    public Employeur getEmployeur() {
        return employeur;
    }

    public ServicesMilitaires getServicesMilitaires() {
        return servicesMilitaires;
    }
}
