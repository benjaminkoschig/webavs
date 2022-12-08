package globaz.corvus.utils;

import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.decisions.REBeneficiaireInfoVO;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

public class REPrestationUtils {

    static final String GENRE_500 = "50.0";
    static final String GENRE_700 = "70.0";
    static final String PATTERN = "#.# %";
    static final String REPLACE_QUOTITE = "{quotite}";
    static final String TYPE_CODE_GENRE  = "REGENRPRST";

    // Class utilitaire
    private REPrestationUtils() {}

    public static String getLibelleGenrePrestation(String genrePrestation, String fraction, String quotite, String codeIsoLangue, BSession session) throws Exception {
        String codePrestation = getCodePrestationCS(genrePrestation, fraction, quotite);
        String libelle = RENumberFormatter.codeSystemToLibelle(
                session.getSystemCode(TYPE_CODE_GENRE, codePrestation),
                codeIsoLangue.toUpperCase(), session);
        if ((Objects.equals(GENRE_500, codePrestation) || Objects.equals(GENRE_700, codePrestation))
                && !JadeStringUtil.isEmpty(quotite)) {
            DecimalFormat dft = new DecimalFormat(PATTERN);
            String quotiteLibelle = dft.format(Double.valueOf(quotite));
            libelle = libelle.replace(REPLACE_QUOTITE, quotiteLibelle);
        }
        return libelle;
    }

    private static String getCodePrestationCS(String genrePrestation, String fraction, String quotite) {
        String codePrestationCS = genrePrestation;
        if (Arrays.stream(REGenresPrestations.GENRE_PRESTATIONS_AI).anyMatch(eachGenrePrestation -> eachGenrePrestation.equals(genrePrestation))) {
            if (!JadeStringUtil.isEmpty(fraction)) {
                codePrestationCS += "." + fraction;
            } else if (!JadeStringUtil.isEmpty(quotite)) {
                if (Objects.equals(REGenresPrestations.GENRE_50,genrePrestation) || Objects.equals(REGenresPrestations.GENRE_70, genrePrestation)) {
                    if (Float.valueOf(quotite) >= 0.70) {
                        codePrestationCS += ".1";
                    } else {
                        codePrestationCS += ".0";
                    }
                } else {
                    codePrestationCS += ".1";
                }
            } else {
                codePrestationCS += ".0";
            }
        } else {
            codePrestationCS += ".0";
        }
        return codePrestationCS;
    }
}
