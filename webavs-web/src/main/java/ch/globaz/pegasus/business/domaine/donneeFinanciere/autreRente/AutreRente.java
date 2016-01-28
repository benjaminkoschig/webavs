package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;

public class AutreRente extends DonneeFinanciere implements Revenu {

    private final Montant montant;
    private final AutreRenteType autreRenteType;
    private final AutreRenteGenre autreRenteGenre;
    private final String libelleAutreRente;
    private final MonnaieEtrangereType monnaieType;

    public AutreRente(Montant montant, AutreRenteType autreRenteType, AutreRenteGenre autreRenteGenre,
            String libelleAutreRente, MonnaieEtrangereType monnaieType, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");
        Checkers.checkNotNull(autreRenteType, "autreRenteType");
        Checkers.checkNotNull(autreRenteGenre, "autreRenteGenre");
        Checkers.checkNotNull(libelleAutreRente, "libelleAutreRente");
        Checkers.checkNotNull(monnaieType, "monnaieType");

        this.autreRenteType = autreRenteType;
        this.autreRenteGenre = autreRenteGenre;
        this.libelleAutreRente = libelleAutreRente;
        this.monnaieType = monnaieType;

        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public AutreRenteType getAutreRenteType() {
        return autreRenteType;
    }

    public MonnaieEtrangereType getMonnaieType() {
        return monnaieType;
    }

    public AutreRenteGenre getAutreRenteGenre() {
        return autreRenteGenre;
    }

    public String getLibelleAutreRente() {
        return libelleAutreRente;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant.annualise();
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.annualise();
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.AUTRE_RENTE;
    }

    public boolean mustConvertToFrancSuisse() {
        return !getMonnaieType().isFrancSuisse() && getAutreRenteGenre().isRenteEtrangere();
    }

    @Override
    public String toString() {
        return "AutreRente [montant=" + montant + ", autreRenteType=" + autreRenteType + ", autreRenteGenre="
                + autreRenteGenre + ", libelleAutreRente=" + libelleAutreRente + ", monnaieType=" + monnaieType + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((autreRenteGenre == null) ? 0 : autreRenteGenre.hashCode());
        result = prime * result + ((autreRenteType == null) ? 0 : autreRenteType.hashCode());
        result = prime * result + ((libelleAutreRente == null) ? 0 : libelleAutreRente.hashCode());
        result = prime * result + ((monnaieType == null) ? 0 : monnaieType.hashCode());
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
        AutreRente other = (AutreRente) obj;
        if (autreRenteGenre != other.autreRenteGenre) {
            return false;
        }
        if (autreRenteType != other.autreRenteType) {
            return false;
        }
        if (libelleAutreRente == null) {
            if (other.libelleAutreRente != null) {
                return false;
            }
        } else if (!libelleAutreRente.equals(other.libelleAutreRente)) {
            return false;
        }
        if (monnaieType == null) {
            if (other.monnaieType != null) {
                return false;
            }
        } else if (!monnaieType.equals(other.monnaieType)) {
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
