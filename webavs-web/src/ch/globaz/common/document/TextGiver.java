package ch.globaz.common.document;

import ch.globaz.jade.business.models.Langues;

/**
 * Définit les méthodes que doit avoir le gestionnaire de text internationalisé.
 * 
 * @author dma
 * 
 * @param <T> Définit le type de la clé
 */
public interface TextGiver<T> {
    /**
     * @param Définition du text a résoudre, utilise la langue de l'utilisateur
     * @return le texte internationalisé
     */
    public String resolveText(T key);

    /**
     * @param Définition du text a résoudre
     * @return le texte internationalisé
     */
    public String resolveText(T definition, Langues langue);

    public String resolveText(T definition, String langue);

    public void setLangue(String langue);

}
