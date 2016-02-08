package ch.globaz.corvus.process.dnra;

import globaz.globall.db.BSessionUtil;
import java.util.Locale;
import ch.globaz.simpleoutputlist.converter.Converter;

public class TypeDifferenceConverter implements Converter<TypeDifference, String> {

    @Override
    public String getValue(TypeDifference difference, Locale locale) {
        return BSessionUtil.getSessionFromThreadContext().getLabel(
                "LIST_CORVUS_DIFFERENCE_TYPE_" + difference.toString());
    }

    @Override
    public Class<TypeDifference> getType() {
        return TypeDifference.class;
    }

}
