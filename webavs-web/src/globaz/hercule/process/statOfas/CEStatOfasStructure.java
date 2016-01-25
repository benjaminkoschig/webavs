package globaz.hercule.process.statOfas;

import globaz.framework.util.FWCurrency;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe permettant de gérer une structure statistique pour les stats OFAS du contrôle d'employeur Cette classe gère
 * les résultats du point 1.1 ou point 2.9 du documents
 * 
 * @author bjo
 * 
 */
public class CEStatOfasStructure {
    public static final String CATEGORIE_0 = "0";
    public static final String CATEGORIE_1 = "1";
    public static final String CATEGORIE_2 = "2";
    public static final String CATEGORIE_3 = "3";
    public static final String CATEGORIE_4 = "4";
    public static final String RESULTAT_CONTROL_ARRIERE = "0";
    public static final String RESULTAT_CONTROL_REMBOURSEMENT = "1";
    public static final String RESULTAT_CONTROL_SANS_PARTICULARITE = "2";

    // structure complete
    private Map<String, Map<String, Map<String, CEStatOfasResultObject>>> structure;

    /**
     * Construit une structure initialisé à 0 pour les statistiques OFAS
     */
    public CEStatOfasStructure() {
        structure = new HashMap<String, Map<String, Map<String, CEStatOfasResultObject>>>();
        for (int i = 0; i < 9; i++) {
            Map<String, Map<String, CEStatOfasResultObject>> unitStructure = createUnitStructure();
            structure.put(i + 1 + "", unitStructure);
        }
    }

    /**
     * Ajoute 1 au nombre de cas pour la statistique de l'annee, de la categorie et du type de resultat du controle
     * 
     * @param annee
     * @param categorieSalaire
     * @param resultatControle
     */
    public void addCas(String annee, String categorieSalaire, String resultatControle) {
        CEStatOfasResultObject resultObject = getStatResultObject(annee, categorieSalaire, resultatControle);
        resultObject.addCas();
    }

    /**
     * Ajoute la masse salariale passee en parametre a la masse salariale de la statistique
     * 
     * @param annee
     * @param categorieSalaire
     * @param resultatControle
     * @param masseSalarialeToAdd
     */
    public void addMasseSalariale(String annee, String categorieSalaire, String resultatControle,
            FWCurrency masseSalarialeToAdd) {
        CEStatOfasResultObject resultObject = getStatResultObject(annee, categorieSalaire, resultatControle);
        resultObject.addMasseSalariale(masseSalarialeToAdd);
    }

    /**
     * Création de la structure unitaire
     * 
     * @return
     */
    public Map<String, Map<String, CEStatOfasResultObject>> createUnitStructure() {
        // création de la structure unitaire
        Map<String, Map<String, CEStatOfasResultObject>> unitStructure = new HashMap<String, Map<String, CEStatOfasResultObject>>();
        for (int i = 0; i < 5; i++) {
            // création de map résultat du contrôle
            Map<String, CEStatOfasResultObject> resultControleMap = new HashMap<String, CEStatOfasResultObject>();
            resultControleMap.put(CEStatOfasStructure.RESULTAT_CONTROL_ARRIERE, new CEStatOfasResultObject());
            resultControleMap.put(CEStatOfasStructure.RESULTAT_CONTROL_REMBOURSEMENT, new CEStatOfasResultObject());
            resultControleMap
                    .put(CEStatOfasStructure.RESULTAT_CONTROL_SANS_PARTICULARITE, new CEStatOfasResultObject());
            // ajout de la map de résultat du contrôle à la catégorie
            unitStructure.put(i + "", resultControleMap);
        }
        return unitStructure;
    }

    /**
     * Retourne la masse salariale en fonction de l'annee, de la categorie et du type de resultat du controle
     * 
     * @param annee
     * @param categorieSalaire
     * @param resultatControle
     * @return
     */
    public String getMasseSalariale(String annee, String categorieSalaire, String resultatControle) {
        CEStatOfasResultObject resultObject = getStatResultObject(annee, categorieSalaire, resultatControle);
        return resultObject.getMasseSalariale().toStringFormat();
    }

