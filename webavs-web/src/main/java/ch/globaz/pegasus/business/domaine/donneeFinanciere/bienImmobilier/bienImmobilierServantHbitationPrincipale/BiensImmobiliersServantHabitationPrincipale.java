package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierServantHbitationPrincipale;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BiensImmobiliers;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BiensImmobiliersList;

public class BiensImmobiliersServantHabitationPrincipale
        extends BiensImmobiliers<BienImmobilierServantHabitationPrincipale, BiensImmobiliersServantHabitationPrincipale>
        implements BiensImmobiliersList {

    public BiensImmobiliersServantHabitationPrincipale() {
        super(BiensImmobiliersServantHabitationPrincipale.class);
    }

    @Override
    public Montant sumMontantLoyerEncaisse() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.getLoyerEncaisse();
            }
        });

    }

    @Override
    public Montant sumMontantValeurLocative() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.getValeurLocative();
            }
        });
    }

    @Override
    public Montant sumMontantValeurLocative(ProprieteType type) {

        return filtreByProprieteType(type).sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.getValeurLocative();
            }
        });
    }

    @Override
    public Montant sumInteretHypothecaire() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeInteret();
            }
        });
    }

    @Override
    public Montant sumMontantValeurLocativePartPropriete() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeValLocativePartPropriete();
            }
        });
    }

    @Override
    public Montant sumMontantValeurLocativePartProprieteEtCoPropiete() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeValLocativePartProprieteEtCoPropiete();
            }
        });
    }

    @Override
    public Montant sumMontantValeurLocativeDH_RPC() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeValLocativeDH_RPC();
            }
        });
    }

    @Override
    public Montant sumMontantValeurLocativePartPropriete(ProprieteType type) {

        return filtreByProprieteType(type).sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeValLocativePartPropriete();
            }
        });
    }

    @Override
    public Montant sumSousLocation() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.getSousLocation();
            }
        });
    }

    public Montant sumSousLocationPartPorietaire() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeSousLocationPartPropriete();
            }
        });
    }

    public Montant sumLoyersEnCaissesPartPorietaire() {

        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeLoyersEnCaissesPartPropriete();
            }
        });
    }

    public Montant sumDepense(final Montant forfaitCharge) {
        return this.sum(new Each<BienImmobilierServantHabitationPrincipale>() {
            @Override
            public Montant getMontant(BienImmobilierServantHabitationPrincipale donnneeFianciere) {
                return donnneeFianciere.computeDepense().isPositive()
                        ? donnneeFianciere.computeDepense().add(forfaitCharge)
                        : Montant.ZERO_ANNUEL;
            }
        });
    }

}
