package ch.globaz.orion.ws.allocationfamiliale;

import java.util.List;
import ch.globaz.orion.ws.AbstractResultSearch;

public class ALDossierAndAdressesResultSearch extends AbstractResultSearch {
    private List<ALDossierAndAdresses> listAlDossiersAndAdresses;

    public ALDossierAndAdressesResultSearch() {
        throw new UnsupportedOperationException();
    }

    public ALDossierAndAdressesResultSearch(List<ALDossierAndAdresses> listAlDossiersAndAdresses, long nbMatchingRows,
            long nbAllRows) {
        super(nbMatchingRows, nbAllRows);
        this.listAlDossiersAndAdresses = listAlDossiersAndAdresses;
    }

    public List<ALDossierAndAdresses> getListAlDossiersAndAdresses() {
        return listAlDossiersAndAdresses;
    }

    public void setListAlDossiersAndAdresses(List<ALDossierAndAdresses> listAlDossiersAndAdresses) {
        this.listAlDossiersAndAdresses = listAlDossiersAndAdresses;
    }
}
