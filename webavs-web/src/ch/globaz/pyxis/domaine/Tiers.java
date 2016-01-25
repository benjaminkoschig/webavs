package ch.globaz.pyxis.domaine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.jade.business.models.Langues;

/**
 * Objet de domaine repr�sentant un tiers (personne physique ou morale) et contenant le nom/pr�nom ou nom de
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
     * Les titres des tiers �tant totalement costumisable chez chaque client, il n'est pas possible de cr�er une �num�r�
     * de ces titres car les codes syst�mes peuvent vari�s d'un client � un autre. Du coup, ces titres sont stock�s
     * directement traduit, avec comme cl� la langue de traduction
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
     * @return une nom d�crivant ce tiers. Peut �tre le nom de l'entreprise (dans le cas d'une personne morale) ou le
     *         nom de famille (dans le cas d'une personne physique).
     */
    public String getDesignation1() {
        return designation1;
    }

    /**
     * @return un compl�ment � {@link #getDesignation1()} permettant d'identifier ce tiers. Peut �tre un compl�ment au
     *         nom de l'entreprise (dans le cas d'une personne morale) ou le pr�nom (dans le cas d'une personne
     *         physique).
     */
    public String getDesignation2() {
        return designation2;
    }

    /**
     * @return le pays dont le tiers poss�de la nationalit�
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
     * (re-)d�fini le nom principal de ce tiers (nom de la soci�t� pour une personne morale, nom de famille pour une
     * personne physique)
     * 
     * @param designation1
     *            une cha�ne de caract�re non nulle
     */
    public void setDesignation1(String designation1) {
        Checkers.checkNotNull(designation1, "designation1");
        this.designation1 = designation1;
    }

    /**
     * (re-)d�fini le nom secondaire de ce tiers (compl�ment au nom de la soci�t� pour une personne morale, pr�nom pour
     * une personne physique)
     * 
     * @param designation2
     *            une cha�ne de caract�re non nulle
     */
    public void setDesignation2(String designation2) {
        Checkers.checkNotNull(designation2, "designation2");
        this.designation2 = designation2;
    }

    /**
     * (re-)d�fini la nationalit� du tiers
     * 
     * @param pays
     *            la nouvelle nationalit� du tiers, repr�sent�e par un pays
     */
    public void setPays(Pays pays) {
        Checkers.checkNotNull(pays, "pays");
        this.pays = pays;
    }

    /**
     * (re-)d�fini toutes les traductions du titre de ce tiers
     * 
     * @param titreParLangue
     *            le titre de ce tiers traduit dans plusieurs langues
     */
    public void setTitreParLangue(Map<Langues, String> titreParLangue) {
        Checkers.checkNotNull(titreParLangue, "titreParLangue");
        this.titreParLangue = titreParLangue;
    }
}
