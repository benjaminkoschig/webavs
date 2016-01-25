package globaz.naos.listes.excel.util;

import globaz.op.common.model.element.Element;

/**
 * @author vyj
 * @version 1.0 created the 18 f�vr. 2005
 */
public class AFElementExtractor {

    /**
     * Permet de r�cup�rer le prochain �l�ment selon l'appartion dans le texte (si c'est une feuille, prend la prochaine
     * feuille du m�me parent, sinon, prend le fils)
     * 
     * @param element
     *            L'�l�ment courant
     * @return L'�l�ment suivante
     */
    public static Element nextElement(Element element) {
        Element nextElement = null;
        // On commence par regarder si l'�l�ment courant � des fils
        if (element.getChildNodes().getAllElementsSize() > 0) {
            // Si il a des fils, alors le prochain �l�ment est le premier fils
            nextElement = (Element) element.getElementsIterator().next();
        } else {
            // Alors l'�l�ment courant est une feuille
            // On prend le p�re
            Element fatherElement = (Element) element.getParentTag();
            Element theCurrentElement = element;
            // Tant que l'on a pas trouv� le prochain fils du p�re courant, on
            // continue avec le p�re du p�re courant
            while (nextElement == null && fatherElement != null) {
                int currentPosition = theCurrentElement.nodePosition();
                if (fatherElement.getChildNodes().getAllElementsSize() > currentPosition + 1) {
                    // Si le p�re � un fils en position +1 par rapport au fils
                    // courant, alors on le prend
                    nextElement = (Element) fatherElement.getChildNodes().getElement(currentPosition + 1);
                } else {
                    // Sinon, prend le p�re et effectue le m�me topo
                    theCurrentElement = fatherElement;
                    fatherElement = (Element) theCurrentElement.getParentTag();
                }
            }
        }
        return nextElement;
    }

    /**
     * Permet de r�cup�rer le prochain �l�ment de type la class qui est pass�e en param�tre
     * 
     * @param element
     *            L'�l�ment courant
     * @param wishedElementClass
     *            Sp�cifie le type du prochain �l�ment souhait�
     * @return Le prochain �l�ment trouv� de type sp�cifi�
     */
    public static Element nextElement(Element element, Class wishedElementClass) {
        Element nextElement = nextElement(element);
        // Tant que l'�l�ment suivant n'est pas null et qu'il n'appartient pas �
        // la class d�sir�e, on continue de parcourir l'arbre
        while (nextElement != null && !wishedElementClass.equals(nextElement.getClass())) {
            nextElement = nextElement(nextElement);
        }
        return nextElement;
    }

    /**
     * Permet de trouver le prochain �l�ment qui appartient � l'un des types sp�cifi�s
     * 
     * @param element
     *            L'�l�ment courant
     * @param wishedElementClasses
     *            Les types dans lequels le prochain �l�ment doit correspondre.
     * @return Le prochain �l�ment trouv� en fonction des types sp�cifi�s.
     */
    public static Element nextElement(Element element, Class[] wishedElementClasses) {
        boolean found = false;
        Element nextElement = nextElement(element);
        // Tant que l'�l�ment suivant n'est pas null et qu'il n'appartient pas �
        // l'une des classes d�sir�es, on continue de parcourir l'arbre
        while (nextElement != null && !found) {
            // Regarde si l'�l�ment suivant appartient � une des classes
            // d�sir�es
            for (int i = 0; i < wishedElementClasses.length; i++) {
                if (wishedElementClasses[i].equals(nextElement.getClass())) {
                    found = true;
                    break;
                }
            }
            // Si l'�l�ment courant ne correspond pas � l'une des classes que
            // l'on souhaite, on prend l'�l�ment suvante et on r�effectue
            // l'op�ration
            if (!found) {
                nextElement = nextElement(nextElement);
            }
        }
        return nextElement;
    }

}
