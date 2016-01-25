package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere;

public class UtilStrategieFinalFortuneImmobiliere {

    protected float plafonneValeurBiensImmoDeduit(float montant) {

        if (montant < 0f) {
            return 0f;
        } else {
            return montant;
        }
    }
}
