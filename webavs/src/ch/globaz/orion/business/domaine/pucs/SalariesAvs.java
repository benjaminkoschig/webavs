package ch.globaz.orion.business.domaine.pucs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SalariesAvs implements Iterable<SalaryAvs> {
    private List<SalaryAvs> list = new ArrayList<SalaryAvs>();

    public SalariesAvs() {

    }

    public SalariesAvs(List<SalaryAvs> list) {
        super();
        this.list = list;
        sortByPeriode();
    }

    private void sortByPeriode() {
        java.util.Collections.sort(list, new Comparator<SalaryAvs>() {
            @Override
            public int compare(SalaryAvs o1, SalaryAvs o2) {
                return o1.getPeriode().compareTo(o2.getPeriode());
            }
        });
    }

    @Override
    public Iterator<SalaryAvs> iterator() {
        return list.iterator();
    }

    public SalaryAvs get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
