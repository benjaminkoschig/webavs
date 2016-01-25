package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonPrincipale;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BiensImmobiliers;

public class BiensImmobiliersNonPrincipale extends
        BiensImmobiliers<BienImmobilierNonPrincipale, BiensImmobiliersNonPrincipale> {

    public BiensImmobiliersNonPrincipale() {
        super(BiensImmobiliersNonPrincipale.class);
    }

    public Montant sumMontantLoyerEncaisse() {
        return this.sum(new Each<BienImmobilierNonPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierNonPrincipale donnneeFianciere) {
                return donnneeFianciere.getLoyerEncaisse();
            }
        });

    }

    public Montant sumInteretHypotecaire() {
        return this.sum(new Each<BienImmobilierNonPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierNonPrincipale donnneeFianciere) {
                return donnneeFianciere.computeInteret();
            }
        });
    }

    public Montant sumMontantValeurLocativePartPropriete() {
        return this.sum(new Each<BienImmobilierNonPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierNonPrincipale donnneeFianciere) {
                return donnneeFianciere.computeValLocativePartPropriete();
            }
        });
    }

    public Montant sumMontantValeurLocativePartPropriete(ProprieteType type) {
        return getBiensImmobiliersByProprieteType(type).sum(new Each<BienImmobilierNonPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierNonPrincipale donnneeFianciere) {
                return donnneeFianciere.computeValLocativePartPropriete();
            }
        });
    }

    public Montant sumMontantValeurLocative() {
        return this.sum(new Each<BienImmobilierNonPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierNonPrincipale donnneeFianciere) {
                return donnneeFianciere.getValeurLocative();
            }
        });
    }

    public Montant sumMontantValeurLocative(ProprieteType type) {
        return getBiensImmobiliersByProprieteType(type).sum(new Each<BienImmobilierNonPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierNonPrincipale donnneeFianciere) {
                return donnneeFianciere.getValeurLocative();
            }
        });
    }

    public Montant sumSousLocation() {
        return this.sum(new Each<BienImmobilierNonPrincipale>() {

            @Override
            public Montant getMontant(BienImmobilierNonPrincipale donnneeFianciere) {
                return donnneeFianciere.getSousLocation();
            }
        });
    }

}
