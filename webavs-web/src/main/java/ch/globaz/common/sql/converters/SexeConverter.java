package ch.globaz.common.sql.converters;

import java.math.BigDecimal;
import ch.globaz.common.sql.ConverterDb;
import ch.globaz.pyxis.domaine.Sexe;

public class SexeConverter implements ConverterDb<Sexe> {

    @Override
    public Sexe convert(Object sexe, String fieldName, String alias) {
        if (sexe instanceof BigDecimal) {
            return Sexe.valueOf(((BigDecimal) sexe).intValue());
        } else if (sexe instanceof Integer) {
            return Sexe.valueOf((Integer) sexe);
        }
        return null;
    }

    @Override
    public Class<Sexe> forType() {
        return Sexe.class;
    }

}
