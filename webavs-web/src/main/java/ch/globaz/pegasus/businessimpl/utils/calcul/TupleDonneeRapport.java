package ch.globaz.pegasus.businessimpl.utils.calcul;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Container des donn�es du calcul.
 * 
 * 
 */
public class TupleDonneeRapport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Arrondis un nombre flottant pass� en param�tre et rertourne le resultat
     * 
     * @param valeurAArrondir
     *            la valeur devant �tre arrondis
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
     * Constructeur vide par d�faut
     */
    public TupleDonneeRapport() {
        super();
    }

    /**
     * Constructeur avec label
     * 
     * @param label
     *            le label voulu pour la cl�
     */
    public TupleDonneeRapport(String label) {
        super();
        this.label = label;
    }

    /**
     * Constructeur standard label - valeur
     * 
     * @param label
     *            le label voulu pour la cl�
     * @param valeur
     *            la valeur de la cl�
     */
    public TupleDonneeRapport(String label, float valeur) {
        this(label);
        this.valeur = arronditValeur(valeur);
    }

    /**
     * Constructeur complet
     * 
     * @param label
     *            le label voulu pour la cl�
     * @param valeur
     *            la valeur de la cl�
     * @param legende
     *            ajout d'une l�gende pour la cl�
     */
    public TupleDonneeRapport(String label, float valeur, String legende) {
        this(label, valeur);
        this.legende = legende;
    }

    /**
     * Constructeur en prenant une insatnce d'un autre objet en param�tre
     * 
     * @param tupleAjoute
     *            le tuple servant de base � la nouvelle instance
     */
    public TupleDonneeRapport(TupleDonneeRapport tupleAjoute) {
        this(tupleAjoute.label, tupleAjoute.valeur);
        legende = tupleAjoute.legende;
    }

    /**
     * Ajout d'un tuple enfant au tuple de base
     * 
     * @param enfant
     *            le tuple enfant � ajout�
     */
    public void addEnfantTuple(TupleDonneeRapport enfant) {
        enfants.put(enfant.label, enfant);
    }

    /**
     * Ajout d'une valeur � la valeur courante --> addition, via la valeur pass� en param�tre
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
     * Retourne true si la key d'un enfant est pr�sente
     * 
     * @param key
     *            , code syst�me de l'enfant recherch�
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
     * Retourne la l�gende (si pr�sente) li� a un enfant, si pas de l�gende, retourne null
     * 
     * @param key
     *            , code syst�me de l'enfant recherch�
     * @return la l�gende li�s � l'enfant, null si pas pr�sente
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
     * Retourne la valeur d'un enfant (si pr�sente)
     * 
     * @param key
     *            , code syst�me de l'enfant recherch�
     * @return la valeur li�s � l'enfant, 0f si pas pr�sente
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
     * Convertis une valeur bool�enne vers une valeur pouvant �tre stock�es dans les tuples</br> <code>true</code> sera
     * convertis vers la valeur String '1' et <code>false</code> sera convertis vers la valeur String '0'
     * 
     * @param value la valeur bool�enne � convertir
     * @return La valeur bool�enne convertie
     */
    public static float writeBoolean(boolean value) {
        if (value) {
            return 1.0f;
        } else {
            return 0.0f;
        }
    }

    /**
     * Lis une valeur bool�en stock�e dans les tuples...
     * Oui, les valeurs bool�enne sont stock�es '0' ou '1' dans les tuples de donn�es
     * 
     * @param value la valeur bool�enne sous forme de string ('1' ou '0')
     * @return <code>true</code> si <code>value</code> vaut '1', <code>false</code> si <code>value</code> vaut '0'. Dans
     *         tous les autres cas une <code></code> sera lanc�e
     */
    public static boolean readBoolean(float value) {
        if (Float.compare(value, 0.5f) > 0) {
            return true;
        } else {
            return false;
        }
    }

}
