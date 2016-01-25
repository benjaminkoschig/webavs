package ch.globaz.vulpecula.domain.models.decompte;

import java.util.Locale;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;

/**
 * @author Arnaud Geiser (AGE) | Créé le 14 mai 2014
 * 
 */
public class CotisationCalculee {
    private final Cotisation cotisation;
    private final Taux taux;
    private final Montant montant;
    private final Annee annee;

    public CotisationCalculee(final Cotisation cotisation, final Montant montant, final Taux taux, final Annee annee) {
        this.cotisation = cotisation;
        this.taux = taux;
        this.montant = montant;
        this.annee = annee;
    }

    public Cotisation getCotisation() {
        return cotisation;
    }

    public Taux getTaux() {
        return taux;
    }

    public Montant getMontant() {
        return montant;
    }

    public String getMontantAsValue() {
        return montant.getValue();
    }

    public String getAssuranceId() {
        return cotisation.getAssuranceId();
    }

    public Montant getMontantCalculee() {
        return montant.multiply(taux);
    }

    public Annee getAnnee() {
        return annee;
    }

    /**
     * Retourne la cotisation calculée arrondie à 5 centimes.
     * 
     * @return String représentant la cotisation calculée
     */
    public String getCotisationCalculee() {
        return getMontantCalculee().getValueNormalisee();
    }

    public String getTauxAsValue() {
        return taux.getValue();
    }

    /**
     * Retourne le plan caisse lié à la cotisation.
     * 
     * @return Le plan caisse lié à la cotisation ou null si inextante
     */
    public PlanCaisse getPlanCaisse() {
        if (cotisation == null) {
            return null;
        }
        return cotisation.getPlanCaisse();
    }

    /**
     * Retourne le libellé de la cotisation.
     * 
     * @return Le libellé de la cotisation ou null
     */
    public String getCotisationLibelle(final Locale locale) {
        if (cotisation == null) {
            return null;
        }
        return cotisation.getAssuranceLibelle(locale);
    }

    /**
     * Retourne si la cotisation calculée passée en paramètre dispose de la même assurance et du même taux que la
     * cotisation calculée courante.
     * 
     * @param cotisationCalculee Cotisation calculée à comparer
     * @return true si même assurance et même taux, false dans le cas contraire
     */
    public boolean hasSameAssuranceAndTaux(final CotisationCalculee cotisationCalculee) {
        return taux.equals(cotisationCalculee.getTaux())
                && getAssuranceId().equals(cotisationCalculee.getAssuranceId());
    }

    /**
     * Retourne si la cotisation calculée passée en paramètre dispose de la même assurance, du même taux et de la même
     * année que la
     * cotisation calculée courante.
     * 
     * @param cotisationCalculee Cotisation calculée à comparer
     * @return true si même assurance, même taux, même année , false dans le cas contraire
     */
    public boolean hasSameAssuranceTauxAndAnnee(final CotisationCalculee cotisationCalculee) {
        return taux.equals(cotisationCalculee.getTaux())
                && getAssuranceId().equals(cotisationCalculee.getAssuranceId())
                && annee.equals(cotisationCalculee.getAnnee());
    }

    /**
     * Additionne le montant de la cotisation calculée si identique
     * 
     * @param cotisationCalculee Cotisation calculée à ajouter
     * @return Nouvelle cotisation calculée avec l'addition des deux montants et le même taux
     */
    public CotisationCalculee add(final CotisationCalculee cotisationCalculee) {
        return new CotisationCalculee(cotisation, montant.add(cotisationCalculee.getMontant()), taux,
                cotisationCalculee.annee);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annee == null) ? 0 : annee.hashCode());
        result = prime * result + ((cotisation == null) ? 0 : cotisation.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((taux == null) ? 0 : taux.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CotisationCalculee other = (CotisationCalculee) obj;
        if (annee == null) {
            if (other.annee != null) {
                return false;
            }
        } else if (!annee.equals(other.annee)) {
            return false;
        }
        if (cotisation == null) {
            if (other.cotisation != null) {
                return false;
            }
        } else if (!cotisation.getAssuranceId().equals(other.cotisation.getAssuranceId())) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (taux == null) {
            if (other.taux != null) {
                return false;
            }
        } else if (!taux.equals(other.taux)) {
            return false;
        }
        return true;
    }

    /**
     * @return le type d'assurance
     */
    public TypeAssurance getTypeAssurance() {
        return getCotisation().getAssurance().getTypeAssurance();
    }

    /** Retourne la rubrique comptable liée à l'assurance */
    public String getIdRubrique() {
        return getCotisation().getAssurance().getRubriqueId();
    }
}
