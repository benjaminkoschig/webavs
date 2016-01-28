package globaz.apg.utils;

/**
 * Classe utilitaire regroupant toutes les m�thodes "outils" utilis�es dans la g�n�ration de la liste de r�capitulation
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
