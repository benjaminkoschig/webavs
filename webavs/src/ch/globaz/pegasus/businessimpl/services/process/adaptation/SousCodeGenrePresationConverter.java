package ch.globaz.pegasus.businessimpl.services.process.adaptation;

import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import ch.globaz.simpleoutputlist.converter.Converter;

public class SousCodeGenrePresationConverter implements Converter<String, String> {

    @Override
    public String getValue(String value, Locale local) {

        Map<String, String> map = new HashMap<String, String>();

        map.put(REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SPAS, "AI_SPAS");
        map.put(REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SASH, "AI_SASH");
        map.put(REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SPAS_HORS_CANTON, "AI_SPAS_HORS_CANTON");
        map.put(REGenresPrestations.PC_SOUS_TYPE_AI_HOME_SASH_HORS_CANTON, "AI_SASH_HORS_CANTON");
        map.put(REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SPAS, "AVS_SPAS");
        map.put(REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SASH, "AVS_SASH");
        map.put(REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SPAS_HORS_CANTON, "AVS_SPAS_HORS_CANTON");
        map.put(REGenresPrestations.PC_SOUS_TYPE_AVS_HOME_SASH_HORS_CANTON, "AVS_SASH_HORS_CANTON");

        return map.get(value);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
