package globaz.apg.enums;

/**
 * <p>
 * Énumère les différents types possibles de service sous la protection civile
 * </p>
 * <p>
 * Voir le document "Contrôles des plausibilité auprès des caisses de compensation et de la centrale" (N° 2.10) pour la
 * définition exacte en fonction du numéro de référence (numéro de compte dans l'ancienne nomenclature).
 * </p>
 * 
 * @author PBA
 */
public enum APTypeProtectionCivile {

    /**
     * Nommé cas 4 dans les manuels de l'OFAS
     */
    CatastropheSituationUrgenceEtRemiseEnEtat,
    /**
     * Nommé cas 3 dans les manuels de l'OFAS
     */
    CoursDeRepetition,
    /**
     * retourné lorsque le refNumber n'est pas valide
     */
    Indefini,
    /**
     * Nommé cas 1 dans les manuels de l'OFAS
     */
    InstructionDeBase,
    /**
     * Nommé cas 2 dans les manuels de l'OFAS
     */
    InstructionDesCadresPerfectionnementEtInstructionSupplementaire,
    /**
     * Nommé cas 5 dans les manuels de l'OFAS
     */
    InterventionEnFaveurDeLaCollectivite,
    /**
     * Spécifique PC Confédération (Code 26.00.2)
     */
    ServiceAccompliDansAdministrationProtectionCivile
}
