package ch.globaz.common.sql.converters;

import java.math.BigDecimal;
import ch.globaz.common.sql.ConverterDb;
import ch.globaz.pyxis.domaine.EtatCivil;

public class EtatCivilConverter implements ConverterDb<EtatCivil> {

    @Override
    public EtatCivil convert(Object codeSysteme, String fieldName, String alias) {
        String code = codeSysteme.toString();
        if (codeSysteme instanceof Integer) {
            code = ((Integer) codeSysteme).toString();
        } else if (codeSysteme instanceof BigDecimal) {
            code = ((BigDecimal) codeSysteme).toString();
        }

        return EtatCivil.parse(code);
    }

    @Override
    public Class<EtatCivil> forType() {
        return EtatCivil.class;
    }

}
