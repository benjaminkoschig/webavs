/*
 * Créé le 9 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface ICTScalableDocumentProperties extends Serializable {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajoute l'anexe donne a la liste des annexes
     * 
     * @param annexe
     */
    public void addAnnexe(ICTScalableDocumentAnnexe annexe);

    /**
     * Ajoute la copie donnee a la liste des copies
     * 
     * @param copie
     */
    public void addCopie(ICTScalableDocumentCopie copie);

    /**
     * Ajoute un niveau selectionnable au document
     * 
     * @param niveau
     */
    public void addNiveau(ICTScalableDocumentNiveau niveau);

    /**
     * Vrais si un parametre existe pour le cle donnee
     * 
     * @param key
     * @return
     */
    public Boolean containsParameter(String key);

    /**
     * Donne l'annexe de la position donnee
     * 
     * @param pos
     * @return
     */
    public ICTScalableDocumentAnnexe getAnnexe(int pos);

    /**
     * Donne un iterateur sur les annexes
     * 
     * @return
     */
    public Iterator getAnnexesIterator();

    /**
     * Donne le nombre d'annexes
     * 
     * @return
     */
    public int getAnnexesSize();

    /**
     * Donne la copie de la position donnee
     * 
     * @param pos
     * @return
     */
    public ICTScalableDocumentCopie getCopie(int pos);

    /**
     * Donne un iterateur sur les copies
     * 
     * @return
     */
    public Iterator getCopiesIterator();

    /**
     * Donne le nombre de copies
     * 
     * @return
     */
    public int getCopiesSize();

    /**
     * Donne l'id du document a publier
     * 
     * @return
     */
    public String getIdDocument();

    /**
     * Donne l'id du tiers principal
     * 
     * @return
     */
    public String getIdTiersPrincipal();

    /**
     * Donne le niveau de la position donnee
     * 
     * @param pos
     * @return
     */
    public ICTScalableDocumentNiveau getNiveau(int pos);

    /**
     * Donne un iterateur sur les niveaux
     * 
     * @return
     */
    public Iterator getNiveauxIterator();

    /**
     * Donne le nombre de niveaux
     * 
     * @return
     */
    public int getNiveauxSize();

    /**
     * Donne le parametre correspondant a la key, null si aucun parametre correspondant
     * 
     * @param key
     * @return
     */
    public String getParameter(String key);

    /**
     * Donne une vue des mapping de la collection des parametres.
     * 
     * @return
     */
    public Set getParametersEntrySet();

    /**
     * Vrais si le document a publier est un meta-document
     * 
     * @return
     */
    public Boolean isMetaDocument();

    /**
     * Vide la liste des annexes
     * 
     */
    public void removeAllAnnexes();

    /**
     * Vide la liste des copies
     * 
     */
    public void removeAllCopies();

    /**
     * Efface les parametres
     * 
     */
    public void removeAllParameters();

    /**
     * Enleve l'annexe donne de la liste des annexes
     * 
     * @param annexe
     */
    public void removeAnnexe(ICTScalableDocumentAnnexe annexe);

    /**
     * Enleve la copie donnee de la liste des copies
     * 
     * @param copie
     */
    public void removeCopie(ICTScalableDocumentCopie copie);

    /**
     * Set l'id du document a publier
     * 
     * @param idTiers
     */
    public void setIdDocument(String idDocument);

    /**
     * Set l'id du tiers principal
     * 
     * @param idTiers
     */
    public void setIdTiersPrincipal(String idTiers);

    /**
     * Set si le document est un meta-document
     * 
     * @return
     */
    public void setIsMetaDocument(Boolean isMetaDocument);

    /**
     * Insert la paire key/value dans la liste des paramètres
     * 
     * @param key
     * @param value
     */
    public void setParameter(String key, String value);

}
