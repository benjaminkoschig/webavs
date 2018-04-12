package ch.globaz.orion.ws.cotisation;

import java.util.List;
import ch.globaz.orion.ws.AbstractResultSearch;

public class SalarieResultSearch extends AbstractResultSearch {
    private List<Salarie> listSalaries;

    public SalarieResultSearch() {
        throw new UnsupportedOperationException();
    }

    public SalarieResultSearch(List<Salarie> listSalaries, long nbMatchingRows, long nbAllRows) {
        super(nbMatchingRows, nbAllRows);
        this.listSalaries = listSalaries;
    }

    public List<Salarie> getListSalaries() {
        return listSalaries;
    }

    public void setListSalaries(List<Salarie> listSalaries) {
        this.listSalaries = listSalaries;
    }
}
