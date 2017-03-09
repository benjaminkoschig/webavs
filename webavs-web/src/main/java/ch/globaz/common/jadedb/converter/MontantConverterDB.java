/*
 * Globaz SA.
 */
package ch.globaz.common.jadedb.converter;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Montant;

public class MontantConverterDB implements ConverterDB<Montant, BigDecimal> {

    @Override
    public Montant fromDB(BigDecimal valueDB) {
        return new Montant(valueDB);
    }

    @Override
    public BigDecimal toDB(Montant valueObject) {
        return valueObject.getBigDecimalValue();
    }

}
