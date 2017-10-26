package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.BiensImmobiliers;

public class BiensImmobiliersNonHabitable extends
        BiensImmobiliers<BienImmobilierNonHabitable, BiensImmobiliersNonHabitable> {

    public BiensImmobiliersNonHabitable() {
        super(BiensImmobiliersNonHabitable.class);
    }

    public Montant sumMontantRendementBrut() {
        return this.sum(new Each<BienImmobilierNonHabitable>() {
            @Override
            public Montant getMontant(BienImmobilierNonHabitable donnneeFianciere) {
                return donnneeFianciere.getRendement();
            }
        });

    }

    public Montant sumInteretHypotecaire() {
        return this.sum(new Each<BienImmobilierNonHabitable>() {
            @Override
            public Montant getMontant(BienImmobilierNonHabitable donnneeFianciere) {
                return donnneeFianciere.computeInteret();
            }
        });
    }

    public Montant sumMontantRendementPartPropriete() {
        return this.sum(new Each<BienImmobilierNonHabitable>() {
            @Override
            public Montant getMontant(BienImmobilierNonHabitable donnneeFianciere) {
                return donnneeFianciere.computeRendementPartPropriete();
            }
        });
    }

    public Montant sumMontantRendementPartPropriete(ProprieteType type) {
        return filtreByProprieteType(type).sum(new Each<BienImmobilierNonHabitable>() {
            @Override
            public Montant getMontant(BienImmobilierNonHabitable donnneeFianciere) {
                return donnneeFianciere.computeRendementPartPropriete();
            }
        });
    }

}
