package globaz.corvus.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilitaire retournant une map ayant comme clés les codes prestation de rentes principales et comme valeurs les
 * complémentaires correspondantes
 * 
 * @author PBA
 */
public class RECodePrestationComplementaireUtil {

    private static Map<Integer, List<Integer>> genererComplementaireCommune() {
        Map<Integer, List<Integer>> complementairesCommunes = new HashMap<Integer, List<Integer>>();

        List<Integer> vieillesse = new ArrayList<Integer>();
        vieillesse.add(33);
        vieillesse.add(36);
        complementairesCommunes.put(10, vieillesse);

        List<Integer> vieillesseExtraordinaire = new ArrayList<Integer>();
        vieillesseExtraordinaire.add(43);
        vieillesseExtraordinaire.add(46);
        complementairesCommunes.put(20, vieillesseExtraordinaire);

        List<Integer> invalidite = new ArrayList<Integer>();
        invalidite.add(53);
        invalidite.add(56);
        complementairesCommunes.put(50, invalidite);

        List<Integer> invaliditeExtraordinaire = new ArrayList<Integer>();
        invaliditeExtraordinaire.add(73);
        invaliditeExtraordinaire.add(76);
        complementairesCommunes.put(70, invaliditeExtraordinaire);

        List<Integer> survivant = new ArrayList<Integer>();
        survivant.add(16);
        complementairesCommunes.put(13, survivant);

        List<Integer> survivantExtraordinaire = new ArrayList<Integer>();
        survivantExtraordinaire.add(26);
        complementairesCommunes.put(23, survivantExtraordinaire);

        return complementairesCommunes;
    }

    private static Map<Integer, List<Integer>> genererComplementaireFemme() {
        Map<Integer, List<Integer>> complementaires = RECodePrestationComplementaireUtil.genererComplementaireCommune();

        complementaires.get(10).add(35);
        complementaires.get(20).add(45);
        complementaires.get(50).add(55);
        complementaires.get(70).add(75);
        complementaires.get(13).add(14);
        complementaires.get(23).add(24);

        return complementaires;
    }

    private static Map<Integer, List<Integer>> genererComplementaireHomme() {
        Map<Integer, List<Integer>> complementaires = RECodePrestationComplementaireUtil.genererComplementaireCommune();

        complementaires.get(10).add(34);
        complementaires.get(20).add(44);
        complementaires.get(50).add(54);
        complementaires.get(70).add(74);
        complementaires.get(13).add(15);
        complementaires.get(23).add(25);

        return complementaires;
    }

    public static Map<Integer, List<Integer>> getComplementairePourFemme() {
        return RECodePrestationComplementaireUtil.genererComplementaireFemme();
    }

    public static Map<Integer, List<Integer>> getComplementairePourHomme() {
        return RECodePrestationComplementaireUtil.genererComplementaireHomme();
    }
}
