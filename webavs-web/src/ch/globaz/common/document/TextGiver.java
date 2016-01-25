package ch.globaz.common.document;

import ch.globaz.jade.business.models.Langues;

/**
 * D�finit les m�thodes que doit avoir le gestionnaire de text internationalis�.
 * 
 * @author dma
 * 
 * @param <T> D�finit le type de la cl�
 */
public interface TextGiver<T> {
    /**
     * @param D�finition du text a r�soudre, utilise la langue de l'utilisateur
     * @return le texte internationalis�
     */
    public String resolveText(T key);

    /**
     * @param D�finition du text a r�soudre
     * @return le texte internationalis�
     */
    public String resolveText(T definition, Langues langue);

    public String resolveText(T definition, String langue);

    public void setLangue(String langue);

}
