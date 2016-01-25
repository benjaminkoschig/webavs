package globaz.naos.listes.excel.util;

import globaz.op.common.model.element.Element;

/**
 * @author vyj
 * @version 1.0 created the 18 févr. 2005
 */
public class AFElementExtractor {

    /**
     * Permet de récupérer le prochain élément selon l'appartion dans le texte (si c'est une feuille, prend la prochaine
     * feuille du même parent, sinon, prend le fils)
     * 
     * @param element
     *            L'élément courant
     * @return L'élément suivante
     */
    public static Element nextElement(Element element) {
        Element nextElement = null;
        // On commence par regarder si l'élément courant à des fils
        if (element.getChildNodes().getAllElementsSize() > 0) {
            // Si il a des fils, alors le prochain élément est le premier fils
            nextElement = (Element) element.getElementsIterator().next();
        } else {
            // Alors l'élément courant est une feuille
            // On prend le père
            Element fatherElement = (Element) element.getParentTag();
            Element theCurrentElement = element;
            // Tant que l'on a pas trouvé le prochain fils du père courant, on
            // continue avec le père du père courant
            while (nextElement == null && fatherElement != null) {
                int currentPosition = theCurrentElement.nodePosition();
                if (fatherElement.getChildNodes().getAllElementsSize() > currentPosition + 1) {
                    // Si le père à un fils en position +1 par rapport au fils
                    // courant, alors on le prend
                    nextElement = (Element) fatherElement.getChildNodes().getElement(currentPosition + 1);
                } else {
                    // Sinon, prend le père et effectue le même topo
                    theCurrentElement = fatherElement;
                    fatherElement = (Element) theCurrentElement.getParentTag();
                }
            }
        }
        return nextElement;
    }

    /**
     * Permet de récupérer le prochain élément de type la class qui est passée en paramètre
     * 
     * @param element
     *            L'élément courant
     * @param wishedElementClass
     *            Spécifie le type du prochain élément souhaité
     * @return Le prochain élément trouvé de type spécifié
     */
    public static Element nextElement(Element element, Class wishedElementClass) {
        Element nextElement = nextElement(element);
        // Tant que l'élément suivant n'est pas null et qu'il n'appartient pas à
        // la class désirée, on continue de parcourir l'arbre
        while (nextElement != null && !wishedElementClass.equals(nextElement.getClass())) {
            nextElement = nextElement(nextElement);
        }
        return nextElement;
    }

    /**
     * Permet de trouver le prochain élément qui appartient à l'un des types spécifiés
     * 
     * @param element
     *            L'élément courant
     * @param wishedElementClasses
     *            Les types dans lequels le prochain élément doit correspondre.
     * @return Le prochain élément trouvé en fonction des types spécifiés.
     */
    public static Element nextElement(Element element, Class[] wishedElementClasses) {
        boolean found = false;
        Element nextElement = nextElement(element);
        // Tant que l'élément suivant n'est pas null et qu'il n'appartient pas à
        // l'une des classes désirées, on continue de parcourir l'arbre
        while (nextElement != null && !found) {
            // Regarde si l'élément suivant appartient à une des classes
            // désirées
            for (int i = 0; i < wishedElementClasses.length; i++) {
                if (wishedElementClasses[i].equals(nextElement.getClass())) {
                    found = true;
                    break;
                }
            }
            // Si l'élément courant ne correspond pas à l'une des classes que
            // l'on souhaite, on prend l'élément suvante et on réeffectue
            // l'opération
            if (!found) {
                nextElement = nextElement(nextElement);
            }
        }
        return nextElement;
    }

}
