package globaz.corvus.utils;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

import java.text.DecimalFormat;
import java.util.Objects;

public class REPrestationUtils {

    static final String GENRE_500 = "50.0";
    static final String GENRE_700 = "70.0";
    static final String PATTERN = "#.# %";
    static final String REPLACE_QUOTITE = "{quotite}";
    static final String TYPE_CODE_GENRE  = "REGENRPRST";

    // Class utilitaire
    private REPrestationUtils() {}
    public static String getLibelleGenrePrestation(String codePrestation, String quotite, String codeIsoLangue, BSession session) throws Exception {
        String libelle = RENumberFormatter.codeSystemToLibelle(
                session.getSystemCode(TYPE_CODE_GENRE, codePrestation),
                codeIsoLangue, session);
        if ((Objects.equals(GENRE_500, codePrestation) || Objects.equals(GENRE_700, codePrestation))
                && !JadeStringUtil.isEmpty(quotite)) {
            DecimalFormat dft = new DecimalFormat(PATTERN);
            String quotiteLibelle = dft.format(Double.valueOf(quotite));
            libelle = libelle.replace(REPLACE_QUOTITE, quotiteLibelle);
        }
        return libelle;
    }
}
