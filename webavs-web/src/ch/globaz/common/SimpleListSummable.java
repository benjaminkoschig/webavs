package ch.globaz.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple liste qui permet de sommer
 * 
 * @author dma
 * 
 */
public class SimpleListSummable<T extends Number> {
    private List<T> elements = null;

    public SimpleListSummable() {
        this.elements = new ArrayList<T>();
    }

    public SimpleListSummable(List<T> elements) {
        this.elements = elements;
    }

    public void add(T element) {
        elements.add(element);
    }

    public int size() {
        return this.elements.size();
    }

    public T get(int index) {
        return this.get(index);
    }

    public BigDecimal sum() {
        BigDecimal total = BigDecimal.ZERO;
        for (T element : elements) {
            total = total.add(BigDecimal.valueOf(element.doubleValue()));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public List<T> elements() {
        return Collections.unmodifiableList(elements);
    }

}
