/*
 * Créé le 26 mars 2010
 */
package globaz.cygnus.api.conventions;

/**
 * @author fha
 */
public interface IRFConventions {

    // CONSTANTES POUR LE TYPE DU BENEFICIAIRE
    public static final String ANNUELLE = "66001801";

    public static final String CS_GENRE_PC_TOUS = "66002901";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String CS_GROUPE_PERIODICITE = "RFMNTCO";

    public static final String CS_TYPE_PC_TOUS = "66002801";
    // constantes pour savoir dans quel écran on se trouve
    public static final String ECRAN_CONVENTION = "convention";
    public static final String ECRAN_MONTANT_CONVENTION = "montantConvention";

    public static final String MENSUELLE = "66001802";
    public static final String TRIMESTRIELLE = "66001803";

}