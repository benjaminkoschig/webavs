package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import java.util.Arrays;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresList;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

public class BiensImmobiliers<T extends BienImmobilier, L extends BiensImmobiliers<T, L>> extends
        DonneesFinancieresList<T, L> {

    public BiensImmobiliers(Class<? super L> clazz) {
        super(clazz);
    }

    public Montant sumFraisEntretientBrut(VariablesMetier variablesMetier) {
        return this.sumFraisEntretientBrut(new FraisEntretiensImmeuble(variablesMetier));
    }

    Montant sumFraisEntretientBrut(final FraisEntretiensImmeuble fraisEntretiensImmeuble) {

        // Il n'y a pas de frais d'entretiens sur les biens immobilier non habitable
        return sum(new Each<T>() {
            @Override
            public Montant getMontant(T bienImmobilier) {
                if (!bienImmobilier.getTypeDonneeFinanciere().isBienImmobilierNonHabitable()) {
                    return fraisEntretiensImmeuble.computeBrut(bienImmobilier);
                } else {
                    return Montant.ZERO;
                }
            }
        });
    }

    public L filtreByProprieteType(ProprieteType... types) {
        L biensImmobiliers = newInstance();
        List<ProprieteType> listType = Arrays.asList(types);
        for (T bienImmobilier : getList()) {
            if (listType.contains(bienImmobilier.getProprieteType())) {
                biensImmobiliers.add(bienImmobilier);
            }
        }
        return biensImmobiliers;
    }

}
