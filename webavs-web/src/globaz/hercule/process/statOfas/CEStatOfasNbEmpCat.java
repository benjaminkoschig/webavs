package globaz.hercule.process.statOfas;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe permettant de g�rer la statistique 3.3
 * 
 * @author bjo
 * 
 */
public class CEStatOfasNbEmpCat {
    Map<String, Integer> nbEmpCat;

    public CEStatOfasNbEmpCat() {
        nbEmpCat = new HashMap<String, Integer>();
        for (int i = 0; i < 5; i++) {
            nbEmpCat.put(i + "", 0);
        }
    }

    /**
     * Ajoute un cas � la cat�gorie sp�cifi�e
     * 
     * @param categorie
     */
    public void addCas(String categorie) {
        int nbCas = nbEmpCat.get(categorie);
        int newNbCas = nbCas + 1;
        nbEmpCat.put(categorie, newNbCas);
    }

    public Integer getNbCas(String categorie) {
        return nbEmpCat.get(categorie);
    }

    public Integer getNbCasTotal() {
        int somme = 0;
        for (int i = 0; i < 5; i++) {
            somme += getNbCas(i + "");
        }
        return somme;
    }
}
