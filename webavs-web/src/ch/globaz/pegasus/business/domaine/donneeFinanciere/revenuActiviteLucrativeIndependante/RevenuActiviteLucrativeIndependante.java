package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenuActiviteLucrativeIndependante;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class RevenuActiviteLucrativeIndependante extends DonneeFinanciere implements Revenu {
    private final Montant montant;
    private final RevenuActiviteLucrativeIndependanteGenreRevenu revenuActiviteLucrativeIndependanteGenreRevenu;

    public RevenuActiviteLucrativeIndependante(Montant montant,
            RevenuActiviteLucrativeIndependanteGenreRevenu revenuActiviteLucrativeIndependanteGenreRevenu,
            DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.revenuActiviteLucrativeIndependanteGenreRevenu = revenuActiviteLucrativeIndependanteGenreRevenu;

        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public RevenuActiviteLucrativeIndependanteGenreRevenu getRevenuActiviteLucrativeIndependanteGenreRevenu() {
        return revenuActiviteLucrativeIndependanteGenreRevenu;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE;
    }

    @Override
    public String toString() {
        return "RevenuActiviteLucrativeIndependante [montant=" + montant
                + ", revenuActiviteLucrativeIndependanteGenreRevenu=" + revenuActiviteLucrativeIndependanteGenreRevenu
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime
                * result
                + ((revenuActiviteLucrativeIndependanteGenreRevenu == null) ? 0
                        : revenuActiviteLucrativeIndependanteGenreRevenu.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RevenuActiviteLucrativeIndependante other = (RevenuActiviteLucrativeIndependante) obj;
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (revenuActiviteLucrativeIndependanteGenreRevenu != other.revenuActiviteLucrativeIndependanteGenreRevenu) {
            return false;
        }
        return true;
    }

}
