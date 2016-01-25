package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilier;

public class BienImmobilierNonHabitable extends BienImmobilier implements Depense {
    private final Montant rendement;
    private final Montant valeurVenale;
    private final BienImmobilierNonHabitableType typeDeBien;

    public BienImmobilierNonHabitable(Montant rendement, Montant valeurVenale, Montant interetHypothecaire,
            Montant dette, BienImmobilierNonHabitableType typeDeBien, Part part, ProprieteType proprieteType,
            DonneeFinanciere donneeFinanciere) {
        super(interetHypothecaire, dette, part, proprieteType, donneeFinanciere);

        this.typeDeBien = typeDeBien;

        this.rendement = rendement.addAnnuelPeriodicity();
        this.valeurVenale = valeurVenale.addAnnuelPeriodicity();
    }

    public Montant getRendement() {
        return rendement;
    }

    public Montant getValeurVenale() {
        return valeurVenale;
    }

    public BienImmobilierNonHabitableType getTypeDeBien() {
        return typeDeBien;
    }

    public Montant computeRendementPartPropriete() {
        if (proprieteType.isUsufruit() || proprieteType.isProprietaire()) {
            return rendement.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeFortuneBrut() {
        return valeurVenale;
    }

    @Override
    public Montant computeFortune() {
        if (proprieteType.isProprietaire()) {
            return valeurVenale.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return rendement;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        if (!proprieteType.isNuProprietaire()) {
            return rendement.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeDepense() {
        if (!proprieteType.isNuProprietaire()) {
            return interetHypothecaire;
        } else {
            return Montant.ZERO_ANNUEL;
        }
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.BIEN_IMMOBILIER_NON_HABITABLE;
    }

    @Override
    public String toString() {
        return "BienImmobilierNonHabitable [rendement=" + rendement + ", valeurVeanle=" + valeurVenale
                + ", interetHypothecaire=" + interetHypothecaire + ", dette=" + dette + ", part=" + part
                + ", proprieteType=" + proprieteType + ", typeDeBien=" + typeDeBien + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((dette == null) ? 0 : dette.hashCode());
        result = prime * result + ((interetHypothecaire == null) ? 0 : interetHypothecaire.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((proprieteType == null) ? 0 : proprieteType.hashCode());
        result = prime * result + ((rendement == null) ? 0 : rendement.hashCode());
        result = prime * result + ((typeDeBien == null) ? 0 : typeDeBien.hashCode());
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
        BienImmobilierNonHabitable other = (BienImmobilierNonHabitable) obj;
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
        if (rendement == null) {
            if (other.rendement != null) {
                return false;
            }
        } else if (!rendement.equals(other.rendement)) {
            return false;
        }
        if (typeDeBien != other.typeDeBien) {
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
