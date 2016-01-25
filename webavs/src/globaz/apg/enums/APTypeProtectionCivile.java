package globaz.apg.enums;

/**
 * <p>
 * �num�re les diff�rents types possibles de service sous la protection civile
 * </p>
 * <p>
 * Voir le document "Contr�les des plausibilit� aupr�s des caisses de compensation et de la centrale" (N� 2.10) pour la
 * d�finition exacte en fonction du num�ro de r�f�rence (num�ro de compte dans l'ancienne nomenclature).
 * </p>
 * 
 * @author PBA
 */
public enum APTypeProtectionCivile {

    /**
     * Nomm� cas 4 dans les manuels de l'OFAS
     */
    CatastropheSituationUrgenceEtRemiseEnEtat,
    /**
     * Nomm� cas 3 dans les manuels de l'OFAS
     */
    CoursDeRepetition,
    /**
     * retourn� lorsque le refNumber n'est pas valide
     */
    Indefini,
    /**
     * Nomm� cas 1 dans les manuels de l'OFAS
     */
    InstructionDeBase,
    /**
     * Nomm� cas 2 dans les manuels de l'OFAS
     */
    InstructionDesCadresPerfectionnementEtInstructionSupplementaire,
    /**
     * Nomm� cas 5 dans les manuels de l'OFAS
     */
    InterventionEnFaveurDeLaCollectivite,
    /**
     * Sp�cifique PC Conf�d�ration (Code 26.00.2)
     */
    ServiceAccompliDansAdministrationProtectionCivile
}
