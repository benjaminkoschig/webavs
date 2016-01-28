package ch.globaz.al.business.models.droit;

import java.util.Collection;

public class TarifAggregator {

    /** Valeur de retour lorsque la collection contient des valeurs diff�rentes */
    public static final String TARIF_MULTIPLE = "*";

    /** Valeur de retour lorsque la collection contient des valeurs ind�finis (0) */
    public static final String TARIF_UNDEFINED = "undefined";

    /** Valeur repr�sentant ind�fini en base de donn�es */
    public static final String TARIF_UNDEFINED_BD = "0";

    /***
     * Calcul � partir d'une collection de String
     * 
     * @param tarifs
     *            D�termine un tarif unique � partir d'une liste de String
     * @return {@link #TARIF_UNDEFINED} lorsque la liste de tarifs est vide. Un code syst�me repr�sentant le canton
     *         lorsque la liste contient des valeurs similaires. {@link #TARIF_MULTIPLE} lorsque les valeurs sont
     *         diff�rentes.
     * 
     */
    public static String calculerTarif(Collection<String> tarifs) {
        String categorieTarif = TarifAggregator.TARIF_UNDEFINED;
        for (String tarif : tarifs) {
            if (TarifAggregator.TARIF_UNDEFINED.equals(categorieTarif)) {
                categorieTarif = tarif;
            } else if (!categorieTarif.equals(tarif)) {
                return TarifAggregator.TARIF_MULTIPLE;
            }
        }

        if (TarifAggregator.isUndefined(categorieTarif)) {
            return TarifAggregator.TARIF_UNDEFINED;
        } else {
            return categorieTarif;
        }
    }

    private static boolean isUndefined(String categorieTarif) {
        return categorieTarif.equals(TarifAggregator.TARIF_UNDEFINED_BD);
    }
}
