package ch.globaz.pegasus.businessimpl.utils.calcul;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee.TypeSeparationCC;

public class PegasusCalculUtil {

    public static final boolean isCoupleSepareParMaladie(TupleDonneeRapport root) throws CalculException {
        if (root == null) {
            throw new CalculException("Tuple is null!");
        }

        return (TypeSeparationCC.CALCUL_SEPARE_MALADIE.getVal().equals(root
                .getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_TYPE_SEPARATION_CC)));

    }

    public static final boolean isRentesPrincipalesCoupleADom(CalculContext context) throws CalculException {
        if (context == null) {
            throw new CalculException("Context is null!");
        }

        return ((TypeSeparationCC) context.get(Attribut.TYPE_SEPARATION_CC) == TypeSeparationCC.CALCUL_DOM2_PRINCIPALE);
    }

    public static final boolean isRentesPrincipalesCoupleADom(TupleDonneeRapport root) throws CalculException {
        if (root == null) {
            throw new CalculException("Tuple is null!");
        }

        return (TypeSeparationCC.CALCUL_DOM2_PRINCIPALE.toString().equals(root
                .getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_TYPE_SEPARATION_CC)));

    }

    public static TypeSeparationCC getTypeSeparation(TupleDonneeRapport root) {
        if (root == null) {
            throw new IllegalArgumentException("Tuple is null!");
        }
        String typeSeparation = root.getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_TYPE_SEPARATION_CC);

        // TypeSeparationCC typeSpearationE = TypeSeparationCC.valueOf(typeSeparation);

        if (TypeSeparationCC.CALCUL_SEPARE_MALADIE.toString().equals(typeSeparation)
                || TypeSeparationCC.CALCUL_SEPARE_MALADIE.getVal().equals(typeSeparation)) {
            return TypeSeparationCC.CALCUL_SEPARE_MALADIE;
        } else if (TypeSeparationCC.CALCUL_DOM2_PRINCIPALE.toString().equals(typeSeparation)
                || TypeSeparationCC.CALCUL_DOM2_PRINCIPALE.getVal().equals(typeSeparation)) {
            return TypeSeparationCC.CALCUL_DOM2_PRINCIPALE;
        } else if (TypeSeparationCC.CALCUL_SANS_SEPARATION.toString().equals(typeSeparation)
                || TypeSeparationCC.CALCUL_SANS_SEPARATION.getVal().equals(typeSeparation)) {
            return TypeSeparationCC.CALCUL_SANS_SEPARATION;
        } else {
            throw new IllegalArgumentException("Unalble to find the type separation with this value: " + typeSeparation);
        }

    }
}
