package ch.globaz.pyxis.domaine;

import java.util.HashMap;
import java.util.Map;

public class PaysList {
    private Map<String, Pays> mapPaysByid = new HashMap<String, Pays>();

    public Pays resolveById(String idPays) {
        Pays pays = new Pays();
        if (mapPaysByid.containsKey(idPays)) {
            pays = mapPaysByid.get(idPays);
        }
        return pays;
    }

    public void add(Pays pays) {
        mapPaysByid.put(String.valueOf(pays.getId()), pays);
    }
}
