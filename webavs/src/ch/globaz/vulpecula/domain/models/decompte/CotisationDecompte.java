package ch.globaz.vulpecula.domain.models.decompte;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class CotisationDecompte implements DomainEntity {
    private String id;
    private Cotisation cotisation;
    private Taux taux;
    private Montant masse;
    private String spy;
    private boolean masseForcee;

    public CotisationDecompte() {

    }

    public CotisationDecompte(AdhesionCotisationPosteTravail adhesion) {
        cotisation = adhesion.getCotisation();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public Cotisation getCotisation() {
        return cotisation;
    }

    public void setCotisation(final Cotisation cotisation) {
        this.cotisation = cotisation;
    }

    public String getTauxAsValue() {
        if (taux != null) {
            return taux.getValue();
        }
        return null;
    }

    public Taux getTaux() {
        return taux;
    }

    public void setTaux(final Taux taux) {
        this.taux = taux;
    }

    public TypeAssurance getTypeAssurance() {
        if (cotisation == null) {
            return null;
        }
        return cotisation.getTypeAssurance();
    }

    public String getPlanCaisseLibelle() {
        if (cotisation == null) {
            return null;
        }
        return cotisation.getPlanCaisseLibelle();
    }

    public Assurance getAssurance() {
        return cotisation.getAssurance();
    }

    public String getAssuranceLibelle(final Locale userLocale) {
        if (cotisation == null) {
            return null;
        }
        return cotisation.getAssuranceLibelle(userLocale);
    }

    public String getCotisationAsValue(Montant value) {
        if (masseForcee) {
            return masse.multiply(taux).getValueNormalisee();
        }
        return value.multiply(taux).getValueNormalisee();
    }

    public Montant getMasse(Montant salaireTotal) {
        if (masseForcee) {
            if (masse == null) {
                return Montant.ZERO;
            }
            return masse;
        }
        return salaireTotal;
    }

    public Montant getDifference(Montant salaireTotal) {
        if (masseForcee) {
            return salaireTotal.substract(masse);
        }
        return Montant.ZERO;
    }

    public Montant getMasse() {
        return masse;
    }

    public void setMasse(Montant masse) {
        this.masse = masse;
    }

    public boolean getMasseForcee() {
        return masseForcee;
    }

    public void setMasseForcee(boolean masseForcee) {
        this.masseForcee = masseForcee;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(final String spy) {
        this.spy = spy;
    }

    /**
     * Retourne si la cotisation décompte est une assurance de type rentier. Soit une assurance où l'on a le droit à une
     * déduction. Le montant de la réduction dépend d'une propriété en base de données.
     * 
     * @return true si assurance avec réduction rentier
     */
    public boolean isAssuranceWithReductionRentier() {
        return getAssurancesWithReductionRentier().contains(cotisation.getTypeAssurance());
    }

    /**
     * Retourne la liste des assurances qui possèdent une réduction pour les rentiers.
     * 
     * @return Liste d'assurances
     */
    public static List<TypeAssurance> getAssurancesWithReductionRentier() {
        return Arrays.asList(TypeAssurance.COTISATION_AVS_AI, TypeAssurance.COTISATION_AF,
                TypeAssurance.COTISATION_FFPP_MASSE, TypeAssurance.COTISATION_FFPP_CAPITATION,
                TypeAssurance.FRAIS_ADMINISTRATION);
    }

    public boolean isAssuranceAC2() {
        return TypeAssurance.COTISATION_AC2.equals(cotisation.getTypeAssurance());
    }

    public boolean isAssuranceAC() {
        return TypeAssurance.ASSURANCE_CHOMAGE.equals(cotisation.getTypeAssurance());
    }

    @Override
    public String toString() {
        return "CotisationDecompte [id=" + id + ", cotisation=" + cotisation + ", taux=" + taux + ", masse=" + masse
                + ", spy=" + spy + ", masseForcee=" + masseForcee + "]";
    }
}
