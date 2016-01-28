package ch.globaz.pegasus.business.domaine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Objet container de liste destiné à fournir un total
 * Le champ qui contient la valeur à additioné doit être spécifié dans la méthode de l'interface Totalizable
 * 
 * @see ch.globaz.pegasus.business.domaine.Totalisable
 * 
 * @param <T> le type de l'objet devant implémenté l'interface Totalizable
 */
public class ListeTotalisable<T extends Totalisable> {

    private List<T> elements = null;

    public ListeTotalisable() {
        this.elements = new ArrayList<T>();
    }

    public ListeTotalisable(List<T> elements) {
        this.elements = elements;
    }

    public void addElement(T element) {
        elements.add(element);
    }

    public int size() {
        return this.elements.size();
    }

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;

        for (T element : elements) {
            total = total.add(element.getMontant());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public List<T> elements() {
        return Collections.unmodifiableList(elements);

    }

}
