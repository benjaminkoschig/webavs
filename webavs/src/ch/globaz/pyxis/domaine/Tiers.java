package ch.globaz.pyxis.domaine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.jade.business.models.Langues;

/**
 * Objet de domaine représentant un tiers (personne physique ou morale) et contenant le nom/prénom ou nom de
 * l'entreprise ainsi que son identifiant unique
 */
public class Tiers extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String designation1;
    private String designation2;
    private Pays pays;
    /**
     * Les titres des tiers étant totalement costumisable chez chaque client, il n'est pas possible de créer une énuméré
     * de ces titres car les codes systèmes peuvent variés d'un client à un autre. Du coup, ces titres sont stockés
     * directement traduit, avec comme clé la langue de traduction
     */
    private Map<Langues, String> titreParLangue;

    public Tiers() {
        super();

        designation1 = "";
        designation2 = "";
        titreParLangue = new HashMap<Langues, String>();
    }

    /**
     * Ajoute une traduction du titre de ce tiers
     * 
     * @param langue
     *            la langue de traduction
     * @param titre
     *            le titre
     */
    public void ajouterTraductionTitre(Langues langue, String titre) {
        Checkers.checkNotNull(langue, "langue");
        Checkers.checkNotNull(titre, "titre");
        titreParLangue.put(langue, titre);
    }

    /**
     * @return une nom décrivant ce tiers. Peut être le nom de l'entreprise (dans le cas d'une personne morale) ou le
     *         nom de famille (dans le cas d'une personne physique).
     */
    public String getDesignation1() {
        return designation1;
    }

    /**
     * @return un complément à {@link #getDesignation1()} permettant d'identifier ce tiers. Peut être un complément au
     *         nom de l'entreprise (dans le cas d'une personne morale) ou le prénom (dans le cas d'une personne
     *         physique).
     */
    public String getDesignation2() {
        return designation2;
    }

    /**
     * @return le pays dont le tiers possède la nationalité
     */
    public Pays getPays() {
        return pays;
    }

    /**
     * @return le titre de ce tiers traduit en plusieurs langues dans un conteneur invariable
     */
    public Map<Langues, String> getTitreParLangue() {
        return Collections.unmodifiableMap(titreParLangue);
    }

    /**
     * @param langue
     *            la langue de traduction voulue
     * @return la traduction du titre de ce tiers dans la langue choisie
     * @throws IllegalArgumentException
     *             si la traduction pour cette langue n'existe pas
     */
    public String getTitreTraduit(Langues langue) {
        if (!titreParLangue.containsKey(langue)) {
            StringBuilder errorMessage = new StringBuilder();

            errorMessage.append("there is no translation of this person's title in this langage (titles available : {");

            boolean firstPass = true;
            for (Entry<Langues, String> uneTraduction : titreParLangue.entrySet()) {
                if (firstPass) {
                    firstPass = false;
                } else {
                    errorMessage.append(",");
                }
                errorMessage.append(uneTraduction.getKey().getCodeIso()).append(":").append(uneTraduction.getValue());
            }

            errorMessage.append("})");
            throw new IllegalArgumentException(errorMessage.toString());
        }
        return titreParLangue.get(langue);
    }

    /**
     * (re-)défini le nom principal de ce tiers (nom de la société pour une personne morale, nom de famille pour une
     * personne physique)
     * 
     * @param designation1
     *            une chaîne de caractère non nulle
     */
    public void setDesignation1(String designation1) {
        Checkers.checkNotNull(designation1, "designation1");
        this.designation1 = designation1;
    }

    /**
     * (re-)défini le nom secondaire de ce tiers (complément au nom de la société pour une personne morale, prénom pour
     * une personne physique)
     * 
     * @param designation2
     *            une chaîne de caractère non nulle
     */
    public void setDesignation2(String designation2) {
        Checkers.checkNotNull(designation2, "designation2");
        this.designation2 = designation2;
    }

    /**
     * (re-)défini la nationalité du tiers
     * 
     * @param pays
     *            la nouvelle nationalité du tiers, représentée par un pays
     */
    public void setPays(Pays pays) {
        Checkers.checkNotNull(pays, "pays");
        this.pays = pays;
    }

    /**
     * (re-)défini toutes les traductions du titre de ce tiers
     * 
     * @param titreParLangue
     *            le titre de ce tiers traduit dans plusieurs langues
     */
    public void setTitreParLangue(Map<Langues, String> titreParLangue) {
        Checkers.checkNotNull(titreParLangue, "titreParLangue");
        this.titreParLangue = titreParLangue;
    }
}
