package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilier;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BienImmobilierHabitableType;

public class BienImmobilierServantHabitationPrincipale extends BienImmobilier implements Depense {
    private final Montant valeurFiscale;
    private final Montant valeurLocative;
    private final Montant loyerEncaisse;
    private final Montant sousLocation;
    private final Integer nbPersonne;
    private final BienImmobilierHabitableType typeDeBien;

    public BienImmobilierServantHabitationPrincipale(Montant valeurFiscale, Montant valeurLocative,
            Montant interetHypothecaire, Montant loyerEncaisse, Montant sousLocation, Montant dette, Integer nbPersonne,
            BienImmobilierHabitableType typeDeBien, Part part, ProprieteType proprieteType,
            DonneeFinanciere donneeFinanciere) {
        super(interetHypothecaire, dette, part, proprieteType, donneeFinanciere);
        this.nbPersonne = nbPersonne;
        this.typeDeBien = typeDeBien;

        this.valeurFiscale = valeurFiscale.addAnnuelPeriodicity();
        this.valeurLocative = valeurLocative.addAnnuelPeriodicity();
        this.loyerEncaisse = loyerEncaisse.addAnnuelPeriodicity();
        this.sousLocation = sousLocation.addAnnuelPeriodicity();
    }

    public Montant getValeurFiscale() {
        return valeurFiscale;
    }

    public Montant getValeurLocative() {
        return valeurLocative;
    }

    public Montant getLoyerEncaisse() {
        return loyerEncaisse;
    }

    public Montant getSousLocation() {
        return sousLocation;
    }

    public Integer getNbPersonne() {
        return nbPersonne;
    }

    public Montant computeValLocativePartPropriete() {
        if (proprieteType.isUsufruit() || proprieteType.isProprietaire()) {
            return valeurLocative.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    public Montant computeValLocativePartProprieteEtCoPropiete() {
        if (proprieteType.isProprietaire() || proprieteType.isCoProprietaire()) {
            return valeurLocative.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    public Montant computeValLocativeDH_RPC() {
        if (proprieteType.isUsufruit() || proprieteType.isDroitHabitation()) {
            return valeurLocative.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    public Montant computeLoyersEnCaissesPartPropriete() {
        if (proprieteType.isUsufruit() || proprieteType.isProprietaire()) {
            return loyerEncaisse.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    public Montant computeSousLocationPartPropriete() {
        if (proprieteType.isUsufruit() || proprieteType.isProprietaire()) {
            return sousLocation.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        Montant sum = Montant.ZERO_ANNUEL;
        if (proprieteType.isUsufruit() || proprieteType.isProprietaire()) {
            sum = sum.add(sousLocation).add(loyerEncaisse);
            sum = sum.multiply(part);
        }
        return sum.annualise();
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        Montant sum = Montant.ZERO_ANNUEL;
        sum = sum.add(sousLocation).add(loyerEncaisse).add(valeurLocative);
        return sum.annualise();
    }

    @Override
    public Montant computeFortuneBrut() {
        return valeurFiscale;
    }

    @Override
    public Montant computeDepense() {
        if (!proprieteType.isNuProprietaire()) {
            return valeurLocative.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeFortune() {
        if (proprieteType.isProprietaire()) {
            return valeurFiscale.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE;
    }

    @Override
    public String toString() {
        return "BienImmobilierServantHbitationPrincipale [valeurFiscale=" + valeurFiscale + ", valeurLocative="
                + valeurLocative + ", interetHypothecaire=" + interetHypothecaire + ", loyerEncaisse=" + loyerEncaisse
                + ", sousLocation=" + sousLocation + ", dette=" + dette + ", nbPersonne=" + nbPersonne + ", part="
                + part + ", proprieteType=" + proprieteType + ", typeDeBien=" + typeDeBien + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((dette == null) ? 0 : dette.hashCode());
        result = prime * result + ((interetHypothecaire == null) ? 0 : interetHypothecaire.hashCode());
        result = prime * result + ((loyerEncaisse == null) ? 0 : loyerEncaisse.hashCode());
        result = prime * result + ((nbPersonne == null) ? 0 : nbPersonne.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((proprieteType == null) ? 0 : proprieteType.hashCode());
        result = prime * result + ((sousLocation == null) ? 0 : sousLocation.hashCode());
        result = prime * result + ((valeurFiscale == null) ? 0 : valeurFiscale.hashCode());
        result = prime * result + ((valeurLocative == null) ? 0 : valeurLocative.hashCode());
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
        BienImmobilierServantHabitationPrincipale other = (BienImmobilierServantHabitationPrincipale) obj;
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
        if (nbPersonne == null) {
            if (other.nbPersonne != null) {
                return false;
            }
        } else if (!nbPersonne.equals(other.nbPersonne)) {
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
        if (valeurFiscale == null) {
            if (other.valeurFiscale != null) {
                return false;
            }
        } else if (!valeurFiscale.equals(other.valeurFiscale)) {
            return false;
        }
        if (valeurLocative == null) {
            if (other.valeurLocative != null) {
                return false;
            }
        } else if (!valeurLocative.equals(other.valeurLocative)) {
            return false;
        }
        return true;
    }

}
