package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilier;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;

public class BienImmobilierNonPrincipale extends BienImmobilier {
    private final Montant valeurVenale;
    private final Montant loyerEncaisse;
    private final Montant sousLocation;
    private final Montant valeurLocative;
    private final BienImmobilierHabitableType typeDeBien;

    public BienImmobilierNonPrincipale(Montant valeurVenale, Montant interetHypothecaire, Montant loyerEncaisse,
            Montant sousLocation, Montant valeurLocative, Montant dette, BienImmobilierHabitableType typeDeBien,
            Part part, ProprieteType proprieteType, DonneeFinanciere donneeFinanciere) {
        super(interetHypothecaire, dette, part, proprieteType, donneeFinanciere);

        this.typeDeBien = typeDeBien;

        this.valeurVenale = valeurVenale.addAnnuelPeriodicity();
        this.loyerEncaisse = loyerEncaisse.addAnnuelPeriodicity();
        this.sousLocation = sousLocation.addAnnuelPeriodicity();
        this.valeurLocative = valeurLocative.addAnnuelPeriodicity();
    }

    public Montant getValeurVenale() {
        return valeurVenale;
    }

    public Montant getLoyerEncaisse() {
        return loyerEncaisse;
    }

    public Montant getSousLocation() {
        return sousLocation;
    }

    public Montant getValeurLocative() {
        return valeurLocative;
    }

    public BienImmobilierHabitableType getTypeDeBien() {
        return typeDeBien;
    }

    public Montant computeValLocativePartPropriete() {
        if (proprieteType.isUsufruit() || proprieteType.isProprietaire()) {
            return valeurLocative.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeFortune() {
        if (proprieteType.isProprietaire()) {
            return valeurVenale.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeFortuneBrut() {
        return valeurVenale;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        if (!proprieteType.isNuProprietaire()) {
            return computeRevenuAnnuelBrut().multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        Montant sum = Montant.ZERO_ANNUEL;
        sum = sum.add(valeurLocative).add(sousLocation).add(loyerEncaisse);
        return sum.annualise();
    }

    @Override
    public String toString() {
        return "BienImmobilierNonPrincipale [valeurVenale=" + valeurVenale + ", interetHypothecaire="
                + interetHypothecaire + ", loyerEncaisse=" + loyerEncaisse + ", sousLocation=" + sousLocation
                + ", valeurLocative=" + valeurLocative + ", dette=" + dette + ", part=" + part + ", proprieteType="
                + proprieteType + ", typeDeBien=" + typeDeBien + "]";
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((dette == null) ? 0 : dette.hashCode());
        result = prime * result + ((interetHypothecaire == null) ? 0 : interetHypothecaire.hashCode());
        result = prime * result + ((loyerEncaisse == null) ? 0 : loyerEncaisse.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((proprieteType == null) ? 0 : proprieteType.hashCode());
        result = prime * result + ((sousLocation == null) ? 0 : sousLocation.hashCode());
        result = prime * result + ((typeDeBien == null) ? 0 : typeDeBien.hashCode());
        result = prime * result + ((valeurLocative == null) ? 0 : valeurLocative.hashCode());
        result = prime * result + ((valeurVenale == null) ? 0 : valeurVenale.hashCode());
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
        BienImmobilierNonPrincipale other = (BienImmobilierNonPrincipale) obj;
        if (dette == null) {
            if (other.dette != null) {
                return false;
            }
        } else if (!dette.equals(other.dette)) {
            return false;
        }
        if (interetHypothecaire == null) {
            if (other.interetHypothecaire != null) {
                return false;
            }
        } else if (!interetHypothecaire.equals(other.interetHypothecaire)) {
            return false;
        }
        if (loyerEncaisse == null) {
            if (other.loyerEncaisse != null) {
                return false;
            }
        } else if (!loyerEncaisse.equals(other.loyerEncaisse)) {
            return false;
        }
        if (part == null) {
            if (other.part != null) {
                return false;
            }
        } else if (!part.equals(other.part)) {
            return false;
        }
        if (proprieteType != other.proprieteType) {
            return false;
        }
        if (sousLocation == null) {
            if (other.sousLocation != null) {
                return false;
            }
        } else if (!sousLocation.equals(other.sousLocation)) {
            return false;
        }
        if (typeDeBien != other.typeDeBien) {
            return false;
        }
        if (valeurLocative == null) {
            if (other.valeurLocative != null) {
                return false;
            }
        } else if (!valeurLocative.equals(other.valeurLocative)) {
            return false;
        }
        if (valeurVenale == null) {
            if (other.valeurVenale != null) {
                return false;
            }
        } else if (!valeurVenale.equals(other.valeurVenale)) {
            return false;
        }
        return true;
    }

}
