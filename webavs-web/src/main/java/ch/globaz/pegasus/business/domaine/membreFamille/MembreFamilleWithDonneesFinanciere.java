package ch.globaz.pegasus.business.domaine.membreFamille;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;

public class MembreFamilleWithDonneesFinanciere {
    private final MembreFamille famille;
    private DonneesFinancieresContainer donneesFinancieres;

    public MembreFamilleWithDonneesFinanciere(MembreFamille famille, DonneesFinancieresContainer donneesFinancieres) {
        this.famille = famille;
        this.donneesFinancieres = donneesFinancieres;
    }

    public MembreFamille getFamille() {
        return famille;
    }

    public DonneesFinancieresContainer getDonneesFinancieres() {
        return donneesFinancieres;
    }

    public RoleMembreFamille getRoleMembreFamille() {
        return famille.getRoleMembreFamille();
    }

    public boolean isRequerant() {
        return famille.getRoleMembreFamille().isRequerant();
    }

    public boolean isEnfant() {
        return famille.getRoleMembreFamille().isEnfant();
    }

    public boolean isConjoint() {
        return famille.getRoleMembreFamille().isConjoint();
    }

    public MembreFamilleWithDonneesFinanciere filtreForPeriode(Date debut, Date fin) {
        return new MembreFamilleWithDonneesFinanciere(famille, donneesFinancieres.filtreForPeriode(debut, fin));
    }
}
