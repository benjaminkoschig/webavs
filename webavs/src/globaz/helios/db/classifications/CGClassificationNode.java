/**
 * 
 */
package globaz.helios.db.classifications;

import java.util.Map;
import java.util.TreeMap;

/**
 * Noeud permettant de stocker un compte et son solde en respectant la classification du compte.
 * 
 * @author sel
 * 
 */
public class CGClassificationNode<T> {
    private Map<String, CGClassificationNode<T>> children = null;
    private CGClassificationNode<T> parent = null;
    private T value = null;
    /**
     * Exemple : soldes exercice précédent
     */
    private T valueToCompare = null;

    /**
	 * 
	 */
    public CGClassificationNode() {
        this.children = new TreeMap<String, CGClassificationNode<T>>();
    }

    /**
     * @param value
     */
    public CGClassificationNode(T value) {
        this(value, null);
    }

    /**
     * @param value
     * @param valueToCompare
     */
    public CGClassificationNode(T value, T valueToCompare) {
        this();
        this.value = value;
        this.valueToCompare = valueToCompare;
    }

    /**
     * Ajout d'un enfant.
     */
    public void addChild(String keyTri, CGClassificationNode<T> child) {
        child.setParent(this);
        this.children.put(keyTri, child);
    }

    /**
     * @return the enfants
     */
    public Map<String, CGClassificationNode<T>> getChildren() {
        return this.children;
    }

    /**
     * @return the parent
     */
    public CGClassificationNode<T> getParent() {
        return this.parent;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * @return the valueToCompare
     */
    public T getValueToCompare() {
        return this.valueToCompare;
    }

    /**
     * @return
     */
    public Boolean hasChildren() {
        return (this.getChildren() != null) && !this.getChildren().isEmpty();
    }

    /**
     * indique si le noeud est une feuille (plus d'enfants).
     */
    public boolean isLeaf() {
        return (this.children.size() == 0);
    }

    /**
     * @return true if parent == null
     */
    public Boolean isRoot() {
        return this.parent == null;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(CGClassificationNode<T> parent) {
        this.parent = parent;
    }

    /**
     * @param valueToCompare
     *            the valueToCompare to set
     */
    public void setValueToCompare(T valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
