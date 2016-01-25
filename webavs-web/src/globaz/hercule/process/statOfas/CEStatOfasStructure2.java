package globaz.hercule.process.statOfas;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe permettant de g�rer une structure statistique pour les stats OFAS du contr�le d'employeur Cette classe g�re
 * les r�sultats du point 3.1 du documents
 * 
 * @author bjo
 * 
 */
public class CEStatOfasStructure2 {

    public static final String CATEGORIE_0 = "0";
    public static final String CATEGORIE_1 = "1";
    public static final String CATEGORIE_2 = "2";
    public static final String CATEGORIE_3 = "3";
    public static final String CATEGORIE_4 = "4";
    public static final String REVISEUR_AUTRE = "2";
    public static final String REVISEUR_INTERNE = "3";
    public static final String REVISEUR_RSA = "1";
    public static final String REVISEUR_SUVA = "0";

    private Map<String, Map<String, Integer>> structure2;

    public CEStatOfasStructure2() {
        structure2 = new HashMap<String, Map<String, Integer>>();
        // pour chaque cat�gorie on ajout une map de r�viseur
        for (int i = 0; i < 5; i++) {
            // cr�ation de la map des r�viseurs initialis� � 0
            Map<String, Integer> reviseurMap = new HashMap<String, Integer>();
            reviseurMap.put(CEStatOfasStructure2.REVISEUR_SUVA, 0);
            reviseurMap.put(CEStatOfasStructure2.REVISEUR_RSA, 0);
            reviseurMap.put(CEStatOfasStructure2.REVISEUR_AUTRE, 0);
            reviseurMap.put(CEStatOfasStructure2.REVISEUR_INTERNE, 0);
            structure2.put(i + "", reviseurMap);
        }
    }

    /**
     * Ajoute 1 au nombre de cas pour une cat�gorie et un r�viseur
     * 
     * @param annee
     * @param categorieSalaire
     * @param reviseur
     */
    public void addCas(String categorieSalaire, String reviseur) {
        int nbCas = getNbCas(categorieSalaire, reviseur);
        int newNbCas = nbCas + 1;
        structure2.get(categorieSalaire).put(reviseur, newNbCas);
    }

    /**
     * Retourne le nombre de cas pour une cat�gorie et une r�viseur
     * 
     * @param annee
     * @param categorieSalaire
     * @param reviseur
     * @return
     */
    public int getNbCas(String categorieSalaire, String reviseur) {
        return structure2.get(categorieSalaire).get(reviseur);
    }

    /**
     * Retourne le nombre de cas total pour une cat�gorie
     * 
     * @param categorie
     * @return
     */
    public int getNbCasTotal(String categorie) {
        int somme = 0;
        for (int i = 0; i < 4; i++) {
            somme += getNbCas(categorie, i + "");
        }
        return somme;
    }

    /**
     * Retourne le nombre de cas total pour un r�viseur (prend en compte toutes les cat�gories)
     * 
     * @param reviseur
     * @return
     */
    public int getNbCasTotalForReviseur(String reviseur) {
        int somme = 0;
        for (int i = 0; i < 5; i++) {
            somme += getNbCas(i + "", reviseur);
        }
        return somme;
    }

    /**
     * Retourne le nombre de cas total pour les r�viseurs externes
     * 
     * @param categorie
     * @return
     */
    public int getNbCasTotalReviseurExternes(String categorie) {
        int somme = 0;
        for (int i = 0; i < 3; i++) {
            somme += getNbCas(categorie, i + "");
        }
        return somme;
    }

    /**
     * Retourne le nombre total de cas pour les r�viseurs externes pour toutes les cat�gories
     * 
     * @return
     */
    public int getNbCasTotalReviseurExternesForAllCategorie() {
        int somme = 0;
        for (int i = 0; i < 3; i++) {
            somme += getNbCasTotalForReviseur(i + "");
        }
        return somme;
    }

    /**
     * Retourne le nombre total de cas pour les tous les type de r�viseurs et toutes les cat�gories
     * 
     * @return
     */
    public int getNbCasTotalReviseurForAllCategorie() {
        int somme = 0;
        for (int i = 0; i < 4; i++) {
            somme += getNbCasTotalForReviseur(i + "");
        }
        return somme;
    }

    public Map<String, Map<String, Integer>> getStructure2() {
        return structure2;
    }

    public void setStructure2(Map<String, Map<String, Integer>> structure2) {
        this.structure2 = structure2;
    }
}
