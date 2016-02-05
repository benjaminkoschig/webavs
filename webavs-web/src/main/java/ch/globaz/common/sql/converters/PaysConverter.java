package ch.globaz.common.sql.converters;

import java.util.Map;
import ch.globaz.common.sql.ConverterDb;
import ch.globaz.pyxis.domaine.Pays;

public class PaysConverter implements ConverterDb<Pays> {

    private Map<String, Pays> mapPaysByCodeCentrale = null;

    public PaysConverter(Map<String, Pays> mapPaysByCodeCentrale) {
        this.mapPaysByCodeCentrale = mapPaysByCodeCentrale;
    }

    @Override
    public Pays convert(Object value, String fieldName, String alias) {
        Pays pays = new Pays();
        if (value != null) {
            if (mapPaysByCodeCentrale.containsKey(value.toString())) {
                pays = mapPaysByCodeCentrale.get(value.toString());
            }
        }
        return pays;
    }

    @Override
    public Class<Pays> forType() {
        return Pays.class;
    }
}
