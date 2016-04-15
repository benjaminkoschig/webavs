package ch.globaz.al.utils;

/**
 * Cette classe permet d'analyser si des changement ont étés apportés sur des champs d'entité
 * Elle propose des méthodes utilitaire pour analyser si un changement de valeur est réalisé sur différents type de
 * champs
 *
 * @author lga
 *
 */
public class ALEntityFieldChangeAnalyser {

    public static boolean hasValueChanged(String date1, String date2) {

        boolean date1Null = date1 == null;
        boolean date2Null = date2 == null;

        // si les 2 champs sont null il sont identique -> retourne false
        if (date1Null && date2Null) {
            return false;
        }

        // Si 1 des 2 champs est null, il ne sont pas identique. (a ce stade, seul un des 2 peut être null)
        if (date1Null || date2Null) {
            return true;
        }

        // Aucun des 2 champs n'est null, valeur identique ?
        return !date1.equals(date2);
    }

}
