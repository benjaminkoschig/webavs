package globaz.apg.utils;

/**
 * Classe utilitaire regroupant toutes les méthodes "outils" utilisées dans la génération de la liste de récapitulation
 * des annonces APG
 * 
 * @author PBA
 */
public class APListeRecapitulationAnnonceUtils {

    public static Double addIfNotNull(Double... values) {
        Double total = null;

        if (values != null) {
            for (Double uneValeur : values) {
                if (uneValeur != null) {
                    if (total == null) {
                        total = uneValeur;
                    } else {
                        total += uneValeur;
                    }
                }
            }
        }

        return total;
    }

    public static Integer addIfNotNull(Integer... values) {
        Integer total = null;

        if (values != null) {
            for (Integer uneValeur : values) {
                if (uneValeur != null) {
                    if (total == null) {
                        total = uneValeur;
                    } else {
                        total += uneValeur;
                    }
                }
            }
        }

        return total;
    }
}
