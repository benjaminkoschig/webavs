package ch.globaz.orion.ws.comptabilite;

import java.util.List;
import ch.globaz.orion.ws.AbstractResultSearch;

public class FactureResultSearch extends AbstractResultSearch {
    private List<Facture> listFactures;

    public FactureResultSearch() {
        throw new UnsupportedOperationException();
    }

    public FactureResultSearch(List<Facture> listFactures, long nbMatchingRows, long nbAllRows) {
        super(nbMatchingRows, nbAllRows);
        this.listFactures = listFactures;
    }

    public List<Facture> getListFactures() {
        return listFactures;
    }

    public void setListFactures(List<Facture> listFactures) {
        this.listFactures = listFactures;
    }
}
