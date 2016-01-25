package ch.globaz.pegasus.businessimpl.services.models.lot;

import java.util.HashMap;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;

public class GroupPcaByMonth {
    HashMap<String, PcaDecompte> mapPcaConjointByMonth = new HashMap<String, PcaDecompte>();
    HashMap<String, PcaDecompte> mapRequerantByMonth = new HashMap<String, PcaDecompte>();

    public HashMap<String, PcaDecompte> getMapPcaConjointByMonth() {
        return mapPcaConjointByMonth;
    }

    public HashMap<String, PcaDecompte> getMapRequerantByMonth() {
        return mapRequerantByMonth;
    }

    public void setMapPcaConjointByMonth(HashMap<String, PcaDecompte> mapPcaConjointByMonth) {
        this.mapPcaConjointByMonth = mapPcaConjointByMonth;
    }

    public void setMapRequerantByMonth(HashMap<String, PcaDecompte> mapRequerantByMonth) {
        this.mapRequerantByMonth = mapRequerantByMonth;
    }
}
