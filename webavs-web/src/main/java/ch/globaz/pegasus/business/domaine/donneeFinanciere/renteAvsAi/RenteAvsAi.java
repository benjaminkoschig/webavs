package ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class RenteAvsAi extends DonneeFinanciere implements Revenu {

    private Montant montant;
    private RenteAvsAiType typeRente;
    private TypeSansRente typeSansRente;
    private Float degreInvalidite;

    public RenteAvsAi(Montant montant, RenteAvsAiType typeRente, TypeSansRente typeSansRente, Float degreInvalidite,
            DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.typeRente = typeRente;
        this.typeSansRente = typeSansRente;

        this.montant = montant.addMensuelPeriodicity();
        this.degreInvalidite = degreInvalidite;
    }

    public Montant getMontant() {
        return montant;
    }

    public RenteAvsAiType getTypeRente() {
        return typeRente;
    }

    public TypeSansRente getTypeSansRente() {
        return typeSansRente;
    }

    public Float getDegreInvalidite() {
        return degreInvalidite;
    }

    public boolean isSansRente() {
        return !typeSansRente.isIndefinit();
    }

    @Override
    public Montant computeRevenuAnnuel() {
        throw new RuntimeException("not yet implmented");
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant.annualise();
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.RENTE_AVS_AI;
    }

    @Override
    public String toString() {
        return "RenteAvsAi [montant=" + montant + ", typeRente=" + typeRente + ", typeSansRente=" + typeSansRente
                + ", parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((typeRente == null) ? 0 : typeRente.hashCode());
        result = prime * result + ((typeSansRente == null) ? 0 : typeSansRente.hashCode());
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
        RenteAvsAi other = (RenteAvsAi) obj;
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (typeRente != other.typeRente) {
            return false;
        }
        if (typeSansRente != other.typeSansRente) {
            return false;
        }
        return true;
    }

}
