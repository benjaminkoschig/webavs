package ch.globaz.orion.business.domaine.pucs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SalariesCaf implements Iterable<SalaryCaf> {
    List<SalaryCaf> list = new ArrayList<SalaryCaf>();

    public SalariesCaf() {
    }

    public SalariesCaf(List<SalaryCaf> salaries) {
        super();
        list = salaries;
        sortByPeriode();
    }

    @Override
    public Iterator<SalaryCaf> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }

    public SalaryCaf get(int index) {
        return list.get(index);
    }

    private void sortByPeriode() {
        java.util.Collections.sort(list, new Comparator<SalaryCaf>() {
            @Override
            public int compare(SalaryCaf o1, SalaryCaf o2) {
                return o1.getPeriode().compareTo(o2.getPeriode());
            }
        });
    }
}
