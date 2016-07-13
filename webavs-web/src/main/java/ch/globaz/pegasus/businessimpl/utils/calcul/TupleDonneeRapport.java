package ch.globaz.pegasus.businessimpl.utils.calcul;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Container des données du calcul.
 * 
 * 
 */
public class TupleDonneeRapport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Arrondis un nombre flottant passé en paramètre et rertourne le resultat
     * 
     * @param valeurAArrondir
     *            la valeur devant être arrondis
     * @return la valeur arrondis
     */
    public static float arronditValeur(float valeurAArrondir) {
        float valeurArrondis = Math.round(valeurAArrondir);
        return valeurArrondis;
    }

    private Map<String, TupleDonneeRapport> enfants = new HashMap<String, TupleDonneeRapport>();
    private String label; // cs de KEY VALEUR PLAN CALCUL
    private String legende = null;

    private float valeur = 0f;

    /**
     * Constructeur vide par défaut
     */
    public TupleDonneeRapport() {
        super();
    }

    /**
     * Constructeur avec label
     * 
     * @param label
     *            le label voulu pour la clé
     */
    public TupleDonneeRapport(String label) {
        super();
        this.label = label;
    }

    /**
     * Constructeur standard label - valeur
     * 
     * @param label
     *            le label voulu pour la clé
     * @param valeur
     *            la valeur de la clé
     */
    public TupleDonneeRapport(String label, float valeur) {
        this(label);
        this.valeur = arronditValeur(valeur);
    }

    /**
     * Constructeur complet
     * 
     * @param label
     *            le label voulu pour la clé
     * @param valeur
     *            la valeur de la clé
     * @param legende
     *            ajout d'une légende pour la clé
     */
    public TupleDonneeRapport(String label, float valeur, String legende) {
        this(label, valeur);
        this.legende = legende;
    }

    /**
     * Constructeur en prenant une insatnce d'un autre objet en paramètre
     * 
     * @param tupleAjoute
     *            le tuple servant de base à la nouvelle instance
     */
    public TupleDonneeRapport(TupleDonneeRapport tupleAjoute) {
        this(tupleAjoute.label, tupleAjoute.valeur);
        legende = tupleAjoute.legende;
    }

    /**
     * Ajout d'un tuple enfant au tuple de base
     * 
     * @param enfant
     *            le tuple enfant à ajouté
     */
    public void addEnfantTuple(TupleDonneeRapport enfant) {
        enfants.put(enfant.label, enfant);
    }

    /**
     * Ajout d'une valeur à la valeur courante --> addition, via la valeur passé en paramètre
     * 
     * @param montant
     *            le montant a ajouter
     */
    public void addValeur(float montant) {
        valeur += arronditValeur(montant);
    }

    public void addValeur(TupleDonneeRapport tuple) {
        this.addValeur(tuple.valeur);
    }

    /**
     * Retourne true si la key d'un enfant est présente
     * 
     * @param key
     *            , code système de l'enfant recherché
     * @return true si la key est presente
     */
    public Boolean containsValeurEnfant(String key) {
        return enfants.containsKey(key);
    }

    public final TupleDonneeRapport getClone() {
        TupleDonneeRapport clone = new TupleDonneeRapport(this);
        for (TupleDonneeRapport enfant : enfants.values()) {
            clone.addEnfantTuple(enfant.getClone());
        }
        return clone;
    }

    /**
     * @return the enfants
     */
    public Map<String, TupleDonneeRapport> getEnfants() {
        return enfants;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the legende
     */
    public String getLegende() {
        return legende;
    }

    /**
     * Retourne la légende (si présente) lié a un enfant, si pas de légende, retourne null
     * 
     * @param key
     *            , code système de l'enfant recherché
     * @return la légende liés à l'enfant, null si pas présente
     */
    public String getLegendeEnfant(String key) {
        if (enfants.containsKey(key)) {
            return enfants.get(key).legende;
        } else {
            return null;
        }
    }

    public TupleDonneeRapport getOrCreateEnfant(String key) {
        if (enfants.containsKey(key)) {
            return enfants.get(key);
        } else {
            TupleDonneeRapport nouvelEnfant = new TupleDonneeRapport(key);
            addEnfantTuple(nouvelEnfant);
            return nouvelEnfant;
        }

    }

    public float getSommeEnfants(String cle) {
        float somme = 0f;

        TupleDonneeRapport tupleBase = enfants.get(cle);
        if (tupleBase != null) {
            for (TupleDonneeRapport tuple : tupleBase.enfants.values()) {
                somme += tuple.getValeur();
            }
            tupleBase.valeur += somme;
        }

        return somme;
    }

    /**
     * @return the valeur
     */
    public float getValeur() {
        return valeur;
    }

    /**
     * Retourne la valeur d'un enfant (si présente)
     * 
     * @param key
     *            , code système de l'enfant recherché
     * @return la valeur liés à l'enfant, 0f si pas présente
     */
    public Float getValeurEnfant(String key) {
        if (enfants.containsKey(key)) {
            return enfants.get(key).valeur;
        } else {
            return 0f;
        }
    }

    /**
     * @param enfants
     *            the enfants to set
     */
    public void setEnfants(Map<String, TupleDonneeRapport> enfants) {
        this.enfants = enfants;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @param legende
     *            the legende to set
     */
    public void setLegende(String legende) {
        this.legende = legende;
    }

    public void setValeur(float valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        String libelle = "";
        if (JadeNumericUtil.isInteger(label)) {
            libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(label);
        }
        return "(" + label + "{" + libelle + " ; " + legende + "}=" + valeur + ")" + enfants.values();
    }

    /**
     * Convertis une valeur booléenne vers une valeur pouvant être stockées dans les tuples</br> <code>true</code> sera
     * convertis vers la valeur String '1' et <code>false</code> sera convertis vers la valeur String '0'
     * 
     * @param value la valeur booléenne à convertir
     * @return La valeur booléenne convertie
     */
    public static float writeBoolean(boolean value) {
        if (value) {
            return 1.0f;
        } else {
            return 0.0f;
        }
    }

    /**
     * Lis une valeur booléen stockée dans les tuples...
     * Oui, les valeurs booléenne sont stockées '0' ou '1' dans les tuples de données
     * 
     * @param value la valeur booléenne sous forme de string ('1' ou '0')
     * @return <code>true</code> si <code>value</code> vaut '1', <code>false</code> si <code>value</code> vaut '0'. Dans
     *         tous les autres cas une <code></code> sera lancée
     */
    public static boolean readBoolean(float value) {
        if (Float.compare(value, 0.5f) > 0) {
            return true;
        } else {
            return false;
        }
    }

}
