package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere;

public class UtilStrategieFinalFortuneImmobiliere {

    protected static float plafonneValeurBiensImmoDeduit(float montant) {

        if (montant < 0f) {
            return 0f;
        } else {
            return montant;
        }
    }
    protected static float plafondDeductionDetteHypo(float montantValeur, float montantDette) {

        if (montantValeur > montantDette) {
            return montantValeur-montantDette;
        } else {
            return 0f;
        }
    }
}