    /**
     * Retourne la masse salariale au format int
     * 
     * @param annee
     * @param categorieSalaire
     * @param resultatControle
     * @return
     */
    public Integer getMasseSalarialeToInt(String annee, String categorieSalaire, String resultatControle) {
        CEStatOfasResultObject resultObject = getStatResultObject(annee, categorieSalaire, resultatControle);
        int masseSalariale = resultObject.getMasseSalariale().intValue();
        masseSalariale = Math.abs(masseSalariale);
        return new Integer(masseSalariale);
    }

    /**
     * Retourne la masse salariale totale pour une année et un résultat de controle
     * 
     * @param annee
     * @param resultatControle
     * @return
     */
    public Integer getMasseSalarialeTotalForAnneeAndResultatControl(String annee, String resultatControle) {
        int somme = 0;
        for (int i = 0; i < 5; i++) {
            CEStatOfasResultObject resultObject = getStatResultObject(annee, i + "", resultatControle);
            somme += resultObject.getMasseSalariale().intValue();
        }
        return Math.abs(somme);
    }

    /**
     * Retourne le nb de cas en fonction de l'annee, de la categorie et du type de resultat du controle
     * 
     * @param annee
     * @param categorieSalaire
     * @param resultatControle
     * @return
     */
    public int getNbCas(String annee, String categorieSalaire, String resultatControle) {
        CEStatOfasResultObject resultObject = getStatResultObject(annee, categorieSalaire, resultatControle);
        return resultObject.getNbCas();
    }

    /**
     * Retourne le nb total de cas pour l'année
     * 
     * @param annee
     * @return
     */
    public int getNbCasTotalForAnnee(String annee) {
        int nbCasTotal = 0;
        for (int i = 0; i < 5; i++) {
            nbCasTotal += getNbCasTotalForAnneeAndCategorie(annee, i + "");
        }
        return nbCasTotal;
    }

    /**
     * Retourne le nb total de cas pour l'année et la catégorie de salaire (prend en compte tout les résultats du
     * control)
     * 
     * @param annee
     * @param categorieSalaire
     * @return
     */
    public int getNbCasTotalForAnneeAndCategorie(String annee, String categorieSalaire) {
        int nbCasTotal = 0;
        for (int i = 0; i < 3; i++) {
            CEStatOfasResultObject resultObject = getStatResultObject(annee, categorieSalaire, i + "");
            nbCasTotal += resultObject.getNbCas();
        }
        return nbCasTotal;
    }

    /**
     * Retourne le nb total de cas pour l'année et le résultat du contrôle (prend en compte toutes les catégories)
     * 
     * @param annee
     * @param resultatControl
     * @return
     */
    public int getNbCasTotalForAnneeAndResultatControl(String annee, String resultatControl) {
        int nbCasTotal = 0;
        for (int i = 0; i < 5; i++) {
            CEStatOfasResultObject resultObject = getStatResultObject(annee, i + "", resultatControl);
            nbCasTotal += resultObject.getNbCas();
        }
        return nbCasTotal;
    }

    /**
     * Retourne l'objet de resultat des statistiques en fonction de l'annee, de la categorie et du type de resultat du
     * controle
     * 
     * @param annee
     * @param categorieSalaire
     * @param resultatControle
     * @return
     */
    public CEStatOfasResultObject getStatResultObject(String annee, String categorieSalaire, String resultatControle) {
        CEStatOfasResultObject resultObject = getStructure().get(annee).get(categorieSalaire).get(resultatControle);
        return resultObject;
    }

    public Map<String, Map<String, Map<String, CEStatOfasResultObject>>> getStructure() {
        return structure;
    }

    public void setStructure(Map<String, Map<String, Map<String, CEStatOfasResultObject>>> structure) {
        this.structure = structure;
    }
}
