package ch.globaz.orion.ws.allocationfamiliale;

import java.util.List;
import ch.globaz.orion.ws.AbstractResultSearch;

public class ALDossierResultSearch extends AbstractResultSearch {
    private List<ALDossier> listAlDossiers;

    public ALDossierResultSearch() {
        throw new UnsupportedOperationException();
    }

    public ALDossierResultSearch(List<ALDossier> listAlDossiers, long nbMatchingRows, long nbAllRows) {
        super(nbMatchingRows, nbAllRows);
        this.listAlDossiers = listAlDossiers;
    }

    public List<ALDossier> getListAlDossiers() {
        return listAlDossiers;
    }

    public void setListAlDossiers(List<ALDossier> listAlDossiers) {
        this.listAlDossiers = listAlDossiers;
    }
}
