package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRevenue;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public final class AutreRevenu extends DonneeFinanciere implements Revenu {
    private final Montant montant;
    private final String libelle;

    public AutreRevenu(Montant montant, String libelle, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");
        Checkers.checkNotNull(libelle, "libelle");

        this.libelle = libelle;

        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public String getLibelle() {
        return libelle;
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
        typeDonnneeFianciere = DonneeFinanciereType.AUTRE_REVENU;
    }

    @Override
    public String toString() {
        return "AutreRevenue [montant=" + montant + ", libelle=" + libelle + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
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
        AutreRevenu other = (AutreRevenu) obj;
        if (libelle == null) {
            if (other.libelle != null) {
                return false;
            }
        } else if (!libelle.equals(other.libelle)) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        return true;
    }

}
